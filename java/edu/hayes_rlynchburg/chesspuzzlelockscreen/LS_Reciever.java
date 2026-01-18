package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class LS_Reciever extends BroadcastReceiver { // Added missing class body braces
    private static final String TAG = "TAGs";

    /*public static List<Puzzle> puzzleList;
    public static String initialLayout;
    public static String finalLayout_;*/

    // Called when we receive something that the app is listening for
    @Override
    public void onReceive(Context ctxt, Intent intent) {
        Log.d(TAG, "LS_Reciever onReceive called");

        // Create a new PhoneListener for handling when you receive a phone call
        PhoneListener phoneListener = new PhoneListener();

        // Used in determining if the phone is ringing or not
        TelephonyManager telephonyManager = (TelephonyManager) ctxt.getSystemService(Context.TELEPHONY_SERVICE);

        // Register our phoneListener to listen for when the phone changes state
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        // If the screen turns off
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d(TAG, "LS_Reciever Screen Off");
            // Start the alarm lockscreenActivity
            startAlarmAndLockscreen(ctxt);
        }

        // If the screen turns on
        else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Log.d(TAG, "LS_Reciever Screen On");
        }

        // If the phone booted up
        else if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "LS_Reciever Boot Complete");
            // Start the lockscreen service and the notification service
            ctxt.startService(new Intent(ctxt, LS_Service.class));
            ctxt.startService(new Intent(ctxt, NotificationListener.class));
            startAlarmAndLockscreen(ctxt);
        }

        // When a notification is received. (sent by this app)
        else if ("NotificationReceived".equals(intent.getAction())) {
            // If the user wanted to see notifications
            if (LS_Service.showNotifications_) {
                Log.d(TAG, "Notification Received");

                // If the app is on screen
                if (LS_Service.lockscreenActivity_ != null) {
                    // Clear out all notifications currently displayed
                    NewLockscreen.listOfNotifications_.removeAllViewsInLayout();

                    // Convert the notifications to views to be displayed on the screen
                    for (int i = 0; i < NotificationListener.numberOfNotifications; ++i) {
                        // Convert a notification to a remote view
                        // RemoteViews (I think) are views that can be displayed in another process
                        RemoteViews remoteView = LS_Service.notifications_[i].contentView;

                        // Inflate the remote view and save it as a view to be displayed
                        View notificationView = remoteView.apply(LS_Service.lockscreenActivity_, NewLockscreen.scrollView_);
                        notificationView.setClickable(true);

                        // Generate a random id for the view
                        notificationView.setId(View.generateViewId());

                        // Handles when a notification is pressed
                        notificationView.setOnClickListener(NewLockscreen.notifClicker);

                        // Add it to the listOfNotifications_ to be displayed
                        NewLockscreen.listOfNotifications_.addView(notificationView, i, NewLockscreen.layoutParamsOfLS_);
                    }
                }
            }
        }

        // If the alarm goes off (Samsung)
        // Phone manufacturers have their own clock apps and they have individual alarm alert messages
        // unless there is a blanket way of doing it, this is how to do it
        // This is a link to listOfNotifications_ of manufacturer alarm alert messages for their clock apps.
        // http://stackoverflow.com/questions/4115649/listing-of-manufacturers-clock-alarm-package-and-class-name-please-add
        if ("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT".equals(intent.getAction()) ||
                "com.android.alarmclock.ALARM_ALERT".equals(intent.getAction())) {
            // If the app is running
            if (LS_Service.lockscreenActivity_ != null) {
                // Close the app to show the alarm
                // When the alarm is handled, the blank alarms class activity should start the
                // lockscreen back up
                LS_Service.lockscreenActivity_.finish();
                LS_Service.alarmAlert_ = true;
            }
            Log.d(TAG, "LS_Reciever alarm went off");
        }
        Log.d(TAG, "LS_Reciever onReceive end");
    }

    // This will start up the Alarm and Lockscreen activities
    private void startAlarmAndLockscreen(Context ctxt) {
        Intent alarmIntent = new Intent(ctxt, Alarms.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctxt.startActivity(alarmIntent);

        Intent lockscreenIntent = new Intent(ctxt, NewLockscreen.class);
        lockscreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctxt.startActivity(lockscreenIntent);
    }

    /*
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }else{
            return false;
        }
    }*/
}