package kirin3.jp.mpandroidcharthorizontalbarchart

import android.graphics.Color
import android.graphics.RectF
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.TextView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import java.util.ArrayList


class MainActivity : AppCompatActivity(),OnChartValueSelectedListener {

    private var chart: HorizontalBarChart? = null
    private val mOnValueSelectedRectF = RectF()

    private var tvX: TextView? = null
    private var tvY:TextView? = null

    protected var tfRegular: Typeface ?= null
    protected var tfLight: Typeface ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")

        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)

        chart = findViewById(R.id.chart1)
        chart!!.setOnChartValueSelectedListener(this)
        // chart!!.setHighlightEnabled(false);

        chart!!.setDrawBarShadow(false)

        chart!!.setDrawValueAboveBar(true)

        chart!!.getDescription().isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart!!.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart!!.setPinchZoom(false)

        // draw shadows for each bar that show the maximum value
        // chart!!.setDrawBarShadow(true);

        chart!!.setDrawGridBackground(false)


        /*
        val xl = chart!!.getXAxis()
        xl.position = XAxis.XAxisPosition.BOTTOM
        xl.typeface = tfLight
        xl.setDrawAxisLine(true)
        xl.setDrawGridLines(false)
        xl.granularity = 10f


        val labels1 = arrayOf("","1","2","3","4","5","6")
        xl.valueFormatter = IndexAxisValueFormatter(labels1)
        xl.labelCount = 7
        xl.setDrawLabels(true)
        */
        //X軸の設定
        val labels = arrayOf("","国語","数学","英語") //最初の””は原点の値
        chart!!.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            labelCount = 3 //表示させるラベル数
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }


        val yl = chart!!.getAxisLeft()
        yl.typeface = tfLight
        yl.setDrawAxisLine(true)
        yl.setDrawGridLines(true)
        yl.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        val yr = chart!!.getAxisRight()
        yr.typeface = tfLight
        yr.setDrawAxisLine(true)
        yr.setDrawGridLines(false)
        yr.axisMinimum = 0f // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart!!.setFitBars(true)
        chart!!.animateY(2500)


        val l = chart!!.getLegend()
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 8f
        l.xEntrySpace = 4f


        setData(7, 100.0f)
        chart!!.setFitBars(true)
        chart!!.invalidate()

    }

    private fun setData(count: Int, range: Float) {

        val barWidth = 9f
        val spaceForBar = 10f
        val values = ArrayList<BarEntry>()

        for (i in 0 until count) {
            val `val` = (Math.random() * range).toFloat()
            values.add(
                BarEntry(
                    i * spaceForBar, `val`,
                    resources.getDrawable(R.drawable.star)
                )
            )
        }

        val set1: BarDataSet

        if (chart!!.getData() != null && chart!!.getData().dataSetCount > 0) {
            set1 = chart!!.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart!!.getData().notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "DataSet 1")

            set1.setDrawIcons(false)

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.barWidth = barWidth
            chart!!.setData(data)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight) {

        if (e == null)
            return

        val bounds = mOnValueSelectedRectF
        chart!!.getBarBounds(e as BarEntry?, bounds)

        val position = chart!!.getPosition(
            e, chart!!.data.getDataSetByIndex(h.dataSetIndex)
                .axisDependency
        )

        Log.i("bounds", bounds.toString())
        Log.i("position", position.toString())

        MPPointF.recycleInstance(position)
    }


    override fun onNothingSelected() {}


    protected fun saveToGallery() {
    }

}