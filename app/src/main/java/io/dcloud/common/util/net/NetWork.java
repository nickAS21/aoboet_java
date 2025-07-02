package io.dcloud.common.util.net;

import android.webkit.CookieManager;

import com.demo.smarthome.service.Cfg;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import io.dcloud.common.DHInterface.IReqListener;
import io.dcloud.common.DHInterface.IResponseListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class NetWork implements Runnable {
    public static long AUTO_RECONNECTTIME = 30000;
    private static final String CONTENT_TYPE_COMMON = "text/plain;charset=UTF-8";
    private static final String CONTENT_TYPE_UPLOAD = "application/x-www-form-urlencoded";
    private static final String DEFALUT_CHARSET = "UTF-8";
    private static final String PARAM_CHARSET = ";charset=";
    public static final int WORK_COMMON = 3;
    public static final int WORK_DOWNLOAD = 2;
    public static final int WORK_UPLOAD = 1;
    protected boolean isAbort;
    DefaultHttpClient mHttpClient;
    public int mPriority;
    protected IReqListener mReqListener;
    protected HttpRequestBase mRequest;
    protected RequestData mRequestData;
    protected HttpResponse mResponse;
    protected InputStream mResponseInput;
    protected IResponseListener mResponseListener;
    protected String mResponseText;
    private int mWorkType;
    NetWorkLoop mNetWorkLoop = null;
    public int mTimes = 1;
    public int MAX_TIMES = 3;
    protected long mRetryIntervalTime = AUTO_RECONNECTTIME;
    protected Map<String, String> mHeaderList = new HashMap();
    protected String mMainBoundry = getBoundry();

    public void dispose() {
    }

    public NetWork(int i, RequestData requestData, IReqListener iReqListener, IResponseListener iResponseListener) {
        this.mWorkType = i;
        this.mRequestData = requestData;
        this.mReqListener = iReqListener;
        this.mResponseListener = iResponseListener;
    }

    public void startWork() {
        Thread thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
        this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_INIT, this.isAbort);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.mRequest = this.mRequestData.getHttpRequest();
            if (!this.mRequestData.containHeader("Content-Type")) {
                int i = this.mWorkType;
                if (i == 1) {
                    this.mRequest.addHeader("Content-Type", CONTENT_TYPE_UPLOAD);
                } else if (i != 2) {
                    this.mRequest.addHeader("Content-Type", CONTENT_TYPE_COMMON);
                }
            }
            try {
                DefaultHttpClient createHttpClient = createHttpClient();
                this.mHttpClient = createHttpClient;
                createHttpClient.setRedirectHandler(new RedirectHandler() { // from class: io.dcloud.common.util.net.NetWork.1
                    @Override // org.apache.http.client.RedirectHandler
                    public URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
                        return null;
                    }

                    @Override // org.apache.http.client.RedirectHandler
                    public boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
                        return false;
                    }
                });
                if (!this.mRequestData.isRedirect) {
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_REQUEST_BEGIN, this.isAbort);
                }
                HttpResponse executeHttpRequest = executeHttpRequest(this.mRequest);
                if (!this.mRequestData.isRedirect) {
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_BEGIN, this.isAbort);
                }
                Header lastHeader = executeHttpRequest.getLastHeader(IWebview.SET_COOKIE);
                if (lastHeader != null) {
                    String value = lastHeader.getValue();
                    if (!PdrUtil.isEmpty(value)) {
                        CookieManager.getInstance().setCookie(this.mRequestData.getUrl(), value);
                    }
                }
                int statusCode = executeHttpRequest.getStatusLine().getStatusCode();
                this.mResponseListener.onResponseState(statusCode, executeHttpRequest.getStatusLine().getReasonPhrase());
                if (statusCode == 200) {
                    setHeadersAndValues(executeHttpRequest.getAllHeaders());
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                    HttpEntity entity = executeHttpRequest.getEntity();
                    if (this.mWorkType == 2) {
                        InputStream ungzippedContent = getUngzippedContent(entity);
                        this.mResponseInput = ungzippedContent;
                        this.mReqListener.onResponsing(ungzippedContent);
                    } else {
                        handleResponseText(executeHttpRequest, entity);
                    }
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                } else {
                    switch (statusCode) {
                        case 301:
                        case 302:
                        case 303:
                            Header[] headers = executeHttpRequest.getHeaders("Location");
                            if (headers != null && headers.length > 0) {
                                String value2 = headers[0].getValue();
                                System.out.println("重定向的URL:" + value2);
                                String replace = value2.replace(" ", "%20");
                                this.mRequestData.clearData();
                                this.mRequestData.setUrl(replace);
                                run();
                                return;
                            }
                            break;
                        default:
                            setHeadersAndValues(executeHttpRequest.getAllHeaders());
                            this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                            handleResponseText(executeHttpRequest, executeHttpRequest.getEntity());
                            this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                            break;
                    }
                }
                this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_END, this.isAbort);
            } catch (Exception e) {
                e.printStackTrace();
                this.mResponseListener.onResponseState(0, e.getMessage());
                this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
                if ((e instanceof ConnectTimeoutException) || (e instanceof ConnectionPoolTimeoutException) || (e instanceof SocketTimeoutException)) {
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_TIMEOUT, this.isAbort);
                } else {
                    this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_ERROR, this.isAbort);
                }
            }
            NetWorkLoop netWorkLoop = this.mNetWorkLoop;
            if (netWorkLoop != null) {
                netWorkLoop.removeNetWork(this);
            }
        } catch (IllegalArgumentException e2) {
            this.mResponseListener.onResponseState(-1, e2.getMessage());
            this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_HANDLE_ING, this.isAbort);
            this.mReqListener.onNetStateChanged(IReqListener.NetState.NET_ERROR, this.isAbort);
            NetWorkLoop netWorkLoop2 = this.mNetWorkLoop;
            if (netWorkLoop2 != null) {
                netWorkLoop2.removeNetWork(this);
            }
        }
    }

    public static String getPAuth() {
        return InvokeExecutorHelper.TrafficFreeHelper.invoke("getPAuth");
    }

    private String getCharset(String str) {
        if (str != null) {
            String replace = str.replace(" ", "");
            if (replace.contains(PARAM_CHARSET)) {
                return replace.substring(replace.indexOf(PARAM_CHARSET) + 9);
            }
        }
        return null;
    }

    public void handleResponseText(HttpResponse httpResponse, HttpEntity httpEntity) throws IOException {
        try {
            String charset = getCharset(this.mRequestData.mOverrideMimeType);
            if (charset == null) {
                charset = getCharset(httpResponse.getFirstHeader("Content-Type").getValue());
            }
            if (charset == null) {
                charset = "UTF-8";
            }
            this.mResponseText = EntityUtils.toString(httpEntity, charset);
        } catch (Exception e) {
            e.printStackTrace();
            this.mResponseText = EntityUtils.toString(httpEntity, "UTF-8");
        }
    }

    public void cancelWork() {
        this.isAbort = true;
        HttpRequestBase httpRequestBase = this.mRequest;
        if (httpRequestBase != null) {
            httpRequestBase.abort();
            this.mRequest = null;
        }
        DefaultHttpClient defaultHttpClient = this.mHttpClient;
        if (defaultHttpClient != null) {
            defaultHttpClient.getConnectionManager().shutdown();
            this.mHttpClient = null;
        }
    }

    public HttpResponse executeHttpRequest(HttpRequestBase httpRequestBase) throws IOException {
        try {
            this.mHttpClient.getConnectionManager().closeExpiredConnections();
            return this.mHttpClient.execute(httpRequestBase);
        } catch (IOException e) {
            httpRequestBase.abort();
            throw e;
        }
    }

    public DefaultHttpClient createHttpClient() {
        SSLSocketFactory sSLSocketFactory;
        try {
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            if (this.mRequestData.URL_METHOD.equals(RequestData.URL_HTTPS)) {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                if (!PdrUtil.isEquals(this.mRequestData.unTrustedCAType, "refuse") && !PdrUtil.isEquals(this.mRequestData.unTrustedCAType, "warning")) {
                    sSLSocketFactory = new SSLSocketFactoryEx(keyStore);
                    sSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    schemeRegistry.register(new Scheme(RequestData.URL_HTTPS, sSLSocketFactory, 443));
                }
                sSLSocketFactory = new SSLSocketFactory(keyStore);
                sSLSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
                schemeRegistry.register(new Scheme(RequestData.URL_HTTPS, sSLSocketFactory, 443));
            } else {
                schemeRegistry.register(new Scheme(RequestData.URL_HTTP, PlainSocketFactory.getSocketFactory(), 80));
            }
            HttpParams createHttpParams = createHttpParams();
            HttpClientParams.setRedirecting(createHttpParams, true);
            return new DefaultHttpClient(new ThreadSafeClientConnManager(createHttpParams, schemeRegistry), createHttpParams);
        } catch (Exception unused) {
            return new DefaultHttpClient();
        }
    }

    public HttpParams createHttpParams() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.mRequestData.mTimeout);
        HttpConnectionParams.setSoTimeout(basicHttpParams, this.mRequestData.mTimeout);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        if (this.mRequestData.URL_METHOD.equals(RequestData.URL_HTTPS)) {
            basicHttpParams.setParameter("http.conn-manager.max-total", 5);
            basicHttpParams.setParameter("http.conn-manager.max-per-route", new ConnPerRouteBean(3));
            basicHttpParams.setParameter("http.protocol.expect-continue", false);
            basicHttpParams.setBooleanParameter("http.protocol.expect-continue", false);
            new BasicCredentialsProvider().setCredentials(new AuthScope(this.mRequestData.getIP(), -1), new UsernamePasswordCredentials("admin", Cfg.KEY_PASS_WORD));
        }
        return basicHttpParams;
    }

    public static InputStream getUngzippedContent(HttpEntity httpEntity) throws IOException {
        Header contentEncoding;
        String value;
        InputStream content = httpEntity.getContent();
        return (content == null || (contentEncoding = httpEntity.getContentEncoding()) == null || (value = contentEncoding.getValue()) == null || !value.contains("gzip")) ? content : new GZIPInputStream(content);
    }

    public static String getBoundry() {
        StringBuffer stringBuffer = new StringBuffer("------");
        for (int i = 1; i < 7; i++) {
            long currentTimeMillis = System.currentTimeMillis() + i;
            long j = currentTimeMillis % 3;
            if (j == 0) {
                stringBuffer.append(((char) currentTimeMillis) % '\t');
            } else if (j == 1) {
                stringBuffer.append((char) ((currentTimeMillis % 26) + 65));
            } else {
                stringBuffer.append((char) ((currentTimeMillis % 26) + 97));
            }
        }
        return stringBuffer.toString();
    }

    private void setHeadersAndValues(Header[] headerArr) {
        for (Header header : headerArr) {
            this.mHeaderList.put(header.getName(), header.getValue());
        }
    }

    public Map<String, String> getHeadersAndValues() {
        return this.mHeaderList;
    }

    public String getResponseText() {
        return this.mResponseText;
    }

    public InputStream getResponseInput() {
        return this.mResponseInput;
    }

    public void setRetryIntervalTime(long j) {
        if (j > 0) {
            this.mRetryIntervalTime = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SSLSocketFactoryEx extends SSLSocketFactory {
        SSLContext sslContext;

        public SSLSocketFactoryEx(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(keyStore);
            this.sslContext = SSLContext.getInstance("TLS");
            this.sslContext.init(null, new TrustManager[]{new DCloudTrustManager()}, null);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
        public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(socket, str, i, z);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }
}
