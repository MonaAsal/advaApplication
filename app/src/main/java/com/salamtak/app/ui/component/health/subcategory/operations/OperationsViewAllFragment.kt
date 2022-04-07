package com.salamtak.app.ui.component.health.subcategory.operations

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.ui.component.health.adapter.OperationsAdapter
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_car_services_providers.*
import kotlinx.android.synthetic.main.activity_operations2.progress_bar
import kotlinx.android.synthetic.main.activity_operations2.rv_operations_list
import kotlinx.android.synthetic.main.activity_operations2.tv_remaining_records
import kotlinx.android.synthetic.main.activity_operations2.view_no_operations
import kotlinx.android.synthetic.main.fragment_operations_new.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

//view all
class OperationsViewAllFragment : BaseFragment(), OperationListenerN {

    override val layoutId: Int
        get() = R.layout.fragment_operations_new
    var subcategoryId=0

    val operationsViewModel: OperationsViewAllViewModel by viewModels()
    lateinit var operationsAdapter: OperationsAdapter
    lateinit var mLayoutManager :LinearLayoutManager
    var state: Parcelable? = null //to save recycler focus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           arguments?.let{
               subcategoryId =   it.getInt(Constants.KEY_ID, 0)
        }
        operationsViewModel.subcategoryId = subcategoryId
        operationsViewModel.page1 = 1
        operationsViewModel.getSubCategoryOperations(subcategoryId)
        operationsAdapter = OperationsAdapter(true, this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("state", "onActivityCreated")
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text =  arguments?.let {it?.getString(Constants.KEY_NAME)}
        mLayoutManager = LinearLayoutManager(context)
        restoreRecyclerPosition()
    }
    override fun onPause() {
        super.onPause()
        state = mLayoutManager.onSaveInstanceState()
    }

    fun restoreRecyclerPosition(){
        if(state != null) {
            mLayoutManager.onRestoreInstanceState(state);
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("operationviewall", "fragment")
            dataObservation()
            restoreRecyclerPosition()

    }

    private fun dataObservation() {
        Log.d("pagenumber viewall",operationsViewModel.page1.toString())
       operationsViewModel.page1 = 1

        with(operationsViewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(operationsList, ::showOperations)

        }
    }


    override fun observeViewModel() {
       /* with(operationsViewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(operationsList, ::showOperations)

        }*/
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("providers state","onViewStateRestored")
        //to save design when swip bwtween fragment
      /*  with(operationsViewModel){
            observe(operationsList, ::showOperations)
        }*/
    }


    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun showOperations(operations: List<Operation>) {
        activity?.let { hideKeyboard(it) }
        val providers = operations.toMutableList()
        Log.d("yasmendata",providers?.get(0)?.name.toString())
        val totalRecords =    operationsViewModel.operationsvResponse.value!!.totalRecords
        tv_remaining_records.text = getRemaining(
            operationsViewModel.operationsvResponse.value!!.totalRecords,
            operationsViewModel.page1
        )
        if (operationsViewModel.page1 > 1) {
            providers?.toMutableList()?.let { operationsAdapter.addData(it) }
        } else {
            if (!providers?.isNullOrEmpty()!!) {
                operationsAdapter.setList(providers)
                rv_operations_list.adapter = operationsAdapter
                rv_operations_list.addOnScrollListener(object :
                    PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                    override fun isLastPage(): Boolean {
                        return operationsViewModel.isLastPage
                    }

                    override fun isLoading(): Boolean {
                        return operationsViewModel.isLoading
                    }

                    override fun loadMoreItems() {
                        if(operationsViewModel.operationsvResponse.value!!.totalPages>operationsViewModel.page1) {
                            operationsViewModel.page1++
                            operationsViewModel.isLoading = true
                            operationsViewModel.loadMorePages(subcategoryId)
                        }
                    }
                })
                showDataView(true)
            } else
                showDataView(false)
        }
        operationsViewModel.isLoading = false
    }

    private fun showPovidersNum(providerNumbers: Int) {
        viewallprovidersnum.text = providerNumbers.toString()
    }

    fun showDataView(show: Boolean) {
        if (show) {
            rv_operations_list.toVisible()
            view_no_operations.toGone()
        } else {
            rv_operations_list.toGone()
            view_no_operations.toVisible()
        }

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