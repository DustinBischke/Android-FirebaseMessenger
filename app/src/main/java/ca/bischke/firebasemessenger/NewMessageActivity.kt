package ca.bischke.firebasemessenger

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    //private lateinit var adapter: UserAdapter
    private lateinit var adapter: UserFirestoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore
        storage = Firebase.storage

        val query = firestore.collection("users")
            .orderBy("username")
        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        //adapter = UserAdapter(users)
        //fetchUsers()
        adapter = UserFirestoreAdapter(options)
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

    /*private fun fetchUsers() {
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
                val username = queryDocumentSnapshot.get("username").toString()

                storage.reference.child("images/profile/$userId").downloadUrl
                    .addOnSuccessListener {
                        val imageUrl = it.toString()
                        val user = User(username, imageUrl)
                        users.add(user)
                        adapter.notifyDataSetChanged()
                    }.addOnFailureListener{
                        val user = User(username, null)
                        users.add(user)
                        adapter.notifyDataSetChanged()
                    }
            }
        }
    }*/
}
