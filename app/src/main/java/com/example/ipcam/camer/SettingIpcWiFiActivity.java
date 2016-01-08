package com.example.ipcam.camer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ipcam.camer.Listener.IpcWiFiListListener;
import com.example.ipcam.camer.Listener.WiFiListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.entity.WiFiMode;
import com.example.ipcam.camer.entity.wifi;
import com.example.ipcam.camer.util.SortComparator;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.CustomDialog;
import com.example.ipcam.camer.view.CustomListView;
import com.example.ipcam.camer.view.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcWiFiActivity extends BaseActivity implements
		WiFiListener, OnClickListener, IpcWiFiListListener {
	private CustomListView listView;
	private IpcDevice device;
	private WiFiMode wifiModel = new WiFiMode();
	private TextView safe_text, channel_text, wifi_ssid;
	private EditText pwd_text;
	private CustomDialog.Builder builder;
	private Dialog alertDialog;
	private LinearLayout wifi_search, ipc_wifi;
	private List<ScanResult> list;
	private WifiManager manager;
	private LoadingDialog dialog;
	private Ipcadpter ipcadapter;
	private Myadpter myadpter;
	private List<wifi> ipc_wifiList = new ArrayList<wifi>();

	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				wifi_ssid.setText(wifiModel.getSsid());
				channel_text.setText(wifiModel.getChannel() + "");
				pwd_text.setText(wifiModel.getWpa_psk());
				safe_text.setText(getSafe(wifiModel.getEncryp()));
				hideProgressDialog();
			} else if (msg.what == 2) {
				dialog.dismiss();
				Comparator<wifi> comp = new SortComparator();
				Collections.sort(ipc_wifiList, comp);
				ipcadapter.notifyDataSetChanged();
			}
		}
	};

	public String getSafe(int id) {
		String str = "";
		switch (id) {
		case 0:
			str = "WEP-NONE";
			break;
		case 1:
			str = "WEP";
			break;
		case 2:
			str = "WPA-PSK TKIP";
			break;
		case 3:
			str = "WPA-PSK AES";
			break;
		case 4:
			str = "WPA2-PSK TKIP";
			break;
		case 5:
			str = "WPA2-PSK AES";
			break;

		default:
			break;
		}
		return str;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingwifi);
		setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
		setTitle("WIFI设置");
		showProgressDialog("加载..");
		BridgeService.setWifiListener(this);
		BridgeService.setIpcWifiLinstener(this);
		initData();
		initView();
		DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_WIFI);
	}

	private void initView() {
		safe_text = (TextView) findViewById(R.id.wifi_safe);
		channel_text = (TextView) findViewById(R.id.wifi_channel);
		wifi_ssid = (TextView) findViewById(R.id.wifi_ssid);
		pwd_text = (EditText) findViewById(R.id.wifi_pwd);
		wifi_search = (LinearLayout) findViewById(R.id.wifi_search);
		ipc_wifi = (LinearLayout) findViewById(R.id.search_ipc_wifi);
		listView = (CustomListView) findViewById(R.id.wifi_listview);
		dialog = new LoadingDialog(this);
		wifi_search.setOnClickListener(this);
		ipc_wifi.setOnClickListener(this);
	}

	private void initData() {
		device = (IpcDevice) getIntent().getSerializableExtra("device");
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {

		if (TitleBar.RIGHT == titleBar) {
			if (!pwd_text.getText().toString().trim().isEmpty()) {

				JSONObject localJSONObject = new JSONObject();
				try {
					localJSONObject.put("ssid", this.wifiModel.getSsid());
					localJSONObject.put("channel", this.wifiModel.getChannel());
					localJSONObject.put("mode", this.wifiModel.getMode());
					localJSONObject.put("enable", 1);
					localJSONObject.put("wpa_psk", pwd_text.getText()
							.toString().trim());
					localJSONObject
							.put("encrypt", this.wifiModel.getSecurity());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long s = DeviceSDK.setDeviceParam(device.getUserid(),
						Util.SET_WIFI, localJSONObject.toString());
				if (s > 0) {
					showToast("修改成功,设备正在重启..");

					device.reopenDevice();
					finish();

				} else {
					showToast("修改失败,请重试");
				}
			} else {
				showToast("wifi密码为空");
			}
		} else {
			finish();
		}
	}

	@Override
	public void callBack_getParam(long UserID, long nType, String param) {

		if (nType == Util.GET_WIFI) {
			try {
				JSONObject localJSONObject = new JSONObject(param);
				this.wifiModel.setEncryp(localJSONObject.getInt("encrypt"));
				this.wifiModel
						.setKeyformat(localJSONObject.getInt("keyformat"));
				this.wifiModel.setDefkey(localJSONObject.getInt("defkey"));
				this.wifiModel.setEnable(localJSONObject.getInt("enable"));
				this.wifiModel.setSsid(localJSONObject.get("ssid").toString());
				this.wifiModel.setChannel(localJSONObject.getInt("channel"));
				this.wifiModel.setMode(localJSONObject.getInt("mode"));
				this.wifiModel.setAuthtype(localJSONObject.getInt("authtype"));
				this.wifiModel.setKey1(localJSONObject.getString("key1"));
				this.wifiModel.setWpa_psk(localJSONObject.getString("wpa_psk"));
				this.wifiModel.setKey2(localJSONObject.getString("key2"));
				this.wifiModel.setKey3(localJSONObject.getString("key3"));
				this.wifiModel.setKey4(localJSONObject.getString("key4"));
				this.wifiModel
						.setKey1_bits(localJSONObject.getInt("key1_bits"));
				this.wifiModel
						.setKey2_bits(localJSONObject.getInt("key2_bits"));
				this.wifiModel
						.setKey3_bits(localJSONObject.getInt("key3_bits"));
				this.wifiModel
						.setKey4_bits(localJSONObject.getInt("key4_bits"));
				System.out.println("wifi设置");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (nType == 0x2014) {
			System.out.println("wifi热点获取-->" + param.toString());
		}
		handler.sendEmptyMessage(0);
	}

	@Override
	public void callBack_setParam(long UserID, long nType, int nResult) {

	}

	public void MyAlert(int id) {
		builder = new CustomDialog.Builder(this);
		ListView listView = new ListView(this);
		if (id == 1) {
			Myadpter myadpter = new Myadpter();
			listView.setAdapter(myadpter);

		} else if (id == 2) {
			Ipcadpter cip = new Ipcadpter();
			listView.setAdapter(cip);
		}

		builder.setTitle("WIFI列表");
		builder.setContentView(listView);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog = builder.create();
		alertDialog.show();

	}

	class Myadpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(SettingIpcWiFiActivity.this)
					.inflate(R.layout.search_wifi_item, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.wifi_img);
			TextView name = (TextView) convertView.findViewById(R.id.wifi_name);
			final ScanResult wifi = list.get(position);
			name.setText(wifi.SSID);
			if (Math.abs(wifi.level) >= 80) {
				img.setImageResource(R.drawable.wifi_levl3);
			} else if (Math.abs(wifi.level) < 80 && Math.abs(wifi.level) >= 50) {
				img.setImageResource(R.drawable.wifi_levl2);
			} else if (Math.abs(wifi.level) < 50 && Math.abs(wifi.level) >= 0) {
				img.setImageResource(R.drawable.wifi_levl1);
			}

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					initView();
					wifiModel.setSsid(wifi.SSID);
					wifiModel.setWpa_psk("");
					handler.sendEmptyMessage(0);
				}
			});
			return convertView;
		}
	}

	class Ipcadpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ipc_wifiList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(SettingIpcWiFiActivity.this)
					.inflate(R.layout.search_wifi_item, null);
			ImageView img = (ImageView) convertView.findViewById(R.id.wifi_img);
			TextView name = (TextView) convertView.findViewById(R.id.wifi_name);
			final wifi wifi = ipc_wifiList.get(position);
			name.setText(wifi.getSsid());
			if (wifi.getDbm0() >= 80) {
				img.setImageResource(R.drawable.wifi_levl3);
			} else if (wifi.getDbm0() < 80 && wifi.getDbm0() >= 50) {
				img.setImageResource(R.drawable.wifi_levl2);
			} else if (wifi.getDbm0() < 50 && wifi.getDbm0() >= 0) {
				img.setImageResource(R.drawable.wifi_levl1);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					initView();
					wifiModel.setSsid(wifi.getSsid());
					wifiModel.setWpa_psk("");
					wifiModel.setChannel(wifi.getChannel());
					handler.sendEmptyMessage(0);
				}
			});
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.wifi_search) {
			manager = (WifiManager) SettingIpcWiFiActivity.this
					.getSystemService(Context.WIFI_SERVICE);
			list = manager.getScanResults();
			openWifi();
			myadpter = new Myadpter();
			listView.setAdapter(myadpter);
			Util.setListViewHeightBasedOnChildren(listView, 0);

		} else if (v.getId() == R.id.search_ipc_wifi) {
			DeviceSDK.getDeviceParam(device.getUserid(), 0x2014);
			ipcadapter = new Ipcadpter();
			listView.setAdapter(ipcadapter);
			Util.setListViewHeightBasedOnChildren(listView, 0);
		}
	}

	/**
	 * 打开WIFI
	 */
	private void openWifi() {
		if (!manager.isWifiEnabled()) {
			manager.setWifiEnabled(true);
		}

	}

	@Override
	public void callBack_getWIFI(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		Log.d("zjm", "wifi回调参数" + param.toString());

		try {
			JSONArray array = new JSONArray(param);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				wifi wifi = new wifi();
				wifi.setSsid(jsonObject.getString("ssid"));
				wifi.setChannel(jsonObject.getInt("channel"));
				wifi.setMac(jsonObject.getString("mac"));
				wifi.setSecurity(jsonObject.getInt("security"));
				wifi.setDbm0(jsonObject.getInt("dbm0"));
				wifi.setDbm1(jsonObject.getInt("dbm1"));
				ipc_wifiList.add(wifi);
			}

			handler.sendEmptyMessage(2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
