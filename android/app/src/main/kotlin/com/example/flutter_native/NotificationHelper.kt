package com.example.flutter_native

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import io.flutter.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

object NotificationHelper {
    private const val CHANNEL_ID = "media_notification_channel"
    private const val CHANNEL_NAME = "Ideas"
    private const val NOTIFICATION_ID = 101
    const val KEY_TEXT_REPLY = "key_text_reply"

    // add following method
    fun showNotification(context: Context ,title: String, content: String) {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = "channel"
            channel.enableVibration(false)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            notificationManager.createNotificationChannel(channel)
        }

        Log.d("NotificationReceiver", "show noti")
        val resultIntent = Intent(context, MediaActionBroadcastReceiver::class.java)

        val resultPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            resultIntent,
            0
        )
//        val playAction = NotificationCompat.Action(
//            android.R.drawable.ic_media_play,
//            "Play",
//            PendingIntent.getBroadcast(
//                context,
//                0,
//                Intent("PLAY_ACTION"),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )
//        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
//            .build()
        val playAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_play,
            "Play", resultPendingIntent)
          //  .addRemoteInput(remoteInput)
            .build()

//        val pauseAction = NotificationCompat.Action(
//            android.R.drawable.ic_media_pause,
//            "Pause",
//            PendingIntent.getBroadcast(
//                context,
//                0,
//                Intent("PAUSE_ACTION"),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )
//
//        val nextAction = NotificationCompat.Action(
//            android.R.drawable.ic_media_next,
//            "Next",
//            PendingIntent.getBroadcast(
//                context,
//                0,
//                Intent("NEXT_ACTION"),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )
//
//        val previousAction = NotificationCompat.Action(
//            android.R.drawable.ic_media_previous,
//            "Previous",
//            PendingIntent.getBroadcast(
//                context,
//                0,
//                Intent("PREVIOUS_ACTION"),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            //.setDefaults(Notification.DEFAULT_ALL)
//            .setContentTitle(title)
//            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Got new ideas? Add Here!")
            .setColor(Color.BLUE)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1) // Indexes of play and pause actions
//                .setMediaSession(MediaSessionCompat(context, "mediaSession").sessionToken)
            )
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
           // .addAction(previousAction)
            .addAction(playAction)
            //.addAction(nextAction)
            .setOngoing(true)
            .build()
        notificationManager.notify(1, notification)
    }
}