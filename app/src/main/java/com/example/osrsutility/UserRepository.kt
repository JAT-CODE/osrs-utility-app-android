package com.example.osrsutility

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(u: User) {
        userDao.insert(u)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(u: User) {
        userDao.delete(u)
    }
}