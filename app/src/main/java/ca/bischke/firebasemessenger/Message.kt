package ca.bischke.firebasemessenger

import com.google.firebase.Timestamp

data class Message(
    val message: String = "",
    val uid: String = "",
    val cid: String = "",
    val timestamp: Timestamp = Timestamp.now()
)