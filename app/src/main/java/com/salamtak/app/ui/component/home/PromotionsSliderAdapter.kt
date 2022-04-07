package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.salamtak.app.R
import com.salamtak.app.data.entities.Promotions
import com.salamtak.app.ui.common.listeners.PromotionsItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_advertisment.*
import kotlinx.android.synthetic.main.item_advertisment.view.*
import kotlinx.android.synthetic.main.item_promotions.*
import kotlinx.android.synthetic.main.item_promotions.view.*

class PromotionsSliderAdapter(private val listener: PromotionsItemListener<Promotions>,private val items: List<Promotions>) :
    SliderViewAdapter<PromotionsSliderAdapter.SliderAdapterVH>() {
    private val onItemClickListener: RecyclerItemListener<Promotions> =
        object : RecyclerItemListener<Promotions> {

            override fun onItemSelected(item: Promotions) {
                listener.onPromotionSelected(item)
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.item_promotions, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getCount(): Int {
        return items.size
    }

    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

        fun bind(item: Promotions) {
            itemView.iv_Promotions.loadRoundedImage( item.imageUrl, Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )
            itemView.tv_promotion_title.text=item.title
            itemView.tv_promotion_subtitle.text=item.subtitle

        }
    }
}
