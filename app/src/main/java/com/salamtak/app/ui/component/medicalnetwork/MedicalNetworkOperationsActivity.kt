package com.salamtak.app.ui.component.medicalnetwork

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.doctor.DoctorViewModel
import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderViewModel
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_operations.progress_bar
import kotlinx.android.synthetic.main.activity_operations.rv_operations_list
import kotlinx.android.synthetic.main.activity_operations.view_no_operations
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MedicalNetworkOperationsActivity : BaseOperationsActivity(), OperationListenerN {
    override val layoutId: Int
        get() = R.layout.activity_doctors_operations


    var doctorId = ""

    val doctorViewModel: DoctorViewModel by viewModels()

    val medicalProviderViewModel: MedicalProviderViewModel by viewModels()

    lateinit var adapter: OperationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var providerName = intent.getStringExtra(Constants.KEY_PROVIDER_NAME)!!
        tv_toolbar_title.text = providerName
        if (intent.extras!!.containsKey(Constants.DOCTOR_ITEM_KEY)) {
            doctorViewModel.doctorId = intent.getStringExtra(Constants.DOCTOR_ITEM_KEY)!!

            doctorViewModel.getDoctorOperations()

        } else {
            var providerId = intent.getStringExtra(Constants.MEDICAL_PROVIDER_ITEM_KEY)
            medicalProviderViewModel.getMedicalProviderOperations(providerId!!)
        }

        adapter = OperationsAdapter(true, this,2)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }

    override fun initializeViewModel() {
//        doctorViewModel =
//            ViewModelProviders.of(this, viewModelFactory).get(DoctorViewModel::class.java)
//
//        medicalProviderViewModel =
//            ViewModelProviders.of(this, viewModelFactory).get(MedicalProviderViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(doctorViewModel.showLoading, ::showLoadingView)
        observe(doctorViewModel.showServerError, ::showServerErrorMessage)
        observe(doctorViewModel.operationsResponse, ::showOperations)

        observe(medicalProviderViewModel.showLoading,::showLoadingView)
        observe(medicalProviderViewModel.showServerError, ::showServerErrorMessage)
        observe(medicalProviderViewModel.operationsResponse, ::showOperations)

    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {
        var operations = salamtakListResponse!!.data!!

        if (doctorViewModel.page > 1) {
            adapter.addData(operations!!)
        } else if (!(operations.isNullOrEmpty())) {
            adapter.setList(operations.toMutableList())
            rv_operations_list.adapter = adapter

            rv_operations_list.addOnScrollListener(object :
                PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return doctorViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return doctorViewModel.isLoading
                }

                override fun loadMoreItems() {
                    doctorViewModel.page++
                    doctorViewModel.isLoading = true
                    doctorViewModel.getDoctorOperations()
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }
        doctorViewModel.isLoading = false
    }


    private fun showDataView(show: Boolean) {
        view_no_operations.visibility = if (show) View.GONE else View.VISIBLE
        rv_operations_list.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }

}
