package com.example.phoneapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class match1(
    internal var dateLocation: String = "",
    internal var dateThisWeek: String = ""
){
    constructor() : this("", "")

    fun getLocation(): String {
        return dateLocation
    }

    fun setLocation(name: String) {
        this.dateLocation = dateLocation
    }

    fun getDate(): String {
        return dateThisWeek
    }

    fun setDate(name: String) {
        this.dateThisWeek = dateThisWeek
    }
}


//class timer : BroadcastReceiver() {
//    @SuppressLint("ServiceCast")
//    override fun onReceive(context: Context, intent: Intent) {
//        // Retrieve data from your database
//        val db = MyDatabaseHelper(context)
//        val data = db.getMatchData()
//
//        // Display the data in a pop-up dialog
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Friday Matches")
//        builder.setMessage(data)
//        builder.setPositiveButton("OK", null)
//        builder.show()
//    }
//}

//class MyDatabaseHelper(context: Context){
//    private lateinit var userRef:DatabaseReference
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var dateThisWeek: String
//    private lateinit var dateLocation: String
//    private lateinit var card_match: Array<match_cards>
//    private lateinit var arrayMatchAdapter: Array_match_adapter<match_cards>
//    private var i: Int = 0
//    private lateinit var rowItems: MutableList<match_cards>
//
//    fun getMatchData(): String {
//        // Your code to retrieve the match data from the database
//        val database =
//            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
//        userRef = database.getReference("users")
//
//        mAuth = FirebaseAuth.getInstance()
//        val user = mAuth.currentUser?.uid
//
//        rowItems = ArrayList()
//        arrayMatchAdapter = Array_match_adapter(this, R.layout.activity_match, rowItems)
//
//            user?.let {
//            userRef.orderByChild("userID").equalTo(user)
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            val currentUser =
//                                dataSnapshot.children.firstOrNull()?.getValue(match1::class.java)
//                            currentUser?.let {
//                                dateThisWeek = currentUser.getDate()
//                                dateLocation = currentUser.getLocation()
//                                if(::dateThisWeek.isInitialized && ::dateLocation.isInitialized) {
//                                    val match = match_cards(dataSnapshot.key ?: "", currentUser.dateLocation, currentUser.dateThisWeek)
//                                    rowItems.add(match)
//                                    arrayMatchAdapter.notifyDataSetChanged()
//                                }
//                            }
//                        }
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//
//                    }
//                })
//        }
//        return "Match data"
//    }
//}

class MainActivity9 : AppCompatActivity() {
    lateinit var userRef:DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var card_match: Array<match_cards>
    lateinit var arrayMatchAdapter: Array_match_adapter<match_cards>
    var i: Int = 0
    lateinit var rowItems: MutableList<match_cards>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer1)

        //val userRef = FirebaseDatabase.getInstance().reference.child("users");
        val database = Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        userRef = database.getReference("users")

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid

        getMatchData()

        rowItems = ArrayList()
        arrayMatchAdapter = Array_match_adapter(this, R.layout.activity_timer1, rowItems)

        getMatchData()

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //val intent = Intent(this, MyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 30)

        // If the current time is after 5:30 pm on Friday, schedule the alarm for the next Friday
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    private lateinit var dateThisWeek: String
    private lateinit var dateLocation: String


    private fun getMatchData() {
        // Your code to retrieve the match data from the database
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        lateinit var userRefToDate: DatabaseReference
        //userRefToDate = database.getReference("users").

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser?.uid

        //rowItems = ArrayList()
        //arrayMatchAdapter = Array_match_adapter(this, R.layout.activity_match, rowItems)

        user?.let {
            userRefToDate = database.getReference("users").child(user).child("dateThisWeek")
            //userRefToDate.orderByChild("userID").equalTo(user)
              //  .addListenerForSingleValueEvent(object : ValueEventListener {
                //    override fun onDataChange(dataSnapshot: DataSnapshot) {
                  //      if (dataSnapshot.exists()) {
                            userRefToDate.setValue("")
                            /*val currentUser =
                                dataSnapshot.children.firstOrNull()?.getValue(match1::class.java)
                            currentUser?.let {
                                dateThisWeek = currentUser.getDate()
                                dateLocation = currentUser.getLocation()
                                if (::dateThisWeek.isInitialized && ::dateLocation.isInitialized) {
                                    val match = match_cards(
                                        dataSnapshot.key ?: "",
                                        currentUser.dateLocation,
                                        currentUser.dateThisWeek
                                    )
                                    rowItems.add(match)
                                    arrayMatchAdapter.notifyDataSetChanged()
                                }
                            }*/
                        }
                    }

                    //override fun onCancelled(databaseError: DatabaseError) {}


               // })
        //})
    //}
}



//class MyAlarmReceiver {
//
//}
