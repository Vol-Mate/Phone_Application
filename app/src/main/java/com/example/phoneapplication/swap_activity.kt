package com.example.phoneapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Keep
class User5(
    internal var userId: String = "",
    internal var name: String = "",
    internal var age: Int = 0,
    internal var answer: String= "",
    internal var gender: String = "",
    internal var genderPref: String = ""
) {
    constructor() : this("", "", 0,"","","")

    fun getUserId(): String {
        return userId
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getAnswer(): String {
        return answer
    }

    fun setAnswer(answer: String) {
        this.answer = answer
    }

    fun getAge(): Int {
        return age
    }

    fun setAge(age: Int) {
        this.age = age
    }

    fun getGender(): String {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getGenderPref(): String {
        return genderPref
    }

    fun setGenderPref (genderPref: String) {
        this.genderPref = genderPref
    }
}

// Code inspired by: https://www.youtube.com/playlist?list=PLxabZQCAe5fio9dm1Vd0peIY6HLfo5MCf
open class swap_activity : AppCompatActivity(){
    // DatabaseReference objects that will be used to access the Firebase Realtime Database.
    private lateinit var userRef: DatabaseReference
    private lateinit var userRef2: DatabaseReference
    // Cards is an array of cards objects, which represent user profiles that will be displayed to the user.
    private lateinit var cards: Array<cards>
    // arrayAdapter is an adapter used to display the cards objects in a ListView
    private lateinit var arrayAdapter: arrayAdapter<cards>
    private var i: Int = 0
    // mAuth is an instance of FirebaseAuth used for authentication
    private lateinit var mAuth: FirebaseAuth
    private lateinit var listView: ListView
    private lateinit var rowItems: MutableList<cards>

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)
        setButton()
        setButton2()

        // creates a FirebaseDatabase object and initializes it with the URL of the Firebase Realtime Database.
        // It then gets a DatabaseReference object that points to the "users" node of the database, and assigns it to the userRef variable.
        val database = Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        userRef = database.getReference("users")

        // gets current userID
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid

        checkUserSex()

        // create a new ArrayList called rowItems, which will hold the cards objects to be displayed,
        // and a new arrayAdapter object that will display the rowItems in the ListView
        rowItems = ArrayList()

        arrayAdapter = arrayAdapter(this, R.layout.item, rowItems)

        val flingContainer = findViewById<View>(R.id.frame) as SwipeFlingAdapterView

        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object : onFlingListener {
            override fun removeFirstObjectInAdapter() { // Define what happens when the first object is removed from the adapter.
                Log.d("LIST", "removed object!")
                rowItems.removeAt(0) // Remove the first object from the rowItems list.
                arrayAdapter.notifyDataSetChanged() // Notify the adapter that the data has changed.
            }

            // Define what happens when the user swipes left on a card.
            override fun onLeftCardExit(dataObject: Any) {
                val obj = dataObject as cards
                val user = obj.getUserId()
                userRef.child(userId.toString()).child("connections").child("nope").child(user)
                    .setValue(false) // Add the user to the 'nope' connections of the current user.
                Toast.makeText(this@swap_activity, "Left", Toast.LENGTH_SHORT).show()
            }

            // Define what happens when the user swipes right on a card.
            override fun onRightCardExit(dataObject: Any) {
                val obj = dataObject as cards
                val user = obj.getUserId()
                userRef.child(userId.toString()).child("connections").child("yeps").child(user)
                    .setValue(true) // Add the user to the 'yeps' connections of the current user.
                //isConnectionMatch(userId)
                Toast.makeText(this@swap_activity, "Right", Toast.LENGTH_SHORT).show()
            }

                override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}
                override fun onScroll(scrollProgressPercent: Float) {}
            })

            // Optionally add an OnItemClickListener
            flingContainer.setOnItemClickListener { _, _, _, _ ->
                Toast.makeText(this@swap_activity, "Item Clicked", Toast.LENGTH_SHORT).show()
            }
    }
    private fun setButton() {
        button = findViewById<Button>(R.id.Back_Main)
        button.setOnClickListener {
            val intent = Intent(this@swap_activity, main_menu::class.java)
            startActivity(intent)
        }
    }

    private fun setButton2() {
        button = findViewById<Button>(R.id.button4)
        button.setOnClickListener {
            val intent = Intent(this@swap_activity, activity_match::class.java)
            startActivity(intent)
        }
    }

    private lateinit var userSex: String
    private lateinit var oppositeUserSex: String

    // isConnectionMatch() function would go here

    private fun checkUserSex() {
        val database =
            Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        userRef = database.getReference("users")

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser?.uid

        user?.let {
            // Query the "users" node in the Firebase Realtime Database for the current user's data by their userID.
            // and attach a listener to the query to listen for a single value event (i.e., the user's data).
            userRef.orderByChild("userID").equalTo(user)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    // Define a callback to be invoked when the data at the specified location changes.
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve the current user's data and map it to a User5 object using the getValue() method.
                            val currentUser =
                                dataSnapshot.children.firstOrNull()?.getValue(User5::class.java)
                            currentUser?.let {
                                // Set the userSex and oppositeUserSex variables to the current user's gender and preference for the opposite sex, respectively.
                                userSex = currentUser.getGender()
                                oppositeUserSex = currentUser.getGenderPref()
                                if(::userSex.isInitialized && ::oppositeUserSex.isInitialized) {
                                    getOppositeSexUsers()
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
        }
    }

    private fun getOppositeSexUsers() {
        val database = Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        userRef = database.getReference("users")
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid
        // Add a child event listener to the "users" node in the database to listen for changes to the child nodes
        userRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User5::class.java)
                // Check if the retrieved user data is not null and if the user's gender matches the oppositeUserSex
                // Also, check if the user's ID is not equal to the current user's ID to avoid displaying the current user's profile
                if (user != null && user.gender == oppositeUserSex && dataSnapshot.key != userId) {
                    // Check if the current user has not already swiped left or right on the displayed user's profile
                    //if (!dataSnapshot.child("connections").child("nope").hasChild(userId.toString())
                    //    && !dataSnapshot.child("connections").child("yeps").hasChild(userId.toString())) {
                        // Create a new card item using the displayed user's data and add it to the list of row items
                        val item = cards(dataSnapshot.key ?: "", user.name, user.age.toString(),user.answer)
                        rowItems.add(item)
                        arrayAdapter.notifyDataSetChanged()
                   // }
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}

