package ca.bischke.firebasemessenger

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat
import java.util.*

class MessageAdapter(options: FirestoreRecyclerOptions<Message>) :
    FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (getItem(viewType).uid == Firebase.auth.uid) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_message_sent, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_message_received, parent, false)
            ViewHolderReceived(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
        holder.bind(model, position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    open inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val textMessage: TextView = itemView.findViewById(R.id.text_message)
        private val textTime: TextView = itemView.findViewById(R.id.text_received)
        private val textDate: TextView = itemView.findViewById(R.id.text_date)

        @SuppressLint("SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("MMMM dd, yyyy")
        @SuppressLint("SimpleDateFormat")
        protected val timeFormat = SimpleDateFormat("h:mm a")

        protected lateinit var date: Date

        open fun bind(model: Message, position: Int) {
            textMessage.text = model.message

            date = model.timestamp.toDate()

            if (position != 0) {
                val previousDate = getItem(position - 1).timestamp.toDate()

                if (dateFormat.format(date) == dateFormat.format(previousDate)) {
                    textDate.visibility = View.GONE
                }
            }

            if (textDate.visibility == View.VISIBLE) {
                textDate.text = dateFormat.format(date).toString()
            }

            if (position < itemCount - 1) {
                if (model.uid == getItem(position + 1).uid) {
                    val nextDate = getItem(position + 1).timestamp.toDate()

                    if (timeFormat.format(date) == timeFormat.format(nextDate)) {
                        textTime.visibility = View.GONE
                    }
                }
            }

            if (textTime.visibility == View.VISIBLE) {
                textTime.text = timeFormat.format(date).toString()
            }
        }
    }

    inner class ViewHolderReceived(containerView: View) : ViewHolder(containerView) {
        private val imageProfile: CircleImageView = itemView.findViewById(R.id.imageview_profile)

        override fun bind(model: Message, position: Int) {
            super.bind(model, position)

            if (position < itemCount - 1) {
                if (model.uid == getItem(position + 1).uid) {
                    val nextDate = getItem(position + 1).timestamp.toDate()

                    if (timeFormat.format(date) == timeFormat.format(nextDate)) {
                        imageProfile.visibility = View.INVISIBLE
                    }
                }
            }

            if (imageProfile.visibility == View.VISIBLE) {
                val uid = model.uid
                val reference = FirebaseStorage.getInstance().reference.child("images/profile/$uid")

                reference.downloadUrl.addOnSuccessListener {
                    Glide.with(imageProfile)
                        .load(reference)
                        .centerCrop()
                        .into(imageProfile)
                }
            }
        }
    }
}