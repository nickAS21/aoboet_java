package io.dcloud.common.adapter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;

/* loaded from: classes.dex */
public class DHFile {
    public static final int BUF_SIZE = 204800;
    public static final byte FS_JAR = 0;
    public static final byte FS_NATIVE = 2;
    public static final byte FS_RMS = 1;
    public static final int READ = 1;
    public static final int READ_WRITE = 3;
    private static final String ROOTPATH = "/";
    public static final int WRITE = 2;

    public static void close(Object obj) throws IOException {
    }

    public static Object createFileHandler(String str) {
        return str.replace('\\', '/');
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0045  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0065 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0067  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte createNewFile(Object obj) {
        byte ERROR = -1;
        byte SUCCESS = 1;
        byte ALREADY_EXISTS = -2;

        if (obj == null) {
            return ERROR;
        }

        File file;
        boolean isDirectory = false;

        if (obj instanceof String) {
            String path = (String) obj;
            // Логування шляху
            io.dcloud.common.adapter.util.Logger.d("createNewFile 0: " + path);

            file = new File(path);
            isDirectory = path.endsWith("/");
        } else if (obj instanceof File) {
            file = (File) obj;
            isDirectory = file.isDirectory(); // додатково перевіримо чи це директорія
        } else {
            // Непідтримуваний тип аргументу
            return ERROR;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean createdParentDirs = parent.mkdirs();
            io.dcloud.common.adapter.util.Logger.d("createNewFile: parentPath mkdirs " + createdParentDirs);
        }

        if (file.exists()) {
            // Файл або директорія вже існує
            return ALREADY_EXISTS;
        }

        try {
            boolean created;
            if (isDirectory) {
                created = file.mkdirs();
            } else {
                created = file.createNewFile();
            }
            if (created) {
                return SUCCESS;
            }
        } catch (IOException e) {
            io.dcloud.common.adapter.util.Logger.w("createNewFile:", e);
        }

        return ERROR;
    }

    public static boolean delete(Object obj) {
        boolean delete;
        if (obj == null) {
            return false;
        }
        try {
            File file = getFile(obj);
            if (!file.exists()) {
                return false;
            }
            if (file.isFile()) {
                return file.delete();
            }
            File[] listFiles = file.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (int i = 0; i < listFiles.length; i++) {
                    Logger.d("delete:" + listFiles[i].getPath());
                    if (listFiles[i].isDirectory()) {
                        delete = delete(file.getPath() + ROOTPATH + listFiles[i].getName());
                    } else {
                        delete = listFiles[i].delete();
                        Thread.sleep(2L);
                    }
                    if (!delete) {
                        return false;
                    }
                }
            }
            boolean delete2 = file.delete();
            Logger.i("delete " + obj + ":" + String.valueOf(delete2));
            return delete2;
        } catch (Exception e) {
            Logger.w("DHFile.delete", e);
            return false;
        }
    }

    public static boolean exists(Object obj) {
        try {
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.endsWith(ROOTPATH)) {
                    str = str.substring(0, str.length() - 1);
                }
                return new File(str).exists();
            }
            if (obj instanceof File) {
                return ((File) obj).exists();
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String getPath(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return str.substring(0, str.lastIndexOf(47) + 1);
        }
        if (obj instanceof File) {
            return ((File) obj).getPath();
        }
        return null;
    }

