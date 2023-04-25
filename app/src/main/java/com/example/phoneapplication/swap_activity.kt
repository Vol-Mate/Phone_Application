package com.example.phoneapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
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
}

open class swap_activity : AppCompatActivity(){
    private lateinit var userRef: DatabaseReference
    private lateinit var userRef2: DatabaseReference
    private lateinit var cards: Array<cards>
    private lateinit var arrayAdapter: arrayAdapter<cards>
    private var i: Int = 0
    private lateinit var mAuth: FirebaseAuth
    private lateinit var listView: ListView
    private lateinit var rowItems: MutableList<cards>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)

        //val userRef = FirebaseDatabase.getInstance().reference.child("users");
        val database = Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        userRef = database.getReference("users")

        val database2 = Firebase.database("https://phone-application-14522.firebaseio.com/")
        val userRef2 = database.getReference("users")

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth.currentUser?.uid

        if (userId != null) {
            checkUserSex()

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
        val userRef = userRef.child(user?.uid ?: "")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User5::class.java)
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
        userRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val user = dataSnapshot.getValue(User5::class.java)
                if (user != null && user.gender == oppositeUserSex && dataSnapshot.key != userId) {
                    if (!dataSnapshot.child("connections").child("nope").hasChild(userId.toString())
                        && !dataSnapshot.child("connections").child("yeps").hasChild(userId.toString())) {
                        val item = cards(dataSnapshot.key ?: "", user.name, user.age.toString()) // user.answer
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

