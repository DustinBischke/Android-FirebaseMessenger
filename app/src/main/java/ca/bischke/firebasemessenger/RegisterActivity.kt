package ca.bischke.firebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val TAG = "FirebaseMessenger"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        button_register.setOnClickListener {
            buttonRegisterClick()
        }

        text_already_registered.setOnClickListener {
            buttonLoginClick()
        }
    }

    private fun buttonRegisterClick() {
        val email = edittext_email.text.toString()
        val password = edittext_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(baseContext, "Email and Password cannot be blank.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        createUserAccount(email, password)
    }

    private fun createUserAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Successfully created account.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Failed to create account.", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener(this) { e ->
                Log.w(TAG, "createUserWithEmail:failure", e)
                Toast.makeText(baseContext, "Failed to create account.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun buttonLoginClick() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
