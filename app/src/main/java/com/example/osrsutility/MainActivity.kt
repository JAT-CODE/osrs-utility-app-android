package com.example.osrsutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent




class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d("jpk", "enter onCreate()");

        val geButton = findViewById<Button>(R.id.geButton);
        val textView = findViewById<TextView>(R.id.helloTextView)

        geButton.setOnClickListener {

            val intent = Intent(this, GrandExchange::class.java)
            startActivity(intent)

            textView.text = "";
            d("jpk", "Button pressed");
        }

        d("jpk", "exit onCreate()");
    }
}