package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.ChronicDiseaseInput
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.ChronicDiseasesResponse
import com.salamtak.app.data.entities.responses.MedicalProfileResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class MedicalProfileUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val _medicalProfileMutableLiveData =
        MutableLiveData<Resource<MedicalProfileResponse>>()
    val medicalProfileLiveData: MutableLiveData<Resource<MedicalProfileResponse>> =
        _medicalProfileMutableLiveData


    private val _postDiseasesResponseMutableLiveData =
        MutableLiveData<Resource<ChronicDiseasesResponse>>()
    val postDiseasesResponseMutableLiveData: MutableLiveData<Resource<ChronicDiseasesResponse>> =
        _postDiseasesResponseMutableLiveData

    private val _deleteDiseasesResponseMutableLiveData =
        MutableLiveData<Resource<SalamtakResponse<BaseResponse>>>()
    val deleteDiseasesResponseMutableLiveData: MutableLiveData<Resource<SalamtakResponse<BaseResponse>>> =
        _deleteDiseasesResponseMutableLiveData

    fun postMedicalProfile(
        PatientBloodTypeId: Int,
        Shareable: Int,
        DateOfBirth: String,
        Weight: String,
        Height: String
    ) {
        var serviceResponse: Resource<MedicalProfileResponse>?
        _medicalProfileMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.postMedicalProfile(
                    PatientBloodTypeId,
                    Shareable,
                    DateOfBirth,
                    Weight,
                    Height
                )
                _medicalProfileMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _medicalProfileMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }



    fun getMedicalProfile() {
        var serviceResponse: Resource<MedicalProfileResponse>?
        _medicalProfileMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getMedicalProfile(
                )
                _medicalProfileMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _medicalProfileMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun addDiseases(chronicDiseases: List<ChronicDiseaseInput>)
    {
        var serviceResponse: Resource<ChronicDiseasesResponse>?
        _postDiseasesResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.postDiseases(chronicDiseases)
                _postDiseasesResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _postDiseasesResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

}
