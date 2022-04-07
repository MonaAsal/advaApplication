package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salamtak.app.R
import com.salamtak.app.data.entities.Advertisement
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_advertisment.view.*

class AdvertisementsSliderAdapter(val items: List<Advertisement>) :
    SliderViewAdapter<AdvertisementsSliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_advertisment, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
            viewHolder.bind(items[position].imageUrl)

    }

    override fun getCount(): Int {
        return items.size
    }

    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

        fun bind(
            item: String
        ) {
            itemView.iv_advertisement.loadRoundedImage(  item, Constants.IMAGE_CORNER, R.drawable.ic_ad_default )
        }
    }
}
