package com.salamtak.app.ui.component.requests

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.bookingrequests.BookedRequestsViewModel
import com.salamtak.app.ui.component.bookingrequests.OperationCancelDialog
import com.salamtak.app.ui.component.bookingrequests.OperationDetailsDialog
import com.salamtak.app.ui.component.bookingrequests.adapter.OperationsTrackingAdapter
import com.salamtak.app.ui.component.health.doctor.DoctorActivity
import com.salamtak.app.ui.component.premiumcard.PremiumActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_booked_operations.progress_bar
import kotlinx.android.synthetic.main.activity_booked_operations.rv_operations
import kotlinx.android.synthetic.main.activity_booked_operations.tv_remaining_records
import kotlinx.android.synthetic.main.activity_booked_operations.v_empty
import kotlinx.android.synthetic.main.layout_no_operations_booking.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class RequestsFragment : BaseFragment() {

    val operationsTrackingViewModel: BookedRequestsViewModel by viewModels()
    lateinit var mAdapter: OperationsTrackingAdapter
    var fromReviews = false
    override val layoutId: Int
        get() = R.layout.fragment_requests

     var canceledItemId :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // Log.d("RequestsFragment","RequestsFragment")
        initViews()

        LogAdjustEvent("z4skao")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        v_empty.btn_book_now2.setOnClickListener {
            navigateToHome()
        }
        tv_toolbar_title.text = getString(R.string.my_installments)
        iv_toolbar_back.visibility=View.GONE
    }


    override fun onResume() {
        super.onResume()

        try {
            loadData()
        }catch (e :Exception){

        }
    }

    fun loadData() {
        operationsTrackingViewModel.page = 1
        operationsTrackingViewModel.getMyBookedOperations()
    }


    private fun initViews() {
        mAdapter = OperationsTrackingAdapter(operationsTrackingViewModel)
    }


    override fun observeViewModel() {
        observe(operationsTrackingViewModel.showLoading, ::showLoadingView)
        observe(operationsTrackingViewModel.showServerError, ::showServerErrorMessage)
        observe(operationsTrackingViewModel.bookedOperationsLiveData, ::handleOperationsResponse)
        observe(operationsTrackingViewModel.openCancelOperation, ::openCancelVisit)
        observe(operationsTrackingViewModel.openOperationDetails, ::openOperationDetails)
        observe(operationsTrackingViewModel.openPaymentScreen, ::openPaymentScreen)
        observe(operationsTrackingViewModel.openMakeReview, ::openReviews)
        observeEvent(operationsTrackingViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun handleOperationsResponse(response: SalamtakListResponse<BookedOperation>) {
        showOperations(response)
        if (activity?.intent?.extras != null && activity?.intent?.extras?.containsKey(Constants.KEY_ID)!!) {
            operationsTrackingViewModel.getOperationById(activity?.intent?.getStringExtra(Constants.KEY_ID)!!)
        }

        if (fromReviews) {
            showOperations(
                operationsTrackingViewModel.bookedOperationsLiveData.value!!
            )
        }

    }

    private fun showOperations(response: SalamtakListResponse<BookedOperation>) {
        var operations = response.data

        if (operationsTrackingViewModel.page > 1) {
            tv_remaining_records.text = getRemaining(
                response!!.totalRecords,
                operationsTrackingViewModel.page
            )
            mAdapter.addData(operations.toMutableList())
        } else if (!(operations.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                response!!.totalRecords,
                operationsTrackingViewModel.page
            )
            mAdapter.setList(operations.toMutableList())
            rv_operations.adapter = mAdapter
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

    private fun openCancelVisit(resource: BookedOperation) {
        val fm = activity?.supportFragmentManager
        val dialog = OperationCancelDialog()
        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_OPERATION_ITEM, resource)
        dialog.arguments = bundle
        fm?.let { dialog.show(it, getString(R.string.app_name)) }
        canceledItemId = resource.id
      //  Log.d("bookoperationsubmitted" ,canceledItemId)

    }


    private fun openOperationDetails(operation: BookedOperation) {
        val fm = activity?.supportFragmentManager
        val dialog = OperationDetailsDialog()
        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_OPERATION_ITEM, operation)
        dialog.arguments = bundle
        fm?.let { dialog.show(it, getString(R.string.app_name)) }
    }

    fun openPaymentScreen(operation: BookedOperation) {
        startActivity(
            activity?.intentFor<PremiumActivity>(
                Constants.KEY_OPERATION_ITEM to operation
            )
        )
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

    fun handleOpenDoctorEvent(navigateEvent: Event<String>) {
        navigateEvent.getContentIfNotHandled()?.let {
            LogUtil.LogFirebaseEvent(
                "btn_operation_doctor",
                "screen_" + activity.let { it!!::getParent::class.java.simpleName},
                "doctor",
                it
            )

            navigateToDoctorScreen(it)
        }
    }

    fun navigateToDoctorScreen(doctorId: String) {
        startActivity(activity?.intentFor<DoctorActivity>(Constants.DOCTOR_ITEM_KEY to doctorId))
        activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }


}