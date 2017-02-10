package com.mobileglobe.android.customdialer.incallui;

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
 * limitations under the License.
 */

        import android.content.Context;
        import android.hardware.camera2.CameraCharacteristics;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Trace;
        import android.telecom.Call.Details;
        import android.telecom.Connection;
        import android.telecom.DisconnectCause;
        import android.telecom.GatewayInfo;
        import android.telecom.InCallService.VideoCall;
        import android.telecom.PhoneAccount;
        import android.telecom.PhoneAccountHandle;
        import android.telecom.TelecomManager;
        import android.telecom.VideoProfile;
        import android.text.TextUtils;
        import android.util.Log;

        import com.mobileglobe.android.customdialer.common.testing.NeededForTesting;
        import com.mobileglobe.android.customdialer.util.IntentUtil;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;
        import java.util.Objects;
/**
 * Describes a single call and its state.
 */
@NeededForTesting
public class Call {
    /* Defines different states of this call */
    public static class State {
        public static final int INVALID = 0;
        public static final int NEW = 1;            /* The call is new. */
        public static final int IDLE = 2;           /* The call is idle.  Nothing active */
        public static final int ACTIVE = 3;         /* There is an active call */
        public static final int INCOMING = 4;       /* A normal incoming phone call */
        public static final int CALL_WAITING = 5;   /* Incoming call while another is active */
        public static final int DIALING = 6;        /* An outgoing call during dial phase */
        public static final int REDIALING = 7;      /* Subsequent dialing attempt after a failure */
        public static final int ONHOLD = 8;         /* An active phone call placed on hold */
        public static final int DISCONNECTING = 9;  /* A call is being ended. */
        public static final int DISCONNECTED = 10;  /* State after a call disconnects */
        public static final int CONFERENCED = 11;   /* Call part of a conference call */
        public static final int SELECT_PHONE_ACCOUNT = 12; /* Waiting for account selection */
        public static final int CONNECTING = 13;    /* Waiting for Telecom broadcast to finish */
        public static final int BLOCKED = 14;       /* The number was found on the block list */
        public static boolean isConnectingOrConnected(int state) {
            switch(state) {
                case ACTIVE:
                case INCOMING:
                case CALL_WAITING:
                case CONNECTING:
                case DIALING:
                case REDIALING:
                case ONHOLD:
                case CONFERENCED:
                    return true;
                default:
            }
            return false;
        }
        public static boolean isDialing(int state) {
            return state == DIALING || state == REDIALING;
        }
        public static String toString(int state) {
            switch (state) {
                case INVALID:
                    return "INVALID";
                case NEW:
                    return "NEW";
                case IDLE:
                    return "IDLE";
                case ACTIVE:
                    return "ACTIVE";
                case INCOMING:
                    return "INCOMING";
                case CALL_WAITING:
                    return "CALL_WAITING";
                case DIALING:
                    return "DIALING";
                case REDIALING:
                    return "REDIALING";
                case ONHOLD:
                    return "ONHOLD";
                case DISCONNECTING:
                    return "DISCONNECTING";
                case DISCONNECTED:
                    return "DISCONNECTED";
                case CONFERENCED:
                    return "CONFERENCED";
                case SELECT_PHONE_ACCOUNT:
                    return "SELECT_PHONE_ACCOUNT";
                case CONNECTING:
                    return "CONNECTING";
                case BLOCKED:
                    return "BLOCKED";
                default:
                    return "UNKNOWN";
            }
        }
    }
    /**
     * Defines different states of session modify requests, which are used to upgrade to video, or
     * downgrade to audio.
     */
    public static class SessionModificationState {
        public static final int NO_REQUEST = 0;
        public static final int WAITING_FOR_RESPONSE = 1;
        public static final int REQUEST_FAILED = 2;
        public static final int RECEIVED_UPGRADE_TO_VIDEO_REQUEST = 3;
        public static final int UPGRADE_TO_VIDEO_REQUEST_TIMED_OUT = 4;
        public static final int REQUEST_REJECTED = 5;
    }
    public static class VideoSettings {
        public static final int CAMERA_DIRECTION_UNKNOWN = -1;
        public static final int CAMERA_DIRECTION_FRONT_FACING =
                CameraCharacteristics.LENS_FACING_FRONT;
        public static final int CAMERA_DIRECTION_BACK_FACING =
                CameraCharacteristics.LENS_FACING_BACK;
        private int mCameraDirection = CAMERA_DIRECTION_UNKNOWN;
        /**
         * Sets the camera direction. if camera direction is set to CAMERA_DIRECTION_UNKNOWN,
         * the video state of the call should be used to infer the camera direction.
         *
         * @see {@link CameraCharacteristics#LENS_FACING_FRONT}
         * @see {@link CameraCharacteristics#LENS_FACING_BACK}
         */
        public void setCameraDir(int cameraDirection) {
            if (cameraDirection == CAMERA_DIRECTION_FRONT_FACING
                    || cameraDirection == CAMERA_DIRECTION_BACK_FACING) {
                mCameraDirection = cameraDirection;
            } else {
                mCameraDirection = CAMERA_DIRECTION_UNKNOWN;
            }
        }
        /**
         * Gets the camera direction. if camera direction is set to CAMERA_DIRECTION_UNKNOWN,
         * the video state of the call should be used to infer the camera direction.
         *
         * @see {@link CameraCharacteristics#LENS_FACING_FRONT}
         * @see {@link CameraCharacteristics#LENS_FACING_BACK}
         */
        public int getCameraDir() {
            return mCameraDirection;
        }
        @Override
        public String toString() {
            return "(CameraDir:" + getCameraDir() + ")";
        }
    }
    /**
     * Tracks any state variables that is useful for logging. There is some amount of overlap with
     * existing call member variables, but this duplication helps to ensure that none of these
     * logging variables will interface with/and affect call logic.
     */
    public static class LogState {
        // Contact lookup type constants
        // Unknown lookup result (lookup not completed yet?)
        public static final int LOOKUP_UNKNOWN = 0;
        public static final int LOOKUP_NOT_FOUND = 1;
        public static final int LOOKUP_LOCAL_CONTACT = 2;
        public static final int LOOKUP_LOCAL_CACHE = 3;
        public static final int LOOKUP_REMOTE_CONTACT = 4;
        public static final int LOOKUP_EMERGENCY = 5;
        public static final int LOOKUP_VOICEMAIL = 6;
        // Call initiation type constants
        public static final int INITIATION_UNKNOWN = 0;
        public static final int INITIATION_INCOMING = 1;
        public static final int INITIATION_DIALPAD = 2;
        public static final int INITIATION_SPEED_DIAL = 3;
        public static final int INITIATION_REMOTE_DIRECTORY = 4;
        public static final int INITIATION_SMART_DIAL = 5;
        public static final int INITIATION_REGULAR_SEARCH = 6;
        public static final int INITIATION_CALL_LOG = 7;
        public static final int INITIATION_CALL_LOG_FILTER = 8;
        public static final int INITIATION_VOICEMAIL_LOG = 9;
        public static final int INITIATION_CALL_DETAILS = 10;
        public static final int INITIATION_QUICK_CONTACTS = 11;
        public static final int INITIATION_EXTERNAL = 12;
        public DisconnectCause disconnectCause;
        public boolean isIncoming = false;
        public int contactLookupResult = LOOKUP_UNKNOWN;
        public int callInitiationMethod = INITIATION_EXTERNAL;
        // If this was a conference call, the total number of calls involved in the conference.
        public int conferencedCalls = 0;
        public long duration = 0;
        public boolean isLogged = false;
        @Override
        public String toString() {
            return String.format(Locale.US, "["
                            + "%s, " // DisconnectCause toString already describes the object type
                            + "isIncoming: %s, "
                            + "contactLookup: %s, "
                            + "callInitiation: %s, "
                            + "duration: %s"
                            + "]",
                    disconnectCause,
                    isIncoming,
                    lookupToString(contactLookupResult),
                    initiationToString(callInitiationMethod),
                    duration);
        }
        private static String lookupToString(int lookupType) {
            switch (lookupType) {
                case LOOKUP_LOCAL_CONTACT:
                    return "Local";
                case LOOKUP_LOCAL_CACHE:
                    return "Cache";
                case LOOKUP_REMOTE_CONTACT:
                    return "Remote";
                case LOOKUP_EMERGENCY:
                    return "Emergency";
                case LOOKUP_VOICEMAIL:
                    return "Voicemail";
                default:
                    return "Not found";
            }
        }
        private static String initiationToString(int initiationType) {
            switch (initiationType) {
                case INITIATION_INCOMING:
                    return "Incoming";
                case INITIATION_DIALPAD:
                    return "Dialpad";
                case INITIATION_SPEED_DIAL:
                    return "Speed Dial";
                case INITIATION_REMOTE_DIRECTORY:
                    return "Remote Directory";
                case INITIATION_SMART_DIAL:
                    return "Smart Dial";
                case INITIATION_REGULAR_SEARCH:
                    return "Regular Search";
                case INITIATION_CALL_LOG:
                    return "Call Log";
                case INITIATION_CALL_LOG_FILTER:
                    return "Call Log Filter";
                case INITIATION_VOICEMAIL_LOG:
                    return "Voicemail Log";
                case INITIATION_CALL_DETAILS:
                    return "Call Details";
                case INITIATION_QUICK_CONTACTS:
                    return "Quick Contacts";
                default:
                    return "Unknown";
            }
        }
    }
}