package com.example.osrsutility

data class UserDetailsData(
    val overall: Stat, // 0
    val attack: Stat, // 1
    val defence: Stat, // 2
    val strength: Stat, // 3
    val hitpoints: Stat, // 4
    val ranged: Stat, // 5
    val prayer: Stat, // 6
    val magic: Stat, // 7
    val cooking: Stat, // 8
    val woodcutting: Stat, // 9
    val fletching: Stat, // 10
    val fishing: Stat, // 11
    val firemaking: Stat, // 12
    val crafting: Stat, // 13
    val smithing: Stat, // 14
    val mining: Stat, // 15
    val herblore: Stat, // 16
    val agility: Stat, // 17
    val thieving: Stat, // 18
    val slayer: Stat, // 19
    val farming: Stat, // 20
    val runecrafting: Stat, // 21
    val hunter: Stat, // 22
    val construction: Stat // 23
)

data class Stat(
    val name: String,
    val rank: Int,
    val level: Int,
    val experience: Long?,
    val icon: String?
)
