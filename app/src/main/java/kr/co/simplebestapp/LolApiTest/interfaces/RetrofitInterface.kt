package kr.co.simplebestapp.LolApiTest.interfaces

import android.media.Image
import com.example.loltest.models.ChampionLevel
import com.example.loltest.models.SummonerHistory
import com.example.loltest.models.SummonerHistoryItem
import com.example.loltest.models.currentMatch.CurrentMatch
import com.example.loltest.models.match.Info
import com.example.loltest.models.match.Match
import com.example.loltest.models.match.Participant
import com.example.loltest.models.matchHistory.MatchHistory
import com.example.loltest.models.most.Most
import com.example.loltest.models.summoner.Summoner
import kr.co.simplebestapp.LolApiTest.models.league.League
import kr.co.simplebestapp.LolApiTest.models.league.LeagueItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun getSummoner(
        @Path("summonerName") name: String,
        @Query("api_key") api_key: String
    ): Response<Summoner>

    @GET("/lol/summoner/v4/summoners/by-account/{encryptedAccountId}")
    suspend fun getPro(
        @Path("encryptedAccountId") id : String,
        @Query("api_key") api_key: String
    ): Response<Summoner>

    @GET("/lol/spectator/v4/active-games/by-summoner/{encryptedSummonerId}")
    suspend fun getCurrentMatch(
        @Path("encryptedSummonerId") encryptedSummonerId : String,
        @Query("api_key") api_key: String
    ): Response<CurrentMatch>

    @GET("/lol/match/v5/matches/by-puuid/{puuid}/ids")
    suspend fun getMatchHistory(
        @Path("puuid") puuid: String,
        @Query("queue") queue : Int,
        @Query("type") type: String,
        @Query("start") start: Int,
        @Query("count") count: Int,
        @Query("api_key") api_key: String
    ): Response<MatchHistory>

    @GET("/lol/match/v5/matches/{matchId}")
    suspend fun getInfo(
        @Path("matchId") matchId: String,
        @Query("api_key") key: String
    ) : Response<Match>

    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    fun getSummonerHistory(
        @Path("encryptedSummonerId") id : String,
        @Query("api_key") key : String
    ) : Call<SummonerHistory>

    @GET("/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/by-champion/{championId}")
    fun getChampionLevel(
        @Path("encryptedSummonerId") id : String,
        @Path("championId") championId : Int,
        @Query("api_key") key : String
    ) : Call<ChampionLevel>

    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun getSummonerTier(
        @Path("encryptedSummonerId") id : String,
        @Query("api_key") key : String
    ) : Response<SummonerHistory>

    @GET("/lol/champion-mastery/v4/champion-masteries/by-summoner/{encryptedSummonerId}/top")
    suspend fun getMost(
        @Path("encryptedSummonerId") id : String,
        @Query("api_key") key : String,
        @Query("count") cnt : Int
    ) : Response<Most>

    @GET("/lol/league/v4/entries/{queue}/{tier}/{division}")
    suspend fun getKda(
        @Path("queue") queue : String,
        @Path("tier") tier: String,
        @Path("division") division : String,
        @Query("api_key") key :String,
        @Query("page") page: Int
    ) : Response<League>
}