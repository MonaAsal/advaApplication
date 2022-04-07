package com.salamtak.app.ui.component.education.categories


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
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
class EducationCategoriesViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val categoriesLiveData= MutableLiveData<SalamtakListResponse<Category>>()

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


}
