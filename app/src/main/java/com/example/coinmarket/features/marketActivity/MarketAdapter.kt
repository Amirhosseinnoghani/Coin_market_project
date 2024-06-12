package com.example.coinmarket.features.marketActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coinmarket.R
import com.example.coinmarket.apiManager.BASE_URL_IMAGE
import com.example.coinmarket.databinding.ItemRecyclerMarketBinding
import com.example.coinmarket.model.CoinsData

class MarketAdapter
    (
    private val data: ArrayList<CoinsData.Data>, private val recyclerCallback: RecyclerCallback
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n", "ResourceType")
        fun bindViews(dataCoin: CoinsData.Data) {

            if (dataCoin.rAW != null && dataCoin.dISPLAY != null) {

                binding.txtCoinname.text = dataCoin.coinInfo.fullName


                binding.txtPrice.text = dataCoin.dISPLAY.uSD.pRICE


                val marketCap = dataCoin.rAW.uSD.mKTCAP / 1000000000
                val indexDot = marketCap.toString().indexOf('.')

                if (marketCap.toString().length > 3) {
                    binding.txtMarketcap.text =
                        "$" + marketCap.toString().substring(0, indexDot + 3) + " B"
                } else if (marketCap.toString().length < 3) {
                    binding.txtMarketcap.text =
                        "$" + marketCap.toString().substring(0, indexDot + 2) + " B"
                }



                binding.txtTaghir.text = dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString()

                val taghir = dataCoin.rAW.uSD.cHANGEPCT24HOUR
                if (taghir > 0) {
                    binding.txtTaghir.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context, R.color.colorGain
                        )
                    )
                    binding.txtTaghir.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"
                } else if (taghir < 0) {
                    binding.txtTaghir.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context, R.color.colorLoss
                        )
                    )
                    binding.txtTaghir.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
                } else {
                    binding.txtTaghir.text = "0%"
                }
                Glide.with(itemView).load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                    .into(binding.imgItem)

                itemView.setOnClickListener {
                    recyclerCallback.onCoinItemClicked(dataCoin)
                }

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(inflater, parent, false)
        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    interface RecyclerCallback {
        fun onCoinItemClicked(dataCoin: CoinsData.Data)
    }

}