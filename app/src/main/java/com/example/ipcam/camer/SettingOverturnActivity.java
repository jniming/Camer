package com.example.ipcam.camer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.ipcam.camer.entity.IpcDevice;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingOverturnActivity extends BaseActivity implements
        OnClickListener {
    private Button fz1;
    private Button fz2;
    private Button fz3;
    private IpcDevice device;

    private void assignViews() {
        fz1 = (Button) findViewById(R.id.fz_1);
        fz2 = (Button) findViewById(R.id.fz_2);
        fz3 = (Button) findViewById(R.id.fz_3);

        fz1.setOnClickListener(this);
        fz2.setOnClickListener(this);
        fz3.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingoverturn);
        setNaView(R.drawable.back, "", 0, "", 0, "", 0, "");
        setTitle("图像方向");
        initData();
        assignViews();
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        back();
    }

    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
    }

    @Override
    public void onClick(View v) {
        JSONObject obj1 = new JSONObject();
        if (v.getId() == fz1.getId()) {
            try {
                obj1.put("param", 5);
                obj1.put("value", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (fz2.getId() == v.getId()) {
            try {
                obj1.put("param", 5);
                obj1.put("value", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (fz3.getId() == v.getId()) {
            try {
                obj1.put("param", 5);
                obj1.put("value", 3);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int id = DeviceSDK.setDeviceParam(device.getUserid(), 0x2026, obj1.toString());
        if (id > 0) {
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();

        }
    }
}
