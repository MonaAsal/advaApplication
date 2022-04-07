package com.salamtak.app.ui.component.finishing.packagedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinishingPackage
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
class FinishingPackageViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false

    val packageDetailsResponse = MutableLiveData<FinishingPackage>()
    fun getPackageDetails(id: String) {
//        if (page == 1 || page <= packageDetailsResponse.value!!?.totalPages!!) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getPackageDetails(
                    id
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        packageDetailsResponse.value = it
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
//        }
    }
}