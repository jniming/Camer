package com.example.ipcam.camer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ipcam.camer.Listener.SeachListener;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.adpter.IpcSearchListAdpter;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class DeviceSeachListActivity extends BaseActivity implements SeachListener, XListView.IXListViewListener {

    private List list = new ArrayList<IpcDevice>();
    private List<IpcDevice> ipclist;
    private IpcSearchListAdpter adpter;
    private XListView xListView;
    private ProgressBar loading;
    private TextView text;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            xListView.stopRefresh();
            xListView.stopLoadMore();
            xListView.setRefreshTime(Util.getNowTime());
            xListView.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            if (list.size() == 0) {
                text.setVisibility(View.VISIBLE);
            } else {
                isAdd(list);
                text.setVisibility(View.GONE);
                xListView.requestLayout();
                adpter.notifyDataSetChanged();
            }
        }

    };
    public void isAdd(List<IpcDevice> ipl) {
        if (ipclist != null && ipl != null) {
            for (IpcDevice cd : ipclist) {
                for (IpcDevice ipcDevice : ipl) {
                    if (ipcDevice.getDeviceid().equals(cd.getDeviceid())) {
                        ipcDevice.setAdd(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list_chang_activity);
        setTitle(getResources().getString(R.string.ipc_list));
        BridgeService.setSearchListener(this);
        xListView = (XListView) findViewById(R.id.ipc_search_xlistview);
        xListView.setPullLoadEnable(false);
        xListView.setIXListViewListener(this);
        loading = (ProgressBar) findViewById(R.id.loading);
        text = (TextView) findViewById(R.id.ipc_search_text);
        setNaView(R.drawable.back, "", 0, "", 0, "", R.drawable.seach_img, "");
        adpter = new IpcSearchListAdpter(list, this);
        xListView.setAdapter(adpter);
        ipclist = DeviceManager.getInstence(this).GetIPCListDevice();
        SearchDevice();

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IpcDevice device = (IpcDevice) list.get(position - 1);
                Intent intent = new Intent();
                intent.putExtra("device", device);
                DeviceSeachListActivity.this.setResult(100, intent);
                finish();
            }
        });
        Timer Timer = new Timer();
        TimerTask TimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        Timer.schedule(TimerTask, 5000);

    }


    public void SearchDevice() {
        list.clear();
        DeviceSDK.searchDevice(); // 初始化搜索


    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            xListView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            SearchDevice();
        } else
            finish();
    }


    @Override
    public void callBack_SeachData(String Deviceinfo) {
        Log.d("zjm", Deviceinfo);
        System.out.println("收到信息" + Deviceinfo);
        boolean temp = false;
        try {
            JSONObject jsonObject = new JSONObject(Deviceinfo);
            IpcDevice device = new IpcDevice();
            device.setMac(jsonObject.getString("Mac"));
            device.setName(jsonObject.getString("DeviceName"));
            device.setDeviceid(jsonObject.getString("DeviceID"));
            device.setIp(jsonObject.getString("IP"));
            device.setPort(jsonObject.getInt("Port"));
            device.setAdd(false);
            if (list.size() == 0) {
                temp = false;
            } else {
                for (int i = 0; i < list.size(); i++) {
                    IpcDevice ipc = (IpcDevice) list.get(i);
                    if (ipc.getDeviceid().equals(device.getDeviceid())) {
                        temp = true;
                    }
                }
            }
            if (!temp) {
                list.add(device);
                handler.sendEmptyMessage(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        SearchDevice();
    }

    @Override
    public void onLoadMore() {

    }

}
