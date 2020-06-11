package ca.bischke.firebasemessenger

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channel(val cid: String = "", val name: String = "", val message: String = "") : Parcelable