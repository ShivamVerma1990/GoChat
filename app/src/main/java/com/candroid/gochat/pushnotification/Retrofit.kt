package com.candroid.gochat.pushnotification

import com.candroid.gochat.pushnotification.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {
companion object {

val retrofit by lazy {
Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

}
    val api by lazy {
    retrofit.create(NotificationApi::class.java)
    }

}

}