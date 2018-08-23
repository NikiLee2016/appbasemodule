package com.niki.appbase.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Bob on 2018/3/16.
 */
public class MD5Util {

    /**
     * 对签名字符串进行MD5加密，生成16位的字符串
     * @param source
     * @return
     */
    public static String encryption16(String source){
        return encryption32(source).substring(8, 24);
    }

    /**
     * 对签名字符串进行MD5加密，生成32位的字符串
     * @param source
     * @return
     */
    public static String encryption32(String source) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(source.getBytes());
            byte  []  b= md5.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
           // logger.error(e.getMessage());
        }
        return result;
    }
}
