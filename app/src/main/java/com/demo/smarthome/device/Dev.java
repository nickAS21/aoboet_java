package com.demo.smarthome.device;

/* loaded from: classes.dex */
public class Dev {
    private boolean dataChange;
    private String id;
    private int lampBVal;
    private int lampGVal;
    private int lampRVal;
    private String nickName = "";
    private String lastUpdate = "";
    private String torken = "";
    private String ipPort = "";
    private String pass = "";
    private int errorCount = 0;
    private final int ERROR_COUNT_MAX = 180;
    private double temp = 0.0d;
    private boolean lightState = false;
    private boolean onLine = false;

    public void runStep() {
        int i = this.errorCount;
        if (i >= 180) {
            this.onLine = false;
        } else {
            this.errorCount = i + 1;
        }
    }

    public boolean isDataChange() {
        return this.dataChange;
    }

    public void isDataChange(boolean z) {
        this.dataChange = z;
        if (z) {
            this.onLine = z;
            this.errorCount = 0;
        }
    }

    public int getLampRVal() {
        return this.lampRVal;
    }

    public void setLampRVal(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 99) {
            i = 99;
        }
        this.lampRVal = i;
        isDataChange(true);
    }

    public int getLampGVal() {
        return this.lampGVal;
    }

    public void setLampGVal(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 99) {
            i = 99;
        }
        this.lampGVal = i;
        isDataChange(true);
    }

    public int getLampBVal() {
        return this.lampBVal;
    }

    public void setLampBVal(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 99) {
            i = 99;
        }
        this.lampBVal = i;
        isDataChange(true);
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String str) {
        this.pass = str;
    }

    public double getTemp() {
        return this.temp;
    }

    public void setTemp(double d) {
        this.temp = d;
        isDataChange(true);
    }

    public boolean isLightState() {
        return this.lightState;
    }

    public void setLightState(boolean z) {
        this.lightState = z;
        isDataChange(true);
    }

    public String getTorken() {
        return this.torken;
    }

    public void setTorken(String str) {
        this.torken = str;
    }

    public String getIpPort() {
        return this.ipPort;
    }

    public void setIpPort(String str) {
        this.ipPort = str;
    }

    public String getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(String str) {
        this.lastUpdate = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String str) {
        this.nickName = str;
    }

    public String toString() {
        return "id:" + this.id + " " + (this.onLine ? "����" : "������") + "  nickName:" + this.nickName + "  lastUpdate:" + this.lastUpdate + "  torken:" + this.torken + "  ipPort:" + this.ipPort;
    }

    public boolean isOnLine() {
        return this.onLine;
    }

    public void setOnLine(boolean z) {
        this.onLine = z;
    }
}
