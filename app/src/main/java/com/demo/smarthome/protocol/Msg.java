package com.demo.smarthome.protocol;

import com.demo.smarthome.iprotocol.IMsg;
import com.demo.smarthome.iprotocol.IProtocol;
import com.demo.smarthome.tools.StrTools;

/* loaded from: classes.dex */
public class Msg implements IMsg {
    private boolean checkOk;
    private MSGCMD cmd;
    private MSGCMDTYPE cmdType;
    private short crc;
    private byte[] data;
    private int dataLen;
    private byte[] deCode;
    private byte[] enCode;
    private byte[] id;
    private int info;
    private boolean isACK;
    private boolean isNoACK;
    private boolean isSponse;
    private int packLen;
    private IProtocol protocol;
    private byte[] recvData;
    private String[] result;
    private byte[] sendData;
    private MSGSTATE state;
    private byte[] torken;

    public Msg() {
        this.id = new byte[0];
        this.cmdType = MSGCMDTYPE.CMDTYPE_A0;
        this.cmd = MSGCMD.CMD00;
        this.torken = new byte[0];
        this.info = 0;
        this.protocol = null;
        this.state = MSGSTATE.MSG_SEND;
    }

    public Msg(byte[] bArr, int i, int i2) {
        this.id = new byte[0];
        this.cmdType = MSGCMDTYPE.CMDTYPE_A0;
        this.cmd = MSGCMD.CMD00;
        this.torken = new byte[0];
        this.info = 0;
        this.protocol = null;
        this.id = bArr;
        this.cmdType = MSGCMDTYPE.valueOf(i);
        this.cmd = MSGCMD.valueOf(i2);
        this.state = MSGSTATE.MSG_SEND;
    }

    public Msg(byte[] bArr, int i, int i2, int i3) {
        this.id = new byte[0];
        this.cmdType = MSGCMDTYPE.CMDTYPE_A0;
        this.cmd = MSGCMD.CMD00;
        this.torken = new byte[0];
        this.info = 0;
        this.protocol = null;
        this.id = bArr;
        this.cmdType = MSGCMDTYPE.valueOf(i);
        this.cmd = MSGCMD.valueOf(i2);
        this.state = MSGSTATE.valueOf(i3);
    }

    public Msg(byte[] bArr, MSGCMDTYPE msgcmdtype, MSGCMD msgcmd) {
        this.id = new byte[0];
        this.cmdType = MSGCMDTYPE.CMDTYPE_A0;
        this.cmd = MSGCMD.CMD00;
        this.torken = new byte[0];
        this.info = 0;
        this.protocol = null;
        this.id = bArr;
        this.cmdType = msgcmdtype;
        this.cmd = msgcmd;
        this.state = MSGSTATE.MSG_SEND;
    }

    public Msg(byte[] bArr, MSGCMDTYPE msgcmdtype, MSGCMD msgcmd, MSGSTATE msgstate) {
        this.id = new byte[0];
        this.cmdType = MSGCMDTYPE.CMDTYPE_A0;
        this.cmd = MSGCMD.CMD00;
        this.torken = new byte[0];
        this.info = 0;
        this.protocol = null;
        this.id = bArr;
        this.cmdType = msgcmdtype;
        this.cmd = msgcmd;
        this.state = msgstate;
    }

    public byte[] getId() {
        return this.id;
    }

    public void setId(byte[] bArr) {
        this.id = bArr;
    }

    public byte[] getTorken() {
        return this.torken;
    }

    public void setTorken(String str) {
        this.torken = StrTools.hexStringToBytes(str);
    }

    public void setTorken(byte[] bArr) {
        this.torken = bArr;
    }

    public int getInfo() {
        return this.info;
    }

    public void setInfo(int i) {
        this.info = i;
    }

    public int getPackLen() {
        return this.packLen;
    }

    public void setPackLen(int i) {
        this.packLen = i;
    }

    public int getDataLen() {
        return this.dataLen;
    }

    public void setDataLen(int i) {
        this.dataLen = i;
    }

    public MSGCMDTYPE getCmdType() {
        return this.cmdType;
    }

    public void setCmdType(MSGCMDTYPE msgcmdtype) {
        this.cmdType = msgcmdtype;
    }

    public MSGCMD getCmd() {
        return this.cmd;
    }

