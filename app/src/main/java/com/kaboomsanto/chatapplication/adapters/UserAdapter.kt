package com.kaboomsanto.chatapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaboomsanto.chatapplication.R
import com.kaboomsanto.chatapplication.models.User

class UserAdapter(private val user: MutableList<User>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val fullName : TextView = itemView.findViewById(R.id.user_name)
        val email: TextView = itemView.findViewById(R.id.user_email)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION){
                     val clickedUser = user[position]
                    itemClickListener.onItemClick(clickedUser.fullName,
                        clickedUser.userId.toString()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = user[position]

        holder.fullName.text = currentUser.fullName
        holder.email.text = currentUser.email
    }


    interface OnItemClickListener{
        fun onItemClick(name: String, userId: String)
    }


}