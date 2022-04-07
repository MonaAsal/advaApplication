//package com.salamtak.app.ui.component.health.operationslist
//
//import android.os.Bundle
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.Operation
//import com.salamtak.app.data.entities.responses.SalamtakListResponse
//import com.salamtak.app.data.enums.OperationsFrom
//import com.salamtak.app.ui.common.listeners.PaginationScrollListener
//import com.salamtak.app.ui.component.health.BaseOperationsActivity
//import com.salamtak.app.utils.Constants
//import com.salamtak.app.utils.observe
//import com.salamtak.app.utils.toGone
//import com.salamtak.app.utils.toVisible
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_operations2.*
//import kotlinx.android.synthetic.main.toolbar.*
//
//@AndroidEntryPoint
//class OperationsActivity : BaseOperationsActivity() {
//    override val layoutId: Int
//        get() = R.layout.activity_operations2
//
//    lateinit var adapter: OperationsAdapter
//
//    override fun initializeViewModel() {
//        //operationsViewModel = viewModelFactory.create(OperationsViewModelN::class.java)
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_operations2)
//
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        tv_toolbar_title.text = intent.getStringExtra(Constants.KEY_NAME)
//        operationsViewModel.from = intent.getIntExtra(Constants.KEY_FROM, 1)
//        if (operationsViewModel.from == OperationsFrom.Subcategories.value)
//            operationsViewModel.subcategoryId = intent.getIntExtra(Constants.KEY_ID, 0)
//        else
//            operationsViewModel.id = intent.getStringExtra(Constants.KEY_ID).toString()
//
//        operationsViewModel.categoryId = intent.getIntExtra(Constants.KEY_CATEGORY_ID, 0)
//
//        operationsViewModel.loadData()
//
//        adapter = OperationsAdapter(true, this)
//    }
//
//    override fun observeViewModel() {
//        with(operationsViewModel)
//        {
//            observe(operationsResponse, ::showOperations)
//            observe(showLoading, ::showLoadingView)
//            observe(showServerError, ::showServerErrorMessage)
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
//                operationsViewModel.page,operationsViewModel.getLocaleString()
//            )
//            adapter.addData(operations!!)
//        } else if (!(operations.isNullOrEmpty())) {
//            tv_remaining_records.text = getRemaining(
//                operationsViewModel.operationsResponse.value!!.totalRecords,
//                operationsViewModel.page,operationsViewModel.getLocaleString()
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
//
//}