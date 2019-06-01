package com.example.planner.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.planner.MainActivity
import com.example.planner.R
import com.example.planner.task.Task
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val INTENT_ACTIVITY_NAME = "android.intent.action.MAIN"
const val CHANNEL_ID = "notification_channel_id"

class FirebasePushService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val action = remoteMessage?.data?.get("action")

        val notificationTitle = when (action) {
            "add" -> getString(R.string.notificationTitleAdd)
            "edit" -> getString(R.string.notificationTitleEdit)
            else -> getString(R.string.notificationTitleRemove)
        }
        val notificationBody = when (action) {
            "remove" -> getString(R.string.notificationDescriptionClose)
            else -> getString(R.string.notificationDescriptionMore)
        }

        val taskId = remoteMessage?.data?.get("task_id")
        val taskTitle = remoteMessage?.data?.get("task_title")
        val taskDescription = remoteMessage?.data?.get("task_description")
        val taskFav = remoteMessage?.data?.get("task_fav")
        val taskDone = remoteMessage?.data?.get("task_done")

        val task = Task(
            taskTitle,
            taskDescription,
            taskId?.toInt() ?: 0,
            taskFav?.toBoolean() ?: false,
            taskDone?.toBoolean() ?: false
        )


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setColor(
                Color.parseColor("#a094b7")
            )
            .setGroupSummary(true)
            .setAutoCancel(true)


        val intent = Intent(this, MainActivity::class.java)
        if (action != "remove") {
            intent.putExtra("task", task)
        }
        intent.putExtra("action", action)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        notificationBuilder.setContentIntent(resultPendingIntent)


        val mNotificationId = 1
        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotifyMgr.notify(mNotificationId, notificationBuilder.build())
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(token)
    }
}
