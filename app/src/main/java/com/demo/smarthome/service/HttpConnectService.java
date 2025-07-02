package com.demo.smarthome.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes.dex */
public class HttpConnectService {
    private static final String TAG = "HttpConnectService";

    public static String registUser(String str, String str2, String str3, String str4, String str5, String str6) {
        String str7 = "";
        boolean z = false;
        try {
            URL url = new URL(Cfg.WEBSERVICE_SERVER_URL);
            outMsg(TAG, "=======registUser======strUrl:http://cloud.ai-thinker.com/service/s.asmx");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpURLConnection.setRequestProperty("SOAPAction", "\"M2MHelper/registUser\"");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            StringBuffer stringBuffer = new StringBuffer(800);
            stringBuffer.append("\r\n\r\n<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body>");
            stringBuffer.append(" <registUser xmlns=\"M2MHelper\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">");
            stringBuffer.append("<userName>");
            stringBuffer.append(str);
            stringBuffer.append("</userName>");
            stringBuffer.append("<passWord>");
            stringBuffer.append(str2);
            stringBuffer.append("</passWord>");
            stringBuffer.append("<mobile>");
            stringBuffer.append(str3);
            stringBuffer.append("</mobile>");
            stringBuffer.append("<email>");
            stringBuffer.append(str4);
            stringBuffer.append("</email>");
            stringBuffer.append("<deviceID>");
            stringBuffer.append(str5);
            stringBuffer.append("</deviceID>");
            stringBuffer.append("<devicePWD>");
            stringBuffer.append(str6);
            stringBuffer.append("</devicePWD>");
            stringBuffer.append("</registUser></s:Body></s:Envelope>");
            stringBuffer.append("                                                                 ");
            outputStream.write(stringBuffer.toString().getBytes());
            outMsg(TAG, "=============StringBuffer.len:" + stringBuffer.length());
            outMsg(TAG, "=============StringBuffer:" + ((Object) stringBuffer));
            InputStream inputStream = httpURLConnection.getInputStream();
            outMsg(TAG, "=============StringBuffer");
            String findResultByString = getFindResultByString(getResultByStream(inputStream), "<registUserResult>", "</registUserResult>");
            if (findResultByString != null && findResultByString.length() >= 2) {
                str7 = findResultByString;
                z = true;
            }
            outputStream.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            outMsg(TAG, "=============注册失败！");
            e.printStackTrace();
        } catch (IOException e2) {
            outMsg(TAG, "=============注册失败！!");
            e2.printStackTrace();
        } catch (Exception e3) {
            outMsg(TAG, "=============注册失败！! !");
            e3.printStackTrace();
        }
        outMsg(TAG, "=============ok:" + z);
        if (!z) {
            outMsg(TAG, "=============注册失败.");
        }
        return str7;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:1|2|3|(2:5|(8:7|8|9|10|11|12|(1:14)|15))|32|10|11|12|(0)|15|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0101, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00ff, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00fd, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0137  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String userLogin(java.lang.String r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 317
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.HttpConnectService.userLogin(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0199  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x019f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.demo.smarthome.device.Dev> getDeviceList(java.lang.String r18, java.lang.String r19) {
        /*
            Method dump skipped, instructions count: 421
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.HttpConnectService.getDeviceList(java.lang.String, java.lang.String):java.util.List");
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:1|2|3|(3:5|6|(8:8|9|10|11|12|13|(1:15)|16))|33|11|12|13|(0)|16|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0103, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0101, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ff, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0139  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String chkUser(java.lang.String r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 319
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.HttpConnectService.chkUser(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x00e6, code lost:
    
        if (r8.equals("ok") != false) goto L34;
     */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0137  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean heartThrob(java.lang.String r7, java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 317
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.demo.smarthome.service.HttpConnectService.heartThrob(java.lang.String, java.lang.String):boolean");
    }

    public static String addLogs(String str, String str2, String str3, String str4, String str5) {
        String str6 = "";
        try {
            URL url = new URL(Cfg.WEBSERVICE_SERVER_URL);
            outMsg(TAG, "=======addLogs======strUrl:http://cloud.ai-thinker.com/service/s.asmx");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpURLConnection.setRequestProperty("SOAPAction", "\"VehicleHelper/addLogs\"");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            StringBuffer stringBuffer = new StringBuffer(500);
            stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            stringBuffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            stringBuffer.append("<soap:Body>");
            stringBuffer.append("<addLogs xmlns=\"VehicleHelper\">");
            stringBuffer.append("<userName>");
            stringBuffer.append(str);
            stringBuffer.append("</userName>");
            stringBuffer.append("<torken>");
            stringBuffer.append(str2);
            stringBuffer.append("</torken>");
            stringBuffer.append("<logDate>");
            stringBuffer.append(str3);
            stringBuffer.append("</logDate>");
            stringBuffer.append("<vehicleNO>");
            stringBuffer.append(str4);
            stringBuffer.append("</vehicleNO>");
            stringBuffer.append("<comment>");
            stringBuffer.append(str5);
            stringBuffer.append("</comment>");
            stringBuffer.append("</addLogs>");
            stringBuffer.append("</soap:Body>");
            stringBuffer.append("</soap:Envelope>");
            stringBuffer.append("                                                                 ");
            outputStream.write(stringBuffer.toString().getBytes());
            outMsg(TAG, "=============StringBuffer.len:" + stringBuffer.length());
            outMsg(TAG, "=============StringBuffer:" + ((Object) stringBuffer));
            InputStream inputStream = httpURLConnection.getInputStream();
            outMsg(TAG, "=============StringBuffer");
            String findResultByString = getFindResultByString(getResultByStream(inputStream), "<addLogsResult>", "</addLogsResult>");
            if (findResultByString != null && findResultByString.length() >= 1) {
                str6 = findResultByString;
            }
            outputStream.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            outMsg(TAG, "=============添加日志失败！");
            e.printStackTrace();
        } catch (IOException e2) {
            outMsg(TAG, "=============添加日志失败！!");
            e2.printStackTrace();
        } catch (Exception e3) {
            outMsg(TAG, "=============注册失败！!");
            e3.printStackTrace();
        }
        return str6;
    }

    public static String getNewAppUrl(String str, String str2, String str3) {
        String str4 = "";
        try {
            URL url = new URL(Cfg.WEBSERVICE_SERVER_URL);
            outMsg(TAG, "=======addLogs======strUrl:http://cloud.ai-thinker.com/service/s.asmx");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            httpURLConnection.setRequestProperty("SOAPAction", "\"VehicleHelper/getNewAPP\"");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            StringBuffer stringBuffer = new StringBuffer(500);
            stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            stringBuffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            stringBuffer.append("<soap:Body>");
            stringBuffer.append("<getNewAPP xmlns=\"VehicleHelper\">");
            stringBuffer.append("<userName>");
            stringBuffer.append(str);
            stringBuffer.append("</userName>");
            stringBuffer.append("<torken>");
            stringBuffer.append(str2);
            stringBuffer.append("</torken>");
            stringBuffer.append("<version>");
            stringBuffer.append(str3);
            stringBuffer.append("</version>");
            stringBuffer.append("</getNewAPP>");
            stringBuffer.append("</soap:Body>");
            stringBuffer.append("</soap:Envelope>");
            stringBuffer.append("                                                                 ");
            outputStream.write(stringBuffer.toString().getBytes());
            outMsg(TAG, "=============StringBuffer.len:" + stringBuffer.length());
            outMsg(TAG, "=============StringBuffer:" + ((Object) stringBuffer));
            InputStream inputStream = httpURLConnection.getInputStream();
            outMsg(TAG, "=============StringBuffer");
            String findResultByString = getFindResultByString(getResultByStream(inputStream), "<getNewAPPResult>", "</getNewAPPResult>");
            if (findResultByString != null && findResultByString.length() >= 10) {
                str4 = findResultByString;
            }
            outputStream.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            outMsg(TAG, "=============检查新版本失败！");
            e.printStackTrace();
        } catch (IOException e2) {
            outMsg(TAG, "=============检查新版本失败！!");
            e2.printStackTrace();
        }
        return str4;
    }

    public static boolean isConnectToNet(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    private static String getFindResultByString(String str, String str2, String str3) {
        int length;
        int indexOf = str.indexOf(str2);
        int indexOf2 = str.indexOf(str3);
        outMsg(TAG, "=============str:" + str);
        outMsg(TAG, "=============indexStart:" + indexOf + "  indexEnd:" + indexOf2);
        if (indexOf != 0 && indexOf < str.length() && indexOf2 != 0 && indexOf2 > (length = indexOf + str2.length())) {
            String substring = str.substring(length, indexOf2);
            outMsg(TAG, "=============str=" + substring);
            if (substring.length() > 0) {
                return substring;
            }
        }
        return "";
    }

    private static String getResultByStream(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        StringBuilder sb = new StringBuilder();
        outMsg(TAG, "=============StringBuffer");
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outMsg(TAG, "=============str=br.readLine()");
                sb.append(new String(bArr, 0, read, "utf-8"));
            } else {
                return sb.toString();
            }
        }
    }

    private static void outMsg(String str, String str2) {
        Log.i(str, str2);
    }
}
