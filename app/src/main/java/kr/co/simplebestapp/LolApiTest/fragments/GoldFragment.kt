package kr.co.simplebestapp.LolApiTest.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import kr.co.simplebestapp.LolApiTest.MyValueFormatter
import kr.co.simplebestapp.LolApiTest.R
import kr.co.simplebestapp.LolApiTest.databinding.FragmentGoldBinding

class GoldFragment : Fragment() {

    private lateinit var bundle: Bundle


    private lateinit var goldList : ArrayList<BarEntry>
    private lateinit var rank : ArrayList<String>

    private lateinit var barchart : BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding by lazy{FragmentGoldBinding.inflate(inflater, container, false)}

        goldList = ArrayList()
        rank = ArrayList()

        barchart = binding.barchart

        return binding.root
    }

    fun setBarChart(tier : String, gold : Int) {
        var tierAvgKda = 0f
        when (tier) {
            "IRON" -> {
                tierAvgKda = 10108f
            }
            "BRONZE" -> {
                tierAvgKda = 10757f
            }
            "SILVER" -> {
                tierAvgKda = 11059f
            }
            "GOLD" -> {
                tierAvgKda = 11023f
            }
            "PLATINUM" -> {
                tierAvgKda = 10841f
            }
            "DIAMOND" -> {
                tierAvgKda = 10258f
            }
            "MASTER" -> {
                tierAvgKda = 9737f
            }
            "GRANDMASTER" -> {
                tierAvgKda = 9744f
            }
            "CHALLENGER" -> {
                tierAvgKda = 9920f
            }
        }
        println(gold)
        goldList.add(BarEntry(0f,gold.toFloat()))
        goldList.add(BarEntry(1f, tierAvgKda))

        rank.add("내 평점")
        rank.add("$tier 평점")

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )

        var myDataSet = BarDataSet(goldList, "gold")
        myDataSet.setDrawValues(true)
        myDataSet.valueTextSize = 15f
        myDataSet.colors = colorList

        var totalData = BarData(myDataSet)
        totalData.barWidth = 0.4f
        totalData.setDrawValues(true)
        totalData.setValueTextSize(10f)
        totalData.setValueFormatter(MyValueFormatter())

        barchart.apply {
            data = totalData

            xAxis.valueFormatter = IndexAxisValueFormatter(rank)
            xAxis.textSize = 10f
            xAxis.setDrawGridLines(false)
            xAxis.isGranularityEnabled = true
            xAxis.granularity = 1f
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawAxisLine(false)
            xAxis.gridLineWidth = 3f

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 5f
                labelCount = 3
                setDrawGridLines(false)
            }

            axisRight.apply {
                setDrawLabels(false)
            }

            animateY(2500)
            description.isEnabled = false
            setTouchEnabled(false)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false

            invalidate()
        }
    }
}