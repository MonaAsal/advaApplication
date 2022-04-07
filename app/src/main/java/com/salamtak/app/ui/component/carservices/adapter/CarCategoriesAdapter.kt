package com.salamtak.app.ui.component.carservices.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarCategoryModel
import com.salamtak.app.data.entities.CarProvidersModel
import com.salamtak.app.ui.component.carservices.interfaces.CarCategoryProviderListener
import com.salamtak.app.ui.component.carservices.interfaces.CarViewAllListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car_category.*

class CarCategoriesAdapter(
    val listener: CarViewAllListener<CarCategoryModel>,
    val providerListener: CarCategoryProviderListener<CarProvidersModel>, var CarCategoriesList: MutableList<CarCategoryModel>
):
    RecyclerView.Adapter<CarCategoriesAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_car_category, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<CarCategoryModel>) {
        this.CarCategoriesList = list
    }

    fun addData(listItems: List<CarCategoryModel>) {
        CarCategoriesList?.let {
            var size = CarCategoriesList?.size
            CarCategoriesList?.addAll(listItems)
            notifyItemRangeInserted(size, CarCategoriesList?.size)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(CarCategoriesList[position],listener,providerListener)
    }

    override fun getItemCount(): Int {
        return if (CarCategoriesList != null) CarCategoriesList.size else 0
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        lateinit  var  mLayoutManager : LinearLayoutManager
        var mLastPosition= 0

        fun bind(
            carCategoryModel: CarCategoryModel,
            listener: CarViewAllListener<CarCategoryModel>,
            providerListener : CarCategoryProviderListener<CarProvidersModel>
        ) {

            tv_viewAll2.setOnClickListener { listener.onClick(carCategoryModel) }
            tv_title.text = carCategoryModel.name

            carCategoryModel.carServiceProviders?.let {
                setupProvidersList(it,providerListener)
            }

        }

        private fun setupProvidersList(
            providersList: ArrayList<CarProvidersModel>,
            providerListener: CarCategoryProviderListener<CarProvidersModel>
        ) {
            var providerAdapter = CarNewProvidersAdapter(providerListener)
            providerAdapter.setList(providersList.toMutableList()) //add operations list
            mLayoutManager = LinearLayoutManager(containerView.context, LinearLayoutManager.HORIZONTAL, false)
            rv_finishing_sub_category.adapter = providerAdapter
            rv_finishing_sub_category.layoutManager = mLayoutManager
            rv_finishing_sub_category.post {
                providerAdapter.notifyDataSetChanged()             //solving crash issue when fast scroll

            }
        }



    }

}