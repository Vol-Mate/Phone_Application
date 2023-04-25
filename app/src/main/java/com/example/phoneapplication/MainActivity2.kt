package com.example.phoneapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        userId = auth.currentUser!!.uid

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")

       // findViewById<TextView>(R.id.textView2).text = "$email\n$displayName"

        findViewById<Button>(R.id.gSignOutBtn).setOnClickListener{
            val age = findViewById<Spinner>(R.id.ageEditText).selectedItem.toString()
            val sex = findViewById<Spinner>(R.id.genderSpinner).selectedItem.toString()
            val genderPref = findViewById<Spinner>(R.id.preferredGenderSpinner).selectedItem.toString()

            if (age == null) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userUpdates = hashMapOf<String, Any>(
                "age" to age,
                "sex" to sex,
                "genderPref" to genderPref
            )

            database.child("users").child(userId).updateChildren(userUpdates).addOnSuccessListener {
                Toast.makeText(this, "User data updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.gSignOutBtn).setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
