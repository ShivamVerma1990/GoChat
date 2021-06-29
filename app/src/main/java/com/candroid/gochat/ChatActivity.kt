package com.candroid.gochat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var chatRecyclerView:RecyclerView

    var chatList= arrayListOf<Chat>()
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

fun readMessage(senderId: String,receiveId: String){
val ref=FirebaseDatabase.getInstance().getReference("Chat")
ref.addValueEventListener(object:ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
        for(dataSnapshot:DataSnapshot in snapshot.children){

            val chat=dataSnapshot.getValue(Chat::class.java)
            if(chat!!.senderId.equals(senderId)&&chat.receiveId.equals(receiveId)||chat!!.senderId.equals(receiveId)&&chat.receiveId.equals(senderId))
            {
chatList.add(chat)
            }
        }

    val chatAdapter=ChatAdapter(this@ChatActivity,chatList)
        val layoutManager=LinearLayoutManager(this@ChatActivity)
        chatRecyclerView.layoutManager=layoutManager
        chatRecyclerView.adapter=chatAdapter




    }

    override fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

})

}
}