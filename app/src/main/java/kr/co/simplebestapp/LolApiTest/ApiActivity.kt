package kr.co.simplebestapp.LolApiTest

import com.example.loltest.models.SummonerHistory
import com.example.loltest.models.SummonerHistoryItem
import com.example.loltest.models.match.Match
import com.example.loltest.models.matchHistory.MatchHistory
import com.example.loltest.models.most.Most
import com.example.loltest.models.summoner.Summoner
import kr.co.simplebestapp.LolApiTest.common.Constants
import kr.co.simplebestapp.LolApiTest.interfaces.RetrofitInterface

class ApiActivity {

    private lateinit var retrofitService: RetrofitInterface

    suspend fun getSummonerInfo(searchName: String): Summoner? {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)

        val result = retrofitService.getSummoner(searchName, Constants.MY_KEY).body()

        println("puuid ${result?.puuid}")
        println("id ${result?.id}")

        return result
    }

    suspend fun searchSummonerTier(searchSummoner: Summoner): SummonerHistoryItem? {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)

        var result = retrofitService.getSummonerTier(searchSummoner.id, Constants.MY_KEY).body()
        result?.removeIf { it.queueType == "RANKED_FLEX_SR" }
        return if (result.isNullOrEmpty()) {
            null
        } else {
            result[0]
        }
    }
    suspend fun getMost(searchSummoner: Summoner): Most {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)
        var result = retrofitService.getMost(searchSummoner.id, Constants.MY_KEY, 3).body()
        return result!!
    }

    suspend fun getMatchHistories(position: Int, searchSummoner: Summoner): MatchHistory? {
        retrofitService = Constants.asiaRetrofit.create(RetrofitInterface::class.java)
        when (position) {
            0 -> {
                var result =
                    retrofitService.getMatchHistory(searchSummoner.puuid,
                        420,
                        "ranked",
                        0,
                        20,
                        Constants.MY_KEY)
                        .body()

                if (result != null) {
                    return result
                }
            }
            1 -> {
                var result =
                    retrofitService.getMatchHistory(searchSummoner.puuid,
                        440,
                        "ranked",
                        0,
                        20,
                        Constants.MY_KEY)
                        .body()

                if (result != null) {
                    return result
                }
            }

            2 -> {
                var result =
                    retrofitService.getMatchHistory(searchSummoner.puuid,
                        430,
                        "normal",
                        0,
                        20,
                        Constants.MY_KEY)
                        .body()

                if (result != null) {
                    return result
                }
            }
            3 -> {
                var result =
                    retrofitService.getMatchHistory(searchSummoner.puuid,
                        450,
                        "normal",
                        0,
                        20,
                        Constants.MY_KEY)
                        .body()

                if (result != null) {
                    return result
                }
            }
        }
        return null
    }

    suspend fun getGame(matchHistoryArray : ArrayList<String>): ArrayList<Match> {
        retrofitService = Constants.asiaRetrofit.create(RetrofitInterface::class.java)
        val result = ArrayList<Match>()
        if (result.isNotEmpty()){
            result.clear()
        }
        for (i in matchHistoryArray) {
            result.add(retrofitService.getInfo(i, Constants.MY_KEY).body()!!)
        }
        return result
    }

    suspend fun getPro(randomPro: String): Summoner? {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)

        return retrofitService.getPro(randomPro, Constants.MY_KEY).body()
    }
}