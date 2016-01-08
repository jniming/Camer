package com.example.ipcam.camer;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.ipcam.camer.Listener.APListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcAPActivity extends BaseActivity implements APListener,
		OnClickListener {

	private EditText a_ssid, a_psw, a_ms, a_ip, a_ym, a_start_ip, a_end_ip;
	private String ssid, psw, ms, ip, ym, startip, endip;
	private IpcDevice device;
	private int ms_id;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			a_ssid.setText(ssid);
			a_psw.setText(psw);
			a_ms.setText(ms);
			a_ip.setText(ip);
			a_ym.setText(ym);
			a_start_ip.setText(startip);
			a_end_ip.setText(endip);
			hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingap);
		setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
		setTitle("AP设置");
		BridgeService.setAPListener(this);
		initData();
		initView();
		DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_AP);
		showProgressDialog("加载..");

	}

	private void initView() {
		a_ssid = (EditText) findViewById(R.id.ap_stt_ssid);
		a_psw = (EditText) findViewById(R.id.ap_stt_psw);
		a_ms = (EditText) findViewById(R.id.ap_stt_encrypt);
		a_ip = (EditText) findViewById(R.id.ap_stt_ip);
		a_ym = (EditText) findViewById(R.id.ap_stt_ym);
		a_start_ip = (EditText) findViewById(R.id.ap_stt_falst_ip);
		a_end_ip = (EditText) findViewById(R.id.ap_stt_end_ip);
		a_ms.setOnClickListener(this);
	}

	private void initData() {
		device = (IpcDevice) getIntent().getSerializableExtra("device");
	}

	public void TestData() {
		ssid = a_ssid.getText().toString().trim();
		psw = a_psw.getText().toString().trim();
		ms = a_ms.getText().toString().trim();
		ip = a_ip.getText().toString().trim();
		ym = a_ym.getText().toString().trim();
		startip = a_start_ip.getText().toString().trim();
		endip = a_end_ip.getText().toString().trim();

	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (TitleBar.RIGHT == titleBar) { // 完成
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("encrypt", modetoid(a_ms.getText().toString()
						.trim()));
				jsonObject.put("ssid", a_ssid.getText().toString());
				jsonObject.put("key", a_psw.getText().toString());
				jsonObject.put("ipaddr", a_ip.getText().toString());
				jsonObject.put("mask", a_ym.getText().toString());
				jsonObject.put("startip", a_start_ip.getText().toString());
				jsonObject.put("endip", a_end_ip.getText().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int temp = DeviceSDK.setDeviceParam(device.getUserid(),
					Util.SET_AP, jsonObject.toString());
			if (temp > 0) {
				showToast("设置成功");
				finish();
			} else {
				showToast("设置失败,请重试!");

			}
		} else {
			finish();
		}
	}

	@Override
	public void callBack_getParam(long UserID, long nType, String param) {
		try {
			JSONObject localJSONObject = new JSONObject(param);
			this.ms = mode(localJSONObject.getInt("encrypt"));
			// this.apModel.setApchannel(localJSONObject.getInt("port"));
			this.ssid = localJSONObject.getString("ssid");
			this.psw = localJSONObject.getString("key");
			this.ip = localJSONObject.getString("ipaddr");
			this.ym = localJSONObject.getString("mask");
			this.startip = localJSONObject.getString("startip");
			this.endip = localJSONObject.getString("endip");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		handler.sendEmptyMessage(0);

	}

	@Override
	public void callBack_setParam(long UserID, long nType, int nResult) {

	}

	private String mode(int id) {
		String str = "";
		switch (id) {
		case 0:
			str = "无加密";
			break;
		case 1:
			str = "WEP:不支持";

			break;
		case 2:
			str = "WPA/AES";
			break;
		case 3:
			str = "WPA/TKIP";

			break;
		case 4:
			str = "WPA2/AES";

			break;
		case 5:
			str = "WPA2/TKIP";
			break;
		default:
			break;
		}
		return str;

	}

	private int modetoid(String str) {
		int i = 0;
		if (str.equals("NONE")) {
			i = 0;
		} else if (str.equals("WEP/NONE")) {
			i = 1;
		} else if (str.equals("WPA/AES")) {
			i = 2;
		} else if (str.equals("WPA/TKIP")) {
			i = 3;
		} else if (str.equals("WPA2/AES")) {
			i = 4;
		} else if (str.equals("WPA2/TKIP")) {
			i = 5;
		}

		return i;

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.ap_stt_encrypt) {
			final Dialog dialog = new Dialog(this);
			dialog.setTitle("模式列表");
			ListView listview = new ListView(this);
			final List<String> li = new ArrayList<String>();
			li.add("NONE");
			li.add("WEP/NONE");
			li.add("WEP/AES");
			li.add("WPA/TKIP");
			li.add("WPA2/TKIP");
			li.add("WPA2/AES");
			ListAdapter list = new ArrayAdapter<String>(this,
					R.layout.ms_list_item, R.id.ms_text, li);
			listview.setAdapter(list);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					System.out.println("arg2--" + arg2 + "arg3--" + arg3);
					switch (arg2) {
					case 0:
						a_ms.setText(li.get(arg2));
						ms_id = 0;
						break;
					case 1:
						a_ms.setText(li.get(arg2));
						ms_id = 1;
						break;
					case 2:
						a_ms.setText(li.get(arg2));
						ms_id = 2;
						break;
					case 3:
						a_ms.setText(li.get(arg2));
						ms_id = 3;
						break;
					case 4:
						ms_id = 4;
						a_ms.setText(li.get(arg2));
						break;

					default:
						break;
					}
					dialog.dismiss();
					;
				}

			});
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setCancelable(true);
			dialog.addContentView(listview, layoutParams);
			dialog.show();

		}
	}
}
