package com.demo.smarthome.tools;

import android.graphics.Bitmap;

import com.demo.smarthome.service.Cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileTools {
    public static boolean saveFile(String dirPath, String fileName, String content) {
        FileOutputStream fos = null;
        try {
            // sanitize file name
            String safeFileName = fileName.replaceAll(":", ".");

            // ensure directory exists
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // create file output stream
            File file = new File(dir, safeFileName);
            fos = new FileOutputStream(file);
            fos.write(content.getBytes("UTF-8")); // safer encoding
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean saveFile(String dirPath, String fileName, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            // sanitize file name
            String safeFileName = fileName.replaceAll(":", ".") + ".png";

            // create directory if it doesn't exist
            File dir = new File(dirPath);
            if (!dir.exists() && !dir.mkdirs()) {
                return false;
            }

            // create file output stream
            File file = new File(dir, safeFileName);
            fos = new FileOutputStream(file);

            // compress and write the bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getFile(String dirPath, String fileName) {
        FileInputStream fis = null;
        try {
            String safeFileName = fileName.replaceAll(":", ".");
            File file = new File(dirPath, safeFileName);

            if (!file.exists()) {
                return "";
            }

            int length = (int) file.length();
            byte[] buffer = new byte[length];

            fis = new FileInputStream(file);
            int readBytes = fis.read(buffer);
            if (readBytes != length) {
                // not fully read
                return "";
            }

            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0071 -> B:9:0x0074). Please report as a decompilation issue!!! */
    public static byte[] getFileToByte(String dirPath, String fileName) {
        FileInputStream fis = null;
        try {
            String safeFileName = fileName.replaceAll(":", ".");
            File file = new File(dirPath, safeFileName);

            if (!file.exists() || !file.isFile()) {
                return new byte[0];
            }

            int length = (int) file.length();
            byte[] buffer = new byte[length];

            fis = new FileInputStream(file);
            int readBytes = fis.read(buffer);
            if (readBytes != length) {
                // Не все прочитано, але частково — можна повернути або обробити як помилку
                return buffer;
            }

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean deleteFile(String str, String str2) {
        File file = new File(Cfg.savePath + "/" + str2.replaceAll(":", "."));
        try {
            if (file.isFile() && file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }
}
