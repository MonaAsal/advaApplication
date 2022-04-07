package com.salamtak.app.ui.component.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.App.Companion.context
import com.salamtak.app.R
import com.salamtak.app.data.entities.HomeCategory
import com.salamtak.app.data.entities.responses.CartItem
import com.salamtak.app.ui.common.listeners.CartItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.common.listeners.ServicesCategoriesItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart.*
import kotlinx.android.synthetic.main.item_featured_categories.*


class CartAdapter(
    private val listener: CartItemListener<CartItem>, private var items: List<CartItem>,var context:Context
) : RecyclerView.Adapter<CartAdapter.CategoryViewHolder>() {
    private val onItemRemovedListener: CartItemListener<CartItem> =
        object : CartItemListener<CartItem> {
            override fun onCartItemRemoved(item: CartItem, Position: Int) {
               // notifyItemRemoved(Position)
              //  val list = ArrayList(items)
             //   list.removeAt(0)
                listener.onCartItemRemoved(item,Position)
            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CategoryViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    holder.bind(items[position], onItemRemovedListener,position)
    }

    override fun getItemCount(): Int {
//        return categories.size
        return if (items != null) items!!.size else 0
    }

    class CategoryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartItem, recyclerItemListener: CartItemListener<CartItem>, position: Int) {
            tv_service_name.text = item.title
            tv_provider_name.text = item.subTitle
            tv_monthly_instalment.text=item.monthlyInstallment +" "+context.getString(R.string.egp_per)+item.installmentMonths+" "+context.getString(R.string.month)
            iv_item_image.loadRoundedImage(  item?.imageUrl, Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )
            iv_Delete.setOnClickListener { recyclerItemListener.onCartItemRemoved(item,position) }
        }
    }
}

