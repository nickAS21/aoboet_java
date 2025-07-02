package io.dcloud.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import io.dcloud.common.adapter.util.Logger;

/* loaded from: classes.dex */
public class ZipUtils {
    private static final int BUFF_SIZE = 1048576;

    public static void zipFiles(File[] fileArr, File file) throws IOException {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file), 1048576));
        for (File file2 : fileArr) {
            zipFile(file2, zipOutputStream, "");
        }
        zipOutputStream.close();
    }

    public static void upZipFile(File file, String str) throws ZipException, IOException {
        if (!str.endsWith("/")) {
            str = str + File.separatorChar;
        }
        File file2 = new File(str);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry nextElement = entries.nextElement();
            if (nextElement.getName().contains("../")) {
                Logger.d("ZIP", "util.ZipUtils Path traversal attack prevented path=" + nextElement.getName());
            } else {
                InputStream inputStream = zipFile.getInputStream(nextElement);
                String replace = new String((str + nextElement.getName()).getBytes(), "UTF-8").replace("\\", "/");
                File file3 = new File(replace);
                File parentFile = file3.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!replace.endsWith("/")) {
                    if (file3.exists()) {
                        file3.delete();
                    }
                    file3.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    byte[] bArr = new byte[1048576];
                    while (true) {
                        int read = inputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        } else {
                            fileOutputStream.write(bArr, 0, read);
                        }
                    }
                    inputStream.close();
                    fileOutputStream.close();
                }
            }
        }
        zipFile.close();
    }

    private static void zipFile(File file, ZipOutputStream zipOutputStream, String str) throws FileNotFoundException, IOException {
        String str2 = new String((str + (str.trim().length() == 0 ? "" : File.separator) + file.getName()).getBytes("UTF-8"), "UTF-8");
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                zipFile(file2, zipOutputStream, str2);
            }
            return;
        }
        byte[] bArr = new byte[1048576];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), 1048576);
        zipOutputStream.putNextEntry(new ZipEntry(str2));
        while (true) {
            int read = bufferedInputStream.read(bArr);
            if (read != -1) {
                zipOutputStream.write(bArr, 0, read);
            } else {
                bufferedInputStream.close();
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
                return;
            }
        }
    }
}
