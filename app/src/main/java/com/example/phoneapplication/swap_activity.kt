package com.example.phoneapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import com.example.phoneapplication.arrayAdapter;
import com.example.phoneapplication.cards;

open class swap_activity : AppCompatActivity(){
    protected lateinit var cards_data: Array<cards>
    private var arrayAdapter: arrayAdapter? = null
    private val i = 0
    private lateinit var auth: FirebaseAuth
    private var currentUId: String? = null
    private var usersDb: DatabaseReference? = null

    var listView: ListView? = null
    var rowItems: MutableList<cards>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swap)
        usersDb = FirebaseDatabase.getInstance().reference.child("users")
        auth = FirebaseAuth.getInstance()
        currentUId = auth!!.currentUser!!.uid
        checkUserSex()
        rowItems = ArrayList<cards>()
        arrayAdapter = arrayAdapter(this, R.layout.item, rowItems as ArrayList<cards>)
        val flingContainer = findViewById<View>(R.id.frame) as SwipeFlingAdapterView
        flingContainer.adapter = arrayAdapter
        flingContainer.setFlingListener(object : onFlingListener {
            override fun removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!")
                rowItems!!.removeAt(0)
                arrayAdapter!!.notifyDataSetChanged()
            }

            fun DatabaseReference.childSafe(child: String): DatabaseReference? {
                return this.child(child)
            }

            override fun onLeftCardExit(dataObject: Any) {
                val obj: cards = dataObject as cards
                val userId: String? = obj.userId
                usersDb!!.child(userId.toString()).child("connections").child("nope")
                    .child(currentUId!!)
                    .setValue(true)
                Toast.makeText(this@swap_activity, "Left", Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(dataObject: Any) {
                val obj: cards = dataObject as cards
                val userId: String? = obj.getUserId()
                usersDb!!.child(userId.toString()).child("connections").child("yeps")
                    .child(currentUId!!)
                    .setValue(true)
                isConnectionMatch(userId.toString())
                Toast.makeText(this@swap_activity, "Right", Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}
            override fun onScroll(scrollProgressPercent: Float) {}
        });
    }

        private var userSex: String? = null
        private var oppositeUserSex: String? = null

        private fun isConnectionMatch(userId: String) {
            val currentUserConnectionsDb = usersDb!!.child(currentUId!!).child("connections").child("yeps").child(userId)
            currentUserConnectionsDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(this@swap_activity, "new Connection", Toast.LENGTH_LONG).show()

                        val key = FirebaseDatabase.getInstance().reference.child("Chat").push().key

                        usersDb!!.child(dataSnapshot.key!!).child("connections").child("matches").child(currentUId!!).child("ChatId").setValue(key)
                        usersDb!!.child(currentUId!!).child("connections").child("matches").child(dataSnapshot.key!!).child("ChatId").setValue(key)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }

        private fun checkUserSex() {
            val user = FirebaseAuth.getInstance().currentUser
            val userDb = usersDb!!.child(user?.uid ?: "")
            userDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("sex").getValue() != null) {
                            userSex = dataSnapshot.child("sex").getValue().toString()
                            when (userSex) {
                                "Male" -> oppositeUserSex = "Female"
                                "Female" -> oppositeUserSex = "Male"
                            }
                            getOppositeSexUsers()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }

        private fun getOppositeSexUsers() {
            usersDb!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    if (dataSnapshot.child("sex").getValue() != null) {
                        if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUId!!) && !dataSnapshot.child("connections").child("yeps").hasChild(
                                currentUId!!
                            ) && dataSnapshot.child("sex").getValue().toString() == oppositeUserSex) {
                            var profileImageUrl = "default"
                            if (!dataSnapshot.child("profileImageUrl").getValue()!!.equals("default")) {
                                profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString()
                            }
                            val item = cards(dataSnapshot.key!!, dataSnapshot.child("name").getValue().toString(), profileImageUrl)
                            (rowItems as ArrayList<cards>).add(item)
                            arrayAdapter!!.notifyDataSetChanged()
                        }
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }