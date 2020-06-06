package ca.bischke.firebasemessenger

import com.google.firebase.Timestamp

data class Message(
    val message: String = "",
    val fromId: String = "",
    val toId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)