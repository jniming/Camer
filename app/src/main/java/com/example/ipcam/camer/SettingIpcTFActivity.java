package com.example.ipcam.camer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.Listener.SdkListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.entity.SdCardModel;
import com.example.ipcam.camer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;
import hsl.p2pipcam.nativecaller.NativeCaller;


public class SettingIpcTFActivity extends BaseActivity implements View.OnClickListener, SdkListener {

    private IpcDevice device;
    private boolean lxfg = false;
    private boolean lxlisten = false;
    private boolean lxds = false;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 0) {
                tfMText.setText(SettingIpcTFActivity.this.sdCardModel.getSdcard_totalsize() + "M");
                if (SettingIpcTFActivity.this.sdCardModel.getRecord_cover() == 1) {
                    audioFg.setImageResource(R.drawable.checkbox_on);
                    lxfg = true;
                }
                if (SettingIpcTFActivity.this.sdCardModel.getRecord_audio() == 1) {
                    listennerAudio.setImageResource(R.drawable.checkbox_on);
                    lxlisten = true;
                }
                if (SettingIpcTFActivity.this.sdCardModel.getRecord_time() == 1) {
                    audioDs.setImageResource(R.drawable.checkbox_on);
                    lxds = true;
                }
                if (sdCardModel.getSdcard_status() > 0) {
                    sdHaveText.setText("已插SD卡");
                } else {
                    sdHaveText.setText("未检测到SD卡");

                }


            }
        }
    };


    private TextView tfMText;
    private TextView sdHaveText;
    private ImageView audioFg;
    private ImageView listennerAudio;
    private ImageView audioDs;
    private Button clearSdBtn;
    private int time15;
    private int time23;
    private int time7;
    private SdCardModel sdCardModel = new SdCardModel();

    private void assignViews() {
        tfMText = (TextView) findViewById(R.id.tf_m_text);
        sdHaveText = (TextView) findViewById(R.id.sd_have_text);
        audioFg = (ImageView) findViewById(R.id.audio_fg);
        listennerAudio = (ImageView) findViewById(R.id.listenner_audio);
        audioDs = (ImageView) findViewById(R.id.audio_ds);
        clearSdBtn = (Button) findViewById(R.id.clear_sd_btn);

        listennerAudio.setOnClickListener(this);
        audioFg.setOnClickListener(this);
        audioDs.setOnClickListener(this);
        clearSdBtn.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingtf);
        setNaView(R.drawable.back, "", 0, "", R.drawable.left_img, "", 0, "");
        setTitle("TF设置");
        initData();
        assignViews();
        BridgeService.setSdkListener(this);
        DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_PARAM_RECORDSCH);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            JSONObject localJSONObject = new JSONObject();
            try {
                localJSONObject.put("record_cover", this.sdCardModel.getRecord_cover());
                localJSONObject.put("record_audio", this.sdCardModel.getRecord_audio());
                localJSONObject.put("time_schedule_enable", this.sdCardModel.getTime_schedule_enable());
                localJSONObject.put("schedule_sun_0", this.time7);
                localJSONObject.put("schedule_sun_1", this.time15);
                localJSONObject.put("schedule_sun_2", this.time23);
                localJSONObject.put("schedule_mon_0", this.time7);
                localJSONObject.put("schedule_mon_1", this.time15);
                localJSONObject.put("schedule_mon_2", this.time23);
                localJSONObject.put("schedule_tue_0", this.time7);
                localJSONObject.put("schedule_tue_1", this.time15);
                localJSONObject.put("schedule_tue_2", this.time23);
                localJSONObject.put("schedule_wed_0", this.time7);
                localJSONObject.put("schedule_wed_1", this.time15);
                localJSONObject.put("schedule_wed_2", this.time23);
                localJSONObject.put("schedule_thu_0", this.time7);
                localJSONObject.put("schedule_thu_1", this.time15);
                localJSONObject.put("schedule_thu_2", this.time23);
                localJSONObject.put("schedule_fri_0", this.time7);
                localJSONObject.put("schedule_fri_1", this.time15);
                localJSONObject.put("schedule_fri_2", this.time23);
                localJSONObject.put("schedule_sat_0", this.time7);
                localJSONObject.put("schedule_sat_1", this.time15);
                localJSONObject.put("schedule_sat_2", this.time23);
                int id = DeviceSDK.setDeviceParam(device.getUserid(), Util.SET_PARAM_RECORDSCH, localJSONObject.toString());
                if (id > 0) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "设置失败,请重试", Toast.LENGTH_SHORT).show();
                }
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            back();
        }

    }


    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
    }


    @Override
    public void onClick(View v) {
        if (clearSdBtn.getId() == v.getId()) {
            String tf_text = tfMText.getText().toString().trim();
            String sdk_info = tf_text.substring(0, tf_text.length() - 1);
            int tf = Integer.valueOf(sdk_info);
            if (tf == 0) {
                Toast.makeText(this, "未插入sd卡或网路不通获取不到sd属性", Toast.LENGTH_SHORT).show();
                return;
            }
            NativeCaller.SetParam(device.getUserid(), Util.SET_PARAM_SDFORMAT, null);
        } else if (audioFg.getId() == v.getId()) {
            if (lxfg) {
                this.sdCardModel.setRecord_cover(0);
                audioFg.setImageResource(R.drawable.checkbox_off);
            } else {
                this.sdCardModel.setRecord_cover(1);
                audioFg.setImageResource(R.drawable.checkbox_on);
            }

        } else if (audioDs.getId() == v.getId()) {
            if (lxds) {
                this.sdCardModel.setRecord_time(0);
                audioDs.setImageResource(R.drawable.checkbox_off);
            } else {
                this.sdCardModel.setRecord_time(1);
                audioDs.setImageResource(R.drawable.checkbox_on);
            }

        } else if (listennerAudio.getId() == v.getId()) {
            if (lxlisten) {
                this.sdCardModel.setRecord_audio(0);
                listennerAudio.setImageResource(R.drawable.checkbox_off);
            } else {
                this.sdCardModel.setRecord_audio(1);
                listennerAudio.setImageResource(R.drawable.checkbox_on);
            }

        }
    }

    @Override
    public void callBack_getParam(long UserID, long nType, String param) {
        try {
            JSONObject localJSONObject = new JSONObject(param);
            this.sdCardModel.setRecord_cover(localJSONObject.getInt("record_cover"));
            this.sdCardModel.setRecord_audio(localJSONObject.getInt("record_audio"));
            this.sdCardModel.setTime_schedule_enable(localJSONObject.getInt("time_schedule_enable"));
            this.sdCardModel.setSdcard_status(localJSONObject.getInt("sdcard_status"));
            this.sdCardModel.setSdcard_totalsize(localJSONObject.getInt("sdcard_totalsize"));
            this.sdCardModel.setRecord_time(localJSONObject.getInt("record_time"));
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callBack_setParam(long UserID, long nType, int nResult) {

    }
}
