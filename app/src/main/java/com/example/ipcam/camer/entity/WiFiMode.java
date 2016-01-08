package com.example.ipcam.camer.entity;

public class WiFiMode {
	private int authtype;
	private int channel;
	private String dbm0;
	private String dbm1;
	private int defkey;
	private String did;
	private int enable;
	private int encryp;
	private String key1;
	private int key1_bits;
	private String key2;
	private int key2_bits;
	private String key3;
	private int key3_bits;
	private String key4;
	private int key4_bits;
	private int keyformat;
	private String mac;
	private int mode;
	private int security;
	private String ssid;
	private String wpa_psk;

	public int getAuthtype() {
		return this.authtype;
	}

	public int getChannel() {
		return this.channel;
	}

	public String getDbm0() {
		return this.dbm0;
	}

	public String getDbm1() {
		return this.dbm1;
	}

	public int getDefkey() {
		return this.defkey;
	}

	public String getDid() {
		return this.did;
	}

	public int getEnable() {
		return this.enable;
	}

	public int getEncryp() {
		return this.encryp;
	}

	public String getKey1() {
		return this.key1;
	}

	public int getKey1_bits() {
		return this.key1_bits;
	}

	public String getKey2() {
		return this.key2;
	}

	public int getKey2_bits() {
		return this.key2_bits;
	}

	public String getKey3() {
		return this.key3;
	}

	public int getKey3_bits() {
		return this.key3_bits;
	}

	public String getKey4() {
		return this.key4;
	}

	public int getKey4_bits() {
		return this.key4_bits;
	}

	public int getKeyformat() {
		return this.keyformat;
	}

	public String getMac() {
		return this.mac;
	}

	public int getMode() {
		return this.mode;
	}

	public int getSecurity() {
		return this.security;
	}

	public String getSsid() {
		return this.ssid;
	}

	public String getWpa_psk() {
		return this.wpa_psk;
	}

	public void setAuthtype(int paramInt) {
		this.authtype = paramInt;
	}

	public void setChannel(int paramInt) {
		this.channel = paramInt;
	}

	public void setDbm0(String paramString) {
		this.dbm0 = paramString;
	}

	public void setDbm1(String paramString) {
		this.dbm1 = paramString;
	}

	public void setDefkey(int paramInt) {
		this.defkey = paramInt;
	}

	public void setDid(String paramString) {
		this.did = paramString;
	}

	public void setEnable(int paramInt) {
		this.enable = paramInt;
	}

	public void setEncryp(int paramInt) {
		this.encryp = paramInt;
	}

	public void setKey1(String paramString) {
		this.key1 = paramString;
	}

	public void setKey1_bits(int paramInt) {
		this.key1_bits = paramInt;
	}

	public void setKey2(String paramString) {
		this.key2 = paramString;
	}

	public void setKey2_bits(int paramInt) {
		this.key2_bits = paramInt;
	}

	public void setKey3(String paramString) {
		this.key3 = paramString;
	}

	public void setKey3_bits(int paramInt) {
		this.key3_bits = paramInt;
	}

	public void setKey4(String paramString) {
		this.key4 = paramString;
	}

	public void setKey4_bits(int paramInt) {
		this.key4_bits = paramInt;
	}

	public void setKeyformat(int paramInt) {
		this.keyformat = paramInt;
	}

	public void setMac(String paramString) {
		this.mac = paramString;
	}

	public void setMode(int paramInt) {
		this.mode = paramInt;
	}

	public void setSecurity(int paramInt) {
		this.security = paramInt;
	}

	public void setSsid(String paramString) {
		this.ssid = paramString;
	}

	public void setWpa_psk(String paramString) {
		this.wpa_psk = paramString;
	}

	public String toString() {
		return "WifiBean [did=" + this.did + ", enable=" + this.enable
				+ ", ssid=" + this.ssid + ", channel=" + this.channel
				+ ", mode=" + this.mode + ", authtype=" + this.authtype
				+ ", encryp=" + this.encryp + ", keyformat=" + this.keyformat
				+ ", defkey=" + this.defkey + ", key1=" + this.key1 + ", key2="
				+ this.key2 + ", key3=" + this.key3 + ", key4=" + this.key4
				+ ", key1_bits=" + this.key1_bits + ", key2_bits="
				+ this.key2_bits + ", key3_bits=" + this.key3_bits
				+ ", key4_bits=" + this.key4_bits + ", wpa_psk=" + this.wpa_psk
				+ "]";
	}
}