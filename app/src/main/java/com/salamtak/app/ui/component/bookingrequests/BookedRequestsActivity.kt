package com.salamtak.app.ui.component.bookingrequests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.bookingrequests.adapter.OperationsTrackingAdapter
import com.salamtak.app.ui.component.premiumcard.PremiumActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_booked_operations.*
import kotlinx.android.synthetic.main.layout_no_operations_booking.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@AndroidEntryPoint
class BookedRequestsActivity : BaseOperationsActivity() {
    override val layoutId: Int
        get() = R.layout.activity_booked_operations

    val operationsTrackingViewModel: BookedRequestsViewModel by viewModels()

    lateinit var adapter: OperationsTrackingAdapter

    var fromReviews = false

    override fun initializeViewModel() {
    }



    override fun observeViewModel() {
        observe(operationsTrackingViewModel.showLoading, ::showLoadingView)
        observe(operationsTrackingViewModel.showServerError, ::showServerErrorMessage)
        observe(operationsTrackingViewModel.bookedOperationsLiveData, ::handleOperationsResponse)
        observe(operationsTrackingViewModel.openCancelOperation, ::openCancelVisit)
        observe(operationsTrackingViewModel.openOperationDetails, ::openOperationDetails)
        observe(operationsTrackingViewModel.openPaymentScreen, ::openPaymentScreen)
//        observeEvent(operationsTrackingViewModel.showToast, ::handleShowMessage)
        observe(operationsTrackingViewModel.openMakeReview, ::openReviews)
        observeEvent(operationsTrackingViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun openReviews(event: BookedOperation) {
//        val fm = supportFragmentManager
//        val dialog =
//            OperationReviewDialog()
//
//        val bundle = Bundle()
//        bundle.putParcelable(Constants.OPERATION_ITEM_KEY, event)
//        dialog.arguments = bundle
//
//        dialog.show(fm, getString(R.string.app_name))
    }

    fun openPaymentScreen(operation: BookedOperation) {
        startActivity(
            intentFor<PremiumActivity>(
                Constants.KEY_OPERATION_ITEM to operation
            )
        )
    }

    private fun openOperationDetails(operation: BookedOperation) {
        val fm = supportFragmentManager
        val dialog = OperationDetailsDialog()

        val bundle = Bundle()

//        Log.e("aaa", operation!!.id)
        bundle.putParcelable(Constants.KEY_OPERATION_ITEM, operation)
        dialog.arguments = bundle

        dialog.show(fm, getString(R.string.app_name))
    }

    private fun openCancelVisit(resource: BookedOperation) {
        val fm = supportFragmentManager
        val dialog = OperationCancelDialog()

        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_OPERATION_ITEM, resource)
        dialog.arguments = bundle

        dialog.show(fm, getString(R.string.app_name))

    }

    private fun handleOperationsResponse(response: SalamtakListResponse<BookedOperation>) {
        showOperations(response)
        if (intent.extras != null && intent.extras?.containsKey(Constants.KEY_ID) == true) {
            var operationId = intent.getStringExtra(Constants.KEY_ID)
            operationId?.let {
             //      operationsTrackingViewModel.getOperationById(it)
                }

         //   Log.d("handleOperationsResponseyasmen2", operationsTrackingViewModel.bookedOperationsLiveData.value?.data.toString())

          //  Log.d("handleOperationsResponseyasmen", operationId.toString())

        }

        if (fromReviews) {
            showOperations(
                operationsTrackingViewModel.bookedOperationsLiveData.value!!
            )
        }

    }

    private fun showOperations(response: SalamtakListResponse<BookedOperation>) {
        var operations = response.data
        //var filteredList = data.filter { it.currentStatus in (statuses) }
        //if (!(filteredList.isNuFfllOrEmpty())) {
//        if (!(data.isNullOrEmpty())) {
//            v_empty.visibility = View.GONE
//            rv_operations.visibility = View.VISIBLE
//            adapter.setList(data.toMutableList())
//            rv_operations.adapter = adapter
//        } else {
//            v_empty.visibility = View.VISIBLE
//            rv_operations.visibility = View.GONE
//        }

        if (operationsTrackingViewModel.page > 1) {
            tv_remaining_records.text = getRemaining(response!!.totalRecords, operationsTrackingViewModel.page)
            adapter.addData(operations.toMutableList())

        } else if (!(operations.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(response!!.totalRecords, operationsTrackingViewModel.page)
            adapter.setList(operations.toMutableList())
            rv_operations.adapter = adapter

            rv_operations.addOnScrollListener(object :
                PaginationScrollListener(rv_operations.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return operationsTrackingViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return operationsTrackingViewModel.isLoading
                }

                override fun loadMoreItems() {
                    operationsTrackingViewModel.page++
                    operationsTrackingViewModel.isLoading = true
                    operationsTrackingViewModel.getMyBookedOperations()
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }
        operationsTrackingViewModel.isLoading = false

    }

    private fun showDataView(show: Boolean) {
        if (show) {
            v_empty.visibility = View.GONE
            rv_operations.visibility = View.VISIBLE
        } else {
            v_empty.visibility = View.VISIBLE
            rv_operations.visibility = View.GONE
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        LogAdjustEvent("z4skao")
    }

    public override fun onResume() {
        try {
            super.onResume()
            operationsTrackingViewModel.getMyBookedOperations()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun initViews() {
        tv_toolbar_title.text = getString(R.string.my_bookings)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        btn_book_now2.setOnClickListener { navigateToMainScreen() }

        adapter = OperationsTrackingAdapter(operationsTrackingViewModel)

//        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                if (operationsTrackingViewModel.bookedOperationsLiveData.value != null && operationsTrackingViewModel.bookedOperationsLiveData.value!!.data != null)
//                    when (tab.position) {
//                        0 -> {
//                            showOperations(
//                                operationsTrackingViewModel.bookedOperationsLiveData.value!!.data?.data!!,
//                                arrayOf(0)
//                            )
//                        }
//                        1 -> { // in progress
//
//                            showOperations(
//                                operationsTrackingViewModel.bookedOperationsLiveData.value!!.data?.data!!,
//                                arrayOf(1, 2, 3, 4, 5, 9)
//                            )
//                        }
////                        2 -> { // canceled
////                            showOperations(
////                                operationsTrackingViewModel.bookedOperationsLiveData.value!!.data?.data!!,
////                                arrayOf(7, 8, 10)
////                            )
////                        }
//                        2 -> {
//
//                            showOperations(
//                                operationsTrackingViewModel.bookedOperationsLiveData.value!!.data?.data!!,
//                                arrayOf(6)
//                            )
//                        }
//                    }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//            }
//        })
//
//        tabs.addTab(tabs.newTab().setText(getString(R.string.submitted)))
//        tabs.addTab(tabs.newTab().setText(getString(R.string.in_progress)))
////        tabs.addTab(tabs.newTab().setText(getString(R.string.calceled)))
//        tabs.addTab(tabs.newTab().setText(getString(R.string.completed)))
    }


}
