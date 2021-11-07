package com.example.osrsutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback

class ItemDetailsActivity : AppCompatActivity() {

    var gson = Gson()

    var currItem: DetailsData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val itemID = intent.getIntExtra("currItemID", -1)
        val itemValue = intent.getIntExtra("currItemValue", -1)
        val itemLowalch = intent.getIntExtra("currItemLowalch", -1)
        val itemHighalch = intent.getIntExtra("currItemHighalch", -1)
        val itemLimit = intent.getIntExtra("currItemLimit", -1)


        if(itemID == -1) {
            finish()
            return
        }

        ItemsApi.invoke().getItemDetails(itemID).enqueue(object : Callback<ItemDetailsData> {
            override fun onResponse(
                call: Call<ItemDetailsData>,
                response: retrofit2.Response<ItemDetailsData>
            ) {
                currItem = response.body()?.item
                displayData(currItem, itemValue, itemLowalch, itemHighalch, itemLimit)
            }

            override fun onFailure(call: Call<ItemDetailsData>, t: Throwable) {
                println("ERROR: " + t.message)
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun displayData(item: DetailsData?, value: Int, lowalch: Int, highalch: Int, limit: Int) {
        // Display item info
        findViewById<TextView>(R.id.itemNameTextView).text = currItem?.name
        if (currItem?.members == true)
            findViewById<ImageView>(R.id.membersOnlyIcon).visibility = View.VISIBLE
        findViewById<TextView>(R.id.itemExamineText).text = currItem?.description

        Glide.with(baseContext)
            .load(currItem?.icon_large)
            .into(findViewById(R.id.itemImageView))

        findViewById<TextView>(R.id.valueText).text = value.toString()
        findViewById<TextView>(R.id.lowalchText).text = lowalch.toString()
        findViewById<TextView>(R.id.highalchText).text = highalch.toString()
        findViewById<TextView>(R.id.limitText).text = limit.toString()

    }
}