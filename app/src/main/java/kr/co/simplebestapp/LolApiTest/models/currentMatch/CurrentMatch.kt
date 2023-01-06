package com.example.loltest.models.currentMatch

data class CurrentMatch(
    val bannedChampions: List<BannedChampion>,
    val gameId: Long,
    val gameLength: Int,
    val gameMode: String,
    val gameQueueConfigId: Int,
    val gameStartTime: Long,
    val gameType: String,
    val mapId: Int,
    val observers: Observers,
    val participants: List<Participant>,
    val platformId: String
)