package com.example.osrsutility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d("jpk", "enter onCreate()");

        val textView = findViewById<TextView>(R.id.helloTextView);

        textView.setOnClickListener {

            val queue = Volley.newRequestQueue(this);
            val url = "https://secure.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=10352";

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Display the first 500 characters of the response string.
                    val obj = JSONObject(response);
                    val item: JSONObject = obj.get("item") as JSONObject;
                    val text = "Item is: ${item.get("name")}\n" +
                            "Current value: ${(item.get("current") as JSONObject).get("price")}"
                    textView.text = text;
                },
                { textView.text = "That didn't work!" }
            )

            // Add the request to the RequestQueue.
            queue.add(stringRequest)




            textView.text = "";
            d("jpk", "Button pressed");
        }

        d("jpk", "exit onCreate()");
    }
}