package com.niki.appbase.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.niki.appbase.global.GlobalUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class StringUtils {
    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim());
    }

    public static boolean isNotEmpty(String ... value) {
        for (String s : value) {
            if (!isNotEmpty(s)){
                return false;
            }
        }
        return true;
    }

    public static boolean contains(String content, String contain){
        if (StringUtils.isEmpty(content)){
            return false;
        }
        return content.contains(contain);
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color,
                                                int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 获取链接样式的字符串，即字符串下面有下划线
     *
     * @param resId 文字资源
     * @return 返回链接样式的字符串
     */
    /*public static Spanned getHtmlStyleString(int resId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
                .append(" </b></u></a>");
        return Html.fromHtml(sb.toString());
    }*/

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
                        / 1024 / (float) 100))
                        + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
                        + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
                        / 1024 / (float) 10))
                        + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
                        + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
                    + "GB";
        }
        return size;
    }

    /**
     * 产生4位随机数(0000-9999)
     *
     * @return 4位随机数
     */
    public static String getFourRandom() {
        Random random = new Random();
        StringBuilder fourRandom = new StringBuilder(random.nextInt(10000) + "");
        int randLength = fourRandom.length();
        if (randLength < 4) {
            for (int i = 1; i <= 4 - randLength; i++)
                fourRandom.insert(0, "0");
        }
        return fourRandom.toString();
    }

    public static String replacePdf2Pcl(String url) {
        return url.substring(0, url.lastIndexOf(".")) + ".pcl";
    }

    public static boolean isUrlString(String str){
        if (isEmpty(str)){
            return false;
        }
        String tag1 = "http://";
        String tag2 = "https://";
        return str.contains(tag1) || str.contains(tag2);
    }

    public static String stringList2String(List<String> strings, String split){
        if (ListUtil.isEmptyList(strings)){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String comma = i == 0 ? "" : split;
            sb.append(comma).append(strings.get(i));
        }
        return sb.toString();
    }

    public static String makeUpZero(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    public static boolean hasNewVersion(String versionName){
        if (isEmpty(versionName)){
            return false;
        }
        try{
            String[] arr = versionName.split("\\.");
            String vmOld = GlobalUtil.getVersionName();
            String[] arrOld = vmOld.split("\\.");
            if (arr.length != arrOld.length){
                return false;
            }
            for (int i = 0; i < arr.length; i++) {
                int codeNew = Integer.parseInt(arr[i]);
                int codeOld = Integer.parseInt(arrOld[i]);
                if (codeNew > codeOld){
                    return true;
                }
            }
        }catch (Exception ignore){

        }
        return false;
    }
}
