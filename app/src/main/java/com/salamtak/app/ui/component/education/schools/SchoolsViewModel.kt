package com.salamtak.app.ui.component.education.schools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.EducationSubcategoriesData
import com.salamtak.app.data.entities.School
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
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
class SchoolsViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val categoriesLiveData = MutableLiveData<SalamtakListResponse<Category>>()
    var page = 1
    var isLastPage = false
    var isLoading = false

    var categoryId = 0
    var subCategoryId = 0
//    val openCategoryDetails: LiveData<Event<Category>> get() = openCategoryDetailsPrivate

    fun getCategories(categoryId: Int) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCategories(
                    categoryId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        categoriesLiveData.value = it
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

    val educationByCategory =
        MutableLiveData<SalamtakObjectListResponse<EducationSubcategoriesData>>()

    fun getAllEducationByCategoryId(
        categoryId: Int
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getAllEducationByCategoryId(categoryId, page, 100)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        educationByCategory.value = it
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

    val schoolsResponse = MutableLiveData<SalamtakListResponse<School>>()
    fun getAllEducationBySubCategoryId(
    ) {
//        if (page == 1 || page <= schoolsResponse.value!!?.totalPages!!) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getAllEducationBySubCategoryId(
                    subCategoryId, page, 100//page, pageSize
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        schoolsResponse.value = it
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
//            }
        }
    }


}
