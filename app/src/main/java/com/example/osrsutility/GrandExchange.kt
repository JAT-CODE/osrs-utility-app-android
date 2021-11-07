package com.example.osrsutility

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONTokener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osrsutility.databinding.ActivityGrandExchangeBinding
import retrofit2.Call
import retrofit2.Callback


class GrandExchange : AppCompatActivity() {
    private lateinit var binding: ActivityGrandExchangeBinding

    private var items: List<ItemData>? = null
    private var query: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grand_exchange)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityGrandExchangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val queue1 = Volley.newRequestQueue(this)
        val url1 = "https://prices.runescape.wiki/api/v1/osrs/latest"

        val stringRequest1 = StringRequest(
            Request.Method.GET, url1,
            { response ->
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val jsonDatapool = jsonObject.getJSONObject("data")
                    println(jsonDatapool)
                //val itemID = jsonDatapool.keys();
                    //println(itemID);

                jsonDatapool.keys()

            },
            { println("That didn't work!") }
        )

        // Add the request to the RequestQueue.
        queue1.add(stringRequest1)

        ItemsApi().getItems().enqueue(object : Callback<List<ItemData>> {
            override fun onResponse(
                call: Call<List<ItemData>>,
                response: retrofit2.Response<List<ItemData>>
            ) {
                items = response.body()

                items?.let {
                    showData(it)
                }

                handleIntent(intent)
            }

            override fun onFailure(call: Call<List<ItemData>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if(Intent.ACTION_SEARCH == intent.action) {
            query = intent.getStringExtra(SearchManager.QUERY)

            val filteredItems = items?.filter { it.name.contains(query.toString(), true) }

            filteredItems?.let {
                showData(it)
            }
        }
    }

    // ADD OPTIONS MENU
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ge_options_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
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
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }

    }

    private fun showData(items: List<ItemData>){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.adapter = ItemAdapter(items, ::onClickItem )
    }

    private fun onClickItem (item: ItemData) {
        val intent = Intent(this, ItemDetailsActivity::class.java)
        intent.putExtra("currItemID", item.id)
        intent.putExtra("currItemValue", item.value)
        intent.putExtra("currItemLowalch", item.lowalch)
        intent.putExtra("currItemHighalch", item.highalch)
        intent.putExtra("currItemLimit", item.limit)

        startActivity(intent);
        return;
    }
}