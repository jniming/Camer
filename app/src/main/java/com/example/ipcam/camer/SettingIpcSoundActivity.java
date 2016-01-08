package com.example.ipcam.camer;

import android.os.Bundle;
import android.view.View;


public class SettingIpcSoundActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinglan);
        setNaView(R.drawable.back, "", 0, "", 0, "", 0, "");
        setTitle("内网配置");
        initData();
        initView();

    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        back();
    }

    private void initView() {
    }

    private void initData() {
    }


}
