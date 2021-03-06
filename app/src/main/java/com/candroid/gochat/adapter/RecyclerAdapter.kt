package com.candroid.gochat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.candroid.gochat.R
import com.candroid.gochat.userinfo.User
import com.candroid.gochat.chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerAdapter(val context: Context,val itemList:ArrayList<User>):RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {
class RecyclerViewHolder(view: View):RecyclerView.ViewHolder(view)
{
    val userName:TextView=view.findViewById(R.id.userName)
    val userImage:CircleImageView=view.findViewById(R.id.userImage)
val temp:TextView=view.findViewById(R.id.temp)
   val  layoutUser:LinearLayout=view.findViewById(R.id.layoutUser)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val cool=itemList[position]
holder.userName.text=cool.userName
Glide.with(context).load(cool.profileImage).placeholder(R.drawable.profile_image).into(holder.userImage)
holder.layoutUser.setOnClickListener{
    val intent= Intent(context, ChatActivity::class.java)
    intent.putExtra("userId",cool.userId)
    intent.putExtra("userName",cool.userName)
    context.startActivity(intent)


}
    }

    override fun getItemCount(): Int {
return itemList.size

    }
}