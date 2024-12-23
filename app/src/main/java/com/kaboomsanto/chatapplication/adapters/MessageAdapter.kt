package com.kaboomsanto.chatapplication.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.kaboomsanto.chatapplication.R
import com.kaboomsanto.chatapplication.models.Message


class MessageAdapter(private val context: Context, private val messageList : MutableList<Message>): RecyclerView.Adapter<ViewHolder>() {

    private var auth = FirebaseAuth.getInstance()
    private var ITEM_RECEIVE = 1
    private var ITEM_SENT = 2


    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sentMessage: TextView = itemView.findViewById(R.id.sent_message_text)

    }

    class ReceiverViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val receivedMessage : TextView = itemView.findViewById(R.id.receiver_message_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == 1){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.receiver_message, parent, false)
            return ReceiverViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
            return SenderViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder is SenderViewHolder) {
            holder.sentMessage.text = currentMessage.message
            Log.d("MessageAdapter", "Binding sent message: ${currentMessage.message}")
        } else if (holder is ReceiverViewHolder) {
            holder.receivedMessage.text = currentMessage.message
            Log.d("MessageAdapter", "Binding sent message: ${currentMessage.message}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(auth.currentUser?.uid.equals(currentMessage.senderId)){
        return ITEM_SENT
        }
        else{
        return ITEM_RECEIVE
        }
    }

}