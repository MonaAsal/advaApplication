package com.salamtak.app.ui.component.medicalnetwork.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalNetwork
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.loadCircleImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_doctor_grid.*
import java.util.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class MedicalNetworkAdapter(
    val listener: RecyclerItemListener<MedicalNetwork>, val grid: Boolean = false
) : RecyclerView.Adapter<MedicalNetworkViewHolder>(),
    Filterable {

    var mFilteredList: MutableList<MedicalNetwork>? = null
    lateinit var doctors: MutableList<MedicalNetwork>

    var filter = DoctorsFilter(this@MedicalNetworkAdapter)
//    private val onItemClickListener: RecyclerItemListener<MedicalNetwork> =
//        object : RecyclerItemListener<MedicalNetwork> {
//
//            override fun onItemSelected(doctor: MedicalNetwork) {
//                doctorsViewModel.openDoctorOperations(doctor)
//            }
//
//        }

    fun setList(doctors: MutableList<MedicalNetwork>) {
        this.doctors = doctors
        mFilteredList = ArrayList()
        mFilteredList!!.addAll(doctors)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalNetworkViewHolder {
        if(grid.not()) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_doctor_grid, parent, false)
            return MedicalNetworkViewHolder(
                view
            )
        }
        else
        {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_medical_network, parent, false)
            return MedicalNetworkViewHolder(
                view
            )

        }
    }

    override fun onBindViewHolder(holder: MedicalNetworkViewHolder, position: Int) {

        holder.bind(mFilteredList!![position], listener)
    }

    override fun getItemCount(): Int {
//        return doctors.size
        return if (mFilteredList != null) mFilteredList!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class DoctorsFilter(private val adapter: MedicalNetworkAdapter) : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = FilterResults()
            if (charSequence.isEmpty()) {
                mFilteredList?.clear()
                if (::doctors.isInitialized)
                    mFilteredList?.addAll(doctors)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                mFilteredList?.clear()
                for (row in doctors) {
                    if ((row.name.toLowerCase().contains(filterPattern))
                    ) {
                        // Timber.d("filtered" + row.slug.toLowerCase())
                        mFilteredList?.add(row)
                    }
                }
            }

            Log.d("Seach Count", "" + mFilteredList?.size.toString())
            results.values = mFilteredList
            results.count = mFilteredList?.size ?: 0
            return results
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            adapter.notifyDataSetChanged()
        }

    }

    init {
        mFilteredList = ArrayList()

        filter = DoctorsFilter(this@MedicalNetworkAdapter)
    }
}

class MedicalNetworkViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(medicalProvider: MedicalNetwork, listener: RecyclerItemListener<MedicalNetwork>) {
        tv_doctor_name.text = medicalProvider.name
        if (medicalProvider.specialization != null)
            tv_doctor_speciality.text = medicalProvider.specialization!!.name
        else
            tv_doctor_speciality.visibility = View.GONE

        if (!medicalProvider.imageUrl.isNullOrEmpty())
            iv_doctor_image.loadCircleImage(medicalProvider.imageUrl, R.drawable.ic_hospital_avatar)
        else
            iv_doctor_image.loadCircleImage(medicalProvider.logo, R.drawable.ic_hospital_avatar)
//        if (!category.imageUrl.isNullOrEmpty()) {
//            val url: String? = BuildConfig.BASE_IMAGE_URL + category.imageUrl
//
//            Glide.with(containerView.context)
//                .load(url)
//                .placeholder(R.drawable.ic_circle)
//                .into(iv_category_image!!)
//        }
        itemView.setOnClickListener { listener.onItemSelected(medicalProvider) }
    }
}

