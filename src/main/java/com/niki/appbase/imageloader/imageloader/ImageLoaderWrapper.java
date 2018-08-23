package com.niki.appbase.imageloader.imageloader;

import android.widget.ImageView;

import java.io.File;

/**
 * 图片加载功能接口
 */
public interface ImageLoaderWrapper {
    /**
     * 显示 图片
     *
     * @param imageView 显示图片的ImageView
     * @param imageFile 图片文件
     * @param option    显示参数设置
     */
    void displayImageView(ImageView imageView, File imageFile, DisplayOption option);

    /**
     * 图片加载参数
     */
    class DisplayOption {
        /**
         * 加载中的资源id
         */
        public int loadingResId;
        /**
         * 加载失败的资源id
         */
        public int loadErrorResId;
    }
}
