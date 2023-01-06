package kr.co.simplebestapp.LolApiTest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.bumptech.glide.Glide
import com.dinuscxj.progressbar.CircleProgressBar
import com.example.loltest.adapter.BaseRecyclerView
import com.example.loltest.models.SummonerHistoryItem
import com.example.loltest.models.currentMatch.Participant
import com.example.loltest.models.match.Match
import com.example.loltest.models.most.Most
import com.example.loltest.models.most.MostItem
import com.example.loltest.models.summoner.Summoner
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Platform
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.staticdata.Champion
import com.merakianalytics.orianna.types.core.staticdata.ProfileIcon
import kotlinx.coroutines.*
import kr.co.simplebestapp.LolApiTest.common.BaseActivity
import kr.co.simplebestapp.LolApiTest.common.Constants
import kr.co.simplebestapp.LolApiTest.common.Constants.MY_KEY
import kr.co.simplebestapp.LolApiTest.databinding.ActivityDetailBinding
import kr.co.simplebestapp.LolApiTest.databinding.ToolbarDefaultBinding
import kr.co.simplebestapp.LolApiTest.dialog.CompareDialogFragment
import kr.co.simplebestapp.LolApiTest.interfaces.RetrofitInterface
import java.security.SecureRandom
import kotlin.math.roundToInt

@OptIn(DelicateCoroutinesApi::class)
class DetailActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private lateinit var gotoMainBtn: Button

    lateinit var showDetailLayout: ConstraintLayout

    private lateinit var searchSummonerIconImg: ImageView
    private lateinit var randomChampionImg: ImageView

    private lateinit var searchSummonerTierImg: ImageView
    private lateinit var searchSummonerRankTv: TextView
    private lateinit var searchSummonerPointTv: TextView
    private lateinit var searchSummonerLevelTv: TextView
    private lateinit var searchSummonerNameTv: TextView
    private lateinit var recentGamesKdaTv: TextView
    private lateinit var recentGamesDpmTv: TextView
    private lateinit var recentGamesGoldTv: TextView
    private lateinit var recentGamesWardTv: TextView
    private lateinit var recentGamesPinkWardTv: TextView

    private lateinit var inGameTrueTv: TextView

    private lateinit var toolbar: ToolbarDefaultBinding
    private lateinit var summonerMostRv: RecyclerView

    private lateinit var currentMatchRv: RecyclerView

    private lateinit var mostChampion: Most

    private lateinit var isIngameBtn: Button

    private lateinit var retrofitService: RetrofitInterface

    private lateinit var searchSummoner: Summoner

    private var searchSummonerTier: SummonerHistoryItem? = null

    private lateinit var participant: ArrayList<com.example.loltest.models.currentMatch.Participant>
    private lateinit var blueTeamParticipant: ArrayList<Participant>
    private lateinit var redTeamParticipant: ArrayList<Participant>

    private lateinit var matchHistoryArray: ArrayList<String>
    private lateinit var imgUrl: String

    private lateinit var searchName: String

    private lateinit var championIdList: ArrayList<Int>
    private lateinit var dataValue: ArrayList<RadarEntry>

    private lateinit var viewPager: ViewPager2

    private lateinit var tabAvgLayout: TabLayout

    private lateinit var circleProgressBar: CircleProgressBar

    private lateinit var winloseTv: TextView

    private lateinit var compareProBtn: Button

    var tabTextList = arrayListOf<String>("KDA", "CS", "골드 획득량", "제어와드 설치")

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var winnerCnt = 0

    private lateinit var compareBtn: Button

    private lateinit var compareDialog: CompareDialogFragment

    private lateinit var avg : ArrayList<String>

    private lateinit var tier: String

    private lateinit var spinner: Spinner

    private lateinit var summoners: ArrayList<String>

    private lateinit var summonersPuuid: ArrayList<String>
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var kindRank = 0

    private lateinit var radarChart: RadarChart

    private var position = arrayListOf<String>("TOP", "JUNGLE", "MIDDLE", "CARRY", "SUPPORT")

    private lateinit var matchResult: ArrayList<Match>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        searchSummonerIconImg = binding.searchSummonerIconImg
        searchSummonerTierImg = binding.searchSummonerTierImg
        searchSummonerLevelTv = binding.searchSummonerLevelTv
        searchSummonerPointTv = binding.searchSummonerPointTv
        searchSummonerNameTv = binding.searchSummonerNameTv
        searchSummonerRankTv = binding.searchSummonerRankTv
        summonerMostRv = binding.summonerMostRv
        toolbar = binding.toolbar
