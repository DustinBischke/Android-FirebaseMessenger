package ca.bischke.firebasemessenger

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.extensions.LayoutContainer

class UserAdapter(options: FirestoreRecyclerOptions<User>, val clickListener: (User) -> Unit) :
    FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: User) {
        holder.apply {
            val username = model.username
            textUsername.text = username

            val uid = model.uid
            val reference = FirebaseStorage.getInstance().reference.child("images/profile/$uid")

            Glide.with(imageProfile)
                .load(reference)
                .centerCrop()
                .into(imageProfile)

            holder.containerView.setOnClickListener { clickListener(model) }
        }
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val textUsername: TextView = itemView.findViewById(R.id.text_username)
        val imageProfile: CircleImageView = itemView.findViewById(R.id.imageview_profile)
    }
}