package com.salamtak.app.ui.component.wishlist

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.ui.component.health.OperationsViewModelN
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class WishListFragment : BaseFragment() , OperationListenerN {
    override val layoutId: Int
        get() = R.layout.fragment_wish_list


    lateinit var adapter: OperationsAdapter
    val operationsViewModel: OperationsViewModelN by viewModels()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        operationsViewModel.getFavourites()
        initViews()
    }


    private fun initViews() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_title.text = getString(R.string.my_wishlist)

        adapter = OperationsAdapter(true, this)
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

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        operationsViewModel.addToFavourite(operation.id)
    }

    override fun onOwnerClicked(operation: Operation) {

        if (operation.owner!!.type == ProviderType.Doctor.typeId) {
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to operation.owner!!.id)
            navigateToDoctor(bundle)

        } else {
            val bundle = bundleOf(Constants.KEY_ID to operation.owner!!.id)
            navigateToMedicalProvider(bundle)
        }

    }

    override fun onDetailsClicked(operation: Operation) {
        navigateToOperationBookingScreen(operation)
        //  val bundle = bundleOf( Constants.KEY_OPERATION_ITEM to operation.id)
        // navigateToOperationBookingScreen(bundle)
    }

    fun navigateToOperationBookingScreen(operation: Operation) {
        LogUtil.LogFirebaseEvent(
            "btn_book_now",
            "screen_" + this::class.java.simpleName,
            "operation",
            operation.id
        )

        startActivity(
            activity?.intentFor<BookHealthRequestActivity>(
                Constants.KEY_OPERATION_ITEM to operation.id
            )
        )
    }

}