package com.example.flutter_native

import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import androidx.core.app.RemoteInput
import io.flutter.Log

class MediaActionBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        val remoteInput = RemoteInput.getResultsFromIntent(intent)
//        if (remoteInput != null) {
//            val title = remoteInput.getCharSequence(
//                NotificationHelper.KEY_TEXT_REPLY).toString()
//            Log.d("NotificationReceiver", title)
//            NativeMethodChannel.showNewIdea(title) // add this line to invoke method channel
////            NotificationHelper.showNotification(context,"hello","123")
//        }

       Log.d("NotificationReceiver", intent.action.toString())
        Log.d("NotificationReceiver", "hello123")

        when (intent?.action) {
            "PLAY_ACTION" -> {
                Log.d("NotificationReceiver", "play")
                NativeMethodChannel.playAction()

            }
            "PAUSE_ACTION" -> {
                NativeMethodChannel.pauseAction()
                // Handle pause action: Pause media playback
            }
            "NEXT_ACTION" -> {
                NativeMethodChannel.nextAction()
                // Handle next action: Play the next media item
            }
            "PREVIOUS_ACTION" -> {
                NativeMethodChannel.previousAction()
                // Handle previous action: Play the previous media item
            }
        }
    }
}