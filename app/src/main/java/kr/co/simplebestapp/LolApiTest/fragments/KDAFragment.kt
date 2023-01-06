package kr.co.simplebestapp.LolApiTest.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import kr.co.simplebestapp.LolApiTest.MyValueFormatter
import kr.co.simplebestapp.LolApiTest.databinding.FragmentKDABinding
import java.text.DecimalFormat


class KDAFragment : Fragment(){

    private lateinit var barchart: BarChart

    private lateinit var bundle: Bundle

    private lateinit var kdaAvgList : ArrayList<BarEntry>
    private lateinit var kdaList : ArrayList<BarEntry>
    private lateinit var rank : ArrayList<String>

    private lateinit var kdaStringList : ArrayList<String>

    private lateinit var totalData : BarData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding by lazy { FragmentKDABinding.inflate(inflater, container, false) }
        bundle = Bundle()

        barchart = binding.barchart

        kdaAvgList = ArrayList()
        kdaList = ArrayList()
        rank = ArrayList()
        kdaStringList = ArrayList()

        totalData = BarData()

        return binding.root
    }

    fun setCompareChart(kda : Float, compareKda : Float){

        kdaList.add(BarEntry(0f,kda))
        kdaList.add(BarEntry(1f, compareKda))

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )

        var myDataSet = BarDataSet(kdaList, "kda")
        myDataSet.setDrawValues(true)
        myDataSet.valueTextSize = 15f
        myDataSet.colors = colorList


        var totalData = BarData(myDataSet)
        totalData.barWidth = 0.3f
        totalData.setDrawValues(true)
        totalData.setValueTextSize(10f)
        totalData.setValueFormatter(MyValueFormatter())


        barchart.apply {
            data = totalData
            setFitBars(true)
            description.isEnabled = false
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(rank)
                setDrawGridLines(false)
                isGranularityEnabled = true
                granularity = 1f
                setCenterAxisLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawAxisLine(false)
                gridLineWidth = 3f
            }

            axisRight.apply {
                isEnabled = false
                setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            }
            axisLeft.apply {
                setDrawGridLines(false)
                axisMinimum = 0f
                axisMaximum = 5f
                labelCount = 3
            }

            animateY(2500)
            description.isEnabled = false
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            invalidate()
        }
    }

    fun setBarChart(type : Int, tier : String, kda : Float) {
        var tierAvgKda = 0f
        when(type){
            0 ->{
                when (tier) {
                    "IRON" -> {
                        tierAvgKda = 1.84f
                    }
                    "BRONZE" -> {
                        tierAvgKda = 2.14f
                    }
                    "SILVER" -> {
                        tierAvgKda = 2.26f
                    }
                    "GOLD" -> {
                        tierAvgKda = 2.29f
                    }
                    "PLATINUM" -> {
                        tierAvgKda = 2.32f
                    }
                    "DIAMOND" -> {
                        tierAvgKda = 2.36f
                    }
                    "MASTER" -> {
                        tierAvgKda = 2.41f
                    }
                    "GRANDMASTER" -> {
                        tierAvgKda = 2.42f
                    }
                    "CHALLENGER" -> {
                        tierAvgKda = 2.49f
                    }
                }
            }
            1 ->{
                when (tier) {
                    "IRON" -> {
                        tierAvgKda = 1.78f
                    }
                    "BRONZE" -> {
                        tierAvgKda = 2.04f
                    }
                    "SILVER" -> {
                        tierAvgKda = 2.26f
                    }
                    "GOLD" -> {
                        tierAvgKda = 2.43f
                    }
                    "PLATINUM" -> {
                        tierAvgKda = 2.55f
                    }
                    "DIAMOND" -> {
                        tierAvgKda = 2.62f
                    }
                    "MASTER" -> {
                        tierAvgKda = 2.75f
                    }
                    "GRANDMASTER" -> {
                        tierAvgKda = 2.74f
                    }
                    "CHALLENGER" -> {
                        tierAvgKda = 2.31f
                    }
                }
            }
            3 ->{
                when (tier) {
                    "IRON" -> {
                        tierAvgKda = 2.98f
                    }
                    "BRONZE" -> {
                        tierAvgKda = 3.17f
                    }
                    "SILVER" -> {
                        tierAvgKda = 3.27f
                    }
                    "GOLD" -> {
                        tierAvgKda = 3.34f
                    }
                    "PLATINUM" -> {
                        tierAvgKda = 3.39f
                    }
                    "DIAMOND" -> {
                        tierAvgKda = 3.43f
                    }
                    "MASTER" -> {
                        tierAvgKda = 3.41f
                    }
                    "GRANDMASTER" -> {
                        tierAvgKda = 3.41f
                    }
                    "CHALLENGER" -> {
                        tierAvgKda = 3.47f
                    }
                }
            }
        }
        if(kdaList.isNotEmpty()){
            kdaList.clear()
        }
        if(kdaStringList.isNotEmpty()){
            kdaStringList.clear()
        }
        if(rank.isNotEmpty()){
            rank.clear()
        }
        kdaList.add(BarEntry(0f,kda))
        kdaList.add(BarEntry(1f, tierAvgKda))

        kdaStringList.add(kda.toString())
        kdaStringList.add(tierAvgKda.toString())
        rank.add("내 평점")
        rank.add("$tier 평점")


        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )
        var myDataSet = BarDataSet(kdaList, "kda")
        myDataSet.valueTextSize = 15f
        myDataSet.colors = colorList


        if(totalData.dataSetCount!= 0){
            totalData.clearValues()
        }
        totalData = BarData(myDataSet)
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