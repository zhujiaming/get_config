package com.zhujm.getconfig.utils;

import android.util.Log;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
public class Logg {
    public static boolean DEBUG = true;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (DEBUG) {
            Log.e(tag, msg, throwable);
        }
    }
}
