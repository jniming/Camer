package com.example.ipcam.camer;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class DeviceWifiSettinActivityOne extends BaseActivity {

    private EditText deviceWifiEdt;
    private TextView deviceWifiSearchBtn;
    private EditText wifiLanEdt;
    private TextView wifiSearchBtn;
    private EditText wifiPwdEdt;
    private TextView connetctWifiHelpText;
    private TextView nextBtn;

    private void assignViews() {
        deviceWifiEdt = (EditText) findViewById(R.id.device_wifi_edt);
        deviceWifiSearchBtn = (TextView) findViewById(R.id.device_wifi_search_btn);
        wifiLanEdt = (EditText) findViewById(R.id.wifi_lan_edt);
        wifiSearchBtn = (TextView) findViewById(R.id.wifi_search_btn);
        wifiPwdEdt = (EditText) findViewById(R.id.wifi_pwd_edt);
        connetctWifiHelpText = (TextView) findViewById(R.id.connetct_wifi_ht);
        nextBtn = (TextView) findViewById(R.id.next_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_add_wifi_setting_one);
        setTitle("热点配置");
        setNaView(R.drawable.back, "", 0, "", 0, "", 0, "");
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        back();
    }


}
