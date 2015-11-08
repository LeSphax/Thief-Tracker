package com.example.sbastien.thieftracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
    private NotificationManager mNM;
    private MediaPlayer player;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.alarm_service_started;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        AlarmService getService() {
            Log.i("AlarmService", "OnGetserv");
            return AlarmService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i("AlarmService", "OnCreate");

        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        AudioManager am =
                (AudioManager) getSystemService(AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);

        player = MediaPlayer.create(this, R.raw.alarm_sound_effect);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
        player.start();
        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AlarmService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    public void stopAlarm(){
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.i("AlarmService", "OnDestroy");
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        player.stop();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.alarm_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlarmActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(getText(R.string.alarm_service_label))  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        startForeground(NOTIFICATION,notification);
    }
}
