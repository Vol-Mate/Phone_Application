package com.example.phoneapplication

/*import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
//import com.example.phoneapplication.question_activity.Companion.RC_SIGN_IN
import com.example.phoneapplication.registration.Companion.RC_SIGN_IN
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
import com.google.firebase.ktx.Firebase
import androidx.annotation.Keep
import com.google.android.gms.auth.api.signin.GoogleSignInClient*/

//package com.example.phoneapplication
//import java.util.Vector
import android.util.Log
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// told to do this from online
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseError

class question_activity : AppCompatActivity() {
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        //FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        // where we call functions ie setButton()
        setButton()
        putAnswerInDatabase()
    }


    private fun putAnswerInDatabase(){

        // get a reference of the database
        lateinit var userRef: DatabaseReference
        lateinit var myRef: DatabaseReference
        val database = Firebase.database("https://phone-application-14522-default-rtdb.firebaseio.com/")
        //val refDatabase = database.getReference("dateThisWeek")

        // get this user specifically
        var mAuth: FirebaseAuth//.getInstance()
        mAuth = FirebaseAuth.getInstance()


        button.setOnClickListener{
            val editText = findViewById<EditText>(R.id.questionInput)
            val userInput = editText.text.toString()
            println(userInput)

            val user = mAuth.currentUser
            user?.let {
                val myId = it.uid

                userRef= database.getReference("users").child(myId).child("answer")//.child("dateThisWeek")
            }
            userRef.setValue(userInput)
            setButton()
        }
    }

    private fun setButton() {
        button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, main_menu::class.java)
            startActivity(intent)
        }
    }

}



/*@Keep

 class User(
    val userId: String = "",
    val name: String = "",
    val answer: String= "",
    val dateThisWeek: String= ""
) {
    // add other methods as needed
    constructor() : this("")

}

class question_activity : AppCompatActivity(){
    val database = Firebase.database("https://phone-application-14522.firebaseio.com/")
    val usersRef = database.getReference("users")
    companion object {
        const val RC_SIGN_IN = 9001
    }

    private lateinit var button: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var answer: EditText
    private lateinit var googleSignInClient: GoogleSignInClient


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        // Get references to EditText views
        answer = findViewById(R.id.questionInput)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
         googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        findViewById<Button>(R.id.button).setOnClickListener {
            signInGoogle()
        }
        // setButton()
    }

//    private fun setButton(){
//        button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            val intent = Intent(this, swap_activity::class.java)
//            startActivity(intent)
//        }
//    }
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
                // Get values from EditText view
                val answer = answer.text.toString()

                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: ""
                        val name = account.displayName ?: ""


                        // Create a User object with the collected data
                        val user = User(
                            userId = userId,
                            name = name,
                            answer = answer
                        )

                        // Update the user's data in the Firebase Realtime Database
                        usersRef.child(userId).setValue(user).addOnCompleteListener { task ->
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

        if (requestCode == registration.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResults(task)
        }
        val intent = Intent(this, swap_activity::class.java)
        startActivity(intent)
    }

}*/
//*package com.example.phoneapplication
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import com.example.phoneapplication.question_activity.Companion.RC_SIGN_IN
//import com.example.phoneapplication.registration.Companion.RC_SIGN_IN
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.tasks.Task
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
//
//data class User1(
//    val answer: String = ""
//) {
//    // add other methods as needed
//}
//
//class question_activity : AppCompatActivity(){
//    val database = Firebase.database.reference
//    val usersRef = database.child("users")
//    companion object {
//        private const val RC_SIGN_IN = 9001
//    }
//
//        private lateinit var button: Button
//        private lateinit var auth: FirebaseAuth
//        private lateinit var answer: EditText
//
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            setContentView(R.layout.activity_question)
//
//            // Get references to EditText views
//            answer = findViewById(R.id.questionInput)
//
//            auth = FirebaseAuth.getInstance()
//
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//            setButton()
//        }
//
//    private fun setButton(){
//        button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            val intent = Intent(this, swap_activity::class.java)
//            startActivity(intent)
//        }
//    }
//
//
//    private val launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//                handleResults(task)
//            }
//        }
//
//    private fun handleResults(task: Task<GoogleSignInAccount>) {
//        if (task.isSuccessful) {
//            val account: GoogleSignInAccount? = task.result
//            if (account != null) {
//                // Get values from EditText view
//                val answer = answer.text.toString()
//
//                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//                auth.signInWithCredential(credential).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val userId = auth.currentUser?.uid ?: ""
//
//                        // Create a User object with the collected data
//                        val user = User(
//                            answer = answer
//                        )
//
//                        // Update the user's data in the Firebase Realtime Database
//                        usersRef.child(userId).setValue(user).addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(this, "User updated in database", Toast.LENGTH_SHORT).show()
//                                setupUserListener()
//                            } else {
//                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//            }
//
//        } else {
//            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun setupUserListener() {
//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val userRef = usersRef.child(userId)
//            userRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val user = snapshot.getValue(com.google.firebase.firestore.auth.User::class.java)
//                    if (user != null) {
//                        // Do something with the user data
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error
//                }
//            })
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == MainActivity.RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleResults(task)
//        }
//        val intent = Intent(this, main_menu::class.java)
//        startActivity(intent)
//    }
//
//}*/
//package com.example.phoneapplication
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//
//class question_activity : AppCompatActivity(){
//    private lateinit var button: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_question)
//        setButton()
//    }
//
//    private fun setButton(){
//        button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            val intent = Intent(this, swap_activity::class.java)
//            startActivity(intent)
//        }
//    }
//}