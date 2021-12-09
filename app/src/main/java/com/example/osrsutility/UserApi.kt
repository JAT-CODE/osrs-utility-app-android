package com.example.osrsutility

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_USER_URL = "https://secure.runescape.com/m=hiscore_oldschool/"


interface UserApi {

    @GET("index_lite.ws")
    fun getUserDetails(@Query("player") username: String) : Call<ResponseBody>

    companion object {
        operator fun invoke() : UserApi{
            return Retrofit.Builder()
                .baseUrl(BASE_USER_URL)
                .build()
                .create(UserApi::class.java)
        }
    }


}