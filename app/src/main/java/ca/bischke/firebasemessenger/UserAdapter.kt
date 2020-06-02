package ca.bischke.firebasemessenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val list: List<User>) : RecyclerView.Adapter<UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = list[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = list.size

}