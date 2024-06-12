package com.example.coinmarket.features.coinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.coinmarket.R
import com.example.coinmarket.apiManager.ALL
import com.example.coinmarket.apiManager.ApiManager
import com.example.coinmarket.apiManager.BASE_URL_TWITTER
import com.example.coinmarket.apiManager.HOUR
import com.example.coinmarket.apiManager.HOUR24
import com.example.coinmarket.apiManager.MONTH
import com.example.coinmarket.apiManager.MONTH3
import com.example.coinmarket.apiManager.WEEK
import com.example.coinmarket.apiManager.YEAR
import com.example.coinmarket.databinding.ActivityCoinBinding
import com.example.coinmarket.features.marketActivity.KEY_DATAABOUT
import com.example.coinmarket.features.marketActivity.KEY_DATACOIN
import com.example.coinmarket.features.marketActivity.KEY_SEND_DATA_TO_ACTIVITY_COINMARKET
import com.example.coinmarket.model.CoinAboutItem
import com.example.coinmarket.model.CoinsData
import ir.dunijet.dunipool.features.apiManager.model.ChartData2

class CoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    private lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val formIntent = intent.getBundleExtra(KEY_SEND_DATA_TO_ACTIVITY_COINMARKET)

        dataThisCoin = formIntent?.getParcelable<CoinsData.Data>(KEY_DATACOIN)!!
        if (formIntent.getParcelable<CoinAboutItem>(KEY_DATAABOUT) != null) {
            dataThisCoinAbout = formIntent.getParcelable<CoinAboutItem>(KEY_DATAABOUT)!!
        } else {
            dataThisCoinAbout = CoinAboutItem()
        }


        initUi()

        binding.includeToolbar.toolbar.title = dataThisCoin.coinInfo!!.fullName

        var period: String = HOUR
        requestAndShowChart(period)

        binding.includeChart.radioGp.setOnCheckedChangeListener { _, checkedId ->


            when (checkedId) {
                R.id.radio_12h -> {
                    period = HOUR
                }

                R.id.radio_1d -> {
                    period = HOUR24
                }

                R.id.radio_1w -> {
                    period = WEEK
                }

                R.id.radio_1m -> {
                    period = MONTH
                }

                R.id.radio_3m -> {
                    period = MONTH3
                }

                R.id.radio_1y -> {
                    period = YEAR
                }

                R.id.radio_all -> {
                    period = ALL
                }
            }
            requestAndShowChart(period)

        }
    }

    private fun initUi() {

        initChartUi()
        initStatisticsUi()
        initAboutUi()

    }

    //  About ui
    @SuppressLint("SetTextI18n")
    private fun initAboutUi() {

        binding.includeAbout.txtWebsite.text = dataThisCoinAbout.coinWebsite
        binding.includeAbout.txtGithub.text = dataThisCoinAbout.coinGithub
        binding.includeAbout.txtTwitter.text = "@" + dataThisCoinAbout.coinTwitter
        binding.includeAbout.txtReddit.text = dataThisCoinAbout.coinReddit
        binding.includeAbout.txtAboutCoin.text = dataThisCoinAbout.coinDesc

        binding.includeAbout.txtWebsite.setOnClickListener {
            clickedItemAbout(BASE_URL_TWITTER + dataThisCoinAbout.coinWebsite!!)
        }
        binding.includeAbout.txtGithub.setOnClickListener {
            clickedItemAbout(dataThisCoinAbout.coinGithub!!)
        }
        binding.includeAbout.txtTwitter.setOnClickListener {
            clickedItemAbout(dataThisCoinAbout.coinTwitter!!)
        }
        binding.includeAbout.txtReddit.setOnClickListener {
            clickedItemAbout(dataThisCoinAbout.coinReddit!!)
        }

    }

    //  Statistics ui
    @SuppressLint("SetTextI18n")
    private fun initStatisticsUi() {
        binding.includeStatistics.tvOpenAmount.text = dataThisCoin.dISPLAY?.uSD?.oPEN24HOUR
        binding.includeStatistics.tvTodaysHighAmount.text = dataThisCoin.dISPLAY?.uSD?.hIGH24HOUR
        binding.includeStatistics.tvTodaysLowAmount.text = dataThisCoin.dISPLAY?.uSD?.lOW24HOUR
        binding.includeStatistics.tvChangeTodayAmount.text = dataThisCoin.dISPLAY?.uSD?.cHANGE24HOUR
        binding.includeStatistics.tvAlgoritm.text = dataThisCoin.coinInfo?.algorithm
        binding.includeStatistics.tvTotalVolume.text = dataThisCoin.dISPLAY?.uSD?.tOTALVOLUME24H
        binding.includeStatistics.tvAvgMarketCapAmount.text = dataThisCoin.dISPLAY?.uSD?.mKTCAP
        binding.includeStatistics.tvSupplyNumber.text = dataThisCoin.dISPLAY?.uSD?.sUPPLY
    }

    //  Chart ui
    private fun initChartUi() {

        binding.includeChart.txtChartPrice.text = dataThisCoin.dISPLAY?.uSD?.pRICE

        binding.includeChart.txtChartChange1.text = " " + dataThisCoin.dISPLAY?.uSD?.cHANGE24HOUR

        if (dataThisCoin.coinInfo?.fullName == "BUSD"){
            binding.includeChart.txtChartChange2.text = "0%"
        }else{
            binding.includeChart.txtChartChange2.text = dataThisCoin.rAW?.uSD?.cHANGEPCT24HOUR.toString().substring(0,4) + "%"
        }

        val taghir = dataThisCoin.rAW?.uSD?.cHANGEPCT24HOUR
        if (taghir!! > 0) {
            binding.includeChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context, R.color.colorGain
                )
            )
            binding.includeChart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context, R.color.colorGain
                )
            )
            binding.includeChart.txtChartUpdown.text = "▲"
            binding.includeChart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context, R.color.colorGain)
        } else if (taghir < 0) {

            binding.includeChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context, R.color.colorLoss
                )
            )
            binding.includeChart.txtChartUpdown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context, R.color.colorLoss
                )
            )
            binding.includeChart.txtChartUpdown.text = "▼"
            binding.includeChart.sparkviewMain.lineColor = ContextCompat.getColor(
                binding.root.context, R.color.colorLoss)

        }

        binding.includeChart.sparkviewMain.setScrubListener {
            if (it==null){
                binding.includeChart.txtChartPrice.text = dataThisCoin.dISPLAY?.uSD?.pRICE
            }else{
                binding.includeChart.txtChartPrice.text = "$ " + (it as ChartData2.Data).close.toString()
            }
        }

    }

    private fun requestAndShowChart(period: String) {

        apiManager.getChartData(
            period,
            dataThisCoin.coinInfo?.name.toString(),
            object : ApiManager.ApiCallback<Pair<List<ChartData2.Data>, ChartData2.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData2.Data>, ChartData2.Data?>) {

                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())
                    binding.includeChart.sparkviewMain.adapter = chartAdapter

                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@CoinActivity, "error =>  $errorMessage", Toast.LENGTH_SHORT)
                        .show()
                }

            })


    }

    private fun clickedItemAbout(url: String) {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}