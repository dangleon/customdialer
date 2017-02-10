package com.mobileglobe.android.customdialer.util;

/**
 * Created by dang on 2/10/17.
 */

import android.os.Looper;
public class Assert {
    public static void assertNotUiThread(String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new AssertionError(msg);
        }
    }
    public static void assertNotNull(Object object, String msg) {
        if (object == null) {
            throw new AssertionError(object);
        }
    }
    public static void assertNotNull(Object object) {
        assertNotNull(object, null);
    }
}
