package com.candroid.gochat.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.candroid.gochat.R
import com.candroid.gochat.chat.ChatActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
const val CHANNEL_ID="channelId"
const val CHANNEL_NAME="channelName"
class MessagingService: FirebaseMessagingService() {
companion object{
var shardPref:SharedPreferences?=null
var token:String?
get() {
    return shardPref?.getString("token","")
}
set(value) {
    shardPref?.edit()?.putString("token", value)?.apply()
}

}

    override fun onNewToken(newToken: String) {

        super.onNewToken(newToken)
    token=newToken
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent=Intent(this,ChatActivity::class.java)
    val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager


        val notificationID=Random.nextInt()
     if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
     createNotification(notificationManager)
     }


       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

val pendingIntent=PendingIntent.getActivity(this,0,intent,FLAG_ONE_SHOT)
    val notification=NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(message.data["title"])
        .setContentText(message.data["message"])
        .setSmallIcon(R.drawable.ic_message)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)


        .build()
notificationManager.notify(notificationID,notification)
    }

@RequiresApi(Build.VERSION_CODES.O)
fun createNotification(notificationManager: NotificationManager){
  val channel=NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH).apply {
enableLights(true)
      lightColor=Color.GREEN
  }
notificationManager.createNotificationChannel(channel)
}

}