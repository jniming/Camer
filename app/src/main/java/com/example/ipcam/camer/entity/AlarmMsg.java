package com.example.ipcam.camer.entity;

public class AlarmMsg {

    private int id;
    private String time;
    private String mac;
    private String msg;
    private String filepath;
    private int infotype = 0;   //100是警报,101是图片抓怕,102是录像
    private String name;
    private boolean ischeck;    //是否选中

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInfotype() {
        return infotype;
    }

    public void setInfotype(int infotype) {
        this.infotype = infotype;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AlarmMsg(int id, String msg, String time, String mac, String filepath, int infotype) {
        this.id = id;
        this.msg = msg;
        this.time = time;
        this.mac = mac;
        this.filepath = filepath;
        this.infotype = infotype;
    }

    public AlarmMsg() {
        // TODO Auto-generated constructor stub
    }
}
