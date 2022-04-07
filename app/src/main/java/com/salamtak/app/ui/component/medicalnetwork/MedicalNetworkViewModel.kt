package com.salamtak.app.ui.component.medicalnetwork

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.MedicalNetwork
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class MedicalNetworkViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val doctorsResponse = MutableLiveData<SalamtakListResponse<MedicalNetwork>>()
    val providerResponse = MutableLiveData<SalamtakListResponse<MedicalNetwork>>()
    val openDoctorOperationsScreen = MutableLiveData<MedicalNetwork>()
    val openMedicalProviderScreen = MutableLiveData<MedicalNetwork>()

    var isDoctor = true

    fun getDoctors() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getDoctors(0, 1000)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        doctorsResponse.value = it
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

    fun getMedicalProviders() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getMedicalProviders(0, 1000)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        providerResponse.value = it
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

    fun openDoctorOperations(item: MedicalNetwork) {
        if (isDoctor)
            openDoctorOperationsScreen.value = item
        else
            openMedicalProviderScreen.value = item
    }
}