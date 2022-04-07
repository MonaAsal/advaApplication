package com.salamtak.app.ui.component.bookingrequests

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.DoctorBase
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class BookedRequestsViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())


    var page = 1
    var isLastPage = false
    var isLoading = false

    var bookedOperationsLiveData = MutableLiveData<SalamtakListResponse<BookedOperation>>()

    val openPaymentScreen: MutableLiveData<BookedOperation> = MutableLiveData()
    val openOperationDetails: MutableLiveData<BookedOperation> = MutableLiveData()
    val openCancelOperation: MutableLiveData<BookedOperation> = MutableLiveData()
    val openMakeReview: MutableLiveData<BookedOperation> = MutableLiveData()
    var reviewAddedResponseLiveData = MutableLiveData<Resource<ActionResponse>>()
    var cancelOperationResponseLiveData = MutableLiveData<ActionResponse>()
    var canceledItemId = MutableLiveData<String>()
//    val reviewResponse = useCase.reviewResponse

    val notificationOperation = MutableLiveData<BookedOperation>()
    private val _openDoctorScreen = MutableLiveData<Event<String>>()
    val openDoctorScreen = _openDoctorScreen

    fun onDoctorClicked(item: DoctorBase) {
        _openDoctorScreen.value = Event(item.id)
    }

    fun getMyBookedOperations() {
        if (page == 1 || page <= bookedOperationsLiveData.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.getBookedOperation(page, Constants.PAGE_SIZE)) {
                    is Resource.Success -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.data?.let {
                            bookedOperationsLiveData.value = it
                        }
                    }
                    is Resource.NetworkError -> {
                        isLoading = false
                        showLoading.postValue(false)
//                        showError.postValue(showError)
                    }

                    is Resource.DataError -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        }
    }

    fun onOperationItemClicked(bookedOperation: BookedOperation?) {
        if (bookedOperation != null) {
            if (bookedOperation.currentStatus == 0)
                openCancelOperation.value = bookedOperation
            else
                openOperationDetails.value = bookedOperation
        }
    }


    fun cancelOperation(bookedOperation: BookedOperation) {
        if (bookedOperation.currentStatus == 0 || bookedOperation.currentStatus == 2) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.cancelOperation(bookedOperation.id)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            //cancelOperation.value = it
                            cancelOperationResponseLiveData.value = it
                        }
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                    }
                }
            }

        } else
            showToastMessage(App.context.getString(R.string.home_visit_cannot_be_canceled))
    }


    fun onMoreClicked(bookedOperation: BookedOperation) {
        if (bookedOperation.currentStatus == 0 || bookedOperation.currentStatus == 2)
            openCancelOperation.value = bookedOperation
        else
            openOperationDetails.value = bookedOperation
    }

    fun onReviewOperationClicked(item: BookedOperation) {
        openMakeReview.value = item
    }


    fun addOperationReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        providerRate: Int,
        review: String
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addBookingReview(
                    requestId,
                    experienceRate,
                    doctorRate,
                    providerRate,
                    review
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {

                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                }
            }
        }

    }

    fun getOperationById(id: String) {
        bookedOperationsLiveData.value?.let {

             notificationOperation.value = it.data?.filter { it.id == id }?.get(0)
             openOperationDetails.value = notificationOperation.value!!

        }
        Log.d("handleOperationsResponseyasmen2",bookedOperationsLiveData.value?.data.toString())
    }

    fun onPayClicked(item: BookedOperation) {
        openPaymentScreen.value = item
    }


}
