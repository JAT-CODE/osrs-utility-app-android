package com.example.osrsutility

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class UserDetailsActivity: AppCompatActivity() {

    lateinit var rv: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_item_details)

        var user = intent.getStringExtra("user")

        rv = findViewById<RecyclerView>(R.id.userDetailsRV)


    }

    fun getUserDetails(user: String) {

    }
}