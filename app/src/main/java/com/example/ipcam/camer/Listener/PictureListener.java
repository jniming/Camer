package com.example.ipcam.camer.Listener;

/**
 * 图片抓拍
 * 
 * @author Administrator
 *
 */
public interface PictureListener {

	public void CallBack_RecordPicture(long userid, byte[] buff, int len);

}
