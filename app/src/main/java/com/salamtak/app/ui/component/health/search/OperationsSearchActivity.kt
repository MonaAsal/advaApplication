package com.salamtak.app.ui.component.health.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_operations_search.progress_bar
import kotlinx.android.synthetic.main.activity_operations_search.rv_operations_list
import kotlinx.android.synthetic.main.activity_operations_search.tv_remaining_records
import kotlinx.android.synthetic.main.activity_operations_search.view_no_operations
import kotlinx.android.synthetic.main.toolbar_back_filter.*

@AndroidEntryPoint
class OperationsSearchActivity : BaseOperationsActivity() {

    val searchViewModel: SearchViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_operations_search

    lateinit var operationsAdapter: OperationsAdapter
    var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        searchViewModel.filter(
//            intent!!.getStringExtra(Constants.QUERY_KEY)!!,
//            intent!!.getParcelableExtra(Constants.CATEGORY_ID_KEY),
//            intent!!.getParcelableExtra(Constants.AREA_KEY)
//        )

        query = intent!!.getStringExtra(Constants.QUERY_KEY)!!
        searchViewModel.query!!.value = query
        Log.e("seach query", query)
        searchViewModel.searchHealth(query)

        initViews()
    }

    private fun initViews() {

//        handleAddFinancialProfile()

        tv_toolbar_title.text =
            if (searchViewModel.query!!.value != null && searchViewModel.query!!.value!!.isNotEmpty()) {
                //             searchViewModel.query.value!!
                getString(R.string.search_result) + " (" + searchViewModel.query!!.value + ")"

            } else {
                getString(R.string.filter_results)
            }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        operationsAdapter =
            OperationsAdapter(
                true, this
            )
    }

    private fun handleAddFinancialProfile() {
//        if (searchViewModel.shouldAddFinancialInfo())
//            tv_add_financial_profile.visibility = View.VISIBLE
//        else
//            tv_add_financial_profile.visibility = View.GONE

//        tv_add_financial_profile.setOnClickListener { navigateToFinancialInfoScreen() }
    }

    override fun initializeViewModel() {
//        searchViewModel = viewModelFactory.create(SearchViewModel::class.java)
    }

    override fun observeViewModel() {
        with(searchViewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(searchViewModel.operationsResponse, ::showOperations)
            observeEvent(searchViewModel.openOperationBooking, ::handleOperationBookingEvent)
            observeEvent(searchViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
            observeEvent(
                searchViewModel.openMedicalProviderScreen,
                ::handleOpenMedicalProviderEvent
            )
            observeEvent(searchViewModel.openReviewsScreen, ::handleOpenReviewsEvent)
        }
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

//    private fun handleSearchResponse1(salamtakListResponse: SalamtakListResponse<OperationN>?) {
//        salamtakListResponse!!.data?.let { bindListData(it) }
//    }

    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {

        var operations = salamtakListResponse!!.data!!

        if (searchViewModel.page > 1) {
            tv_remaining_records.text = getRemaining(
                searchViewModel.operationsResponse.value!!.totalRecords,
                searchViewModel.page
            )
            operationsAdapter.addData(operations!!)
        } else if (!(operations.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                searchViewModel.operationsResponse.value!!.totalRecords,
                searchViewModel.page
            )
            operationsAdapter.setList(operations.toMutableList())
            rv_operations_list.adapter = operationsAdapter

            rv_operations_list.addOnScrollListener(object :
                PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return searchViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return searchViewModel.isLoading
                }

                override fun loadMoreItems() {
                    searchViewModel.page++
                    searchViewModel.isLoading = true
                    searchViewModel.searchHealth(query)
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }
        searchViewModel.isLoading = false
    }


//    private fun handleSearchResponse(categoryDetailsModel: Resource<SalamtakListResponse<OperationN>>) {
//        when (categoryDetailsModel) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.Success -> categoryDetailsModel.data?.let { bindListData(it.data!!) }
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                showDataView(false)
//                categoryDetailsModel.errorCode?.let {
//                    var error = searchViewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                showDataView(false)
//                categoryDetailsModel.errorResponse?.let { showServerErrorMessage(it) }
//            }
//        }
//    }

    private fun bindListData(operations: List<Operation>) {

        if (!(operations.isNullOrEmpty())) {
            operationsAdapter.setList(operations.toMutableList())
            rv_operations_list.adapter = operationsAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun showDataView(show: Boolean) {

        view_no_operations.visibility = if (show) View.GONE else View.VISIBLE
        rv_operations_list.visibility = if (show) View.VISIBLE else View.GONE
        progress_bar.toGone()
    }


}
