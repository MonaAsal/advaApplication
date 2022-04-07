package com.salamtak.app.ui.component.health.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.health.BaseOperationViewModelN
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseOperationViewModelN(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var categoryDetailsLiveData = MutableLiveData<Resource<SalamtakListResponse<Operation>>>()
//    var searchResult = MutableLiveData<SalamtakListResponse<OperationN>>()
    var categoryItem: MutableLiveData<Category> = MutableLiveData()
    var areaItem: MutableLiveData<Area> = MutableLiveData()

    var query: MutableLiveData<String> = MutableLiveData()

    var page = 1
    var isLastPage = false
    var isLoading = false
    val operations: MutableLiveData<List<Operation>> = MutableLiveData()



    fun searchOperations(query: String) {
        searchOperations(
            query,
            if (categoryItem!!.value != null) categoryItem!!.value!!.id.toString() else "",
            ""
        )
    }

    fun filter(queryString: String, category: Category?, area: Area?) {
        query.value = queryString
        categoryItem.value = category
        areaItem.value = area
        searchOperations(
            queryString,
            category?.id?.toString() ?: "",
//            "1003"
            area?.id?.toString() ?: ""
        )
    }

    fun searchHealth(
        query: String
    ) {
        if (page == 1 || page <= operationsResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.searchHealth(query, page, Constants.PAGE_SIZE)) {
                    is Resource.Success -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsResponse.value = it
                            operations.value = it.data

                        }
                    }
                    is Resource.NetworkError -> {
                        isLoading = false
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
        }
    }



}