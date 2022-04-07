package com.salamtak.app.ui.component.paymentmethods.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.PaymentMethodCard
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.utils.Constants
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_payment_card.*


/**
 * Created by RadwaElsahn on 5/13/2020.
 */

class CardViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        card: PaymentMethodCard,
        position: Int,
        onDefaultClickListener: RecyclerItemPositionListener<PaymentMethodCard>
    ) {
        tv_card_number.text = card.maskedPan
        iv_icon.setImageResource(if (card.subtype.toLowerCase() == Constants.TYPE_VISA) R.drawable.ic_visa else R.drawable.ic_mastercard)
        if (card.isDefault) {
            cb_defult.isChecked = true
            tv_default.visibility = View.VISIBLE
        } else {
            cb_defult.isChecked = false
            tv_default.visibility = View.GONE
        }

        cb_defult.setOnCheckedChangeListener { view, checked ->
            if(checked)
            onDefaultClickListener.onItemSelected(card, position)
        }

    }


}

