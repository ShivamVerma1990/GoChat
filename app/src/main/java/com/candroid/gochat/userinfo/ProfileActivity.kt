package com.candroid.gochat.userinfo

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.candroid.gochat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class ProfileActivity : AppCompatActivity() {
lateinit var mAuth:FirebaseAuth
var filePath: Uri?=null

lateinit var firebaseStorage: FirebaseStorage
lateinit var storageRef:StorageReference
    final  var PICK_IMAGE_REQUEST:Int=2021
lateinit var firebaseDatabase: FirebaseDatabase
lateinit var firebaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        imgBack.setOnClickListener {
            val intent= Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }//here we choose our images
btnLogout.setOnClickListener {
    mAuth.signOut()
    finish()
}

        userImage.setOnClickListener{

            chooseImage()

        }
        //here we save image on firebase storage
        btnSave.setOnClickListener {
            uploadImage()
        progressBar.visibility=View.VISIBLE
        }


      firebaseStorage= FirebaseStorage.getInstance()
        storageRef=firebaseStorage.reference

        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        val userId=user!!.uid
 firebaseDatabase=FirebaseDatabase.getInstance()
        firebaseReference=firebaseDatabase.getReference("users").child(userId)

        firebaseReference.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
            val userData=snapshot.getValue(User::class.java)
                etUserName.setText(userData!!.userName)
     //here we check our images are come from firebase or not
            if(userData.profileImage==""){
                userImage.setImageResource(R.drawable.profile_image)

            }else{
                Glide.with(this@ProfileActivity).load(userData.profileImage).into(userImage)

            }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }
    //here we create implicit intent
    private fun chooseImage() {
    var intent = Intent()
intent.type="image/*"
    intent.action=Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_REQUEST)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && requestCode != null) {

            filePath = data!!.data

            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                userImage.setImageBitmap(bitmap)
                btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    //here we upload image using firebaseStorage
private fun uploadImage(){
if(filePath!=null){
    val ref:StorageReference=storageRef.child("image/"+UUID.randomUUID().toString())
    ref.putFile(filePath!!).addOnSuccessListener {

val hashMap=HashMap<String,String>()
hashMap.put("userName",etUserName.text.toString())
hashMap.put("profileImage",filePath.toString())
        firebaseReference.updateChildren(hashMap as Map<String,Any>)
        Toast.makeText(this,"Successfully",Toast.LENGTH_SHORT).show()
        progressBar.visibility=View.GONE
        btnSave.visibility=View.GONE
    }.addOnFailureListener{
        Toast.makeText(this,"Successfully",Toast.LENGTH_SHORT).show()
        progressBar.visibility=View.GONE
    }
}

}

}

