package com.mobileglobe.android.customdialer.util;

/**
 * Created by dang on 2/10/17.
 */

import android.provider.CallLog.Calls;
public final class AppCompatConstants {
    public static final int CALLS_INCOMING_TYPE = Calls.INCOMING_TYPE;
    public static final int CALLS_OUTGOING_TYPE = Calls.OUTGOING_TYPE;
    public static final int CALLS_MISSED_TYPE = Calls.MISSED_TYPE;
    public static final int CALLS_VOICEMAIL_TYPE = Calls.VOICEMAIL_TYPE;
    // Added to android.provider.CallLog.Calls in N+.
    public static final int CALLS_REJECTED_TYPE = 5;
    // Added to android.provider.CallLog.Calls in N+.
    public static final int CALLS_BLOCKED_TYPE = 6;
}
