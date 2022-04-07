package com.salamtak.app.ui.component.carservices.providers.carlocation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Area
import com.salamtak.app.ui.common.listeners.RecyclerItemMultiSelectionListener
import com.salamtak.app.utils.makeFontBold
import com.salamtak.app.utils.makeFontNormal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_brand.*
import kotlinx.android.synthetic.main.item_car_brand.iv_selected
import kotlinx.android.synthetic.main.item_car_brand.main_layout
import kotlinx.android.synthetic.main.item_car_brand.tv_name
import kotlinx.android.synthetic.main.item_car_service.*
import java.util.ArrayList

class CarAreasAdapter(private val listener: CarAreasFilterListener) :
    RecyclerView.Adapter<CarAreasAdapter.CarModelViewHolder>()
    ,Filterable{
    var mFilteredList: MutableList<Area>? = null
    lateinit var mAreasList: MutableList<Area>

    var selectedAreaList: MutableList<Area>? = null
    var filter = areaModelFilter(this@CarAreasAdapter)

    init {
        mFilteredList = ArrayList()
        filter = areaModelFilter(this@CarAreasAdapter)
    }

    private val onItemClickListener: RecyclerItemMultiSelectionListener<Area> = object :
        RecyclerItemMultiSelectionListener<Area> {

        override fun onItemMultiSelected(item: Area) {

            mFilteredList!!.filter { it.id == item.id }[0].isSelected = true
            if (selectedAreaList == null)
                selectedAreaList = mutableListOf(item)
            else {
                if (selectedAreaList!!.contains(item).not())
                    selectedAreaList!!.add(item)
            }

            listener.onCarLocationSelected(selectedAreaList)
            notifyDataSetChanged()
        }

        override fun onItemDeSelected(item: Area) {
            mFilteredList!!.filter { it.id == item.id }[0].isSelected = false

            selectedAreaList?.let {
                var index = it.indexOfFirst { it.id == item.id }
                if (index != -1)
                    it.removeAt(index)
            }

            listener.onCarLocationSelected(selectedAreaList)
            notifyDataSetChanged()

        }
    }

    fun setUnSelected(areaModel: Area) {
        areaModel.isSelected = false
    }

    fun setSelected(areaModel: Area) {
        areaModel.isSelected = true
    }

    fun clearSelection() {
        selectedAreaList = null
        mFilteredList!!.map {it.isSelected = false }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_area, parent, false)
        return CarModelViewHolder(view)
    }


    fun setList(list: MutableList<Area>, selectedList: List<Area>?) {
        this.mAreasList = list
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(list)
        selectedList?.let {
            selectedAreaList = it.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        holder.bind(mFilteredList!![position] ,onItemClickListener, selectedAreaList)
        holder.setIsRecyclable(false);

    }

    class CarModelViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(
            areaModel: Area, listener: RecyclerItemMultiSelectionListener<Area>,
            selectedAreaModel: MutableList<Area>?,
        ) {
            tv_name.text = areaModel.name
//            if (areaModel.isSelected)
//                selectedBg()
//            else
//                unSelectedBg()
//
//            main_layout.setOnClickListener {
//                if (areaModel.isSelected)
//                    listener.onItemDeSelected(areaModel)
//                else listener.onItemMultiSelected(areaModel)
//            }
//
            if(!selectedAreaModel.isNullOrEmpty()){
                for (item in selectedAreaModel){
                    if(areaModel.id == item.id && item.isSelected == true ){
                        selectedBg()
                        tv_name.makeFontBold()
                        areaModel.isSelected=true
                    }
                }
            }

            main_layout.setOnClickListener {
                if(areaModel.isSelected){
                    listener.onItemDeSelected(areaModel)
                    areaModel.isSelected = false
                    unSelectedBg()
                }else{
                    areaModel.isSelected = true
                    listener.onItemMultiSelected(areaModel)
                    selectedBg()
                }
            }
        }

        private fun unSelectedBg() {
            iv_selected.setImageResource(R.drawable.ic_circle_orange_border)
            tv_name.makeFontNormal()
        }

        private fun selectedBg() {
            iv_selected.setImageResource(R.drawable.ic_tick_orange)
            tv_name.makeFontBold()
        }
    }

    override fun getFilter(): Filter {
        return filter;
    }

    inner class areaModelFilter(private val adapter: CarAreasAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if (::mAreasList.isInitialized)
                    mFilteredList?.addAll(mAreasList)
            } else {

                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                mFilteredList?.clear()
                for (row in mAreasList) {
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

}