package com.example.ipcam.camer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.ipcam.camer.Fragment.MainFragment;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.db.DeviceDb;

public class WelcomeActivity extends Activity {

    private Handler hanlder = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Intent Intent = new Intent(WelcomeActivity.this, MainFragment.class);
            startActivity(Intent);
            finish();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        Intent intent = new Intent(this, BridgeService.class);
        startService(intent); // 启动摄像头服务
        init();
    }

    /**
     * 初始化一些数据
     */
    public void init() {
        DeviceDb db = new DeviceDb(this);
        hanlder.sendEmptyMessageDelayed(0, 1500);
    }


}
