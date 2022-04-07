package com.salamtak.app.ui.component.carservices.providers.carlocation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.City
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.makeFontBold
import com.salamtak.app.utils.makeFontNormal
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_brand.tv_name
import kotlinx.android.synthetic.main.item_car_city.*

class CarCitiesAdapter (private val listener: RecyclerItemListener<City>) :
    RecyclerView.Adapter<CarCitiesAdapter.CarModelViewHolder>() {
    var mCitiesList: MutableList<City>? = null
    var selectedCityModel: City? = null

    private val onItemClickListener: RecyclerItemListener<City> = object : RecyclerItemListener<City> {

        override fun onItemSelected(cityModel: City) {
            selectedCityModel = cityModel
            setSelected(cityModel)
                listener.onItemSelected(cityModel)
                notifyDataSetChanged()


        }
    }

    fun setSelected(cityModel: City?) {
        cityModel?.let {
            mCitiesList!!.map { it.isSelected = false }
            mCitiesList!!.filter { it.id == cityModel.id }[0].isSelected = false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car_city, parent, false)
        return CarModelViewHolder(view)
    }

    fun setList(list: MutableList<City>, cityModel: City?) {
        this.mCitiesList = list
        selectedCityModel = cityModel
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CarModelViewHolder, position: Int) {
        holder.bind(mCitiesList!![position] , onItemClickListener, selectedCityModel )
    }

    override fun getItemCount(): Int {
       return if (mCitiesList != null) mCitiesList!!.size else 0
    }

    fun clearSelection() {
        selectedCityModel = null
        mCitiesList!!.map {it.isSelected = false }
        notifyDataSetChanged()
    }

    class CarModelViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(carCity: City, listener: RecyclerItemListener<City>, selectedCityModel: City?) {

            tv_name.text = carCity.name
            if(carCity.id == selectedCityModel?.id){
                selectedBg(tv_name)
                tv_name.makeFontBold()
            }else{
                defaultBg(tv_name)
               tv_name.makeFontNormal()
            }

            main_city_layout.setOnClickListener {
                selectedBg(tv_name)
                listener.onItemSelected(carCity)
            }

        }

        fun defaultBg(tv_name: TextView) {
            tv_name.setBackgroundResource(R.drawable.bg_curved_unselected_city)
        }

        fun selectedBg(tv_name: TextView) {
            tv_name.setBackgroundResource(R.drawable.bg_curved_selected_city)
        }

    }


}