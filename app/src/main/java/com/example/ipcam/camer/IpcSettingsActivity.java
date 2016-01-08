package com.example.ipcam.camer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.ipcam.camer.entity.IpcDevice;

import java.util.HashMap;
import java.util.List;


public class IpcSettingsActivity extends BaseActivity implements
        OnClickListener {
    private List<HashMap<String, String>> list = null;
    private IpcDevice device;
    private LinearLayout name_lt, ap_lt, eamil_lt, fz_layout, tf_lt, ftp_lt, alarm_lt, wifi_lt, time_lt, user_lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        setNaView(R.drawable.back, "", 0, "", 0, "", 0, "");
        setTitle("设置页面");
        initData();
        initView();

    }

    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
    }

    private void initView() {
        name_lt = (LinearLayout) findViewById(R.id.name_lt);
        ap_lt = (LinearLayout) findViewById(R.id.ap_lt);
        eamil_lt = (LinearLayout) findViewById(R.id.eamil_lt);
        tf_lt = (LinearLayout) findViewById(R.id.tf_lt);
        ftp_lt = (LinearLayout) findViewById(R.id.ftp_lt);
        alarm_lt = (LinearLayout) findViewById(R.id.alarm_lt);
        wifi_lt = (LinearLayout) findViewById(R.id.wifi_lt);
        time_lt = (LinearLayout) findViewById(R.id.time_lt);
        user_lt = (LinearLayout) findViewById(R.id.user_lt);
        fz_layout = (LinearLayout) findViewById(R.id.fz_layout);


        user_lt.setOnClickListener(this);
        ap_lt.setOnClickListener(this);
        eamil_lt.setOnClickListener(this);
        tf_lt.setOnClickListener(this);
        ftp_lt.setOnClickListener(this);
        alarm_lt.setOnClickListener(this);
        wifi_lt.setOnClickListener(this);
        time_lt.setOnClickListener(this);
        user_lt.setOnClickListener(this);
        name_lt.setOnClickListener(this);
        fz_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        intent.putExtra("device", device);
        if (id == name_lt.getId()) {
            intent.setClass(this, SettingIpcNameActivity.class);
        } else if (id == ap_lt.getId()) {
            intent.setClass(this, SettingIpcAPActivity.class);
        } else if (id == eamil_lt.getId()) {
            intent.setClass(this, SettingIpcEmailActivity.class);
        } else if (id == tf_lt.getId()) {
            intent.setClass(this, SettingIpcTFActivity.class);
        } else if (id == ftp_lt.getId()) {
            intent.setClass(this, SettingIpcFTPActivity.class);
        } else if (id == alarm_lt.getId()) {
            intent.setClass(this, SettingIpcAlarmActivity.class);
        } else if (id == wifi_lt.getId()) {
            intent.setClass(this, SettingIpcWiFiActivity.class);
        } else if (id == time_lt.getId()) {
            intent.setClass(this, SettingIpcTimeActivity.class);
        } else if (id == user_lt.getId()) {
            intent.setClass(this, SettingIpcUserActivity.class);
        } else if (fz_layout.getId() == v.getId()) {
            intent.setClass(this, SettingOverturnActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        finish();
    }

}
