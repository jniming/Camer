package com.example.ipcam.camer.entity;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.util.Config;
import com.example.ipcam.camer.util.FileHelper;
import com.example.ipcam.camer.util.Util;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import hsl.p2pipcam.nativecaller.DeviceSDK;


/**
 * 设备信息
 *
 * @author Administrator
 */
public class IpcDevice implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String ip;
    private String mac; // 设备物理地址
    private String deviceid; // 设备id ....这个才是出厂的id
    private String name; // 设备名称
    private int devicetype; // 设备类型
    private int port = 0; // 端口
    private int smartconnect = 0;
    private long userid; // 连接后返回的userid
    private int connect_state; // 连接状态
    private String psw;
    private String admin_name;
    private Bitmap bitmap;
    private String pic;
    private boolean add;   //是否已添加
    private boolean alarmonline = false;   //是否布防

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isAlarmonline() {
        return alarmonline;
    }

    public void setAlarmonline(boolean alarmonline) {
        this.alarmonline = alarmonline;
    }

    /**
     * 参数二是分辨率
     *
     * @param userid
     * @param subStreamId
     */
    public static void startPlayStream(long userid, int subStreamId) {
        DeviceSDK.startPlayStream(userid, 0, subStreamId);

    }

    /**
     * 参数二是分辨率
     *
     * @param userid
     * @param subStreamId
     */
    public static void stopPlayStream(long userid) {
        DeviceSDK.stopPlayStream(userid);

    }

    public void reopenDevice() {
        try {
            DeviceSDK.closeDevice(this.userid);
            Thread.sleep(200);
            DeviceSDK.openDevice(this.userid);
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void capturePicture(Context paramContext, long userid) {
        String str1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + ".jpg";
        File localFile = new File(FileHelper.IMAGE_PATH);
        if (!localFile.exists())
            localFile.mkdirs();
        String str2 = FileHelper.IMAGE_PATH + "/" + "_" + str1;
        DeviceSDK.GetCapturePicture(userid, str2);
        IpcDevice mac = DeviceManager.getInstence(paramContext).QueryDevice(
                userid);
        AlarmMsg am = new AlarmMsg(0, Config.CUP_IMGTYPE, Util.getNowTime(), mac.getDeviceid(),
                str2, Config.alarm_msg);
        DeviceManager.getInstence(paramContext).SaveMsg(am);
    }

    public static int StartRecord(Context paramContext, long userid, int width,
                                  int height, int framerate) {
        String str1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + ".mp4";
        File localFile = new File(FileHelper.AUDIO_PATH);
        if (!localFile.exists())
            localFile.mkdirs();
        String str2 = FileHelper.AUDIO_PATH + "/" + "_" + str1;
        IpcDevice mac = DeviceManager.getInstence(paramContext).QueryDevice(
                userid);
        AlarmMsg am = new AlarmMsg(0, Config.VIDE_TYPE, Util.getNowTime(), mac.getDeviceid(),
                str2, Config.audio_msg);
        DeviceManager.getInstence(paramContext).SaveMsg(am);
        return DeviceSDK.StartRecord(userid, str2, width, height, framerate);
    }

    public static int StopRecord(long userid) {
        return DeviceSDK.stopRecord(userid);
    }

    public IpcDevice() {
        // TODO Auto-generated constructor stub
    }

    public String getPsw() {
        return psw;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getConnect_state() {
        return connect_state;
    }

    public void setConnect_state(int connect_state) {
        this.connect_state = connect_state;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public IpcDevice(String ip, String mac, String deviceid, String name,
                     int devicetype, int port, int smartconnect, long userid) {
        super();
        this.ip = ip;
        this.mac = mac;
        this.deviceid = deviceid;
        this.name = name;
        this.devicetype = devicetype;
        this.port = port;
        this.smartconnect = smartconnect;
        this.userid = userid;
    }

    public IpcDevice(String ip, String mac, String deviceid, String name,
                     int devicetype, int port, int smartconnect, long userid,
                     int connect_state, String psw, String admin_name) {
        super();
        this.ip = ip;
        this.mac = mac;
        this.deviceid = deviceid;
        this.name = name;
        this.devicetype = devicetype;
        this.port = port;
        this.smartconnect = smartconnect;
        this.userid = userid;
        this.connect_state = connect_state;
        this.psw = psw;
        this.admin_name = admin_name;
    }

    public IpcDevice(String ip, String mac, String deviceid, String name,
                     int devicetype, int port, int smartconnect, long userid,
                     int connect_state, String psw) {
        super();
        this.ip = ip;
        this.mac = mac;
        this.deviceid = deviceid;
        this.name = name;
        this.devicetype = devicetype;
        this.port = port;
        this.smartconnect = smartconnect;
        this.userid = userid;
        this.connect_state = connect_state;
        this.psw = psw;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(int devicetype) {
        this.devicetype = devicetype;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSmartconnect() {
        return smartconnect;
    }

    public void setSmartconnect(int smartconnect) {
        this.smartconnect = smartconnect;
    }

}
