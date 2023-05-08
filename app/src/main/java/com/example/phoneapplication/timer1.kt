//package com.example.phoneapplication
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.widget.Toast
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import java.util.*
//
//class timer1 : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val builder = NotificationCompat.Builder(context, "default")
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle("Have a nice date")
//            .setContentText("It's 10:55 pm on Friday!")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(0, builder.build())
//        }
//    }
//}
//fun scheduleNotification(context: Context) {
//    val calendar = Calendar.getInstance()
//    calendar.timeInMillis = System.currentTimeMillis()
//    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
//    calendar.set(Calendar.HOUR_OF_DAY, 10) // set hour to 10pm
//    calendar.set(Calendar.MINUTE, 55) // set minute to 55
//    calendar.set(Calendar.SECOND, 0) // set seconds to 0
//    calendar.set(Calendar.MILLISECOND, 0) // set milliseconds to 0
//
//    val intent = Intent(context, timer1::class.java)
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//
//    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    alarmManager.setRepeating(
//        AlarmManager.RTC_WAKEUP,
//        calendar.timeInMillis,
//        AlarmManager.INTERVAL_DAY * 7,
//        pendingIntent
//    )
//}
//
//
//
//class Timer2 : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_timer1)
//
//        scheduleNotification(this.applicationContext)
//    }
//}
//
