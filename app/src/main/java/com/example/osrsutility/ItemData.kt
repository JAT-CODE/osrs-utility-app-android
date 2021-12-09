package com.example.osrsutility

import androidx.lifecycle.LiveData
import androidx.room.*

data class ItemData(
    val id: Int,
    val examine: String,
    val members: Boolean,
    val lowalch: Int,
    val limit: Int,
    val value: Int,
    val highalch: Int,
    val icon: String,
    val name: String
)
