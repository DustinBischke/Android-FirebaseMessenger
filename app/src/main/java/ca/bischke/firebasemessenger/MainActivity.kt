package ca.bischke.firebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        verifyUserIsLoggedIn()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                startNewMessageActivity()
            }
            R.id.menu_sign_out -> {
                signOut()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun verifyUserIsLoggedIn() {
        val uid = auth.uid

        if (uid == null) {
            startLoginActivity()
        }
    }

    private fun signOut() {
        auth.signOut()
        startLoginActivity()
    }

    private fun startNewMessageActivity() {
        val intent = Intent(this, NewMessageActivity::class.java)
        startActivity(intent)
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
