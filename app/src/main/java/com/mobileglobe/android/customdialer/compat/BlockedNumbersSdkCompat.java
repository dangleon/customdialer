package com.mobileglobe.android.customdialer.compat;

/**
 * Created by dang on 2/22/17.
 */

import android.content.Context;
import android.net.Uri;
public class BlockedNumbersSdkCompat {
    public static final Uri CONTENT_URI = null;
    public static final String _ID = null;
    public static final String COLUMN_ORIGINAL_NUMBER = null;
    public static final String E164_NUMBER = null;
    public static boolean canCurrentUserBlockNumbers(Context context) {
        return false;
    }
}