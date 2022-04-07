package com.salamtak.app.ui.component.finishing.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingPackage
import com.salamtak.app.data.enums.PricingType
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.utils.changeTextColor
import com.salamtak.app.utils.toDecimalNumberFormat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_finishing_package.*
import java.util.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class FinishingPackagesAdapter(
    val packages: MutableList<FinishingPackage>,
    val listener: FinishingDetailsListener,
    val locale: Locale
) : RecyclerView.Adapter<PackagesViewHolder>() {

    var selectedIndex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finishing_package, parent, false)

        val display = parent.context.resources.displayMetrics
        val width = display.widthPixels * 2 / 3
        val params = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = params

        return PackagesViewHolder(
            view
        )
    }

    fun selectPackage(index: Int) {
        selectedIndex = index
        notifyDataSetChanged()
    }

    private val onItemClickListener: RecyclerItemPositionListener<FinishingPackage> =
        object : RecyclerItemPositionListener<FinishingPackage> {
            override fun onItemSelected(type: FinishingPackage, position: Int) {
                selectedIndex = position
                notifyDataSetChanged()
            }
        }

    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) {
        holder.bind(packages!![position], listener, selectedIndex, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (packages != null) packages!!.size else 0
    }

}

class PackagesViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        item: FinishingPackage,
        recyclerItemListener: FinishingDetailsListener,
        selectedIndex: Int,
        listener: RecyclerItemPositionListener<FinishingPackage>
    ) {

        tv_package_name.text = item.name
        if (item.pricingType!!.equals(PricingType.PerMeter.id))
            tv_price.text = String.format(
                itemView.context.getString(R.string.num_egp_m2),
                toDecimalNumberFormat(
                    item.pricePerMeter.toDouble()
                )
            )
        else
            tv_price.text = String.format(
                itemView.context.getString(R.string.egp_amount),
                toDecimalNumberFormat(
                    item.pricePerMeter.toDouble()
                )
            )
        tv_desc.text = item.subtitle


        if (adapterPosition == selectedIndex) {
            formatSelectedItem()

        } else {
            formatNonSelectedItem()
        }

        main_layout.setOnClickListener {
            recyclerItemListener.onFinishingPackageSelected(item)
            listener.onItemSelected(item, adapterPosition)
        }

        iv_more.setOnClickListener {
//            formatSelectedItem()
            recyclerItemListener.openPackageDetails(item)
        }


    }


    private fun formatSelectedItem() {
        main_layout.setCardBackgroundColor(
            ContextCompat.getColor(
                containerView.context,
                R.color.orange
            )
        )

        changeTextColor(tv_package_name, android.R.color.white)
        changeTextColor(tv_price, android.R.color.white)
        changeTextColor(tv_desc, android.R.color.white)

        iv_more.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_white_show_details_bg)
//        DrawableCompat.setTint(
//            iv_more.drawable,
//            ContextCompat.getColor(itemView.context, R.color.white)
//        )

    }

    private fun formatNonSelectedItem() {
        changeTextColor(tv_desc, R.color.colorPrimary)
        changeTextColor(tv_price, R.color.orange)
        changeTextColor(tv_package_name, R.color.orange)

        iv_more.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_orange_show_details_bg)
//        DrawableCompat.setTint(
//            iv_more.drawable,
//            ContextCompat.getColor(itemView.context, R.color.orange)
//        )

        main_layout.setCardBackgroundColor(
            ContextCompat.getColor(
                containerView.context,
                android.R.color.white
            )
        )
    }


}


