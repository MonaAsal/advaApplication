package com.salamtak.app.ui.component.medicalhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.ChronicDisease
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.financialinfo.bank.MedicalProfileInfoViewModel

/**
 * Created by RadwaElsahn on 4/7/2020.
 */

class DiseasesAdapter(
    private val medicalProfileInfoViewModel: MedicalProfileInfoViewModel
) : RecyclerView.Adapter<DiseaseViewHolder>() {

    private val onItemClickListener: RecyclerItemPositionListener<ChronicDisease> =
        object : RecyclerItemPositionListener<ChronicDisease> {

            override fun onItemSelected(disease: ChronicDisease, position: Int) {
                medicalProfileInfoViewModel.toggleSelectDisease(position)

                notifyDataSetChanged()
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiseaseViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_disease, parent, false)
        return DiseaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {

        holder.bind(medicalProfileInfoViewModel.diseases!![position], onItemClickListener, position)
    }

    override fun getItemCount(): Int {
        return if (medicalProfileInfoViewModel.diseases != null) medicalProfileInfoViewModel.diseases!!.size else 0
    }


}

