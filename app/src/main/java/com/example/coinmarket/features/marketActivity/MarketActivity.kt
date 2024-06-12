package com.example.coinmarket.features.marketActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinmarket.apiManager.ApiManager
import com.example.coinmarket.databinding.ActivityMarketBinding
import com.example.coinmarket.features.coinActivity.CoinActivity
import com.example.coinmarket.model.CoinAboutData
import com.example.coinmarket.model.CoinAboutItem
import com.example.coinmarket.model.CoinsData
import com.google.gson.Gson

const val KEY_SEND_DATA_TO_ACTIVITY_COINMARKET = "dataToSend"
const val KEY_DATACOIN = "dataToSend2"
const val KEY_DATAABOUT = "dataToSend3"

class MarketActivity : AppCompatActivity(), MarketAdapter.RecyclerCallback {

    private lateinit var binding: ActivityMarketBinding
    private lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>
    private val apiManager = ApiManager()
    lateinit var dataNews: ArrayList<Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.includeWhatchlist.btnShowmore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }
        binding.swipeRefreshMain.setOnRefreshListener {
            initUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefreshMain.isRefreshing = false
            }, 1000)
        }
        getAboutDataFromAssets()
    }

    private fun initUi() {

        getNewsFromApi()
        getTopCoinsFromApi()
        binding.includeToolbar.toolbar.title = "Coin Market"
    }

    private fun getAboutDataFromAssets() {

        val fileInString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        aboutDataMap = mutableMapOf<String, CoinAboutItem>()
        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString, CoinAboutData::class.java)

        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )
        }


    }

    private fun getTopCoinsFromApi() {
        apiManager.getCoinsList(object : ApiManager.ApiCallback<List<CoinsData.Data>> {
            override fun onSuccess(data: List<CoinsData.Data>) {
                showDataInRecycler(cleanData(data))
                Log.v("ltest", data.toString())
            }


            override fun onError(errorMessage: String) {
                Toast.makeText(this@MarketActivity, "Error", Toast.LENGTH_SHORT).show()
                Log.v("ltest1", errorMessage)
            }

        })
    }

    private fun getNewsFromApi() {

        apiManager.getNews(object : ApiManager.ApiCallback<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                dataNews = data
                refreshNews()
            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MarketActivity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.v("newslog",errorMessage)
            }

        })

    }

    private fun refreshNews() {
        val randomAccess = (0 until 49).random()
        binding.includeNews.txtNews.text = dataNews[randomAccess].first
        binding.includeNews.txtNews.setOnClickListener {
            refreshNews()
        }
        binding.includeNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dataNews[randomAccess].second))
            startActivity(intent)
        }

    }

    private fun showDataInRecycler(data: List<CoinsData.Data>) {

        val marketAdapter = MarketAdapter(ArrayList(data), this)
        binding.includeWhatchlist.recyclerMain.adapter = marketAdapter
        binding.includeWhatchlist.recyclerMain.layoutManager = LinearLayoutManager(this)

    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {
        val intent = Intent(this, CoinActivity::class.java)


        val bundle = Bundle()

        bundle.putParcelable(KEY_DATACOIN, dataCoin)
        bundle.putParcelable(KEY_DATAABOUT, aboutDataMap[dataCoin.coinInfo?.name])

        intent.putExtra(KEY_SEND_DATA_TO_ACTIVITY_COINMARKET, bundle)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        initUi()
    }

    private fun cleanData(data: List<CoinsData.Data>): List<CoinsData.Data> {

        val newData = mutableListOf<CoinsData.Data>()
        data.forEach {
            if (it.rAW != null && it.dISPLAY != null) {
                newData.add(it)
            }
        }
        return newData
    }
}

