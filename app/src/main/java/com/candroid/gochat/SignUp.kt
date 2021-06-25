package com.candroid.gochat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
lateinit var mAuth:FirebaseAuth
lateinit var firebaseDatabase: FirebaseDatabase
lateinit var firebaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
mAuth= FirebaseAuth.getInstance()
        firebaseDatabase= FirebaseDatabase.getInstance()
firebaseReference=firebaseDatabase.getReference("users")
        btnSignUp.setOnClickListener {
            val email=etEmail.text.toString()
val password=etPassword.text.toString()
val confirmPassword=etConfirmPassword.text.toString()

val name=etName.text.toString()
     if(email.isEmpty()&&name.isEmpty()&&password.isEmpty()&&confirmPassword.isEmpty()){

         Toast.makeText(this,"invalid field",Toast.LENGTH_SHORT).show()
     }

       else if(!password.contentEquals(confirmPassword)) {
         Toast.makeText(this, "your password is not matching", Toast.LENGTH_SHORT).show()
     }

else {
         addUsers(email,password,name)
     }


        }





    }

fun addUsers(email:String,password:String,name: String){
   mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
  if(it.isSuccessful){
storeUser(email,name)
      Toast.makeText(this,"successfully",Toast.LENGTH_SHORT).show()
      Toast.makeText(this, "you successfully login", Toast.LENGTH_SHORT).show()
      val intent=Intent(this,UserActivity::class.java)
      startActivity(intent)
      finish()

  }
       else{
           Toast.makeText(this,"something wrongs",Toast.LENGTH_SHORT).show()


       }

   }
}

fun storeUser(email: String,name: String){
val user=mAuth.currentUser
    val userId=user!!.uid
val userData= hashMapOf<String,String>()
    userData.put("email",email)
    userData.put("userName",name)
    userData.put("userId",userId)
    userData.put("profileImage","")
firebaseReference.child(userId).setValue(userData).addOnCompleteListener {
    if(it.isSuccessful){
        etEmail.setText("")
        etName.setText("")
        etPassword.setText("")
        etConfirmPassword.setText("")


    }
    else{

        Toast.makeText(this,"this email are already used by another person",Toast.LENGTH_SHORT).show()
    }
}

}



}