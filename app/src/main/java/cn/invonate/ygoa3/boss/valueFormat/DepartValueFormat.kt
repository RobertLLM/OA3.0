package cn.invonate.ygoa3.boss.valueFormat

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class DepartValueFormat(private val xValues: List<String>) : IAxisValueFormatter {

    override fun getFormattedValue(v: Float, axisBase: AxisBase): String {
        val index = ((v - 10) / 10).toInt()
        return if (index > -1 && index < xValues.size) {
            xValues[index]
        } else {
            ""
        }


    }

    override fun getDecimalDigits(): Int {
        return 5
    }
}
