package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.ChronicDiseaseInput
import com.salamtak.app.data.entities.FinancialTypeAttachments
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.Error
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class ProfileUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    val logoutResponseLiveData = MutableLiveData<Resource<ActionResponse>>()
    val contactUsResponseLiveData = MutableLiveData<Resource<ContactUsResponse>>()

    private val _medicalProfileMutableLiveData =
        MutableLiveData<Resource<MedicalProfileResponse>>()
    val medicalProfileLiveData: MutableLiveData<Resource<MedicalProfileResponse>> =
        _medicalProfileMutableLiveData

    private val _employmentProfileResponseMutableLiveData =
        MutableLiveData<Resource<EmploymentProfileResponse>>()
    val employmentProfileLiveData: MutableLiveData<Resource<EmploymentProfileResponse>> =
        _employmentProfileResponseMutableLiveData


    private val _financialProfileResponseMutableLiveData =
        MutableLiveData<Resource<FinancialProfileResponse>>()
    val financialProfileLiveData: MutableLiveData<Resource<FinancialProfileResponse>> =
        _financialProfileResponseMutableLiveData

    private val _getFinancialProfileResponseMutableLiveData =
        MutableLiveData<Resource<FinancialProfileResponse>>()
    val getFinancialProfileLiveData: MutableLiveData<Resource<FinancialProfileResponse>> =
        _getFinancialProfileResponseMutableLiveData

    private val _getEmploymentProfileResponseMutableLiveData =
        MutableLiveData<Resource<EmploymentProfileResponse>>()
    val getEmploymentProfileLiveData: MutableLiveData<Resource<EmploymentProfileResponse>> =
        _getEmploymentProfileResponseMutableLiveData

    private val _getBasicProfileResponseMutableLiveData =
        MutableLiveData<Resource<BasicProfileInfoResponse>>()
    val getBasicProfileResponseMutableLiveData: MutableLiveData<Resource<BasicProfileInfoResponse>> =
        _getBasicProfileResponseMutableLiveData


    private val _updateBasicProfileResponseMutableLiveData =
        MutableLiveData<Resource<BasicProfileInfoResponse>>()
    val updateBasicProfileResponseMutableLiveData: MutableLiveData<Resource<BasicProfileInfoResponse>> =
        _updateBasicProfileResponseMutableLiveData

