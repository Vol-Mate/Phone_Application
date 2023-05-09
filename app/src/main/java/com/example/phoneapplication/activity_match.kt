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


// global variable to help w while loop later in code
var otherPersonDate: String = ""
var myDateThisWeek: String = ""
var dateLocation: String = ""

// everytime you click this button, it gives you a new date under "dateThisWeek"
class myUser(
    internal var userId: String = "",
){}

class activity_match : AppCompatActivity() {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        //FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        val notificationUtils = NotificationUtils(this)
        notificationUtils.scheduleNotification()
        setButton()

        // where we call functions ie setButton()
        match()
    }

    private fun setButton() {
        button = findViewById<Button>(R.id.matches)
        button.setOnClickListener {
            val intent = Intent(this@activity_match, show_match::class.java)
            startActivity(intent)
        }
    }

    // where we define our functions ie
    // private fun nameOfFunc(){
    //   code
    // }
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
            databaseRef = database.getReference("users")//.child(myId)//.child("connections")
            connectionsRef = database.getReference("users").child(myId).child("connections")
            refToMyDate = database.getReference("users").child(myId).child("dateThisWeek")
            refToLocation = database.getReference("users").child(myId).child("dateLocation")

            // use addListenerForSingleEvent to see if there is already a date there
            // If there is, don't match, but if there isn't, match
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val valueOfDate =
                        dataSnapshot.child(myId).child("dateThisWeek").getValue(String::class.java)
                    println("valueOfDate" + valueOfDate)
                    //                dateLocation = dataSnapshot.child(myId).child("dateLocation")
                    //                   .getValue(String::class.java)!!
                    println("first dateLocation: " + dateLocation)

                    if (valueOfDate == "") {
                        connectionsRef.addChildEventListener(object : ChildEventListener {
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
                                println("matchList: " + matchList)
                                //Log.i(TAG, matchList)
                                // randomize matchList and pick one
                                matchList.shuffle()
                                if (matchList.isNotEmpty()) {
                                    //println(matchList.first())
                                    //refToDate = database.getReference("users").child(matchList.first()).child("dateThisWeek")

                                    // do while matchList is not empty and the date has "" in its date location
                                    // set date = matchList.first() and remove it

                                    //   override fun onDataChange(myss: DataSnapshot) {
                                    //otherPersonDate =
                                    //   dataSnapshot.child(matchList.first()).child("dateThisWeek")
                                    //       .getValue(String::class.java)!!
                                    //println("before loop other date" + otherPersonDate)
                                    //println("before loop my date: " + matchList.first())

                                    // I think this works correctly now yayayay
                                    do {
                                        myDateThisWeek = matchList.first()
                                        println("my date: " + myDateThisWeek)
                                        //refToDate = database.getReference("users").child(matchList.first()).child("dateThisWeek")
                                        //val otherDate =
                                        //   dataSnapshot.child(potentialDateThisWeek).child("dateThisWeek")
                                        //      .getValue(String::class.java)
                                        //println("val of other person's date" + otherDate)

                                        //refToMyDate.setValue(potentialDateThisWeek)

                                        //refToOthersDate = database.getReference("users").child(m).child("dateThisWeek")
                                        println("ere")
                                        otherPersonDate = dataSnapshot.child(matchList.first())
                                            .child("dateThisWeek")
                                            .getValue(String::class.java)!!
                                        println("other person's date: " + otherPersonDate)
                                        //refToOthersDate.setValue(myId)

                                        matchList.remove(matchList.first())
                                        println("removing it")
                                    } while (matchList.isNotEmpty() && otherPersonDate != "")
                                    println("outside of while loop")
                                    // if matchlist is empty, no date
                                    if (matchList.isEmpty() && otherPersonDate != "") {
                                        refToMyDate.setValue("None")

                                    }
                                    // else, set values here
                                    else {
                                        refToMyDate.setValue(myDateThisWeek)
                                        refToOthersDate =
                                            database.getReference("users").child(myDateThisWeek)
                                                .child("dateThisWeek")
                                        refToOthersDate.setValue(myId)

                                        // give them a place to meet
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
                                        println("Date location: " + dateLocation)
                                        refToLocation.setValue(dateLocation)
                                        //same location for date
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

                    // print out the info
                    // have to find it again in database and not use the data variable because
                    // there is a possibility that they are clicking this button and coming to
                    // this page after they have already gotten their match

                    // go to refToMyDate and print it out
                    // need to find the value of the date and then go to that date and find
                    // the name
                    /*databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dsnapshot: DataSnapshot) {
                            // val dateName = dsnapshot.child().getValue(String::class.java)
                            val datePrintedOut = findViewById<TextView>(R.id.dateName)
                            // datePrintedOut.text = printOutDate // might be setText
                            // if that place != null else the datePrintedOut = "None- sorry!"
                        }
                        override fun onCancelled(error: DatabaseError) { }
                    })*/
                    //refToLocation.addListenerForSingleValueEvent(object : ValueEventListener{
                    //    override fun onDataChange(myNewSS: DataSnapshot){
                    //        val printOutLocation = myNewSS.getValue(String::class.java)
                    /*println("dateLocation: " + dateLocation)
                    val locationPrintedOut = findViewById<TextView>(R.id.locationText)
                    locationPrintedOut.text = dateLocation // might be setText

                    println("name of date: " + myDateThisWeek)
                    val nameOfDate = dataSnapshot.child(myDateThisWeek).child("name").getValue(String::class.java)
                    val datePrintedOut = findViewById<TextView>(R.id.dateName)
                    datePrintedOut.text = nameOfDate*/
                    //   }

                    //   override fun onCancelled(error: DatabaseError) { }

                    //})
                }

                override fun onCancelled(error: DatabaseError) { }
            })
        }
    }
}



