package com.example.phoneapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

data class User2(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val gender: String = "",
    val genderPref: String = "",
    val answer: String= "",
    //  val answer: String = ""
) {
    // add other methods as needed
}
class registration : AppCompatActivity() {
    val database = Firebase.database.reference
    val usersRef = database.child("users")
    companion object {
        const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var ageEditText: EditText
    private lateinit var genderEditText: Spinner
    private lateinit var genderPrefEditText: Spinner


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Get references to EditText views
        ageEditText = findViewById(R.id.ageEditText)
        genderEditText = findViewById(R.id.genderSpinner)
        genderPrefEditText = findViewById(R.id.preferredGenderSpinner)


        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        findViewById<Button>(R.id.gSignInBtn).setOnClickListener {
            signInGoogle()
        }
      //  setButton()
    }
  /* private fun setButton(){
        var button = findViewById<Button>(R.id.gSignInBtn)
        button.setOnClickListener {
            val intent = Intent(this, question_activity::class.java)
            startActivity(intent)
        }
    }*/


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                // Get values from EditText views
                val gender = genderEditText.selectedItem.toString()
                val age = ageEditText.text.toString().toIntOrNull() ?: 0
                val genderPref = genderPrefEditText.selectedItem.toString()

                // val answer = answerEditText.text.toString()

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val name = account.displayName ?: ""
                        val email = account.email ?: ""

                        val answer = ""
                        val user2 = User2 (
                             userId=userId,
                           name=name,
                            email=email,
                          age=age,
                            gender=gender,
                           genderPref=genderPref,
                            answer=answer
                        )


                        // Update the user's data in the Firebase Realtime Database

                        usersRef.child(userId).setValue(user2).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "User updated in database", Toast.LENGTH_SHORT).show()
                                setupUserListener()
                            } else {
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUserListener() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = usersRef.child(userId)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        // Do something with the user data
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResults(task)
        }
        val intent = Intent(this, question_activity::class.java)
        startActivity(intent)
    }

}

