package ca.bischke.firebasemessenger

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channel(
    val cid: String = "",
    val name: String = "",
    val message: String = "",
    val timestamp: Timestamp = Timestamp.now()
) : Parcelable