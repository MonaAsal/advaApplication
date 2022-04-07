package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class CategoriesUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val _categoriesMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<Category>>>()
    val categoriesLiveData: MutableLiveData<Resource<SalamtakListResponse<Category>>> =
        _categoriesMutableLiveData


    private val _categoryDetailsMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<Operation>>>()
    val categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<Operation>>> =
        _categoryDetailsMutableLiveData


    fun getCategories(categoryId: Int) {
        var serviceResponse: Resource<SalamtakListResponse<Category>>?
        _categoriesMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getCategories(
                    categoryId
                )
                _categoriesMutableLiveData.postValue(serviceResponse!!)
                if (serviceResponse?.data != null)
                    dataRepository.saveCategories(serviceResponse?.data?.data!!)
            } catch (e: Exception) {
                _categoriesMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun searchByTitle(keyWord: String): Category? {
        val news = _categoriesMutableLiveData.value?.data?.data
        if (!news.isNullOrEmpty()) {
//            for (newsItem in news) {
//                if (newsItem.title.isNotEmpty() && newsItem.title.toLowerCase().contains(keyWord.toLowerCase())) {
//                    return newsItem
//                }
//            }
        }
        return null
    }


}
