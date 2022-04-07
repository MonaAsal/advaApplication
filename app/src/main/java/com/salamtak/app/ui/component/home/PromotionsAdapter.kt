package com.salamtak.app.ui.component.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Promotions
import com.salamtak.app.ui.common.listeners.PromotionsItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_advertisment.main_layout
import kotlinx.android.synthetic.main.item_featured_categories.*
import kotlinx.android.synthetic.main.item_promotions.*

class PromotionsAdapter(
    private val listener: PromotionsItemListener<Promotions>, private val items: List<Promotions>
) : RecyclerView.Adapter<PromotionsAdapter.CategoryViewHolder>() {

    private val onItemClickListener: RecyclerItemListener<Promotions> =
        object : RecyclerItemListener<Promotions> {

            override fun onItemSelected(item: Promotions) {
                listener.onPromotionSelected(item)
            }

        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_promotions, parent, false)
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

        fun bind(item: Promotions, recyclerItemListener: RecyclerItemListener<Promotions>) {
            tv_promotion_title.text=item.title
            tv_promotion_subtitle.text=item.subtitle
            iv_Promotions.loadRoundedImage(  item?.imageUrl, Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )
            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(item) }
        }
    }
}

