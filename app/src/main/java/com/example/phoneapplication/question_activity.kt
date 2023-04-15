package com.example.phoneapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

    class question_activity : AppCompatActivity(){
        private lateinit var button: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_question)
            setButton()
        }

    private fun setButton(){
        button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, swap_activity::class.java)
            startActivity(intent)
        }
    }
}