package com.example.ipcam.camer.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ipcam.camer.IpcAlarmMsgActivity;
import com.example.ipcam.camer.PicasaActivity;
import com.example.ipcam.camer.R;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;

import java.util.List;


public class IpcAlarmListAdpter extends BaseAdapter {

    private List<IpcDevice> list;
    private Context context;
    private int temp = 0;    // 1代表消息,2代表录像

    public IpcAlarmListAdpter(Context context, List<IpcDevice> list, int temp) {
        this.context = context;
        this.list = list;
        this.temp = temp;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Myview view;
        if (convertView == null) {
            view = new Myview();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.ipc_devcie_list_item, null);
            view.name = (TextView) convertView
                    .findViewById(R.id.devicelist_item_ipcname);
            view.ipcimg = (ImageView) convertView.findViewById(R.id.ipc_img);
            view.img = (ImageView) convertView.findViewById(R.id.setting_img);

            convertView.setTag(view);
        } else {
            view = (Myview) convertView.getTag();
        }

        final IpcDevice map = list.get(position);
        view.name.setText(map.getName());
        if ("".equals(map.getPic())) {
            view.ipcimg.setImageResource(R.drawable.index_default);
        } else {
            Bitmap bitmap = Util.decodeBitmap(map.getPic());
            if (bitmap == null) {
                view.ipcimg.setImageResource(R.drawable.index_default);

            } else
                view.ipcimg.setImageBitmap(Util.decodeBitmap(map.getPic()));
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp == 1) {
                    Intent intent = new Intent(context, IpcAlarmMsgActivity.class);
                    intent.putExtra("device", map);
                    context.startActivity(intent);

                } else if (temp == 2) {
                    Intent intent = new Intent(context, PicasaActivity.class);
                    intent.putExtra("device", map);
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    class Myview {
        TextView name;
        ImageView img, ipcimg;
    }

}
