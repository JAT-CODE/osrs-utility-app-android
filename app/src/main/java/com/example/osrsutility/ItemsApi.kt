package com.example.osrsutility

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_ITEMS_URL = "https://prices.runescape.wiki/api/v1/osrs/"
const val BASE_ITEM_DETAILS_URL = "https://secure.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json"


interface ItemsApi {

    @GET("mapping")
    fun getItems() : Call<List<ItemData>>

    @GET(BASE_ITEM_DETAILS_URL)
    fun getItemDetails(@Query("item") itemID: Int) : Call<ItemDetailsData>

    companion object {

        operator fun invoke() : ItemsApi{
            return Retrofit.Builder()
                .baseUrl(BASE_ITEMS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItemsApi::class.java)
        }
    }


}