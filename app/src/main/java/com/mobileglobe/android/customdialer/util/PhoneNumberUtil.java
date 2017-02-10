package com.mobileglobe.android.customdialer.util;

/**
 * Created by dang on 2/10/17.
 */

import android.content.Context;
import android.provider.CallLog;
import android.telecom.PhoneAccountHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.mobileglobe.android.customdialer.common.util.PhoneNumberHelper;
import com.mobileglobe.android.customdialer.common.util.TelephonyManagerUtils;
import com.google.common.collect.Sets;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static android.util.Log.VERBOSE;

public class PhoneNumberUtil {
    private static final String TAG = "PhoneNumberUtil";
    private static final Set<String> LEGACY_UNKNOWN_NUMBERS = Sets.newHashSet("-1", "-2", "-3");
    /** Returns true if it is possible to place a call to the given number. */
    public static boolean canPlaceCallsTo(CharSequence number, int presentation) {
        return presentation == CallLog.Calls.PRESENTATION_ALLOWED
                && !TextUtils.isEmpty(number) && !isLegacyUnknownNumbers(number);
    }
    /**
     * Returns true if the given number is the number of the configured voicemail. To be able to
     * mock-out this, it is not a static method.
     */
    public static boolean isVoicemailNumber(
            Context context, PhoneAccountHandle accountHandle, CharSequence number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        return TelecomUtil.isVoicemailNumber(context, accountHandle, number.toString());
    }
    /**
     * Returns true if the given number is a SIP address. To be able to mock-out this, it is not a
     * static method.
     */
    public static boolean isSipNumber(CharSequence number) {
        return number != null && PhoneNumberHelper.isUriNumber(number.toString());
    }
    public static boolean isUnknownNumberThatCanBeLookedUp(
            Context context,
            PhoneAccountHandle accountHandle,
            CharSequence number,
            int presentation) {
        if (presentation == CallLog.Calls.PRESENTATION_UNKNOWN) {
            return false;
        }
        if (presentation == CallLog.Calls.PRESENTATION_RESTRICTED) {
            return false;
        }
        if (presentation == CallLog.Calls.PRESENTATION_PAYPHONE) {
            return false;
        }
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (isVoicemailNumber(context, accountHandle, number)) {
            return false;
        }
        if (isLegacyUnknownNumbers(number)) {
            return false;
        }
        return true;
    }
    public static boolean isLegacyUnknownNumbers(CharSequence number) {
        return number != null && LEGACY_UNKNOWN_NUMBERS.contains(number.toString());
    }
    /**
     * @return a geographical description string for the specified number.
     * @see com.android.i18n.phonenumbers.PhoneNumberOfflineGeocoder
     */
    public static String getGeoDescription(Context context, String number) {
        Log.v(TAG, "getGeoDescription('" + pii(number) + "')...");
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        com.google.i18n.phonenumbers.PhoneNumberUtil util =
                com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance();
        PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
        Locale locale = context.getResources().getConfiguration().locale;
        String countryIso = TelephonyManagerUtils.getCurrentCountryIso(context, locale);
        Phonenumber.PhoneNumber pn = null;
        try {
            Log.v(TAG, "parsing '" + pii(number)
                    + "' for countryIso '" + countryIso + "'...");
            pn = util.parse(number, countryIso);
            Log.v(TAG, "- parsed number: " + pii(pn));
        } catch (NumberParseException e) {
            Log.v(TAG, "getGeoDescription: NumberParseException for incoming number '" +
                    pii(number) + "'");
        }
        if (pn != null) {
            String description = geocoder.getDescriptionForNumber(pn, locale);
            Log.v(TAG, "- got description: '" + description + "'");
            return description;
        }
        return null;
    }
    private static String pii(Object pii) {
        if (pii == null) {
            return String.valueOf(pii);
        }
        return "[" + secureHash(String.valueOf(pii).getBytes()) + "]";
    }

    private static String secureHash(byte[] input) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        messageDigest.update(input);
        byte[] result = messageDigest.digest();
        return encodeHex(result);
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer hex = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int byteIntValue = bytes[i] & 0xff;
            if (byteIntValue < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString(byteIntValue, 16));
        }
        return hex.toString();
    }


}