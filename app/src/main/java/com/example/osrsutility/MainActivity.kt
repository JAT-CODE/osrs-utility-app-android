package com.example.osrsutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
import android.content.Intent


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d("jpk", "enter onCreate()")

        val geButton = findViewById<Button>(R.id.geButton)
        val profileButton = findViewById<Button>(R.id.profileBtn)

        geButton.setOnClickListener {

            val intent = Intent(this, GrandExchange::class.java)
            startActivity(intent)

        }

        profileButton.setOnClickListener  {
            val intent = Intent(this, ProfilesActivity::class.java)
            startActivity(intent)

        }

        d("jpk", "exit onCreate()")
    }
}