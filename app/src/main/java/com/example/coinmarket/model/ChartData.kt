package com.example.coinmarket.model


import com.google.gson.annotations.SerializedName

data class ChartData(
    @SerializedName("Data")
    val `data`: Data,
    @SerializedName("HasWarning")
    val hasWarning: Boolean,
    @SerializedName("Message")
    val message: String,
    @SerializedName("RateLimit")
    val rateLimit: RateLimit,
    @SerializedName("Response")
    val response: String,
    @SerializedName("Type")
    val type: Int
) {
    data class Data(
        @SerializedName("Aggregated")
        val aggregated: Boolean,
        @SerializedName("Data")
        val `data`: List<Data>,
        @SerializedName("TimeFrom")
        val timeFrom: Int,
        @SerializedName("TimeTo")
        val timeTo: Int
    ) {
        data class Data(
            val close: Double,
            val conversionSymbol: String,
            val conversionType: String,
            val high: Double,
            val low: Double,
            val `open`: Double,
            val time: Int,
            val volumefrom: Double,
            val volumeto: Double
        )
    }

    class RateLimit
}