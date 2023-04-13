package com.example.phoneapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.Button
import com.example.phoneapplication.MainActivity2
import com.example.phoneapplication.R


class MainActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        auth = FirebaseAuth.getInstance()

        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")

        findViewById<TextView>(R.id.textView2).text = email + "\n" + displayName

        findViewById<Button>(R.id.gSignOutBtn).setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
