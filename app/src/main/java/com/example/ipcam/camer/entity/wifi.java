package com.example.ipcam.camer.entity;

public class wifi {
	private int channel;
	private int dbm0;
	private String mode;
	private String ssid;
	private String mac;
	private int security;
private int dbm1;

	public int getDbm1() {
	return dbm1;
}

public void setDbm1(int dbm1) {
	this.dbm1 = dbm1;
}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getDbm0() {
		return dbm0;
	}

	public void setDbm0(int dbm0) {
		this.dbm0 = dbm0;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getSecurity() {
		return security;
	}

	public void setSecurity(int security) {
		this.security = security;
	}



	public wifi(int channel, int dbm0, String mode, String ssid, String mac,
			int security, int dbm1) {
		super();
		this.channel = channel;
		this.dbm0 = dbm0;
		this.mode = mode;
		this.ssid = ssid;
		this.mac = mac;
		this.security = security;
		this.dbm1 = dbm1;
	}

	public wifi() {
		// TODO Auto-generated constructor stub
	}
}