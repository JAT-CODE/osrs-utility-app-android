package com.example.osrsutility

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteItemViewModel(application: Application): AndroidViewModel(application) {
    val allFavoriteItems: LiveData<List<FavoriteItem>>
    val repository: FavoriteItemRepository

    init {
        val dao = AppDatabase.getDatabase(application).favoriteItemDao()
        repository = FavoriteItemRepository(dao)
        allFavoriteItems = repository.allFavoriteItems
    }

    fun deleteFavoriteItem(i: FavoriteItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(i)
    }

    fun insertFavoriteItem(i: FavoriteItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(i)
    }
}