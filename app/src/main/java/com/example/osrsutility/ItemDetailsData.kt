package com.example.osrsutility

data class ItemDetailsData (
    val item: DetailsData
)

data class DetailsData (
    val icon: String,
    val icon_large: String,
    val id: Int,
    val type: String,
    val typeIcon: String,
    val name: String,
    val description: String,
    val members: Boolean,
    val current: TrendAndPrice,
    val today: TrendAndPrice,
    val day30: TrendAndChange,
    val day90: TrendAndChange,
    val day180: TrendAndChange,
)

data class TrendAndPrice (
    val trend: String,
    val price: String
)

data class TrendAndChange (
    val trend: String,
    val change: String
)
