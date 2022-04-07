package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Doctor
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.OperationsViewModelN

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class DoctorsAdapter(operationsViewModel: OperationsViewModelN) : RecyclerView.Adapter<DoctorViewHolder>() {

    lateinit var doctors: MutableList<Doctor>

    fun setList(list: MutableList<Doctor>) {
        this.doctors = list
    }

    private val onItemClickListener: RecyclerItemListener<Doctor> =
        object : RecyclerItemListener<Doctor> {

            override fun onItemSelected(doctor:Doctor) {
                operationsViewModel.onDoctorClicked(doctor!!)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {

        holder.bind(doctors!![position],onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (doctors != null) doctors!!.size else 0
    }


}

