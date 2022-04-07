package com.salamtak.app.ui.component.finishing.providers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinishingCategoryModel
import com.salamtak.app.data.entities.SubCategoryModel
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishingCategoriesViewModel@Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel()  {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false

    val finishingCategoriesList= MutableLiveData<List<FinishingCategoryModel>>()
    val finishingCategorieResponse: MutableLiveData<SalamtakListResponse<FinishingCategoryModel>> = MutableLiveData()

    fun getFinishingCategories() {
        if (page == 1 || page <= finishingCategorieResponse.value?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                isLoading = true
                when (val resource =
                    dataRepository.getFinishingCategories(page, Constants.PAGE_SIZE)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data.let {
                            finishingCategorieResponse.value = it
                            it?.data?.let { list ->
                                finishingCategoriesList.value = list
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

}