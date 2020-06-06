package ca.bischke.firebasemessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val username: String = "", val uid: String = "") : Parcelable