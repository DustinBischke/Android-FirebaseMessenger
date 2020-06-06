package ca.bischke.firebasemessenger

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val TAG = "FirebaseMessenger"
    private val REQUEST_IMAGE_CODE = 69
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        button_select_photo.setOnClickListener {
            buttonSelectPhotoClick()
        }

        button_register.setOnClickListener {
            buttonRegisterClick()
        }

        text_already_registered.setOnClickListener {
            buttonLoginClick()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.data != null) {
                imageUri = data.data!!

                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                } else {
                    val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(source)
                }

                button_select_photo.setImageBitmap(bitmap)
            }
        }
    }

    private fun buttonSelectPhotoClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CODE)
    }

    private fun buttonRegisterClick() {
        val email = edittext_email.text.toString()
        val password = edittext_password.text.toString()
        val username = edittext_username.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(baseContext, "All fields cannot be blank.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        createUserAccount(email, password, username)
    }

    private fun buttonLoginClick() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun createUserAccount(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Successfully created account.", Toast.LENGTH_SHORT)
                        .show()
                    val user = auth.currentUser

                    if (user != null) {
                        val uid = user.uid
                        createUserFirestore(uid, username)

                        if (::imageUri.isInitialized) {
                            uploadProfileImage(username, imageUri)
                        }
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
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

    private fun createUserFirestore(uid: String, username: String) {
        val userHashMap = hashMapOf(
            "username" to username,
            "uid" to uid
        )

        firestore.collection("users").document(uid)
            .set(userHashMap)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully written.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document.", e)
            }
    }

    private fun uploadProfileImage(username: String, uri: Uri) {
        val reference = storage.getReference("images/profile/$username")

        reference.putFile(uri)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully uploaded image.")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Failed to upload image.", e)
            }
    }
}
