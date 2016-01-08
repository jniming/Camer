package com.example.ipcam.camer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.ipcam.camer.Listener.UserListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcUserActivity extends BaseActivity implements
        UserListener {
    private EditText czz_name, czz_psw, admin_name, admin_psw;
    private IpcDevice device;
    private LoadingDialog dialog;
    private String c_name, c_psw, a_name, a_psw;
    private String optname, optpsw, adminname, adminpsw;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            czz_name.setText(optname);
            czz_psw.setText(optpsw);
            admin_name.setText(adminname);
            admin_psw.setText(adminpsw);
            hideProgressDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinguser);
        setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
        setTitle("名称修改");
        showProgressDialog("加载..");
        BridgeService.setUserListener(this);
        initData();
        initView();
        DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_USER);
    }

    private void initView() {
        czz_name = (EditText) findViewById(R.id.czz_user_name);
        czz_psw = (EditText) findViewById(R.id.czz_user_psw);
        admin_name = (EditText) findViewById(R.id.admin_user_name);
        admin_psw = (EditText) findViewById(R.id.admin_user_psw);

    }

    private void initData() {
        device = (IpcDevice) getIntent().getSerializableExtra("device");
        dialog = new LoadingDialog(this);
        dialog.setTitle("修改中..");
    }

    private boolean isMatcher() {
        c_name = czz_name.getText().toString().trim();
        a_name = admin_name.getText().toString().trim();
        a_psw = admin_psw.getText().toString().trim();
        c_psw = czz_psw.getText().toString().trim();

        Pattern localPattern = Pattern
                .compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
        Matcher localMatcher2 = localPattern.matcher(c_name);
        Matcher localMatcher3 = localPattern.matcher(a_name);
        Matcher localMatcher5 = localPattern.matcher(a_psw);
        Matcher localMatcher6 = localPattern.matcher(c_psw);
        if (localMatcher2.find() || localMatcher3.find()
                || localMatcher5.find() || localMatcher6.find()) {
            return false;
        }
        return true;
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {

        if (TitleBar.RIGHT == titleBar) {
            if (!isMatcher()) {
                showToast("非法字符");
                return;
            }
            dialog.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("pwd2", c_name);
                jsonObject.put("pwd3", a_name);
                jsonObject.put("user2", c_psw);
                jsonObject.put("user3", a_name);
                String param = jsonObject.toString();
                int temp = DeviceSDK.setDeviceParam(device.getUserid(), 0x2002,
                        param);
                if (temp > 0) {
                    finish();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    @Override
    public void callBack_getParam(long UserID, long nType, String param) {
        System.out.println("get");
        try {
            JSONObject localJSONObject = new JSONObject(param);
            this.optname = localJSONObject.getString("user2");
            this.optpsw = localJSONObject.getString("pwd2");
            this.adminname = localJSONObject.getString("user3");
            this.adminpsw = localJSONObject.getString("pwd3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void callBack_setParam(long UserID, long nType, int nResult) {
        System.out.println("userid-->" + UserID + "---ntype-->" + nType);
        if (nResult > 0) {
            dialog.dismiss();
            Looper.prepare();
            showToast("设置成功");
            Looper.loop();
            finish();
        } else {
            dialog.dismiss();
            Looper.prepare();
            showToast("设置失败");
            Looper.loop();
            return;
        }
    }
}
