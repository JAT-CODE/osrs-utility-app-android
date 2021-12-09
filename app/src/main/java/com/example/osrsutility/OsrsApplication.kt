package com.example.osrsutility

import android.app.Application

class OsrsApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val favoriteItemRepository by lazy { FavoriteItemRepository(database.favoriteItemDao()) }
}