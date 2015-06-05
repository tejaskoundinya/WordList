package com.tejaskoundinya.wordlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WordReminderReceiver extends BroadcastReceiver {
    public WordReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get word and meaning data from Intent Extra
        Bundle bundle = intent.getExtras();
        String word = bundle.getString("word");
        String meaning = bundle.getString("meaning");

        // Create notification
        Notification notification = new Notification.Builder(context)
                .setContentTitle(word)
                .setContentText(meaning)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
