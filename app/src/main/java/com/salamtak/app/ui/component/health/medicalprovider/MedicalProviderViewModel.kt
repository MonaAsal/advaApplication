package com.salamtak.app.ui.component.health.medicalprovider

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalProviderViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false


    var medicalProviderInfo = MutableLiveData<MedicalProviderDetails>()
    val operationsResponse = MutableLiveData<SalamtakListResponse<Operation>>()
    val operations = MutableLiveData<List<Operation>>()
//
//    val medicalProviderDoctors: MutableLiveData<Resource<DoctorsResponse>> =
//        operationsUseCase.medicalProviderDoctors
  val addToFavouriteLiveData = MutableLiveData<ActionResponse>()

    val openMap: MutableLiveData<Branch> = MutableLiveData<Branch>()
    val callNumber = MutableLiveData<String>()
    val openLink = MutableLiveData<String>()
    val sendEmail = MutableLiveData<String>()
//    val reviewsResponse = operationsUseCase.reviewsResponse

    fun getMedicalProviderInfo(providerId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getMedicalProviderInfo(providerId, page, Constants.PAGE_SIZE)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        medicalProviderInfo.value = it
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

    fun onLocationClicked(item: Branch) {
        openMap.value = item
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

    fun getProviderReviews(providerId: String) {
//        operationsUseCase.getProviderReviews(providerId, 0, Constants.PAGE_SIZE)
    }


    fun getLocale(): String {
        return dataRepository.getLocale()
    }

    fun getMedicalProviderOperations(
        providerId: String
    ) {
        if (page == 1 || page <= operationsResponse.value!!.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getMedicalProviderOperations(
                        providerId,
                        page,
                        Constants.PAGE_SIZE,
                        null
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsResponse.value = it
                            operations.value = it.data
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

    fun onStart(provider: String) {
        getMedicalProviderInfo(provider)
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
