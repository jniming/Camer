package com.example.ipcam.camer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.example.ipcam.camer.R;


public class LoadingDialog extends Dialog {

	private TextView textView;

	public LoadingDialog(Context context) {
		super(context, R.style.CustomDialog);
		View view = LayoutInflater.from(context).inflate(R.layout.loading_view,
				null);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		textView = (TextView) view.findViewById(R.id.loading_tv);
		this.setCanceledOnTouchOutside(false);
		setContentView(view);

	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.loading_view);
	//
	// }

	public void setText(String text) {

		textView.setText(text);

	}
}
