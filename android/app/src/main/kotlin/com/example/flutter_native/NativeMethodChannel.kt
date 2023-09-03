package com.example.flutter_native

import io.flutter.Log
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Context
object NativeMethodChannel {
    private lateinit var methodChannel: MethodChannel

    fun configureChannel(mainMethodChannel: MethodChannel) {
        methodChannel = mainMethodChannel
    }


    // // add the following method, it passes a string to flutter
    // fun showNewIdea(idea: String) {
    //     methodChannel.invokeMethod("onPlayActionClicked", idea)
    // }

    fun playAction() {
        Log.d("NotificationReceiver", "playActon")
        methodChannel.invokeMethod("onPlayActionClicked", "hello play")
    }
    fun pauseAction() {
        methodChannel.invokeMethod("onPauseActionClicked", "hello pause")
    }
    fun previousAction() {
        methodChannel.invokeMethod("onPreviousActionClicked", null)
    }
    fun nextAction() {
        methodChannel.invokeMethod("onNextActionClicked", null)
    }
}

