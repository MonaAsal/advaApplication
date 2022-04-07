package com.salamtak.app.ui.component.paymentmethods.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.PaymentMethodCard
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.paymentmethods.PaymentMethodsViewModel

/**
 * Created by RadwaElsahn on 5/13/2020.
 */

class CardsAdapter(val viewModel: PaymentMethodsViewModel) : RecyclerView.Adapter<CardViewHolder>() {

//    lateinit var cards: MutableList<PaymentMethodCard>

//    fun setList(workPlaces: MutableList<PaymentMethodCard>) {
//        this.cards = workPlaces
//    }

    private val onDefaultClickListener: RecyclerItemPositionListener<PaymentMethodCard> =
        object : RecyclerItemPositionListener<PaymentMethodCard> {
            override fun onItemSelected(item: PaymentMethodCard, position: Int) {
                viewModel.toggleDefaultCard(position)

                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_payment_card, parent, false)
        return CardViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        holder.bind(viewModel.getPaymentMethodsList()!![position], position, onDefaultClickListener)
    }

    override fun getItemCount(): Int {
        return if (viewModel.getPaymentMethodsList() != null) viewModel.getPaymentMethodsList()!!.size else 0
    }


}

