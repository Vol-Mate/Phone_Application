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
        //FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_match)
        showMatches()
    }

    private fun showMatches() {
        lateinit var databaseRef: DatabaseReference
        //lateinit var refToMyDate: DatabaseReference
        //lateinit var refToLocation: DatabaseReference
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com")

        var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        val user = mAuth.currentUser
        user?.let {
            val myId = it.uid
            databaseRef = database.getReference("users")

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val valueOfDate =
                        dataSnapshot.child(myId).child("dateThisWeek").getValue(String::class.java)
                    val nameOfDate = dataSnapshot.child(valueOfDate!!).child("name").getValue(String::class.java)
                    val valueOfLocation =
                        dataSnapshot.child(myId).child("dateLocation").getValue(String::class.java)


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