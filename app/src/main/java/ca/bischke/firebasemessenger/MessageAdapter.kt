package ca.bischke.firebasemessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat

class MessageAdapter(options: FirestoreRecyclerOptions<Message>) :
    FirestoreRecyclerAdapter<Message, MessageAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (getItem(viewType).fromId == Firebase.auth.uid) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_message_sent, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_message_received, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
        holder.apply {
            textMessage.text = model.message

            val date = model.timestamp.toDate()
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy")
            val timeFormat = SimpleDateFormat("h:mm a")
            textReceived.text = timeFormat.format(date).toString()

            if (position == 0) {
                textDate.text = dateFormat.format(date).toString()
            }

            if (position > 0) {
                val previousDate = getItem(position - 1).timestamp.toDate()

                if (dateFormat.format(date) == dateFormat.format(previousDate)) {
                    textDate.visibility = View.GONE
                } else {
                    textDate.text = dateFormat.format(date).toString()
                }
            }

            if (position < itemCount - 1) {
                val nextDate = getItem(position + 1).timestamp.toDate()

                if (timeFormat.format(date) == timeFormat.format(nextDate)) {
                    textReceived.visibility = View.GONE
                    imageProfile.visibility = View.INVISIBLE
                } else {
                    textReceived.text = timeFormat.format(date).toString()
                }
            }
        }
    }

    // TODO Set view type based on sender
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val textMessage: TextView = itemView.findViewById(R.id.text_message)
        val textReceived: TextView = itemView.findViewById(R.id.text_received)
        val textDate: TextView = itemView.findViewById(R.id.text_date)
        val imageProfile: CircleImageView = itemView.findViewById(R.id.imageview_profile)
    }
}