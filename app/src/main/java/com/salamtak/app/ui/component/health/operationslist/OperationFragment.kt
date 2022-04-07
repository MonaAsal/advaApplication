//package com.salamtak.app.ui.component.health.operationslist
//
//import android.os.Bundle
//import android.util.Log
//import androidx.core.os.bundleOf
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.Operation
//import com.salamtak.app.data.entities.responses.SalamtakListResponse
//import com.salamtak.app.data.enums.OperationsFrom
//import com.salamtak.app.data.enums.ProviderType
//import com.salamtak.app.ui.common.BaseFragment
//import com.salamtak.app.ui.common.listeners.PaginationScrollListener
//import com.salamtak.app.ui.component.health.adapter.OperationListenerN
//import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
//import com.salamtak.app.utils.*
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_operations2.*
//import kotlinx.android.synthetic.main.toolbar.*
//import org.jetbrains.anko.intentFor
//
//@AndroidEntryPoint
//class OperationFragment : BaseFragment(), OperationListenerN {
//    override val layoutId: Int
//        get() = R.layout.fragment_operation
//    lateinit var adapter: OperationsAdapter
//    val operationsViewModel: OperationsViewModelN by viewModels()
//
//
//    override fun onResume() {
//        super.onResume()
//        Log.e("operation", "fragment")
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        Log.d("state", "onActivityCreated")
//
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        tv_toolbar_title.text = arguments!!.getString(Constants.KEY_NAME)
//        operationsViewModel.from = arguments!!.getInt(Constants.KEY_FROM, 1)
//        if (operationsViewModel.from == OperationsFrom.Subcategories.value)
//            operationsViewModel.subcategoryId = arguments!!.getInt(Constants.KEY_ID, 0)
//        else
//            operationsViewModel.id = arguments!!.getString(Constants.KEY_ID).toString()
//
//        operationsViewModel.categoryId = arguments!!.getInt(Constants.KEY_CATEGORY_ID, 0)
//
//        operationsViewModel.loadData()
//
//        adapter = OperationsAdapter(true, this)
//    }
//
//    override fun observeViewModel() {
//        with(operationsViewModel)
//        {
//            observe(showLoading, ::showLoadingView)
//            observe(showServerError, ::showServerErrorMessage)
//            observe(operationsResponse, ::showOperations)
//
//        }
//    }
//
//    fun showLoadingView(show: Boolean) {
//        if (show)
//            progress_bar.toVisible()
//        else
//            progress_bar.toGone()
//    }
//
//    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {
//        var operations = salamtakListResponse!!.data!!
//
//        if (operationsViewModel.page > 1) {
//            tv_remaining_records.text = getRemaining(
//                operationsViewModel.operationsResponse.value!!.totalRecords,
//                operationsViewModel.page, operationsViewModel.getLocaleString()
//            )
//            adapter.addData(operations!!)
//        } else if (!(operations.isNullOrEmpty())) {
//            tv_remaining_records.text = getRemaining(
//                operationsViewModel.operationsResponse.value!!.totalRecords,
//                operationsViewModel.page, operationsViewModel.getLocaleString()
//            )
//            adapter.setList(operations.toMutableList())
//            rv_operations_list.adapter = adapter
//
//            rv_operations_list.addOnScrollListener(object :
//                PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
//                override fun isLastPage(): Boolean {
//                    return operationsViewModel.isLastPage
//                }
//
//                override fun isLoading(): Boolean {
//                    return operationsViewModel.isLoading
//                }
//
//                override fun loadMoreItems() {
//                    operationsViewModel.page++
//                    operationsViewModel.isLoading = true
//                    operationsViewModel.loadData()
//                }
//            })
//            showDataView(true)
//        } else {
//            showDataView(false)
//        }
//        operationsViewModel.isLoading = false
//    }
//
//    fun showDataView(show: Boolean) {
//        if (show) {
//            rv_operations_list.toVisible()
//            view_no_operations.toGone()
//        } else {
//            rv_operations_list.toGone()
//            view_no_operations.toVisible()
//        }
//
//    }
//
//    override fun onFavouriteClicked(operation: Operation, position: Int) {
//        operationsViewModel.addToFavourite(operation.id)
//    }
//
//    override fun onOwnerClicked(operation: Operation) {
//        if (operation.owner!!.type == ProviderType.Doctor.typeId) {
//            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to operation.owner!!.id)
//            navigateToDoctor(bundle)
//
//        } else {
//            val bundle = bundleOf(Constants.KEY_ID to operation.owner!!.id)
//            navigateToMedicalProvider(bundle)
//        }
//
//    }
//
//    override fun onDetailsClicked(operation: Operation) {
//        navigateToOperationBookingScreen(operation)
//        //  val bundle = bundleOf( Constants.KEY_OPERATION_ITEM to operation.id)
//        // navigateToOperationBookingScreen(bundle)
//    }
//
//    fun navigateToOperationBookingScreen(operation: Operation) {
//        LogUtil.LogFirebaseEvent(
//            "btn_book_now",
//            "screen_" + this::class.java.simpleName,
//            "operation",
//            operation.id
//        )
//
//        startActivity(
//            activity?.intentFor<BookHealthRequestActivity>(
//                Constants.KEY_OPERATION_ITEM to operation.id
//            )
//        )
//    }
//
//}