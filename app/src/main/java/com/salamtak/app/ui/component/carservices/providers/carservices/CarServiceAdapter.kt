package com.salamtak.app.ui.component.carservices.providers.carservices

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Category
import com.salamtak.app.ui.common.listeners.RecyclerItemMultiSelectionListener
import com.salamtak.app.utils.makeFontBold
import com.salamtak.app.utils.makeFontNormal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_service.*
import java.util.*

/**
 * Created by RadwaElsahn on 2/2/2022.
 */

class CarServiceAdapter( private val listener: CarServiceFilterListener,
) : RecyclerView.Adapter<CarServiceAdapter.CarServiceViewHolder>(),
    Filterable {

    var mFilteredList: MutableList<Category>? = null

    lateinit var services: MutableList<Category>
    var selectedServices: MutableList<Category>? = null

    var filter = CarServiceFilter(this@CarServiceAdapter)

    private val onItemClickListener: RecyclerItemMultiSelectionListener<Category> =
        object : RecyclerItemMultiSelectionListener<Category> {

            override fun onItemMultiSelected(item: Category) {
                mFilteredList!!.filter { it.id == item.id }[0].isSelected = true

                if (selectedServices == null)
                    selectedServices = mutableListOf(item)
                else {

                    if (selectedServices!!.contains(item).not())
                        selectedServices!!.add(item)
                }

                listener.onCarServicesSelected(selectedServices)
                notifyDataSetChanged()
            }

            override fun onItemDeSelected(item: Category) {
                mFilteredList!!.filter { it.id == item.id }[0].isSelected = false

                selectedServices?.let {
                    var index = it.indexOfFirst { it.id == item.id }
                    if (index != -1)
                        it.removeAt(index)
                }

                listener.onCarServicesSelected(selectedServices)
                notifyDataSetChanged()
            }
        }

    fun setList(list: MutableList<Category>, selectedList: List<Category>?) {
        this.services = list
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(list)
        selectedList?.let {
            selectedServices = it.toMutableList()
            mFilteredList!!.filter { it.id in selectedList.map { it.id } }.map { it.isSelected = true }
//            mFilteredList!!.sortByDescending { it.isSelected }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_service, parent, false)
        return CarServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarServiceViewHolder, position: Int) {
        holder.bind(mFilteredList!![position], onItemClickListener, selectedServices)
    }

    override fun getItemCount(): Int {
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun clearSelection() {
        selectedServices = null
        mFilteredList!!.map { it.isSelected = false }
        notifyDataSetChanged()
    }

    inner class CarServiceFilter(private val adapter: CarServiceAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if (::services.isInitialized)
                    mFilteredList?.addAll(services)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                mFilteredList?.clear()
                for (row in services) {
                    if ((row.name.toLowerCase().contains(filterPattern) || row.name2!!.toLowerCase()
                            .contains(filterPattern))
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

        filter = CarServiceFilter(this@CarServiceAdapter)
    }

    class CarServiceViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(
            category: Category,
            listener: RecyclerItemMultiSelectionListener<Category>,
            selectedList: List<Category>?
        ) {
            tv_name.text = category.name

            if (category.isSelected)
                selectViews()
            else
                unSelectViews()

            main_layout.setOnClickListener {
                if (category.isSelected)
                    listener.onItemDeSelected(category)
                else listener.onItemMultiSelected(category)
            }
        }

        fun selectViews() {
            iv_selected.setImageResource(R.drawable.ic_tick_orange)
            tv_name.makeFontBold()
        }

        fun unSelectViews() {
            iv_selected.setImageResource(R.drawable.ic_circle_orange_border)
            tv_name.makeFontNormal()
        }
    }


}

