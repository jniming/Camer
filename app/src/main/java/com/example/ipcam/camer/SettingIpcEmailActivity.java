package com.example.ipcam.camer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.Listener.EmailListenner;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcEmailActivity extends BaseActivity implements EmailListenner, View.OnClickListener {
    private IpcDevice device;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 0) {
                reciveUserOne.setText(recive_one);
                reciveUserTwo.setText(recive_two);
                reciveUserThree.setText(recive_three);
                settingSendUser.setText(send_user);
                settingServerAdressEd.setText(smtp_server);
                settingSmtpPort.setText(smtp_port + "");
                if (ssl_str == 0) {
                    settingSslEd.setText("none");
                } else if (ssl_str == 1) {
                    settingSslEd.setText("starttls");
                } else {
                    settingSslEd.setText("tls");
                }


                settingServerAdressEd.setText(smtp_server);
                if (ssl_str == 0) {
                    listennerOnOff.setImageResource(R.drawable.checkbox_off);
                    ssl_bol = false;
                } else {
                    ssl_bol = true;
                    listennerOnOff.setImageResource(R.drawable.checkbox_on);
                }
            }


        }
    };
    private EditText settingSendUser;
    private TextView settingServerAdressEd;
    private ImageView settingServerAdress;
    private EditText settingSmtpPort;
    private ImageView listennerOnOff;
    private TextView settingSslEd;
    private ImageView settingSslImg;
    private EditText reciveUserOne;
    private EditText reciveUserTwo;
    private EditText reciveUserThree;

    private String send_user, smtp_server, yz_str, recive_one, recive_two, recive_three;
    private int smtp_port, ssl_str;
    private boolean ssl_bol = false;

    private PopupWindow smtppop, sslppop;

    private void assignViews() {
        settingSendUser = (EditText) findViewById(R.id.setting_send_user);
        settingServerAdressEd = (TextView) findViewById(R.id.setting_server_adress_ed);
        settingServerAdress = (ImageView) findViewById(R.id.setting_server_adress);
        settingSmtpPort = (EditText) findViewById(R.id.setting_smtp_port);
        listennerOnOff = (ImageView) findViewById(R.id.listenner_on_off);
        settingSslEd = (TextView) findViewById(R.id.setting_ssl_ed);
        settingSslImg = (ImageView) findViewById(R.id.setting_ssl_img);
        reciveUserOne = (EditText) findViewById(R.id.recive_user_one);
        reciveUserTwo = (EditText) findViewById(R.id.recive_user_two);
        reciveUserThree = (EditText) findViewById(R.id.recive_user_three);
        settingServerAdress.setOnClickListener(this);
        settingSslImg.setOnClickListener(this);
        listennerOnOff.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingemail);
        setNaView(R.drawable.back, "", 0, "", R.drawable.left_img, "", 0, "");
        BridgeService.setEmailLinstener(this);
        initData();
        DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_EMAIL);
        setTitle("TF设置");
        assignViews();
        initSMTPPopwindon();
        initSSLpopwindow();
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            send_user = settingSendUser.getText().toString().trim();
            smtp_server = settingServerAdressEd.getText().toString().trim();
            recive_one = reciveUserOne.getText().toString().trim();
            recive_two = reciveUserTwo.getText().toString().trim();
            recive_three = reciveUserThree.getText().toString().trim();
            String port = settingSmtpPort.getText().toString().trim();
            String ssl = settingSslEd.getText().toString().trim();
            if (send_user.isEmpty() || smtp_server.isEmpty() || port.isEmpty() || ssl.isEmpty()) {
                Toast.makeText(this, "信息输入不完整", Toast.LENGTH_SHORT).show();
                return;
            }
            if (recive_two.isEmpty() && recive_one.isEmpty() && recive_three.isEmpty()) {
                Toast.makeText(this, "收件人必须指定一个!", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("sender", send_user);
                jsonObject.put("port", Integer.valueOf(port));
                if (ssl.equals("none")) {
                    jsonObject.put("ssl", 0);
                } else if (ssl.equals("starttls")) {
                    jsonObject.put("ssl", 1);
                } else if (ssl.equals("tls")) {
                    jsonObject.put("ssl", 2);
                }

                jsonObject.put("server", smtp_server);
                jsonObject.put("receive1", recive_one);
                jsonObject.put("receive2", recive_two);
                jsonObject.put("receive3", recive_three);
                jsonObject.put("receive4", "");
                int tem = DeviceSDK.setDeviceParam(device.getUserid(), Util.SET_EMAIL, jsonObject.toString());
                if (tem > 0) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            back();

        } else {
            back();
        }

    }

    public void initSSLpopwindow() {


        View view = LayoutInflater.from(this).inflate(R.layout.ssl_pop, null);
        LinearLayout sslNone = (LinearLayout) view.findViewById(R.id.ssl_none);
        LinearLayout sslSsl = (LinearLayout) view.findViewById(R.id.ssl_ssl);
        LinearLayout sslTls = (LinearLayout) view.findViewById(R.id.ssl_tls);

        TextView none = (TextView) view.findViewById(R.id.none_text);
        TextView ssl = (TextView) view.findViewById(R.id.ssl_text);
        TextView tls = (TextView) view.findViewById(R.id.tls_text);

        setSmtpListen(0, sslNone, none);
        setSmtpListen(0, sslSsl, ssl);
        setSmtpListen(0, sslTls, tls);

        sslppop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    public void initSMTPPopwindon() {

        View view = LayoutInflater.from(this).inflate(R.layout.smtp_pop, null);
        LinearLayout smtp1 = (LinearLayout) view.findViewById(R.id.smtp_1);
        LinearLayout smtp2 = (LinearLayout) view.findViewById(R.id.smtp_2);
        LinearLayout smtp3 = (LinearLayout) view.findViewById(R.id.smtp_3);
        LinearLayout smtp4 = (LinearLayout) view.findViewById(R.id.smtp_4);
        LinearLayout smtp5 = (LinearLayout) view.findViewById(R.id.smtp_5);
        LinearLayout smtp6 = (LinearLayout) view.findViewById(R.id.smtp_6);
        LinearLayout smtp7 = (LinearLayout) view.findViewById(R.id.smtp_7);
        LinearLayout smtp8 = (LinearLayout) view.findViewById(R.id.smtp_8);
        LinearLayout smtp9 = (LinearLayout) view.findViewById(R.id.smtp_9);
        LinearLayout smtp10 = (LinearLayout) view.findViewById(R.id.smtp_10);
        LinearLayout smtp11 = (LinearLayout) view.findViewById(R.id.smtp_11);
        LinearLayout smtp12 = (LinearLayout) view.findViewById(R.id.smtp_12);
        TextView t1 = (TextView) view.findViewById(R.id.text1);
        TextView t2 = (TextView) view.findViewById(R.id.text2);
        TextView t3 = (TextView) view.findViewById(R.id.text3);
        TextView t4 = (TextView) view.findViewById(R.id.text4);
        TextView t5 = (TextView) view.findViewById(R.id.text5);
        TextView t6 = (TextView) view.findViewById(R.id.text6);
        TextView t7 = (TextView) view.findViewById(R.id.text7);
        TextView t8 = (TextView) view.findViewById(R.id.text8);
        TextView t9 = (TextView) view.findViewById(R.id.text9);
        TextView t10 = (TextView) view.findViewById(R.id.text10);
        TextView t11 = (TextView) view.findViewById(R.id.text11);
        TextView t12 = (TextView) view.findViewById(R.id.text12);
        setSmtpListen(1, smtp1, t1);
        setSmtpListen(1, smtp2, t2);
        setSmtpListen(1, smtp3, t3);
        setSmtpListen(1, smtp4, t4);
        setSmtpListen(1, smtp5, t5);
        setSmtpListen(1, smtp6, t6);
        setSmtpListen(1, smtp7, t7);
        setSmtpListen(1, smtp8, t8);
        setSmtpListen(1, smtp9, t9);
        setSmtpListen(1, smtp10, t10);
        setSmtpListen(1, smtp11, t11);
        setSmtpListen(1, smtp12, t12);

        smtppop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    public void setSmtpListen(final int temp, LinearLayout smtp, final TextView text) {

        smtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closepoop();
                if (temp == 1) {
                    settingServerAdressEd.setText(text.getText());
                } else {
                    settingSslEd.setText(text.getText());

                }
            }
        });
    }


    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
    }


    @Override
    public void callBack_getParam(long UserID, long nType, String param) {
        try {
            JSONObject json = new JSONObject(param);
            send_user = json.getString("sender");
            smtp_port = json.getInt("port");
            ssl_str = json.getInt("ssl");
            smtp_server = json.getString("server");
            recive_one = json.getString("receive1");
            recive_two = json.getString("receive2");
            recive_three = json.getString("receive3");

            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callBack_setParam(long UserID, long nType, int nResult) {

    }

    private void closepoop() {
        if (smtppop.isShowing() && smtppop != null) {
            smtppop.dismiss();
        }
        if (sslppop.isShowing() && sslppop != null)
            sslppop.dismiss();

    }

    @Override
    public void onClick(View v) {
        if (settingServerAdress.getId() == v.getId()) {
            closepoop();
            smtppop.showAsDropDown(settingServerAdress);
        } else if (v.getId() == settingSslImg.getId()) {
            closepoop();
            sslppop.showAsDropDown(settingSslImg);
        } else if (v.getId() == listennerOnOff.getId()) {
            if (ssl_bol) {
                ssl_bol = false;
                ssl_str = 0;
                listennerOnOff.setImageResource(R.drawable.checkbox_off);
            } else
                ssl_str = 1;
            ssl_bol = true;
            listennerOnOff.setImageResource(R.drawable.checkbox_on);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        closepoop();
        return super.onTouchEvent(event);
    }

}
