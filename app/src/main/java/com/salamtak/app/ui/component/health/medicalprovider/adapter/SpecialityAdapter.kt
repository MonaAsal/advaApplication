package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Specialization

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class SpecialityAdapter : RecyclerView.Adapter<SpecialityViewHolder>() {

    lateinit var specializations: MutableList<Specialization>

    fun setList(list: MutableList<Specialization>) {
        this.specializations = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialityViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_medical_speciality, parent, false)
        return SpecialityViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SpecialityViewHolder, position: Int) {

        holder.bind(specializations!![position])
    }

    override fun getItemCount(): Int {
        return if (specializations != null) specializations!!.size else 0
    }


}

