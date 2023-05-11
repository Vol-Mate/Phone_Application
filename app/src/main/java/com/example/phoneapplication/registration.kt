// Creates a new user node in the database with infromation regarding the user
// Uses google auth
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
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase


@Keep
class Users(
    // data class which has all the user information
    val userID: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val answer: String="",
    val gender: String = "",
    val genderPref: String = "",
    val dateThisWeek: String = ""
) {
    // add other methods as needed
    constructor() : this("", "", "", 0,"","", "")
}
class registration : AppCompatActivity(){
    // check is we have access to the users node
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
        FirebaseApp.initializeApp(this)
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
    }

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

                // Gets the Google sign-in credential
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                // Sign in to Firebase with the credential
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val userID = auth.currentUser?.uid ?: ""
                        val name = account.displayName ?: ""
                        val email = account.email ?: ""

                        val answer = ""
                        val user2 = Users(
                            userID = userID,
                            name = name,
                            email = email,
                            age = age,
                            gender = gender,
                            genderPref = genderPref,
                        )

                        val refToUser = database.child("users").child(userID)
                        refToUser.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // Only write to the database if there isn't already a node there for it
                                // Ensures that data that is already there is not overridden
                                if (!snapshot.exists()) {
                                    println(userID)
                                    refToUser.child("userID").setValue(userID)
                                    refToUser.child("name").setValue(name)
                                    refToUser.child("email").setValue(email)
                                    refToUser.child("age").setValue(age)
                                    refToUser.child("gender")
                                        .setValue(gender)
                                    refToUser.child("genderPref")
                                        .setValue(genderPref)
                                    refToUser.child("dateThisWeek")
                                        .setValue("")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                    }

                }
            }
        }
    }

    // sets up a listener for the current user's data in database
    private fun setupUserListener() {
        val userID = auth.currentUser?.uid
        if (userID != null) {
            val userRef = usersRef.child(userID)
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    // after google sign in, this function is called to process the result and start next activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Companion.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResults(task)
        }
        val intent = Intent(this, main_menu::class.java)
        startActivity(intent)
    }
}
