//package com.salamtak.app.ui.component.operations.adapter
//
//import android.database.DataSetObserver
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Filter
//import android.widget.Filterable
//import android.widget.SpinnerAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.Governorate
//import com.salamtak.app.data.entities.Operation
//import com.salamtak.app.ui.base.listeners.RecyclerItemListener
//import com.salamtak.app.ui.component.operations.list.OperationsViewModel
//import java.util.ArrayList
//
///**
// * Created by RadwaElsahn on 3/29/2020.
// */
//
//class GovernoratesAdapter(
//    private val operationsViewModel: OperationsViewModel,
//    private val governorate: MutableList<Governorate>
//) : SpinnerAdapter {
//
//    private val onItemClickListener: RecyclerItemListener<Governorate> =
//        object : RecyclerItemListener<Governorate> {
//
//            override fun onItemSelected(governorate: Governorate) {
//                operationsViewModel.governorateSelected(governorate)
//            }
//
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_spinner, parent, false)
//        return SpinnerViewHolder(
//            view
//        )
//    }
//
//    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
//
//        holder.bind(mFilteredList!![position], onItemClickListener)
//    }
//
//    override fun getItemCount(): Int {
//        return if (mFilteredList != null) mFilteredList!!.size else 0
//    }
//
//    override fun isEmpty(): Boolean {
//        return governorate.isEmpty()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun registerDataSetObserver(observer: DataSetObserver?) {
//
//    }
//
//    override fun getItemViewType(position: Int): Int {
//
//    }
//
//    override fun getItem(position: Int): Any {
//        return governorate[position]
//    }
//
//    override fun getViewTypeCount(): Int {
//        return governorate.size
//    }
//
//    override fun getItemId(position: Int): Long {
//        return governorate[position].id.toLong()
//    }
//
//    override fun hasStableIds(): Boolean {
//
//    }
//
//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
//
//    }
//
//    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
//
//    }
//
//    override fun getCount(): Int {
//        return governorate.size
//    }
//
//}
//
