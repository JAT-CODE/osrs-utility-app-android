package com.example.osrsutility

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    val allUsers: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val dao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(dao)
        allUsers = repository.allUsers
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(user)
    }

    fun insertUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }


}