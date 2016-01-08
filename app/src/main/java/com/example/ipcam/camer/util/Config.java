package com.example.ipcam.camer.util;

import android.os.Environment;

public class Config {
    public static int audio_msg = 100;
    public static int alarm_msg = 101;

    public static int leakageProtection = 1000; // 漏保
    public static int splitter = 1001; // 分路

    public static String SUBDOMAIN = "kqt"; // 网关子域
    public static String SERVICENAME = ""; // 服务名
    public static String MajorDomain = "allen"; // 主域
    public static int MajorDomainId = 380; // 主域id
    public static String LIGHT_SUBDOMAIN = "dimmlight"; // 子设备

    public static int KEY = 2;
    public static int VALUE_OPEN = 1;
    public static int VALUE_CLOSE = 0;
    public static int SHUNT_CODE = 200;

    public static int IPC_TYPE = 2; // 摄像头
    public static int BOX_TYPE = 1; // 电箱

    public static String BOX_D = "boxdevice"; // 电箱对象传递

    public static String CUP_IMGTYPE = "图片抓拍";
    public static String VIDE_TYPE = "手动录像";


    public static String MODE_TYPE_POWER = "power";   //开关型设备


    public static String IPCIMG_PATH = Environment
            .getExternalStorageDirectory().getParent() + "/ipcimg/";

    /**
     * 通过子域id获取子域名
     *
     * @param subdomainId
     * @return
     */
    public static String GetsubDomain(long subdomainId) {
        if (subdomainId != 0) {

            int id = (int) subdomainId;
            String subdomain = "";

            switch (id) {
                case 381:
                    subdomain = "111";
                    break;
                case 460:
                    subdomain = "kqt";
                    break;
                case 593:
                    subdomain = "dimmlight";
                    break;
                case 610:
                    subdomain = "test";
                    break;

                default:
                    break;
            }
            return subdomain;
        }
        return "";
    }

}
