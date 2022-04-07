package com.salamtak.app.ui.component.carservices.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarProviderDetails.Location
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_branch.*
import kotlinx.android.synthetic.main.item_car_city.*
import kotlinx.android.synthetic.main.item_car_phone.*
import kotlinx.android.synthetic.main.item_car_phone.tv_name
import kotlinx.android.synthetic.main.item_car_phone.main_layout



class CarBranchesAdapter (private val listener: RecyclerItemListener<Location>)
    : RecyclerView.Adapter<CarBranchesAdapter.CarViewHolder>(){
    var mBranchesList: MutableList<Location>? = null

    fun setList(list: MutableList<Location>) {
        this.mBranchesList = list
    }

    private val onItemClickListener: RecyclerItemListener<Location> = object : RecyclerItemListener<Location> {

        override fun onItemSelected(location: Location) {
            listener.onItemSelected(location)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_branch, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(mBranchesList!![position] , onItemClickListener )
    }

    override fun getItemCount(): Int {
        return if (mBranchesList != null) mBranchesList!!.size else 0
    }

    class CarViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(location: Location, listener: RecyclerItemListener<Location>) {

            tv_city_name.text = location.areaName
            tv_name.text = location.address
            main_layout.setOnClickListener {
                listener.onItemSelected(location)
            }

        }

    }
}