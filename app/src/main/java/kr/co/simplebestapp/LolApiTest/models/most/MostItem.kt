package com.example.loltest.models.most

data class MostItem(
    val championId: Int,
    val championLevel: Int,
    val championPoints: Int,
    val championPointsSinceLastLevel: Int,
    val championPointsUntilNextLevel: Int,
    val chestGranted: Boolean,
    val lastPlayTime: Long,
    val summonerId: String,
    val tokensEarned: Int
)