package ca.bischke.firebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "FirebaseMessenger"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        button_login.setOnClickListener {
            buttonLoginClick()
        }

        text_not_registered.setOnClickListener {
            buttonRegisterClick()
        }
    }

    private fun buttonLoginClick() {
        val email = edittext_email.text.toString()
        val password = edittext_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Email and Password cannot be blank.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        loginUserAccount(email, password)
    }

    private fun loginUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Successfully logged in.")
                    Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT)
                        .show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Failed to log in.",
                        Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener(this) { e ->
                Log.w(TAG, "signInWithEmail:failure", e)
                Toast.makeText(baseContext, "Failed to log in.",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun buttonRegisterClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
