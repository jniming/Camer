package com.example.ipcam.camer.entity;

/**
 * Created by Administrator on 2016/1/6.
 */
public class SdCardModel {

    private int record_audio;
    private int record_cover;
    private int sdcard_status;
    private int sdcard_totalsize;
    private int time_schedule_enable;
    private int record_time;

    public int getRecord_time() {
        return record_time;
    }

    public void setRecord_time(int record_time) {
        this.record_time = record_time;
    }

    public int getRecord_audio() {
        return this.record_audio;
    }

    public int getRecord_cover() {
        return this.record_cover;
    }

    public int getSdcard_status() {
        return this.sdcard_status;
    }

    public int getSdcard_totalsize() {
        return this.sdcard_totalsize;
    }

    public int getTime_schedule_enable() {
        return this.time_schedule_enable;
    }

    public void setRecord_audio(int paramInt) {
        this.record_audio = paramInt;
    }

    public void setRecord_cover(int paramInt) {
        this.record_cover = paramInt;
    }

    public void setSdcard_status(int paramInt) {
        this.sdcard_status = paramInt;
    }

    public void setSdcard_totalsize(int paramInt) {
        this.sdcard_totalsize = paramInt;
    }

    public void setTime_schedule_enable(int paramInt) {
        this.time_schedule_enable = paramInt;
    }
}
