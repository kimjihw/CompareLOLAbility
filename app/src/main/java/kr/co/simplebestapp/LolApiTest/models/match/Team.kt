package com.example.loltest.models.match

data class Team(
    val bans: List<Ban>,
    val objectives: Objectives,
    val teamId: Int,
    val win: Boolean
)