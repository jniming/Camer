/**
 *
 */
package com.example.ipcam.camer.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.ipcam.camer.Callback.InitCallBack;
import com.example.ipcam.camer.DeviceAlarmActivity;
import com.example.ipcam.camer.Listener.APListener;
import com.example.ipcam.camer.Listener.AlarmInformationMsg;
import com.example.ipcam.camer.Listener.AlarmListener;
import com.example.ipcam.camer.Listener.DateListener;
import com.example.ipcam.camer.Listener.DeviceStatusListener;
import com.example.ipcam.camer.Listener.EmailListenner;
import com.example.ipcam.camer.Listener.FTPListener;
import com.example.ipcam.camer.Listener.GraphicListener;
import com.example.ipcam.camer.Listener.IpcWiFiListListener;
import com.example.ipcam.camer.Listener.NameListener;
import com.example.ipcam.camer.Listener.PictureListener;
import com.example.ipcam.camer.Listener.PlayListener;
import com.example.ipcam.camer.Listener.RecorderListener;
import com.example.ipcam.camer.Listener.SdkListener;
import com.example.ipcam.camer.Listener.SeachListener;
import com.example.ipcam.camer.Listener.SettingsListener;
import com.example.ipcam.camer.Listener.UserListener;
import com.example.ipcam.camer.Listener.WiFiListener;
import com.example.ipcam.camer.db.DeviceManager;
import com.example.ipcam.camer.util.Config;

import hsl.p2pipcam.nativecaller.DeviceSDK;


/**
 * @author wang.jingui
 */
public class BridgeService extends Service {
    // 监听回调接口
    private static PlayListener playListener;
    private static DeviceStatusListener deviceStatusListener;
    private static RecorderListener recorderListener;
    private static SettingsListener settingsListener;
    private static SeachListener seachlistenner;
    private static DateListener datelistener;
    private static WiFiListener wifilistener;
    private static FTPListener ftplistener;
    private static NameListener namelistener;
    private static APListener aplistener;
    private static AlarmListener alarmlistener;
    private static UserListener userlistener;
    private static PictureListener picturelistener;
    private static GraphicListener graphiclistener;
    private static AlarmInformationMsg informationMsg; // 报警信息回调
    private static IpcWiFiListListener ipcWiFiListListener;
    private static EmailListenner emailListenner;
    private static SdkListener sdkListener;
    private Thread initThtread;
    public static InitCallBack callbacke;
    public static boolean isBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initThtread = new Thread(new Runnable() {

            @Override
            public void run() {

                DeviceSDK.initialize("");
                DeviceSDK.setCallback(BridgeService.this);
                DeviceSDK.networkDetect();
                DeviceSDK.setSearchCallback(BridgeService.this);
            }
        });
        initThtread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DeviceSDK.unInitialize();
        initThtread = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void setDeviceStatusListener(
            DeviceStatusListener deviceStatusListener) {
        BridgeService.deviceStatusListener = deviceStatusListener;
    }

    public static void setAlarmInformationMsg(
            AlarmInformationMsg deviceStatusListener) {
        BridgeService.informationMsg = deviceStatusListener;
    }

    public static void setInitCallBack(InitCallBack callbacke) {
        BridgeService.callbacke = callbacke;
    }

    // 设置时间设置的监听
    public static void setDateListener(DateListener dateListener) {
        BridgeService.datelistener = dateListener;
    }

    public static void setIpcWifiLinstener(IpcWiFiListListener dateListener) {
        BridgeService.ipcWiFiListListener = dateListener;
    }

    public static void setSdkListener(SdkListener sdkListener) {
        BridgeService.sdkListener = sdkListener;
    }

    public static void setEmailLinstener(EmailListenner emailListenner) {
        BridgeService.emailListenner = emailListenner;
    }

    public static void setWifiListener(WiFiListener wifilistener) {
        BridgeService.wifilistener = wifilistener;
    }

    public static void setAlarmListener(AlarmListener alarmilistener) {
        BridgeService.alarmlistener = alarmilistener;
    }

    public static void setPlayListener(PlayListener playListener) {
        BridgeService.playListener = playListener;
    }

    public static void setNameListener(NameListener namelistener) {
        BridgeService.namelistener = namelistener;
    }

    public static void setUserListener(UserListener userlistener) {
        BridgeService.userlistener = userlistener;
    }

    public static void setFTPListener(FTPListener ftpListener) {
        BridgeService.ftplistener = ftpListener;
    }

    public static void setAPListener(APListener apListener) {
        BridgeService.aplistener = apListener;
    }

    public void setRecorderListener(RecorderListener recorderListener) {
        BridgeService.recorderListener = recorderListener;
    }

    public static void setSearchListener(SeachListener seachlistenner) {
        BridgeService.seachlistenner = seachlistenner;
    }

    public static void setSettingsListener(SettingsListener settingsListener) {
        BridgeService.settingsListener = settingsListener;
    }

    public static void setGraphicListener(GraphicListener graphiclistener) {
        BridgeService.graphiclistener = graphiclistener;
    }

