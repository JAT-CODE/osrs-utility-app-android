package com.example.osrsutility

import android.app.SearchManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import android.content.Context
import android.content.Intent
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osrsutility.databinding.ActivityGrandExchangeBinding
import retrofit2.Call
import retrofit2.Callback


class GrandExchange : AppCompatActivity() {

    private lateinit var binding: ActivityGrandExchangeBinding
    private lateinit var viewModel: FavoriteItemViewModel

    private var items: List<ItemData>? = null
    private var query: String? = null

    private var displayOnlyFavorites = false
    private lateinit var favoriteItems: List<FavoriteItem>

    lateinit var hidefavorites: MenuItem
    lateinit var showfavorites: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grand_exchange)

        //Kaikkien itemien haku ja esitys
        //Suosikki itemien haku ja esitys kauppasivulla (jos valittuja)
        viewModel =  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(FavoriteItemViewModel::class.java)

        viewModel.allFavoriteItems.observe (this,{
            favoriteItems = it
            if(displayOnlyFavorites)
                getItems()
        })

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityGrandExchangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getItems()


    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if(Intent.ACTION_SEARCH == intent.action) {
            query = intent.getStringExtra(SearchManager.QUERY)
            filterItems()
        }
    }

    // ADD OPTIONS MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ge_options_menu, menu)

        hidefavorites = menu.findItem(R.id.action_hide_favorites)
        showfavorites = menu.findItem(R.id.action_show_favorites)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnCloseListener { onSearchCloseListener() }
            setQuery(query, false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.home -> {
            finish()
            true
        }

        R.id.action_refresh -> {
            getItems()
            true
        }

        //Suosikkien näyttö, nappi vaihdetaan käänteiseksi (Vain suosikit näkyy <-> Kaikki näkyy)
        R.id.action_show_favorites -> {
            displayOnlyFavorites = true
            getItems()
            showfavorites.isVisible = false
            hidefavorites.isVisible = true
            true
        }
        R.id.action_hide_favorites -> {
            displayOnlyFavorites = false
            getItems()
            showfavorites.isVisible = true
            hidefavorites.isVisible = false
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }

    }

    //Recyclerviewn luonti itemien korttimaista esitystä varten
    private fun showData(items: List<ItemData>){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)

        if(displayOnlyFavorites) {
            val filteredFavorites = items.filter { item ->
                favoriteItems.contains(FavoriteItem(item.id))
            }
            binding.recyclerViewItems.adapter = ItemAdapter(filteredFavorites, ::onClickItem )
        } else {
            binding.recyclerViewItems.adapter = ItemAdapter(items, ::onClickItem )
        }
    }

    //API kutsu itemien saamiseksi, ja sitten niiden esitys käyttäen edellistä funktiota
    //Retrofit, recyclerview
    private fun getItems() {
        ItemsApi().getItems().enqueue(object : Callback<List<ItemData>> {
            override fun onResponse(
                call: Call<List<ItemData>>,
                response: retrofit2.Response<List<ItemData>>
            ) {
                items = response.body()

                if(query.isNullOrEmpty())
                    items?.let { list ->
                        showData(list)
                    }
                else
                    filterItems()
                handleIntent(intent)
            }
            override fun onFailure(call: Call<List<ItemData>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
    //Itemien filtteröinti haku ehdoilla
    private fun filterItems() {
        val filteredItems = items?.filter { it.name.contains(query.toString(), true) }

        filteredItems?.let {
            showData(it)
        }
    }

    private fun onSearchCloseListener (): Boolean {
        query = null
        getItems()
        return false
    }
    //Itemin valinta, lisää item tietoa intentille valitusta itemistä
    private fun onClickItem (item: ItemData) {
        val intent = Intent(this, ItemDetailsActivity::class.java)
        intent.putExtra("currItemID", item.id)
        intent.putExtra("currItemValue", item.value)
        intent.putExtra("currItemLowalch", item.lowalch)
        intent.putExtra("currItemHighalch", item.highalch)
        intent.putExtra("currItemLimit", item.limit)

        startActivity(intent)
        return
    }
}