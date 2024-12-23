package com.kaboomsanto.chatapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kaboomsanto.chatapplication.R
import com.kaboomsanto.chatapplication.adapters.UserAdapter
import com.kaboomsanto.chatapplication.databinding.ActivityMainBinding
import com.kaboomsanto.chatapplication.models.User

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {
    private lateinit var auth : FirebaseAuth
    private lateinit var dbRef : FirebaseDatabase
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        supportActionBar?.title = "Tech_Santo ChatApp Users"
        auth = FirebaseAuth.getInstance()
        userList = mutableListOf<User>()
        dbRef  = FirebaseDatabase.getInstance()
        myRecyclerView = mainBinding.userRecyclerView
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList, this)
        myRecyclerView.adapter = userAdapter



        dbRef.reference.child("users").addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for(postSnapShot in snapshot.children){
                    val currentUser = postSnapShot.getValue(User::class.java)

                    if (currentUser != null && currentUser.userId != null && auth.currentUser?.uid != currentUser.userId) {
                        userList.add(currentUser)
                    }


                }
                Log.d("MainActivity", "User List after fetch: $userList")

                userAdapter.notifyDataSetChanged()



            }


            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }

        }

        )







    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        }

    private fun logOut(){
            auth.signOut()
            Toast.makeText(applicationContext, "You have successfully logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)

        
    }

    override fun onItemClick(name: String, userId : String){
        val intent = Intent(this@MainActivity, ChatActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }


    }
