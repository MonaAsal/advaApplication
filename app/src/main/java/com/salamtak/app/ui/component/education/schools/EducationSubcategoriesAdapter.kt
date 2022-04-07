package com.salamtak.app.ui.component.education.schools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.EducationSubCategory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_education_subcategory.*

/**
 * Created by RadwaElsahn on 4/26/2021.
 */

class EducationSubCategoryAdapter(val listener: SchoolListener) :
    RecyclerView.Adapter<EducationSubCategoryViewHolder>() {

    lateinit var subcategories: MutableList<EducationSubCategory>

    fun setList(list: MutableList<EducationSubCategory>) {
        this.subcategories = list
        this.subcategories[0].selected = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EducationSubCategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_education_subcategory, parent, false)
        return EducationSubCategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: EducationSubCategoryViewHolder, position: Int) {

        holder.bind(subcategories!![position], listener)
    }

    override fun getItemCount(): Int {
        return if (subcategories != null) subcategories!!.size else 0
    }

    fun selectSubcategory(subCategory: EducationSubCategory) {

        subcategories.forEach { it.selected = false }
        subcategories.filter { it == subCategory }[0].selected = true
        notifyDataSetChanged()
    }
}

class EducationSubCategoryViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        subCategory: EducationSubCategory, listener: SchoolListener
    ) {
        tv_subcategory_name.text = subCategory.name
        if (subCategory.selected) {
            tv_subcategory_name.setBackgroundResource(R.drawable.bg_curved_primary_small)
            tv_subcategory_name.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        }
        else {
            tv_subcategory_name.setBackgroundResource(R.drawable.bg_curved_white_small)
            tv_subcategory_name.setTextColor(ContextCompat.getColor(itemView.context, R.color.textColor))
        }

        itemView.setOnClickListener {
            listener.onSubcategoryClicked(subCategory)
        }
    }


}


