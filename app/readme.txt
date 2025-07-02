# 📦 AoBoET Android Project

**Проєкт:** AoBoET Android (декомпільований WebView-додаток для роботи з BMS через Wi-Fi)

## 📁 Структура проєкту

```
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
│   │   │   │       ├── login.html        <-- СТАРТОВА ФОРМА
│   │   │   │       ├── css/
│   │   │   │       ├── js/
│   │   │   │       └── img/
│   │   │   ├── java/
│   │   │   │   └── com/lt/batteryManage/ <-- Java-код (MainActivity)
│   │   │   └── AndroidManifest.xml       <-- стартова Activity
```

## 🚀 Стартова Activity

Файл `AndroidManifest.xml` містить запуск через `<intent-filter>` з `MAIN` та `LAUNCHER`. Це означає, що стартовий клас виглядає якось так:

```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 🌐 login.html — головний інтерфейс

Файл `login.html` знаходиться тут:
```
app/src/main/assets/apps/H5057CD3A/www/login.html
```

Цей файл використовується у WebView як стартова точка інтерфейсу. Код Java зазвичай містить:

```java
webView.loadUrl("file:///android_asset/apps/H5057CD3A/www/login.html");
```

## 📌 Висновок

- ✅ Додаток — гібридний WebView (на кшталт DCloud 5+)
- ✅ login.html — основна форма, яка відображається через WebView
- ✅ Код `MainActivity` ймовірно створює WebView, а `AndroidManifest.xml` запускає її автоматично

> Якщо потрібно реконструювати точний `MainActivity.java` або конфігурацію WebView — скажи, і я згенерую її автоматично.
