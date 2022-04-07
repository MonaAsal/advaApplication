package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_medical_provider.*


/**
 * Created by RadwaElsahn on 4/26/2021.
 */

class MedicalProvidersAdapter(val listener: RecyclerItemPositionListener<MedicalProviderDetails>) :
    RecyclerView.Adapter<MedicalProvidersViewHolder>() {

    lateinit var medicalProviders: MutableList<MedicalProviderDetails>

    fun setList(list: MutableList<MedicalProviderDetails>) {
        this.medicalProviders = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalProvidersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_medical_provider, parent, false)
        return MedicalProvidersViewHolder(
            view
        )
    }

    fun addData(listItems: List<MedicalProviderDetails>) {
        var size = medicalProviders?.size!!
        medicalProviders?.addAll(listItems)
        notifyItemRangeInserted(size, medicalProviders?.size!!)
    }

    override fun onBindViewHolder(holder: MedicalProvidersViewHolder, position: Int) {

        holder.bind(medicalProviders!![position])
    }

    override fun getItemCount(): Int {
        return if (medicalProviders != null) medicalProviders!!.size else 0
    }
}

class MedicalProvidersViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        medicalProvider: MedicalProviderDetails
    ) {
        iv_medical_provider_image.loadImage(
            medicalProvider.image
        )
        tv_medical_provider_name.text = medicalProvider.name
//        tv_medical_provider_address.text = medicalProvider.about
    }


}


