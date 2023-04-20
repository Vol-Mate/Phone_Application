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
    val userId: String = "",
    val name: String = "",
    val email: String = "",
) {
    constructor() : this("", "", "")

}

class login : AppCompatActivity() {
    val database = Firebase.database.reference
    val usersRef = database.child("users")

    companion object {
        const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
        //setButton()
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
    /*private fun setButton(){
        var button = findViewById<Button>(R.id.gSignInBtn)
        button.setOnClickListener {
            val intent = Intent(this, main_menu::class.java)
            startActivity(intent)
        }
    }*/

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

                        val user1 = User1(
                            userId = userId,
                            name = name,
                            email = email
                        )

                        // Update the user's data in the Firebase Realtime Database
                        usersRef.child(userId).setValue(user1).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "User updated in database", Toast.LENGTH_SHORT)
                                    .show()
                                setupUserListener()
                            } else {
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to update user in database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

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
    
