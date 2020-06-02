package ca.bischke.firebasemessenger

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_new_message.*


class NewMessageActivity : AppCompatActivity() {
    private val TAG = "FirebaseMessenger"
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private val users = mutableListOf<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore
        storage = Firebase.storage

        adapter = UserAdapter(users)
        recyclerview_users.adapter = adapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val reference = firestore.collection("users")
        reference.get()
            .addOnSuccessListener { collection ->
                if (collection != null) {
                    Log.d(TAG, "Lol")
                    displayUsers(collection)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun displayUsers(collection: QuerySnapshot) {
        users.clear()
        Log.d(TAG, "User count: " + collection.size().toString())

        for (queryDocumentSnapshot in collection) {
            if (queryDocumentSnapshot.get("username") != null) {
                val userId = queryDocumentSnapshot.id
                val username = queryDocumentSnapshot.get("username")
                var imageUrl: String? = null

                storage.reference.child("images/profile/$userId").downloadUrl
                    .addOnSuccessListener {
                        imageUrl = it.toString()
                    }.addOnFailureListener{
                        Log.d(TAG, "Unable to load image url")
                    }

                val user = User(queryDocumentSnapshot.get("username").toString(), imageUrl)
                users.add(user)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