//        recentGamesKdaTv = binding.recentGamesKdaTv
//        recentGamesDpmTv = binding.recentGamesDpmTv
//        recentGamesGoldTv = binding.recentGamesGoldTv
//        recentGamesWardTv = binding.recentGamesWardTv
//        recentGamesPinkWardTv = binding.recentGamesPinkWardTv
        randomChampionImg = binding.randomChampionImg
//        viewPager = binding.vp2
//        tabAvgLayout = binding.tabAvgLayout
        circleProgressBar = binding.circleProgress
        winloseTv = binding.winloseTv
        compareBtn = binding.compareBtn
        showDetailLayout = binding.showDetailLayout
        spinner = binding.spinner
        radarChart = binding.radarChart
        compareProBtn = binding.compareProBtn

        pref = getPreferences(MODE_PRIVATE)
        editor = pref.edit()

        championIdList = ArrayList()
        participant = ArrayList()
        blueTeamParticipant = ArrayList()
        redTeamParticipant = ArrayList()
        matchHistoryArray = ArrayList()
        dataValue = ArrayList()
        summoners = ArrayList()
        summonersPuuid = ArrayList()
        matchResult = ArrayList()
        avg = ArrayList()

        compareDialog = CompareDialogFragment()

        searchName = if (pref.getString("name", "")!!.isEmpty()) {
            intent.extras?.getString("name").toString()
        } else {
            pref.getString("name", "").toString()
        }


        val config = Orianna.Configuration()

        config.defaultPlatform = Platform.KOREA
        Orianna.loadConfiguration(config)
        Orianna.setRiotAPIKey(Constants.MY_KEY)
        Orianna.setDefaultRegion(Region.KOREA)
        Orianna.setDefaultPlatform(Platform.KOREA)
        Orianna.setDefaultLocale("ko_KR")

        toolbar.toolbarArrowLayout.setOnClickListener {
            onBackPressed()
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

        var random: SecureRandom = SecureRandom()
        var skinRandom: Int = 0

        CoroutineScope(Dispatchers.Default).launch {
            var randomNum = random.nextInt(161)
            skinRandom =
                (1 until Champion.withId(championIdList[randomNum]).get().skins.size).random()

            imgUrl =
                Champion.withId(championIdList[randomNum]).get().skins[skinRandom].splashImageURL
            randomChampionImg.load(imgUrl) {
                crossfade(true)
                crossfade(2000)
//                transformations(RoundedCornersTransformation(30f, 30f, 30f, 30f))
            }
        }

        compareProBtn.setOnClickListener {
            var intent = Intent(this, CompareActivity::class.java)
            intent.putExtra("searchName", searchSummoner)
            intent.putExtra("searchTier", searchSummonerTier)
//            intent.putExtra("searchAvg", avg)
            startActivity(intent)
        }

        ArrayAdapter.createFromResource(this,
            R.array.spinnerItem,
            android.R.layout.simple_spinner_dropdown_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = this


        compareBtn.setOnClickListener {
            var bundle = Bundle()
            bundle.putParcelable("searchName", searchSummoner)
            bundle.putParcelable("searchTier", searchSummonerTier)
            bundle.putStringArrayList("searchAvg", avg)
            compareDialog.arguments = bundle
            compareDialog.show(supportFragmentManager, compareDialog.tag)
        }
//        viewPager.adapter = ViewPagerAdapter(this, tabTextList.size)
//        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//
//        TabLayoutMediator(tabAvgLayout, viewPager) { tab, position ->
//            tab.text = tabTextList[position]
//        }.attach()

        toolbar.toolbarArrowLayout.bringToFront()
        toolbar.toolbarArrowLayout.setOnClickListener {
            finish()
        }
        toolbar.toolbarSearchLayout.bringToFront()

        toolbar.toolbarSearchLayout.setOnClickListener {
            finish()
        }

    }

    private fun startCoroutine() {
        GlobalScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.IO) {
                launch {
                    searchSummoner = ApiActivity().getSummonerInfo(searchName)!!
                }
            }
            withContext(Dispatchers.IO) {
                launch {
                    searchSummonerTier = ApiActivity().searchSummonerTier(searchSummoner)
                }
            }
            CoroutineScope(Dispatchers.Default).launch {
                showTier()
            }
            CoroutineScope(Dispatchers.IO).launch {
                mostChampion = ApiActivity().getMost(searchSummoner)
                showMost()
            }
            withContext(Dispatchers.IO) {

                launch {
                    if (matchHistoryArray.isNotEmpty()) {
                        matchHistoryArray.clear()
                    }

                    matchHistoryArray = if(!ApiActivity().getMatchHistories(0, searchSummoner).isNullOrEmpty()){
                        ApiActivity().getMatchHistories(0, searchSummoner)!!
                    }else{
                        return@launch
                    }
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                if(matchResult.isNotEmpty()){
                    matchResult.clear()
                }
                matchResult = ApiActivity().getGame(matchHistoryArray)
                analysisGame()
            }
        }
    }