    public void setCmd(MSGCMD msgcmd) {
        this.cmd = msgcmd;
    }

    public boolean isCheckOk() {
        return this.checkOk;
    }

    public void setCheckOk(boolean z) {
        this.checkOk = z;
    }

    public boolean isACK() {
        return this.isACK;
    }

    public void setACK(boolean z) {
        this.isACK = z;
    }

    public boolean isNoACK() {
        return this.isNoACK;
    }

    public void setNoACK(boolean z) {
        this.isNoACK = z;
    }

    public boolean isSponse() {
        return this.isSponse;
    }

    public void setSponse(boolean z) {
        this.isSponse = z;
    }

    public MSGSTATE getState() {
        return this.state;
    }

    public void setState(MSGSTATE msgstate) {
        this.state = msgstate;
    }

    public short getCrc() {
        return this.crc;
    }

    public void setCrc(short s) {
        this.crc = s;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public byte[] getDeCode() {
        return this.deCode;
    }

    public void setDeCode(byte[] bArr) {
        this.deCode = bArr;
    }

    public byte[] getEnCode() {
        return this.enCode;
    }

    public void setEnCode(byte[] bArr) {
        this.enCode = bArr;
    }

    public byte[] getRecvData() {
        return this.recvData;
    }

    public void setRecvData(byte[] bArr) {
        this.recvData = bArr;
    }

    public byte[] getSendData() {
        return this.sendData;
    }

    public void setSendData(byte[] bArr) {
        this.sendData = bArr;
    }

    public String[] getResult() {
        return this.result;
    }

    public void setResult(String[] strArr) {
        this.result = strArr;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(IProtocol iProtocol) {
        this.protocol = iProtocol;
    }

    public boolean msgTypeIsLoginOk() {
        return this.cmdType == MSGCMDTYPE.CMDTYPE_A0 && this.cmd == MSGCMD.CMD00 && this.state == MSGSTATE.MSG_SEND_OK;
    }

    public boolean msgIsACKByCmdType(MSGCMDTYPE msgcmdtype, MSGCMD msgcmd) {
        return this.cmdType == msgcmdtype && this.cmd == msgcmd && this.isACK;
    }

    public boolean msgIsSendAndACKByCmdType(int i, int i2) {
        return this.cmdType == MSGCMDTYPE.valueOf(i) && this.cmd == MSGCMD.valueOf(i2) && (this.isACK || this.state == MSGSTATE.MSG_SEND);
    }

    public boolean msgIsSendAndACKByCmdType(MSGCMDTYPE msgcmdtype, MSGCMD msgcmd) {
        return this.cmdType == msgcmdtype && this.cmd == msgcmd && (this.isACK || this.state == MSGSTATE.MSG_SEND);
    }

    public boolean msgStateTypeIsSend() {
        return this.state == MSGSTATE.MSG_SEND_OK;
    }

    public void setAck(boolean z) {
        this.isACK = z;
        this.isNoACK = false;
    }

    public void setNoAck(boolean z) {
        this.isNoACK = z;
        this.isACK = false;
    }

    public String toString() {
        String str = ((((((((((((((("\t\tid:" + this.id) + "\t\tpackLen:" + this.packLen) + "\t\tdataLen:" + this.dataLen) + "\t\tcmdType:" + this.cmdType) + "\t\tcmd:" + this.cmd) + "\t\tcheckOk:" + this.checkOk) + "\t\tisACK:" + this.isACK) + "\t\tisNoACK:" + this.isNoACK) + "\t\tisSponse:" + this.isSponse) + "\t\tstate:" + this.state) + "\t\tcrc:" + ((int) this.crc)) + "\t\tdata:" + StrTools.bytesToHexString(this.data)) + "\t\tdeCode:" + StrTools.bytesToHexString(this.deCode)) + "\t\tenCode:" + StrTools.bytesToHexString(this.enCode)) + "\t\trecvData:" + StrTools.bytesToHexString(this.recvData)) + "\t\tsendData:" + StrTools.bytesToHexString(this.sendData);
        String[] strArr = this.result;
        if (strArr != null) {
            int length = strArr.length;
            int i = 0;
            int i2 = 1;
            while (i < length) {
                str = str + "  " + i2 + " : " + strArr[i];
                i++;
                i2++;
            }
        }
        return str;
    }
}
