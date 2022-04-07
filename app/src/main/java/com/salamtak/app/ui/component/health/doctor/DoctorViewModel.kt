package com.salamtak.app.ui.component.health.doctor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Contact
import com.salamtak.app.data.entities.DoctorBase
import com.salamtak.app.data.entities.DoctorDetails
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.ActionResponse
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
class DoctorViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel()
   {
       val addToFavouriteLiveData = MutableLiveData<ActionResponse>()

       override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var doctorInfo = MutableLiveData<DoctorDetails>()
    val callNumber = MutableLiveData<String>()
    val openLink = MutableLiveData<String>()
    val sendEmail = MutableLiveData<String>()

    var doctorId =""

    var page = 1
    var isLastPage = false
    var isLoading = false

    var operationsResponse = MutableLiveData<SalamtakListResponse<Operation>>()

//    val reviewsResponse = operationsUseCase.reviewsResponse

//    val providerOperations =
//        operationsUseCase.operationsResponse

    val openDoctorOperationsScreen = MutableLiveData<DoctorBase>()
    val openMedicalProviderScreen = MutableLiveData<Event<String>>()
    fun getDoctorInfo(doctorId: String) {
//        if (page == 1 || page <= doctorInfo.value!!.totalPages!!) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getDoctorInfo(doctorId, 0, Constants.PAGE_SIZE)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        doctorInfo.value = it
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
//            }
        }
    }


    fun getDoctorOperations() {
        if (page == 1 || page <= operationsResponse.value!!.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getDoctorOperations(
                        doctorId,
                        page,
                        Constants.PAGE_SIZE, "true", null
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsResponse.value = it
                        }
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                    showServerError.postValue(it)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        }
    }

    fun onContactClicked(contact: Contact) {
        when (contact.contactType) {
            1 -> {
                callNumber.value = contact.contact
            }
            2 -> {
                sendEmail.value = contact.contact
            }
            4, 5 -> {
                openLink.value = contact.contact
            }
        }

    }

    fun getDoctorReviews(doctorId: String) {
//        operationsUseCase.getDoctorReviews(doctorId, 0, Constants.PAGE_SIZE)
    }

    fun onMedicalProviderClicked(id: String) {
        openMedicalProviderScreen.value = Event(id)

    }

    fun openDoctorOperations(item: DoctorBase) {
        openDoctorOperationsScreen.value = item
    }

//    fun getMedicalProviderOperations(
//        providerId: String
//    ) {
//        return operationsUseCase.getMedicalProviderOperations(providerId, 0, Constants.PAGE_SIZE)
//    }

    fun getLocale(): String {
        return dataRepository.getLocale()
    }

       fun addToFavourite(id: String) {
           viewModelScope.launch {
               showLoading.postValue(true)
               when (val resource =
                   dataRepository.postAddToFavourite(id)) {
                   is Resource.Success -> {
                       showLoading.postValue(false)
                       resource.data?.let {
                           addToFavouriteLiveData.value = it
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

   }
