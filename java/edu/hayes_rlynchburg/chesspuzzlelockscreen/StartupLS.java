package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StartupLS extends Activity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "LS Startup onCreate Start");
        super.onCreate(savedInstanceState);

        // Start the lock screen service
        this.startService(new Intent(this, LS_Service.class));

        // Start the notification service
        this.startService(new Intent(this, NotificationListener.class));

        // Start the settings activity
        this.startActivity(new Intent(this, Settings.class));

        // Close this activity
        this.finish();
        Log.d(TAG, "LS Startup onCreate End");
    }
}