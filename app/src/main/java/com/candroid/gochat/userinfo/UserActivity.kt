package com.candroid.gochat.userinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.candroid.gochat.R
import com.candroid.gochat.adapter.RecyclerAdapter
import com.candroid.gochat.pushnotification.MessagingService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
   lateinit var recyclerAdapter: RecyclerAdapter
   lateinit var userRecyclerView:RecyclerView
lateinit var firebaseDatabase: FirebaseDatabase
lateinit var firebaseReference: DatabaseReference
lateinit var mAuth:FirebaseAuth
lateinit var firebaseMessaging:FirebaseMessaging
   val userList= arrayListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        MessagingService.shardPref=getSharedPreferences("pref", MODE_PRIVATE)
        firebaseMessaging= FirebaseMessaging.getInstance()
        firebaseMessaging.token.addOnSuccessListener {
            MessagingService.token = it


        }
        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgProfile.setOnClickListener{
            val intent=Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

        userRecyclerView=findViewById(R.id.userRecyclerView)
mAuth= FirebaseAuth.getInstance()
  val users=mAuth.currentUser
      firebaseMessaging.subscribeToTopic("/topics/${users!!.uid}")
        firebaseDatabase= FirebaseDatabase.getInstance()
        firebaseReference=firebaseDatabase.getReference("users")
        firebaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

    val currentUser=snapshot.getValue(User::class.java)
                if(currentUser!!.profileImage==""){

                    imgProfile.setImageResource(R.drawable.profile_image)

                }
                else{


                    Glide.with(this@UserActivity).load(currentUser.profileImage).into(imgProfile)

                }


                for(dataSnapShot:DataSnapshot in snapshot.children){
                    val user=dataSnapShot.getValue(
                    User::class.java)
                    if(!user!!.userId.equals( users?.uid)){

                       userList.add(user)

                    }

                }
                recyclerAdapter= RecyclerAdapter(this@UserActivity,userList)
                val layoutManager=LinearLayoutManager(this@UserActivity)
                userRecyclerView.adapter=recyclerAdapter
                userRecyclerView.layoutManager=layoutManager

            }

            override fun onCancelled(error: DatabaseError) {
Toast.makeText(this@UserActivity,error.message,Toast.LENGTH_SHORT).show()
            }


        })




















    }
}