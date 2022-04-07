package com.salamtak.app.ui.component.health.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.DoctorDetails


import com.salamtak.app.ui.component.health.doctor.adapter.WorkPlacesAdapter
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_doctor_work.*

@AndroidEntryPoint
class DoctorWorkFragment : Fragment()  {

    val doctorViewModel: DoctorViewModel by viewModels()


    lateinit var doctor: DoctorDetails
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctor = it.getParcelable(Constants.DOCTOR_ITEM_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_work, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        doctorViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(DoctorViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initViews() {
//        if (doctor.doctorMedicalProviders != null) {
//            layout_owner_provider.visibility = View.VISIBLE
//            tv_workPlace_name.text = doctor.doctorMedicalProviders?.name
//            rb_workPlace.rating = doctor.doctorMedicalProviders?.rate?.toFloat()!!
//            tv_branches.text = doctor.doctorMedicalProviders?[0]!!.name
//            iv_workPlace_image.setImageResource(R.drawable.ic_clinic_avatar)
//        }

        if (doctor.doctorMedicalProviders == null || doctor.doctorMedicalProviders!!.isEmpty()) {
        } else {
            var adapter =
                WorkPlacesAdapter(doctorViewModel)
            adapter.setList(doctor.doctorMedicalProviders!!.toMutableList())
            rb_medical_providers.adapter = adapter
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: DoctorDetails) =
            DoctorWorkFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.DOCTOR_ITEM_KEY, param1)
                }
            }
    }
}
