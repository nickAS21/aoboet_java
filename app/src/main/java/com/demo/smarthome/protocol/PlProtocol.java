package com.demo.smarthome.protocol;

import android.util.Log;

import com.demo.smarthome.device.Dev;
import com.demo.smarthome.iprotocol.IProtocol;
import com.demo.smarthome.service.Cfg;
import com.demo.smarthome.tools.DateTools;
import com.demo.smarthome.tools.IpTools;
import com.demo.smarthome.tools.StrTools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.dcloud.common.DHInterface.IApp;

/* loaded from: classes.dex */
public class PlProtocol implements IProtocol {
    private int packLeng = 21;
    private String TAG = "PlProtocol";

    private void DeCodeMessage0AAfter(Msg msg) {
    }

    private void DeCodeMessage0ABfore(Msg msg) {
    }

    private void DeCodeMessageA0After(Msg msg) {
    }

    private void DeCodeMessageA0Bfore(Msg msg) {
    }

    private void DeCodeMessageAAAfter(Msg msg) {
    }

    private void DeCodeMessageAABfore(Msg msg) {
    }

    private void DeCodeMessageACAfter(Msg msg) {
    }

    private void DeCodeMessageACBfore(Msg msg) {
    }

    private void DeCodeMessageEFAfter(Msg msg) {
    }

    private void DeCodeMessageEFBfore(Msg msg) {
    }

    private void DeCodeMessageF0After(Msg msg) {
    }

    private void DeCodeMessageF0Bfore(Msg msg) {
    }

    private void MessagePackDataBefore(Msg msg) {
    }

    @Override // com.demo.smarthome.iprotocol.IProtocol
    public boolean MessageEnCode(Msg msg) {
        int i;
        int i2;
        int i3;
        if (msg == null) {
            return false;
        }
        int dataLen = msg.getDataLen() + this.packLeng + 1;
        if (msg.getTorken() != null) {
            dataLen += msg.getTorken().length;
        }
        byte[] bArr = new byte[dataLen];
        byte[] bArr2 = new byte[dataLen * 2];
        bArr[0] = (byte) (dataLen % 256);
        bArr[1] = (byte) (dataLen / 256);
        bArr[2] = (byte) (msg.getCmdType().val() % 256);
        bArr[3] = (byte) (msg.getCmd().val() % 256);
        bArr[4] = 0;
        bArr[5] = 0;
        bArr[6] = 0;
        bArr[7] = 0;
        int i4 = 9;
        bArr[8] = msg.getState().val();
        byte[] id = msg.getId();
        long length = id.length;
        for (int i5 = 0; i5 < 8; i5++) {
            if (i5 < length) {
                i3 = i4 + 1;
                bArr[i4] = (byte) (id[i5] % 256);
            } else {
                i3 = i4 + 1;
                bArr[i4] = 0;
            }
            i4 = i3;
        }
        byte[] torken = msg.getTorken();
        long length2 = torken == null ? 0L : torken.length;
        int i6 = i4 + 1;
        bArr[i4] = (byte) (length2 % 256);
        int i7 = 0;
        while (i7 < length2) {
            bArr[i6] = (byte) (torken[i7] % 256);
            i7++;
            i6++;
        }
        if (msg.getDataLen() > 0) {
            System.arraycopy(msg.getData(), 0, bArr, i6, msg.getDataLen());
            i6 += msg.getDataLen();
        }
        int i8 = i6 + 1;
        byte[] bArr3 = new byte[i8];
        bArr3[0] = 85;
        for (int i9 = 1; i9 <= i6; i9++) {
            bArr3[i9] = bArr[i9 - 1];
        }
        CRC16 crc16 = new CRC16();
        crc16.reset();
        crc16.update(bArr3, 0, i8);
        long value = crc16.getValue();
        bArr2[0] = 85;
        int i10 = 0;
        int i11 = 1;
        while (i10 < i6) {
            bArr2[i11] = bArr[i10];
            if (bArr[i10] == 85) {
                bArr2[i11] = 84;
                i11++;
                bArr2[i11] = 1;
            } else if (bArr[i10] == 84) {
                bArr2[i11] = 84;
                i11++;
                bArr2[i11] = 2;
            }
            i10++;
            i11++;
        }
        byte b = (byte) (value / 256);
        if (b == 84) {
            int i12 = i11 + 1;
            bArr2[i11] = 84;
            i = i12 + 1;
            bArr2[i12] = 2;
        } else if (b == 85) {
            int i13 = i11 + 1;
            bArr2[i11] = 84;
            i = i13 + 1;
            bArr2[i13] = 1;
        } else {
            i = i11 + 1;
            bArr2[i11] = b;
        }
        byte b2 = (byte) (value % 256);
        if (b2 == 84) {
            int i14 = i + 1;
            bArr2[i] = 84;
            i2 = i14 + 1;
            bArr2[i14] = 2;
        } else if (b2 == 85) {
            int i15 = i + 1;
            bArr2[i] = 84;
            i2 = i15 + 1;
            bArr2[i15] = 1;
        } else {
            bArr2[i] = b2;
            i2 = i + 1;
        }
        int i16 = i2 + 1;
        bArr2[i2] = 85;
        msg.setSendData(new byte[i16]);
        System.arraycopy(bArr2, 0, msg.getSendData(), 0, i16);
        return true;
    }

