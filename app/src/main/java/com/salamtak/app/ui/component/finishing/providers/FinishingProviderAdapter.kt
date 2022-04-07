package com.salamtak.app.ui.component.finishing.providers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingProvider
import com.salamtak.app.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_finishing_provider.*

/**
 * Created by RadwaElsahn on 12/8/2021.
 */

class FinishingProviderAdapter(val listener: FinishingProviderListener) :
    RecyclerView.Adapter<ProviderViewHolder>() {

    lateinit var providers: MutableList<FinishingProvider>

    fun setList(list: MutableList<FinishingProvider>) {
        this.providers = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finishing_provider, parent, false)
        return ProviderViewHolder(
            view
        )
    }

    fun addData(listItems: List<FinishingProvider>) {
        var size = providers?.size!!
        providers?.addAll(listItems)
        notifyItemRangeInserted(size, providers?.size!!)
    }

    override fun onBindViewHolder(holder: ProviderViewHolder, position: Int) {

        holder.bind(providers!![position], listener)
    }

    override fun getItemCount(): Int {
        return if (providers != null) providers!!.size else 0
    }
}

class ProviderViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        provider: FinishingProvider, listener: FinishingProviderListener
    ) {
        iv_logo.loadRoundedImage(
            provider.logoUrl,Constants.IMAGE_CORNER2)

        iv_image.loadRoundedImage(provider.imageUrl, Constants.IMAGE_CORNER2)
//        changeIconFavouriteToIsFavourite(
//            iv_school_favorite,
//            provider.isLiked ?: false
//        )
        tv_name.text = provider.name
        rb_provider.text = (provider.rating?.let { provider.rating } ?: 5f).toString()
        tv_category_name.text = provider.categoryName
//        tv_price.text =
//            String.format(
//                itemView.context.getString(R.string.num_egp_m2),
//                toDecimalNumberFormat(
//                    provider.pricePerMeter.toDouble()
//                )
//            )

//        tv_price.text = String.format(itemView.context.getString(R.string.egp_amount),
//            toDecimalNumberFormat(provider.pricePerMeter))

        provider.pricePerMeter?.let {
            tv_price.text = it.toString()  +" " + provider.pricingType
        }

//        provider.locations?.let {
//            if (it.isNotEmpty())
//                tv_location.text = it[0]
//            else
//                tv_location.toGone()
//        }

        itemView.setOnClickListener { listener.onProviderClicked(provider) }
//        btn_details.setOnClickListener { listener.onProviderClicked(provider) }
    }


}


