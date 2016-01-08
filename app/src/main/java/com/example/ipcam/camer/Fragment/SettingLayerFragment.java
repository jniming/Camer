package com.example.ipcam.camer.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.R;
import com.example.ipcam.camer.util.SharedPrefer;
import com.shizhefei.fragment.LazyFragment;

import java.util.HashMap;


public class SettingLayerFragment extends LazyFragment implements OnClickListener {


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private TextView appVersions;
    private CheckBox alarmListennerAppst;
    private CheckBox alarmZhenAppst;
    private Button update, email_fk, alarm_time;
    HashMap<String, Object> map = null;
    private Dialog dialog;

    private void assignViews() {
        appVersions = (TextView) findViewById(R.id.app_versions);
        update = (Button) findViewById(R.id.update_app);
        email_fk = (Button) findViewById(R.id.email_fk);
        alarm_time = (Button) findViewById(R.id.alarm_time_seting_btn);
        alarmListennerAppst = (CheckBox) findViewById(R.id.alarm_listenner_appst);
        alarmZhenAppst = (CheckBox) findViewById(R.id.alarm_shake_appst);
        alarm_time.setOnClickListener(this);
        email_fk.setOnClickListener(this);
        update.setOnClickListener(this);


        alarmListennerAppst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(SharedPrefer.ALARM_LS, isChecked);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
            }
        });
        alarmZhenAppst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(SharedPrefer.ALARM_Z, isChecked);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
            }
        });

    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.app_setting);
        assignViews();
        initdata();
    }

    public void initdata() {
        map = SharedPrefer.GetAppSetingData(SettingLayerFragment.this.getActivity());
        boolean ls_b = (boolean) map.get(SharedPrefer.ALARM_LS);
        boolean zd_b = (boolean) map.get(SharedPrefer.ALARM_Z);
        int time = (int) map.get(SharedPrefer.ALARM_T);
        alarmListennerAppst.setChecked(ls_b);
        alarmZhenAppst.setChecked(zd_b);
        alarm_time.setText(time + "");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == update.getId()) {
            Toast.makeText(SettingLayerFragment.this.getActivity(), "已是最新版", Toast.LENGTH_SHORT).show();
        } else if (id == alarm_time.getId()) {
            AlertBuilder();
        } else if (id == email_fk.getId()) {
            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse("mailto:qq10000@qq.com"));
            data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
            data.putExtra(Intent.EXTRA_TEXT, "这是内容");
            startActivity(data);
        }
    }

    public void AlertBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingLayerFragment.this.getActivity());
        builder.setTitle("提示");
        View view = LayoutInflater.from(SettingLayerFragment.this.getActivity()).inflate(R.layout.alarm_time_builder, null);

        LinearLayout itemFiveLayout = (LinearLayout) view.findViewById(R.id.item_five_layout);
        LinearLayout itemtenLayout = (LinearLayout) view.findViewById(R.id.item_ten_layout);
        LinearLayout itemtwoLayout = (LinearLayout) view.findViewById(R.id.item_two_layout);
        LinearLayout itemthreeLayout = (LinearLayout) view.findViewById(R.id.item_threee_layout);
        itemFiveLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                alarm_time.setText("5s");
                map.put(SharedPrefer.ALARM_T, 5);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
            }
        });
        itemtenLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                alarm_time.setText("10s");
                map.put(SharedPrefer.ALARM_T, 10);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
            }
        });
        itemtwoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                alarm_time.setText("20s");
                map.put(SharedPrefer.ALARM_T, 20);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
            }
        });
        itemthreeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                map.put(SharedPrefer.ALARM_T, 30);
                SharedPrefer.SaveAppSetingData(SettingLayerFragment.this.getActivity(), map);
                alarm_time.setText("30s");
            }
        });
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


}
