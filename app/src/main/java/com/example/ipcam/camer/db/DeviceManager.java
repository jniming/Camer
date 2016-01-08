package com.example.ipcam.camer.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ipcam.camer.entity.AlarmMsg;
import com.example.ipcam.camer.entity.IpcDevice;
import com.example.ipcam.camer.util.Util;

import java.util.ArrayList;
import java.util.List;


public class DeviceManager {

    private DeviceDb db;
    private SQLiteDatabase Readdatabase, wirtedatabase;
    private static DeviceManager deviceManager;

    public static DeviceManager getInstence(Context context) {
        if (deviceManager == null) {
            deviceManager = new DeviceManager(context);

        }
        return deviceManager;

    }

    public DeviceManager(Context context) {
        db = new DeviceDb(context);
        Readdatabase = db.getReadableDatabase();
        wirtedatabase = db.getWritableDatabase();
    }

    public List<IpcDevice> GetIPCListDevice() {
        Cursor cursor = Readdatabase.query(DeviceDb.tableIPCName, new String[]{
                        "device_deviceid", "device_adminname", "device_pwd", "device_name",
                        "device_userid", "device_pic", "device_status", "device_input"}, null, null,
                null, null, null);
        List<IpcDevice> list = new ArrayList<IpcDevice>();
        while (cursor.moveToNext()) {
            IpcDevice ipcDevice = new IpcDevice();
            String mac = cursor.getString(0);
            String adminName = cursor.getString(1);
            String pwd = cursor.getString(2);
            String name = cursor.getString(3);
            int device_userid = cursor.getInt(4);
            String pic = cursor.getString(5);
            int status = cursor.getInt(6);
            int input = cursor.getInt(7);
            ipcDevice.setDeviceid(mac);
            ipcDevice.setName(name);
            ipcDevice.setAdmin_name(adminName);
            ipcDevice.setPsw(pwd);
            ipcDevice.setUserid(device_userid);
            ipcDevice.setPic(pic);
            ipcDevice.setConnect_state(status);
            ipcDevice.setAlarmonline(input > 0 ? true : false);
            list.add(ipcDevice);
        }
        return list;
    }

    /**
     * 通过userid查找ipc设备
     *
     * @param userid
     * @return
     */
    public IpcDevice QueryDevice(long userid) {
        Cursor cursor = Readdatabase.query(DeviceDb.tableIPCName, new String[]{
                        "device_deviceid", "device_adminname", "device_pwd", "device_name",
                        "device_pic", "device_input"}, "device_userid=?",
                new String[]{userid + ""}, null, null, null);
        IpcDevice device = new IpcDevice();
        if (cursor.moveToFirst()) {
            String mac = cursor.getString(0);
            String adminName = cursor.getString(1);
            String pwd = cursor.getString(2);
            String name = cursor.getString(3);
            String pic = cursor.getString(4);
            int input = cursor.getInt(5);
            device.setDeviceid(mac);
            device.setName(name);
            device.setAdmin_name(adminName);
            device.setPsw(pwd);
            device.setUserid(userid);
            device.setPic(pic);
            device.setAlarmonline(input > 0 ? true : false);
            return device;
        }

        return null;

    }

    public IpcDevice Query_DeviceID_Device(String deviceid) {
        Cursor cursor = Readdatabase.query(DeviceDb.tableIPCName, new String[]{
                        "device_deviceid", "device_adminname", "device_pwd", "device_name",
                        "device_pic", "device_userid"}, "device_deviceid=?",
                new String[]{deviceid}, null, null, null);
        IpcDevice device = new IpcDevice();
        if (cursor.moveToFirst()) {
            String mac = cursor.getString(0);
            String adminName = cursor.getString(1);
            String pwd = cursor.getString(2);
            String name = cursor.getString(3);
            String pic = cursor.getString(4);
            long userid = cursor.getLong(5);
            device.setDeviceid(mac);
            device.setName(name);
            device.setAdmin_name(adminName);
            device.setPsw(pwd);
            device.setUserid(userid);
            device.setPic(pic);
            return device;
        }

        return null;

    }

    /**
     * 插入图片
     *
     * @param device
     * @return
     */
    public boolean updateIPC_name(IpcDevice device) {
        ContentValues values = new ContentValues();
        values.put("device_name", device.getName());
        long id = wirtedatabase.update(DeviceDb.tableIPCName, values,
                "device_userid=?", new String[]{device.getUserid() + ""});
        if (id > 0) {
            return true;
        }
        return false;
    }

