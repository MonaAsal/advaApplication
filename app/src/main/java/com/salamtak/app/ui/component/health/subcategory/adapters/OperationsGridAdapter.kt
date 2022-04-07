package com.salamtak.app.ui.component.health.subcategory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.subcategory.listeners.OperationListener
import com.salamtak.app.utils.changeIconFavouriteToIsFavourite
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_new_operation_vertical.*


class OperationsGridAdapter(val listener: OperationListener<Operation>,
                            val Favlistener: OperationListenerN)
    : RecyclerView.Adapter<OperationsGridAdapter.ViewHolder>() {
    var subSubCategoriesList: MutableList<Operation>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_new_operation_vertical, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<Operation>) {
        subSubCategoriesList = list
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(subSubCategoriesList!![position],listener,Favlistener)

    }

    override fun getItemCount(): Int {
        return if (subSubCategoriesList != null) subSubCategoriesList!!.size else 0
    }


    class ViewHolder(
        override val containerView: View,
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(
            subSubCategory: Operation,
            listener: OperationListener<Operation>,
            Favlistener: OperationListenerN,
        ) {

            //load image ......
            subSubCategory.imageUrl?.let {
                iv_operation_image.loadImage(subSubCategory.imageUrl) //operation image
            }
            //load provider data .....
            tv_provider_name.text =subSubCategory.name  //doctor name
            tv_subsubcategory_name.text = subSubCategory.providerName //operation
            rb_provider.text = subSubCategory.rate.toString()
            tv_op_price.text = subSubCategory.startFrom.toString()
          //  tv_op_currency.text = " / "+ subSubCategory.maxMonth.toString() + containerView.context.getString(R.string.mmonths)

            //favourite.....
            changeIconFavouriteToIsFavourite(iv_operation_favorite, subSubCategory.isFavourite)

            //on favourite click.....
            iv_operation_favorite.setOnClickListener {
                changeIconFavouriteToIsFavourite(
                    iv_operation_favorite,
                    (subSubCategory.isFavourite).not()
                )
                Favlistener.onFavouriteClicked(subSubCategory, adapterPosition)
                subSubCategory.isFavourite = subSubCategory.isFavourite.not()
            }

            //on card click...
            data_cardView.setOnClickListener {
                listener.onOperationClicked(subSubCategory)

            }
            image_card.setOnClickListener {
                listener.onOperationClicked(subSubCategory)
            }

         /*   tv_provider_name.setOnClickListener {
                listener.onOwnerClicked(subSubCategory)
            }*/

        }

    }

}