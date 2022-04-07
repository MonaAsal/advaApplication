package com.salamtak.app.ui.component.carservices.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.City
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.carservices.providers.carlocation.CarCitiesAdapter
import com.salamtak.app.utils.makeFontBold
import com.salamtak.app.utils.makeFontNormal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_brand.*
import kotlinx.android.synthetic.main.item_car_city.*
import kotlinx.android.synthetic.main.item_car_phone.*
import kotlinx.android.synthetic.main.item_car_phone.main_layout


class CarPhoneAdapter (private val listener: RecyclerItemListener<String>)
    : RecyclerView.Adapter<CarPhoneAdapter.CarViewHolder>(){
    var mPhoneList: MutableList<String>? = null

    fun setList(list: MutableList<String>) {
        this.mPhoneList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_phone, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(mPhoneList!![position] , onItemClickListener )
    }

    override fun getItemCount(): Int {
        return if (mPhoneList != null) mPhoneList!!.size else 0
    }

    class CarViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(phoneNumber: String, listener: RecyclerItemListener<String>) {

            tv_num.text = phoneNumber
            main_layout.setOnClickListener {
                listener.onItemSelected(phoneNumber)
            }

        }

    }

    private val onItemClickListener: RecyclerItemListener<String> = object : RecyclerItemListener<String> {

        override fun onItemSelected(phone: String) {
            listener.onItemSelected(phone)
            notifyDataSetChanged()
        }
    }


}