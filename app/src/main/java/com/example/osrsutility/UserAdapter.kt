package com.example.osrsutility

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.osrsutility.databinding.ListItemBinding

class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem.name == newItem.name

}

class UserAdapter( val listener: (User) -> Unit, val deleteListener: (User) -> Unit) : ListAdapter<User, UserViewHolder>(UserItemDiffCallback()) {

    private var _binding: ListItemBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        Log.d("User", "CreateViewHolder")

        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)

        Log.d("User", "BindViewHolder")

        holder.itemView.setOnClickListener { listener(user) }
        holder.view.findViewById<ImageButton>(R.id.deleteUserButton).setOnClickListener { deleteListener(user) }
        holder.view.findViewById<TextView>(R.id.tvHeading).text = user.name

    }


}

class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view)
