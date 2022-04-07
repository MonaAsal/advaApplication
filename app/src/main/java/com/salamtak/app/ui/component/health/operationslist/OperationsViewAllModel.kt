//package com.salamtak.app.ui.component.health.operationslist
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//
//import com.salamtak.app.data.DataRepository
//import com.salamtak.app.data.Resource
//import com.salamtak.app.data.entities.MedicalProvider
//import com.salamtak.app.data.entities.Operation
//import com.salamtak.app.data.entities.responses.SalamtakListResponse
//import com.salamtak.app.data.enums.OperationsFrom
//import com.salamtak.app.data.error.mapper.ErrorMapper
//import com.salamtak.app.ui.common.BaseViewModel
//import com.salamtak.app.usecase.errors.ErrorManager
//import com.salamtak.app.utils.Constants
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
///**
// * Created by Radwa Elsahn on 2/16/2021
// */
//
//@HiltViewModel
//class OperationsViewAllModel @Inject
//constructor(private val dataRepository: DataRepository) : BaseViewModel() {
//    override val errorManager: ErrorManager
//        get() = ErrorManager(ErrorMapper())
//
//    var isSearch = false
//    var page = 1
//    var isLastPage = false
//    var isLoading = false
//    var id = ""
//    var subcategoryId = 0
//    var categoryId = 0
//    var from: Int = 1
//    var operationsvResponse = MutableLiveData<SalamtakListResponse<Operation>>()
//    val operationsList = MutableLiveData<List<Operation>>()
//
//
//    fun loadData() {
//        when (from) {
//            OperationsFrom.Subcategories.value ->
//                getSubCategoryOperations(subcategoryId)
//
//            OperationsFrom.MedicalProvider.value ->
//                getMedicalProviderOperations(id)
//
//        }
//    }
//
//
//    fun getSubCategoryOperations(subcategoryId: Int) {
//        if (page == 1 || page <= operationsvResponse.value?.totalPages!!) {
//            viewModelScope.launch {
//                showLoading.postValue(true)
//                isLoading = true
//                when (val resource =
//                    dataRepository.getSubCategoryOperations(
//                        subcategoryId,
//                        page,
//                        Constants.PAGE_SIZE
//                    )) {
//                    is Resource.Success -> {
//                        showLoading.postValue(false)
//                        resource.data?.let {
//                            operationsvResponse.value = it
//                            operationsList.value = it.data
//
//                        }
//                    }
//                    is Resource.NetworkError -> {
//                        showLoading.postValue(false)
////                    showServerError.postValue(it)
//                    }
//
//                    is Resource.DataError -> {
//                        showLoading.postValue(false)
//
//                        resource.errorResponse?.let { showServerError.postValue(it) }
//                        //showError.postValue(Event(resource.errorResponse!!.message!!))
//                        // error ="cannot create the live liveSession"
//                    }
//                }
//            }
//        }else
//            isLastPage = true
//    }
//
//    fun getMedicalProviderOperations(
//        providerId: String
//    ) {
//        if (page == 1 || page <= operationsvResponse.value?.totalPages!!) {
//            viewModelScope.launch {
//                showLoading.postValue(true)
//                when (val resource =
//                    dataRepository.getMedicalProviderOperations(
//                        providerId,
//                        page,
//                        Constants.PAGE_SIZE,
//                        if (categoryId != 0) categoryId.toString() else null
//                    )) {
//                    is Resource.Success -> {
//                        showLoading.postValue(false)
//                        resource.data?.let {
//                            operationsvResponse.value = it
//                            operationsList.value = it.data
//
//                        }
//
//                    }
//                    is Resource.NetworkError -> {
//                        showLoading.postValue(false)
////                    showServerError.postValue(it)
//                    }
//
//                    is Resource.DataError -> {
//                        showLoading.postValue(false)
//                        resource.errorResponse?.let { showServerError.postValue(it) }
//                        //showError.postValue(Event(resource.errorResponse!!.message!!))
//                        // error ="cannot create the live liveSession"
//                    }
//                }
//            }
//        }
//        else
//            isLastPage = true
//
//    }
//
////    fun getFavourites() {
////        if (page == 1 || page <= favouritesResponse.value!!.totalPages!!) {
////            viewModelScope.launch {
////                showLoading.postValue(true)
////                when (val resource =
////                    dataRepository.getFavourites(page, Constants.PAGE_SIZE)) {
////                    is Resource.Success -> {
////                        showLoading.postValue(false)
////                        resource.data?.let {
////                            favouritesResponse.value = it
////
////                        }
////                    }
////                    is Resource.NetworkError -> {
////                        showLoading.postValue(false)
//////                    showServerError.postValue(it)
////                    }
////
////                    is Resource.DataError -> {
////                        showLoading.postValue(false)
////                        resource.errorResponse?.let { showServerError.postValue(it) }
////                        //showError.postValue(Event(resource.errorResponse!!.message!!))
////                        // error ="cannot create the live liveSession"
////                    }
////                }
////            }
////        }
////    }
//
//
//}