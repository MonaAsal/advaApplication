package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.City
import com.salamtak.app.data.entities.MedicalProfileLookupsResponse
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.ProfileLookupsResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class LookupsUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val _categoriesMutableLiveData = MutableLiveData<Resource<SalamtakListResponse<Category>>>()
    val categoriesLiveData: MutableLiveData<Resource<SalamtakListResponse<Category>>> =
        _categoriesMutableLiveData

    private val _categoryDetailsMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<Operation>>>()
    val categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<Operation>>> =
        _categoryDetailsMutableLiveData

    private val _citiesMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<City>>>()
    val citiesLiveData: MutableLiveData<Resource<SalamtakListResponse<City>>> =
        _citiesMutableLiveData


    private val _medicalLookupsMutableLiveData =
        MutableLiveData<Resource<MedicalProfileLookupsResponse>>()
    val medicalLookupsLiveData: MutableLiveData<Resource<MedicalProfileLookupsResponse>> =
        _medicalLookupsMutableLiveData

    private val _profileLookupsMutableLiveData =
        MutableLiveData<Resource<ProfileLookupsResponse>>()
    val profileLookupsLiveData: MutableLiveData<Resource<ProfileLookupsResponse>> =
        _profileLookupsMutableLiveData


    fun getCategories(): List<Category> {
        return dataRepository.getCategories()!!
    }

    fun getCities() {
        var serviceResponse: Resource<SalamtakListResponse<City>>?
        launch {
            try {
                serviceResponse =
                    dataRepository.requestCities()
                _citiesMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _citiesMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun fetchMedicalLookups() {
        var serviceResponse: Resource<MedicalProfileLookupsResponse>?
        launch {
            try {
                serviceResponse =
                    dataRepository.fetchMedicalLookups()
                _medicalLookupsMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _medicalLookupsMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun fetchProfileLookups() {
        var serviceResponse: Resource<ProfileLookupsResponse>?
        launch {
            try {
                serviceResponse =
                    dataRepository.fetchProfileLookups()
                _profileLookupsMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _profileLookupsMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }
}
