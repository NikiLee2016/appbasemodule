package com.niki.appbase.utils;

import android.content.Context;

/**
 * Created by ylb on 2016/4/21.
 *
 */
public class ResourceIdUtils {
    public static int getResourceId(Context context, String resId, String defType) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getIdentifier(resId, defType, context.getPackageName());
    }

    public static int getViewId(Context context, String resId) {
        return getResourceId(context, resId, "id");
    }

    public static int getAnimId(Context context, String resId) {
        return getResourceId(context, resId, "anim");
    }

    public static int getDimenId(Context context, String resId) {
        return getResourceId(context, resId, "dimen");
    }

    public static int getStringId(Context context, String resId) {
        return getResourceId(context, resId, "string");
    }

    public static int getLayoutId(Context context, String resId) {
        return getResourceId(context, resId, "layout");
    }

    public static int getDrawableId(Context context, String resId) {
        return getResourceId(context, resId, "drawable");
    }

    public static int getColorId(Context context, String resId) {
        return getResourceId(context, resId, "color");
    }

    public static int getMenuId(Context context, String resId) {
        return getResourceId(context, resId, "menu");
    }

    public static int getAttrId(Context context, String resId) {
        return getResourceId(context, resId, "attr");
    }

    public static int getStyleId(Context context, String resId) {
        return getResourceId(context, resId, "style");
    }

    @Deprecated
    public static int getStyleableId(Context context, String name) {
        String packageName = context.getPackageName();//TODO-多包
//        String packageName = "cc.quanhai.youbiquan";
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals("styleable")) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Deprecated
    public static int[] getStyleableAry(Context context, String name) {
        String packageName = context.getPackageName();//TODO-多包
//        String packageName = "cc.quanhai.youbiquan";
        Class r = null;
        int[] ids = null;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals("styleable")) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null) && (desireClass.getField(name).get(desireClass).getClass().isArray()))
                ids = (int[]) desireClass.getField(name).get(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
