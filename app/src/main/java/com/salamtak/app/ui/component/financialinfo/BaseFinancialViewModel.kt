package com.salamtak.app.ui.component.financialinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseFinancialViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())
    val financialProgressResponse = MutableLiveData<BaseResponse>()

    fun getFinancialProgress() { //showProgress: Boolean
        viewModelScope.launch {
//            if (showProgress)
//                showLoading.postValue(true)
            when (val resource =
                dataRepository.getFinancialProgress()) {
                is Resource.Success -> {
//                    if (showProgress)
//                        showLoading.postValue(false)
                    financialProgressResponse.value = resource.data!!
                }

                is Resource.NetworkError -> {
//                    if (showProgress)
//                        showLoading.postValue(false)
                }

                is Resource.DataError -> {
//                    if (showProgress)
//                        showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                }
            }
        }
    }

}