package com.example.ipcam.camer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.view.BaseTitleLayout;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends Activity {
	protected TextView backItem;
	protected TextView titleItem;
	private BaseTitleLayout baselinlayout;
	protected Dialog progressDialog;
	public static List<Activity> activitys = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activitys.add(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		if (baselinlayout == null) {
			baselinlayout = new BaseTitleLayout(this, layoutResID);
		}
		super.setContentView(baselinlayout);
		this.setClickLintener(new View[] { baselinlayout.left_li,
				baselinlayout.right_li });
	}

	public void titleShow(boolean flg) {
		if (flg) {
			baselinlayout.titlebar.setVisibility(View.GONE);
		} else {
			baselinlayout.titlebar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activitys.remove(this);
	}

	/**
	 * 返回按钮事件 默认情况下是返回前一个Activity
	 */
	protected void back() {
		finish();
	}

	@Override
	public void setTitle(CharSequence title) {
		if (!TextUtils.isEmpty(title)) {
			baselinlayout.center_text.setText(title);
		}

	}

	@Override
	public void setTitleColor(int textColor) {
		// TODO Auto-generated method stub
		super.setTitleColor(textColor);
		if (textColor != 0) {
			baselinlayout.titlebar.setBackgroundResource(textColor);
		}
	}

	/**
	 * 对应着各个位置的组件,一个int对应一个组件,后面只是当该组件要显示文字是就可以添加上去
	 * 
	 * @param
	 * @param
	 */
	@SuppressWarnings("unused")
	public void setNaView(int left1, String left_str1, int left2,
			String left_str2, int right1, String right_st, int right2,
			String right_st2) {
		if (baselinlayout != null) {
			setResource(baselinlayout.left_img, left1, left_str1);
			setResource(baselinlayout.left_text, left2, left_str2);
			setResource(baselinlayout.right_text, right1, right_st);
			setResource(baselinlayout.right_img, right2, right_st2);
		}
	}

	public void setResource(TextView v, int res, String str) {
		if (res > 0) {
			v.setBackgroundResource(res);
		} else if (!TextUtils.isEmpty(str)) {
			v.setText(str);
		} else {
			v.setVisibility(View.GONE);

		}
	}

	/**
	 * 注册所有按键监听
	 * 
	 * @param viewGrop
	 */
	public void setClickLintener(View[] viewGrop) {

		for (View view : viewGrop) {
			view.setOnClickListener(listener);
		}

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.equals(baselinlayout.left_li)) { // 说实话我也不知道要干嘛
				viewEvent(TitleBar.LIEFT, v);
			}
			if (v.equals(baselinlayout.right_li)) {
				viewEvent(TitleBar.RIGHT, v);
			}
			if (v.equals(baselinlayout.center_text)) {

				viewEvent(TitleBar.CENTER, v);
			}

		}
	};

	/**
	 * 处理事件的抽象方法
	 * 
	 * @param titleBar
	 * @param
	 */
	public abstract void viewEvent(TitleBar titleBar, View v);

	public enum TitleBar {
		LIEFT, RIGHT, CENTER

	}

	protected void hideFunction() {
		baselinlayout.setVisibility(View.GONE);
	}

	protected void showProgressDialog(String msg) {

		progressDialog = null;
		progressDialog = new ProgressDialog(this);

		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		progressDialog.show();
		progressDialog.setContentView(R.layout.loading_press);
	}

	protected void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	protected void showToast(String msg) {
		Looper.prepare();
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Looper.loop();
	}
}
