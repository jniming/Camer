package com.example.ipcam.camer.adpter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcam.camer.IpcAlarmMsgActivity;
import com.example.ipcam.camer.IpcSettingsActivity;
import com.example.ipcam.camer.R;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Ttoast;
import com.example.ipcam.camer.util.Util;
import com.example.ipcam.camer.view.CustomDialog;
import com.example.ipcam.camer.view.MainDeviceMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;


public class IpcListAdpter extends BaseAdapter {
    private List<IpcDevice> list;
    private Context context;
    private Handler handler;

    public IpcListAdpter(Context context, List<IpcDevice> list, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.list = list;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Myview view;
        if (convertView == null) {
            view = new Myview();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.device_listview_item, null);
            view.name = (TextView) convertView
                    .findViewById(R.id.ipc_name);
            view.state = (TextView) convertView
                    .findViewById(R.id.ipc_status);
            view.ipcimg = (TextView) convertView.findViewById(R.id.ipc_state_img);
            view.img = (LinearLayout) convertView.findViewById(R.id.ipc_setting_tv);
            view.alarm_img = (TextView) convertView.findViewById(R.id.ipc_list_item_alarm_img);
            convertView.setTag(view);
        } else {
            view = (Myview) convertView.getTag();
        }
        final IpcDevice map = list.get(position);
        view.name.setText(map.getName());
        view.state.setText(Util.state(map.getConnect_state()));
        view.alarm_img.setVisibility(map.isAlarmonline() ? View.VISIBLE : View.GONE);
        if ("".equals(map.getPic())) {
            view.ipcimg.setBackgroundResource(R.drawable.ipcimg);
        } else {
            Bitmap bitmap = Util.decodeBitmap(map.getPic());
            if (bitmap == null) {
                view.ipcimg.setBackgroundResource(R.drawable.ipcimg);

            } else {
                BitmapDrawable bd = new BitmapDrawable(Util.decodeBitmap(map.getPic()));
                view.ipcimg.setBackground(bd);
            }
        }

        view.img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (map.getConnect_state() != 100) {
                    Ttoast.show(context, "设备必须在线");
                    return;
                } else {
                    MainDeviceMenu mainDeviceMenu = new MainDeviceMenu(context);
                    setListence(mainDeviceMenu, map, position);
                    mainDeviceMenu.show();


                }


            }
        });

        return convertView;
    }

    class Myview {
        TextView name, state, ipcimg, alarm_img;
        LinearLayout img;
    }

    public JSONObject setdata(boolean flg) {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("motion_armed", 0);
            localJSONObject.put("motion_sensitivity", 2);
            localJSONObject.put("iolinkage", 7);
            localJSONObject.put("iolinkage_level", 1);
            localJSONObject.put("alarmpresetsit", 0); // 预置位
            localJSONObject.put("mail", 0); // 邮件通知
            localJSONObject.put("record", 0); // 禁止录像
            localJSONObject.put("upload_interval", 1); // 报警上传数量
            localJSONObject.put("pirenable", 0);
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
                CustomDialog.Builder Builder = new CustomDialog.Builder(context);
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
                Intent intent = new Intent(context, IpcAlarmMsgActivity.class);
                intent.putExtra("device", device);
                context.startActivity(intent);
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
                    Toast.makeText(context, "布防成功", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(3);

                } else {
                    Toast.makeText(context, "布防失败", Toast.LENGTH_SHORT).show();
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
                    handler.sendEmptyMessage(3);
                    Toast.makeText(context, "撤防成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "撤防失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainDeviceMenu.SettingListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDeviceMenu.dismiss();
                Intent intet = new Intent(context, IpcSettingsActivity.class);
                intet.putExtra("device", device);
                context.startActivity(intet);
            }
        });
    }

    public void delect(IpcDevice device) {
        DeviceSDK.closeDevice(device.getUserid());
        DeviceSDK.destoryDevice(device.getUserid());
        int id = DeviceManager.getInstence(context).delectDevice(
                device.getDeviceid());
        if (id > 0) {
            Ttoast.show(context, "删除成功");
        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
