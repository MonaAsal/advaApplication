package com.salamtak.app.ui.component.medicalhistory.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.ChronicDisease
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_disease.*

/**
 * Created by RadwaElsahn on 4/7/2020.
 */

class DiseaseViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        disease: ChronicDisease,
        recyclerItemListener: RecyclerItemPositionListener<ChronicDisease>,
        position: Int
    ) {

        if (disease.hasThisChronic!!) {
            iv_selected.setBackgroundResource(R.drawable.ic_tick_primary_circle)
        } else {
            iv_selected.setBackgroundResource(R.drawable.ic_circle_grey_border)
        }

        tv_disease.text = disease.name

        tv_disease.setOnClickListener {
            recyclerItemListener.onItemSelected(disease, position)
        }

        iv_selected.setOnClickListener { recyclerItemListener.onItemSelected(disease, position) }
    }


}

