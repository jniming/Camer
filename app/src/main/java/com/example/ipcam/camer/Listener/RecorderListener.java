/**
 * 
 */
package com.example.ipcam.camer.Listener;

/**
 * @author Administrator
 *
 */
public interface RecorderListener
{
	public void callBack_RecordPlayPos(long userid, int pos);
	public void callBackAudioData(long userID, byte[] pcm, int size);
}
