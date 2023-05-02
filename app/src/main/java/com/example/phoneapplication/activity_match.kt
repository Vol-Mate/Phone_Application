package com.example.phoneapplication
//import java.util.Vector
import android.util.Log
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// told to do this from online

// everytime you click this button, it gives you a new date under "dateThisWeek"
class myUser(
    internal var userId: String = "",
){}

/*data class myUser2(
    var myDate: String = ""
)*/

class activity_match : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        // where we call functions ie setButton()
        match()
    }

    // where we define our functions ie
    // private fun nameOfFunc(){
    //   code
    // }
    private fun match() {
        // acts like a vector
        val matchList = mutableListOf<String>()

        // get a reference of the database
        lateinit var userRef: DatabaseReference
        lateinit var myRef: DatabaseReference
        lateinit var refToDate: DatabaseReference
        lateinit var refToLocation: DatabaseReference
        lateinit var refToDateLocation: DatabaseReference
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com")
        //val refDatabase = database.getReference("dateThisWeek")

        // get this user specifically
        var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        //val userId = mAuth.currentUser?.uid

        // got this idea from https://firebase.google.com/docs/auth/android/manage-users
        val user = mAuth.currentUser
        user?.let {
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val myId = it.uid
            //val dateUID = it.

            // get to all of the matches in the database under the user
            userRef = database.getReference("users").child(myId).child("connections")
            myRef = database.getReference("users").child(myId)
                .child("dateThisWeek")//.child("dateThisWeek")
            refToLocation = database.getReference("users").child(myId).child("dateLocation")
            //refToDate = database.getReference("users").child(myId).child("dateLocation")

            // use addListenerForSingleEvent to see if there is already a date there
            // If there is, don't match, but if there isn't, match
            myRef.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val valueOfDate = dataSnapshot.getValue(String::class.java)
                    if(valueOfDate == ""){
                        userRef.addChildEventListener(object : ChildEventListener {
                            // loop inspired by https://stackoverflow.com/questions/50372353/iterate-through-firebase-database-using-kotlin
                            override fun onChildAdded(
                                snapshot: DataSnapshot,
                                previousChildName: String?
                            ) {
                                // only do the following if the user has "" for "dateThisWeek"
                                //println(snapshot.value.toString())
                                // if (snapshot.getValue(String::class.java).toString() == "") {
                                for (childSnapshot in snapshot.children) {
                                    //val childValue = childSnapshot.getValue(String::class.java)
                                    // Do something with the child value
                                    //val name = childSnapshot.child("connections").child("yeps").value
                                    if (childSnapshot.value == true) {
                                        //Log.i(TAG, childSnapshot.key.toString())
                                        matchList.add(childSnapshot.key.toString())
                                    }
                                }
                                //Log.i(TAG, matchList)
                                // randomize matchList and pick one
                                matchList.shuffle()
                                if (matchList.isNotEmpty()) {
                                    println(matchList.first())
                                    //myRef.setValue(matchList.first())
                                    //val myDateThisWeek = mapOf("dateThisWeek" to matchList.first())
                                    myRef.setValue(matchList.first())
                                    // set date's date to this person too
                                    refToDate = database.getReference("users").child(matchList.first())
                                    refToDate.child("dateThisWeek").setValue(myId)

                                    // give them a place to meet
                                    val locationList = /*mutableListOf<String>()*/ listOf("Bench in front of Ayres", "Engineering Courtyard",
                                                               "HSS Lawn at table", "Min Kao 6th floor patio",
                                                               "SU in front of Starbucks", "Starbucks in Hodges")
                                    val dateLocation = locationList[(0..5).random()]
                                    println("Date location: " + dateLocation)
                                    refToLocation.setValue(dateLocation)
                                    //same location for date
                                    refToDateLocation = database.getReference("users").child(matchList.first())
                                    refToDateLocation.child("dateLocation").setValue(dateLocation)

                                } else {
                                    myRef.setValue("None")

                                }
                            }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                            TODO("Not yet implemented")
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            TODO("Not yet implemented")
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
            /*userRef.addChildEventListener(object : ChildEventListener {
                // loop inspired by https://stackoverflow.com/questions/50372353/iterate-through-firebase-database-using-kotlin
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    // only do the following if the user has "" for "dateThisWeek"
                    //println(snapshot.value.toString())
                   // if (snapshot.getValue(String::class.java).toString() == "") {
                        for (childSnapshot in snapshot.children) {
                            //val childValue = childSnapshot.getValue(String::class.java)
                            // Do something with the child value
                            //val name = childSnapshot.child("connections").child("yeps").value
                            if (childSnapshot.value == true) {
                                //Log.i(TAG, childSnapshot.key.toString())
                                matchList.add(childSnapshot.key.toString())
                            }
                        }
                        //Log.i(TAG, matchList)
                        // randomize matchList and pick one
                        matchList.shuffle()
                        if (matchList.isNotEmpty()) {
                            println(matchList.first())
                            //myRef.setValue(matchList.first())
                            //val myDateThisWeek = mapOf("dateThisWeek" to matchList.first())
                            myRef.setValue(matchList.first())
                            // set date's date to this person too
                            refToDate = database.getReference("users").child(matchList.first())
                            refToDate.child("dateThisWeek").setValue(myId)
                        }
                        else{
                            myRef.setValue("None")

                        }
                    }
                //}

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }*/


                // userRef = database.getReference(myId)/*.child("connections").child("yeps")*//*.child(userId!!).child("connections"). child("yeps")*/

                // go through this user's matches header
                //userRef.addListenerForSingleValueEvent()
                // got this online

                /*       userRef.addChildEventListener(object : ChildEventListener {
        // loop inspired by https://stackoverflow.com/questions/50372353/iterate-through-firebase-database-using-kotlin
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            for (childSnapshot in snapshot.children) {
                //val childValue = childSnapshot.getValue(String::class.java)
                // Do something with the child value
                //val name = childSnapshot.child("connections").child("yeps").value
                if (childSnapshot.value == true) {
                    //Log.i(TAG, childSnapshot.key.toString())
                    matchList.add(childSnapshot.key.toString())
                }
            }
            //Log.i(TAG, matchList)
            // randomize matchList and pick one
            matchList.shuffle()
            if (matchList.isNotEmpty()) {
                println(matchList.first())
                //myRef.setValue(matchList.first())
                //val myDateThisWeek = mapOf("dateThisWeek" to matchList.first())
                myRef.setValue(matchList.first())
                refToDate.getReference("users").child(matchList.first()).child("dateThisWeek").setValue()
            }*/


                // need to figure out how not to give each person more than 1 date a week and both
                // people are alerted abt the date they have w one another!!

                // can make the button a "click here when finished swiping" button and it says wait to be matched
                // or something and does all this and then could we insert it as a node under that person
                // then show notif every week and deletes the node of the person they were dating that week

            //})
        }
    }
}
