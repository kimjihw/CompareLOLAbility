package kr.co.simplebestapp.LolApiTest.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kr.co.simplebestapp.LolApiTest.MyValueFormatter
import kr.co.simplebestapp.LolApiTest.R
import kr.co.simplebestapp.LolApiTest.databinding.FragmentMinionBinding

class MinionFragment : Fragment() {

    private lateinit var bundle: Bundle

    private lateinit var barchart: BarChart


    private lateinit var csAvgList : ArrayList<BarEntry>
    private lateinit var csList : ArrayList<BarEntry>
    private lateinit var rank : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding by lazy { FragmentMinionBinding.inflate(inflater, container, false) }

        bundle = Bundle()
        barchart = binding.barchart


        csAvgList = ArrayList()
        csList = ArrayList()
        rank = ArrayList()
        return binding.root
    }
    fun setBarChart(tier : String, cs : Float) {
        var tierAvgKda = 0f
        when (tier) {
            "IRON" -> {
                tierAvgKda = 92f
            }
            "BRONZE" -> {
                tierAvgKda = 106f
            }
            "SILVER" -> {
                tierAvgKda = 114f
            }
            "GOLD" -> {
                tierAvgKda = 117f
            }
            "PLATINUM" -> {
                tierAvgKda = 117f
            }
            "DIAMOND" -> {
                tierAvgKda = 112f
            }
            "MASTER" -> {
                tierAvgKda = 109f
            }
            "GRANDMASTER" -> {
                tierAvgKda = 114f
            }
            "CHALLENGER" -> {
                tierAvgKda = 111f
            }
        }
        csList.add(BarEntry(0f,cs))
        csList.add(BarEntry(1f, tierAvgKda))
        rank.add("내 CS")
        rank.add("$tier CS")

        val colorList = listOf(
            Color.rgb(192, 255, 140),
            Color.rgb(255, 247, 97)
        )

        var myDataSet = BarDataSet(csList, "cs")
        if(myDataSet.entryCount != 0){
            myDataSet.clear()
        }
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
                axisMaximum = 200f
                labelCount = 3
                setDrawGridLines(false)
            }

            axisRight.apply {
                setDrawLabels(false)
            }

            animateY(1500)
            description.isEnabled = false
            setTouchEnabled(false)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
        }

        barchart.invalidate()
    }


}