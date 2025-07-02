package io.dcloud.common.util;

import android.text.TextUtils;
import android.util.Log;

import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.constant.StringConst;

/* loaded from: classes.dex */
public class NetTool {
    private static String TAG = "NetTool";
    static DCloudTrustManager sCustomTrustManager;
    static HostnameVerifier sCustomeHostnameVerifier;

    public static byte[] httpGet(String str, HashMap<String, String> hashMap) throws Exception {
        return request(str, null, hashMap, "GET");
    }

    public static byte[] httpGet(String str) {
        try {
            return httpGet(str, null);
        } catch (Exception e) {
            if (((e instanceof SocketTimeoutException) || (e instanceof UnknownHostException)) && StringConst.canChangeHost(str)) {
                return httpGet(StringConst.changeHost(str));
            }
            return null;
        }
    }

    public static byte[] httpGetThrows(String str) throws Exception {
        return httpGet(str, null);
    }

    private static byte[] request(String str, String str2, HashMap<String, String> hashMap, String str3) {
        if (str == null || str.length() == 0) {
            Log.e(TAG, "httpPost, url is null");
            return null;
        }
        try {
            HttpURLConnection createConnection = createConnection(new URL(str), str3);
            if (hashMap != null && !hashMap.isEmpty()) {
                for (String str4 : hashMap.keySet()) {
                    createConnection.setRequestProperty(str4, hashMap.get(str4));
                }
            }
            if (!TextUtils.isEmpty(str3) && TextUtils.equals(str3.toLowerCase(), "post")) {
                write(createConnection.getOutputStream(), str2);
            }
            int responseCode = createConnection.getResponseCode();
            if (responseCode != 200) {
                Log.e(TAG, "httpGet fail, status code = " + responseCode);
                return null;
            }
            return read(createConnection.getInputStream());
        } catch (Exception e) {
            if (((e instanceof SocketTimeoutException) || (e instanceof UnknownHostException)) && StringConst.canChangeHost(str)) {
                return httpPost(StringConst.changeHost(str), str2, hashMap);
            }
            Log.e(TAG, "httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] httpPost(String str, String str2, HashMap<String, String> hashMap) {
        return request(str, str2, hashMap, "POST");
    }

    private static void write(OutputStream outputStream, String str) {
        if (str != null) {
            try {
                if (str.length() > 0) {
                    outputStream.write(str.getBytes("UTF-8"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] read(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int i = 10240;
            int min = Math.min(10240, inputStream.available());
            if (min > 0) {
                i = min;
            }
            byte[] bArr = new byte[i];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static HttpURLConnection createConnection(URL url, String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                try {
                    SSLContext sSLContext = SSLContext.getInstance("TLSv1");
                    sSLContext.init(null, new TrustManager[]{getDefaultTrustManager()}, new SecureRandom());
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLContext.getSocketFactory());
                    ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(getDefaultHostnameVerifier());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            httpURLConnection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod(str);
            httpURLConnection.setDoInput(true);
            return httpURLConnection;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static DCloudTrustManager getDefaultTrustManager() {
        DCloudTrustManager dCloudTrustManager = sCustomTrustManager;
        return dCloudTrustManager != null ? dCloudTrustManager : new DCloudTrustManager();
    }

    private static HostnameVerifier getDefaultHostnameVerifier() {
        HostnameVerifier hostnameVerifier = sCustomeHostnameVerifier;
        return hostnameVerifier != null ? hostnameVerifier : new HostnameVerifier() { // from class: io.dcloud.common.util.NetTool.1
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }
        };
    }
}
