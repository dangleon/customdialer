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

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.mobileglobe.android.customdialer.commonbind.analytics.AnalyticsUtil;
import com.mobileglobe.android.customdialer.dialerbind.ObjectFactory;
import com.mobileglobe.android.customdialer.incallui.Call;

/**
 * Single entry point for all logging/analytics-related work for all user interactions.
 */
public abstract class Logger {
    public static final String TAG = "Logger";

    public static Logger getInstance() {
        return ObjectFactory.getLoggerInstance();
    }

    /**
     * Logs a call event. PII like the call's number or caller details should never be logged.
     *
     * @param call to log.
     */
    public static void logCall(Call call) {
        final Logger logger = getInstance();
        if (logger != null) {
            logger.logCallImpl(call);
        }
    }


    /**
     * Logs an interaction that occurred
     *
     * @param interaction an integer representing what interaction occurred.
     * {@see com.android.dialer.logging.InteractionEvent}
     */
    public static void logInteraction(int interaction) {
        final Logger logger = getInstance();
        if (logger != null) {
            logger.logInteractionImpl(interaction);
        }
    }

    public abstract void logCallImpl(Call call);
    public abstract void logScreenViewImpl(int screenType);
    public abstract void logInteractionImpl(int dialerInteraction);
}