//    private val _getIDProfileResponseMutableLiveData =
//        MutableLiveData<Resource<FinancialProfileResponse>>()
//    val getFinancialProfileLiveData: MutableLiveData<Resource<FinancialProfileResponse>> =
//        _getFinancialProfileResponseMutableLiveData

    private val _financialLookupsResponseMutableLiveData =
        MutableLiveData<Resource<FinancialLookupsResponse>>()
    val financialLookupsLiveData: MutableLiveData<Resource<FinancialLookupsResponse>> =
        _financialLookupsResponseMutableLiveData


    private val _postDiseasesResponseMutableLiveData =
        MutableLiveData<Resource<ChronicDiseasesResponse>>()
    val postDiseasesResponseMutableLiveData: MutableLiveData<Resource<ChronicDiseasesResponse>> =
        _postDiseasesResponseMutableLiveData


    private val _getProfileInfoResponse =
        MutableLiveData<Resource<ProfileResponse>>()
    val getProfileInfoResponse: MutableLiveData<Resource<ProfileResponse>> =
        _getProfileInfoResponse

    private val _updateProfileResponse =
        MutableLiveData<Resource<UpdateProfileResponse>>()
    val updateProfileResponse: MutableLiveData<Resource<UpdateProfileResponse>> =
        _updateProfileResponse


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


    fun postFinancialProfile(
        NumberOfChildren: Int,
        CarManufactureYear: String,
        CarModelId: Int,
        ClubId: Int,
        BankInfoId: Int,
        MaritalStatusId: Int
    ) {
        var serviceResponse: Resource<FinancialProfileResponse>?
        _financialProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.postFinancialProfile(
                    NumberOfChildren,
                    CarManufactureYear,
                    CarModelId,
                    ClubId,
                    BankInfoId, MaritalStatusId
                )
                _financialProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _financialProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }


    fun postEmploymentProfile(
        Profession: String,
        CompanyName: String,
        HiringDate: String,
        NetMonthlyIncome: String,
        NetMonthlyExpense: String,
        EmploymentTypeId: Int,
        EmploymentFieldId: Int,
        StreetAddress: String,
        BuildingNum: Int,
        CityId: Int,
        AreaId: String
    ) {
        var serviceResponse: Resource<EmploymentProfileResponse>?
        _employmentProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.postEmploymentProfile(
                    Profession, CompanyName, HiringDate, NetMonthlyIncome, NetMonthlyExpense,
                    EmploymentTypeId, EmploymentFieldId, StreetAddress, BuildingNum, CityId, AreaId
                )
                _employmentProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _employmentProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
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

    fun getEmploymentProfile(
    ) {
        var serviceResponse: Resource<EmploymentProfileResponse>?
        _getEmploymentProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getEmploymentProfile()
                _getEmploymentProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _getEmploymentProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun getFinancialProfile() {
        var serviceResponse: Resource<FinancialProfileResponse>?
        _financialProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getFinancialProfile()
                _getFinancialProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _getFinancialProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun getFinancialLookups() {
        var serviceResponse: Resource<FinancialLookupsResponse>?
        _financialLookupsResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.getFinancialLookups()
                _financialLookupsResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _financialLookupsResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun postBasicProfile(
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        gender: Int,
        nationalId: String,
        nationalIdExpireDate: String,
        isEgyptian: Int,
        residentalStatusId: Int,
        otherResidentalStatus: String,
        passportNumber: String,
        passportExpireDate: String,
        streetAddress: String,
        buildingNum: String,
        cityId: String,
        areaId: String,
        image: String?
    ) {
        var serviceResponse: Resource<BasicProfileInfoResponse>?
        _updateBasicProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.postBasicProfile(
                        firstName,
                        secondName,
                        thirdName,
                        lastName,
                        gender,
                        nationalId,
                        nationalIdExpireDate,
                        isEgyptian,
                        residentalStatusId,
                        otherResidentalStatus,
                        passportNumber,
                        passportExpireDate,
                        streetAddress,
                        buildingNum,
                        cityId,
                        areaId, image
                    )
                if (serviceResponse!!.data != null) {
                    //dataRepository.saveUserProfile(serviceResponse!!.data!!.data!!)

                }
                _updateBasicProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                _updateBasicProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun getBasicProfile() {
        var serviceResponse: Resource<BasicProfileInfoResponse>?
        _getBasicProfileResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.getBasicProfile()
                _getBasicProfileResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _getBasicProfileResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun addDiseases(chronicDiseases: List<ChronicDiseaseInput>) {
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

//    fun getProfileInfo() {
//        var serviceResponse: Resource<ProfileResponse>?
//        _getProfileInfoResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getProfileInfo()
//
//                if (serviceResponse!!.data != null) {
//
//                    dataRepository.setVerified(
//                        serviceResponse!!.data!!.data?.isMailVerified!!,
//                        serviceResponse!!.data!!.data?.isPhoneVerified!!
//                    )
//
//                }
//                _getProfileInfoResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _getProfileInfoResponse.postValue(
//                    Resource.NetworkError(
//                        Error.NETWORK_ERROR
//                    )
//                )
//            }
//        }
//
//    }

    fun updateProfileInfo(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        Image: String?
    ) {
        var serviceResponse: Resource<UpdateProfileResponse>?
        _updateProfileResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.updateProfileInfo(firstName, lastName, email, phone, Image)

                if (serviceResponse!!.data != null) {
                    var user = getUser()
                    user.email = serviceResponse!!.data?.email
                    user.phone = serviceResponse!!.data?.phone!!
                    user.firstName = serviceResponse!!.data?.basicProfile?.firstName!!
                    user.lastName = serviceResponse!!.data?.basicProfile?.lastName!!
                    user.imageUrl = serviceResponse!!.data?.basicProfile?.imageUrl


                    //radwa
//                    user.firstName = serviceResponse!!.data!!.data?.first
                    //user.basicProfile = serviceResponse!!.data!!.data?.basicProfile!!
                    if (serviceResponse!!.data != null) {
                        dataRepository.saveUser(user)
                        //  dataRepository.saveUserProfile(serviceResponse!!.data!!.data?.basicProfile!!)
                    }
                    _updateProfileResponse.postValue(serviceResponse)
                }

            } catch (e: Exception) {
                e.printStackTrace()
//                _updateProfileResponse.postValue(serviceResponse)
                _updateProfileResponse.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun logout() {

        var serviceResponse: Resource<ActionResponse>?
        logoutResponseLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.logout()
                dataRepository.logoutLocal()
                getFCMToken()
                logoutResponseLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                logoutResponseLiveData.postValue(Resource.NetworkError(Error.NETWORK_ERROR))
            }
        }
    }

    fun contactUs(
        Phone: String,
        Message: String
    ) {
        var serviceResponse: Resource<ContactUsResponse>?
        contactUsResponseLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.contactUs(Phone, Message)
                contactUsResponseLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                contactUsResponseLiveData.postValue(Resource.NetworkError(Error.NETWORK_ERROR))
            }
        }
    }


    fun saveUserFinancialData(key: String, value: HashMap<String, String>) {
        dataRepository.saveUserFinancialData(key, value)
    }

    fun getFinancialData(key: String): HashMap<String, String>? {
        return dataRepository.getFinancialData(key)
    }


    fun saveUserFinancialData(key: String, value: List<FinancialTypeAttachments>) {
        dataRepository.saveUserFinancialImages(key, value)
    }

    fun getFinancialImages(key: String): List<FinancialTypeAttachments>? {
        return dataRepository.getUserFinancialImages(key)
    }


}