    public static void setPictureListener(PictureListener picturelistener) {
        BridgeService.picturelistener = picturelistener;
    }

    // -------------------------------------------------------------------------
    // ---------------------------以下是JNI层回调的接口-------------------------------
    // -------------------------------------------------------------------------

    public void CallBack_SnapShot(long UserID, byte[] buff, int len) {
        System.out.println("CallBack_SnapShot回调");
        if (picturelistener != null) {
            picturelistener.CallBack_RecordPicture(UserID, buff, len);
        }

    }

    public void CallBack_GetParam(long UserID, long nType, String param) {
        // if (settingsListener != null)
        // settingsListener.callBack_getParam(UserID, nType, param);
        switch (new Long(nType).intValue()) {
            case 0x2016:
                if (datelistener != null)
                    datelistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2013:
                if (wifilistener != null)
                    wifilistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2014:
                if (ipcWiFiListListener != null)
                    ipcWiFiListListener.callBack_getWIFI(UserID, nType, param);
                break;
            case 0x2007:
                if (ftplistener != null)
                    ftplistener.callBack_getParam(UserID, nType, param);
                break;

            case 0x2703:
                if (aplistener != null)
                    aplistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2018:
                if (alarmlistener != null)
                    alarmlistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2003:
                if (userlistener != null)
                    userlistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2025:
                if (graphiclistener != null)
                    graphiclistener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2021:
                if (sdkListener != null)
                    sdkListener.callBack_getParam(UserID, nType, param);
                break;
            case 0x2009:
                if (emailListenner != null)
                    emailListenner.callBack_getParam(UserID, nType, param);
                break;
            default:
                break;
        }

    }

    public void CallBack_SetParam(long UserID, long nType, int nResult) {
        // if (settingsListener != null)
        // settingsListener.callBack_setParam(UserID, nType, nResult);
        switch (new Long(nType).intValue()) {
            case 0x2015:
                if (datelistener != null)
                    datelistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2014:
                if (wifilistener != null)
                    wifilistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2006:
                if (ftplistener != null)
                    ftplistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2704:
                if (aplistener != null)
                    aplistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2017:
                if (alarmlistener != null)
                    alarmlistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2002:
                if (userlistener != null)
                    userlistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2026:
                if (graphiclistener != null)
                    graphiclistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2702:
                if (namelistener != null)
                    namelistener.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2008:
                if (emailListenner != null)
                    emailListenner.callBack_setParam(UserID, nType, nResult);
                break;
            case 0x2022:
                if (sdkListener != null)
                    sdkListener.callBack_setParam(UserID, nType, nResult);
                break;
            default:
                break;
        }
    }

    public void CallBack_Event(long UserID, long nType) {
        int status = new Long(nType).intValue();
        Log.d("zjm", "服务中初始化摄像头的状态--" + status);
        if (deviceStatusListener != null)
            deviceStatusListener.receiveDeviceStatus(UserID, status);

        DeviceManager.getInstence(this).updateIPC_status(UserID, status);

    }

    public void VideoData(long UserID, byte[] VideoBuf, int h264Data, int nLen,
                          int Width, int Height, int time) {

    }

    public void callBackAudioData(long nUserID, byte[] pcm, int size) {
        if (playListener != null)
            playListener.callBackAudioData(nUserID, pcm, size);
        if (recorderListener != null)
            recorderListener.callBackAudioData(nUserID, pcm, size);
    }

    /**
     * 录像查询
     *
     * @param UserID
     * @param filecount
     * @param fname
     * @param strDate
     * @param size
     */
    public void CallBack_RecordFileList(long UserID, int filecount,
                                        String fname, String strDate, int size) {
        if (settingsListener != null)
            settingsListener.recordFileList(UserID, filecount, fname, strDate,
                    size);

    }

    public void CallBack_RecordFileListV2(long UserID, String param) {

    }

    public void CallBack_SearchDevice(String DeviceInfo) {
        seachlistenner.callBack_SeachData(DeviceInfo);
    }

    public void CallBack_P2PMode(long UserID, int nType) {
    }

    public void CallBack_RecordPicture(long userid, byte[] buff, int len) {
        System.out.println("手动抓拍后回调--->");

    }

    public void CallBack_RecordPlayPos(long userid, int pos) {
    }

    /**
     * 录像回调
     *
     * @param nUserID
     * @param data
     * @param type
     * @param size
     */
    public void CallBack_VideoData(long nUserID, byte[] data, int type, int size) {
    }

    public void CallBack_AlarmMessage(long UserID, int nType) {
        // informationMsg.alarmMgs(UserID, nType);
        try {
            showNotification(UserID, nType);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showNotification(long userid, int nType) throws InterruptedException {
        DeviceSDK.getDeviceParam(userid, 0x270E);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, DeviceAlarmActivity.class);
        intent.putExtra("userid", userid);
        intent.putExtra("nType", nType);
        intent.putExtra("pic", "");
        intent.putExtra("type", Config.alarm_msg);
        boolean flg = DeviceAlarmActivity.isvis;
        if (!flg) {
            startActivity(intent);

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
