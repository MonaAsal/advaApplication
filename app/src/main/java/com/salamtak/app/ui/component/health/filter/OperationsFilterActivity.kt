package com.salamtak.app.ui.component.health.filter

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_operations_search.*
import kotlinx.android.synthetic.main.toolbar_back_filter.*

@AndroidEntryPoint
class OperationsFilterActivity : BaseOperationsActivity() {

    val filterViewModel: FilterViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_operations_search

    lateinit var operationsAdapter: OperationsAdapter
    var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filterViewModel.advanced = intent.getBooleanExtra(Constants.ADVANCED, false)
        filterViewModel.categoryId = intent.getStringExtra(Constants.CATEGORY_ID_KEY)!!
        filterViewModel.selectedSubCategoryId =
            intent.getStringExtra(Constants.KEY_SUBCATEGORY_ID)!!
        filterViewModel.cityId = intent.getStringExtra(Constants.KEY_CITY_ID)!!
        filterViewModel.medicalProvider = intent.getStringExtra(Constants.KEY_PROVIDER_NAME)!!
        filterViewModel.operationName = intent.getStringExtra(Constants.KEY_OPERATION_ITEM)!!
        filterViewModel.from = intent.getStringExtra(Constants.KEY_FROM)!!
        filterViewModel.to = intent.getStringExtra(Constants.KEY_TO)!!

        filterViewModel.findResults()
//        filterViewModel.filter(
//            intent!!.getStringExtra(Constants.QUERY_KEY)!!,
//            intent!!.getParcelableExtra(Constants.CATEGORY_ID_KEY),
//            intent!!.getParcelableExtra(Constants.AREA_KEY)
//        )

//        query = intent!!.getStringExtra(Constants.QUERY_KEY)!!
//        filterViewModel.query!!.value = query
//        Log.e("seach query", query)
//        filterViewModel.searchHealth(query)

        initViews()
    }

    private fun initViews() {

//        handleAddFinancialProfile()

        tv_toolbar_title.text = getString(R.string.filter_results)
//            if (filterViewModel.query!!.value != null && filterViewModel.query!!.value!!.isNotEmpty()) {
//                //             filterViewModel.query.value!!
//                getString(R.string.search_result) + " (" + filterViewModel.query!!.value + ")"
//
//            } else {
//                getString(R.string.filter_results)
//            }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        operationsAdapter =
            OperationsAdapter(
                true, this
            )
    }


    override fun initializeViewModel() {
//        filterViewModel = viewModelFactory.create(FilterViewModel::class.java)
    }

    override fun observeViewModel() {
        with(filterViewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(filterViewModel.operationsResponse, ::showOperations)
//            observeEvent(filterViewModel.openOperationBooking, ::handleOperationBookingEvent)
//            observeEvent(filterViewModel.openDoctorScreen, ::handleOpenDoctorEvent)
//            observeEvent(
//                filterViewModel.openMedicalProviderScreen,
//                ::handleOpenMedicalProviderEvent
//            )
//            observeEvent(filterViewModel.openReviewsScreen, ::handleOpenReviewsEvent)
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

    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>) {

        if (salamtakListResponse.related!! && salamtakListResponse.data.isNotEmpty())
            tv_suggested.toVisible()
        else
            tv_suggested.toGone()

        var operations = salamtakListResponse!!.data!!

        if (filterViewModel.page > 1) {
            tv_remaining_records.text = getRemaining(
                filterViewModel.operationsResponse.value!!.totalRecords,
                filterViewModel.page
            )
            operationsAdapter.addData(operations!!)
        } else if (!(operations.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                filterViewModel.operationsResponse.value!!.totalRecords,
                filterViewModel.page
            )
            operationsAdapter.setList(operations.toMutableList())
            rv_operations_list.adapter = operationsAdapter

            rv_operations_list.addOnScrollListener(object :
                PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return filterViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return filterViewModel.isLoading
                }

                override fun loadMoreItems() {
                    filterViewModel.page++
                    filterViewModel.isLoading = true
                    filterViewModel.findResults()
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }
        filterViewModel.isLoading = false
    }


//    private fun handleSearchResponse(categoryDetailsModel: Resource<SalamtakListResponse<OperationN>>) {
//        when (categoryDetailsModel) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.Success -> categoryDetailsModel.data?.let { bindListData(it.data!!) }
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                showDataView(false)
//                categoryDetailsModel.errorCode?.let {
//                    var error = filterViewModel.getToastMessage(it)
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
