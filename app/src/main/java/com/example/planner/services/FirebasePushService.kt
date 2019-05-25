package com.example.planner.services

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebasePushService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage?.from)
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(token)
    }
}
