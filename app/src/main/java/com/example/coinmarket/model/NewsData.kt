package com.example.coinmarket.model


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("Data")
    val `data`: List<Data>,
) {
    data class Data(
        @SerializedName("body")
        val body: String,
        @SerializedName("downvotes")
        val downvotes: String,
        @SerializedName("imageurl")
        val imageurl: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String
    )
}