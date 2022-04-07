package com.salamtak.app.ui.component.carservices.providers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.carservices.CarBaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class CarServicesViewModel @Inject constructor(dataRepository: DataRepository) :
    CarBaseViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false
    var categoryId = MainCategories.CARS.typeId

    var query: String? = null

    val providersResponse = MutableLiveData<SalamtakListResponse<CarCategoryModel>>()
    val CarCategoriesList= MutableLiveData<List<CarCategoryModel>>()
    val carBrandsLiveData = MutableLiveData<SalamtakListResponse<CarModel>>()
    val citiesLiveData = MutableLiveData<SalamtakListResponse<City>>()

    val selectedCarBrands = MutableLiveData<List<CarModel>>()
    val selectedCarBrandsTmp = MutableLiveData<List<CarModel>>()
    val selectedCarServices = MutableLiveData<List<Category>>()
    val selectedCarServicesTmp = MutableLiveData<List<Category>>()
    val selectedCarAreasTmp = MutableLiveData<List<Area>>()
    val selectedCity = MutableLiveData<City?>()
    val selectedArea = MutableLiveData<List<Area>>()


    fun createInputObject(): GetCarServiceInput {
//        return GetCarServiceInput(100, page, null, null, null)

        var services = selectedCarServices.value?.let {
                services ->
            services.map {
                it.id
            }
        } ?: listOf()

        var brands = selectedCarBrands.value?.let { brands ->
            brands.map { it.id }
        } ?: listOf()

        // @Yasmine, u should put ur selected areas values here
        // please make sure it's correct
        var locations = selectedArea.value.let {
            areasList ->  areasList?.map { it.id } }?: listOf()

//        var locations = selectedCity.value?.let { city ->
//            city?.areas?.let { areas ->
//                areas?.let { it.filter { it.isSelected }.map { it.id } } ?: listOf()
//            } ?: listOf()
//        } ?: listOf()

        return GetCarServiceInput(
            Constants.PAGE_SIZE, page, brands, services, locations, query
        )
    }
    fun loadMorePages() {
        getCarServicesProviders()
    }


    fun getCarServicesProviders(
    ) {
        var input = createInputObject()
        Log.e("input", Gson().toJson(input))
        LogUtil.LogFirebaseEvent(
            "GetCarServiceCenter",
            "CarServicesWithFilter_Android",
            "input",
            Gson().toJson(input)
        )
        if (page == 1 || page <= providersResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.getCarServiceCenters(
                        input
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            providersResponse.value = it
                            it?.data?.let { list ->
                                CarCategoriesList.value = list
                            }
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

    fun getServices(categoryId: Int) {
        if (categoriesLiveData.value == null || categoriesLiveData.value!!.data.isEmpty()) {
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
        else
            categoriesLiveData.value!!.data!!.map { it.isSelected = false }
    }

    fun getCarBrands() {
        if (carBrandsLiveData.value == null || carBrandsLiveData.value!!.data.isEmpty()) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.getCarBrands(
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            carBrandsLiveData.value = it
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
        } else
            carBrandsLiveData.value!!.data!!.map { it.isSelected = false }

    }

    fun getCitiesForCarFilter() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCitiesForCarFilter(
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        citiesLiveData.value = it
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

    fun saveSelectedCity(city: City?) {
        city?.let {
            dataRepository.saveSelectedCity(city)
        }
    }

    fun getSelectedCity(): City? {
        return dataRepository.getSelectedCity()
    }
}