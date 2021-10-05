package com.example.osrsutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osrsutility.databinding.ActivityGrandExchangeBinding
import retrofit2.Call
import retrofit2.Callback


class GrandExchange : AppCompatActivity() {
    private lateinit var binding: ActivityGrandExchangeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grand_exchange)

        binding = ActivityGrandExchangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val queue1 = Volley.newRequestQueue(this);
        val url1 = "https://prices.runescape.wiki/api/v1/osrs/latest";

        val stringRequest1 = StringRequest(
            Request.Method.GET, url1,
            { response ->
                val jsonObject = JSONTokener(response).nextValue() as JSONObject;
                val jsonDatapool = jsonObject.getJSONObject("data")
                    println(jsonDatapool)
                //val itemID = jsonDatapool.keys();
                    //println(itemID);

                val keys = jsonDatapool.keys();
                while (keys.hasNext()) {
                    val itemID = keys.next()
                    println(itemID);
                }

            },
            { println("That didn't work!") }
        )

        // Add the request to the RequestQueue.
        queue1.add(stringRequest1)

        ItemsApi().getItems().enqueue(object : Callback<List<itemData>> {
            override fun onResponse(
                call: Call<List<itemData>>,
                response: retrofit2.Response<List<itemData>>
            ) {
                val items = response.body()

                items?.let {
                    showData(it)
                }
            }

            override fun onFailure(call: Call<List<itemData>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun showData(items: List<itemData>){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewItems.adapter = itemAdapter(items)
    }
}