package kr.co.simplebestapp.LolApiTest

import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat


class MyValueFormatter : ValueFormatter(),
    IValueFormatter {
    private val mFormat: DecimalFormat = DecimalFormat("###,###,##0.00")
    fun getFormattedValue(
        value: Float,
        entry: Map.Entry<*, *>?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?,
    ): String {
        // write your logic here
        return mFormat.format(value).toString() // e.g. append a dollar-sign
    }

    init {
        // use one decimal
    }
}