package com.example.loltest.models.match

data class Metadata(
    val dataVersion: String,
    val matchId: String,
    val participants: List<String>
)