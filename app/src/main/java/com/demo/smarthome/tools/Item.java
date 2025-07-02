package com.demo.smarthome.tools;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Item implements Serializable {
    private int dbm;
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getDbm() {
        return this.dbm;
    }

    public void setDbm(int i) {
        this.dbm = i;
    }
}
