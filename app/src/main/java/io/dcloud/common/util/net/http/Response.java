package io.dcloud.common.util.net.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.AbsoluteConst;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class Response implements Runnable {
    AbsMgr mNetMgr;
    Socket mSocket;
    private static final byte CR = 13;
    private static final byte LF = 10;
    private static final byte[] CRLF = {CR, LF};
    String mUrl = null;
    final int BUFFER_SIZE = 10240;

    public Response(Socket socket, AbsMgr absMgr) {
        this.mSocket = null;
        this.mNetMgr = null;
        this.mSocket = socket;
        this.mNetMgr = absMgr;
        new Thread(this).start();
    }

    @Override // java.lang.Runnable
    public void run() {
        InputStream inputStream;
        Throwable th;
        OutputStream outputStream;
        IOException e;
        InputStream inputStream2 = null;
        try {
            try {
                InputStream inputStream3 = this.mSocket.getInputStream();
                try {
                    Request request = new Request(inputStream3);
                    request.parse();
                    String data = request.getData();
                    if (data.startsWith(AbsoluteConst.SOCKET_NATIVE_COMMAND)) {
                        this.mNetMgr.processEvent(IMgr.MgrType.AppMgr, 7, data);
                        outputStream = null;
                    } else if (data.startsWith(AbsoluteConst.SOCKET_CONNECTION)) {
                        String substring = data.substring(6);
                        OutputStream outputStream2 = this.mSocket.getOutputStream();
                        try {
                            String str = PdrUtil.isEquals(substring, AbsoluteConst.SOCKET_CONN_REQUEST_ROOT_PATH) ? DeviceInfo.sDeviceRootDir : "";
                            Logger.d("miniserver", substring, str);
                            outputStream2.write(str.getBytes());
                            outputStream = outputStream2;
                        } catch (IOException e2) {
                            e = e2;
                            outputStream = outputStream2;
                            inputStream = null;
                            inputStream2 = inputStream3;
                            try {
                                e.printStackTrace();
                                IOUtil.close(inputStream2);
                                IOUtil.close(inputStream);
                                IOUtil.close(outputStream);
                                this.mSocket.close();
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    IOUtil.close(inputStream2);
                                    IOUtil.close(inputStream);
                                    IOUtil.close(outputStream);
                                    this.mSocket.close();
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            outputStream = outputStream2;
                            inputStream = null;
                            inputStream2 = inputStream3;
                            IOUtil.close(inputStream2);
                            IOUtil.close(inputStream);
                            IOUtil.close(outputStream);
                            this.mSocket.close();
                            throw th;
                        }
                    } else {
                        String uri = request.getUri();
                        this.mUrl = uri;
                        if (uri == null) {
                            try {
                                IOUtil.close(inputStream3);
                                IOUtil.close((InputStream) null);
                                IOUtil.close((OutputStream) null);
                                this.mSocket.close();
                                return;
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                return;
                            }
                        }
                        outputStream = this.mSocket.getOutputStream();
                        try {
                            byte[] bArr = new byte[10240];
                            inputStream2 = this.mUrl.startsWith(AbsoluteConst.MINI_SERVER_BASE_RES) ? PlatformUtil.getResInputStream("res/" + this.mUrl.substring(5)) : (InputStream) this.mNetMgr.processEvent(IMgr.MgrType.AppMgr, 2, this.mUrl);
                            if (inputStream2 != null) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                while (true) {
                                    int read = inputStream2.read(bArr);
                                    if (read <= 0) {
                                        break;
                                    } else {
                                        byteArrayOutputStream.write(bArr, 0, read);
                                    }
                                }
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                addResponseHead(byteArray.length, outputStream);
                                outputStream.write(byteArray);
                            } else {
                                Logger.i("miniserver", "error url=" + this.mUrl);
                                outputStream.write("HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: 23\r\n\r\n<h1>File Not Found</h1>".getBytes());
                            }
                        } catch (IOException e5) {
                            e = e5;
                            inputStream = null;
                            inputStream2 = inputStream3;
                            e.printStackTrace();
                            IOUtil.close(inputStream2);
                            IOUtil.close(inputStream);
                            IOUtil.close(outputStream);
                            this.mSocket.close();
                        } catch (Throwable th4) {
                            th = th4;
                            inputStream = null;
                            inputStream2 = inputStream3;
                            IOUtil.close(inputStream2);
                            IOUtil.close(inputStream);
                            IOUtil.close(outputStream);
                            this.mSocket.close();
                            throw th;
                        }
                    }
                    IOUtil.close(inputStream3);
                    IOUtil.close(inputStream2);
                    IOUtil.close(outputStream);
                    this.mSocket.close();
                } catch (IOException e6) {
                    e = e6;
                    outputStream = null;
                    inputStream2 = inputStream3;
                    inputStream = null;
                } catch (Throwable th5) {
                    th = th5;
                    outputStream = null;
                    inputStream2 = inputStream3;
                    inputStream = null;
                }
            } catch (IOException e7) {
                inputStream = null;
                e = e7;
                outputStream = null;
            } catch (Throwable th6) {
                inputStream = null;
                th = th6;
                outputStream = null;
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
    }

    private void writeRequest(OutputStream outputStream, String str) throws IOException {
        outputStream.write("GET /index.html HTTP/1.1".getBytes());
        byte[] bArr = CRLF;
        outputStream.write(bArr);
        outputStream.write(("Host: " + str).getBytes());
        outputStream.write(bArr);
        outputStream.write(bArr);
        outputStream.flush();
    }

    private void write(OutputStream outputStream, String str) throws IOException {
        outputStream.write(str.getBytes());
    }

    private void addResponseHead(long j, OutputStream outputStream) throws IOException {
        writeHeader(outputStream, "HTTP/1.1 200 OK");
        writeHeader(outputStream, "Content-Type: " + PdrUtil.getMimeType(this.mUrl));
        writeHeader(outputStream, "Access-Control-Allow-Origin: *");
        writeHeader(outputStream, "Access-Control-Allow-Headers: *");
        writeHeader(outputStream, "Content-Length: " + j);
        outputStream.write(CRLF);
        outputStream.flush();
    }

    void writeHeader(OutputStream outputStream, String str) throws IOException {
        write(outputStream, str);
        outputStream.write(CRLF);
    }
}
