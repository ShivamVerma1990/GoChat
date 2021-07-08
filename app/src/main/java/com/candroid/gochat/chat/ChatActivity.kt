package com.candroid.gochat.chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.candroid.gochat.adapter.ChatAdapter
import com.candroid.gochat.R
import com.candroid.gochat.pushnotification.NotificationData
import com.candroid.gochat.pushnotification.PushNotification
import com.candroid.gochat.pushnotification.Retrofit
import com.candroid.gochat.userinfo.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.lang.Exception

class ChatActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var chatRecyclerView:RecyclerView

    var chatList= arrayListOf<Chat>()
    var topic=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView=findViewById(R.id.chatRecyclerView)
        mAuth = FirebaseAuth.getInstance()
        val chatUser=mAuth.currentUser
        val chatUserId=chatUser!!.uid
        firebaseDatabase = FirebaseDatabase.getInstance()
        val intent = getIntent()
        val userId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        databaseReference = firebaseDatabase.getReference("users").child(userId!!)
        imgProfile.setOnClickListener {
            onBackPressed()
        }

        btnSendMessage.setOnClickListener{
            val message=etMessage.text.toString().trim()
            if(message.isEmpty()){
                Toast.makeText(this,"there is no message",Toast.LENGTH_SHORT).show()

            }
            else{

                sendMessage(chatUserId,userId,message)
            etMessage.setText("")
                topic="/topics/$userId"
PushNotification(NotificationData(userName!!,message),topic).also {
    sendsMessage(it)
}

            }


        }
        readMessage(chatUserId,userId)


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)

                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }
//here we send message to specific user
    fun sendMessage(senderId: String, receiveId: String, message: String) {
        val chatReference = FirebaseDatabase.getInstance().getReference()
        val hashMap = HashMap<String, String>()
        hashMap.put("senderId", senderId)
        hashMap.put("receiveId", receiveId)
        hashMap.put("message", message)
        chatReference.child("Chat").push().setValue(hashMap).addOnCompleteListener {
            if(it.isSuccessful){

                etMessage.setText("")
            }
            else{

                Toast.makeText(this,"something wrong",Toast.LENGTH_SHORT).show()

            }
        }


    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiveId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiveId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                var layoutManager=LinearLayoutManager(this@ChatActivity)

                chatRecyclerView.layoutManager=layoutManager

                chatRecyclerView.adapter = chatAdapter


            }
        })
    }
fun sendsMessage(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
    try {
        val response = Retrofit.api.postNotification(notification)
        if(response.isSuccessful) {
            Log.d("TAG", "Response: ${Gson().toJson(response)}")
        } else {
            Log.e("TAG", response.errorBody()!!.string())
        }
    } catch(e: Exception) {
        Log.e("TAG", e.toString())
    }
}
}