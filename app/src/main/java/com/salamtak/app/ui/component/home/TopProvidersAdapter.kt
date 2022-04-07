package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Providers
import com.salamtak.app.ui.common.listeners.ProvidersItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadImage
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_advertisment.main_layout
import kotlinx.android.synthetic.main.item_new_operation_vertical.*
import kotlinx.android.synthetic.main.item_promotions.*
import kotlinx.android.synthetic.main.item_top_provider.*

class TopProvidersAdapter(
    private val listener: ProvidersItemListener<Providers>, private val items: List<Providers>
) : RecyclerView.Adapter<TopProvidersAdapter.CategoryViewHolder>() {

    private val onItemClickListener: RecyclerItemListener<Providers> =
        object : RecyclerItemListener<Providers> {

            override fun onItemSelected(item: Providers) {
                listener.onProviderSelected(item)
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_top_provider, parent, false)
        return CategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.bind(items[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: Providers, recyclerItemListener: RecyclerItemListener<Providers>) {
         //   iv_provider_image.loadImage(  item?.imageUrl )
            item.imageUrl?.let {
                iv_provider_image.loadRoundedImage(item?.imageUrl , Constants.IMAGE_CORNER) //operation image
            }
            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(item) }
        }
    }
}