package kr.co.simplebestapp.LolApiTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.loltest.adapter.BaseRecyclerView
import com.example.loltest.models.SummonerHistory
import com.example.loltest.models.SummonerHistoryItem
import com.example.loltest.models.match.Match
import com.example.loltest.models.match.Participant
import com.example.loltest.models.most.MostItem
import com.example.loltest.models.summoner.Summoner
import com.google.android.material.tabs.TabLayout
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Platform
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.merakianalytics.orianna.types.core.staticdata.ProfileIcon
import kotlinx.coroutines.*
import kr.co.simplebestapp.LolApiTest.common.Constants
import kr.co.simplebestapp.LolApiTest.common.Constants.MY_KEY
import kr.co.simplebestapp.LolApiTest.databinding.ActivityCompareBinding
import kr.co.simplebestapp.LolApiTest.interfaces.RetrofitInterface
import kr.co.simplebestapp.LolApiTest.models.compare.Compare
import java.security.SecureRandom
import kotlin.math.roundToInt

class CompareActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCompareBinding.inflate(layoutInflater) }

    private lateinit var searchSummonerTierImg: ImageView
    private lateinit var searchSummonerIconImg: ImageView
    private lateinit var compareSummonerIconImg: ImageView
    private lateinit var compareSummonerTierImg: ImageView
    private lateinit var randomChampionImg: ImageView

    private lateinit var championIdList: ArrayList<Int>
    private lateinit var matchHistoryArray: ArrayList<String>


    private lateinit var imgUrl: String
    private lateinit var searchName: String
    private lateinit var compareName: String

    private lateinit var searchSummonerRankTv: TextView
    private lateinit var searchSummonerPointTv: TextView
    private lateinit var searchSummonerLevelTv: TextView
    private lateinit var searchSummonerNameTv: TextView
    private lateinit var compareSummonerRankTv: TextView
    private lateinit var compareSummonerPointTv: TextView
    private lateinit var compareSummonerLevelTv: TextView
    private lateinit var compareSummonerNameTv: TextView
    private lateinit var winloseTv: TextView
    private lateinit var compareWinloseTv: TextView

    private lateinit var retrofitService: RetrofitInterface

    private lateinit var searchSummoner: Summoner
    private lateinit var compareSummoner: Summoner

    private var compareSummonerTier: SummonerHistoryItem? = null
    private lateinit var searchSummonerTier: SummonerHistoryItem

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var compareInfoRv: RecyclerView
    private lateinit var myInfoRv: RecyclerView

    val tabTextList = arrayListOf<String>("KDA", "CS", "골드 획득량", "제어와드 설치")

    private lateinit var viewPager: ViewPager2

    private lateinit var tabAvgLayout: TabLayout

    private lateinit var searchAvg: ArrayList<String>

    private lateinit var searchProgressBar: CircleProgressBar

    private lateinit var compareProgressBar: CircleProgressBar

    private var winnerCnt = 0

    private lateinit var progamer: ArrayList<String>

    private lateinit var randomPro: String

    private lateinit var resetPro: Button

    private lateinit var compareList: ArrayList<Compare>

    private lateinit var changePro: Summoner

    private lateinit var changeProResult: SummonerHistory

    private lateinit var matchResult: ArrayList<Match>

    private lateinit var compareStandard: ArrayList<String>

    private var searchAvgWinCnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = Orianna.Configuration()

        config.defaultPlatform = Platform.KOREA
        Orianna.loadConfiguration(config)
        Orianna.setRiotAPIKey(Constants.MY_KEY)
        Orianna.setDefaultRegion(Region.KOREA)
        Orianna.setDefaultPlatform(Platform.KOREA)
        Orianna.setDefaultLocale("ko_KR")

        randomChampionImg = binding.randomChampionImg
        searchSummonerRankTv = binding.searchSummonerRankTv
        searchSummonerPointTv = binding.searchSummonerPointTv
        searchSummonerNameTv = binding.searchSummonerNameTv
        searchSummonerIconImg = binding.searchSummonerIconImg
        searchSummonerTierImg = binding.searchSummonerTierImg
        compareSummonerNameTv = binding.compareSummonerNameTv
        compareSummonerPointTv = binding.compareSummonerPointTv
        compareSummonerRankTv = binding.compareSummonerRankTv
        compareSummonerIconImg = binding.compareSummonerIconImg
        compareSummonerTierImg = binding.compareSummonerTierImg
        winloseTv = binding.winloseTv
        compareWinloseTv = binding.compareWinLoseTv
        resetPro = binding.resetPro
        compareInfoRv = binding.compareInfoRv
        myInfoRv = binding.myInfoRv

        championIdList = ArrayList()
        matchHistoryArray = ArrayList()
        progamer = ArrayList()
        compareList = ArrayList()
        matchResult = ArrayList()
        compareStandard = ArrayList()
        searchAvg = ArrayList()

        compareName = ""

        if (intent.hasExtra("searchName")) {
            searchSummoner = intent.extras?.getParcelable("searchName")!!
        }
        if (intent.hasExtra("compareName")) {
            compareName = intent.extras?.getString("compareName").toString()
        }
        if (intent.hasExtra("searchTier")) {
            searchSummonerTier = intent.extras?.getParcelable("searchTier")!!
        }
        if (intent.hasExtra("searchAvg")) {
            searchAvg = intent.extras?.getStringArrayList("searchAvg")!!

            searchAvgWinCnt = searchAvg[searchAvg.size - 1].toInt()
            searchAvg.removeAt(searchAvg.size - 1)
        }
        val championId = arrayOf(266,
            103,
            84,
            166,
            12,
            32,
            34,
            1,
            523,
            22,
            136,
            268,
            432,
            200,
            53,
            63,
            201,
            51,
            164,
            69,
            31,
            42,
            122,
            131,
            119,
            36,
            245,
            60,
            28,
            81,
            9,
            114,
            105,
            3,
            41,
            86,
            150,
            79,
            104,
            887,
            120,
            74,
            420,
            39,
            427,
            40,
            59,
            24,
            126,
            202,
            222,
            145,
            429,
            43,
            30,
            38,
            55,
            10,
            141,
            85,
            121,
            203,
            240,
            96,
            897,
            7,
            64,
            89,
            876,
            127,
            236,
            117,
            99,
            54,
            90,
            57,
            11,
            21,
            62,
            82,
            25,
            267,
            75,
            111,
            518,
            76,
            895,
            56,
            20,
            2,
            61,
            516,
            80,
            78,
            555,
            246,
            133,
            497,
            33,
            421,
            526,
            888,
            58,
            107,
            92,
            68,
            13,
            360,
            113,
            235,
            147,
            875,
            35,
            98,
            102,
            27,
            14,
            15,
            72,
            37,
            16,
            50,
            517,
            134,
            223,
            163,
            91,
            44,
            17,
            412,
            18,
            48,
            23,
            4,
            29,
            77,
            6,
            110,
            67,
            45,
            161,
            711,
            254,
            234,
            112,
            8,
            106,
            19,
            498,
            101,
            5,
            157,
            777,
            83,
            350,
            154,
            238,
            221,
            115,
            26,
            142,
            143)
        championIdList = ArrayList()
        championIdList.addAll(championId)

        println(searchAvg)


        var random: SecureRandom = SecureRandom()
        var skinRandom: Int = 0

        settingPro()

        startCoroutine()

        resetPro.setOnClickListener {
            settingPro()
            startCoroutine()
        }

        setContentView(binding.root)
    }

    private fun settingPro() {
        var proList = arrayListOf<String>("u_6sL4ePQHYaIu0AmgpQ_ecBwwsxbn2P84GubVG-SwHV",
            "O_iVwpyOfYgudC9TdgS-g_UZu4Hm01z_V4LwKnAnqhHq4xM",
            "PgRZxGDZJ93_QnrIu9PWt5t5cewuRHCWXp4oGKjzYfHgM7wCR7Aod4iy",
            "X-0ohkli4FMg9gzGTjw4W0Pd-FLslygjI7rjtwRYhv8hFmw",
            "qG_Xg-y-cW5S0YIF7djwEoWcaC9xiEWhzAq84qLCA87Tx74",
            "yoZsgXwYaSm_ZPrS3zTYVSM5T66C4UMjDHRoGjCv2YWD5Xk",
            "EUthlaGHK7Sw83u7rBI_tfcQS_KELlhl_MpLheMt5OLS9A0",
            "U27TxzaynPpsWb4az0C-RQ2_qb6-EZ4YGy-pSwcCOeUH64Y",
            "gLQn0U2uWYmfsWVYaNRyH4cWYZLCM5YKw3PdXLYPcUi54nkigs9JYrra",
            "yEZ_CimZIVuKdfCm87BM1GhZpoasK8P7rYKE9XUlCROz-Dg1eQOSVP4F",
            "rkhuU0cs352_p2dR_Cjsy6PGGVF-uDNd3xFyOGGt2hayMlo",
            "uH4i_1BttTmAzZt0aBqwdepnUJzCDXNDZ-JhlMLreUbYTh0",
            "IdSSDtinfZ4hhzSfYXWJoQqbKfAB3yXAJff9MsH7QR1q2ms",
            "NO4H7WyO5pQ0LoBkdycJvlLVy6DAjDGOcqPfx1wGtQYAlPE",
            "Wr5q9C855Zpq53hauhMOO0ksZmdonMo07pU4uMsgZlp2Wns",
            "Qvnk6pIa2yLTMNzYo60zKEWZS2KvtusDzD_hxl18ZqF1TqY",
            "X0iPzuREdbcLTJgHufXoPwV5ai1HRnCnqEwTEdHa4hJRuCJ4tWsw-H2M",
            "iVWNktYuEQtVQpCcoudyMr-qoA4-YJuZMQkPOlYoaEwCtW4LEMCQu12N",
            "k0pRnRQiwnYEhDGyDWZE9_-DbqRFb8tlXb9ghfL71fVeuxk",
            "zTXMFPxPhzKXoeA1kCVHx2UGz9pQyIDQ0ap4mw2B6Wo-FMk",
            "2XdTGJFS2iVQvn3FDBFtO0kGVn7yuHqqaq1HFyunbaBPJrM",
            "ufvjzflYUMpNkjCh1eCn99c5ywQif6pStv472Wy9k_yxYBBSmMd3wxXo",
            "wgS_huboKnI7lTmZh5bx0Sg79gVI-fIKq_PWDhEsw1WSLU4",
            "7khFJVICbrlOzyolQXt5rAxx-xyPLbUS1uMxTY2s1w8",
            "B_7bsMJO8R6yvcB4S05ZJidYF2IpLvI9avWYGsa6m8GQ0jQqZsudZzWB",
            "2KdvKK597W0FLgxmMmyxMsLorBHm3rrQ5YmSXo3W9M3usE2OmmY0T8ld",
            "mRigDZakMKwckR8fnvjbg81mvQip7r4q0k2Ikth3E7qJIq79DF9asNIh",
            "TihDNbWt9uyR_MMGntrQ6So7SocfHu8iFAJvCZV7pJ6Bv4UjLu5I35LA",
            "O5W5lLqG3KBH0jnONMLHhsNNh9uV3SQxPpenH4QiHl9TG4xDbsofgguE"
        )
        progamer.addAll(proList)
        var random = SecureRandom().nextInt(progamer.size - 1)
        randomPro = progamer[random]
    }


    private fun startCoroutine() {
        GlobalScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                launch {
                    if (compareName.isNotEmpty()) {
                        compareSummoner = ApiActivity().getSummonerInfo(compareName)!!
                        resetPro.visibility = View.GONE
                    } else {
                        compareSummoner = ApiActivity().getPro(randomPro)!!
                        resetPro.visibility = View.VISIBLE
                    }
                }
            }
            withContext(Dispatchers.IO) {
                launch {
                    compareSummonerTier = ApiActivity().searchSummonerTier(compareSummoner)
                }
            }
            CoroutineScope(Dispatchers.Default).launch {
                showTier()
            }
            withContext(Dispatchers.IO) {
                launch {
                    if (matchHistoryArray.isNotEmpty()) {
                        matchHistoryArray.clear()
                    }
                    matchHistoryArray = ApiActivity().getMatchHistories(0, compareSummoner)!!
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                matchResult = ApiActivity().getGame(matchHistoryArray)
                analysisGame()
            }
        }
    }

    private fun showTier() {
        val searchTier: String = searchSummonerTier.tier

        val compareTier: String = if (compareSummonerTier?.tier == null) {
            "UNRANKED"
        } else {
            compareSummonerTier?.tier!!
        }

        val searchRank: String = searchSummonerTier.rank

        val compareRank: String = if (compareSummonerTier?.rank == null) {
            ""
        } else {
            compareSummonerTier?.rank!!
        }

        val searchUrl = ProfileIcon.withId(searchSummoner.profileIconId).get().image.url
        val compareUrl = ProfileIcon.withId(compareSummoner.profileIconId).get().image.url

        uiScope.launch {
            searchSummonerPointTv.text = "${searchSummonerTier?.leaguePoints}LP"
            searchSummonerNameTv.text = searchSummoner.name

            searchSummonerIconImg.load(searchUrl) {
                transformations(RoundedCornersTransformation(30f, 30f, 30f, 30f))
            }

            if (compareSummonerTier?.leaguePoints == null) {
                compareSummonerPointTv.visibility = View.GONE
            } else {
                compareSummonerPointTv.text = "${compareSummonerTier?.leaguePoints}LP"
            }

            compareSummonerNameTv.text = compareSummoner.name

            compareSummonerIconImg.load(compareUrl) {
                transformations(RoundedCornersTransformation(30f, 30f, 30f, 30f))
            }
        }
        when (compareTier) {
            "IRON" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_iron)
                    compareSummonerRankTv.text = "Iron $compareRank"
                }
            }
            "BRONZE" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_bronze)
                    compareSummonerRankTv.text = "Bronze $compareRank"
                }
            }
            "SILVER" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_silver)
                    compareSummonerRankTv.text = "Silver $compareRank"
                }
            }
            "GOLD" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_gold)
                    compareSummonerRankTv.text = "Gold $compareRank"
                }
            }
            "PLATINUM" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_platinum)
                    compareSummonerRankTv.text = "Platinum $compareRank"
                }
            }
            "DIAMOND" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_diamond)
                    compareSummonerRankTv.text = "Diamond $compareRank"
                }
            }
            "MASTER" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_master)
                    compareSummonerRankTv.text = "Master"
                }
            }
            "GRANDMASTER" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_grandmaster)
                    compareSummonerRankTv.text = "Grand Master"
                }
            }
            "CHALLENGER" -> {
                uiScope.launch {
                    compareSummonerTierImg.setImageResource(R.drawable.emblem_challenger)
                    compareSummonerRankTv.text = "Challenger"
                }
            }
            "UNRANKED" -> {
                uiScope.launch {
                    compareSummonerRankTv.text = "Unranked"
                }
            }
        }

        when (searchTier) {
            "IRON" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_iron)
                    searchSummonerRankTv.text = "Iron $searchRank"
                }
            }
            "BRONZE" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_bronze)
                    searchSummonerRankTv.text = "Bronze $searchRank"
                }
            }
            "SILVER" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_silver)
                    searchSummonerRankTv.text = "Silver $searchRank"
                }
            }
            "GOLD" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_gold)
                    searchSummonerRankTv.text = "Gold $searchRank"
                }
            }
            "PLATINUM" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_platinum)
                    searchSummonerRankTv.text = "Platinum $searchRank"
                }
            }
            "DIAMOND" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_diamond)
                    searchSummonerRankTv.text = "Diamond $searchRank"
                }
            }
            "MASTER" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_master)
                    searchSummonerRankTv.text = "Master"
                }
            }
            "GRANDMASTER" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_grandmaster)
                    searchSummonerRankTv.text = "Grand Master"
                }
            }
            "CHALLENGER" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_challenger)
                    searchSummonerRankTv.text = "Challenger"
                }
            }
            "UNRANKED" -> {
                uiScope.launch {
                    searchSummonerRankTv.text = "Unranked"
                }
            }
        }
    }


    private suspend fun analysisGame() {
        var kill = 0f
        var assist = 0f
        var death = 0f
        var damageForChampion = 0
        var cs = 0
        var ward = 0
        var pinkWard = 0
        var gold = 0
        var heal = 0
        var doubleKill = 0
        var tripleKill = 0
        var quadraKill = 0
        var pentaKill = 0

        if (compareStandard.isNotEmpty()) {
            compareStandard.clear()
        }

        if (winnerCnt != 0) {
            winnerCnt = 0
        }

        for (i in matchResult) {
            ioScope.launch {
                val resultChallenges =
                    i.info.participants.filter { it.summonerName == compareSummoner.name }[0]

                kill += resultChallenges.kills.toFloat()
                assist += resultChallenges.assists.toFloat()
                death += resultChallenges.deaths.toFloat()
                heal += resultChallenges.totalHeal

                damageForChampion += (resultChallenges.totalDamageDealtToChampions)
                cs += (resultChallenges.totalMinionsKilled)
                ward += resultChallenges.wardsPlaced
                pinkWard += resultChallenges.detectorWardsPlaced
                gold += resultChallenges.goldEarned

                doubleKill += resultChallenges.doubleKills
                tripleKill += resultChallenges.tripleKills
                quadraKill += resultChallenges.quadraKills
                pentaKill += resultChallenges.pentaKills

                if (resultChallenges.win) {
                    winnerCnt++
                }
            }
        }
        uiScope.launch {
//
            val kdaAvg = (((kill + assist) / death * 100).roundToInt()) / 100f
            val damageForChampionAvg = damageForChampion / matchHistoryArray.size
            val pinkWardAvg = (pinkWard / matchHistoryArray.size)
            val csAvg = cs / matchHistoryArray.size
            val goldAvg = gold / matchHistoryArray.size
            val wardAvg = (ward / matchHistoryArray.size)
            val healAvg = heal / matchHistoryArray.size

            compareStandard.add(kdaAvg.toString())
            compareStandard.add(goldAvg.toString())
            compareStandard.add(csAvg.toString())
            compareStandard.add(wardAvg.toString())
            compareStandard.add(pinkWardAvg.toString())
            compareStandard.add(damageForChampionAvg.toString())
            compareStandard.add(healAvg.toString())
            compareStandard.add(doubleKill.toString())
            compareStandard.add(tripleKill.toString())
            compareStandard.add(quadraKill.toString())
            compareStandard.add(pentaKill.toString())

            Log.e("info", compareStandard.toString())

            compareWinloseTv.text = "${winnerCnt}승 ${matchHistoryArray.size - winnerCnt}패"
            winloseTv.text =
                "${searchAvgWinCnt.toInt()}승 ${matchHistoryArray.size - searchAvgWinCnt.toInt()}패"
        }

        val bindingInterface =
            object : BaseRecyclerView.BaseRecyclerViewBindingInterface<String> {
                override fun bindData(item: String, view: View) {
                    uiScope.launch {
                        val kdaTextView: TextView = view.findViewById(R.id.compareTv)
                        kdaTextView.text = item
                    }
                }
            }
        ioScope.launch {
            val compareAdapter =
                BaseRecyclerView(compareStandard, R.layout.item_compare, bindingInterface)
            compareInfoRv.layoutManager =
                LinearLayoutManager(this@CompareActivity, LinearLayoutManager.VERTICAL, false)
            compareInfoRv.adapter = compareAdapter

        }
        ioScope.launch {
            val searchAdapter =
                BaseRecyclerView(searchAvg, R.layout.item_compare, bindingInterface)
            myInfoRv.layoutManager =
                LinearLayoutManager(this@CompareActivity, LinearLayoutManager.VERTICAL, false)
            myInfoRv.adapter = searchAdapter
        }

    }

}
