package com.example.ipcam.camer.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ipcam.camer.R;
import com.example.ipcam.camer.adpter.IpcAlarmListAdpter;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.XListView;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;


public class NewsLayerFragment extends LazyFragment implements
        XListView.IXListViewListener, OnClickListener {

    private XListView xListView;
    private IpcAlarmListAdpter adpter;
    private List<IpcDevice> list;



    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            xListView.stopRefresh();
            xListView.stopLoadMore();
            xListView.setRefreshTime(Util.getNowTime());

            if (msg.what == 0) {
                initView();
            }
        }
    };
    ;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.ipc_alarm_msg);
        xListView = (XListView) findViewById(R.id.alarm_listivew);
        xListView.setPullLoadEnable(false);
        xListView.setIXListViewListener(this);
        initView();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        initView();
    }

    @SuppressWarnings("unchecked")
    private void initView() {

        list = DeviceManager.getInstence(NewsLayerFragment.this.getActivity()).GetIPCListDevice();
        adpter = new IpcAlarmListAdpter(NewsLayerFragment.this.getActivity(), list, 1);
        xListView.setAdapter(adpter);

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onClick(View v) {

    }
}
