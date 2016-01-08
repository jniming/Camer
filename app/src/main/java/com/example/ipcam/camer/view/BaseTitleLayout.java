package com.example.ipcam.camer.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ipcam.camer.R;


/**
 * 自己一布局
 * 
 * @author Administrator
 * 
 */
public class BaseTitleLayout extends LinearLayout {
	public TextView left_img;
	public TextView left_text;
	public TextView center_text;
	public TextView right_text;
	public TextView right_img;
	public LinearLayout left_li;
	public LinearLayout right_li;
	public View titlebar;

	public BaseTitleLayout(Context context, int defStyleAttr) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		titlebar = inflater.inflate(R.layout.title, this, false);
		addView(titlebar);

		View content = inflater.inflate(defStyleAttr, null);

		LayoutParams params = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		addView(content, params);
		left_img = (TextView) titlebar.findViewById(R.id.left_img);
		left_text = (TextView) titlebar.findViewById(R.id.left_text);
		center_text = (TextView) titlebar.findViewById(R.id.title_center_text);
		right_text = (TextView) titlebar.findViewById(R.id.right_text);
		right_img = (TextView) titlebar.findViewById(R.id.right_img);
		left_li = (LinearLayout) titlebar.findViewById(R.id.left_layout);
		right_li = (LinearLayout) titlebar.findViewById(R.id.right_layout);

	}

	@SuppressWarnings("unchecked")
	private <T extends View> T getview(int id) {
		return (T) titlebar.findViewById(id);
	}


}
