package com.example.ipcam.camer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ipcam.camer.Listener.AlarmListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class SettingIpcAlarmActivity extends BaseActivity implements
		OnClickListener, AlarmListener {

	private LinearLayout move_spl, power, img_num_linlayout, alarm_stt_li,
			io_input_linlayout, click_power, yuzhiwei;
	private TextView img_num_tv, setting_on_off, power_tv, yuzhiwei_tv;
	private ImageView io, email, audio, img;
	private LoadingDialog dialog;
	private IpcDevice device;
	private int temp;
	private boolean ismove, isio, isemail, isaudio, isimg, islisten, ispir,
			isinput;
	private int intervar, spl, po, bit; // 上传的图片间隔
	private PopupWindow splpopup, powerpopup, bitpopup, input_popwin,
			listen_ppwin;
	private ImageView listen_of, input_of, pir_of, move_of; // 四个布防
	private LinearLayout input_spl, listen_ll;
	private TextView listen_text, input_text, move_text;
	private int listen_lm; // 声音灵敏度
	private LinearLayout listen_pop, input_pop, move_pop;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (ismove || ispir || isinput || islisten) {
					alarm_stt_li.setVisibility(View.VISIBLE);
					if (ismove) {
						move_of.setImageResource(R.drawable.checkbox_on);
						move_spl.setVisibility(View.VISIBLE);
						move_text.setText(spl + "");
					} else {
						move_of.setImageResource(R.drawable.checkbox_off);
						move_spl.setVisibility(View.GONE);
					}
					if (isinput) {
						input_of.setImageResource(R.drawable.checkbox_on);
						input_spl.setVisibility(View.VISIBLE);
						if (po > 0) {
							input_text.setText("高电平");
						} else {
							input_text.setText("低电平");
						}
					} else {
						input_of.setImageResource(R.drawable.checkbox_off);
						input_spl.setVisibility(View.GONE);
					}

					if (islisten) {
						listen_of.setImageResource(R.drawable.checkbox_on);
						switch (listen_lm) {
						case 0:
							listen_text.setText("禁止检测");
							break;
						case 1:
							listen_text.setText("高灵敏度");
							break;
						case 2:
							listen_text.setText("中灵敏度");
							break;
						case 3:
							listen_text.setText("低灵敏度");
							break;
						default:
							break;
						}
						listen_ll.setVisibility(View.VISIBLE);
					} else {
						listen_of.setImageResource(R.drawable.checkbox_off);
						listen_ll.setVisibility(View.GONE);
					}
					if (bit == 0) {
						yuzhiwei_tv.setText("无");
					} else {
						yuzhiwei_tv.setText("" + bit);
					}
					if (isio) {
						io.setImageResource(R.drawable.checkbox_on);
						power.setVisibility(View.VISIBLE);

					} else {
						io.setImageResource(R.drawable.checkbox_off);
						power.setVisibility(View.GONE);
					}
					if (isimg) {
						img.setImageResource(R.drawable.checkbox_on);
						img_num_linlayout.setVisibility(View.VISIBLE);
						img_num_tv.setText(intervar + "");
					} else {
						img.setImageResource(R.drawable.checkbox_off);
						img_num_linlayout.setVisibility(View.GONE);
					}
					if (isemail) {
						email.setImageResource(R.drawable.checkbox_on);
					} else {
						email.setImageResource(R.drawable.checkbox_off);
					}
					if (isaudio) {
						audio.setImageResource(R.drawable.checkbox_off);
					} else {
						audio.setImageResource(R.drawable.checkbox_on);
					}

				} else {

					alarm_stt_li.setVisibility(View.GONE);
					move_spl.setVisibility(View.GONE);
					input_spl.setVisibility(View.GONE);
					listen_ll.setVisibility(View.GONE);

				}
			} else if (msg.what == 3) {
				showToast("设置成功");
			} else if (msg.what == 4) {
				showToast("设置失败");
			}
			hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingalarm_thing);
		setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.left_img, "");
		BridgeService.setAlarmListener(this);
		setTitle("报警设置");
		showProgressDialog("加载..");
		initData();
		initView();
		DeviceSDK.getDeviceParam(device.getUserid(), Util.GET_ALARM);
		handler.sendEmptyMessage(0);
	}

	private void initView() {
		power = (LinearLayout) findViewById(R.id.io_power_num);
		img_num_linlayout = (LinearLayout) findViewById(R.id.img_num_linlayout);
		yuzhiwei = (LinearLayout) findViewById(R.id.yuzhiwei_ll);
		alarm_stt_li = (LinearLayout) findViewById(R.id.alarm_stting_li);

		click_power = (LinearLayout) findViewById(R.id.power_onclick_ll); // io输出电平

		// click_spl = (LinearLayout) findViewById(R.id.spl_onclick_ll);
		listen_pop = (LinearLayout) findViewById(R.id.listenner_spl_onclick_ll);
		input_pop = (LinearLayout) findViewById(R.id.input_pop);
		move_pop = (LinearLayout) findViewById(R.id.spl_onclick_ll);

		// 四个item附带的item
		move_spl = (LinearLayout) findViewById(R.id.move_spl_num);
		input_spl = (LinearLayout) findViewById(R.id.input_power);
		listen_ll = (LinearLayout) findViewById(R.id.linstenner_ll);

		img_num_tv = (TextView) findViewById(R.id.img_num_tv);
		power_tv = (TextView) findViewById(R.id.power_tv);
		yuzhiwei_tv = (TextView) findViewById(R.id.yuzhiwei_tv);

		io = (ImageView) findViewById(R.id.io_on_off);
		img = (ImageView) findViewById(R.id.img_up_on_off);
		email = (ImageView) findViewById(R.id.email_on_off);
		audio = (ImageView) findViewById(R.id.bj_audio_on_off);

		// 三个pop的数字显示区
		move_text = (TextView) findViewById(R.id.spl_sp_tv);
		input_text = (TextView) findViewById(R.id.input_power_tv);
		listen_text = (TextView) findViewById(R.id.listenner_spl_sp_tv);

		// 四个item的开关
		listen_of = (ImageView) findViewById(R.id.listenner_on_off);
		input_of = (ImageView) findViewById(R.id.input_on_off);
		move_of = (ImageView) findViewById(R.id.move_on_off);
		pir_of = (ImageView) findViewById(R.id.pir_on_off);

		initSplPopuWindon();
		initpowerPopuWindon();
		inityuzhiPopuWindon();
		initinput_popwin();
		initlistenpopup();

		email.setOnClickListener(this);
		audio.setOnClickListener(this);
		img.setOnClickListener(this);
		io.setOnClickListener(this);
		move_pop.setOnClickListener(this);
		click_power.setOnClickListener(this);
		yuzhiwei.setOnClickListener(this);

		move_of.setOnClickListener(this);
		input_of.setOnClickListener(this);
		listen_of.setOnClickListener(this);
		pir_of.setOnClickListener(this);

		input_pop.setOnClickListener(this);
		listen_pop.setOnClickListener(this);
		move_pop.setOnClickListener(this);
	}

	private void initData() {
		device = (IpcDevice) getIntent().getSerializableExtra("device");
		temp = getIntent().getIntExtra("item", 0);
		dialog = new LoadingDialog(this);
		dialog.setTitle("修改中..");
	}

	public JSONObject  setdata() {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("motion_armed", this.ismove ? 1 : 0);
			localJSONObject.put("motion_sensitivity", this.spl);
			localJSONObject.put("iolinkage", this.isio);
			localJSONObject.put("iolinkage_level", this.po);
			localJSONObject.put("alarmpresetsit", this.bit); // 预置位
			localJSONObject.put("mail", this.isemail); // 邮件通知
			localJSONObject.put("record", this.isaudio); // 禁止录像
			localJSONObject.put("upload_interval", this.intervar); // 报警上传数量
			localJSONObject.put("pirenable", this.ispir);
			localJSONObject.put("input_armed", this.isinput); //输入布防
			localJSONObject.put("ioin_level", this.po); // 输入报警触发电平
			localJSONObject.put("alarm_audio", this.listen_lm); // 声音报警

			// localJSONObject.put("snapshot", this.alarmModel.getSnapshot());
			// //报警时禁止拍照
			// localJSONObject.put("alarm_temp",
			// this.alarmModel.getAlarm_temp()); //温度报警

			// localJSONObject.put("schedule_enable",
			// this.alarmModel.getSchedule_enable()); // 存储录像
			localJSONObject.put("schedule_sun_0", -1);
			localJSONObject.put("schedule_sun_1", -1);
			localJSONObject.put("schedule_sun_2", -1);
			localJSONObject.put("schedule_mon_0", -1);
			localJSONObject.put("schedule_mon_1", -1);
			localJSONObject.put("schedule_mon_2", -1);
			localJSONObject.put("schedule_tue_0", -1);
			localJSONObject.put("schedule_tue_1", -1);
			localJSONObject.put("schedule_tue_2", -1);
			localJSONObject.put("schedule_wed_0", -1);
			localJSONObject.put("schedule_wed_1", -1);
			localJSONObject.put("schedule_wed_2", -1);
			localJSONObject.put("schedule_thu_0", -1);
			localJSONObject.put("schedule_thu_1", -1);
			localJSONObject.put("schedule_thu_2", -1);
			localJSONObject.put("schedule_fri_0", -1);
			localJSONObject.put("schedule_fri_1", -1);
			localJSONObject.put("schedule_fri_2", -1);
			localJSONObject.put("schedule_sat_0", -1);
			localJSONObject.put("schedule_sat_1", -1);
			localJSONObject.put("schedule_sat_2", -1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localJSONObject;
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {

		if (TitleBar.RIGHT == titleBar) { // 完成设置
			JSONObject object = setdata();
			this.showProgressDialog("设置中");
			int flg = DeviceSDK.setDeviceParam(device.getUserid(),
					Util.SET_ALARM, object.toString());
			if (flg > 0) { // 设置成功
				SettingIpcAlarmActivity.this.finish();
			}

		} else {
			finish();
		}
	}

	public void setimg(boolean flg, ImageView move) {
		if (flg) {
			move.setImageResource(R.drawable.checkbox_off);
			flg = false;
		} else {
			flg = true;
			move.setImageResource(R.drawable.checkbox_on);
		}
		handler.sendEmptyMessage(0);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.listenner_on_off:
			if (islisten) {
				listen_of.setImageResource(R.drawable.checkbox_off);
				islisten = false;
			} else {
				islisten = true;
				listen_of.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
			break;
		case R.id.move_on_off:
			if (ismove) {
				move_of.setImageResource(R.drawable.checkbox_off);
				ismove = false;
			} else {
				ismove = true;
				move_of.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
			break;
		case R.id.input_on_off:
			if (isinput) {
				input_of.setImageResource(R.drawable.checkbox_off);
				isinput = false;
			} else {
				isinput = true;
				input_of.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);

			break;
		case R.id.pir_on_off:
			if (ispir) {
				pir_of.setImageResource(R.drawable.checkbox_off);
				ispir = false;
			} else {
				ispir = true;
				pir_of.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);

			break;
		default:
			break;
		}

		if (v.getId() == R.id.io_on_off) {// io联动

			if (isio) {
				io.setImageResource(R.drawable.checkbox_off);
				isio = false;
			} else {
				isio = true;
				io.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
		} else if (v.getId() == R.id.img_up_on_off) {// 图片上传
			if (isimg) {
				img.setImageResource(R.drawable.checkbox_off);
				isimg = false;
			} else {
				isimg = true;
				img.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
		} else if (v.getId() == R.id.bj_audio_on_off) {// 录像
			if (isaudio) {
				audio.setImageResource(R.drawable.checkbox_off);
				isaudio = false;
			} else {
				isaudio = true;
				audio.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
		} else if (v.getId() == R.id.email_on_off) {// 邮箱
			if (isemail) {
				email.setImageResource(R.drawable.checkbox_off);
				isemail = false;
			} else {
				isemail = true;
				email.setImageResource(R.drawable.checkbox_on);
			}
			handler.sendEmptyMessage(0);
		}
		if (v.getId() == R.id.spl_onclick_ll) { // 灵敏度
			closePopup();
			splpopup.showAsDropDown(this.move_pop, -30, 2);

		}
		if (v.getId() == R.id.power_onclick_ll) {
			closePopup();
			powerpopup.showAsDropDown(this.click_power, -30, 2);
		}
		if (v.getId() == R.id.yuzhiwei_ll) {
			closePopup();
			bitpopup.showAsDropDown(this.yuzhiwei, -30, 2);
		}

		if (v.getId() == R.id.t0) {
			move_text.setText("0");
			spl = 0;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t1) {
			move_text.setText("1");
			spl = 1;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t2) {
			move_text.setText("2");
			spl = 2;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t3) {
			move_text.setText("3");
			spl = 3;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t4) {
			move_text.setText("4");
			spl = 4;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t5) {
			move_text.setText("5");
			spl = 5;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t6) {
			move_text.setText("6");
			spl = 6;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t7) {
			move_text.setText("7");
			spl = 7;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t8) {
			move_text.setText("8");
			spl = 8;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.t9) {
			move_text.setText("9");
			spl = 9;
			splpopup.dismiss();
		}
		if (v.getId() == R.id.gao_ll) {
			power_tv.setText("高电平");
			po = 1;
			powerpopup.dismiss();
		}
		if (v.getId() == R.id.di_ll) {
			power_tv.setText("低电平");
			po = 0;
			powerpopup.dismiss();
		}
		if (v.getId() == R.id.input_pop) {
			closePopup();
			input_popwin.showAsDropDown(this.input_pop, -30, 2);
		}
		if (v.getId() == R.id.listenner_spl_onclick_ll) {
			closePopup();
			listen_ppwin.showAsDropDown(this.listen_pop, -30, 2);
		}

	}

	@Override
	public void callBack_getParam(long UserID, long nType, String param) {
		System.out.println("报警的设置信息---userid-->" + UserID + "---ntype-->"
				+ nType);
		try {
			JSONObject localJSONObject = new JSONObject(param);
			this.ismove = localJSONObject.getInt("motion_armed") > 0 ? true
					: false;
			// localJSONObject.getInt("alarmpresetsit");
			this.spl = localJSONObject.getInt("motion_sensitivity");
			po = localJSONObject.getInt("ioin_level");
			this.bit = localJSONObject.getInt("alarmpresetsit");
			System.out.println("iolinkage_level" + po);
			this.isio = localJSONObject.getInt("iolinkage") > 0 ? true : false;
			this.intervar = localJSONObject.getInt("upload_interval");
			this.isaudio = localJSONObject.getInt("record") > 0 ? true : false;
			this.isemail = localJSONObject.getInt("mail") > 0 ? true : false;

			this.isinput = localJSONObject.getInt("input_armed") > 0 ? true
					: false;

			this.islisten = localJSONObject.getInt("alarm_audio") > 0 ? true
					: false;
			this.listen_lm = localJSONObject.getInt("alarm_audio");
			this.ispir = localJSONObject.getInt("pirenable") > 0 ? true : false;
			isimg = (intervar > 0 ? true : false);
			handler.sendEmptyMessage(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void callBack_setParam(long UserID, long nType, int nResult) {
		System.out.println("userid-->" + UserID + "---ntype-->" + nType);
		if (nResult > 0) {
			handler.sendEmptyMessage(1);
		} else {

			handler.sendEmptyMessage(2);
		}
	}

	private void initpowerPopuWindon() {
		View view = LayoutInflater.from(this).inflate(R.layout.power_popup,
				null);
		LinearLayout g = (LinearLayout) view.findViewById(R.id.gao_ll);
		LinearLayout d = (LinearLayout) view.findViewById(R.id.di_ll);
		g.setOnClickListener(this);
		d.setOnClickListener(this);
		powerpopup = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	private void initlistenpopup() {
		View view = LayoutInflater.from(this)
				.inflate(R.layout.listen_pop, null);
		LinearLayout d = (LinearLayout) view.findViewById(R.id.lis_di);
		LinearLayout z = (LinearLayout) view.findViewById(R.id.lis_zhong);
		LinearLayout g = (LinearLayout) view.findViewById(R.id.lis_gao);
		LinearLayout n = (LinearLayout) view.findViewById(R.id.lis_none);
		setlistenlistener(n, 0);
		setlistenlistener(d, 3);
		setlistenlistener(z, 2);
		setlistenlistener(g, 1);
		listen_ppwin = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	private void initinput_popwin() {
		View view = LayoutInflater.from(this).inflate(R.layout.power_popup,
				null);
		LinearLayout g = (LinearLayout) view.findViewById(R.id.gao_ll);
		LinearLayout d = (LinearLayout) view.findViewById(R.id.di_ll);
		setinputlistener(g, 1);
		setinputlistener(d, 0);
		input_popwin = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	public void setlistenlistener(View view, final int id) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				po = id;
				switch (id) {
				case 0:
					listen_text.setText("禁止检测");
					break;
				case 1:
					listen_text.setText("高灵敏度");
					break;
				case 2:
					listen_text.setText("中灵敏度");
					break;
				case 3:
					listen_text.setText("低灵敏度");
					break;
				default:
					break;
				}
				if (listen_ppwin != null) {
					listen_ppwin.dismiss();
				}
			}
		});

	}

	public void setinputlistener(View view, final int id) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				po = id;
				if (id > 0) {
					input_text.setText("高电平");
				} else {
					input_text.setText("低电平");
				}
				if (input_popwin != null) {
					input_popwin.dismiss();
				}
			}
		});

	}

	private void inityuzhiPopuWindon() {
		View view = LayoutInflater.from(this)
				.inflate(R.layout.bitl_popup, null);
		LinearLayout t0 = (LinearLayout) view.findViewById(R.id.bit_t0);
		LinearLayout t1 = (LinearLayout) view.findViewById(R.id.bit_t1);
		LinearLayout t2 = (LinearLayout) view.findViewById(R.id.bit_t2);
		LinearLayout t3 = (LinearLayout) view.findViewById(R.id.bit_t3);
		LinearLayout t4 = (LinearLayout) view.findViewById(R.id.bit_t4);
		LinearLayout t5 = (LinearLayout) view.findViewById(R.id.bit_t5);
		LinearLayout t6 = (LinearLayout) view.findViewById(R.id.bit_t6);
		LinearLayout t7 = (LinearLayout) view.findViewById(R.id.bit_t7);
		LinearLayout t8 = (LinearLayout) view.findViewById(R.id.bit_t8);
		LinearLayout t9 = (LinearLayout) view.findViewById(R.id.bit_t9);
		LinearLayout t10 = (LinearLayout) view.findViewById(R.id.bit_t10);
		LinearLayout t11 = (LinearLayout) view.findViewById(R.id.bit_t11);
		LinearLayout t12 = (LinearLayout) view.findViewById(R.id.bit_t12);
		LinearLayout t13 = (LinearLayout) view.findViewById(R.id.bit_t13);
		LinearLayout t14 = (LinearLayout) view.findViewById(R.id.bit_t14);
		LinearLayout t15 = (LinearLayout) view.findViewById(R.id.bit_t15);
		LinearLayout t16 = (LinearLayout) view.findViewById(R.id.bit_t16);

		setlistener(t0, 0);
		setlistener(t1, 1);
		setlistener(t2, 2);
		setlistener(t3, 3);
		setlistener(t4, 4);
		setlistener(t5, 5);
		setlistener(t6, 6);
		setlistener(t7, 7);
		setlistener(t8, 8);
		setlistener(t9, 9);
		setlistener(t10, 10);
		setlistener(t11, 11);
		setlistener(t12, 12);
		setlistener(t13, 13);
		setlistener(t14, 14);
		setlistener(t15, 15);
		setlistener(t16, 16);

		bitpopup = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	public void setlistener(View view, final int id) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bit = id;
				yuzhiwei_tv.setText("" + id);
				if (bitpopup != null) {
					bitpopup.dismiss();
				}
			}
		});

	}

	private void initSplPopuWindon() {
		View view = LayoutInflater.from(this).inflate(R.layout.spl_popup, null);
		LinearLayout t0 = (LinearLayout) view.findViewById(R.id.t0);
		LinearLayout t1 = (LinearLayout) view.findViewById(R.id.t1);
		LinearLayout t2 = (LinearLayout) view.findViewById(R.id.t2);
		LinearLayout t3 = (LinearLayout) view.findViewById(R.id.t3);
		LinearLayout t4 = (LinearLayout) view.findViewById(R.id.t4);
		LinearLayout t5 = (LinearLayout) view.findViewById(R.id.t5);
		LinearLayout t6 = (LinearLayout) view.findViewById(R.id.t6);
		LinearLayout t7 = (LinearLayout) view.findViewById(R.id.t7);
		LinearLayout t8 = (LinearLayout) view.findViewById(R.id.t8);
		LinearLayout t9 = (LinearLayout) view.findViewById(R.id.t9);

		t0.setOnClickListener(this);
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
		t4.setOnClickListener(this);
		t5.setOnClickListener(this);
		t6.setOnClickListener(this);
		t7.setOnClickListener(this);
		t8.setOnClickListener(this);
		t9.setOnClickListener(this);

		splpopup = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

	}

	private void closePopup() {
		if (splpopup != null && splpopup.isShowing()) {
			splpopup.dismiss();
		}
		if (powerpopup != null && powerpopup.isShowing()) {
			powerpopup.dismiss();
		}
		if (bitpopup != null && bitpopup.isShowing()) {
			bitpopup.dismiss();
		}
		if (listen_ppwin != null && listen_ppwin.isShowing()) {
			listen_ppwin.dismiss();
		}
		if (input_popwin != null && input_popwin.isShowing()) {
			input_popwin.dismiss();

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		closePopup();
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("dianji");
		closePopup();
		return super.onTouchEvent(event);
	}
}