    @Override // com.demo.smarthome.iprotocol.IProtocol
    public boolean MessagePackData(Msg msg, String[] strArr) {
        boolean MessagePackDataEF;
        MessagePackDataBefore(msg);
        if (MSGCMDTYPE.CMDTYPE_A0 == msg.getCmdType()) {
            MessagePackDataEF = MessagePackDataA0(msg, strArr);
        } else if (MSGCMDTYPE.CMDTYPE_AA == msg.getCmdType()) {
            MessagePackDataEF = MessagePackDataAA(msg, strArr);
        } else if (MSGCMDTYPE.CMDTYPE_AC == msg.getCmdType()) {
            MessagePackDataEF = MessagePackDataAC(msg, strArr);
        } else if (MSGCMDTYPE.CMDTYPE_F0 == msg.getCmdType()) {
            MessagePackDataEF = MessagePackDataF0(msg, strArr);
        } else {
            MessagePackDataEF = MSGCMDTYPE.CMDTYPE_EF == msg.getCmdType() ? MessagePackDataEF(msg, strArr) : false;
        }
        MessagePackDataAfter(msg);
        return MessagePackDataEF;
    }

    private void MessagePackDataAfter(Msg msg) {
        MessageEnCode(msg);
    }

    private boolean MessagePackDataA0(Msg msg, String[] strArr) {
        if (MSGCMD.CMD00 == msg.getCmd()) {
            return MessagePackDataA000(msg, strArr);
        }
        if (MSGCMD.CMD01 == msg.getCmd()) {
            return MessagePackDataA001(msg, strArr);
        }
        return false;
    }

