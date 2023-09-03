package com.example.flutter_native



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodCall

import android.content.Intent
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.flutter.Log
import io.flutter.embedding.engine.FlutterEngineCache
import androidx.media.app.NotificationCompat as MediaNotificationCompat

class MainActivity: FlutterActivity() {
    private val CHANNEL_NAME = "channel"
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcastReceivers()
    }

    private fun registerBroadcastReceivers() {
        // ... repeat the above process for other actions ...
        val filter = IntentFilter()
        filter.addAction("PLAY_ACTION")
        filter.addAction("PAUSE_ACTION")
        filter.addAction("NEXT_ACTION")
        filter.addAction("PREVIOUS_ACTION")
        Log.d("NotificationReceiver","adding action done")
        registerReceiver(MediaActionBroadcastReceiver(), filter)
    }

    //    private val CHANNEL = "player/color_channel"
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
//        NotificationHelper.showNotification(this, "this is title",
//            "this is content"
//        )
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_NAME)
        NativeMethodChannel.configureChannel(methodChannel)
        methodChannel.setMethodCallHandler{
                 call, result ->
             when {
                 call.method.equals("showPersistentNotification") -> {
                     val title = call.argument<String>("title")
                     val content = call.argument<String>("content")
                     val isPlay = call.argument<Boolean>("isPlay")
                     if (title != null && content != null) {
                         showPersistentMediaNotification(this, title, content, isPlay!!)
                         result.success(null)
                     } else {
                         result.error("MISSING_ARGS", "Title and content are required", null)
                     }
                 }
             }
         }
    }

    private fun changeColor(call: MethodCall, result: MethodChannel.Result) {
        var color = call.argument<String>("color");
        result.success(color);
    }

//    private fun showPersistentNotification(title: String, content: String) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(CHANNEL_ID, "Persistent Notification", NotificationManager.IMPORTANCE_LOW)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle(title)
//            .setContentText(content)
//            .setSmallIcon(android.R.drawable.ic_dialog_info)
//            .setOngoing(true)
//            .build()
//
//        notificationManager.notify(1, notification)
//    }

    private fun showPersistentMediaNotification(context: Context, title: String, content: String ,isPlay: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "media_notification_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Media Notification Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }


//        val resultIntent = Intent(context, MediaActionBroadcastReceiver::class.java)
//
//        val resultPendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            resultIntent,
//            0
//        )
//        val playAction = NotificationCompat.Action(
//            android.R.drawable.ic_media_play,
//            "Play",
//            resultPendingIntent
//        )



        val playAction = NotificationCompat.Action(
            android.R.drawable.ic_media_play,
            "Play",
            PendingIntent.getBroadcast(
                context,
                0,
                Intent("PLAY_ACTION"),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )


        val pauseAction = NotificationCompat.Action(
            android.R.drawable.ic_media_pause,
            "Pause",
            PendingIntent.getBroadcast(
                context,
                0,
                Intent("PAUSE_ACTION"),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val nextAction = NotificationCompat.Action(
            android.R.drawable.ic_media_next,
            "Next",
            PendingIntent.getBroadcast(
                context,
                0,
                Intent("NEXT_ACTION"),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val previousAction = NotificationCompat.Action(
            android.R.drawable.ic_media_previous,
            "Previous",
            PendingIntent.getBroadcast(
                context,
                0,
                Intent("PREVIOUS_ACTION"),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1) // Indexes of play and pause actions
//                .setMediaSession(MediaSessionCompat(context, "mediaSession").sessionToken)
               )
            .addAction(previousAction)
            .addAction(if(isPlay) playAction else pauseAction)
            .addAction(nextAction)
            .setOngoing(true)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        lateinit var methodChannel: MethodChannel
    }
}
