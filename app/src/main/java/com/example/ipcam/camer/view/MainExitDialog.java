package com.example.ipcam.camer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ipcam.camer.R;


public class MainExitDialog extends Dialog {

    private Context context;

    private Button exit, boung, center;


    public void setExitListener(View.OnClickListener listener) {
        exit.setOnClickListener(listener);

    }

    public void setExitBoundListener(View.OnClickListener listener) {
        boung.setOnClickListener(listener);

    }

    public void setExitDimssListener(View.OnClickListener listener) {
        center.setOnClickListener(listener);

    }


    public MainExitDialog(final Context context) {
        super(context, R.style.mainstyle);
        this.context = context;
        View view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.exit_dialog, null);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        exit = (Button) view.findViewById(R.id.exit_btn);
        boung = (Button) view.findViewById(R.id.exit_bround_btn);
        center = (Button) view.findViewById(R.id.center_btn);
        setContentView(view);

    }

}
