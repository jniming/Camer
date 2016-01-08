package com.example.ipcam.camer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Util {

    public static boolean network = false;

    public static int TIMEOUT_WHAT = 5; // 设置用户信息

    public static int SET_USER = 0x2002; // 设置用户信息
    public static int GET_USER = 0x2003; // 获取用户信息
    public static int SET_NAME = 0x2702; // 设置设备别名
    public static int GET_AP = 0x2703; // 获取用户信息
    public static int SET_AP = 0x2704; // 设置用户信息
    public static int SET_FTP = 0x2006; // 设置FTP信息
    public static int GET_FTP = 0x2007; // 获取FTP信息
    public static int GET_ALARM = 0x2018; // 获取报警信息
    public static int SET_ALARM = 0x2017; // 设置报警信息

    public static int SET_TIME = 0x2015; // 设置时钟信息
    public static int GET_TIME = 0x2016; // 获取时钟信息

    public static int SET_WIFI = 0x2012; // 设置wifi信息
    public static int GET_WIFI = 0x2013; // 获取wifi

    public static int SET_EMAIL = 0x2008; // 设置email信息
    public static int GET_EMAIL = 0x2009; // 获取email

    public static int SET_PARAM_SDFORMAT = 0x2024;  //格式化sd卡
    public static int GET_PARAM_RECORDSCH = 0x2021;  //获取tf参数
    public static int SET_PARAM_RECORDSCH = 0x2022;  //设置tf参数


    public static void deleteFile(String uri) {
        File dirFile = new File(uri);
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        dirFile.delete();
    }


    public static String getString(Context context, int id) {
        String str = "";
        str = context.getResources().getString(id);
        return str;
    }

    public static Bitmap decodeBitmap(String buff) {
        byte[] buf = null;
        if (buff != null) {
            try {
                buf = buff.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("转回数组-->" + buf);
            Bitmap bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length);
            return bitmap;
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Save Bitmap to a file.保存图片到SD卡。
     *
     * @param bitmap
     * @param
     * @return error message if the saving is failed. null if the saving is
     * successful.
     * @throws IOException
     */
    public static void saveBitmapToFile(Bitmap bitmap, String _file)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(_file);
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("zjm", e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 设置listview高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        int id) {
        // 获取ListView对应的Adapter
        Adapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    public static List<String> GetImgList(String path, String type) {
        List<String> imagePathArray = new ArrayList<String>();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file != null) {

            for (int i = 0; i < file.length; i++) {
                if (file[i].isFile()) {
                    String icon = file[i].getAbsolutePath();
                    if (icon.endsWith(type))
                        imagePathArray.add(icon);
                    System.out.println(icon);
                }
            }
        }
        return imagePathArray;
    }

    public static String state(int state) {
        String str = "";
        switch (state) {
            case 100:
                str = "在线";
                break;
            case 11:
                str = "离线";
                break;
            case 1:
                str = "账号错误";
                break;

            default:
                str = "连接中";
                break;
        }
        return str;
    }


    /**
     * 根据警报类型获取报警信息
     *
     * @param nType
     * @return
     */
    public static String GetAlarmMsg(int nType) {
        String msg = "";
        switch (nType) {
            case 0x20:
                msg = "移动侦测警报";
                break;
            case 0x21:
                msg = "GPIO警报";
                break;
            case 0x24:
                msg = "声音警报";
                break;
            case 0x25:
                msg = "红外警报";
                break;
            case 0x6:
                msg = "门磁警报";
                break;
            case 0x27:
                msg = "烟感警报";
                break;
            case 0x28:
                msg = "红外对射警报";
                break;
            case 0x29:
                msg = "门铃按下";
                break;

            default:
                msg = "未知报警";
                break;
        }

        return msg;

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        return time;

    }

    public static String GetEorrMsg(int eorrid) {
        String msg = "";
        switch (eorrid) {
            case 3811:
                msg = "设备已绑定";
                break;
            case 3504:
                msg = "密码错误";
                break;
            case 3501:
                msg = "账号不存在";
                break;
            case 3503:
                msg = "账号不合法";
                break;
            case 3506:
                msg = "验证码已失效";
                break;
            case 3508:
                msg = "手机不合法";
                break;
            case 3505:
                msg = "验证码错误";
                break;
            case 3817:
                msg = "分享码已过期";
                break;
            case 3815:
                msg = "分享码不存在";
                break;
            case 3816:
                msg = "分享码不合法";
                break;

            default:
                msg = "未知错误" + eorrid;
                break;
        }
        return msg;

    }
}
