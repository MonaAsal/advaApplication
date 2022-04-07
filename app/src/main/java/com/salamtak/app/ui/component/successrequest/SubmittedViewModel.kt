package com.salamtak.app.ui.component.successrequest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialProfileCompleted
import com.salamtak.app.data.entities.User
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
class SubmittedViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    fun getUserName(): String {
        try {
            return dataRepository.getUser()!!.firstName + " " + dataRepository.getUser()!!.lastName
        } catch (e: Exception) {
            return ""
        }
    }

    fun getUser(): User? {
        return dataRepository.getUser()
    }

    fun isFinancialProfileCompleted() {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.isFinancialProfileCompleted()) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            financialProfileCompleted.value = it
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
