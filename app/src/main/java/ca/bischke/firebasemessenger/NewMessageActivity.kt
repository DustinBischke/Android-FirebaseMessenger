package ca.bischke.firebasemessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_new_message.*

// TODO Setup Channels now that this doesn't work anymore :D
class NewMessageActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore

        val query = firestore.collection("users")
            .orderBy("username")
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        adapter = UserAdapter(options) { user: User -> userItemClicked(user) }
        recyclerview_users.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun userItemClicked(user: User) {
        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra("USER", user)
        startActivity(intent)
        finish()
    }
}
