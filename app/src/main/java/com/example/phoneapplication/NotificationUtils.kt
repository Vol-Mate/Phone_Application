package com.example.phoneapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import java.util.*


class NotificationUtils(private val context: Context) {

    fun scheduleNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 35)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}

//package com.example.phoneapplication
//
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.format.DateFormat
//import android.view.LayoutInflater
//import code.with.cal.localnotificationstutorial.databinding.ActivityMainBinding
//import java.util.*
//
//class NotificationUtils : AppCompatActivity()
//{
//    private val timePicker: Any
//    private val submitButton: Any
//    private lateinit var binding : NotificationUtils
//
//    override fun onCreate(savedInstanceState: Bundle?)
//    {
//        super.onCreate(savedInstanceState)
//        binding = NotificationUtils().inflate(layoutInflater)
//        setContentView(binding.root)
//
//        createNotificationChannel()
//        binding.submitButton.setOnClickListener { scheduleNotification() }
//    }
//
//    private fun inflate(layoutInflater: LayoutInflater): NotificationUtils {
//
//    }
//
//    private fun scheduleNotification()
//    {
//        val intent = Intent(applicationContext, Notification::class.java)
//        val title = binding.titleET.text.toString()
//        val message = binding.messageET.text.toString()
//        intent.putExtra(titleExtra, title)
//        intent.putExtra(messageExtra, message)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            notificationID,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        val time = getTime()
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            time,
//            pendingIntent
//        )
//        showAlert(time, title, message)
//    }
//
//    private fun showAlert(time: Long, title: String, message: String)
//    {
//        val date = Date(time)
//        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
//        val timeFormat = DateFormat.getTimeFormat(applicationContext)
//
//        AlertDialog.Builder(this)
//            .setTitle("Notification Scheduled")
//            .setMessage(
//                "Title: " + title +
//                        "\nMessage: " + message +
//                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
//            .setPositiveButton("Okay"){_,_ ->}
//            .show()
//    }
//
//    private fun getTime(): Long
//    {
//        val minute = binding.timePicker.minute
//        val hour = binding.timePicker.hour
//        val day = binding.datePicker.dayOfMonth
//        val month = binding.datePicker.month
//        val year = binding.datePicker.year
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, day, hour, minute)
//        return calendar.timeInMillis
//    }
//
//    private fun createNotificationChannel()
//    {
//        val name = "Notif Channel"
//        val desc = "A Description of the Channel"
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(channelID, name, importance)
//        channel.description = desc
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//}
//
//
//
//
//
//
//






