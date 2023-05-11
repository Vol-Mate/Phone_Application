// Allows the user to login without overriding their data
// Uses google auth
package com.example.phoneapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import androidx.annotation.Keep

@Keep
 class User1(
    // data class which has the user information
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val dateThisWeek: String = ""
) {
    constructor() : this("", "", "")

}

class login : AppCompatActivity() {
    // get reference to the firebase database
    val database = Firebase.database.reference
    val usersRef = database.child("users")

    companion object {
        const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    
    @SuppressLint("MissingInflatedId")
    // initializes  FirebaseAuth and GoogleSignInClient, and sets up the Google sign-in button click listener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // get instance of firebaseAuth
        auth = FirebaseAuth.getInstance()

        // google sign in options with desired sign in options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // google sign in client with the specified options
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //sign out the user
        googleSignInClient.signOut()

        // sets the google sign in button
        findViewById<Button>(R.id.gSignInBtn).setOnClickListener {
            signInGoogle()
        }
    }

    // launches the google sign in
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // creates a launcher for handling the result of sign in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //check if sign in was successful
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

    // retrieves the GoogleSignInAccount from the task and signs in to Firebase by using account's data
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val name = account.displayName ?: ""
                        val email = account.email ?: ""

                        // Creates a User1 object with user information
                        val user1 = User1(
                            userId = userId,
                            name = name,
                            email = email
                        )

                        // Gets a reference to the user's node in the "users" collection in the database
                        val refToUser = database.child("users").child(userId)
                        refToUser.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // only write to the database if there isn't already a node there for it
                                if (!snapshot.exists()) {
                                    println(userId)
                                    // Set the values for "userID", "name", and "email" in the user's node
                                    refToUser.child("userID").setValue(userId)
                                    refToUser.child("name").setValue(name)
                                    refToUser.child("email").setValue(email)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handles database errors
                            }
                        })
                    } else {
                        // shows a toast message if failed to update user in the database
                        Toast.makeText(
                            this,
                            "Failed to update user in database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                // shows a toast message with the exception if the account is null
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // sets up a listener for the current user's data in database
    private fun setupUserListener() {
        val userId = auth.currentUser?.uid ?: ""
        usersRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user1 = snapshot.getValue(User1::class.java)
        if (userId != null) {
            Toast.makeText(this@login, "Logged in successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@login, main_menu::class.java)
            startActivity(intent)
            finish()
        } else {
            // Handle unsuccessful login
            Toast.makeText(this@login, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
        }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@login, "Failed to retrieve user data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
            })
        }

    // after google sign in, this function is called to process the result and start next activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == registration.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResults(task)
        }
        val intent = Intent(this, main_menu::class.java)
        startActivity(intent)
    }
}
    
