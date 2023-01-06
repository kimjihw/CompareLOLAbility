package com.example.loltest.models.summoner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Summoner(
    val accountId: String,
    val id: String,
    val name: String,
    val profileIconId: Int,
    val puuid: String,
    val revisionDate: Long,
    val summonerLevel: Int
) : Parcelable