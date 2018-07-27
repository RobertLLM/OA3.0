package cn.invonate.ygoa3.boss

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import cn.invonate.ygoa3.BaseActivity
import cn.invonate.ygoa3.Entry.DepartInfo
import cn.invonate.ygoa3.R
import cn.invonate.ygoa3.boss.valueFormat.DepartValueFormat
import cn.invonate.ygoa3.httpUtil.HttpUtil4
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.activity_depart_info.*
import rx.Subscriber
import java.util.*


class DepartInfoActivity : BaseActivity() {

    private lateinit var pk: String


    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depart_info)
        pk = intent.getStringExtra("pk")
        Log.i("pk", pk)
        refresh.setOnRefreshListener {
            getDepartInfo()
        }
        pic_back.setOnClickListener { finish() }
        getDepartInfo()
    }

    /**
     * 获取部门统计信息
     */
    private fun getDepartInfo() {
        val subscriber = object : Subscriber<DepartInfo>() {
            override fun onNext(data: DepartInfo?) {
                if (data != null) {
                    if (data.code == "0000" && data.result != null && !data.result.isEmpty())
                        setData(data.result[0])
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh(false)
                Snackbar.make(refresh, "获取数据失败", Snackbar.LENGTH_LONG).show()
            }
        }
        HttpUtil4.getInstance(this, false).getDepartInfo(subscriber, pk)
    }

    /**
     * 设置数据
     */
    private fun setData(data: DepartInfo.ResultBean) {
        setPie(data)
        // 设置干龄
        setAge(data)
        // 设置学历
        setEdu(data)
        // 设置年龄
        setRage(data)
        // 设置股权等级
        setShares(data)
        // 设置工资
        setWage(data)
        // 设置工龄
        setGage(data)
    }


    /**
     * 设置年龄分布
     */
    private fun setPie(data: DepartInfo.ResultBean) {
        pc_sex.setUsePercentValues(true)
        pc_sex.description.isEnabled = false
        pc_sex.setExtraOffsets(5f, 10f, 5f, 5f)

        pc_sex.holeRadius = 50f
        pc_sex.transparentCircleRadius = 61f

        pc_sex.rotationAngle = -90f
        // 触摸旋转
        pc_sex.isRotationEnabled = true
        pc_sex.isHighlightPerTapEnabled = true

        //模拟数据
        val entries = ArrayList<PieEntry>()

        if (data.empSex1 + data.empSex2 == 0) {
            return // 若总人数为0 不执行
        }

        val man = (data.empSex1 * 1000 / (data.empSex1 + data.empSex2)).toFloat() / 100
        val woman = (data.empSex2 * 1000 / (data.empSex1 + data.empSex2)).toFloat() / 100

        entries.add(PieEntry(man, "男"))
        entries.add(PieEntry(woman, "女"))

        //设置数据
        val dataSet = PieDataSet(entries, "性别")

        dataSet.sliceSpace = 3f        //设置饼状Item之间的间隙
        dataSet.selectionShift = 10f      //设置饼状Item被选中时变化的距离

        //数据和颜色
        val colors = ArrayList<Int>()
        colors.add(getRandomColor())
        colors.add(getRandomColor())
        dataSet.colors = colors
        val sex = PieData(dataSet)
        sex.setValueFormatter(PercentFormatter())
        sex.setValueTextSize(11f)
        sex.setValueTextColor(Color.WHITE)
        pc_sex.data = sex
        pc_sex.highlightValues(null)

        //刷新
        pc_sex.invalidate()

        pc_sex.animateY(1400, Easing.EasingOption.EaseInOutQuad)

        val l = pc_sex.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

    }

    /**
     * 设置干龄
     */
    private fun setAge(data: DepartInfo.ResultBean) {
        val ages = listOf(
                "5年及以下",
                "6-10年",
                "11-15年",
                "16-20年",
                "21-25年",
                "26-30年",
                "31-35年",
                "36-40年",
                "41年及以上"
        )
        val values = listOf(
                data.cadreAge1.toFloat(),
                data.cadreAge2.toFloat(),
                data.cadreAge3.toFloat(),
                data.cadreAge4.toFloat(),
                data.cadreAge5.toFloat(),
                data.cadreAge6.toFloat(),
                data.cadreAge7.toFloat(),
                data.cadreAge8.toFloat(),
                data.cadreAge9.toFloat())
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_age.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_age.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(ages)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = ages.size
        xAxis.axisMaximum = 100f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_age.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }
        val rightAxis = bc_age.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_age.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.cadreAge1.toFloat()))
        yVals1.add(BarEntry(20f, data.cadreAge2.toFloat()))
        yVals1.add(BarEntry(30f, data.cadreAge3.toFloat()))
        yVals1.add(BarEntry(40f, data.cadreAge4.toFloat()))
        yVals1.add(BarEntry(50f, data.cadreAge5.toFloat()))
        yVals1.add(BarEntry(60f, data.cadreAge6.toFloat()))
        yVals1.add(BarEntry(70f, data.cadreAge7.toFloat()))
        yVals1.add(BarEntry(80f, data.cadreAge8.toFloat()))
        yVals1.add(BarEntry(90f, data.cadreAge9.toFloat()))
        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "干龄")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_age.data = age
        bc_age.data.barWidth = 5f

        bc_age.animateY(3000)
    }

    /**
     * 设置学历
     */
    private fun setEdu(data: DepartInfo.ResultBean) {
        val tabs = listOf(
                "小学及以下",
                "初中/技校",
                "高中/中专/职高",
                "大专",
                "本科",
                "硕士"
        )
        val values = listOf(
                data.education1.toFloat(),
                data.education2.toFloat(),
                data.education3.toFloat(),
                data.education4.toFloat(),
                data.education5.toFloat(),
                data.education6.toFloat())
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_edu.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_edu.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(tabs)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = tabs.size
        xAxis.axisMaximum = 70f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_edu.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }
        val rightAxis = bc_edu.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_edu.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.education1.toFloat()))
        yVals1.add(BarEntry(20f, data.education2.toFloat()))
        yVals1.add(BarEntry(30f, data.education3.toFloat()))
        yVals1.add(BarEntry(40f, data.education4.toFloat()))
        yVals1.add(BarEntry(50f, data.education5.toFloat()))
        yVals1.add(BarEntry(60f, data.education6.toFloat()))
        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "现学历")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_edu.data = age
        bc_edu.data.barWidth = 5f

        bc_edu.animateY(3000)
    }

    /**
     * 设置年龄
     */
    private fun setRage(data: DepartInfo.ResultBean) {
        val tabs = listOf(
                "30岁及以下",
                "31-40岁",
                "41-50岁",
                "51岁及以上"
        )
        val values = listOf(
                data.empAge1.toFloat(),
                data.empAge2.toFloat(),
                data.empAge3.toFloat(),
                data.empAge4.toFloat())
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_rage.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_rage.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(tabs)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = tabs.size
        xAxis.axisMaximum = 50f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_rage.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }
        val rightAxis = bc_rage.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_rage.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.empAge1.toFloat()))
        yVals1.add(BarEntry(20f, data.empAge2.toFloat()))
        yVals1.add(BarEntry(30f, data.empAge3.toFloat()))
        yVals1.add(BarEntry(40f, data.empAge4.toFloat()))
        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "年龄")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_rage.data = age
        bc_rage.data.barWidth = 5f

        bc_rage.animateY(3000)
    }

    /**
     * 设置股份
     */
    private fun setShares(data: DepartInfo.ResultBean) {
        val tabs = listOf(
                "无",
                "助理",
                "副科",
                "正科",
                "副处",
                "正处",
                "总经理\n助理",
                "总裁助理",
                "副总裁",
                "常务\n副总裁",
                "总裁"
        )
        val values = listOf(
                data.stockLevel1.toFloat(),
                data.stockLevel2.toFloat(),
                data.stockLevel3.toFloat(),
                data.stockLevel4.toFloat(),
                data.stockLevel5.toFloat(),
                data.stockLevel6.toFloat(),
                data.stockLevel7.toFloat(),
                data.stockLevel8.toFloat(),
                data.stockLevel9.toFloat(),
                data.stockLevel10.toFloat(),
                data.stockLevel11.toFloat()
        )
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_shares.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_shares.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(tabs)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = tabs.size
        xAxis.axisMaximum = 120f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_shares.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }
        val rightAxis = bc_shares.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_shares.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.stockLevel1.toFloat()))
        yVals1.add(BarEntry(20f, data.stockLevel2.toFloat()))
        yVals1.add(BarEntry(30f, data.stockLevel3.toFloat()))
        yVals1.add(BarEntry(40f, data.stockLevel4.toFloat()))
        yVals1.add(BarEntry(50f, data.stockLevel5.toFloat()))
        yVals1.add(BarEntry(60f, data.stockLevel6.toFloat()))
        yVals1.add(BarEntry(70f, data.stockLevel7.toFloat()))
        yVals1.add(BarEntry(80f, data.stockLevel8.toFloat()))
        yVals1.add(BarEntry(90f, data.stockLevel9.toFloat()))
        yVals1.add(BarEntry(100f, data.stockLevel10.toFloat()))
        yVals1.add(BarEntry(110f, data.stockLevel11.toFloat()))

        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "股份等级")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_shares.data = age
        bc_shares.data.barWidth = 5f

        bc_shares.animateY(3000)
    }

    /**
     * 设置工资
     */
    private fun setWage(data: DepartInfo.ResultBean) {
        val tabs = listOf(
                "25级及以下",
                "26-29级",
                "30-33级",
                "34-37级",
                "38-41级",
                "42-48级",
                "49级级以上"
        )
        val values = listOf(
                data.stockLevel1.toFloat(),
                data.stockLevel2.toFloat(),
                data.stockLevel3.toFloat(),
                data.stockLevel4.toFloat(),
                data.stockLevel5.toFloat(),
                data.stockLevel6.toFloat(),
                data.stockLevel7.toFloat()
        )
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_wage.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_wage.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(tabs)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = tabs.size
        xAxis.axisMaximum = 80f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_wage.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }
        val rightAxis = bc_wage.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_wage.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.wageLevel1.toFloat()))
        yVals1.add(BarEntry(20f, data.wageLevel2.toFloat()))
        yVals1.add(BarEntry(30f, data.wageLevel3.toFloat()))
        yVals1.add(BarEntry(40f, data.wageLevel4.toFloat()))
        yVals1.add(BarEntry(50f, data.wageLevel5.toFloat()))
        yVals1.add(BarEntry(60f, data.wageLevel6.toFloat()))
        yVals1.add(BarEntry(70f, data.wageLevel7.toFloat()))

        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "工资等级")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_wage.data = age
        bc_wage.data.barWidth = 5f

        bc_wage.animateY(3000)
    }

    /**
     * 设置工龄
     */
    private fun setGage(data: DepartInfo.ResultBean) {
        val tabs = listOf(
                "2年及以下",
                "3-5年",
                "6-9年",
                "10年及以上"
        )
        val values = listOf(
                data.workingAge1.toFloat(),
                data.workingAge2.toFloat(),
                data.workingAge3.toFloat(),
                data.workingAge4.toFloat()
        )
        val value = findMax(values) // 最大值

        //条形图
        //设置表格上的点，被点击的时候，的回调函数
        bc_gage.description.isEnabled = false

        //上面右边效果图的部分代码，设置X轴
        val xAxis = bc_gage.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)

        val xAxisFormatter = DepartValueFormat(tabs)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 6f
        xAxis.labelCount = tabs.size
        xAxis.axisMaximum = 50f
        xAxis.axisMinimum = 0f
        xAxis.valueFormatter = xAxisFormatter

        val leftAxis = bc_gage.axisLeft
        leftAxis.setLabelCount(10, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = when {
            value == 0f -> 10f
            value < 10 -> value + 1f
            value < 30 -> value + 5f
            else -> value + 10f
        }
        val rightAxis = bc_gage.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(10, false)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f
        rightAxis.axisMaximum = if (value == 0f) {
            10f
        } else {
            value
        }

        // 设置标示，就是那个一组y的value的
        val l2 = bc_gage.legend
        l2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l2.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l2.orientation = Legend.LegendOrientation.HORIZONTAL
        l2.setDrawInside(false)
        //样式
        l2.form = Legend.LegendForm.SQUARE
        //字体
        l2.formSize = 8f
        //大小
        l2.textSize = 8f
        l2.xEntrySpace = 10f

        //设置数据
        val yVals1 = ArrayList<BarEntry>()
        yVals1.add(BarEntry(10f, data.workingAge1.toFloat()))
        yVals1.add(BarEntry(20f, data.workingAge2.toFloat()))
        yVals1.add(BarEntry(30f, data.workingAge3.toFloat()))
        yVals1.add(BarEntry(40f, data.workingAge4.toFloat()))

        val set1: BarDataSet
        set1 = BarDataSet(yVals1, "工龄")
        //设置有四种颜色
        set1.setColors(
                getRandomColor(),
                getRandomColor(),
                getRandomColor(),
                getRandomColor()
        )
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val age = BarData(dataSets)
        age.setValueTextSize(10f)
        age.barWidth = 0.9f
        //设置数据
        bc_gage.data = age
        bc_gage.data.barWidth = 5f

        bc_gage.animateY(3000)
    }


    /**
     * 获取随机颜色
     */
    private fun getRandomColor(): Int {
        return Color.rgb((Math.random() * 200).toInt(), (Math.random() * 200).toInt(), (Math.random() * 200).toInt())
    }

    /**
     * 寻找最大值
     */
    private fun findMax(array: List<Float>): Float {
        var max = array[0]
        for (item in array) {
            if (item > max)
                max = item
        }
        return max
    }
}
