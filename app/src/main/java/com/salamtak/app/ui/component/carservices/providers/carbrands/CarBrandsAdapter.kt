package com.salamtak.app.ui.component.carservices.providers.carbrands

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarModel
import com.salamtak.app.ui.common.listeners.RecyclerItemMultiSelectionListener
import com.salamtak.app.utils.loadImage
import com.salamtak.app.utils.makeFontBold
import com.salamtak.app.utils.makeFontNormal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_brand.*
import kotlinx.android.synthetic.main.item_car_brand.iv_selected
import kotlinx.android.synthetic.main.item_car_brand.main_layout
import kotlinx.android.synthetic.main.item_car_brand.tv_name
import kotlinx.android.synthetic.main.item_car_service.*
import java.util.*

/**
 * Created by RadwaElsahn on 1/26/2022.
 */

class CarBrandsAdapter(
    private val listener: CarBrandsFilterListener,
) : RecyclerView.Adapter<CarBrandsAdapter.CarModelViewHolder>(),
    Filterable {

    var mFilteredList: MutableList<CarModel>? = null
    lateinit var carModels: MutableList<CarModel>
    var selectedCarModels: MutableList<CarModel>? = null

    var filter = CarModelFilter(this@CarBrandsAdapter)

    private val onItemClickListener: RecyclerItemMultiSelectionListener<CarModel> =
        object : RecyclerItemMultiSelectionListener<CarModel> {

            override fun onItemMultiSelected(item: CarModel) {
                mFilteredList!!.filter { it.id == item.id }[0].isSelected = true

                if (selectedCarModels == null)
                    selectedCarModels = mutableListOf(item)
                else {
                    if (selectedCarModels!!.contains(item).not())
                        selectedCarModels!!.add(item)
                }

                listener.onCarBrandsSelected(selectedCarModels)
                notifyDataSetChanged()
            }

            override fun onItemDeSelected(item: CarModel) {
                mFilteredList!!.filter { it.id == item.id }[0].isSelected = false

                selectedCarModels?.let {
                    var index = it.indexOfFirst { it.id == item.id }
                    if (index != -1)
                        it.removeAt(index)
                }

                listener.onCarBrandsSelected(selectedCarModels)
                notifyDataSetChanged()
            }
        }

    fun setList(list: MutableList<CarModel>, carModels: List<CarModel>?) {
        this.carModels = list
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(list)
        carModels?.let {
            selectedCarModels = it.toMutableList()
//            mFilteredList!!.sortByDescending { it.isSelected }
            mFilteredList!!.filter { it.id in carModels.map { it.id } }
                .map { it.isSelected = true }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_car_brand, parent, false)
        return CarModelViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        holder.bind(mFilteredList!![position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun clearSelection() {
        selectedCarModels = null
        mFilteredList!!.map { it.isSelected = false }
        notifyDataSetChanged()
    }


    inner class CarModelFilter(private val adapter: CarBrandsAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if (::carModels.isInitialized)
                    mFilteredList?.addAll(carModels)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                mFilteredList?.clear()
                for (row in carModels) {
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

        filter = CarModelFilter(this@CarBrandsAdapter)
    }

    class CarModelViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(
            carModel: CarModel,
            listener: RecyclerItemMultiSelectionListener<CarModel>
        ) {
            tv_name.text = carModel.name
            iv_logo.loadImage(carModel.logoURL)

            if (carModel.isSelected)
                selectViews()
            else
                unSelectViews()


            main_layout.setOnClickListener {
                if (carModel.isSelected)
                    listener.onItemDeSelected(carModel)
                else listener.onItemMultiSelected(carModel)
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

