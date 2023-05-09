package com.example.phoneapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class show_match : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_match)
        showMatches()
    }

    private fun showMatches() {
        // Get an instance of the database
        lateinit var databaseRef: DatabaseReference
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com")

        var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        user?.let {
            val myId = it.uid
            databaseRef = database.getReference("users")

            // Use addListenerForSingleValueEvent to get a value one time
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get the name of the date
                    val valueOfDate =
                        dataSnapshot.child(myId).child("dateThisWeek").getValue(String::class.java)
                    val nameOfDate = dataSnapshot.child(valueOfDate!!).child("name").getValue(String::class.java)

                    // Get the value of the date
                    val valueOfLocation =
                        dataSnapshot.child(myId).child("dateLocation").getValue(String::class.java)

                    // Display both date name and location on the app itself
                    val datePrintedOut = findViewById<TextView>(R.id.dateName)
                    datePrintedOut.text = nameOfDate // might be setText

                    val locationPrintedOut = findViewById<TextView>(R.id.locationText)
                    locationPrintedOut.text = valueOfLocation // might be setText

                }

                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }
}