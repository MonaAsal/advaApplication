package com.salamtak.app.ui.component.health.medicalprovider

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Doctor
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.ui.common.BaseFragment


import com.salamtak.app.ui.component.health.medicalprovider.adapter.DoctorsAdapter
import com.salamtak.app.ui.component.health.medicalprovider.adapter.SpecialityAdapter
import com.salamtak.app.ui.component.health.OperationsViewModelN
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Event
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_doctor_overview.tv_about
import kotlinx.android.synthetic.main.fragment_medical_provider_overview.*

@AndroidEntryPoint
class MedicalProviderOverviewFragment : BaseFragment()  {

    lateinit var medicalProvider: MedicalProviderDetails

    val operationsViewModel: OperationsViewModelN by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_medical_provider_overview

    override fun observeViewModel() {
        observeEvent(operationsViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
    }

    fun handleOpenDoctorEvent(navigateEvent: Event<String>) {
        navigateEvent.getContentIfNotHandled()?.let {
            LogUtil.LogFirebaseEvent(
                "btn_operation_doctor",
                "screen_" + this::getActivity::class.java.simpleName,
                "doctor",
                it
            )
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to it)
            navigateToDoctor(bundle)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicalProvider = it.getParcelable(Constants.BRANCH_ITEM_KEY)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        operationsViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(OperationsViewModelN::class.java)

        initViews()
    }

    private fun initViews() {
        tv_about.text = medicalProvider.about

        medicalProvider.specialization?.let {
            tv_medical_specialities_lbl.visibility = View.VISIBLE
            var specialityAdapter = SpecialityAdapter()
            specialityAdapter.setList(medicalProvider.specialization!!.toMutableList())
            rv_specialities.layoutManager =
                StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL)

            rv_specialities.adapter = specialityAdapter
        }
        /*if(medicalProvider.specialization==null) {
            tv_medical_specialities_lbl.visibility = View.GONE
        }*/
//        bindDoctorsList(medicalProvider.doctors!!)
    }

    fun bindDoctorsList(doctors: List<Doctor>) {
        if (doctors != null) {
            var doctorsAdapter = DoctorsAdapter(operationsViewModel)
            doctorsAdapter.setList(doctors!!.toMutableList())

            rv_doctors.adapter = doctorsAdapter
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(medicalProvider: MedicalProviderDetails) =
            MedicalProviderOverviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.BRANCH_ITEM_KEY, medicalProvider)
                }
            }
    }
}
