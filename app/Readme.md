# 📦 AoBoET Android Project

**Project:** AoBoET Android (decompiled WebView-based app for BMS over Wi-Fi)

## 📁 Project Structure

```text
aoboet_java/
├── .gradle/
├── .idea/
├── app/
│   ├── build/
│   ├── libs/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── apps/H5057CD3A/www/
│   │   │   │       ├── login.html        <-- STARTUP FORM
│   │   │   │       ├── css/
│   │   │   │       ├── js/
│   │   │   │       └── img/
│   │   │   ├── java/
│   │   │   │   └── com/lt/batteryManage/ <-- Java code (MainActivity)
│   │   │   └── AndroidManifest.xml       <-- startup Activity
```

## 🚀 Startup Activity

The `AndroidManifest.xml` contains an `<intent-filter>` with `MAIN` and `LAUNCHER`, meaning the starting class looks like:

```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 🌐 login.html — main UI

This file is located at:

```
app/src/main/assets/apps/H5057CD3A/www/login.html
```

This file is typically loaded inside a WebView like this:

```java
webView.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
```

## 📌 Summary

- ✅ The app is a hybrid WebView (DCloud 5+ style)
- ✅ `login.html` is the main UI entry point
- ✅ Java `MainActivity` likely initializes the WebView

> If you'd like to auto-generate a working `MainActivity.java` that loads `login.html`, just ask.
