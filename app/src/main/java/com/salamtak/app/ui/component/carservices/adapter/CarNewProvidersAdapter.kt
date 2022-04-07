package com.salamtak.app.ui.component.carservices.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.App.Companion.context
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarProvidersModel
import com.salamtak.app.ui.component.carservices.interfaces.CarCategoryProviderListener
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car.*
import kotlinx.android.synthetic.main.item_new_operation_vertical.iv_operation_image
import kotlinx.android.synthetic.main.item_new_operation_vertical.rb_provider
import kotlinx.android.synthetic.main.item_new_operation_vertical.tv_provider_name

class CarNewProvidersAdapter(val listener : CarCategoryProviderListener<CarProvidersModel>)
    : RecyclerView.Adapter<CarNewProvidersAdapter.ViewHolder>() {
    var providersList: MutableList<CarProvidersModel>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<CarProvidersModel>) {
        providersList = list
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        providersList?.get(position)?.let {holder.bind(it,listener) }

    }

    override fun getItemCount(): Int {
        return providersList?.size ?: 0
    }

    class ViewHolder(
        override val containerView: View,
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bind(
        subCategory: CarProvidersModel,
        listener: CarCategoryProviderListener<CarProvidersModel>
        ) {

            //load image ......
            subCategory.logoUrl?.let {
                iv_logo.loadImage(subCategory.logoUrl) //provider image
            }
            //load provider data .....
            tv_name.text = subCategory.name //doctor name
            rb_providers.text = subCategory.rating.toString()
            tv_branches.text = subCategory.branches.toString()+" "+context.getString(R.string.branch)
            //  tv_op_currency.text = " / "+ subSubCategory.maxMonth.toString() + containerView.context.getString(R.string.mmonths)

            //on card click...
            data_cardView.setOnClickListener {
                listener.onProviderClick(subCategory)

            }
            image_card.setOnClickListener {
                listener.onProviderClick(subCategory)

            }
        }

    }

}