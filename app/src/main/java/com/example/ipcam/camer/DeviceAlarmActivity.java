package com.example.ipcam.camer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ipcam.camer.Listener.PictureListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.AlarmMsg;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.FileHelper;
import com.example.ipcam.camer.util.SharedPrefer;
import com.example.ipcam.camer.util.Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class DeviceAlarmActivity extends Activity implements OnClickListener,
        PictureListener {
    private TextView ipcName, alarmMsg, timemsg;
    private LinearLayout check, dimss;
    private long userid;
    private String msg_str;
    public static boolean isvis = false;
    private SoundPool sound;
    private int music;
    private Vibrator vibrator;
    private Bitmap bitmap;
    private ImageView imageView;
    private String imgstr;
    private IpcDevice device;
    private int infotype;
    private String time;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (bitmap != null)
                    imageView.setImageBitmap(bitmap);
            } else if (msg.what == 2) {
                String str2 = (String) msg.obj;
                AlarmMsg alarmmsg = new AlarmMsg(0, msg_str, time,
                        device.getDeviceid(), str2, infotype);
                DeviceManager.getInstence(DeviceAlarmActivity.this).SaveMsg(alarmmsg);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        BridgeService.setPictureListener(this);
        isvis = true;
        setContentView(R.layout.activity_alarm);
        initView();
        init();

    }

    public void init() {
        mediaPlayer = MediaPlayer.create(this, R.raw.sms_received3);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        HashMap<String, Object> map = SharedPrefer.GetAppSetingData(this);
        int time = (int) map.get(SharedPrefer.ALARM_T);         //报警时间
        boolean zd = (boolean) map.get(SharedPrefer.ALARM_Z);  //报警震动
        boolean ls = (boolean) map.get(SharedPrefer.ALARM_LS);  //报警声音
        if (ls && zd) {
            mediaPlayer.start();
            vibrator.vibrate(pattern, 2);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    mediaPlayer.stop();
                    vibrator.cancel();
                }
            };
            timer.schedule(task, time * 1000);
        }


    }

    private void initView() {
        Intent intent = getIntent();
        userid = intent.getLongExtra("userid", -1);
        int type = intent.getIntExtra("nType", -1);
        infotype = intent.getIntExtra("type", -1);
        String pic = intent.getStringExtra("pic");
        msg_str = Util.GetAlarmMsg(type);
        DeviceSDK.getDeviceParam(userid, 0x270E);
        device = DeviceManager.getInstence(this).QueryDevice(userid);

        ipcName = (TextView) findViewById(R.id.alarm_ipcname);
        timemsg = (TextView) findViewById(R.id.alarm_time);
        alarmMsg = (TextView) findViewById(R.id.alarm_msg);
        check = (LinearLayout) findViewById(R.id.alarm_check);
        dimss = (LinearLayout) findViewById(R.id.alarm_dimss);
        imageView = (ImageView) findViewById(R.id.alarm_img);
        check.setOnClickListener(this);
        dimss.setOnClickListener(this);
        if (type == -1 && device == null) {
            alarmMsg.setText(msg_str);
            ipcName.setText("未知设备");

        } else {
            if (!"".equals(device.getName())) {
                alarmMsg.setText(msg_str);
                ipcName.setText(device.getName());
            }
        }
        time = Util.getNowTime();
        timemsg.setText(time);
        DeviceSDK.getDeviceParam(userid, 0x270E);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isvis = false;
        vibrator.cancel();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == check.getId()) {
            Intent intent = new Intent(this, PlayDeviceActivity.class);
            intent.putExtra("userid", userid);
            startActivity(intent);
            finish();
        } else if (id == dimss.getId()) {
            finish();
            // moveTaskToBack(true);
        }
    }

    public void save(Bitmap bitmap) {
        String str1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + ".jpg";
        File localFile = new File(FileHelper.IMAGE_PATH);
        if (!localFile.exists())
            localFile.mkdirs();
        String str2 = FileHelper.IMAGE_PATH + "/" + "_" + str1;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(str2));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.what = 2;
        msg.obj = str2;
        handler.sendMessage(msg);

    }

    @Override
    public void CallBack_RecordPicture(long userid, byte[] buff, int len) {
        try {
            imgstr = new String(buff, "ISO-8859-1");
            bitmap = Util.decodeBitmap(imgstr);
            save(bitmap);
            handler.sendEmptyMessage(1);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