    private boolean MessagePackDataA000(Msg msg, String[] strArr) {
        int i;
        boolean z;
        if (strArr == null || strArr.length != 5) {
            return false;
        }
        byte[] bArr = new byte[15];
        long stringToInt = StrTools.stringToInt(strArr[0]);
        if (stringToInt < 0 || stringToInt > 1) {
            i = 0;
            z = true;
        } else {
            bArr[0] = (byte) (stringToInt % 256);
            z = false;
            i = 1;
        }
        String str = strArr[1];
        if (str == null) {
            z = true;
        }
        System.arraycopy(IpTools.getIpV4Byte(str), 0, bArr, i, 4);
        int i2 = i + 4;
        String str2 = strArr[2];
        if (str2 == null) {
            z = true;
        }
        System.arraycopy(IpTools.getIpV4Byte(str2), 0, bArr, i2, 4);
        int i3 = i2 + 4;
        String str3 = strArr[3];
        if (str3 == null) {
            z = true;
        }
        System.arraycopy(IpTools.getIpV4Byte(str3), 0, bArr, i3, 4);
        int i4 = i3 + 4;
        long stringToInt2 = StrTools.stringToInt(strArr[4]);
        if (stringToInt2 < 0 || stringToInt2 > 65535) {
            z = true;
        } else {
            bArr[i4] = (byte) (stringToInt2 / 256);
            bArr[i4 + 1] = (byte) (stringToInt2 % 256);
        }
        if (z) {
            return false;
        }
        msg.setDataLen(15);
        msg.setData(bArr);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataA001(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 6) {
            return false;
        }
        byte[] bArr = new byte[42];
        bArr[0] = 6;
        bArr[1] = 0;
        bArr[2] = 1;
        bArr[3] = 0;
        String str = strArr[0];
        boolean z = str == null;
        System.arraycopy(IpTools.getIpV4Byte(str), 0, bArr, 4, 4);
        bArr[8] = 2;
        int i = 10;
        bArr[9] = 0;
        long stringToInt = StrTools.stringToInt(strArr[1]);
        if (stringToInt < 0 || stringToInt > 65535) {
            z = true;
        } else {
            bArr[10] = (byte) (stringToInt / 256);
            i = 12;
            bArr[11] = (byte) (stringToInt % 256);
        }
        int i2 = i + 1;
        bArr[i] = 3;
        int i3 = i2 + 1;
        bArr[i2] = 0;
        String str2 = strArr[2];
        int i4 = i3 + 8;
        int i5 = i4 + 1;
        bArr[i4] = 4;
        int i6 = i5 + 1;
        bArr[i5] = 0;
        String str3 = strArr[3];
        if (str3 == null) {
            z = true;
        }
        System.arraycopy(IpTools.getIpV4Byte(str3), 0, bArr, i6, 4);
        int i7 = i6 + 4;
        int i8 = i7 + 1;
        bArr[i7] = 5;
        int i9 = i8 + 1;
        bArr[i8] = 0;
        long stringToInt2 = StrTools.stringToInt(strArr[4]);
        if (stringToInt2 < 0 || stringToInt2 > 65535) {
            z = true;
        } else {
            int i10 = i9 + 1;
            bArr[i9] = (byte) (stringToInt2 / 256);
            i9 = i10 + 1;
            bArr[i10] = (byte) (stringToInt2 % 256);
        }
        bArr[i9] = 6;
        bArr[i9 + 1] = 0;
        if (z) {
            return false;
        }
        msg.setDataLen(42);
        msg.setData(bArr);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataAA(Msg msg, String[] strArr) {
        if (MSGCMD.CMDEE == msg.getCmd()) {
            return MessagePackDataAAEE(msg, strArr);
        }
        if (MSGCMD.CMDFF == msg.getCmd()) {
            return MessagePackDataAAFF(msg, strArr);
        }
        if (MSGCMD.CMD00 == msg.getCmd()) {
            return MessagePackDataAA00(msg, strArr);
        }
        return false;
    }

    private boolean MessagePackDataAAEE(Msg msg, String[] strArr) {
        System.out.println("MessagePackDataAAEE");
        int i = 0;
        if (strArr == null || strArr.length != 169) {
            return false;
        }
        byte[] bArr = new byte[169];
        int i2 = 0;
        int i3 = 0;
        while (i < 169) {
            bArr[i2] = (byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[i3])) % 256);
            i++;
            i2++;
            i3++;
        }
        msg.setData(bArr);
        msg.setDataLen(i2);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataAAFF(Msg msg, String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 833) {
            return false;
        }
        byte[] bArr = new byte[833];
        int i2 = 0;
        int i3 = 0;
        while (i < 833) {
            bArr[i3] = (byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[i2])) % 256);
            i++;
            i3++;
            i2++;
        }
        msg.setDataLen(833);
        msg.setData(bArr);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataAA00(Msg msg, String[] strArr) {
        boolean z;
        int i = 0;
        if (strArr == null) {
            return false;
        }
        int i2 = 2;
        if (strArr.length == 2) {
            msg.setDataLen(2);
            msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256), (byte) (StrTools.stringToInt(strArr[1]) % 256)});
            msg.setCheckOk(true);
            i = 2;
            z = true;
        } else {
            z = false;
            i2 = 0;
        }
        if (strArr.length != 259) {
            return z;
        }
        byte[] bArr = new byte[259];
        int i3 = i + 1;
        long intToBcdInt = StrTools.intToBcdInt(StrTools.stringToInt(strArr[i]));
        int i4 = i2 + 1;
        bArr[i2] = (byte) (intToBcdInt % 256);
        int i5 = 1;
        while (i5 < 259) {
            bArr[i4] = (byte) (StrTools.stringToInt(strArr[i3]) % 256);
            i5++;
            i4++;
            i3++;
        }
        msg.setDataLen(259);
        msg.setData(bArr);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataAC(Msg msg, String[] strArr) {
        if (MSGCMD.CMD01 == msg.getCmd()) {
            return MessagePackDataAC01(msg, strArr);
        }
        if (MSGCMD.CMD02 == msg.getCmd()) {
            return MessagePackDataAC02(msg, strArr);
        }
        return false;
    }

    private boolean MessagePackDataAC01(Msg msg, String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 7) {
            return false;
        }
        byte[] bArr = new byte[7];
        int i2 = 0;
        int i3 = 0;
        while (i < 7) {
            bArr[i2] = (byte) (StrTools.stringToInt(strArr[i3]) % 256);
            i++;
            i2++;
            i3++;
        }
        msg.setData(bArr);
        msg.setDataLen(i2);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataAC02(Msg msg, String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 7) {
            return false;
        }
        byte[] bArr = new byte[7];
        int i2 = 0;
        int i3 = 0;
        while (i < 7) {
            bArr[i2] = (byte) (StrTools.stringToInt(strArr[i3]) % 256);
            i++;
            i2++;
            i3++;
        }
        msg.setData(bArr);
        msg.setDataLen(i2);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF0(Msg msg, String[] strArr) {
        if (MSGCMD.CMD55 == msg.getCmd()) {
            return MessagePackDataF055(msg, strArr);
        }
        if (MSGCMD.CMD56 == msg.getCmd()) {
            return MessagePackDataF056(msg, strArr);
        }
        if (MSGCMD.CMD57 == msg.getCmd()) {
            return MessagePackDataF057(msg, strArr);
        }
        if (MSGCMD.CMDAA == msg.getCmd()) {
            return MessagePackDataF0AA(msg, strArr);
        }
        if (MSGCMD.CMDAB == msg.getCmd()) {
            return MessagePackDataF0AB(msg, strArr);
        }
        if (MSGCMD.CMDF0 == msg.getCmd()) {
            return MessagePackDataF0F0(msg, strArr);
        }
        return false;
    }

    private boolean MessagePackDataF055(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF056(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF057(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF0AA(Msg msg, String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 7) {
            return false;
        }
        byte[] bArr = new byte[7];
        int i2 = 0;
        int i3 = 0;
        while (i < 7) {
            bArr[i2] = (byte) (StrTools.stringToInt(strArr[i3]) % 256);
            i++;
            i2++;
            i3++;
        }
        msg.setData(bArr);
        msg.setDataLen(i2);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF0AB(Msg msg, String[] strArr) {
        int i = 0;
        if (strArr == null || strArr.length != 7) {
            return false;
        }
        byte[] bArr = new byte[7];
        int i2 = 0;
        int i3 = 0;
        while (i < 7) {
            bArr[i2] = (byte) (StrTools.stringToInt(strArr[i3]) % 256);
            i++;
            i2++;
            i3++;
        }
        msg.setData(bArr);
        msg.setDataLen(i2);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataF0F0(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataEF(Msg msg, String[] strArr) {
        if (MSGCMD.CMD00 == msg.getCmd()) {
            return MessagePackDataEF00(msg, strArr);
        }
        if (MSGCMD.CMD01 == msg.getCmd()) {
            return MessagePackDataEF01(msg, strArr);
        }
        if (MSGCMD.CMD06 == msg.getCmd()) {
            return MessagePackDataEF06(msg, strArr);
        }
        return false;
    }

    private boolean MessagePackDataEF00(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataEF01(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        msg.setData(new byte[]{(byte) (StrTools.intToBcdInt(StrTools.stringToInt(strArr[0])) % 256)});
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    private boolean MessagePackDataEF06(Msg msg, String[] strArr) {
        if (strArr == null || strArr.length != 1) {
            return false;
        }
        long intToBcdInt = StrTools.intToBcdInt(StrTools.stringToInt(strArr[0]));
        byte[] bArr = {(byte) (intToBcdInt % 256)};
        msg.setData(bArr);
        msg.setDataLen(1);
        msg.setCheckOk(true);
        return true;
    }

    @Override // com.demo.smarthome.iprotocol.IProtocol
    public List<Msg> checkMessage(Buff buff) {
        System.out.println("public List<Msg> checkMessage( byte[] data start......");
        ArrayList arrayList = new ArrayList();
        List<byte[]> recvDataToList = recvDataToList(buff);
        System.out.println("public List<Msg> checkMessage( byte[] data recvDataToList...... listData:" + recvDataToList.size());
        for (byte[] bArr : recvDataToList) {
            System.out.println("public List<Msg> checkMessage( byte[] data recvDataToList...... da:" + StrTools.bytesToHexString(bArr));
            Msg dataToMessage = dataToMessage(bArr);
            System.out.println("public List<Msg> checkMessage( byte[] data recvDataToList...... msg:" + dataToMessage);
            List<Msg> DeCodeMessage = DeCodeMessage(dataToMessage);
            if (DeCodeMessage != null) {
                System.out.println("public List<Msg> checkMessage( byte[] data end......Msg" + dataToMessage.getId() + ":" + DeCodeMessage.size());
                Iterator<Msg> it = DeCodeMessage.iterator();
                while (it.hasNext()) {
                    arrayList.add(it.next());
                }
            } else {
                System.out.println("public List<Msg> checkMessage( byte[] data end......Msg" + dataToMessage.getId() + ": not result data");
            }
        }
        System.out.println("public List<Msg> checkMessage( byte[] data end......listResultMsg:" + arrayList.size());
        return arrayList;
    }

    private List<byte[]> recvDataToList(Buff buff) {
        System.out.println("private List<byte[]> recvDataToList( byte[] data start......bu.length:" + buff.length);
        ArrayList arrayList = new ArrayList();
        byte[] bArr = new byte[buff.data.length];
        int i = 0;
        while (i < buff.data.length && buff.data[i] != 85) {
            i++;
        }
        int i2 = 0;
        int i3 = 0;
        while (i < buff.data.length) {
            int i4 = i2 + 1;
            bArr[i2] = buff.data[i];
            if (buff.data[i] == 85) {
                if (i3 != 0) {
                    byte[] checkStrMsg = checkStrMsg(bArr, i4);
                    if (checkStrMsg != null) {
                        arrayList.add(checkStrMsg);
                    }
                    i2 = 0;
                } else {
                    bArr[0] = buff.data[i];
                    i2 = 1;
                }
                i3 = i2;
            } else {
                i2 = i4;
            }
            i++;
        }
        buff.data = new byte[i2];
        buff.length = i2;
        System.arraycopy(bArr, 0, buff.data, 0, i2);
        System.out.println("bu.length:" + buff.length);
        StrTools.printHexString(buff.data);
        System.out.println("private List<byte[]> recvDataToList( byte[] data end......listData:" + arrayList.size());
        return arrayList;
    }

    private byte[] checkStrMsg(byte[] bArr, int i) {
        System.out.println(" private byte[] checkStrMsg(byte[] data, int len) start......len:" + i);
        byte[] bArr2 = new byte[i * 2];
        bArr2[0] = 85;
        int i2 = 1;
        int i3 = 1;
        while (i2 < i - 1) {
            int i4 = i3 + 1;
            bArr2[i3] = bArr[i2];
            if (bArr[i2] == 84) {
                int i5 = i2 + 1;
                if (bArr[i5] == 1) {
                    bArr2[i4 - 1] = 85;
                } else if (bArr[i5] == 2) {
                    bArr2[i4 - 1] = 84;
                } else {
                    i3 = i4 + 1;
                    bArr2[i4] = bArr[i2];
                    i2++;
                }
                i2 = i5;
            }
            i3 = i4;
            i2++;
        }
        int i6 = i3 + 1;
        bArr2[i3] = bArr[i2];
        if (i6 < 9) {
            return null;
        }
        byte b = bArr2[i6 - 3];
        byte b2 = bArr2[i6 - 2];
        byte[] bArr3 = new byte[i6];
        System.arraycopy(bArr2, 0, bArr3, 0, i6);
        System.out.println(" private byte[] checkStrMsg(byte[] data, int len) end......check ok buffLength:" + i6);
        return bArr3;
    }

    private Msg dataToMessage(byte[] bArr) {
        System.out.println(" private Msg dataToMessage(byte[] data) start....data:" + StrTools.bytesToHexString(bArr));
        Msg msg = new Msg();
        int byteToUint = (StrTools.byteToUint(bArr[2]) * 256) + StrTools.byteToUint(bArr[1]);
        msg.setPackLen(byteToUint);
        msg.setCmdType(MSGCMDTYPE.valueOf(bArr[3] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE));
        System.out.println("data[3]:" + ((int) bArr[3]));
        System.out.println("msg.getCmdType():" + msg.getCmdType());
        msg.setCmd(MSGCMD.valueOf(bArr[4] & IApp.ABS_PRIVATE_WWW_DIR_APP_MODE));
        System.out.println("data[4]:" + ((int) bArr[4]));
        System.out.println("msg.getCmd():" + msg.getCmd());
        msg.setState(MSGSTATE.valueOf(bArr[9]));
        msg.setId(new byte[]{bArr[10], bArr[11], bArr[12], bArr[13], bArr[14], bArr[15], bArr[16], bArr[17]});
        int byteToUint2 = StrTools.byteToUint(bArr[18]);
        new String(bArr, 19, byteToUint2);
        int i = byteToUint - ((this.packLeng + byteToUint2) + 1);
        msg.setDataLen(0);
        if (i > 0) {
            msg.setDataLen(i);
            byte[] bArr2 = new byte[i];
            System.arraycopy(bArr, (this.packLeng + byteToUint2) - 2, bArr2, 0, i);
            msg.setData(bArr2);
        }
        System.out.println(" private Msg dataToMessage(byte[] data) eng......");
        return msg;
    }

    private List<Msg> DeCodeMessage(Msg msg) {
        List<Msg> list;
        System.out.println(" private List<Msg> DeCodeMessage(Msg msg) start......msgId:" + msg.getId() + " msgCmdType:" + msg.getCmdType());
        DeCodeMessageBfore(msg);
        if (MSGCMDTYPE.CMDTYPE_A0 == msg.getCmdType()) {
            list = DeCodeMessageA0(msg);
        } else if (MSGCMDTYPE.CMDTYPE_AA == msg.getCmdType()) {
            list = DeCodeMessageAA(msg);
        } else if (MSGCMDTYPE.CMDTYPE_AC == msg.getCmdType()) {
            list = DeCodeMessageAC(msg);
        } else if (MSGCMDTYPE.CMDTYPE_F0 == msg.getCmdType()) {
            list = DeCodeMessageF0(msg);
        } else if (MSGCMDTYPE.CMDTYPE_EF == msg.getCmdType()) {
            list = DeCodeMessageEF(msg);
        } else if (MSGCMDTYPE.CMDTYPE_EF == msg.getCmdType()) {
            list = DeCodeMessageEF(msg);
        } else {
            System.out.println(" private List<Msg> DeCodeMessage(Msg msg)  msg.getCmdType()error:" + msg.getCmdType());
            list = null;
        }
        DeCodeMessageAfter(msg);
        System.out.println(" private List<Msg> DeCodeMessage(Msg msg) eng......");
        return list;
    }

    private void DeCodeMessageBfore(Msg msg) {
        if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
            msg.setAck(true);
        } else if (msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            msg.setNoAck(true);
        } else {
            msg.setAck(false);
        }
    }

    private void DeCodeMessageAfter(Msg msg) {
        MessageEnCode(msg);
    }

    private List<Msg> DeCodeMessageA0(Msg msg) {
        List<Msg> DeCodeMessageA001;
        System.out.println(" private List<Msg> DeCodeMessageA0(Msg msg) start......msg:" + msg.getId());
        DeCodeMessageA0Bfore(msg);
        if (MSGCMD.CMD00 == msg.getCmd()) {
            DeCodeMessageA001 = DeCodeMessageA000(msg);
        } else {
            DeCodeMessageA001 = MSGCMD.CMD01 == msg.getCmd() ? DeCodeMessageA001(msg) : null;
        }
        DeCodeMessageA0After(msg);
        System.out.println(" private List<Msg> DeCodeMessageA0(Msg msg) end......");
        return DeCodeMessageA001;
    }

    private List<Msg> DeCodeMessageAA(Msg msg) {
        List<Msg> DeCodeMessageAAFF;
        DeCodeMessageAABfore(msg);
        if (MSGCMD.CMD00 == msg.getCmd()) {
            DeCodeMessageAAFF = DeCodeMessageAA00(msg);
        } else if (MSGCMD.CMDEE == msg.getCmd()) {
            DeCodeMessageAAFF = DeCodeMessageAAEE(msg);
        } else {
            DeCodeMessageAAFF = MSGCMD.CMDFF == msg.getCmd() ? DeCodeMessageAAFF(msg) : null;
        }
        DeCodeMessageAAAfter(msg);
        return DeCodeMessageAAFF;
    }

    private List<Msg> DeCodeMessageAC(Msg msg) {
        List<Msg> DeCodeMessageAC02;
        System.out.println(" private List<Msg> DeCodeMessageAC(Msg msg) start......msgId:" + msg.getId() + " msgCmdType:" + msg.getCmdType());
        DeCodeMessageACBfore(msg);
        if (MSGCMD.CMD01 == msg.getCmd()) {
            DeCodeMessageAC02 = DeCodeMessageAC01(msg);
        } else {
            DeCodeMessageAC02 = MSGCMD.CMD02 == msg.getCmd() ? DeCodeMessageAC02(msg) : null;
        }
        DeCodeMessageACAfter(msg);
        System.out.println(" private List<Msg> DeCodeMessageAC(Msg msg) end......");
        return DeCodeMessageAC02;
    }

    private List<Msg> DeCodeMessageF0(Msg msg) {
        List<Msg> DeCodeMessageF0F0;
        DeCodeMessageF0Bfore(msg);
        if (MSGCMD.CMD55 == msg.getCmd()) {
            DeCodeMessageF0F0 = DeCodeMessageF055(msg);
        } else if (MSGCMD.CMD56 == msg.getCmd()) {
            DeCodeMessageF0F0 = DeCodeMessageF056(msg);
        } else if (MSGCMD.CMD57 == msg.getCmd()) {
            DeCodeMessageF0F0 = DeCodeMessageF057(msg);
        } else if (MSGCMD.CMDAA == msg.getCmd()) {
            DeCodeMessageF0F0 = DeCodeMessageF0AA(msg);
        } else if (MSGCMD.CMDAB == msg.getCmd()) {
            DeCodeMessageF0F0 = DeCodeMessageF0AB(msg);
        } else {
            DeCodeMessageF0F0 = MSGCMD.CMDF0 == msg.getCmd() ? DeCodeMessageF0F0(msg) : null;
        }
        DeCodeMessageF0After(msg);
        return DeCodeMessageF0F0;
    }

    private List<Msg> DeCodeMessageEF(Msg msg) {
        List<Msg> DeCodeMessageEF07;
        DeCodeMessageEFBfore(msg);
        if (MSGCMD.CMD00 == msg.getCmd()) {
            DeCodeMessageEF07 = DeCodeMessageEF00(msg);
        } else if (MSGCMD.CMD01 == msg.getCmd()) {
            DeCodeMessageEF07 = DeCodeMessageEF01(msg);
        } else if (MSGCMD.CMD06 == msg.getCmd()) {
            DeCodeMessageEF07 = DeCodeMessageEF06(msg);
        } else {
            DeCodeMessageEF07 = MSGCMD.CMD07 == msg.getCmd() ? DeCodeMessageEF07(msg) : null;
        }
        DeCodeMessageEFAfter(msg);
        return DeCodeMessageEF07;
    }

    private List<Msg> DeCodeMessageA000(Msg msg) {
        System.out.println(" private List<Msg> DeCodeMessageA000(Msg msg) start......msg:" + msg.getId());
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
            System.out.println(" private List<Msg> DeCodeMessageA000(Msg msg)  execute ok......");
            Cfg.tcpTorken = msg.getData();
            arrayList.add(msg);
        }
        System.out.println(" private List<Msg> DeCodeMessageA000(Msg msg) end......");
        return arrayList;
    }

    private List<Msg> DeCodeMessageA001(Msg msg) {
        System.out.println(" private List<Msg> DeCodeMessageA001(Msg msg) ......msg:" + msg.getId());
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            arrayList.add(msg);
        } else {
            msg.setSponse(true);
            arrayList.add(msg);
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageAA00(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 1 && msg.getData() != null) {
                int length = msg.getData().length;
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageAAEE(Msg msg) {
        Dev devById;
        byte[] data;
        System.out.println("DeCodeMessageAAEE");
        ArrayList arrayList = new ArrayList();
        if ((msg.getState() == MSGSTATE.MSG_SEND || msg.getState() == MSGSTATE.MSG_SEND_OK) && (devById = Cfg.getDevById(StrTools.byteHexNumToStr(msg.getId()))) != null && (data = msg.getData()) != null && data.length > 5) {
            String[] split = new String(data).split(":");
            for (String str : split) {
                System.out.println("info:" + str);
            }
            if (split.length == 2) {
                if (split[0].equals("LIGHT") && !split[1].equals("?")) {
                    String[] split2 = split[1].split(";");
                    if (split2[0].equals("1")) {
                        devById.setLightState(true);
                        System.out.println("DeCodeMessageAAEE ");
                    } else if (split2[0].equals("0")) {
                        System.out.println("DeCodeMessageAAEE");
                        devById.setLightState(false);
                    } else {
                        System.out.println("DeCodeMessageAAEE֪");
                    }
                }
                if (split[0].equals("TEMP") && !split[1].equals("?")) {
                    String[] split3 = split[1].split(";");
                    System.out.println("DeCodeMessageAAEE txtVal TEMP:" + split3[0]);
                    System.out.println("DeCodeMessageAAEE num  TEMP:" + StrTools.stringToInt(split3[0]));
                    System.out.println("DeCodeMessageAAEE TEMP:" + (StrTools.stringToInt(split3[0]) / 10.0d));
                    devById.setTemp(StrTools.stringToInt(split3[0]) / 10.0d);
                    System.out.println("DeCodeMessageAAEE dev TEMP :" + devById.getTemp());
                }
                if (split[0].equals("LIGHTR")) {
                    String[] split4 = split[1].split(";");
                    System.out.println("DeCodeMessageAAEE txtVal TEMP:" + split4[0]);
                    System.out.println("DeCodeMessageAAEE num  TEMP:" + StrTools.stringToInt(split4[0]));
                    System.out.println("DeCodeMessageAAEE TEMP:" + (StrTools.stringToInt(split4[0]) / 10.0d));
                    devById.setLampRVal((int) StrTools.stringToInt(split4[0]));
                }
                if (split[0].equals("LIGHTG")) {
                    String[] split5 = split[1].split(";");
                    System.out.println("DeCodeMessageAAEE txtVal TEMP:" + split5[0]);
                    System.out.println("DeCodeMessageAAEE num  TEMP:" + StrTools.stringToInt(split5[0]));
                    System.out.println("DeCodeMessageAAEE TEMP:" + (StrTools.stringToInt(split5[0]) / 10.0d));
                    devById.setLampGVal((int) StrTools.stringToInt(split5[0]));
                }
                if (split[0].equals("LIGHTB")) {
                    String[] split6 = split[1].split(";");
                    System.out.println("DeCodeMessageAAEE txtVal TEMP:" + split6[0]);
                    System.out.println("DeCodeMessageAAEE num  TEMP:" + StrTools.stringToInt(split6[0]));
                    System.out.println("DeCodeMessageAAEE TEMP:" + (StrTools.stringToInt(split6[0]) / 10.0d));
                    devById.setLampBVal((int) StrTools.stringToInt(split6[0]));
                }
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageAAFF(Msg msg) {
        Dev devById;
        byte[] data;
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK && (devById = Cfg.getDevById(StrTools.byteHexNumToStr(msg.getId()))) != null && (data = msg.getData()) != null && data.length > 5) {
            String[] split = new String(data).split(":");
            if (split.length != 2) {
                if (split[0].equals("LIGHT") && !split[1].equals("?")) {
                    if (split[1].equals("1")) {
                        devById.setLightState(true);
                    } else if (split[1].equals("0")) {
                        devById.setLightState(false);
                    }
                }
                if (split[0].equals("TEMP") && !split[1].equals("?")) {
                    devById.setTemp(Integer.parseInt(split[1]));
                }
                if (split[0].equals("LIGHTR")) {
                    devById.setLampRVal(Integer.parseInt(split[1]));
                }
                if (split[0].equals("LIGHTG")) {
                    devById.setLampGVal(Integer.parseInt(split[1]));
                }
                if (split[0].equals("LIGHTB")) {
                    devById.setLampBVal(Integer.parseInt(split[1]));
                }
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageAC01(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 15 && msg.getData() != null && msg.getData().length >= 15) {
                byte[] data = msg.getData();
                msg.setResult(new String[]{StrTools.byteToUint(data[0]) + "", IpTools.getIpV4StringByByte(data, 1), IpTools.getIpV4StringByByte(data, 5), IpTools.getIpV4StringByByte(data, 9), ((StrTools.byteToUint(data[13]) * 256) + StrTools.byteToUint(data[14])) + ""});
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageAC02(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 28 && msg.getData() != null && msg.getData().length >= 28) {
                byte[] data = msg.getData();
                msg.setResult(new String[]{IpTools.getIpV4StringByByte(data, 0), ((StrTools.byteToUint(data[4]) * 256) + StrTools.byteToUint(data[5])) + "", "", IpTools.getIpV4StringByByte(data, 14), ((StrTools.byteToUint(data[18]) * 256) + StrTools.byteToUint(data[19])) + "", ""});
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF055(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 169 && msg.getData() != null && msg.getData().length >= 169) {
                String[] strArr = new String[169];
                for (int i = 0; i < 169; i++) {
                    long bcdIntToInt = StrTools.bcdIntToInt(StrTools.byteToUint(msg.getData()[i]));
                    strArr[i] = bcdIntToInt + "";
                    if (i % 7 == 0) {
                        System.out.println("");
                    } else {
                        System.out.print(bcdIntToInt + "\t\t\t    ");
                    }
                }
                System.out.println("");
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF056(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 833 && msg.getData() != null && msg.getData().length >= 833) {
                String[] strArr = new String[833];
                for (int i = 0; i < 833; i++) {
                    strArr[i] = StrTools.bcdIntToInt(StrTools.byteToUint(msg.getData()[i])) + "";
                }
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF057(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 259 && msg.getData() != null && msg.getData().length >= 259) {
                String[] strArr = new String[259];
                for (int i = 0; i < 259; i++) {
                    long byteToUint = StrTools.byteToUint(msg.getData()[i]);
                    if (i == 0) {
                        byteToUint = StrTools.bcdIntToInt(byteToUint);
                    }
                    strArr[i] = byteToUint + "";
                }
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF0AA(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 120 && msg.getData() != null && msg.getData().length >= 120) {
                String[] strArr = new String[120];
                for (int i = 0; i < 120; i++) {
                    strArr[i] = StrTools.bcdIntToInt(StrTools.byteToUint(msg.getData()[i])) + "";
                }
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF0AB(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 7 && msg.getData() != null && msg.getData().length >= 7) {
                String[] strArr = new String[7];
                for (int i = 0; i < 7; i++) {
                    strArr[i] = ((int) msg.getData()[i]) + "";
                }
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageF0F0(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (msg.getState() == MSGSTATE.MSG_SEND_OK && msg.getDataLen() == 56 && msg.getData() != null && msg.getData().length >= 56) {
                String[] strArr = new String[56];
                for (int i = 0; i < 56; i++) {
                    strArr[i] = StrTools.byteToUint(msg.getData()[i]) + "";
                }
                msg.setResult(strArr);
            }
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageEF00(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if ((msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) && MessageEnCode(msg)) {
            arrayList.add(msg);
            if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
                Log.i(this.TAG, DateTools.getNowTimeString() + "==> socket��¼�ɹ���");
            } else if (msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
                Log.i(this.TAG, DateTools.getNowTimeString() + "==>socket��¼ʧ�ܣ�");
            }
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageEF01(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK || msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
            if (MessageEnCode(msg)) {
                arrayList.add(msg);
                if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
                    Log.i(this.TAG, DateTools.getNowTimeString() + "==> socket�����ɹ���");
                } else if (msg.getState() == MSGSTATE.MSG_SEND_ERROE) {
                    Log.i(this.TAG, DateTools.getNowTimeString() + "==>socket����ʧ�ܣ�");
                }
            }
        } else {
            msg.setState(MSGSTATE.MSG_SEND_OK);
            msg.setSponse(true);
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageEF06(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
            Cfg.isSubmitDev = true;
        }
        return arrayList;
    }

    private List<Msg> DeCodeMessageEF07(Msg msg) {
        ArrayList arrayList = new ArrayList();
        if (msg.getState() == MSGSTATE.MSG_SEND_OK) {
            Cfg.isDeleteDev = true;
        }
        return arrayList;
    }
}
