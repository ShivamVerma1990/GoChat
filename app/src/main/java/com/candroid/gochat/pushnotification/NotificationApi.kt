package com.candroid.gochat.pushnotification

import com.candroid.gochat.pushnotification.Constant.Companion.CONTENT_TYPE
import com.candroid.gochat.pushnotification.Constant.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi  {

    @Headers("Authorization :key=$SERVER_KEY","CONTENT_TYPE=$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
  @Body notification: PushNotification
):Response<ResponseBody>

}