package com.example.ipcam.camer.Listener;

public interface GraphicListener {
	void callBack_getParam(long UserID, long nType, String param);

	void callBack_setParam(long UserID, long nType, int nResult);
}
