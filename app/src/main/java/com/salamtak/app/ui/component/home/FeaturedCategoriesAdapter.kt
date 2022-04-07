package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.HomeCategory
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_featured_categories.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class FeaturedCategoriesAdapter(
    private val listener: RecyclerItemListener<HomeCategory>, private val items: List<HomeCategory>
) : RecyclerView.Adapter<FeaturedCategoriesAdapter.CategoryViewHolder>() {

    private val onItemClickListener: RecyclerItemListener<HomeCategory> =
        object : RecyclerItemListener<HomeCategory> {

            override fun onItemSelected(item: HomeCategory) {
                listener.onItemSelected(item)
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_featured_categories, parent, false)
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

        fun bind(item: HomeCategory, recyclerItemListener: RecyclerItemListener<HomeCategory>) {
            tv_category_name.text = item.name
           // iv_category_image.setImageResource(item.drawable)
            iv_category_image.loadRoundedImage(  item?.imageUrl, Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )

            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(item) }
        }
    }
}

