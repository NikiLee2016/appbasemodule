package com.niki.appbase.imageloader.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.niki.appbase.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;

/**
 * 开源框架 Android-Universal-Image-Loader 的封装实现
 * <p/>
 */
public class MyImageLoader implements ImageLoaderWrapper {
    private final static String HTTP = "http";
    private final static String HTTPS = "https";

    /**
     * 初始化Universal-Image-Loader框架的参数设置
     *
     * @param context
     */
    public static void init(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.imageDownloader(new AuthImageDownloader(context));
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public void displayImageView(ImageView imageView, File imageFile, DisplayOption option) {
        int imageLoadingResId = R.drawable.img_default;
        int imageErrorResId = R.drawable.img_error;
        if (option != null) {
            imageLoadingResId = option.loadingResId;
            imageErrorResId = option.loadErrorResId;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(imageLoadingResId)
                .showImageForEmptyUri(imageErrorResId)
                .showImageOnFail(imageErrorResId)
                .cacheInMemory(true) //加载本地图片不需要再做SD卡缓存，只做内存缓存即可
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        String uri;
        if (imageFile == null) {
            uri = "";
        } else {
            uri = ImageDownloader.Scheme.FILE.wrap(imageFile.getAbsolutePath());
        }
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void displayImage(ImageView imageView, String url){
        ImageLoader.getInstance().displayImage(url, imageView, new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.img_default)
                .showImageOnFail(R.drawable.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                //.extraForDownloader()
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build());
    }
}
