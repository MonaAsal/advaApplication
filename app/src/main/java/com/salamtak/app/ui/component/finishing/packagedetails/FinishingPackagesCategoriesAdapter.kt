package com.salamtak.app.ui.component.finishing.packagedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.PackageCategory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_package_category.*

/**
 * Created by RadwaElsahn on 1/5/2022.
 */

class FinishingPackagesCategoriesAdapter(
    private val packageCategories: MutableList<PackageCategory>
) : RecyclerView.Adapter<PackageCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageCategoriesViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_package_category, parent, false)

        val display = parent.context.resources.displayMetrics
        val width = display.widthPixels - display.widthPixels / 2
//        val height = width * 2 / 3
        val params = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT)
        view.layoutParams = params

        return PackageCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageCategoriesViewHolder, position: Int) {

        holder.bind(packageCategories!![position])
    }

    override fun getItemCount(): Int {
        return if (packageCategories != null) packageCategories!!.size else 0
    }


}

class PackageCategoriesViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        item: PackageCategory
    ) {
        tv_package_category_name.text = item.name.trim()
        item.items?.let {
            val adapter = BulletStringsAdapter(
//                mutableListOf<String>(it)
                it.trim().split("||").toMutableList()
            )
            rv_package_category_items.adapter = adapter
        }
    }
}


