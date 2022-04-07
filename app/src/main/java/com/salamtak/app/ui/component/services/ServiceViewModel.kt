package com.salamtak.app.ui.component.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class ServiceViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val servicesCategoriesResponse = MutableLiveData<ServicesCategoriesResponse>()


    fun getServicesCategories() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getServicesCategories()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        servicesCategoriesResponse.value = it.data!!
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
