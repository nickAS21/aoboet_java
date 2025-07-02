package io.dcloud.common.util.net;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class RequestData {
    public static final String URL_HTTP = "http";
    public static final String URL_HTTPS = "https";
    public String URL_METHOD;
    private ArrayList<String> mBodys;
    private long mContentLength;
    private HashMap<String, File> mFiles;
    private HashMap<String, String> mHeads;
    private HttpRequestBase mHttpRequest;
    private String mIp;
    private HashMap<String, String> mNameValue;
    private String mPort;
    private String mReqmethod;
    private String mUrl;
    public String unTrustedCAType = "accept";
    public boolean isRedirect = false;
    public int mTimeout = 120000;
    public String mOverrideMimeType = null;

    /* loaded from: classes.dex */
    enum HttpOption {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        TRACE,
        OPTIONS
    }

    public RequestData(String str, String str2) {
        this.URL_METHOD = URL_HTTP;
        this.mUrl = str;
        this.mReqmethod = str2;
        if (str != null && str.startsWith(URL_HTTPS)) {
            this.URL_METHOD = URL_HTTPS;
            initIpAndPort();
        }
        this.mNameValue = new HashMap<>();
        this.mHeads = new HashMap<>();
        this.mFiles = new HashMap<>();
        this.mBodys = new ArrayList<>();
    }

    public String getReqmethod() {
        return this.mReqmethod;
    }

    public void setReqmethod(String str) {
        this.mReqmethod = str;
    }

    public boolean addParemeter(String str, String str2) {
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || this.mFiles.containsKey(str) || this.mNameValue.containsKey(str)) {
            return false;
        }
        this.mNameValue.put(str, str2);
        return true;
    }

    public boolean addFile(String str, File file) {
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(file) || this.mFiles.containsKey(str) || this.mNameValue.containsKey(str)) {
            return false;
        }
        this.mFiles.put(str, file);
        return true;
    }

    public boolean addHeader(String str, String str2) {
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || this.mHeads.containsKey(str)) {
            return false;
        }
        this.mHeads.put(str, str2);
        return true;
    }

    public boolean containHeader(String str) {
        HashMap<String, String> hashMap = this.mHeads;
        if (hashMap != null) {
            return hashMap.containsKey(str);
        }
        return false;
    }

    public boolean addBody(String str) {
        if (PdrUtil.isEmpty(str)) {
            return false;
        }
        this.mBodys.add(str);
        return true;
    }

    public HttpRequestBase getHttpRequest() throws IllegalArgumentException {
        if (this.mHttpRequest == null) {
            try {
                switch (AnonymousClass1.$SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.valueOf(this.mReqmethod.toUpperCase()).ordinal()]) {
                    case 1:
                        HttpPost httpPost = new HttpPost(this.mUrl);
                        this.mHttpRequest = httpPost;
                        addBody(httpPost);
                        break;
                    case 2:
                        HttpPut httpPut = new HttpPut(this.mUrl);
                        this.mHttpRequest = httpPut;
                        addBody(httpPut);
                        break;
                    case 3:
                        this.mHttpRequest = new HttpDelete(this.mUrl);
                        break;
                    case 4:
                        this.mHttpRequest = new HttpHead(this.mUrl);
                        break;
                    case 5:
                        this.mHttpRequest = new HttpTrace(this.mUrl);
                        break;
                    case 6:
                        this.mHttpRequest = new HttpOptions(this.mUrl);
                        break;
                    default:
                        String format = URLEncodedUtils.format(getReqData(), "UTF-8");
                        if (PdrUtil.isEmpty(format)) {
                            this.mHttpRequest = new HttpGet(this.mUrl);
                            break;
                        } else {
                            this.mHttpRequest = new HttpGet(this.mUrl + "?" + format);
                            break;
                        }
                }
                addHeader(this.mHttpRequest);
            } catch (IllegalArgumentException e) {
                throw e;
            }
        }
        return this.mHttpRequest;
    }

    /* renamed from: io.dcloud.common.util.net.RequestData$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption;

        static {
            int[] iArr = new int[HttpOption.values().length];
            $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption = iArr;
            try {
                iArr[HttpOption.POST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.PUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.DELETE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.HEAD.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.TRACE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.OPTIONS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$io$dcloud$common$util$net$RequestData$HttpOption[HttpOption.GET.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public void clearData() {
        this.mHttpRequest.abort();
        this.mHttpRequest = null;
    }

    private String initIpAndPort() {
        String str;
        String substring = this.mUrl.substring(8);
        int lastIndexOf = substring.lastIndexOf(":");
        str = "443";
        if (lastIndexOf == -1) {
            substring.substring(0, substring.indexOf("/"));
        } else {
            int indexOf = substring.indexOf("/");
            int i = lastIndexOf + 1;
            str = indexOf > i ? substring.substring(i, indexOf) : "443";
            substring.substring(0, lastIndexOf);
        }
        return str;
    }

    public String getIP() {
        return this.mIp;
    }

    public String getPort() {
        return this.mPort;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }

    public void addHeader(HttpRequestBase httpRequestBase) {
        for (String str : this.mHeads.keySet()) {
            httpRequestBase.addHeader(str, this.mHeads.get(str));
        }
    }

    public void addHeader(HttpURLConnection httpURLConnection) {
        for (String str : this.mHeads.keySet()) {
            httpURLConnection.addRequestProperty(str, this.mHeads.get(str));
        }
    }

    private void addBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
        Iterator<String> it = this.mBodys.iterator();
        while (it.hasNext()) {
            String next = it.next();
            Vector<InputStream> vector = new Vector<>();
            this.mContentLength = appendPostParemeter(vector, next, this.mContentLength);
            httpEntityEnclosingRequestBase.setEntity(new InputStreamEntity(new SequenceInputStream(vector.elements()), this.mContentLength));
        }
    }

    private List<NameValuePair> getReqData() {
        ArrayList arrayList = new ArrayList();
        for (String str : this.mNameValue.keySet()) {
            arrayList.add(new BasicNameValuePair(str, this.mNameValue.get(str)));
        }
        return arrayList;
    }

    public long appendPostParemeter(Vector<InputStream> vector, String str, long j) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes("UTF-8"));
            vector.add(bais);
            return bais.available() + j;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
