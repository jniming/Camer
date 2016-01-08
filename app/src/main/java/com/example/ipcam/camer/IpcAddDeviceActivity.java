package com.example.ipcam.camer;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ipcam.camer.Callback.InitCallBack;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Ttoast;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.CustomDialog;
import com.zxing.decoding.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class IpcAddDeviceActivity extends BaseActivity implements
        View.OnClickListener {

    private LinearLayout wifi_search_device, auto_add_device, lan, sound;
    private EditText device_mac, device_name, device_admin, device_psw;
    private List<IpcDevice> searchlist = new ArrayList<IpcDevice>(); // 搜索结果返回的列表
    private List<IpcDevice> querylist; // 查询返回的列表
    private CustomDialog.Builder builder;
    private Dialog alertDialog;
    private IpcDevice ipc;
    private InitCallBack callBack;
    private int msgCode = 2;
    private int searchCode = 100;
    private int SacnCode = 101;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tem = msg.arg1;
            int what = msg.what;
            if (what == msgCode) {
                IpcDevice ipcDevice = (IpcDevice) msg.obj;
                if (ipcDevice != null) {
                    device_mac.setText(ipcDevice.getDeviceid() + "");
                    device_name.setText(ipcDevice.getName());
                    device_psw.setHint("默认为空");
                }
            } else if (what == 5) {
                String uuid = (String) msg.obj;
                device_mac.setText(uuid);
                device_name.setText("IPCAM");
                device_psw.setHint("默认为空");

            }
        }

    };


    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ipc);
        setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
        setTitle("设备添加");
        DeviceSDK.initSearchDevice();
        wifi_search_device = (LinearLayout) findViewById(R.id.wifi_search_device);
        auto_add_device = (LinearLayout) findViewById(R.id.auto_add_device);
        lan = (LinearLayout) findViewById(R.id.ipc_add_lan_lt);
        sound = (LinearLayout) findViewById(R.id.ipc_add_sound_lt);
        device_mac = (EditText) findViewById(R.id.device_mac);
        device_name = (EditText) findViewById(R.id.device_name);
        device_admin = (EditText) findViewById(R.id.device_admin);
        device_psw = (EditText) findViewById(R.id.device_psw);
        wifi_search_device.setOnClickListener(this);
        auto_add_device.setOnClickListener(this);
        sound.setOnClickListener(this);
        lan.setOnClickListener(this);
        initView();
    }


    public void initView() {
        querylist = DeviceManager.getInstence(this).GetIPCListDevice();

    }

    @SuppressLint("NewApi")
    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.LIEFT) {
            finish();
        } else { // 完成
            String dname = device_name.getText().toString().trim();
            String did = device_mac.getText().toString().trim();
            String dadmin = device_admin.getText().toString().trim();
            String dpsw = device_psw.getText().toString().trim();
            if (dname.isEmpty() || did.isEmpty() || dadmin.isEmpty()) {
                Toast.makeText(this, "设备信息输入不全,重新输入", Toast.LENGTH_SHORT)
                        .show();
                return;
            } else {
                // dialog.show();
                // dialog.setText("添加中..");
                ipc = new IpcDevice();
                ipc.setName(dname);
                ipc.setDeviceid(did);
                ipc.setAdmin_name(dadmin);
                ipc.setPsw(dpsw);

                long temp = DeviceSDK.createDevice(ipc.getAdmin_name(), "",
                        ipc.getIp(), ipc.getPort(), ipc.getDeviceid(), 1);
                if (temp > 0) {
                    long flg1 = DeviceSDK.openDevice(temp);
                    if (flg1 > 0) {
                        ipc.setUserid(temp);
                        boolean flg = false;
                        if (querylist != null) {
                            for (IpcDevice d : querylist) {
                                if (ipc.getDeviceid().equals(d.getDeviceid())) {
                                    flg = true;
                                    Ttoast.show(IpcAddDeviceActivity.this, getResources().getString(R.string.ipc_ishave));
                                    return;
                                }
                            }
                        } else {
                            flg = false;
                        }
                        if (!flg) {
                            DeviceManager
                                    .getInstence(IpcAddDeviceActivity.this)
                                    .SaveIpcDevice(ipc);
                            DeviceSDK.getDeviceParam(temp, Util.GET_ALARM);  //获取报警是否开启
                            finish();
                        }

                    }

                }

            }

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == wifi_search_device.getId()) {
            Intent Intent = new Intent(this, DeviceWifiSettinActivityOne.class);
            startActivityForResult(Intent, searchCode);
        } else if (id == auto_add_device.getId()) {
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, SacnCode);

        } else if (id == lan.getId()) {
            Intent Intent = new Intent(this, DeviceSeachListActivity.class);
            startActivityForResult(Intent, searchCode);
        } else if (id == sound.getId()) {
            Intent intent = new Intent(this, SettingIpcSoundActivity.class);
            startActivityForResult(intent, 2);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            IpcDevice device = (IpcDevice) data.getSerializableExtra("device");
            Message msg = handler.obtainMessage();
            msg.obj = device;
            msg.what = msgCode;
            handler.sendMessage(msg);
        } else if (resultCode == 101) {  //扫描返回
            // UUID
            String code = data.getStringExtra(CaptureActivity.CODE_RESULT);
            Message msg = handler.obtainMessage();
            msg.obj = code;
            msg.what = 5;
            handler.sendMessage(msg);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DeviceSDK.unInitSearchDevice();

    }
}
