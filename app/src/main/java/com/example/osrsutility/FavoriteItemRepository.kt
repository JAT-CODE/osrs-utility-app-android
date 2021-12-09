package com.example.osrsutility

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class FavoriteItemRepository(private val favoriteItemDao: FavoriteItemDao) {

    val allFavoriteItems: LiveData<List<FavoriteItem>> = favoriteItemDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(i: FavoriteItem) {

        favoriteItemDao.insert(i)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(i: FavoriteItem) {
        favoriteItemDao.delete(i)
    }
}