package com.candroid.gochat
import android.view.View

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(val context: Context,val chatList:ArrayList<Chat>):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
val MESSAGE_LEFT_ITME=0
    val MESSAGE_RIGHT_ITEM=0
val mAuth=FirebaseAuth.getInstance()
    val user=mAuth.currentUser
    val userId=user!!.uid

    class ChatViewHolder(view:View):RecyclerView.ViewHolder(view){
   val  tvMessage:TextView=view.findViewById(R.id.tvMessage)
  val   userImage:ImageView=view.findViewById(R.id.userImage)

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
if(viewType==MESSAGE_RIGHT_ITEM){
    val view=LayoutInflater.from(parent.context).inflate(R.layout.item_right,parent,false)
return ChatViewHolder(view)
}else{
    val view=LayoutInflater.from(parent.context).inflate(R.layout.item_left,parent,false)
    return ChatViewHolder(view)

}
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat=chatList[position]
        holder.tvMessage.text=chat.message
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(chatList[position].senderId==userId){
            MESSAGE_RIGHT_ITEM

        } else{

            MESSAGE_LEFT_ITME
        }

    }

}