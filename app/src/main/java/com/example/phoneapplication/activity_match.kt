// Last edited: 5/9/2023
// This file will do the matching in the database (get and sets values) with both
// the date and the location.
package com.example.phoneapplication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// Global variables to help with while loop later in code
var otherPersonDate: String = ""
var myDateThisWeek: String = ""
var dateLocation: String = ""

class myUser(
    internal var userId: String = "",
){}

class activity_match : AppCompatActivity() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        val notificationUtils = NotificationUtils(this)
        notificationUtils.scheduleNotification()
        setButton()

        // match() function will actually do the matching in the database
        match()
    }

    // When clicking this button, will take us to the place where matches are shown
    private fun setButton() {
        button = findViewById<Button>(R.id.matches)
        button.setOnClickListener {
            val intent = Intent(this@activity_match, show_match::class.java)
            startActivity(intent)
        }
    }

    private fun match() {
        // acts like a vector
        val matchList = mutableListOf<String>()

        // get a reference of the database
        lateinit var databaseRef: DatabaseReference
        lateinit var connectionsRef: DatabaseReference
        lateinit var refToMyDate: DatabaseReference
        lateinit var refToOthersDate: DatabaseReference
        lateinit var refToLocation: DatabaseReference
        lateinit var refToDateLocation: DatabaseReference
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com")

        // Get this user specifically
        var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // got this idea from https://firebase.google.com/docs/auth/android/manage-users
        val user = mAuth.currentUser
        user?.let {
            // Get user's id
            val myId = it.uid

            // References to different locations in the database so we can set values later on
            databaseRef = database.getReference("users")
            connectionsRef = database.getReference("users").child(myId).child("connections")
            refToMyDate = database.getReference("users").child(myId).child("dateThisWeek")
            refToLocation = database.getReference("users").child(myId).child("dateLocation")

            // use addListenerForSingleEvent to see if there is already a date there
            // If there is, don't match, but if there isn't (there is a "" there), match
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val valueOfDate =
                        dataSnapshot.child(myId).child("dateThisWeek").getValue(String::class.java)

                    if (valueOfDate == "") {
                        connectionsRef.addChildEventListener(object : ChildEventListener {
                            override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                // Loop inspired by https://stackoverflow.com/questions/50372353/iterate-through-firebase-database-using-kotlin
                                for (childSnapshot in snapshot.children) {
                                    // Add it to the mutableList if the user is under "yeps" under the user in the database
                                    if (childSnapshot.value == true) {
                                        matchList.add(childSnapshot.key.toString())
                                    }
                                }

                                // Randomize our mutableList
                                matchList.shuffle()
                                if (matchList.isNotEmpty()) {

                                    // Keep going through the matches in our mutableList until we find one that also
                                    // does not have a date (has "" under dateThisWeek)
                                    do {
                                        myDateThisWeek = matchList.first()

                                        otherPersonDate = dataSnapshot.child(matchList.first())
                                            .child("dateThisWeek")
                                            .getValue(String::class.java)!!

                                        matchList.remove(matchList.first())

                                    } while (matchList.isNotEmpty() && otherPersonDate != "")

                                    // If matchList is empty, the user will not get a date
                                    if (matchList.isEmpty() && otherPersonDate != "") {
                                        refToMyDate.setValue("None")

                                    }
                                    else {
                                        refToMyDate.setValue(myDateThisWeek)
                                        // Set date's dateThisWeek to this userID
                                        refToOthersDate =
                                            database.getReference("users").child(myDateThisWeek)
                                                .child("dateThisWeek")
                                        refToOthersDate.setValue(myId)

                                        // Give them a place to meet
                                        val locationList =
                                            listOf(
                                                "Bench in front of Ayres",
                                                "Engineering Courtyard",
                                                "HSS Lawn at table",
                                                "Min Kao 6th floor patio",
                                                "SU in front of Starbucks",
                                                "Starbucks in Hodges"
                                            )
                                        dateLocation = locationList[(0..5).random()]
                                        refToLocation.setValue(dateLocation)

                                        // Same location for the date
                                        refToDateLocation = database.getReference("users")
                                            .child(myDateThisWeek).child("dateLocation")
                                        refToDateLocation.setValue(dateLocation)
                                    }

                                } else {
                                    refToMyDate.setValue("None")
                                }
                            }

                            override fun onChildChanged(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                            }

                            override fun onChildRemoved(snapshot: DataSnapshot) {}

                            override fun onChildMoved(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })

                    }
                }
                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }
}



