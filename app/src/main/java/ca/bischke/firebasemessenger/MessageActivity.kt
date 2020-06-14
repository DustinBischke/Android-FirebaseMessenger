package ca.bischke.firebasemessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {
    private val TAG = "FirebaseMessenger"
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val channel = intent.getParcelableExtra<Channel>("CHANNEL")
        supportActionBar?.title = channel?.name

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        val query = firestore.collection("channels").document(channel.cid).collection("messages")
            .orderBy("timestamp")
        val options = FirestoreRecyclerOptions.Builder<Message>()
            .setQuery(query, Message::class.java)
            .build()

        adapter = MessageAdapter(options)
        adapter.hasStableIds()
        recyclerview_messages.adapter = adapter

        button_send.setOnClickListener {
            buttonSendClick()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun buttonSendClick() {
        if (edittext_message.text.isBlank()) {
            return
        }

        val channel = intent.getParcelableExtra<Channel>("CHANNEL")
        val message = edittext_message.text.toString()
        val uid = auth.uid
        val cid = channel?.cid
        val timestamp = Timestamp.now()

        val messageHashMap = hashMapOf(
            "message" to message,
            "uid" to uid,
            "timestamp" to timestamp
        )

        firestore.collection("channels").document(channel.cid).collection("messages")
            .add(messageHashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully written.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document.", e)
            }

        val messageMap = mapOf(
            "message" to message,
            "timestamp" to timestamp
        )

        firestore.collection("channels").document(channel.cid)
            .update(messageMap)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully written.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document.", e)
            }

        edittext_message.text.clear()
    }
}