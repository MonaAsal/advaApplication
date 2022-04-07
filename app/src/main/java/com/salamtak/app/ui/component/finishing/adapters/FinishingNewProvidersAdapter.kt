package com.salamtak.app.ui.component.finishing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingProvidersModel
import com.salamtak.app.ui.component.finishing.interfaces.FinishingCategoryProviderListener
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_finishing_provider.*
import kotlinx.android.synthetic.main.item_finishing_subcategory.*
import kotlinx.android.synthetic.main.item_finishing_subcategory.tv_price
import kotlinx.android.synthetic.main.item_new_operation_vertical.iv_operation_image
import kotlinx.android.synthetic.main.item_new_operation_vertical.rb_provider
import kotlinx.android.synthetic.main.item_new_operation_vertical.tv_provider_name

class FinishingNewProvidersAdapter(val listener : FinishingCategoryProviderListener<FinishingProvidersModel>)
    : RecyclerView.Adapter<FinishingNewProvidersAdapter.ViewHolder>() {
    var providersList: MutableList<FinishingProvidersModel>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_finishing_subcategory, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<FinishingProvidersModel>) {
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

        fun bind(
        subCategory: FinishingProvidersModel,
        listener: FinishingCategoryProviderListener<FinishingProvidersModel>
        ) {

            //load image ......
            subCategory.imageUrl?.let {
                iv_operation_image.loadImage(subCategory.imageUrl) //provider image
            }
            //load provider data .....
            tv_provider_name.text = subCategory.name //doctor name
            rb_provider.text = subCategory.rating.toString()
           // tv_price.text = subCategory.pricePerMeter.toString()
            subCategory.pricePerMeter?.let {
                tv_price.text = it.toString() +" "+ subCategory.pricingType
            }
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