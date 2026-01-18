package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Ryan Hayes on 10/9/2016.
 */
public class PhoneListener extends PhoneStateListener { // Added opening brace for class body
    private static final String TAG = "TAG";

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            // Getting a phone call
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "phone is ringing");
                if (LS_Service.lockscreenActivity_ != null) {
                    LS_Service.lockscreenActivity_.finish();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                // No action needed for idle state
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d(TAG, "phone is off hook");
                break; // Added break to ensure proper switch-case termination
            default:
                Log.d(TAG, "Unknown phone state: " + state); // Handle unexpected states
                break;
        }
    }
}