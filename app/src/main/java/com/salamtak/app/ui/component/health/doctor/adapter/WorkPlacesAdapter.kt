package com.salamtak.app.ui.component.health.doctor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.WorkPlace
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.doctor.DoctorViewModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_workplace.*

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class WorkPlacesAdapter(doctorViewModel: DoctorViewModel) :
    RecyclerView.Adapter<WorkPlacesViewHolder>() {

    lateinit var workPlaces: MutableList<WorkPlace>

    fun setList(workPlaces: MutableList<WorkPlace>) {
        this.workPlaces = workPlaces
    }

    private val onItemClickListener: RecyclerItemListener<WorkPlace> =
        object : RecyclerItemListener<WorkPlace> {

            override fun onItemSelected(workPlace: WorkPlace) {
                doctorViewModel.onMedicalProviderClicked(workPlace.medicalProviderBranch?.medicalProvider?.id!!.toString())
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkPlacesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_workplace, parent, false)
        return WorkPlacesViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: WorkPlacesViewHolder, position: Int) {

        holder.bind(workPlaces!![position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (workPlaces != null) workPlaces!!.size else 0
    }


}
class WorkPlacesViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        workplace: WorkPlace, onItemClickListener: RecyclerItemListener<WorkPlace>
    ) {
        tv_workPlace_name.text = workplace.medicalProviderBranch?.medicalProvider?.name
        rb_workPlace.rating = workplace.medicalProviderBranch?.rate?.toFloat()!!
        tv_branches.text = workplace.medicalProviderBranch?.name
//
        iv_workPlace_image.loadRoundedImage(
            workplace.medicalProviderBranch?.imageUrl,
            Constants.IMAGE_CORNER, R.drawable.ic_hospital_avatar
        )
        containerView.setOnClickListener {
            onItemClickListener.onItemSelected(workplace)
        }
    }


}
