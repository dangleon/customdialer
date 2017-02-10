package com.mobileglobe.android.customdialer.dialerbind;

/**
 * Created by dang on 2/10/17.
 */

/*
 * Copyright (C) 2013 The Android Open Source Project
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
 * limitations under the License
 */


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.mobileglobe.android.customdialer.calllog.CallLogAdapter;
import com.mobileglobe.android.customdialer.calllog.ContactInfoHelper;
import com.mobileglobe.android.customdialer.common.logging.Logger;
import com.mobileglobe.android.customdialer.list.RegularSearchFragment;
import com.mobileglobe.android.customdialer.service.CachedNumberLookupService;
import com.mobileglobe.android.customdialer.service.SpamButtonRenderer;
import com.mobileglobe.android.customdialer.voicemail.VoicemailPlaybackPresenter;

/**
 * Default static binding for various objects.
 */
public class ObjectFactory {
    public static CachedNumberLookupService newCachedNumberLookupService() {
        // no-op
        return null;
    }

    public static String getFilteredNumberProviderAuthority() {
        return "com.android.dialer.database.filterednumberprovider";
    }

    public static SpamButtonRenderer newSpamButtonRenderer(
            Context context,
            ViewStub stub) {
        return null;
    }



    public static Logger getLoggerInstance() {
        // no-op
        return null;
    }

    public static RegularSearchFragment newRegularSearchFragment() {
        return new RegularSearchFragment();
    }
}