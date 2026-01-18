package edu.hayes_rlynchburg.chesspuzzlelockscreen;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.TreeMap;

/**
 * Created by Ryan Hayes on 10/9/2016.
 */
public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "TAG";

    // A receiver to listen for when we get a new notification
    private NotificationReceiver receiver;

    public static int numberOfNotifications;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Listener Notification onCreate");
        // Set up our receiver and register it
        receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_LISTENER");
        registerReceiver(receiver, filter);
        Log.d(TAG, "Listener Notification onCreate end");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Listener Notification onDestroy");
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    // Called when a new notification is added to the status bar
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // If the user wants to see notifications
        if (LS_Service.showNotifications_) {
            Log.d(TAG, "Listener Notification Received");
            super.onNotificationPosted(sbn);

            // Get all notifications on the status bar
            StatusBarNotification[] n = getActiveNotifications();

            // Filter out bad ones
            filterNotifications(n);

            // Let the LS Receiver know that a new notification was received
            sendBroadcast(new Intent("NotificationReceived"));
        }
    }

    // Filter out notifications you don't want and save the new list
    private void filterNotifications(StatusBarNotification[] notifs) {
        // Used to count how many notifications of a certain package we have
        TreeMap<String, Notification> stringNotificationMap = new TreeMap<>();
        numberOfNotifications = 0;

        // New list of notifications to be saved
        Notification[] newList = new Notification[notifs.length];

        for (int i = 0; i < notifs.length; ++i) {
            Log.d(TAG, notifs[i].getPackageName());
            // Get the package of the notification
            String pack = notifs[i].getPackageName();

            // These are the packages currently being filtered out
            if (!pack.equals("android") && !pack.equals("com.android.settings") && !pack.equals("com.android.systemui")) {
                // Get the current notification
                Notification n = notifs[i].getNotification().clone();
                // Avoiding notifications from the same package name (multiple emails, texts etc)
                if (!stringNotificationMap.containsKey(pack)) {
                    // Save the notification in the new list
                    newList[numberOfNotifications] = n;
                    stringNotificationMap.put(pack, n);
                    ++numberOfNotifications;
                } else {
                    stringNotificationMap.get(pack).number++;
                }
            }
        }
        // Save the list of notifications to be displayed
        LS_Service.notifications_ = newList;
    }

    // Called when a notification is removed
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (LS_Service.showNotifications_) {
            Log.d(TAG, "Listener Notification Removed");

            super.onNotificationRemoved(sbn);
            // Send the current list of notifications through the filter
            StatusBarNotification[] n = getActiveNotifications();
            filterNotifications(n);

            // Update the screen
            sendBroadcast(new Intent("NotificationReceived"));
        }
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "NotificationReceiver");
            if ("NotificationReceived".equals(intent.getAction())) {
                // Handle the received notification
            }
        }
    }
}