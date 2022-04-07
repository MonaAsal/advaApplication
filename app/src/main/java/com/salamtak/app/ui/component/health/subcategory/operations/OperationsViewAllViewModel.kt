package com.salamtak.app.ui.component.health.subcategory.operations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Operation
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
class OperationsViewAllViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var isSearch = false
    var page1 = 1
    var isLastPage = false
    var isLoading = false
    var subcategoryId = 0
    val operationsvResponse = MutableLiveData<SalamtakListResponse<Operation>>()
    val operationsList = MutableLiveData<List<Operation>>()
    val addToFavouriteLiveData = MutableLiveData<ActionResponse>()


    fun getSubCategoryOperations(subcategoryId: Int) {
        if (page1 == 1 || page1 <= operationsvResponse.value?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getSubCategoryOperations(
                        subcategoryId,
                        page1,
                        Constants.PAGE_SIZE
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsvResponse.value = it
                            operationsList.value = it.data

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
        }else
            isLastPage = true
    }

    fun loadMorePages(subcategoryId: Int) {
        getSubCategoryOperations(subcategoryId)
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