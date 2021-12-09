package com.example.osrsutility

import androidx.lifecycle.LiveData
import androidx.room.*
import org.jetbrains.annotations.NotNull

@Entity
data class FavoriteItem(
    @PrimaryKey @NotNull val id: Int
)

@Dao
interface FavoriteItemDao {
    @Query("SELECT * FROM favoriteitem")
    fun getAll(): LiveData<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavoriteItem)

    @Delete
    fun delete(user: FavoriteItem)
}