//    private fun changeRank() {
//        GlobalScope.launch(Dispatchers.Default) {
//            withContext(Dispatchers.IO) {
//                launch {
//                    searchSummoner = ModeActivity().getSummonerInfo(searchName)!!
//                }
//            }
//            withContext(Dispatchers.IO) {
//                launch { searchSummonerFlexTier() }
//            }
//            CoroutineScope(Dispatchers.Default).launch {
//                showTier()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                showMost()
//            }
//            withContext(Dispatchers.IO) {
//                launch {
//                    getMatchHistories(1)
//                }
//            }
//        }
//    }

//    private fun blizard() {
////        tabTextList = arrayListOf<String>("KDA", "CS", "골드 획득량")
////
////        uiScope.launch {
////            viewPager.adapter = ViewPagerAdapter(this@DetailActivity, tabTextList.size)
////            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
////
////            TabLayoutMediator(tabAvgLayout, viewPager) { tab, position ->
////                tab.text = tabTextList[position]
////            }.attach()
////        }
//
//        GlobalScope.launch(Dispatchers.Default) {
//            withContext(Dispatchers.IO) {
//                launch { ModeActivity().getSummonerInfo(searchName) }
//            }
//            withContext(Dispatchers.IO) {
//                launch { searchSummonerTier() }
//            }
//            CoroutineScope(Dispatchers.Default).launch {
//                showTier()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                showMost()
//            }
//            withContext(Dispatchers.IO) {
//                launch {
//                    getMatchHistories(2)
//                }
//            }
//        }
//    }


    suspend fun searchSummonerFlexTier() {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)

        var result = retrofitService.getSummonerTier(searchSummoner.id, MY_KEY).body()
        result?.removeIf { it.queueType == "RANKED_SOLO_5x5" }
        searchSummonerTier = if (result.isNullOrEmpty()) {
            null
        } else {
            result[0]
        }
    }

    private fun showTier() {
        tier = if (searchSummonerTier?.tier == null) {
            "UNRANKED"
        } else {
            searchSummonerTier?.tier!!
        }
        val rank: String = if (searchSummonerTier?.rank == null) {
            ""
        } else {
            searchSummonerTier?.rank!!
        }

        val url = ProfileIcon.withId(searchSummoner.profileIconId).get().image.url

        uiScope.launch {
            if (searchSummonerTier?.leaguePoints == null) {
                searchSummonerPointTv.visibility = View.GONE
            } else {
                searchSummonerPointTv.text = "${searchSummonerTier?.leaguePoints}LP"
            }
            searchSummonerNameTv.text = searchSummoner.name
            Glide.with(this@DetailActivity)
                .load(url)
                .into(searchSummonerIconImg)
        }

        when (tier) {
            "IRON" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_iron)
                    searchSummonerRankTv.text = "Iron $rank"
                }
            }
            "BRONZE" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_bronze)
                    searchSummonerRankTv.text = "Bronze $rank"
                }
            }
            "SILVER" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_silver)
                    searchSummonerRankTv.text = "Silver $rank"
                }
            }
            "GOLD" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_gold)
                    searchSummonerRankTv.text = "Gold $rank"
                }
            }
            "PLATINUM" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_platinum)
                    searchSummonerRankTv.text = "Platinum $rank"
                }
            }
            "DIAMOND" -> {
                uiScope.launch {
                    searchSummonerTierImg.setImageResource(R.drawable.emblem_diamond)
                    searchSummonerRankTv.text = "Diamond $rank"
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

    private fun showMost() {
        val bindingInterface =
            object : BaseRecyclerView.BaseRecyclerViewBindingInterface<MostItem> {
                override fun bindData(item: MostItem, view: View) {
                    CoroutineScope(Dispatchers.Default).launch {
                        val mostChampionImg: ImageView = view.findViewById(R.id.mostChampionImg)
                        val mostChampionName: TextView = view.findViewById(R.id.mostChampionNameTv)
                        val mostChampionPoint: TextView =
                            view.findViewById(R.id.mostChampionPointTv)
                        val mostChampionLevel: TextView =
                            view.findViewById(R.id.mostChampionLevelTv)

                        val url = Champion.withId(item.championId).get().image.url

                        uiScope.launch {
                            mostChampionName.text = Champion.withId(item.championId).get().name
                            mostChampionPoint.text = "${item.championPoints}점"
                            mostChampionLevel.text = "${item.championLevel} 레벨"

                            Glide.with(this@DetailActivity)
                                .load(url)
                                .into(mostChampionImg)
                        }
                    }
                }
            }
        uiScope.launch {
            val adapter =
                BaseRecyclerView(mostChampion, R.layout.item_most, bindingInterface)
            summonerMostRv.layoutManager =
                LinearLayoutManager(this@DetailActivity, LinearLayoutManager.VERTICAL, false)
            summonerMostRv.adapter = adapter
        }
    }

    private fun analysisGame() {
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

        var positionList = ArrayList<String>()

        var roleList = ArrayList<String>()

        var positionCnt = arrayListOf<Int>(0, 0, 0, 0, 0)

        if (winnerCnt != 0) {
            winnerCnt = 0
        }
        if (dataValue.isNotEmpty()) {
            dataValue.clear()
        }
        for (i in matchResult) {
            uiScope.launch {
                val resultChallenges =
                    i.info.participants.filter { it.summonerName == searchSummoner.name }[0]
                kill += resultChallenges.kills.toFloat()
                assist += resultChallenges.assists.toFloat()
                death += resultChallenges.deaths.toFloat()
                heal += resultChallenges.totalHeal
                doubleKill += resultChallenges.doubleKills
                tripleKill += resultChallenges.tripleKills
                quadraKill += resultChallenges.quadraKills
                pentaKill += resultChallenges.pentaKills

                roleList.add(resultChallenges.role)
                positionList.add(resultChallenges.lane)
                damageForChampion += (resultChallenges.totalDamageDealtToChampions).toInt()
                cs += (resultChallenges.totalMinionsKilled).toInt()
                ward += resultChallenges.wardsPlaced
                pinkWard += resultChallenges.detectorWardsPlaced
                gold += resultChallenges.goldEarned

                if (resultChallenges.win) {
                    winnerCnt++
                }
            }
        }

        uiScope.launch {

            val kdaAvg = (((kill + assist) / death * 100).roundToInt()) / 100f
            val damageForChampionAvg = damageForChampion / matchHistoryArray.size
            val csAvg = cs / matchHistoryArray.size
            val goldAvg = gold / matchHistoryArray.size
            val wardAvg = (ward / matchHistoryArray.size) + (pinkWard / matchHistoryArray.size)
            val pinkWardAvg = pinkWard / matchHistoryArray.size

            val labels = arrayListOf<String>("KDA", "Ward", "CS", "GOLD", "DAMAGE")
            val data = RadarData()

            dataValue.add(RadarEntry(kdaAvg))
            dataValue.add(RadarEntry(wardAvg.toFloat() / 10f))
            dataValue.add(RadarEntry(csAvg / 100f))
            dataValue.add(RadarEntry(goldAvg / 10000f))
            dataValue.add(RadarEntry(damageForChampionAvg / 10000f))

            for (i in 0 until positionList.size) {
                when (positionList[i]) {
                    "TOP" -> {
                        positionCnt[0] += 1
                    }
                    "JUNGLE" -> {
                        positionCnt[1] += 1
                    }
                    "MIDDLE" -> {
                        positionCnt[2] += 1
                    }
                    else -> {
                        when (roleList[i]) {
                            "CARRY" -> {
                                positionCnt[3] += 1
                            }
                            "SUPPORT" -> {
                                positionCnt[4] += 1
                            }
                        }
                    }

                }
            }
            avg.add(kdaAvg.toString())
            avg.add(goldAvg.toString())
            avg.add(csAvg.toString())
            avg.add((ward / matchHistoryArray.size).toString())
            avg.add(pinkWardAvg.toString())
            avg.add(damageForChampionAvg.toString())
            avg.add((heal / matchHistoryArray.size).toString())
            avg.add(doubleKill.toString())
            avg.add(tripleKill.toString())
            avg.add(quadraKill.toString())
            avg.add(pentaKill.toString())
            avg.add(winnerCnt.toString())
//            avg[5] =
//                ((winnerCnt.toFloat() / matchHistoryArray.size.toFloat()) * 100).toInt().toFloat()
//            avg[6] = winnerCnt.toFloat()


            circleProgressBar.progress =
                ((winnerCnt.toFloat() / matchHistoryArray.size.toFloat()) * 100).toInt()

            winloseTv.text = "${winnerCnt}승 ${matchHistoryArray.size - winnerCnt}패"
            var dataSet = RadarDataSet(dataValue, "DATA")
            data.addDataSet(dataSet)

            var xAxis = radarChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            radarChart.isRotationEnabled = false
            radarChart.data = data
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        ioScope.launch {
//            delay(3000)
//            withContext(Dispatchers.IO) {
//                launch {
//                    getMatchHistories(position)
//                }
//            }
//            withContext(Dispatchers.Main) {
//                launch { analysisGame(position) }
//            }
            kindRank = position
            when (position) {
                0 -> {
                    startCoroutine()
                }
//                1 -> {
//                    changeRank()
//                }
//                3 -> {
//                    blizard()
//                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}