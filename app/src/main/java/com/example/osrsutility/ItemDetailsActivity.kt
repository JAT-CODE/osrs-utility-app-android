package com.example.osrsutility

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import kotlin.math.roundToInt

class ItemDetailsActivity : AppCompatActivity() {

    var gson = Gson()
    lateinit var favorite: MenuItem
    lateinit var unfavorite: MenuItem

    private lateinit var viewModel: FavoriteItemViewModel

    var itemID = -1
    var currItem: DetailsData? = null
    var itemFavorited: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        itemID = intent.getIntExtra("currItemID", -1)
        val itemValue = intent.getIntExtra("currItemValue", -1)
        val itemLowalch = intent.getIntExtra("currItemLowalch", -1)
        val itemHighalch = intent.getIntExtra("currItemHighalch", -1)
        val itemLimit = intent.getIntExtra("currItemLimit", -1)


        if(itemID == -1) {
            finish()
            return
        }


        viewModel =  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(FavoriteItemViewModel::class.java)

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



    // ADD OPTIONS MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_options_menu, menu)
        menu.findItem(R.id.action_favorite)

        favorite = menu.findItem(R.id.action_favorite)
        unfavorite = menu.findItem(R.id.action_unfavorite)

        viewModel.allFavoriteItems.observe(this, {
            itemFavorited = it.contains(FavoriteItem(itemID))
            unfavorite.isVisible = itemFavorited
            favorite.isVisible = !itemFavorited
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            favoriteItem()
            true
        }
        R.id.action_unfavorite -> {
            unfavoriteItem()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun favoriteItem() {
        println("favorite pressed")
        viewModel.insertFavoriteItem(FavoriteItem(itemID))
    }

    private fun unfavoriteItem() {
        println("unfavorite pressed")
        viewModel.deleteFavoriteItem(FavoriteItem(itemID))
    }

        @SuppressLint("SetTextI18n")
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

        findViewById<TextView>(R.id.currentPriceText).text = item?.current?.price

        findViewById<TextView>(R.id.todayChangeAmountText).text = item?.today?.price
        findViewById<TextView>(R.id.todayChangePercentageText).text = calculateChangePercentage(item?.today?.price!!, item.current.price) + "%"

        findViewById<TextView>(R.id.days30ChangeAmountText).text = calculateOriginalPrice(item.current.price, item.day30.change)
        findViewById<TextView>(R.id.days30ChangePercentageText).text = item.day30.change

        findViewById<TextView>(R.id.days90ChangeAmountText).text = calculateOriginalPrice(item.current.price, item.day90.change)
        findViewById<TextView>(R.id.days90ChangePercentageText).text = item.day90.change

        findViewById<TextView>(R.id.days180ChangeAmountText).text = calculateOriginalPrice(item.current.price, item.day180.change)
        findViewById<TextView>(R.id.days180ChangePercentageText).text = item.day180.change

        // Change appropriate arrow
        setCorrectArrow(findViewById(R.id.todayChangeIcon), item.today.trend)
        setCorrectArrow(findViewById(R.id.days30ChangeIcon), item.day30.trend)
        setCorrectArrow(findViewById(R.id.days90ChangeIcon), item.day90.trend)
        setCorrectArrow(findViewById(R.id.days180ChangeIcon), item.day180.trend)

    }

    private fun calculateChangePercentage(changeAmt: String, currentValue: String): String {

        val currValue = changeToNumberFormat(currentValue)
        val changeAmount = changeToNumberFormat(changeAmt)


        if(currValue != null && changeAmount != null)
            return ((changeAmount / currValue) * 100).roundToInt().toString()
        return ""
    }

    private fun setCorrectArrow(v: ImageView, trend: String?) {
        if(trend == "positive")
            v.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.positive))
        else if (trend == "negative")

            v.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.negative))
    }

    private fun changeToNumberFormat(v: String): Double {
        val value = v.replace(" ", "")
        val l = value.get(value.length-1)
        if(l.isLetter() == true) {
            val num = value
                .replace(l.toString(), "")
                .toDouble()
            if(l == 'k') return num * 1000
            else if(l == 'm') return num * 1000000
            else if(l == 'b') return num * 1000000000
        }
        return value.replace(",",".").toDouble()
    }

    private fun calculateOriginalPrice(v: String, changed: String): String {
        val changeMultiplier = 1 + (changed
            .replace(" ", "")
            .replace("%","")
            .replace(",", ".")
            .toDouble())
            .div(100)

        if (changeMultiplier == 1.0)
            return "-"

        val value = changeToNumberFormat(v)

        val result = value - (value / changeMultiplier)

        println("PRICE: " + value)

        println("CHANGEMULTIPLIER: " + changeMultiplier)

        println("RESULT: " + result)


        if(result >= 1000000000 || result <= -1000000000)
            return formatDouble(result / 1000000000) + "b"
        else if(result >= 1000000 || result <= -1000000)
            return formatDouble(result / 1000000) + "m"
        else if(result >= 1000 || result <= -1000)
            return formatDouble(result / 1000) + "k"

        return formatDouble(result)
    }

    private fun formatDouble (num: Double): String {
        return String.format("%.1f", num)
    }
}