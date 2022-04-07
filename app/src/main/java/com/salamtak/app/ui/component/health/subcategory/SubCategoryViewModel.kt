package com.salamtak.app.ui.component.health.subcategory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.data.entities.SubCategory
import com.salamtak.app.data.entities.SubCategoryModel
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
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
class SubCategoryViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false
    var isSearch = false

    val subCategories: MutableLiveData<List<SubCategory>> = MutableLiveData()
    val subCategoriesResponse: MutableLiveData<SalamtakListResponse<SubCategory>> = MutableLiveData()

    var categoryId = ""

    val newSubCategories= MutableLiveData<List<SubCategoryModel>>()
    val newSubCategoriesResponse: MutableLiveData<SalamtakListResponse<SubCategoryModel>> = MutableLiveData()
    val addToFavouriteLiveData = MutableLiveData<ActionResponse>()


    fun shouldAddFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun getSubCategories() {
        if (page == 1 || page <= subCategoriesResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getSubCategories(categoryId, page, Constants.PAGE_SIZE)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            subCategoriesResponse.value = it
                            it.data?.let { list ->
                                subCategories.value = list
                            }

                        }
                    }
                    is Resource.NetworkError -> {
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
        } else
            isLastPage = true

    }

    fun getNewSubCategories(categoryId: Int) {
        if (page == 1 || page <= newSubCategoriesResponse.value?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getNewSubCategories(categoryId, page, Constants.PAGE_SIZE)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            newSubCategoriesResponse.value = it
                            it.data?.let { list ->
                                newSubCategories.value = list
                            }

                        }
                    }
                    is Resource.NetworkError -> {
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
        } else
            isLastPage = true

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