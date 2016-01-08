package com.example.ipcam.camer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ipcam.camer.Listener.DateListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Ttoast;
import com.example.ipcam.camer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcTimeActivity extends BaseActivity implements
        DateListener, OnClickListener {
    private LinearLayout time_pop, ntp_pop, ntp_ll;
    private TextView ntp_text, local_text, nowtime_text;
    private IpcDevice device;
    private Button button;
    private int jiaoz = 0, timezone = 0; // 校准
    private String web; // ntp服务器
    private ImageView ntp_of;
    private PopupWindow timeZpop, ntpPop;
    private long nowtime;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy年MM月dd日 HH:mm:ss");
            String d = dateFormat.format(date);
            nowtime_text.setText(d);
            getzone(timezone);
            if (jiaoz > 0) {
                ntp_of.setImageResource(R.drawable.checkbox_on);
                ntp_ll.setVisibility(View.VISIBLE);
            } else {
                ntp_of.setImageResource(R.drawable.checkbox_off);
                ntp_ll.setVisibility(View.GONE);
            }
            ntp_text.setText(web);
            hideProgressDialog();

        }
    };

    public void getzone(int timezone) {
        String str = "";
        switch (timezone) {
            case 39600:
                str = "(GMT-11:00)" + "中途岛,萨摩亚群岛";
                break;
            case 36000:
                str = "(GMT-10:00)" + "夏威夷";
                break;
            case 32400:
                str = "(GMT-09:00)" + "阿拉斯加";
                break;
            case 28800:
                str = "(GMT-08:00)" + "美国加拿大";
                break;
            case 25200:
                str = "(GMT-07:00)" + "山地时间(美国加拿大)";
                break;
            case 21600:
                str = "(GMT-06:00)" + "中部时间(美国加拿大)";
                break;
            case 18000:
                str = "(GMT-05:00)" + "东部时间(美国加拿大)";
                break;
            case 14400:
                str = "(GMT-04:00)" + "太平洋时间(加拿大)";
                break;
            case 12600:
                str = "(GMT-03:30)" + "纽芬兰";
                break;
            case 10800:
                str = "(GMT-03:00)" + "巴西利亚";
                break;
            case 7200:
                str = "(GMT-02:00)" + "中大西洋";
                break;
            case 3600:
                str = "(GMT-01:00)" + "拂得角群岛";
                break;
            case 0:
                str = "(GMT)" + "格林威治平时";
                break;
            case -3600:
                str = "(GMT+01:00)" + "布鲁塞尔";
                break;
            case -7200:
                str = "(GMT+02:00)" + "雅典,耶路撒冷";
                break;
            case -10800:
                str = "(GMT+03:00)" + "内罗华";
                break;
            case -12600:
                str = "(GMT+03:30)" + "德黑兰";
                break;
            case -14400:
                str = "(GMT+04:00)" + "巴库,第比利斯";
                break;
            case -16200:
                str = "(GMT+04:30)" + "科布尔";
                break;
            case -18000:
                str = "(GMT+05:00)" + "伊斯兰堡,卡拉奇";
                break;
            case -19800:
                str = "(GMT+05:30)" + "加尔个答,孟买,马德拉斯";
                break;
            case -21600:
                str = "(GMT+06:00)" + "阿拉木图,新西伯利亚";
                break;
            case -25200:
                str = "(GMT+07:00)" + "曼谷,河内,雅加达";
                break;
            case -28800:
                str = "(GMT+08:00)" + "北京,新加坡,台北";
                break;
            case -32400:
                str = "(GMT+09:00)" + "首尔,雅库茨克";
                break;
            case -34200:
                str = "(GMT+09:30)" + "达尔文";
                break;
            case -36000:
                str = "(GMT+10:00)" + "关岛,墨尔本";
                break;
            case -39600:
                str = "(GMT+11:00)" + "马加丹,所罗门群岛";
                break;
            case -43200:
                str = "(GMT+12:00)" + "奥克兰,惠灵顿,斐济";
                break;
        }
        local_text.setText(str);
    }

    public void initNtpWeb() {
        View view = LayoutInflater.from(this).inflate(R.layout.ntp_popup, null);
        LinearLayout z0 = (LinearLayout) view.findViewById(R.id.n0);
        LinearLayout z1 = (LinearLayout) view.findViewById(R.id.n1);
        LinearLayout z2 = (LinearLayout) view.findViewById(R.id.n2);
        LinearLayout z3 = (LinearLayout) view.findViewById(R.id.n3);
        setntpListenner(z3, "time.windows.com");
        setntpListenner(z2, "time.nuri.net");
        setntpListenner(z1, "time.nist.gov");
        setntpListenner(z0, "time.kriss.re.kr");
        ntpPop = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

    }

    private void setntpListenner(View view, final String st) {

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ntp_text.setText(st);
                web = st;
                closepop();
            }
        });

    }

    public void initTimeZone() {
        View view = LayoutInflater.from(this).inflate(R.layout.timepop_popup,
                null);
        LinearLayout z0 = (LinearLayout) view.findViewById(R.id.z0);
        LinearLayout z1 = (LinearLayout) view.findViewById(R.id.z1);
        LinearLayout z2 = (LinearLayout) view.findViewById(R.id.z2);
        LinearLayout z3 = (LinearLayout) view.findViewById(R.id.z3);
        LinearLayout z4 = (LinearLayout) view.findViewById(R.id.z4);
        LinearLayout z5 = (LinearLayout) view.findViewById(R.id.z5);
        LinearLayout z6 = (LinearLayout) view.findViewById(R.id.z6);
        LinearLayout z7 = (LinearLayout) view.findViewById(R.id.z7);
        LinearLayout z8 = (LinearLayout) view.findViewById(R.id.z8);
        LinearLayout z9 = (LinearLayout) view.findViewById(R.id.z9);
        LinearLayout z10 = (LinearLayout) view.findViewById(R.id.z10);
        LinearLayout z11 = (LinearLayout) view.findViewById(R.id.z11);
        LinearLayout z12 = (LinearLayout) view.findViewById(R.id.z12);
        LinearLayout z13 = (LinearLayout) view.findViewById(R.id.z13);
        LinearLayout z14 = (LinearLayout) view.findViewById(R.id.z14);
        LinearLayout z15 = (LinearLayout) view.findViewById(R.id.z15);
        LinearLayout z16 = (LinearLayout) view.findViewById(R.id.z16);
        LinearLayout z17 = (LinearLayout) view.findViewById(R.id.z17);
        LinearLayout z18 = (LinearLayout) view.findViewById(R.id.z18);
        LinearLayout z19 = (LinearLayout) view.findViewById(R.id.z19);
        LinearLayout z20 = (LinearLayout) view.findViewById(R.id.z20);
        LinearLayout z21 = (LinearLayout) view.findViewById(R.id.z21);
        LinearLayout z22 = (LinearLayout) view.findViewById(R.id.z22);
        LinearLayout z23 = (LinearLayout) view.findViewById(R.id.z23);
        LinearLayout z24 = (LinearLayout) view.findViewById(R.id.z24);
        LinearLayout z25 = (LinearLayout) view.findViewById(R.id.z25);
        LinearLayout z26 = (LinearLayout) view.findViewById(R.id.z26);
        LinearLayout z27 = (LinearLayout) view.findViewById(R.id.z27);
        LinearLayout z28 = (LinearLayout) view.findViewById(R.id.z28);

        setTimeZlisten(z0, 39600);
        setTimeZlisten(z1, 36000);
        setTimeZlisten(z2, 32400);
        setTimeZlisten(z3, 28800);
        setTimeZlisten(z4, 25200);
        setTimeZlisten(z5, 21600);
        setTimeZlisten(z6, 18000);
        setTimeZlisten(z7, 14400);
        setTimeZlisten(z8, 12600);
        setTimeZlisten(z9, 10800);
        setTimeZlisten(z10, 7200);
        setTimeZlisten(z11, 3600);
        setTimeZlisten(z12, 0);
        setTimeZlisten(z13, -3600);
        setTimeZlisten(z14, -7200);
        setTimeZlisten(z15, -10800);
        setTimeZlisten(z16, -12600);
        setTimeZlisten(z17, -14400);
        setTimeZlisten(z18, -16200);
        setTimeZlisten(z19, -18000);
        setTimeZlisten(z20, -19800);
        setTimeZlisten(z21, -21600);
        setTimeZlisten(z22, -25200);
        setTimeZlisten(z23, -28800);
        setTimeZlisten(z24, -32400);
        setTimeZlisten(z25, -34200);
        setTimeZlisten(z26, -36000);
        setTimeZlisten(z27, -39600);
        setTimeZlisten(z28, -43200);

        timeZpop = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

    }

    private void setTimeZlisten(View view, final int id) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getzone(id);
                timezone = id;
                closepop();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingtime);
        setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
        setTitle("时钟设置");
        BridgeService.setDateListener(this);
        initData();
        initView();
        DeviceSDK.getDeviceParam(device.getUserid(), 8214);
        showProgressDialog("加载..");
    }

    private void initView() {
        time_pop = (LinearLayout) findViewById(R.id.localtime_pop);
        ntp_pop = (LinearLayout) findViewById(R.id.ntp_pop);
        ntp_ll = (LinearLayout) findViewById(R.id.net_setting);
        ntp_text = (TextView) findViewById(R.id.ntp_text);
        local_text = (TextView) findViewById(R.id.localtime_text);
        nowtime_text = (TextView) findViewById(R.id.nowtime);
        ntp_of = (ImageView) findViewById(R.id.ntp_on_off);
        button = (Button) findViewById(R.id.correct_btn);
        initTimeZone();
        initNtpWeb();
        ntp_pop.setOnClickListener(this);
        time_pop.setOnClickListener(this);
        ntp_of.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
    }

    public JSONObject setData() {
        String tz = local_text.getText().toString().trim();
        String ntp = ntp_text.getText().toString().trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time_zone", tz);
            jsonObject.put("ntp_svr", ntp);
            jsonObject.put("ntp_enable", jiaoz);
            jsonObject.put("now", 0);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (TitleBar.RIGHT == titleBar) { // 完成
            JSONObject jsonObject = setData();
            long temp = DeviceSDK.setDeviceParam(device.getUserid(),
                    Util.SET_TIME, jsonObject.toString());
            if (temp > 0) {
                showToast("设置成功");
                finish();
            }
        } else {
            finish();
        }
    }

    @Override
    public void callBack_getParam(long UserID, long nType, String param) {

        try {
            JSONObject localJSONObject = new JSONObject(param);
            this.nowtime = localJSONObject.getLong("now");
            this.jiaoz = localJSONObject.getInt("ntp_enable"); // 校准
            this.timezone = localJSONObject.getInt("timezone");
            this.web = localJSONObject.getString("ntp_svr");
            System.out.println("服务器" + web + "校准" + jiaoz + "当前时间" + nowtime
                    + "时区" + timezone);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void callBack_setParam(long UserID, long nType, int nResult) {
        System.out.println("nType--" + nType);
        hideProgressDialog();
        if (nResult > 0) {

            Ttoast.show(SettingIpcTimeActivity.this, "设置成功");
            finish();
        } else {
            Ttoast.show(SettingIpcTimeActivity.this, "设置失败");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ntp_on_off) {
            if (jiaoz > 0) {
                ntp_of.setImageResource(R.drawable.checkbox_off);
                jiaoz = 0;
                ntp_ll.setVisibility(View.GONE);
            } else {
                ntp_of.setImageResource(R.drawable.checkbox_on);
                ntp_ll.setVisibility(View.VISIBLE);
                jiaoz = 1;
            }

            handler.sendEmptyMessage(0);
        } else if (id == R.id.localtime_pop) {
            closepop();
            timeZpop.showAsDropDown(time_pop, 30, -3);
        } else if (id == R.id.ntp_pop) {
            closepop();
            ntpPop.showAsDropDown(ntp_pop, 30, -3);
        } else if (id == R.id.correct_btn) // 本地校准
        {
            int i = -TimeZone.getDefault().getRawOffset() / 1000;
            int j = (int) (Calendar.getInstance().getTimeInMillis() / 1000L);
            JSONObject localJSONObject = new JSONObject();
            try {
                localJSONObject.put("now", j);
                localJSONObject.put("tz", i);
                localJSONObject.put("ntp_enable", jiaoz);
                localJSONObject.put("ntp_svr", web);
                long f = DeviceSDK.setDeviceParam(device.getUserid(), Util.SET_TIME,
                        localJSONObject.toString());
                if (f > 0) {
                    showToast("校准成功");
                    finish();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    private void closepop() {
        if (timeZpop != null && timeZpop.isShowing()) {
            timeZpop.dismiss();
        }
        if (ntpPop != null && ntpPop.isShowing()) {
            ntpPop.dismiss();

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        closepop();
        return super.dispatchTouchEvent(ev);
    }

}
