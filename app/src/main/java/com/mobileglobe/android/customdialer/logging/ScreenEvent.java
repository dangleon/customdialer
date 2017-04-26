/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobileglobe.android.customdialer.logging;

import android.text.TextUtils;

import com.mobileglobe.android.customdialer.dialpad.DialpadFragment;
import com.mobileglobe.android.customdialer.filterednumber.BlockedNumbersFragment;
import com.mobileglobe.android.customdialer.list.BlockedListSearchFragment;
import com.mobileglobe.android.customdialer.list.RegularSearchFragment;
import com.mobileglobe.android.customdialer.list.SmartDialSearchFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores constants identifying individual screens/dialogs/fragments in the application, and also
 * provides a mapping of integer id -> screen name mappings for analytics purposes.
 */
public class ScreenEvent {
    private static final Map<Integer, String> sScreenNameMap = new HashMap<>();

    public static final String FRAGMENT_TAG_SEPARATOR = "#";

    public static final int UNKNOWN = 0;

    // The dialpad in the main Dialer activity
    public static final int DIALPAD = 1;


    // List of search results returned by typing into the search box.
    public static final int REGULAR_SEARCH = 6;

    // List of search results returned by typing into the dialpad.
    public static final int SMART_DIAL_SEARCH = 7;

    // The dialpad displayed in-call that is used to send dtmf tones.
    public static final int INCALL_DIALPAD = 16;

    // Menu options displayed when long pressing on a call log entry.
    public static final int CALL_LOG_CONTEXT_MENU = 17;

    // Screen displayed to allow the user to see an overview of all blocked
    // numbers.
    public static final int BLOCKED_NUMBER_MANAGEMENT = 18;

    // Screen displayed to allow the user to add a new blocked number.
    public static final int BLOCKED_NUMBER_ADD_NUMBER = 19;

    static {
        sScreenNameMap.put(ScreenEvent.DIALPAD,
                getScreenNameWithTag(DialpadFragment.class.getSimpleName(), "Dialer"));
        sScreenNameMap.put(ScreenEvent.REGULAR_SEARCH,
                RegularSearchFragment.class.getSimpleName());
        sScreenNameMap.put(ScreenEvent.SMART_DIAL_SEARCH,
                SmartDialSearchFragment.class.getSimpleName());
        sScreenNameMap.put(ScreenEvent.INCALL_DIALPAD,
                getScreenNameWithTag(DialpadFragment.class.getSimpleName(), "InCall"));
        sScreenNameMap.put(ScreenEvent.CALL_LOG_CONTEXT_MENU, "CallLogContextMenu");
        sScreenNameMap.put(ScreenEvent.BLOCKED_NUMBER_MANAGEMENT,
                BlockedNumbersFragment.class.getSimpleName());
        sScreenNameMap.put(ScreenEvent.BLOCKED_NUMBER_ADD_NUMBER,
                BlockedListSearchFragment.class.getSimpleName());
    }

    /**
     * For a given screen type, returns the actual screen name that is used for logging/analytics
     * purposes.
     *
     * @param screenType unique ID of a type of screen
     *
     * @return the tagged version of the screen name corresponding to the provided screenType,
     *         or {@null} if the provided screenType is unknown.
     */
    public static String getScreenName(int screenType) {
        return sScreenNameMap.get(screenType);
    }

    /**
     * Build a tagged version of the provided screenName if the tag is non-empty.
     *
     * @param screenName Name of the screen.
     * @param tag Optional tag describing the screen.
     * @return the unchanged screenName if the tag is {@code null} or empty, the tagged version of
     *         the screenName otherwise.
     */
    public static String getScreenNameWithTag(String screenName, String tag) {
        if (TextUtils.isEmpty(tag)) {
            return screenName;
        }
        return screenName + FRAGMENT_TAG_SEPARATOR + tag;
    }
}
