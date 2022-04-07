package com.salamtak.app.ui.component.health.categoryproviders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 2/16/2021
 */
@HiltViewModel
class ProvidersViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())


    var page = 1
    var subcategoryId =""
    var isLastPage = false
    var isLoading = false
    val categoryProvidersResponse: MutableLiveData<SalamtakListResponse<MedicalProvider>> =
        MutableLiveData()
    val moreProvidersResponse: MutableLiveData<SalamtakListResponse<MedicalProvider>> =
        MutableLiveData()

    val filterProvidersList: MutableLiveData<List<MedicalProvider>> =
        MutableLiveData()

    val providers = MutableLiveData<List<MedicalProvider>>()

    val openMedicalProviderProfile = MutableLiveData<MedicalProvider>()
    val openDoctorProfile = MutableLiveData<MedicalProvider>()

    var isSearch = false
    var categoryId = ""
    var providersNumbers = 0

    fun shouldAddFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun getProvidersNames(categoryId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            isLoading = true
            when (val resource =
                dataRepository.getProvidersNames(
                    categoryId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        filterProvidersList.value = it
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

    fun getCategoryProviders(categoryId: Int) {
        isSearch = false
        if (page == 1 || page <= categoryProvidersResponse.value?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getCategoryProviders(
                        categoryId,
                        page,
                        Constants.PAGE_SIZE,
                        null
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            categoryProvidersResponse.value = it
                            providers.value = it.data

                        }
                        resource.data?.totalRecords?.let {
                            providersNumbers=it
                        }
                        Log.d("providerscategories",resource.data.toString())
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                        showError.postValue(showError)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        } else
            isLastPage = true
    }

    fun searchProviders(categoryId: Int, filter: String) {
        isSearch = true
        if (page == 1 || page <= categoryProvidersResponse.value?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getCategoryProviders(
                        categoryId,
                        page,
                        Constants.PAGE_SIZE,
                        filter
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            categoryProvidersResponse.value = it
                            providers.value = it.data
                        }

                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                        showError.postValue(showError)
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
        else
            isLastPage = true
    }

    fun itemSelected(item: MedicalProvider) {
        if (item.type == ProviderType.MedicalProvider.typeId)
            openMedicalProviderProfile.value = item
        else
            openDoctorProfile.value = item
    }

    fun loadMorePages(categoryId: Int, filter: String) {
        if (isSearch.not() || filter.isEmpty())
            getCategoryProviders(categoryId)
        else
            searchProviders(categoryId, filter)
    }

    fun getSelectedItem(name: String) {
        var provider = filterProvidersList.value!!.filter { it.name == name }[0]
        itemSelected(provider)
    }

    fun getMoreDoctors()
    {
        if (page == 1 || page <= moreProvidersResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getMoreDoctors(
                        subcategoryId,
                        page,
                        Constants.PAGE_SIZE
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            moreProvidersResponse.value = it
                            providers.value = it.data

                        }
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                        showError.postValue(showError)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        } else
            isLastPage = true
    }

}