package com.mobileglobe.android.customdialer.calllog;

import android.content.Context;
import android.provider.CallLog.Calls;
import android.text.TextUtils;
import com.mobileglobe.android.customdialer.R;
import com.mobileglobe.android.customdialer.util.PhoneNumberUtil;
/**
 * Helper for formatting and managing the display of phone numbers.
 */
public class PhoneNumberDisplayUtil {
    /**
     * Returns the string to display for the given phone number if there is no matching contact.
     */
    /* package */ static CharSequence getDisplayName(
            Context context,
            CharSequence number,
            int presentation,
            boolean isVoicemail) {
        if (presentation == Calls.PRESENTATION_UNKNOWN) {
            return context.getResources().getString(R.string.unknown);
        }
        if (presentation == Calls.PRESENTATION_RESTRICTED) {
            return context.getResources().getString(R.string.private_num);
        }
        if (presentation == Calls.PRESENTATION_PAYPHONE) {
            return context.getResources().getString(R.string.payphone);
        }
        if (isVoicemail) {
            return context.getResources().getString(R.string.voicemail);
        }
        if (PhoneNumberUtil.isLegacyUnknownNumbers(number)) {
            return context.getResources().getString(R.string.unknown);
        }
        return "";
    }
    /**
     * Returns the string to display for the given phone number.
     *
     * @param number the number to display
     * @param formattedNumber the formatted number if available, may be null
     */
    public static CharSequence getDisplayNumber(
            Context context,
            CharSequence number,
            int presentation,
            CharSequence formattedNumber,
            CharSequence postDialDigits,
            boolean isVoicemail) {
        final CharSequence displayName = getDisplayName(context, number, presentation, isVoicemail);
        if (!TextUtils.isEmpty(displayName)) {
            return displayName;
        }
        if (!TextUtils.isEmpty(formattedNumber)) {
            return formattedNumber;
        } else if (!TextUtils.isEmpty(number)) {
            return number.toString() + postDialDigits;
        } else {
            return context.getResources().getString(R.string.unknown);
        }
    }
}