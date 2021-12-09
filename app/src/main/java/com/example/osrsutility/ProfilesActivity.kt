package com.example.osrsutility

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osrsutility.databinding.ActivityProfilesBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilesBinding

    private var alertDialog: AlertDialog? = null

    private lateinit var viewModel: UserViewModel

    private val adapter = UserAdapter(::onClickUser, ::onDeleteUser)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profiles)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(UserViewModel::class.java)

        binding = ActivityProfilesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter.submitList(viewModel.allUsers.value)
        binding.userRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.userRV.adapter = adapter


        viewModel.allUsers.observe(this, {
            Log.d("User", it.toString())
            adapter.submitList(it.toMutableList())
            Log.d("User", adapter.currentList.toString())
        })


        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        alertDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Add new user")
                val input = EditText(this.context)
                setView(input)
                setPositiveButton("Add", DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK
                    CheckUserExists(input.text.toString())
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled

                })


            }
            builder.create()
        }


    }


    private fun CheckUserExists(text: String) {
        UserApi().getUserDetails(text).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.d("UserChecking", "HERE")
                if(response.isSuccessful) {
                    Log.d("UserChecking", "Successful")
                    viewModel.insertUser(User(text))
                }
                else {
                    Log.d("UserChecking", "Failure")
                    Toast.makeText(applicationContext, "User not found", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("UserChecking", "Error")
                Log.d("UserChecking", "" + t.message)
                Toast.makeText(applicationContext, "User not found", Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add -> {
            // User chose the "add" item
            alertDialog?.show()
            true
        }

        R.id.home -> {
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun onClickUser(user: User) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra("username", user.name)

        startActivity(intent)
        return
    }

    private fun onDeleteUser(user: User) {
        val deleteDialog = let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("Are you sure you want to delete user " + user.name)
                setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK
                    viewModel.deleteUser(user)
                })
                setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled
                })
            }
            builder.create()
        }
        deleteDialog.show()
        return
    }

}