package com.niki.appbase.utils;

import android.graphics.Bitmap;

import com.nanchen.compresshelper.CompressHelper;
import com.niki.appbase.global.GlobalUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Niki on 2018/2/27 09:51
 * E-Mail Address：m13296644326@163.com
 */

public class BitmapUtil {
    public static String compressJpgBitmap(String path, String desPath, boolean deleteOld){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String name = sdf.format(new Date()) + StringUtils.getFourRandom() + ".jpeg";
            File file = new CompressHelper.Builder(GlobalUtil.getContext())
                    .setQuality(83)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setFileName(name)
                    .setDestinationDirectoryPath(desPath)
                    .build()
                    .compressToFile(new File(path));
            if (deleteOld){
                new File(path).delete();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File saveBitmap2Local(Bitmap bitmap, String pathDir, String fileName) {
        return saveBitmap2Local(bitmap, pathDir, fileName, false);
    }
    public static File saveBitmap2Local(Bitmap bitmap, String pathDir, String fileName, boolean asPng) {
        File dir = new File(pathDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            File image = new File(dir, fileName);
            fos = new FileOutputStream(image);
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            if (asPng){
                format = Bitmap.CompressFormat.PNG;
            }
            bitmap.compress(format, 100, fos);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
