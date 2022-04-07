package com.salamtak.app.ui.component.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialStatus
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.error.Error
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.ProfileUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class ProfileActivityViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val logoutResponseLiveData = MutableLiveData<ActionResponse>()
    val financialResponse = MutableLiveData<BaseResponse>()
    val requestVerificationResponseMutableLiveData= MutableLiveData<ActionResponse>()


    var image = MutableLiveData<String>()

    fun getUserInfo(): User {
//        userInfo.postValue(sharedUseCase.getUser())
        return dataRepository!!.getUser()!!
    }

    fun requestVerifyNumber() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.requestVerifyNumber(Constants.TYPE_MAIL)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    requestVerificationResponseMutableLiveData.value = resource.data!!
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


    fun getFinancialProgress() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getFinancialProgress()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    financialResponse.value = resource.data!!
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

    fun logout() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource = dataRepository.logout()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    logoutResponseLiveData.value = resource.data!!
                    dataRepository.logoutLocal()
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
