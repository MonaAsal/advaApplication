package com.salamtak.app.ui.component.health.bookoperation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.utils.changeTextColor
import com.salamtak.app.utils.color
import com.salamtak.app.utils.toDecimalNumberFormat
import com.salamtak.app.utils.toGone
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_installment_type.*
import java.util.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class InstallmentTypesAdapter(
    val listener: RecyclerItemPositionListener<InstallmentType>,
    val locale: Locale
) : RecyclerView.Adapter<InstallmentTypeViewHolder>() {

    lateinit var types: MutableList<InstallmentType>
    var selectedIndex = 0

    fun setSelectedItem(position: Int, type: InstallmentType) {
        selectedIndex = position
        notifyDataSetChanged()
    }


    fun setList(types: MutableList<InstallmentType>) {
        this.types = types
        notifyDataSetChanged()
//        listener.onItemSelected(types[0], 0)
    }

    fun resetList(types: MutableList<InstallmentType>) {
        this.types = types
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstallmentTypeViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_installment_type, parent, false)
        return InstallmentTypeViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: InstallmentTypeViewHolder, position: Int) {

        holder.bind(types!![position], listener, selectedIndex, locale)
    }

    override fun getItemCount(): Int {
        return if (types != null) types!!.size else 0
    }


}

class InstallmentTypeViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        type: InstallmentType,
        recyclerItemListener: RecyclerItemPositionListener<InstallmentType>,
        selectedIndex: Int, locale: Locale
    ) {

        if (adapterPosition == selectedIndex) {
            formatSelectedItem()

        } else {
            formatNonSelectedItem()
        }

        tv_number.text = type.numberOfMonths.toString()
        type.monthlyAmount?.let {
            tv_months.text =
                tv_months.context.resources.getQuantityString(
                    R.plurals.months1,
                    type.numberOfMonths,
                    type.numberOfMonths
                )
            tv_monthly_amount.text =

                String.format(
                    locale,
                    containerView.context.getString(R.string.egp_amount),
                    toDecimalNumberFormat(
                        it
                        //bookOperationViewModel.getInstallmentPerMonth(type).toDouble()
                    )
                )
        } ?: run {
            tv_monthly_amount.toGone()
        }

        main_layout.setOnClickListener {
            recyclerItemListener.onItemSelected(type, adapterPosition)
        }
    }


    private fun formatSelectedItem() {
        //iv_check.visibility = View.VISIBLE
        //main_layout.setCardBackgroundColor(
        main_layout.setBackgroundResource(R.drawable.ic_selected)
        changeTextColor(tv_monthly_amount, android.R.color.white)
        changeTextColor(tv_months, android.R.color.white)
        changeTextColor(tv_number, android.R.color.white)
    }

    private fun formatNonSelectedItem() {
        changeTextColor(tv_monthly_amount, R.color.textColorLight)
        changeTextColor(tv_months, R.color.colorPrimary)
        changeTextColor(tv_number, R.color.colorPrimary)
       // iv_check.visibility = View.GONE
        main_layout.setBackgroundResource(R.drawable.ic_nonselected)
        //main_layout.setBackgroundColor(itemView.context.color(android.R.color.white))//setCardBackgroundColor(
    }
}


