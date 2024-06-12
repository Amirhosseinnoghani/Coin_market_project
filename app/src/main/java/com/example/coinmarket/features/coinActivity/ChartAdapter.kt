package com.example.coinmarket.features.coinActivity

import com.robinhood.spark.SparkAdapter
import ir.dunijet.dunipool.features.apiManager.model.ChartData2

class ChartAdapter(
    private val historicalData: List<ChartData2.Data>,
    private val baseLine: String?
) : SparkAdapter() {
    override fun getCount(): Int {
        return historicalData.count()
    }

    override fun getItem(index: Int): ChartData2.Data {
        return historicalData[index]
    }

    override fun getY(index: Int): Float {
        return historicalData[index].close.toFloat()
    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return baseLine?.toFloat() ?: super.getBaseLine()
    }
}