package com.example.ipcam.camer.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.IpcAddDeviceActivity;
import com.example.ipcam.camer.IpcAlarmMsgActivity;
import com.example.ipcam.camer.IpcSettingsActivity;
import com.example.ipcam.camer.Listener.AlarmListener;
import com.example.ipcam.camer.Listener.DeviceStatusListener;
import com.example.ipcam.camer.Listener.PictureListener;
import com.example.ipcam.camer.PlayDeviceActivity;
import com.example.ipcam.camer.R;
import com.example.ipcam.camer.Service.BridgeService;
import com.example.ipcam.camer.adpter.IpcListAdpter;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Ttoast;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.CustomDialog;
import com.example.ipcam.camer.view.MainDeviceMenu;
import com.example.ipcam.camer.view.XListView;
import com.shizhefei.fragment.LazyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class DeviceLayerFragment extends LazyFragment implements
        OnClickListener, XListView.IXListViewListener, DeviceStatusListener, PictureListener, AlarmListener {
    private XListView listView;
    private List<IpcDevice> ipcList;
    private IpcListAdpter adpter;
    private TextView textView, right;
    private int refinshMsg = 102;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.stopRefresh();
            listView.stopLoadMore();
            listView.setRefreshTime(Util.getNowTime());
            if (msg.what == 0) {
                adpter = new IpcListAdpter(DeviceLayerFragment.this.getActivity(), ipcList, handler);
                listView.setAdapter(adpter);
            } else if (msg.what == 1) {
//                Reopen();
                initDevice();
            } else if (msg.what == 2) {
                initData();
            } else if (msg.what == refinshMsg) {
               refinshopen();
            } else if (msg.what == 3) {
                listView.requestLayout();
                adpter.notifyDataSetChanged();
            }
            if (ipcList.size() == 0) {
                textView.setVisibility(View.VISIBLE);
            } else
                textView.setVisibility(View.GONE);
        }

    };

    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(0);

    }


    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        handler.sendEmptyMessage(2);
    }

    @Override
    protected void onFragmentStopLazy() {
        super.onFragmentStopLazy();
        for (int i = 0; i < ipcList.size(); i++) {
            IpcDevice device = ipcList.get(i);
            DeviceManager.getInstence(DeviceLayerFragment.this.getActivity()).updateIPC_pic(device);
        }
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.devicelist);
        listView = (XListView) findViewById(R.id.devicelist_listview);
        textView = (TextView) findViewById(R.id.devicelist_msg);
        right = (TextView) findViewById(R.id.right_img);
        BridgeService.setDeviceStatusListener(this);
        BridgeService.setPictureListener(this);
        BridgeService.setAlarmListener(this);
        listView.setPullLoadEnable(false);
        listView.setIXListViewListener(this);
        right.setOnClickListener(this);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IpcDevice ipcDevice = (IpcDevice) ipcList.get(position - 1);
                if (ipcDevice.getConnect_state() != 100) {
                    Ttoast.show(DeviceLayerFragment.this.getActivity(), "设备必须在线!");
                    return;
                }
                Intent intent = new Intent(DeviceLayerFragment.this.getActivity(),
                        PlayDeviceActivity.class);
                intent.putExtra("userid", ipcDevice.getUserid());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                IpcDevice device = (IpcDevice) ipcList.get(position - 1);
                MainDeviceMenu mainDeviceMenu = new MainDeviceMenu(DeviceLayerFragment.this.getActivity());
                setListence(mainDeviceMenu, device, position - 1);
                mainDeviceMenu.show();
                return true;
            }
        });

        initData();
        handler.sendEmptyMessage(1);

    }

    public JSONObject setdata(boolean flg) {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("motion_armed", 0);
            localJSONObject.put("motion_sensitivity", 2);
            localJSONObject.put("iolinkage", 7);
            localJSONObject.put("iolinkage_level", 1);
            localJSONObject.put("alarmpresetsit", 0); // 预置位
            localJSONObject.put("mail", false); // 邮件通知
            localJSONObject.put("record", true); // 禁止录像
            localJSONObject.put("upload_interval", 1); // 报警上传数量
            localJSONObject.put("pirenable", false);
            if (flg) {
                localJSONObject.put("input_armed", 1);  //输入布防
            } else {
                localJSONObject.put("input_armed", 0);  //输入撤防

            }
            localJSONObject.put("ioin_level", 1); // 输入报警触发电平
            localJSONObject.put("alarm_audio", 0); // 声音报警

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

    public void setListence(final MainDeviceMenu mainDeviceMenu, final IpcDevice device, final int postion) {
        mainDeviceMenu.setdelectListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                CustomDialog.Builder Builder = new CustomDialog.Builder(DeviceLayerFragment.this.getActivity());
                Builder.setTitle("提示");
                Builder.setMessage("是否删除");
                Builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delect(device);
                        handler.sendEmptyMessage(2);

                    }
                });
                Builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                Builder.create().show();
            }
        });
        mainDeviceMenu.setConListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                DeviceSDK.closeDevice(device.getUserid());
                DeviceSDK.openDevice(device.getUserid());
            }
        });
        mainDeviceMenu.setAlarmListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                Intent intent = new Intent(DeviceLayerFragment.this.getActivity(), IpcAlarmMsgActivity.class);
                intent.putExtra("device", device);
                startActivity(intent);
            }
        });
        mainDeviceMenu.setKsbfListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                JSONObject object = setdata(true);
                int flg = DeviceSDK.setDeviceParam(device.getUserid(),
                        Util.SET_ALARM, object.toString());
                if (flg > 0) { // 设置成功
                    device.setAlarmonline(true);
                    ipcList.set(postion, device);
                    handler.sendEmptyMessage(3);
                    Toast.makeText(DeviceLayerFragment.this.getActivity(), "布防成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DeviceLayerFragment.this.getActivity(), "布防失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainDeviceMenu.setkscfListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                JSONObject object = setdata(false);
                int flg = DeviceSDK.setDeviceParam(device.getUserid(),
                        Util.SET_ALARM, object.toString());
                if (flg > 0) { // 设置成功
                    device.setAlarmonline(false);
                    ipcList.set(postion, device);
                    handler.sendEmptyMessage(3);
                    Toast.makeText(DeviceLayerFragment.this.getActivity(), "撤防成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DeviceLayerFragment.this.getActivity(), "撤防失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mainDeviceMenu.SettingListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                Intent intet = new Intent(DeviceLayerFragment.this.getActivity(), IpcSettingsActivity.class);
                intet.putExtra("device", device);
                startActivity(intet);
            }
        });
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        for (IpcDevice device : ipcList) {
            DeviceManager.getInstence(DeviceLayerFragment.this.getActivity()).updateIPC_input(device);
        }

    }

    public void delect(IpcDevice device) {
        DeviceSDK.closeDevice(device.getUserid());
        DeviceSDK.destoryDevice(device.getUserid());
        int id = DeviceManager.getInstence(DeviceLayerFragment.this.getActivity()).delectDevice(
                device.getDeviceid());
        DeviceManager.getInstence(DeviceLayerFragment.this.getActivity()).DelectMsg(device.getDeviceid());

        if (id > 0) {
            Toast.makeText(DeviceLayerFragment.this.getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(DeviceLayerFragment.this.getActivity(), "删除失败", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void initData() {
        ipcList = DeviceManager.getInstence(DeviceLayerFragment.this.getActivity()).GetIPCListDevice();
        adpter = new IpcListAdpter(DeviceLayerFragment.this.getActivity(), ipcList, handler);
        listView.setAdapter(adpter);
    }

    @Override
    public void CallBack_RecordPicture(long userid, byte[] buff, int len) {
        // TODO Auto-generated method stub
        String imgstr = "";
        try {
            imgstr = new String(buff, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        if (ipcList != null) {
            for (IpcDevice l : ipcList) {
                if (l.getUserid() == userid) {
                    l.setPic(imgstr);
                }
            }
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void receiveDeviceStatus(long userid, int status) {
        Log.d("zjm", "初始化摄像头信息--" + "userid-" + userid + "status-" + status);
        // TODO Auto-generated method stub
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (ipcList != null) {
            for (IpcDevice id : ipcList) {
                long uid = id.getUserid();
                if (uid == userid) {
                    id.setConnect_state(status);
                    handler.sendEmptyMessage(0);
                }
            }

        }
    }

    //初始化程序
    public void initDevice() {
        for (IpcDevice dvice : ipcList) {
            long userid = DeviceSDK.createDevice("admin", "", "", 0,
                    dvice.getDeviceid(), 1);
            int temp = DeviceSDK.openDevice(userid);
            dvice.setUserid(userid);
        }

    }

    public void refinshopen() {
        for (IpcDevice dvice : ipcList) {
            DeviceSDK.closeDevice(dvice.getUserid());
            DeviceSDK.openDevice(dvice.getUserid());
        }
    }

    /**
     * 初始化摄像头
     */
    public void Reopen() {
        for (IpcDevice dvice : ipcList) {
            if (dvice.getConnect_state() != 100) {
                DeviceSDK.closeDevice(dvice.getUserid());
                DeviceSDK.openDevice(dvice.getUserid());
            }
        }

    }

    @Override
    public void onRefresh() {

        handler.sendEmptyMessage(refinshMsg);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == right.getId()) {
            Intent intent = new Intent(DeviceLayerFragment.this.getActivity(), IpcAddDeviceActivity.class);
            startActivityForResult(intent, 5);

        }
    }

    @Override
    public void callBack_getParam(long UserID, long nType, String param) {

        Log.d("zjm", "报警参数回调");

        try {
            JSONObject json = new JSONObject(param);
            boolean armed = json.getInt("input_armed") > 0 ? true
                    : false;
            for (IpcDevice id : ipcList) {
                long uid = id.getUserid();
                if (uid == UserID) {
                    id.setAlarmonline(armed);
                    handler.sendEmptyMessage(0);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callBack_setParam(long UserID, long nType, int nResult) {


    }
}
