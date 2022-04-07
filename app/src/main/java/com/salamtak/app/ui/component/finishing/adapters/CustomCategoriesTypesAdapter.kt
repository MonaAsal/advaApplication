package com.salamtak.app.ui.component.finishing.adapters

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
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_generic_category.*
import java.util.*

class CustomCategoriesTypesAdapter(
    private val listener: RecyclerItemListener<Category>, val highlightSelected: Boolean = false
) : RecyclerView.Adapter<CustomCategoriesTypesAdapter.CategoryViewHolder>(),
    Filterable {

    var selectedIndex = -1

    var mFilteredList: MutableList<Category>? = null
    lateinit var categories: MutableList<Category>

    var filter = CategoryFilter(this@CustomCategoriesTypesAdapter)
    private val onItemClickListener: RecyclerItemPositionListener<Category> =
        object : RecyclerItemPositionListener<Category> {

            override fun onItemSelected(category: Category, position: Int) {

                listener.onItemSelected(category)
                if (highlightSelected) {
                    selectedIndex = position
                    notifyDataSetChanged()
                }
            }

        }

    fun selectCategory(position: Int) {
        selectedIndex = position
        notifyDataSetChanged()
    }

    fun setList(categories: MutableList<Category>) {
        this.categories = categories
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(categories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.type_services, parent, false)
        return CategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(
            mFilteredList!![position],
            onItemClickListener,
            selectedIndex,
            highlightSelected
        )
    }

    override fun getItemCount(): Int {
//        return categories.size
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class CategoryFilter(private val adapter: CustomCategoriesTypesAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if (::categories.isInitialized)
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

        filter = CategoryFilter(this@CustomCategoriesTypesAdapter)
    }

    class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(
            category: Category,
            recyclerItemListener: RecyclerItemPositionListener<Category>,
            selectedIndex: Int, highlightSelected: Boolean
        ) {
            tv_category_name.text = category.name

            if (highlightSelected) {
                if (adapterPosition == selectedIndex)
                    main_layout.setBackgroundResource(R.drawable.bg_small_curved_blue)
                else
                    main_layout.setBackgroundResource(R.drawable.bg_small_curved_dark_blue)
            }

           // iv_category_image.loadImage(category.imageUrl)

            main_layout.setOnClickListener {

                recyclerItemListener.onItemSelected(category, adapterPosition)
            }
        }
    }
}
