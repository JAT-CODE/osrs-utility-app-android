package com.example.osrsutility

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast

private const val REQUEST_CODE = 1337
class MainActivity : AppCompatActivity() {

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        d("jpk", "enter onCreate()")

        val geButton = findViewById<Button>(R.id.geButton)
        val profileButton = findViewById<Button>(R.id.profileBtn)
        val mainImageview: ImageView = findViewById<ImageView>(R.id.osrsLogo)

        var easterEggvalue = 0

        //kauppasivun avaus
        geButton.setOnClickListener {
            val intent = Intent(this, GrandExchange::class.java)
            startActivity(intent)

        }
        //profiilisivun avaus
        profileButton.setOnClickListener  {
            val intent = Intent(this, ProfilesActivity::class.java)
            startActivity(intent)
        }
        //kamera funktion lisääminen kurssisuoritusta varten
        mainImageview.setOnClickListener {
            easterEggvalue++;
            if (easterEggvalue == 10) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                    Toast.makeText(this, "EASTER EGG!", Toast.LENGTH_SHORT).show()
                    startActivityForResult(takePictureIntent, REQUEST_CODE)
                    easterEggvalue = 0;
                } else {
                    Toast.makeText(this, "Cannot access camera", Toast.LENGTH_SHORT).show()
                    easterEggvalue = 0;
                }
            }
        }

        d("jpk", "exit onCreate()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val mainImageview: ImageView = findViewById<ImageView>(R.id.osrsLogo)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenImage = data?.extras?.get("data") as Bitmap
            mainImageview.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}