package com.example.ipcam.camer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ipcam.camer.R;

public class MainDeviceMenu extends Dialog {

    private TextView textView;
    private Context context;
    private LinearLayout con, sett, kscf, ksbf, alarm, delect;

    public void SettingListener(View.OnClickListener settingListener) {
        sett.setOnClickListener(settingListener);
    }

    public void setConListener(View.OnClickListener conListener) {
        con.setOnClickListener(conListener);
    }

    public void setKsbfListener(View.OnClickListener ksbfListener) {
        ksbf.setOnClickListener(ksbfListener);
    }

    public void setkscfListener(View.OnClickListener kscfListener) {
        kscf.setOnClickListener(kscfListener);

    }

    public void setAlarmListener(View.OnClickListener alarmListener) {
        alarm.setOnClickListener(alarmListener);
    }

    public void setdelectListener(View.OnClickListener delectListener) {
        delect.setOnClickListener(delectListener);

    }



    public MainDeviceMenu(final Context context) {
        super(context, R.style.mainstyle);
        this.context = context;
        View view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.infread_pop, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        con = (LinearLayout) view
                .findViewById(R.id.main_pop_connect);
        sett = (LinearLayout) view
                .findViewById(R.id.main_pop_setting);
        ksbf = (LinearLayout) view
                .findViewById(R.id.main_pop_ksbf);
        kscf = (LinearLayout) view
                .findViewById(R.id.main_pop_kscf);
        alarm = (LinearLayout) view
                .findViewById(R.id.main_pop_alarm);
        delect = (LinearLayout) view
                .findViewById(R.id.main_pop_delect);
        setContentView(view);

    }


    public void setText(String text) {

        textView.setText(text);

    }
}
