/*
 * Copyright (C) 2011 Pixmob (http://github.com/pixmob)
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
package org.pixmob.droidlink.receiver;

import static org.pixmob.droidlink.Constants.DEVELOPER_MODE;
import static org.pixmob.droidlink.Constants.SHARED_PREFERENCES_FILE;
import static org.pixmob.droidlink.Constants.TAG;

import org.pixmob.droidlink.feature.Features;
import org.pixmob.droidlink.feature.SharedPreferencesSaverFeature;
import org.pixmob.droidlink.service.MissedCallHandlerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * When a call is received, this receiver is notified. If the call was not
 * answered, the call log is read in order to upload the event to the remote
 * server at a later time.
 * @author Pixmob
 */
public class MissedCallReceiver extends BroadcastReceiver {
    private static final String SP_KEY_LAST_CALL_STATE = "lastCallState";
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
            prefsEditor = prefs.edit();
        }
        
        final String callState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        final String previousCallState = prefs.getString(SP_KEY_LAST_CALL_STATE, null);
        
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(callState)) {
            if (DEVELOPER_MODE) {
                final String incomingNumber = intent
                        .getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.i(TAG, "Call state is RINGING: incoming number is [" + incomingNumber + "]");
            }
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(callState)) {
            if (DEVELOPER_MODE) {
                Log.i(TAG, "Call state is IDLE");
            }
            
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(previousCallState)) {
                if (DEVELOPER_MODE) {
                    Log.i(TAG, "Previous call state was RINGING: "
                            + "read call log to get missed call number");
                }
                context.startService(new Intent(context, MissedCallHandlerService.class));
            }
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(callState)) {
            if (DEVELOPER_MODE) {
                Log.i(TAG, "Call state is OFFHOOK");
            }
        } else {
            Log.w(TAG, "Unknown call state: " + callState);
        }
        
        prefsEditor.putString(SP_KEY_LAST_CALL_STATE, callState);
        Features.getFeature(SharedPreferencesSaverFeature.class).save(prefsEditor);
    }
}
