package ca.bischke.firebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChannelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        verifyUserIsLoggedIn()

        firestore = Firebase.firestore

        val query = firestore.collection("channels")
            .orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<Channel>()
            .setQuery(query, Channel::class.java)
            .build()

        adapter = ChannelAdapter(options) { channel: Channel -> channelItemClicked(channel) }
        recyclerview_channels.adapter = adapter

        fab_message.setOnClickListener {
            startNewMessageActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
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

    private fun channelItemClicked(channel: Channel) {
        val intent = Intent(this, MessageActivity::class.java)
        intent.putExtra("CHANNEL", channel)
        startActivity(intent)
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
