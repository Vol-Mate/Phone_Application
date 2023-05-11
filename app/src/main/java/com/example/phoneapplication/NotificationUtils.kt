package com.example.phoneapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class NotificationUtils(private val context: Context) {

    fun scheduleNotification() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        // this section schedules the message at a specific time
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
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
            // makes the notification come up at a specific time
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        // Resets "dateThisWeek" in the database to ""
        lateinit var databaseRef: DatabaseReference
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com")

        var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        user?.let {
            // Get user's id
            val myId = it.uid
            databaseRef =database.getReference("users").child(myId).child("dateThisWeek")
            databaseRef.setValue("")
        }
    }
}

