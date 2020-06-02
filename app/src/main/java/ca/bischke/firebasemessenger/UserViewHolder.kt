package ca.bischke.firebasemessenger

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class UserViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_user, parent, false)) {
    private var textUsername: TextView = itemView.findViewById(R.id.text_username)
    private var imageProfile: CircleImageView = itemView.findViewById(R.id.imageview_profile)

    fun bind(user: User) {
        textUsername.text = user.username

        if (user.imageUrl != null) {
            Glide.with(imageProfile)
                .load(user.imageUrl)
                .centerCrop()
                .into(imageProfile)
        }
    }
}