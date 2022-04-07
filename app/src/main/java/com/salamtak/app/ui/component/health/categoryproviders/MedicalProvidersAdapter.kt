package com.salamtak.app.ui.component.health.categoryproviders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_provider.*
import kotlinx.coroutines.withContext
import org.jetbrains.anko.internals.AnkoInternals.getContext
import java.math.RoundingMode

/**
 * Created by RadwaElsahn on 4/26/2021.
 */

class MedicalProvidersAdapter(val listener: RecyclerItemListener<MedicalProvider>) :
    RecyclerView.Adapter<MedicalProvidersViewHolder>() {

    lateinit var medicalProviders: MutableList<MedicalProvider>

    fun setList(list: MutableList<MedicalProvider>) {
        this.medicalProviders = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalProvidersViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_provider, parent, false)
        return MedicalProvidersViewHolder(
            view
        )
    }

    fun addData(listItems: List<MedicalProvider>) {
        var size = medicalProviders?.size!!
        medicalProviders?.addAll(listItems)
        notifyItemRangeInserted(size, medicalProviders?.size!!)
    }

    override fun onBindViewHolder(holder: MedicalProvidersViewHolder, position: Int) {

        holder.bind(medicalProviders!![position], listener)
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
        medicalProvider: MedicalProvider, listener: RecyclerItemListener<MedicalProvider>
    ) {
        if (medicalProvider.type == ProviderType.Doctor.typeId) {
            iv_provider_image.loadImage(
                medicalProvider.image
            )
        } else
            iv_provider_image.loadImage(
                medicalProvider.image
            )

        tv_provider_name.text = medicalProvider.name
        tv_category_name.text = medicalProvider.providerType
        rb_provider.text = medicalProvider.rate.toString()
        tv_price.text = medicalProvider.startFrom!!.toBigDecimal().setScale(0, RoundingMode.UP)
                .toInt().toString()
       // tv_currency.text = " / "+ medicalProvider.maxMonth.toString() + containerView.context.getString(R.string.mmonths)

        itemView.setOnClickListener { listener.onItemSelected(medicalProvider) }


    }


}


