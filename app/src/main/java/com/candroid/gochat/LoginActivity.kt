package com.candroid.gochat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    mAuth= FirebaseAuth.getInstance()
if(mAuth.currentUser!=null){
    val intent=Intent(this,UserActivity::class.java)
    startActivity(intent)
    finish()

}







        register.setOnClickListener {
            val intent=Intent(this,SignUp::class.java)
            startActivity(intent)
            finish()
        }




    btnLogin.setOnClickListener {
        val email=etEmails.text.toString()
        val password=etPasswords.text.toString()
    if(email.isEmpty()&&password.isEmpty()){

        Toast.makeText(this, "one of your field are invalid", Toast.LENGTH_SHORT).show()


    }
        else {
        loginUser(email, password)
    }
    }

    }


fun loginUser(email:String ,password:String){
    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {
        if(it.isSuccessful){
            Toast.makeText(this, "you successfully login", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,UserActivity::class.java)
            startActivity(intent)
        finish()
        }
    else{

            Toast.makeText(this, "please check your fields", Toast.LENGTH_SHORT).show()


        }


    }
}


}