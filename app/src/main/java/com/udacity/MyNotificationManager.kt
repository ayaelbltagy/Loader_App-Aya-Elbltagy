package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity.Companion.CHANNEL_ID


fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // to define which activity that notification will return on it after clicking
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        0,
        contentIntent,
        // to update the old not create new
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // create builder for notification
    val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            // make all notification is clickable
           // .setContentIntent(contentPendingIntent)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // make only (check the status) is clickable
            .addAction(R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button), contentPendingIntent)
        // Deliver the notification
        notify(CHANNEL_ID, builder.build())
    }



fun NotificationManager.cancelNotifications() {
    cancelAll()
}


// if you want to cancel the old notification call this
//val notificationManager =
//    ContextCompat.getSystemService(
//        app,
//        NotificationManager::class.java
//    ) as NotificationManager
//notificationManager.cancelNotifications()