    /**
     * 输入报警状态跟新
     *
     * @param device
     * @return
     */
    public boolean updateIPC_input(IpcDevice device) {
        ContentValues values = new ContentValues();
        values.put("device_input", device.isAlarmonline() ? 1 : 0);
        long id = wirtedatabase.update(DeviceDb.tableIPCName, values,
                "device_userid=?", new String[]{device.getUserid() + ""});
        if (id > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入图片
     *
     * @param device
     * @return
     */
    public boolean updateIPC_pic(IpcDevice device) {
        ContentValues values = new ContentValues();
        values.put("device_pic", device.getPic());
        long id = wirtedatabase.update(DeviceDb.tableIPCName, values,
                "device_userid=?", new String[]{device.getUserid() + ""});
        if (id > 0) {
            return true;
        }
        return false;
    }

    /**
     * 插入图片
     *
     * @param
     * @return
     */
    public boolean updateIPC_status(long userid, int status) {
        ContentValues values = new ContentValues();
        values.put("device_status", status);
        long id = wirtedatabase.update(DeviceDb.tableIPCName, values,
                "device_userid=?", new String[]{userid + ""});
        if (id > 0) {
            return true;
        }
        return false;
    }

    public boolean SaveIpcDevice(IpcDevice device) {
        ContentValues values = new ContentValues();
        values.put("device_name", device.getName());
        values.put("device_pwd", device.getPsw());
        values.put("device_deviceid", device.getDeviceid());
        values.put("device_userid", device.getUserid());
        values.put("device_pic", device.getPic());

        long id = wirtedatabase.insert(DeviceDb.tableIPCName, null, values);

        if (id > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存消息
     *
     * @param am
     * @return
     */
    public boolean SaveMsg(AlarmMsg am) {
        ContentValues values = new ContentValues();
        values.put("time", am.getTime());
        values.put("device_deviceid", am.getMac());
        values.put("msg", am.getMsg());
        values.put("filepath", am.getFilepath());
        values.put("msg_type", am.getInfotype());
        long id = wirtedatabase.insert(DeviceDb.tableMsg, null, values);
        if (id > 0) {
            return true;

        }

        return false;
    }

    /**
     * 根据msgid删除消息
     *
     * @param
     * @return
     */
    public boolean DelectMsg_msgid(int msgid) {
        List<AlarmMsg> list = QueryMsg_MsgId(msgid);  //查询所有的该userid下的消息
        for (AlarmMsg msg : list) {
            String uri = msg.getFilepath();
            Util.deleteFile(uri);
        }
        int index = wirtedatabase.delete(DeviceDb.tableMsg, "msg_id=?",
                new String[]{msgid + ""});
        if (index > 0) {
            return true;
        }

        return false;
    }

    /**
     * 根据deviceId删除消息
     *
     * @param
     * @return
     */
    public boolean DelectMsg(String devcieId) {
        List<AlarmMsg> list = QueryMsg(devcieId); //查询所有的该userid下的消息
        for (AlarmMsg msg : list) {
            String uri = msg.getFilepath();
            Util.deleteFile(uri);
        }
        int index = wirtedatabase.delete(DeviceDb.tableMsg, "device_deviceid=?",
                new String[]{devcieId});
        if (index > 0) {
            return true;
        }

        return false;
    }

    /**
     * 根据msgid查询报警信息
     *
     * @param msgId
     * @return
     */
    public List<AlarmMsg> QueryMsg_MsgId(int msgId) {
        List<AlarmMsg> alarmMsgs = new ArrayList<AlarmMsg>();
        Cursor cursor = Readdatabase.query(DeviceDb.tableMsg, new String[]{
                        "msg_id", "device_deviceid", "time", "msg", "filepath", "msg_type"},
                "msg_id=?", new String[]{msgId + ""}, null, null, null);
        while (cursor.moveToNext()) {
            AlarmMsg alarm = new AlarmMsg();
            int id = cursor.getInt(0);
            String mac1 = cursor.getString(1);
            String time = cursor.getString(2);
            String msg = cursor.getString(3);
            String file = cursor.getString(4);
            int type = cursor.getInt(5);
            alarm.setId(id);
            alarm.setMac(mac1);
            alarm.setTime(time);
            alarm.setMsg(msg);
            alarm.setFilepath(file);
            alarm.setInfotype(type);
            alarmMsgs.add(alarm);
        }
        return alarmMsgs;
    }


    /**
     * 查询消息
     *
     * @param
     * @return
     */
    public List<AlarmMsg> QueryMsg(String deviceId) {
        List<AlarmMsg> alarmMsgs = new ArrayList<AlarmMsg>();
        Cursor cursor = Readdatabase.query(DeviceDb.tableMsg, new String[]{
                        "msg_id", "device_deviceid", "time", "msg", "filepath", "msg_type"},
                "device_deviceid=?", new String[]{deviceId}, null, null, null);
        while (cursor.moveToNext()) {
            AlarmMsg alarm = new AlarmMsg();
            int id = cursor.getInt(0);
            String mac1 = cursor.getString(1);
            String time = cursor.getString(2);
            String msg = cursor.getString(3);
            String file = cursor.getString(4);
            int type = cursor.getInt(5);
            alarm.setId(id);
            alarm.setMac(mac1);
            alarm.setTime(time);
            alarm.setMsg(msg);
            alarm.setFilepath(file);
            alarm.setInfotype(type);
            alarmMsgs.add(alarm);
        }
        return alarmMsgs;
    }


    public int delectDevice(String uuid) {

        int index = wirtedatabase.delete(DeviceDb.tableIPCName, "device_deviceid=?",
                new String[]{uuid});
        return index;
    }


}
