package com.example.phoneapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import com.example.phoneapplication.arrayAdapter
import com.example.phoneapplication.cards
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Keep
class User3(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val gender: String = "",
    val genderPref: String = "",
    var answer: String= ""
) {

    // add other methods as needed
    constructor() : this("", "", "", 0,"", "","")
}

open class swap_activity : AppCompatActivity(){
    val database = Firebase.database("https://phone-application-14522.firebaseio.com/")
    val usersRef = database.getReference("users")
    private lateinit var cards: Array<cards>
    private lateinit var arrayAdapter: arrayAdapter
    private var i: Int = 0
    private lateinit var mAuth: FirebaseAuth
    private lateinit var listView: ListView
    private lateinit var rowItems: MutableList<cards>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            val userRef = usersRef.child(userId)
            //checkUserSex()

            rowItems = ArrayList()

            arrayAdapter = arrayAdapter(this, R.layout.item, rowItems)

            val flingContainer = findViewById<View>(R.id.frame) as SwipeFlingAdapterView

            flingContainer.adapter = arrayAdapter
            flingContainer.setFlingListener(object : onFlingListener {
                override fun removeFirstObjectInAdapter() {
                    Log.d("LIST", "removed object!")
                    rowItems.removeAt(0)
                    arrayAdapter.notifyDataSetChanged()
                }

                override fun onLeftCardExit(dataObject: Any) {
                    val obj = dataObject as cards
                    val user = obj.getUserId()
                    userRef.child(userId).child("connections").child("nope").child(user)
                        .setValue(true)
                    Toast.makeText(this@swap_activity, "Left", Toast.LENGTH_SHORT).show()
                }

                override fun onRightCardExit(dataObject: Any) {
                    val obj = dataObject as cards
                    val user = obj.getUserId()
                    userRef.child(userId).child("connections").child("yeps").child(user)
                        .setValue(true)
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
    }

    private lateinit var userSex: String
    private lateinit var oppositeUserSex: String

    // isConnectionMatch() function would go here

    private fun checkUserSex() {
        val user = FirebaseAuth.getInstance().currentUser
        val userRef = usersRef.child(user?.uid ?: "")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User3::class.java)
                    user?.let {
                        userSex = user.gender
                        oppositeUserSex = user.genderPref
                        getOppositeSexUsers()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getOppositeSexUsers() {
        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid
        usersRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User3::class.java)
                if (user != null && user.gender == oppositeUserSex && dataSnapshot.key != userId) {
                    if (!dataSnapshot.child("connections").child("nope").hasChild(userId.toString())
                        && !dataSnapshot.child("connections").child("yeps").hasChild(userId.toString())) {
                        val item = cards(dataSnapshot.key ?: "", user.name, user.age, user.answer)
                        rowItems.add(item)
                        arrayAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}

