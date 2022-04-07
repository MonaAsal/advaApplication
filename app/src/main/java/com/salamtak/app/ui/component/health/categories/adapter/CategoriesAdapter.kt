package com.salamtak.app.ui.component.health.categories.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Category
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.loadImage
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*
import java.util.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class CategoriesAdapter(
    private val listener: RecyclerItemListener<Category>
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(),
    Filterable {

    var mFilteredList: MutableList<Category>? = null
    lateinit var categories: MutableList<Category>

    var filter = CategoryFilter(this@CategoriesAdapter)
    private val onItemClickListener: RecyclerItemListener<Category> =
        object : RecyclerItemListener<Category> {

            override fun onItemSelected(category: Category) {
                listener.onItemSelected(category)
            }

        }

    fun setList(categories: MutableList<Category>) {
        this.categories = categories
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.bind(mFilteredList!![position], onItemClickListener)
    }

    override fun getItemCount(): Int {
//        return categories.size
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class CategoryFilter(private val adapter: CategoriesAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if(::categories.isInitialized)
                mFilteredList?.addAll(categories)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                mFilteredList?.clear()
                for (row in categories) {
                    if ((row.name.toLowerCase().contains(filterPattern))
                    ) {
                        // Timber.d("filtered" + row.slug.toLowerCase())
                        mFilteredList?.add(row)
                    }
                }
            }

            Log.d("Seach Count", "" + mFilteredList?.size.toString())
            results.values = mFilteredList
            results.count = mFilteredList?.size ?: 0
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            adapter.notifyDataSetChanged()
        }

    }

    init {
        mFilteredList = ArrayList()

        filter = CategoryFilter(this@CategoriesAdapter)
    }

    class CategoryViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(category: Category, recyclerItemListener: RecyclerItemListener<Category>) {
            tv_category_name.text = category.name
            if (category.id != 0)
                iv_category_image.loadRoundedImage(category.imageUrl)
            else {
                iv_category_image.setImageResource(R.color.transparentOverlay)
            }

            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(category) }
        }
    }
}

