package com.example.phoneapplication

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val builder = NotificationCompat.Builder(context!!, "channel_id")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Have a nice date!")
            .setContentText("Have a nice date!")
             .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}

//package code.with.cal.localnotificationstutorial
//
//import android.app.NotificationManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.app.NotificationCompat
//
//const val notificationID = 1
//const val channelID = "channel1"
//const val titleExtra = "titleExtra"
//const val messageExtra = "messageExtra"
//
//class Notification : BroadcastReceiver()
//{
//    override fun onReceive(context: Context, intent: Intent)
//    {
//        val notification = NotificationCompat.Builder(context, channelID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle(intent.getStringExtra(titleExtra))
//            .setContentText(intent.getStringExtra(messageExtra))
//            .build()
//
//        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.notify(notificationID, notification)
//    }
//
//}