    public static String getName(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.endsWith(ROOTPATH)) {
                str = str.substring(0, str.length() - 1);
            }
            return str.substring(str.lastIndexOf(47) + 1);
        }
        return ((File) obj).getName();
    }

    public static Object getParent(Object obj) throws IOException {
        StringBuffer stringBuffer = new StringBuffer(getPath(obj));
        if (((File) obj).isDirectory()) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.delete(stringBuffer.toString().lastIndexOf(47), stringBuffer.length());
        return createFileHandler(stringBuffer.toString());
    }

    public static boolean isDirectory(Object obj) throws IOException {
        return ((File) obj).isDirectory();
    }

    public static long length(Object obj) {
        try {
            return ((File) obj).length();
        } catch (Exception e) {
            Logger.w("length:", e);
            return -1L;
        }
    }

    public static String[] list(Object obj) throws IOException {
        Object[] listFiles = listFiles(obj);
        if (listFiles == null) {
            return null;
        }
        String[] strArr = new String[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            File file = (File) listFiles[i];
            if (file.isDirectory()) {
                strArr[i] = file.getName() + ROOTPATH;
            } else {
                strArr[i] = file.getName();
            }
        }
        return strArr;
    }

    public static String[] listDir(Object obj) throws IOException {
        Object[] listFiles = listFiles(obj);
        if (listFiles == null) {
            return null;
        }
        String[] strArr = new String[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            File file = (File) listFiles[i];
            if (file.isDirectory()) {
                strArr[i] = file.getName() + ROOTPATH;
            }
        }
        return strArr;
    }

    public static Object[] listFiles(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        file.isDirectory();
        try {
            return file.listFiles();
        } catch (Exception e) {
            Logger.w("listFiles:", e);
            return null;
        }
    }

    public static String[] listRoot() throws IOException {
        return new File(ROOTPATH).list();
    }

    protected static Object openFile(String str, int i, boolean z) throws IOException {
        return createFileHandler(getRealPath(str));
    }

    public static Object openFile(String str, int i) throws IOException {
        return openFile(str, i, false);
    }

    public static OutputStream getOutputStream(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        if (file.canWrite()) {
            try {
                return new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                Logger.w("getOutputStream:", e);
                return null;
            }
        }
        Logger.i("getOutputStream:can not write");
        return null;
    }

    public static OutputStream getOutputStream(Object obj, boolean z) throws IOException {
        File file;
        if (obj instanceof String) {
            file = new File((String) obj);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null) {
            return null;
        }
        if (file.canWrite()) {
            try {
                return new FileOutputStream(file, z);
            } catch (FileNotFoundException e) {
                Logger.w("getOutputStream:", e);
                return null;
            }
        }
        Logger.i("getOutputStream:can not write");
        return null;
    }

    public static InputStream getInputStream(Object obj) throws IOException {
        File file;
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                str = str.substring(7);
            }
            file = new File(str);
        } else {
            file = obj instanceof File ? (File) obj : null;
        }
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException unused) {
            Logger.e("DHFile getInputStream not found file: " + file.getPath());
            return null;
        } catch (SecurityException e) {
            Logger.w("getInputStream2", e);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:38:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v7, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] readAll(Object r5) {
        /*
            r0 = 0
            java.io.InputStream r5 = getInputStream(r5)     // Catch: java.lang.Throwable -> L27 java.io.IOException -> L2c java.lang.SecurityException -> L39 java.io.FileNotFoundException -> L46
            if (r5 == 0) goto L1c
            byte[] r0 = io.dcloud.common.util.IOUtil.getBytes(r5)     // Catch: java.io.IOException -> L16 java.lang.SecurityException -> L18 java.io.FileNotFoundException -> L1a java.lang.Throwable -> L68
            if (r5 == 0) goto L15
            r5.close()     // Catch: java.io.IOException -> L11
            goto L15
        L11:
            r5 = move-exception
            r5.printStackTrace()
        L15:
            return r0
        L16:
            r1 = move-exception
            goto L2e
        L18:
            r1 = move-exception
            goto L3b
        L1a:
            r1 = move-exception
            goto L48
        L1c:
            if (r5 == 0) goto L67
            r5.close()     // Catch: java.io.IOException -> L22
            goto L67
        L22:
            r5 = move-exception
            r5.printStackTrace()
            goto L67
        L27:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
            goto L69
        L2c:
            r1 = move-exception
            r5 = r0
        L2e:
            java.lang.String r2 = "readAll 2:"
            io.dcloud.common.adapter.util.Logger.w(r2, r1)     // Catch: java.lang.Throwable -> L68
            if (r5 == 0) goto L67
            r5.close()     // Catch: java.io.IOException -> L22
            goto L67
        L39:
            r1 = move-exception
            r5 = r0
        L3b:
            java.lang.String r2 = "readAll 1:"
            io.dcloud.common.adapter.util.Logger.w(r2, r1)     // Catch: java.lang.Throwable -> L68
            if (r5 == 0) goto L67
            r5.close()     // Catch: java.io.IOException -> L22
            goto L67
        L46:
            r1 = move-exception
            r5 = r0
        L48:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L68
            r2.<init>()     // Catch: java.lang.Throwable -> L68
            java.lang.String r3 = "readAll 0:"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L68
            java.lang.String r1 = r1.getLocalizedMessage()     // Catch: java.lang.Throwable -> L68
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch: java.lang.Throwable -> L68
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L68
            io.dcloud.common.adapter.util.Logger.i(r1)     // Catch: java.lang.Throwable -> L68
            if (r5 == 0) goto L67
            r5.close()     // Catch: java.io.IOException -> L22
        L67:
            return r0
        L68:
            r0 = move-exception
        L69:
            if (r5 == 0) goto L73
            r5.close()     // Catch: java.io.IOException -> L6f
            goto L73
        L6f:
            r5 = move-exception
            r5.printStackTrace()
        L73:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.io.DHFile.readAll(java.lang.Object):byte[]");
    }

    public static String getFilePath(Object obj) {
        return getPath(obj);
    }

    public static String getFileUrl(Object obj) {
        return getPath(obj);
    }

    public static String getFileName(Object obj) {
        return getName(obj);
    }


    public static int copyFile(String sourcePath, String destPath, boolean deleteIfExists, boolean skipIfExists) {
        InputStream input = null;
        OutputStream output = null;

        try {
            String realSource = getRealPath(sourcePath);
            String realDest = getRealPath(destPath);

            File srcFile = new File(realSource);
            File destFile = new File(realDest);

            // 1. Skip if file exists and skipping is enabled
            if (destFile.exists()) {
                if (skipIfExists) {
                    return -2;
                }
                if (deleteIfExists && destFile.isFile()) {
                    deleteFile(realDest);
                }
            }

            // 2. Source doesn't exist
            if (!srcFile.exists()) {
                return -1;
            }

            // 3. Directory — copy recursively
            if (srcFile.isDirectory()) {
                if (!destFile.exists()) {
                    destFile.mkdirs();
                }

                String[] files = list(srcFile);
                for (String name : files) {
                    int res = copyFile(
                            new File(srcFile, name).getAbsolutePath(),
                            new File(destFile, name).getAbsolutePath(),
                            deleteIfExists,
                            skipIfExists
                    );
                    if (res != 1) {
                        return res;
                    }
                }
                return 1;
            }

            // 4. Normal file copy
            input = new FileInputStream(srcFile);
            Object destFileObj = createFileHandler(realDest);
            if (!isExist(destFileObj)) {
                createNewFile(destFileObj);
            }
            output = getOutputStream(destFileObj);

            byte[] buffer = new byte[BUF_SIZE];
            int len;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();

            return 1;
        } catch (FileNotFoundException e) {
            Logger.i("copyFile: FileNotFound " + sourcePath);
            return -1;
        } catch (IOException e) {
            Logger.w("copyFile: IOException", e);
            return -1;
        } finally {
            IOUtil.close(input);
            IOUtil.close(output);
        }
    }

    public static int copyFile(String str, String str2) {
        return copyFile(str, str2, false, false);
    }

    public static int deleteFile(String str) throws IOException {
        return delete(new File(getRealPath(str))) ? 1 : -1;
    }

    public static int rename(String str, String str2) throws IOException {
        String str3;
        String realPath = getRealPath(str);
        if (realPath.endsWith(ROOTPATH)) {
            if (!str2.endsWith(ROOTPATH)) {
                str2 = str2 + ROOTPATH;
            }
            str3 = realPath.substring(0, realPath.length() - 1);
        } else {
            str3 = null;
        }
        if (str3 == null) {
            return -1;
        }
        if (!PdrUtil.isDeviceRootDir(str2)) {
            str2 = str3.substring(0, str3.lastIndexOf(ROOTPATH) + 1) + str2;
        }
        String realPath2 = getRealPath(str2);
        File file = new File(realPath);
        if (!file.exists()) {
            return -1;
        }
        File file2 = new File(realPath2);
        return (file2.exists() || !file.renameTo(file2)) ? -1 : 1;
    }

    public static boolean isExist(String str) throws IOException {
        return exists(getFile(getRealPath(str)));
    }

    public static boolean hasFile() {
        try {
            return new File("/sdcard/.system/45a3c43f-5991-4a65-a420-0a8a71874f72").exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isExist(Object obj) throws IOException {
        File file = getFile(obj);
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    public static boolean canRead(String str) throws IOException {
        return getFile(getRealPath(str)).canRead();
    }

    public static boolean isHidden(Object obj) throws IOException {
        File file = getFile(obj);
        if (file == null) {
            return false;
        }
        return file.isHidden();
    }

    private static File getFile(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (str.endsWith(ROOTPATH)) {
                str = str.substring(0, str.length() - 1);
            }
            return new File(str);
        }
        if (obj instanceof File) {
            return (File) obj;
        }
        return null;
    }

    public static boolean isHidden(String str) throws IOException {
        File file = new File(getRealPath(str));
        if (file.exists()) {
            return isHidden(file);
        }
        return false;
    }

    public static long getFileSize(File file) {
        long j = 0;
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                j += getFileSize(file2);
            }
            return j;
        }
        return 0 + file.length();
    }

    public static long getLastModify(String str) throws IOException {
        File file = new File(getRealPath(str));
        if (file.exists()) {
            return file.lastModified();
        }
        return 0L;
    }

    private static String getRealPath(String str) {
        String str2 = DeviceInfo.sBaseFsRootPath;
        String str3 = DeviceInfo.sBaseFsRootPath;
        StringBuffer stringBuffer = new StringBuffer();
        if ("".equals(str)) {
            return str2;
        }
        try {
            char[] charArray = str.toCharArray();
            int i = 0;
            while (i < charArray.length) {
                int i2 = 3;
                if ((charArray[0] == 'C' || charArray[0] == 'c') && i == 0) {
                    stringBuffer.append(str2);
                    i = 3;
                }
                if ((charArray[0] == 'D' || charArray[0] == 'd') && i == 0) {
                    stringBuffer.append(str3);
                } else {
                    i2 = i;
                }
                if (charArray[i2] == '\\') {
                    stringBuffer.append('/');
                } else {
                    stringBuffer.append(charArray[i2]);
                }
                i = i2 + 1;
            }
            return stringBuffer.toString();
        } catch (ArrayIndexOutOfBoundsException unused) {
            return str;
        }
    }

    public static void addFile(String str, byte[] bArr) throws IOException {
        Object createFileHandler = createFileHandler(str);
        createNewFile(createFileHandler);
        OutputStream outputStream = getOutputStream(createFileHandler, false);
        if (outputStream != null) {
            try {
                outputStream.write(bArr, 0, bArr.length);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            close(createFileHandler);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v14 */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r4v5 */
    /* JADX WARN: Type inference failed for: r4v9 */
    public static boolean writeFile(InputStream inputStream, String path) throws IOException {
        if (inputStream == null || path == null) return false;

        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                return false;
            }
        }

        final int BUF_SIZE = 8192; // або використай існуючу константу BUF_SIZE
        byte[] buffer = new byte[BUF_SIZE];

        try (FileOutputStream out = new FileOutputStream(file)) {
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void writeFile(byte[] bArr, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            Logger.i(str + "cannot create!");
            return;
        }
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.setLength(bArr.length + i);
                randomAccessFile.seek(i);
                randomAccessFile.write(bArr);
                randomAccessFile.close();
                return;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        try {
            file.createNewFile();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e4) {
            e4.printStackTrace();
        }
        if (fileOutputStream != null) {
            try {
                if (bArr != null) {
                    try {
                        try {
                            fileOutputStream.write(bArr, 0, bArr.length);
                            fileOutputStream.flush();
                        } catch (Throwable th) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (IOException e6) {
                        e6.printStackTrace();
                        fileOutputStream.close();
                    }
                }
                fileOutputStream.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        }
    }

    public static void writeFile(InputStream inputStream, int i, String str) {
        File file = new File(str);
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            Logger.i(str + "cannot create!");
            return;
        }
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws");
                randomAccessFile.seek(file.length());
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = inputStream.read(bArr, 0, 8192);
                    if (read != -1) {
                        randomAccessFile.write(bArr, 0, read);
                    } else {
                        randomAccessFile.close();
                        return;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
            try {
                try {
                    if (fileOutputStream != null) {
                        try {
                            byte[] bArr2 = new byte[8192];
                            while (true) {
                                int read2 = inputStream.read(bArr2, 0, 8192);
                                if (read2 == -1) {
                                    break;
                                } else {
                                    fileOutputStream.write(bArr2, 0, read2);
                                }
                            }
                            fileOutputStream.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                            fileOutputStream.close();
                        }
                    }
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
                throw th;
            }
        }
    }

    public static void copyDir(String str, String str2) {
        if (str == null || str2 == null) {
            return;
        }
        try {
            if (str.charAt(0) == '/') {
                str = str.substring(1, str.length());
            }
            if (str.charAt(str.length() - 1) == '/') {
                str = str.substring(0, str.length() - 1);
            }
            String[] listResFiles = PlatformUtil.listResFiles(str);
            if (createNewFile(str2) != -1) {
                for (String str3 : listResFiles) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(str);
                    stringBuffer.append('/');
                    stringBuffer.append(str3);
                    String stringBuffer2 = stringBuffer.toString();
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append(str2);
                    stringBuffer3.append(str3);
                    String stringBuffer4 = stringBuffer3.toString();
                    if (!copyAssetsFile(stringBuffer2, stringBuffer4)) {
                        if (checkIsNeedReload(str3)) {
                            if (!copyAssetsFile(stringBuffer2, stringBuffer4) && !copyAssetsFile(stringBuffer2, stringBuffer4)) {
                                Logger.d("PlatFU copyDir fail 3 times!!!!" + stringBuffer2);
                                copyDir(stringBuffer2, stringBuffer4 + ROOTPATH);
                            }
                        } else {
                            copyDir(stringBuffer2, stringBuffer4 + ROOTPATH);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyAssetsFile(String str, String str2) {
        boolean z = false;
        InputStream inputStream = null;
        try {
            try {
                inputStream = PlatformUtil.getResInputStream(str);
                if (inputStream != null) {
                    z = writeFile(inputStream, str2);
                } else if (checkIsNeedReload(str)) {
                    Logger.d("PlatFU copyAssetsFile fail ！！！！  is = null < " + str + " > to < " + str2 + " >");
                }
                Logger.d("PlatFU copyAssetsFile < " + str + " > to < " + str2 + " >");
            } catch (Exception e) {
                Logger.d("PlatFU copyAssetsFile " + str2 + " error!!!  is it a dir ?");
                Logger.d("PlatFU copyAssetsFile " + e);
                if (checkIsNeedReload(str)) {
                    Logger.d("PlatFU copyAssetsFile fail ！！！！ Exception< " + str + " > to < " + str2 + " >");
                }
            }
            return z;
        } finally {
            IOUtil.close(inputStream);
        }
    }

    private static boolean checkIsNeedReload(String str) {
        return str.endsWith(".png") || str.endsWith(".jpg") || str.endsWith(".xml") || str.endsWith(".bmp");
    }
}
