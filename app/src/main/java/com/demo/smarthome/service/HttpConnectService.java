package com.demo.smarthome.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpConnectService {

    private static final String TAG = "HttpConnectService";

    // Timeout constants (ms)
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 2000;

    /**
     * Регістрація користувача через SOAP запит.
     */
    public static String registUser(String userName, String password, String mobile,
                                    String email, String deviceID, String devicePWD) {
        String soapAction = "\"M2MHelper/registUser\"";
        String xmlBody = buildRegistUserSoap(userName, password, mobile, email, deviceID, devicePWD);

        return sendSoapRequest(Cfg.WEBSERVICE_SERVER_URL, soapAction, xmlBody, "<registUserResult>", "</registUserResult>");
    }

    /**
     * Метод додавання логів.
     */
    public static String addLogs(String userName, String token, String logDate, String vehicleNO, String comment) {
        String soapAction = "\"VehicleHelper/addLogs\"";
        String xmlBody = buildAddLogsSoap(userName, token, logDate, vehicleNO, comment);

        return sendSoapRequest(Cfg.WEBSERVICE_SERVER_URL, soapAction, xmlBody, "<addLogsResult>", "</addLogsResult>");
    }

    /**
     * Отримання URL нової версії додатку.
     */
    public static String getNewAppUrl(String userName, String token, String version) {
        String soapAction = "\"VehicleHelper/getNewAPP\"";
        String xmlBody = buildGetNewAppSoap(userName, token, version);

        return sendSoapRequest(Cfg.WEBSERVICE_SERVER_URL, soapAction, xmlBody, "<getNewAPPResult>", "</getNewAPPResult>");
    }

    /**
     * Перевіряє чи є підключення до інтернету.
     */
    public static boolean isConnectToNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    // --------------------------------------
    // Приватні допоміжні методи
    // --------------------------------------

    private static String sendSoapRequest(String urlString, String soapAction, String xmlBody,
                                          String resultTagStart, String resultTagEnd) {
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        String result = "";

        try {
            URL url = new URL(urlString);
            Log.i(TAG, "SOAP request to: " + urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            connection.setRequestProperty("SOAPAction", soapAction);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

            outputStream = connection.getOutputStream();
            outputStream.write(xmlBody.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            inputStream = connection.getInputStream();
            String responseStr = readStreamToString(inputStream);

            result = extractSoapResult(responseStr, resultTagStart, resultTagEnd);

            Log.i(TAG, "SOAP response: " + result);

        } catch (Exception e) {
            Log.e(TAG, "SOAP request failed: " + e.getMessage(), e);
        } finally {
            closeQuietly(outputStream);
            closeQuietly(inputStream);
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }

    private static String readStreamToString(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static String extractSoapResult(String response, String startTag, String endTag) {
        if (response == null || startTag == null || endTag == null) return "";
        int start = response.indexOf(startTag);
        int end = response.indexOf(endTag);
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start + startTag.length(), end);
        }
        return "";
    }

    private static void closeQuietly(java.io.Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {}
    }

    // --------------------------------------
    // Побудова SOAP повідомлень (приклади)
    // --------------------------------------

    private static String buildRegistUserSoap(String userName, String password, String mobile,
                                              String email, String deviceID, String devicePWD) {
        return
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<s:Body>" +
                        "<registUser xmlns=\"M2MHelper\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                        "<userName>" + userName + "</userName>" +
                        "<passWord>" + password + "</passWord>" +
                        "<mobile>" + mobile + "</mobile>" +
                        "<email>" + email + "</email>" +
                        "<deviceID>" + deviceID + "</deviceID>" +
                        "<devicePWD>" + devicePWD + "</devicePWD>" +
                        "</registUser>" +
                        "</s:Body>" +
                        "</s:Envelope>";
    }

    private static String buildAddLogsSoap(String userName, String token, String logDate,
                                           String vehicleNO, String comment) {
        return
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap:Body>" +
                        "<addLogs xmlns=\"VehicleHelper\">" +
                        "<userName>" + userName + "</userName>" +
                        "<torken>" + token + "</torken>" +
                        "<logDate>" + logDate + "</logDate>" +
                        "<vehicleNO>" + vehicleNO + "</vehicleNO>" +
                        "<comment>" + comment + "</comment>" +
                        "</addLogs>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";
    }

    private static String buildGetNewAppSoap(String userName, String token, String version) {
        return
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                        "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<soap:Body>" +
                        "<getNewAPP xmlns=\"VehicleHelper\">" +
                        "<userName>" + userName + "</userName>" +
                        "<torken>" + token + "</torken>" +
                        "<version>" + version + "</version>" +
                        "</getNewAPP>" +
                        "</soap:Body>" +
                        "</soap:Envelope>";
    }

    public static void heartThrob(String userName, String token) {
        // Реалізація відправки heartbeat-запиту (можливо HTTP POST/GET)
        // Твої логіки тут
        Log.i("HttpConnectService", "Heartbeat sent for user: " + userName);
    }
    // TODO: Додати userLogin, getDeviceList, chkUser, heartThrob за аналогією,
    // якщо надаси їх код, допоможу переписати.

}
