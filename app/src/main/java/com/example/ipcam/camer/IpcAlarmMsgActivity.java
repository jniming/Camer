package com.example.ipcam.camer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ipcam.camer.adpter.AlarmMsgAdpter;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.AlarmMsg;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.AlarmSortComparator;
import com.example.ipcam.camer.util.Config;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.CustomDialog;
import com.example.ipcam.camer.view.XListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 消息中摄像头列表
 *
 * @author Administrator
 */
public class IpcAlarmMsgActivity extends BaseActivity implements
        XListView.IXListViewListener {
    private XListView listView;
    private List<AlarmMsg> list = null;
    private AlarmMsgAdpter adpter;
    private IpcDevice ipcDevice;
    private PopupWindow menupop;

    private TextView text;
    private CustomDialog.Builder dialog;

    private List<AlarmMsg> alarmlist = new ArrayList<AlarmMsg>();
    private boolean gl_msg = true;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (list.size() == 0) {
                text.setVisibility(View.VISIBLE);
            } else {
                text.setVisibility(View.GONE);
            }
            if (msg.what == 1) {
                initView();
            } else if (msg.what == 2) {
                listView.stopRefresh();
                listView.stopLoadMore();
                listView.setRefreshTime(Util.getNowTime());
                initView();
            } else if (msg.what == 0) {
                adpter.notifyDataSetChanged();
            } else if (msg.what == 3) {
                if (gl_msg) {
                    adpter.setIsvisb(true);
                    setNaView(R.drawable.back, "", 0, "", 0, "取消", 0, "");
                    gl_msg = false;
                    menupop.showAtLocation(listView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                } else {
                    adpter.setIsvisb(false);
                    gl_msg = true;
                    setNaView(R.drawable.back, "", 0, "", 0, "管理", 0, "");
                    menupop.dismiss();
                }
                adpter.notifyDataSetChanged();

            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_alarm_list_msg);
        setTitle("消息列表");
        setNaView(R.drawable.back, "", 0, "", 0, "管理", 0, "");
        listView = (XListView) findViewById(R.id.alarm_listivew);
        text = (TextView) findViewById(R.id.reminder_text);
        listView.setPullLoadEnable(false);
        listView.setIXListViewListener(this);
        menuPop();
        initView();

        dialog = new CustomDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除所选中项目?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (AlarmMsg msg : alarmlist) {
                    if (msg.ischeck()) {
                        DeviceManager.getInstence(IpcAlarmMsgActivity.this).DelectMsg_msgid(msg.getId());
                    }
                }
                handler.sendEmptyMessage(1);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

    }

    @SuppressWarnings("unchecked")
    private void initView() {
        alarmlist.clear();

        ipcDevice = (IpcDevice) getIntent().getSerializableExtra("device");
        list = DeviceManager.getInstence(this).QueryMsg(ipcDevice.getDeviceid());


        for (int i = 0; i < list.size(); i++) {
            AlarmMsg device = list.get(i);
            String name = DeviceManager.getInstence(this).Query_DeviceID_Device(device.getMac()).getName();
            device.setName(name);
            if (device.getInfotype() == Config.alarm_msg)
                alarmlist.add(device);
        }
        if (alarmlist != null) {
            AlarmSortComparator comparator = new AlarmSortComparator();
            Collections.sort(alarmlist, comparator);
        }


        if (list.size() == 0) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
        }

        adpter = new AlarmMsgAdpter(alarmlist, this);
        listView.setAdapter(adpter);

    }

    public void menuPop() {

        View view = LayoutInflater.from(this).inflate(R.layout.msg_menu_pop, null);

        LinearLayout delect_lt = (LinearLayout) view.findViewById(R.id.delect_layout);
        LinearLayout all = (LinearLayout) view.findViewById(R.id.all_layout);


        final TextView delect = (TextView) view.findViewById(R.id.delect);
        TextView select_all = (TextView) view.findViewById(R.id.all_select);
        menupop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        delect_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create().show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AlarmMsg msg : alarmlist) {
                    msg.setIscheck(true);
                }
                handler.sendEmptyMessage(0);
            }
        });


    }


    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            handler.sendEmptyMessage(3);

        } else
            back();
    }

}
