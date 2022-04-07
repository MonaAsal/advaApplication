package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.OfferHistory
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class OffersUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val _categoriesMutableLiveData = MutableLiveData<Resource<SalamtakListResponse<Category>>>()
    val categoriesLiveData: MutableLiveData<Resource<SalamtakListResponse<Category>>> =
        _categoriesMutableLiveData

    val _offersResponse = MutableLiveData<Resource<OffersResponse>>()
    val offersResponse = _offersResponse

    val _offersUsageResponse = MutableLiveData<Resource<SalamtakListResponse<OfferHistory>>>()
    val offersUsageResponse = _offersUsageResponse

    val _providerResponse = MutableLiveData<Resource<OffersProvidersResponse>>()
    val providerResponse = _providerResponse

    val _qrCodeResponse = MutableLiveData<Resource<SalamtakResponse<QrData>>>()
    val qrCodeResponse = _qrCodeResponse

    private val _categoryDetailsMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<Operation>>>()
    val categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<Operation>>> =
        _categoryDetailsMutableLiveData


    fun getCategories() {
        var serviceResponse: Resource<SalamtakListResponse<Category>>?
        _categoriesMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.requestCategories()
                _categoriesMutableLiveData.postValue(serviceResponse)
                dataRepository.saveCategories(serviceResponse?.data?.data!!)
            } catch (e: Exception) {
                _categoriesMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }


    fun getOffersByProvider(
        providerId: String, page: Int, pageSize: Int
    ) {
        var serviceResponse: Resource<OffersResponse>?
        _offersResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getOffersByProvider(providerId, page, pageSize)
                _offersResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _offersResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun getOffersUsage(page: Int, pageSize: Int) {
        var serviceResponse: Resource<SalamtakListResponse<OfferHistory>>?
        _offersUsageResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getOffersUsage(page, pageSize)
                _offersUsageResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _offersUsageResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun getOffersProviders(page: Int, pageSize: Int) {
        var serviceResponse: Resource<OffersProvidersResponse>?
        _providerResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getOffersProviders(page, pageSize)
                _providerResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _providerResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }

    }

    fun getQrCode(id: String) {
        var serviceResponse: Resource<SalamtakResponse<QrData>>?
        _qrCodeResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getQrCode(id)
                _qrCodeResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _qrCodeResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }


}
