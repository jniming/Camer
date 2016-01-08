package com.example.ipcam.camer;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ipcam.camer.Listener.FTPListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class SettingIpcFTPActivity extends BaseActivity implements
		FTPListener {

	private EditText ftp, psw, username, port, time;
	private String ftp_str, psw_str, user, port_str, time_str;
	private int mode;
	private IpcDevice device;
	private RadioGroup rg;
	private RadioButton rb1, rb2;
	private long rg_id;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			ftp.setText(ftp_str);
			psw.setText(psw_str);
			username.setText(user);
			port.setText(port_str);
			time.setText(time_str);
			if (mode == 0) {
				rb1.setChecked(true);
			} else {
				rb2.setChecked(true);
			}
			hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingftp);
		setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
		setTitle("FTP设置");
		BridgeService.setFTPListener(this);
		initData();
		initView();
		DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_FTP);
		showProgressDialog("加载..");

	}

	private void initView() {
		ftp = (EditText) findViewById(R.id.ftp_st_ftp);
		psw = (EditText) findViewById(R.id.ftp_st_psw);
		username = (EditText) findViewById(R.id.ftp_st_username);
		port = (EditText) findViewById(R.id.ftp_st_port);
		time = (EditText) findViewById(R.id.ftp_st_time);
		rg = (RadioGroup) findViewById(R.id.ftp_st_ms);
		rb1 = (RadioButton) findViewById(R.id.radioButton1);
		rb2 = (RadioButton) findViewById(R.id.radioButton2);
		// rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		//
		// }
		// });

	}

	private void initData() {
		device = (IpcDevice) getIntent().getSerializableExtra("device");
	}

	public void TestData() {
		ftp_str = ftp.getText().toString().trim();
		psw_str = psw.getText().toString().trim();
		user = username.getText().toString().trim();
		port_str = port.getText().toString().trim();
		time_str = time.getText().toString().trim();
		rg_id = rg.getCheckedRadioButtonId();
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (TitleBar.RIGHT == titleBar) { // 完成
			TestData();
			System.out.println(rg_id == R.id.radioButton1 ? 0 : 1);
			JSONObject param = new JSONObject();
			try {
				param.put("port", Integer.valueOf(port_str));
				param.put("mode", rg_id == R.id.radioButton1 ? 0 : 1);
				param.put("pwd", psw_str);
				param.put("server", ftp_str);
				param.put("user", user);
				param.put("upload_interval", Integer.valueOf(time_str));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int temp = DeviceSDK.setDeviceParam(device.getUserid(),
					Util.SET_FTP, param.toString());
			if (temp > 0) {
				showToast("设置成功");
				finish();
			} else {
				showToast("设置失败");
			}
		} else {
			finish();
		}
	}

	@Override
	public void callBack_getParam(long UserID, long nType, String param) {
		System.out.println("userid-->" + UserID + "---ntype-->" + nType);
		try {
			JSONObject localJSONObject = new JSONObject(param);
			this.mode = localJSONObject.getInt("mode");
			this.psw_str = localJSONObject.getString("pwd");
			this.ftp_str = localJSONObject.getString("server");
			this.time_str = localJSONObject.getInt("upload_interval") + "";
			this.user = localJSONObject.getString("user");
			this.port_str = localJSONObject.getString("port") + "";
		} catch (JSONException e) {
			e.printStackTrace();
		}
		handler.sendEmptyMessage(0);

	}

	@Override
	public void callBack_setParam(long UserID, long nType, int nResult) {
	}

}
