package com.example.osrsutility

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.osrsutility.databinding.ListItemBinding

class UserDetailsAdapter(val context: Context, private val stats: List<Stat>) : RecyclerView.Adapter<UserDataViewHolder>() {



    private var _binding: ListItemBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataViewHolder {
        Log.d("User", "CreateViewHolder")

        return UserDataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_stat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) {
        val stat = stats[position]

        println(stat.toString())

        Log.d("User", "BindViewHolder")

        holder.view.findViewById<TextView>(R.id.tvHeading).text = stat.name
        holder.view.findViewById<TextView>(R.id.rankTV).text = "#" + stat.rank.toString()
        holder.view.findViewById<TextView>(R.id.xpTV).text = stat.experience.toString()
        holder.view.findViewById<TextView>(R.id.levelTV).text = stat.level.toString()
        if(stat.name.lowercase() != "overall") {
            val drawableId = context.resources.getIdentifier(stat.name.lowercase(), "drawable", context.packageName)
            holder.view.findViewById<ImageView>(R.id.title_image).setImageResource(drawableId)
        }
    }

    override fun getItemCount() = stats.size


}

class UserDataViewHolder(val view: View) : RecyclerView.ViewHolder(view)
