package com.example.osrsutility

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://prices.runescape.wiki/api/v1/osrs/"

interface ItemsApi {

    @GET("mapping")
    fun getItems() : Call<List<itemData>>

    companion object {

        operator fun invoke() : ItemsApi{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItemsApi::class.java)
        }
    }
}