package com.example.osrsutility

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.osrsutility.databinding.ActivityProfilesBinding
import com.example.osrsutility.databinding.ActivityUserDetailsBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class UserDetailsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding

    lateinit var rv: RecyclerView
    lateinit var tv: TextView
    lateinit var userNameTV: TextView
    lateinit var user: String
    lateinit var adapter: UserDetailsAdapter

    var userStats: List<Stat> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val username = intent.getStringExtra("username")

        if(username == null) {
            finish()
            return
        } else {
            user = username
        }

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter = UserDetailsAdapter(applicationContext, userStats)
        binding.userDetailsRV.layoutManager = LinearLayoutManager(this)
        binding.userDetailsRV.adapter = adapter

        userNameTV = findViewById(R.id.userNameTextView)
        userNameTV.text = user


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        UserApi().getUserDetails(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                val csv = response.body()!!.string()
                val userStats = parseUserDetails(csv)
                binding.userDetailsRV.adapter = UserDetailsAdapter(applicationContext, userStats)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("ERROR: " + t.message)
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    fun getUserDetails(user: String) {


    }

    fun parseUserDetails(details: String): ArrayList<Stat> {
        var userStats: ArrayList<Stat> = ArrayList();

        var statNames = arrayOf(
            "Overall", "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
        )
        val dataRows = details.split("\n")
        println("DATAROWS: " + dataRows.size)
        for (i in statNames.indices) {
            val values = dataRows[i].split(",")
            if (values.isNotEmpty())
            userStats.add(Stat(
                name = statNames[i],
                rank = values[0].toInt(),
                level =values[1].toInt(),
                experience = values[2].toLong(),
                null ))
        }
        println("USER STATS: " + userStats.size)
        return userStats
    }
}