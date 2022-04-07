package com.salamtak.app.ui.component.health.subcategory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.SubCategory
import com.salamtak.app.ui.component.health.subcategory.listeners.SubcategoryListener
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.toVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_subcategory.*

/**
 * Created by Radwa Elsahn on 10/23/2018.
 */
class SubCategoriesAdapter(val listener: SubcategoryListener<SubCategory>) :
    RecyclerView.Adapter<SubCategoriesAdapter.SubCategoryViewHolder>() {

    var subCategories: MutableList<SubCategory>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subcategory, parent, false)
        return SubCategoryViewHolder(view)
    }

    fun setList(list: MutableList<SubCategory>) {
        subCategories = list
    }


    fun addData(listItems: List<SubCategory>) {
        subCategories?.let {
            var size = subCategories?.size!!
            subCategories?.addAll(listItems)
            notifyItemRangeInserted(size, subCategories?.size!!)
        } ?: run {
            subCategories = mutableListOf<SubCategory>()
            subCategories?.addAll(listItems)
            notifyDataSetChanged()
        }

    }


    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(
            subCategories!![position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return if (subCategories != null) subCategories!!.size else 0
    }

    class SubCategoryViewHolder(
        override val containerView: View,
    ) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(
            subCategory: SubCategory,
            listener: SubcategoryListener<SubCategory>

        ) {
            tv_subcategory_name.text = subCategory.name
            tv_price.text = subCategory.startsFrom.toInt().toString()
            //tv_more.text = String.format(itemView.context.getString(R.string.num_more_doctor), 10)

            tv_more.setOnClickListener {
                listener.onMoreDoctorsClick(subCategory)
            }
            subCategory.drImagesUrls?.let {
                if (subCategory.drImagesUrls.isNotEmpty())
                    when (subCategory.drImagesUrls.size) {
                        1 -> {
                            iv_dr1.loadCircleImage(subCategory.drImagesUrls[0])
                            iv_dr1.toVisible()
                        }

                        2 -> {
                            iv_dr1.loadCircleImage(subCategory.drImagesUrls[0])
                            iv_dr2.loadCircleImage(subCategory.drImagesUrls[1])
                            iv_dr1.toVisible()
                            iv_dr2.toVisible()
                        }
                        3 -> {
                            iv_dr1.loadCircleImage(subCategory.drImagesUrls[0])
                            iv_dr2.loadCircleImage(subCategory.drImagesUrls[1])
                            iv_dr3.loadCircleImage(subCategory.drImagesUrls[2])

                            iv_dr1.toVisible()
                            iv_dr2.toVisible()
                            iv_dr3.toVisible()
                        }
                    }
                else {
//                    iv_dr1.toGone()
//                    iv_dr2.toGone()
//                    iv_dr3.toGone()
                }


            }

            itemView.setOnClickListener {
                listener.onClick(subCategory)
            }
        }
    }

}



