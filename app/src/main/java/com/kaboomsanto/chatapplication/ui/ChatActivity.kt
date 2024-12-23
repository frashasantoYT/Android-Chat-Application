package com.kaboomsanto.chatapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaboomsanto.chatapplication.adapters.MessageAdapter
import com.kaboomsanto.chatapplication.databinding.ActivityChat2Binding
import com.kaboomsanto.chatapplication.models.Message

class ChatActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var chatBinding : ActivityChat2Binding
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>
    private lateinit var database : FirebaseDatabase
    private var senderRoom : String? = null
    private var receiverRoom : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        chatBinding = ActivityChat2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(chatBinding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val intent = getIntent()
        val fullName = intent.getStringExtra("name")
        val receiverUserId = intent.getStringExtra("userId")
        supportActionBar?.title = fullName
        messageList = mutableListOf<Message>()
        messageRecyclerView = chatBinding.recyclerViewMessages
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(this, messageList)
        messageRecyclerView.adapter = messageAdapter

         val senderUserId = auth.currentUser?.uid
        if (senderUserId != null) {
            Log.d("userId", senderUserId)
        }

        senderRoom = receiverUserId + senderUserId
        receiverRoom = senderUserId + receiverUserId

        //logic for adding data to recyclerView

        database.reference.child("chats").child(senderRoom!!).child("messages").addValueEventListener(
            object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapShot in snapshot.children){
                       val message = postSnapShot.getValue(Message::class.java)
                        if(message != null){
                            messageList.add(message)
                        }

                    }

                    Log.d("ChatActivity", "Message List Size: ${messageList.size}")
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(applicationContext, error.toException().localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
        )


        chatBinding.buttonSend.setOnClickListener {
            var message = chatBinding.editTextMessage.text.toString()
            val messageObject = Message(senderUserId!!, message)

            database.reference.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).addOnSuccessListener {
                database.reference.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)

            }
            chatBinding.editTextMessage.setText("")



        }








    }
}