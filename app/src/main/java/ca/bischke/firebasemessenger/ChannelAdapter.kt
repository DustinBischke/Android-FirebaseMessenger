package ca.bischke.firebasemessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.extensions.LayoutContainer
import java.text.SimpleDateFormat

class ChannelAdapter(options: FirestoreRecyclerOptions<Channel>, val clickListener: (Channel) -> Unit) :
    FirestoreRecyclerAdapter<Channel, ChannelAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_channel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Channel) {
        holder.apply {
            textChannel.text = model.name
            textMessage.text = model.message

            val date = model.timestamp.toDate()
            val timeFormat = SimpleDateFormat("h:mm a")

            textTime.text = timeFormat.format(date).toString()

            containerView.setOnClickListener { clickListener(model) }
        }
    }

    override fun startListening() {
        super.startListening()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val textChannel: TextView = itemView.findViewById(R.id.text_channel)
        val textMessage: TextView = itemView.findViewById(R.id.text_message)
        val textTime: TextView = itemView.findViewById(R.id.text_time)
    }
}