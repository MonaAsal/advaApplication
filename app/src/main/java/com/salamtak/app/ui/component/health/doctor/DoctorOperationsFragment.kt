package com.salamtak.app.ui.component.health.doctor

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment


import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.ui.component.medicalprovider.adapter.ProviderOperationsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_doctor_operations.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class DoctorOperationsFragment : BaseFragment() , OperationListenerN {
    override val layoutId: Int
        get() = R.layout.fragment_doctor_operations

    lateinit var doctor: DoctorDetails

    val doctorViewModel: DoctorViewModel by viewModels()

    lateinit var adapter: ProviderOperationsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctor = it.getParcelable(Constants.DOCTOR_ITEM_KEY)!!
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        doctorViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(DoctorViewModel::class.java)

        observeViewModel()

        doctorViewModel.doctorId = doctor.id
        doctorViewModel.getDoctorOperations()
        adapter = ProviderOperationsAdapter(false, this)

//        showOperations(doctor.operations!!)
    }

    private fun showDataView(show: Boolean) {
        view_no_operations.visibility = if (show) View.GONE else View.VISIBLE
        rv_operations_list.visibility = if (show) View.VISIBLE else View.GONE
    }

   override fun observeViewModel() {
//        observeEvent(operationsViewModel.openReviewsScreen, ::handleOpenReviewsEvent)
        observe(doctorViewModel.showLoading, ::showLoadingView)
        observe(doctorViewModel.showServerError, ::showServerErrorMessage)
        observe(doctorViewModel.operationsResponse, ::showOperations)
    }


    fun showLoadingView(show: Boolean) {
        //(activity as DoctorActivity).showLoadingView(show)
        try {
            (parentFragment as DoctorFragment).showLoadingView(show)
        }catch (e:Exception)
        {}

    }

    companion object {

        @JvmStatic
        fun newInstance(doctor: DoctorDetails) =//, operations: List<Operation>) =
            DoctorOperationsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.DOCTOR_ITEM_KEY, doctor)
                }
            }
    }


    private fun showOperations(salamtakListResponse: SalamtakListResponse<Operation>?) {
        var operations = salamtakListResponse!!.data!!

        if (salamtakListResponse.totalRecords > 0) {
            tv_remaining_records.text = getRemaining(
                doctorViewModel.operationsResponse.value!!.totalRecords,
                doctorViewModel.page, doctorViewModel.getLocale()
            )
            if (doctorViewModel.page > 1) {
                adapter.addData(operations!!)
            } else if (!(operations.isNullOrEmpty())) {
                adapter.setList(operations.toMutableList())
                rv_operations_list.adapter = adapter

                rv_operations_list.addOnScrollListener(object :
                    PaginationScrollListener(rv_operations_list.layoutManager as LinearLayoutManager) {
                    override fun isLastPage(): Boolean {
                        return doctorViewModel.isLastPage
                    }

                    override fun isLoading(): Boolean {
                        return doctorViewModel.isLoading
                    }

                    override fun loadMoreItems() {
                        doctorViewModel.page++
                        doctorViewModel.isLoading = true
                        doctorViewModel.getDoctorOperations()
                    }
                })
                showDataView(true)
            }
        } else {
            showDataView(false)
        }
        doctorViewModel.isLoading = false
    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {
       // (parentFragment as DoctorFragment).onFavouriteClicked(operation, position)

       // (activity as DoctorActivity).onFavouriteClicked(operation, position)
        doctorViewModel.addToFavourite(operation.id)

    }

    override fun onOwnerClicked(operation: Operation) {
       // (parentFragment as DoctorFragment).onOwnerClicked(operation)

     //   (activity as DoctorActivity).onOwnerClicked(operation)
        if (operation.owner!!.type == ProviderType.Doctor.typeId) {
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to operation.owner!!.id)
            navigateToDoctor(bundle)

        } else {
            val bundle = bundleOf(Constants.KEY_ID to operation.owner!!.id)
            navigateToMedicalProvider(bundle)
        }
    }

    override fun onDetailsClicked(operation: Operation) {
      //  (parentFragment as DoctorFragment).onDetailsClicked(operation)
        navigateToOperationBookingScreen(operation)
      //  (activity as DoctorActivity).onDetailsClicked(operation)
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
