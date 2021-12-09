package com.example.osrsutility

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class User(
    @PrimaryKey val name: String,
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}
