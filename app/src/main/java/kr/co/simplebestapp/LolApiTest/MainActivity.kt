package kr.co.simplebestapp.LolApiTest

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Platform
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.core.staticdata.*
import kotlinx.coroutines.*
import kr.co.simplebestapp.LolApiTest.adapter.ClickBaseRecyclerView
import kr.co.simplebestapp.LolApiTest.common.AppPreferences
import kr.co.simplebestapp.LolApiTest.common.BaseActivity
import kr.co.simplebestapp.LolApiTest.common.Constants
import kr.co.simplebestapp.LolApiTest.common.Constants.MY_KEY
import kr.co.simplebestapp.LolApiTest.databinding.ActivityMainBinding
import kr.co.simplebestapp.LolApiTest.interfaces.RetrofitInterface
import kr.co.simplebestapp.LolApiTest.models.sharedpreferences.History
import java.security.SecureRandom


@OptIn(DelicateCoroutinesApi::class)
class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var inputNameEt: EditText

    private lateinit var historyRv: RecyclerView

    private lateinit var searchImg: ImageView

    private lateinit var retrofitService: RetrofitInterface

    private lateinit var randomChampionImg: ImageView

    private lateinit var imgUrl: String

    private lateinit var baseName : String

    private lateinit var prefs : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private lateinit var championIdList: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val config = Orianna.Configuration()

        config.defaultPlatform = Platform.KOREA
        Orianna.loadConfiguration(config)
        Orianna.setRiotAPIKey(Constants.MY_KEY)
        Orianna.setDefaultRegion(Region.KOREA)
        Orianna.setDefaultPlatform(Platform.KOREA)
        Orianna.setDefaultLocale("ko_KR")

        prefs = getPreferences(MODE_PRIVATE)
        editor = prefs.edit()

        inputNameEt = binding.inputNameEt
        historyRv = binding.historyRv
        searchImg = binding.searchImg
        randomChampionImg = binding.randomChampionImg

        val championId = arrayOf(266,
            103,
            84,
            143)
        championIdList = ArrayList()
        championIdList.addAll(championId)

        setAdapter()
        var random: SecureRandom = SecureRandom()
        var skinRandom: Int = 0

//        CoroutineScope(Dispatchers.Default).launch {
//            var randomNum = random.nextInt(3)
//            skinRandom =
//                (1 until Champion.withId(championIdList[randomNum]).get().skins.size).random()
//
//            imgUrl =
//                Champion.withId(championIdList[randomNum]).get().skins[skinRandom].splashImageURL
//            randomChampionImg.load(imgUrl) {
//                transformations(RoundedCornersTransformation(30f, 30f, 30f, 30f))
//            }
//        }


        searchImg.setOnClickListener {
            if (inputNameEt.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "소환사를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                editor.putString("name", inputNameEt.text.toString())
                editor.commit()
                GlobalScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.Default) {
                        launch { getSummonerInfo() }
                    }
                }
            }
        }

        if(prefs.getString("name","")!!.isNotEmpty()){
            baseName = prefs.getString("name", "").toString()
            var intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("name", baseName)
            startActivity(intent)
        }
    }


    private suspend fun getSummonerInfo() {
        retrofitService = Constants.krRetrofit.create(RetrofitInterface::class.java)

        var result =
            retrofitService.getSummoner(inputNameEt.text.toString(), MY_KEY).body()
        AppPreferences.setHistoryInfo(History(ProfileIcon.withId(result?.profileIconId!!)
            .get().image.url, inputNameEt.text.toString()))

        var sendIntent = Intent(this, DetailActivity::class.java)
        sendIntent.putExtra("name", inputNameEt.text.toString())
        startActivity(sendIntent)
    }

    private fun setAdapter() {

        val bindingInterface =
            object : ClickBaseRecyclerView.ClickBaseRecyclerViewBindingInterface<History> {
                override fun bindData(item: History, view: View, position: Int) {
                    val profileIconImg: ImageView = view.findViewById(R.id.profileIconImg)
                    val historyNameTv: TextView =
                        view.findViewById(R.id.historyNameTv)
                    Glide.with(this@MainActivity).load(item.img).into(profileIconImg)
                    historyNameTv.text = item.name

                }
            }
        val adapter = ClickBaseRecyclerView(AppPreferences.getHistoryInfo(),
            R.layout.item_history,
            bindingInterface)
        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        mLayoutManager.stackFromEnd = true
        historyRv.layoutManager = mLayoutManager
        historyRv.setHasFixedSize(true)
        historyRv.adapter = adapter



        adapter.setOnItemClickListener(object : ClickBaseRecyclerView.OnItemClickListener {
            override fun onItemClick(v: View, pos: Int) {

                var intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("name", AppPreferences.getHistoryInfo()[pos].name)
//                if(AppPreferences.getHistoryInfo()[pos].name != AppPreferences.getHistoryInfo()[adapter.itemCount - 1].name){
//                    adapter.addItem(adapter.itemCount - 1, AppPreferences.getHistoryInfo()[pos])
//                    adapter.removeItem(pos)
//                    adapter.notifyDataSetChanged()
//                }
                startActivity(intent)
            }
        })
    }
}