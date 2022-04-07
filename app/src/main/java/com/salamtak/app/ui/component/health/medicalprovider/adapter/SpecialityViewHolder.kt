package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.data.entities.Specialization
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_medical_speciality.*

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class SpecialityViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        specialization: Specialization
    ) {
        tv_speciality.text = specialization.specialization?.name
    }


}

