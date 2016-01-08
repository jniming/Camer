package com.example.ipcam.camer.adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ipcam.camer.R;
import com.example.ipcam.camer.entity.IpcDevice;

import java.util.List;


public class IpcSearchListAdpter extends BaseAdapter {
    private List<IpcDevice> srarchlist;
    private Context context;

    public IpcSearchListAdpter(List<IpcDevice> srarchlist, Context context) {
        this.srarchlist = srarchlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (srarchlist == null || srarchlist.size() == 0) {
            return 0;
        }
        return srarchlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return srarchlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressLint("view")
    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        viewHolder view;
        if (convertView == null) {
            view = new viewHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.search_device_item, null);
            view.uid = (TextView) convertView
                    .findViewById(R.id.search_device_uuid);
            view.ip = (TextView) convertView
                    .findViewById(R.id.search_device_ip);
            view.add = (TextView) convertView
                    .findViewById(R.id.search_isadd_text);
            convertView.setTag(view);
        } else {
            view = (viewHolder) convertView.getTag();
        }
        IpcDevice ipcDevice = srarchlist.get(position);
        view.uid.setText(ipcDevice.getDeviceid());
        view.ip.setText(ipcDevice.getIp());
        if (ipcDevice.isAdd()) {
            view.add.setText("已添加");
        } else
            view.add.setText("未添加");
        return convertView;
    }

    class viewHolder {
        TextView uid, ip, add;
    }

}
