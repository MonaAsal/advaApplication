package com.salamtak.app.ui.component.health.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.data.entities.Governorate
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_spinner.*

/**
 * Created by RadwaElsahn on 3/29/2020.
 */

class SpinnerViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(governorate: Governorate, recyclerItemListener: RecyclerItemListener<Governorate>) {
        tv_spinner_item.text = governorate.name
        tv_spinner_item.setOnClickListener { recyclerItemListener.onItemSelected(governorate) }
    }
}

