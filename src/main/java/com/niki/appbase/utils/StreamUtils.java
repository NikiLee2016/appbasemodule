package com.niki.appbase.utils;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class StreamUtils {

    private static String CRASH_PATH;

    public static void init(String crashPath) {
        CRASH_PATH = crashPath;
    }

    public static String getCrashPath(){
        if (StringUtils.isEmpty(CRASH_PATH)){
            throw new IllegalStateException("CRASH_PATH值为空, 请先初始化StreamUtils");
        }
        return CRASH_PATH;
    }

    /**
     * 将内存中的字符串储存到本地.
     * 注意:该文件将不会被重复刷新内容,而且每条日志之间会有换行符
     *
     * @param content  内存中的字符串
     * @param fileName 自定义的文件名
     */
    public static void saveCrashInfo2File(String content, String fileName) {
        content += "\n";
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = CRASH_PATH;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write(content.getBytes());
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据日志文件名, 拿到该文件的字符输出流
     *
     * @param fileName 日志文件名
     * @return 该文件的字符输出流
     */
    public static BufferedReader getBufferedReader(String fileName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String path = CRASH_PATH;
            File file = new File(path, fileName);
            if (file.exists()) {
                try {
                    return new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 拿到日志文件名对应的文件
     *
     * @param name 日志文件名
     * @return 文件
     */
    public static File getFileByName(String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String path = CRASH_PATH;
            File file = new File(path, name);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    /**
     * 删除日志文件名对应的文件
     *
     * @param name 日志文件名
     */
    public static void deleteFileByName(String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String path = CRASH_PATH;
            File file = new File(path, name);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static void unZip(InputStream in, String outputPath) {
        try {
            ZipInputStream Zin = new ZipInputStream(in);//输入源zip路径
            BufferedInputStream Bin = new BufferedInputStream(Zin);
            //File Parent = GlobalUtil.getContext().getFilesDir();; //输出路径（文件夹目录）
            File Fout = null;
            ZipEntry entry;
            try {
                while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
                    Fout = new File(outputPath);
                    if (!Fout.exists()) {
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(Fout);
                    BufferedOutputStream Bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = Bin.read()) != -1) {
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                    //LogUtil.print("解压成功!", 1);
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //LogUtil.print("解压失败01!", 1);
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFile(String inputPath, String outputPath) {
        // the file path need to compress
        File file = new File(inputPath);
        FileInputStream fis = null;
        ZipOutputStream zos = null;
        try {
            fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            zos = new ZipOutputStream(new FileOutputStream(outputPath));
            BufferedOutputStream bos = new BufferedOutputStream(zos);
            int index = inputPath.lastIndexOf(File.separator);
            String fileName = inputPath.substring(index + 1);
            zos.putNextEntry(new ZipEntry(fileName));

            byte[] b = new byte[1024];
            while (true) {
                int len = bis.read(b);
                if (len == -1)
                    break;
                bos.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (zos != null) {
                        zos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
