package com.salamtak.app.ui.component.health.medicalprovider

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.BaseFragment


import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.medicalprovider.adapter.ProviderOperationsAdapter
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_operations.rv_operations_list
import kotlinx.android.synthetic.main.activity_operations.view_no_operations
import kotlinx.android.synthetic.main.fragment_doctor_operations.*

@AndroidEntryPoint
class MedicalProviderOperationsFragment : BaseFragment() , OperationListenerN {

    override val layoutId: Int
        get() = R.layout.fragment_doctor_operations

    lateinit var medicalProvider: MedicalProviderDetails

    val medicalProviderViewModel: MedicalProviderViewModel by viewModels()


    lateinit var adapter: ProviderOperationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicalProvider = it.getParcelable(Constants.MEDICAL_PROVIDER_ITEM_KEY)!!
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeViewModel()
        medicalProviderViewModel.getMedicalProviderOperations(medicalProvider.id)
        adapter = ProviderOperationsAdapter(false, this)
    }

    private fun showDataView(show: Boolean) {
        view_no_operations.visibility = if (show) View.GONE else View.VISIBLE
        rv_operations_list.visibility = if (show) View.VISIBLE else View.GONE
    }



  override  fun observeViewModel() {

        observe(medicalProviderViewModel.showLoading, ::showLoadingView)
        observe(medicalProviderViewModel.showServerError, ::showServerErrorMessage)
        observe(medicalProviderViewModel.operationsResponse, ::showOperations)
    }

    fun showLoadingView(show: Boolean) {
        (parentFragment as MedicalProviderFragment).showLoadingView(show)
    }


    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {
        var operations = salamtakListResponse!!.data!!

        if (salamtakListResponse.totalRecords > 0) {
            tv_remaining_records.text = getRemaining(
                medicalProviderViewModel.operationsResponse.value!!.totalRecords,
                medicalProviderViewModel.page, medicalProviderViewModel.getLocale()
            )
            if (medicalProviderViewModel.page > 1) {
                adapter.addData(operations!!)
            } else if (!(operations.isNullOrEmpty())) {
                adapter.setList(operations.toMutableList())
                rv_operations_list.adapter = adapter

                rv_operations_list.addOnScrollListener(object :
                    PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                    override fun isLastPage(): Boolean {
                        return medicalProviderViewModel.isLastPage
                    }

                    override fun isLoading(): Boolean {
                        return medicalProviderViewModel.isLoading
                    }

                    override fun loadMoreItems() {
                        medicalProviderViewModel.page++
                        medicalProviderViewModel.isLoading = true
                        medicalProviderViewModel.getMedicalProviderOperations(medicalProvider.id)
                    }
                })
                showDataView(true)
            }
        } else {
            showDataView(false)
        }
        medicalProviderViewModel.isLoading = false
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: MedicalProviderDetails) =
            MedicalProviderOperationsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.MEDICAL_PROVIDER_ITEM_KEY, param1)
                }
            }
    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        (parentFragment as MedicalProviderFragment).onFavouriteClicked(operation, position)
    }

    override fun onOwnerClicked(operation: Operation) {
        (parentFragment as MedicalProviderFragment).onOwnerClicked(operation)
    }

    override fun onDetailsClicked(operation: Operation) {
        (parentFragment as MedicalProviderFragment).onDetailsClicked(operation)
    }
}
