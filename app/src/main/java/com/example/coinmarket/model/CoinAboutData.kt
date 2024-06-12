package com.example.coinmarket.model


import com.google.gson.annotations.SerializedName

class CoinAboutData : ArrayList<CoinAboutData.CoinAboutDataItem>(){
    data class CoinAboutDataItem(
        val currencyName: String,
        val info: Info
    ) {
        data class Info(
            val desc: String,
            val github: String,
            val reddit: String,
            val twt: String,
            val web: String
        )
    }
}