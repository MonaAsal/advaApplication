package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Advertisement
import com.salamtak.app.ui.common.listeners.AdvertisementsItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_advertisment.*

class AdvertisementsAdapter(
    private val listener: AdvertisementsItemListener<Advertisement>, private val items: List<Advertisement>
) : RecyclerView.Adapter<AdvertisementsAdapter.CategoryViewHolder>() {

    private val onItemClickListener: RecyclerItemListener<Advertisement> =
        object : RecyclerItemListener<Advertisement> {

            override fun onItemSelected(item: Advertisement) {
                listener.onAdvertisementSelected(item)
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_advertisment, parent, false)
        return CategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

    holder.bind(items[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
//        return categories.size
        return if (items != null) items!!.size else 0
    }

    class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: Advertisement, recyclerItemListener: RecyclerItemListener<Advertisement>) {
            iv_advertisement.loadRoundedImage(  item?.imageUrl, Constants.IMAGE_CORNER, R.drawable.ic_ad_default )
            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(item) }
        }
    }
}

