package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Doctor
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.loadCircleImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_doctor.*

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class DoctorViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        doctor: Doctor,
        onItemClickListener: RecyclerItemListener<Doctor>
    ) {
        iv_doctor_image.loadCircleImage(doctor.imageUrl, R.drawable.ic_doctor_avatar)
        tv_doctor_name.text = doctor.name
        tv_title.text = doctor.currentTitle
        rb_doctor.rating = doctor.rate.toFloat()
//        tv_medical_provider_address.text = medicalProvider.about
        containerView.setOnClickListener {
            onItemClickListener.onItemSelected(doctor)
        }
    }


}

