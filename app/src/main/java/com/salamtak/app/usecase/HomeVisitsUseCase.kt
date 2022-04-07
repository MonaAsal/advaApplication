package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
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

class HomeVisitsUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val _homeVisitsMutableLiveData = MutableLiveData<Resource<HomeVisitsResponse>>()
    val homeVisitsMutableLiveData: MutableLiveData<Resource<HomeVisitsResponse>> =
        _homeVisitsMutableLiveData

    private val _homeVisitMutableLiveData = MutableLiveData<Resource<HomeVisitResponse>>()
    val homeVisitMutableLiveData: MutableLiveData<Resource<HomeVisitResponse>> =
        _homeVisitMutableLiveData

    private val _categoryDetailsMutableLiveData =
        MutableLiveData<Resource<SalamtakListResponse<Operation>>>()
    val categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<Operation>>> =
        _categoryDetailsMutableLiveData

    private val _cancelVisitResponseMutableLiveData = MutableLiveData<Resource<ActionResponse>>()
    val cancelVisitResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
        _cancelVisitResponseMutableLiveData

    private val _reviewAddedResponseMutableLiveData = MutableLiveData<Resource<ActionResponse>>()
    val reviewAddedResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
        _reviewAddedResponseMutableLiveData

    private val _addVisitResponseMutableLiveData = MutableLiveData<Resource<AddHomeVisitResponse>>()
    val addVisitResponseMutableLiveData: MutableLiveData<Resource<AddHomeVisitResponse>> =
        _addVisitResponseMutableLiveData

    private val _preferredTimeResponseMutableLiveData =
        MutableLiveData<Resource<PreferredTimesResponse>>()
    val preferredTimeResponseMutableLiveData: MutableLiveData<Resource<PreferredTimesResponse>> =
        _preferredTimeResponseMutableLiveData


    private val _drSpecializationResponseMutableLiveData =
        MutableLiveData<Resource<DoctorSpecializationsResponse>>()
    val drSpecializationResponseMutableLiveData =
        _drSpecializationResponseMutableLiveData


    private val _reviewResponse =
        MutableLiveData<Resource<ActionResponse>>()
    val reviewResponse =
        _reviewResponse

    fun getHomeVisit(requestId: String) {
        var serviceResponse: Resource<HomeVisitResponse>?
        _homeVisitMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getHomeVisit(requestId)
                _homeVisitMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _homeVisitMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun getMyHomeVisits(page: Int, pageSize: Int) {
        var serviceResponse: Resource<HomeVisitsResponse>?
        _homeVisitsMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getMyHomeVisits(page, pageSize)
                _homeVisitsMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _homeVisitsMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun cancelHomeVisit(id: String) {
        var serviceResponse: Resource<ActionResponse>?
        _cancelVisitResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.cancelHomeVisit(id)
                _cancelVisitResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _cancelVisitResponseMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun addHomeVisit(
        DoctorSpecializationId: Int,
        PreferredTimeId: Int,
        IsForYou: Int, Name: String, Age: String,
        Notes: String, ContactNumber: String,
        StreetAddress: String, BuildingNum: String,
        AppartmentNumber: String, CityId: Int,
        AreaId: String, Lat: Double,
        Lng: Double, PaymentMethodId: Int, CardId: String
    ) {
        var serviceResponse: Resource<AddHomeVisitResponse>?
        _addVisitResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.addHomeVisit(
                    DoctorSpecializationId,
                    PreferredTimeId,
                    IsForYou,
                    Name,
                    Age,
                    Notes,
                    ContactNumber,
                    StreetAddress,
                    BuildingNum,
                    AppartmentNumber,
                    CityId,
                    AreaId,
                    Lat,
                    Lng,
                    PaymentMethodId,
                    CardId
                )
                _addVisitResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _addVisitResponseMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun getPreferredTime() {
        var serviceResponse: Resource<PreferredTimesResponse>?
        _preferredTimeResponseMutableLiveData.postValue(Resource.Loading())
        showLoadingLiveData.postValue(true)
        launch {
            try {
                serviceResponse = dataRepository.getPreferredTimes()
                showLoadingLiveData.postValue(false)
                _preferredTimeResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                showLoadingLiveData.postValue(false)
                _preferredTimeResponseMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }

    }

    fun getDoctorSpecializations() {
        var serviceResponse: Resource<DoctorSpecializationsResponse>?
        _drSpecializationResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getDoctorSpecializations()
                _drSpecializationResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _drSpecializationResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun addHomeVisitReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        review: String
    ) {
        var serviceResponse: Resource<ActionResponse>?
        _reviewResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.addHomeVisitReview(requestId, experienceRate, doctorRate, review)
                _reviewResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _reviewResponse.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }


}
