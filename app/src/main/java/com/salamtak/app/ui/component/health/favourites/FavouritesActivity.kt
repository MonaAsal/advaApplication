package com.salamtak.app.ui.component.health.favourites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R

import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.listeners.PaginationScrollListener

import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class FavouritesActivity : BaseOperationsActivity() {

    override val layoutId: Int
        get() = R.layout.activity_favourites

    lateinit var adapter: OperationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        operationsViewModel.getFavourites()

        initViews()
    }

    private fun initViews() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_title.text = getString(R.string.favourites)

        adapter =
            OperationsAdapter(
                true, this
            )
    }

    override fun initializeViewModel() {
//        operationsViewModel = viewModelFactory.create(OperationsViewModelN::class.java)
    }

    override fun observeViewModel() {

        observe(operationsViewModel.showLoading,::showLoading)
        observe(operationsViewModel.favouritesResponse, ::showOperations)
//        observeEvent(operationsViewModel.openOperationBooking, ::handleOperationBookingEvent)
//        observeEvent(operationsViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
//        observeEvent(operationsViewModel.openReviewsScreen, ::handleOpenReviewsEvent)
//        observeEvent(
//            operationsViewModel.openMedicalProviderScreen,
//            ::handleOpenMedicalProviderEvent
//        )
    }

//    private fun handleFavouriteResponse(response: SalamtakListResponse<OperationN>) {
//        response.data?.let { bindListData(it!!)
//        }
//    }

    private fun showDataView(show: Boolean) {
        view_no_operations.visibility = if (show) View.GONE else View.VISIBLE
        rv_operations_list.visibility = if (show) View.VISIBLE else View.GONE
        progress_bar.toGone()
    }

    fun showLoading( show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {
        var operations = salamtakListResponse!!.data!!

        if (operationsViewModel.page > 1) {
            tv_remaining_records.text = getRemaining(
                salamtakListResponse!!.totalRecords,
                operationsViewModel.page
            )
            adapter.addData(operations!!)
        } else if (!(operations.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                salamtakListResponse!!.totalRecords,
                operationsViewModel.page
            )
            adapter.setList(operations.toMutableList())
            rv_operations_list.adapter = adapter

            rv_operations_list.addOnScrollListener(object :
                PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return operationsViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return operationsViewModel.isLoading
                }

                override fun loadMoreItems() {
                    operationsViewModel.page++
                    operationsViewModel.isLoading = true
                    operationsViewModel.getFavourites()
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }
        operationsViewModel.isLoading = false
    }


//    private fun bindListData(operations: List<OperationN>) {

//        if (!(operations.isNullOrEmpty())) {
//            operationsAdapter.setList(operations.toMutableList())
//            rv_operations_list.adapter = operationsAdapter
//            showDataView(true)
//        } else {
//            showDataView(false)
//        }
//    }

}
