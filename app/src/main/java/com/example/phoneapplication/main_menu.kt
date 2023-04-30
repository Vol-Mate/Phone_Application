package com.example.phoneapplication
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
//import android.util.Log

class main_menu : AppCompatActivity(){
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        setButton()
        setButton2()
        //Log.i(TAG, "Hello World")
}
    private fun setButton(){
        button = findViewById<Button>(R.id.MatchButton)
        button.setOnClickListener {
            val intent = Intent(this, swap_activity::class.java)
            startActivity(intent)
        }
    }
    private fun setButton2(){
        button = findViewById<Button>(R.id.QuestionButton)
        button.setOnClickListener {
            val intent = Intent(this, question_activity::class.java)
            startActivity(intent)
        }
    }


}