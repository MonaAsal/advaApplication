//package com.salamtak.app.usecase
//
//import androidx.lifecycle.MutableLiveData
//import com.salamtak.app.data.DataSource
//import com.salamtak.app.data.Resource
//import com.salamtak.app.data.entities.*
//import com.salamtak.app.data.entities.responses.*
//import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//import kotlin.coroutines.CoroutineContext
//
//
///**
// * Created by Radwa Elsahn on 3/21/2020
// */
//
//class OperationsUseCase @Inject
//constructor(
//    private val dataRepository: DataSource,
//    override val coroutineContext: CoroutineContext
//) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {
//
//    private val _categoriesMutableLiveData =
//        MutableLiveData<Resource<SalamtakListResponse<Category>>>()
//    val categoriesLiveData = _categoriesMutableLiveData
//
//    private val _categoryDetailsMutableLiveData =
//        MutableLiveData<Resource<SalamtakListResponse<OperationN>>>()
//    val categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<OperationN>>> =
//        _categoryDetailsMutableLiveData
//
//
//    val _operationsLiveData = MutableLiveData<Resource<SalamtakListResponse<OperationN>>>()
//    val operations = _operationsLiveData
//
//    private val _governoratesMutableLiveData =
//        MutableLiveData<Resource<SalamtakListResponse<City>>>()
//    val governoratesLiveData: MutableLiveData<Resource<SalamtakListResponse<City>>> =
//        _governoratesMutableLiveData
//
//    val operationsN = MutableLiveData<Resource<SalamtakListResponse<OperationN>>>()
//
//
//    private val _bookOperationMutableLiveData =
//        MutableLiveData<Resource<SalamtakResponse<Booking>>>()
//    val bookOperationLiveData =
//        _bookOperationMutableLiveData
//
//
//    private val _addToFavouriteMutableLiveData =
//        MutableLiveData<Resource<ActionResponse>>()
//    val addToFavouriteLiveData: MutableLiveData<Resource<ActionResponse>> =
//        _addToFavouriteMutableLiveData
//
//    private val _bookedOperationsMutableLiveData =
//        MutableLiveData<Resource<BookedOperationsResponse>>()
//    val _bookedOperationsLiveData: MutableLiveData<Resource<BookedOperationsResponse>> =
//        _bookedOperationsMutableLiveData
//
//    private val _cancelOperationResponseMutableLiveData =
//        MutableLiveData<Resource<ActionResponse>>()
//    val cancelOperationResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
//        _cancelOperationResponseMutableLiveData
//
//    private val _reviewAddedResponseMutableLiveData = MutableLiveData<Resource<ActionResponse>>()
//    val reviewAddedResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
//        _reviewAddedResponseMutableLiveData
//
//
//    private val _operationsResponse =
//        MutableLiveData<Resource<SalamtakListResponse<OperationN>>>()
//    val operationsResponse = _operationsResponse
//
//    private val _getFavourites =
//        MutableLiveData<Resource<SalamtakListResponse<OperationN>>>()
//    val getFavourites: MutableLiveData<Resource<SalamtakListResponse<OperationN>>> =
//        _getFavourites
//
//    private val _doctorInfo = MutableLiveData<Resource<DoctorResponse>>()
//    val doctorInfo = _doctorInfo
//
//    private val _medicalProviderDoctors = MutableLiveData<Resource<DoctorsResponse>>()
//    val medicalProviderDoctors: MutableLiveData<Resource<DoctorsResponse>> = _medicalProviderDoctors
//
//    private val _medicalProviderInfo = MutableLiveData<Resource<MedicalProviderResponse>>()
//    val medicalProviderInfo = _medicalProviderInfo
//
//    private val _reviewResponse =
//        MutableLiveData<Resource<ActionResponse>>()
//    val reviewResponse =
//        _reviewResponse
//
//    private val _reviewsResponse = MutableLiveData<Resource<ReviewsResponse>>()
//    val reviewsResponse = _reviewsResponse
//
//
//    private val _drSpecializationResponseMutableLiveData =
//        MutableLiveData<Resource<DoctorSpecializationsResponse>>()
//    val drSpecializationResponseMutableLiveData =
//        _drSpecializationResponseMutableLiveData
//
//    var customOperationResponse = MutableLiveData<Resource<BaseResponse>>()
//    var lookupsResponse = MutableLiveData<Resource<CustomOperationLookupsResponse>>()
//
//
//    private val _doctorsResponse = MutableLiveData<Resource<SalamtakListResponse<MedicalNetwork>>>()
//    val doctorsResponse = _doctorsResponse
//
//    private val _providerResponse =
//        MutableLiveData<Resource<SalamtakListResponse<MedicalNetwork>>>()
//    val providerResponse = _providerResponse
//
//
////    fun getOperations(categoryId: Int, page: Int, pageSize: Int) {
////        var serviceResponse: Resource<SalamtakListResponse<OperationCardData>N>?
////        _categoryDetailsMutableLiveData.postValue(Resource.Loading())
////        launch {
////            try {
////                serviceResponse =
////                    dataRepository.requestCategoryDetails(categoryId, page, pageSize)
////                _categoryDetailsMutableLiveData.postValue(serviceResponse)
////            } catch (e: Exception) {
////                _categoryDetailsMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
////            }
////        }
////    }
//
//    fun getCategories(): List<Category> {
//        return dataRepository.getCategories()!!
//    }
//
//
//    fun getCities() {
//        var serviceResponse: Resource<SalamtakListResponse<City>>?
////        _governoratesMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.requestCities()
//                _governoratesMutableLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _governoratesMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
//            }
//        }
//    }
//
//
//    fun addToFavourite(operationId: String) {
//        var serviceResponse: Resource<ActionResponse>?
////        _addToFavouriteMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.postAddToFavourite(operationId)
//                _addToFavouriteMutableLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _addToFavouriteMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
//            }
//        }
//    }
//
//    fun getBookedOperation(page: Int, pageSize: Int) {
//        var serviceResponse: Resource<List<BookedOperation>>?
//        _bookedOperationsMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getBookedOperation(page, pageSize)
//                _bookedOperationsMutableLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _bookedOperationsMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
//            }
//        }
//    }
//
//    fun cancelOperation(id: String) {
//        var serviceResponse: Resource<ActionResponse>?
//        _cancelOperationResponseMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.cancelOperation(id)
//                _cancelOperationResponseMutableLiveData.value = (serviceResponse)
//            } catch (e: Exception) {
//                _cancelOperationResponseMutableLiveData.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun searchOperations(
//        query: String, categoryId: String, areaId: String, page: Int, pageSize: Int
//    ) {
//        var serviceResponse: Resource<SalamtakListResponse<OperationN>>?
//        _operationsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.searchOperations(query, categoryId, areaId, page, pageSize)
//                _operationsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _operationsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
////    fun getDoctorInfo(
////        doctorId: String,
////        page: Int,
////        pageSize: Int
////    ) {
////        var serviceResponse: Resource<DoctorResponse>?
////        _doctorInfo.postValue(Resource.Loading())
////        launch {
////            try {
////                serviceResponse =
////                    dataRepository.getDoctorInfo(doctorId, page, pageSize)
////                _doctorInfo.postValue(serviceResponse)
////            } catch (e: Exception) {
////                _doctorInfo.postValue(
////                    Resource.NetworkError(
////                        NETWORK_ERROR
////                    )
////                )
////            }
////        }
////    }
//
//    fun getFavourites(page: Int, pageSize: Int) {
//        var serviceResponse: Resource<SalamtakListResponse<OperationN>>?
//        _getFavourites.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getFavourites(page, pageSize)
//                _getFavourites.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _getFavourites.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//
//    fun getMedicalProviderDoctors(
//        providerId: String,
//        page: Int,
//        pageSize: Int
//    ) {
//        var serviceResponse: Resource<DoctorsResponse>?
//        _medicalProviderDoctors.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getMedicalProviderDoctors(providerId, page, pageSize)
//                _medicalProviderDoctors.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _medicalProviderDoctors.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getMedicalProviderOperations(
//        providerId: String,
//        page: Int,
//        pageSize: Int
//    ) {
////        var serviceResponse: Resource<SalamtakListResponse<OperationN>>?
////        _operationsResponse.postValue(Resource.Loading())
////        launch {
////            try {
////                serviceResponse =
//////                    dataRepository.getMedicalProviderOperations(providerId, page, pageSize, null)
////                _operationsResponse.postValue(serviceResponse)
////            } catch (e: Exception) {
////                _operationsResponse.postValue(
////                    Resource.NetworkError(
////                        NETWORK_ERROR
////                    )
////                )
////            }
////        }
//    }
//
//    fun addOperationReview(
//        requestId: String,
//        experienceRate: Int,
//        doctorRate: Int,
//        providerRate: Int,
//        review: String
//    ) {
//        var serviceResponse: Resource<ActionResponse>?
//        _reviewResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.addBookingReview(
//                        requestId,
//                        experienceRate,
//                        doctorRate,
//                        providerRate,
//                        review
//                    )
//                _reviewResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _reviewResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getOperationReviews(
//        operationId: String,
//        page: Int,
//        pageSize: Int
//    ) {
//        var serviceResponse: Resource<ReviewsResponse>?
//        _reviewsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getOperationReviews(operationId, page, pageSize)
//                _reviewsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _reviewsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getDoctorReviews(
//        doctorId: String,
//        page: Int,
//        pageSize: Int
//    ) {
//        var serviceResponse: Resource<ReviewsResponse>?
//        _reviewsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getDoctorReviews(doctorId, page, pageSize)
//                _reviewsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _reviewsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getProviderReviews(
//        providerId: String,
//        page: Int,
//        pageSize: Int
//    ) {
//        var serviceResponse: Resource<ReviewsResponse>?
//        _reviewsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.getProviderReviews(providerId, page, pageSize)
//                _reviewsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _reviewsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//
//    fun fetchCategoryOperations(
//        categoryId: Int,
//        page: Int,
//        pageSize: Int,
//        alphabetical: String
//    ) {
//        var serviceResponse: Resource<SalamtakListResponse<OperationN>>
//        _operationsLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.fetchCategoryOperations(categoryId, page, pageSize, alphabetical)
//                _operationsLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _operationsLiveData.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//
//    }
//
//    fun getDoctorSpecializations() {
//        var serviceResponse: Resource<DoctorSpecializationsResponse>?
//        _drSpecializationResponseMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.getDoctorSpecializations()
//                _drSpecializationResponseMutableLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _drSpecializationResponseMutableLiveData.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//
//    fun bookCustomOperation(
//        FullName: String,
//        Phone: String,
//        DoctorName: String,
//        OperationName: String,
//        CategoryId: Int,
//        InstallmentTypeId: String,
//        MonthlyInstallment: Double,
//        DownPayment: Double,
//        TotalCost: Double
//    ) {
//        var serviceResponse: Resource<BaseResponse>?
//        customOperationResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.bookCustomOperation(
//                    FullName,
//                    Phone,
//                    DoctorName,
//                    OperationName,
//                    CategoryId,
//                    InstallmentTypeId,
//                    MonthlyInstallment,
//                    DownPayment,
//                    TotalCost
//                )
//                customOperationResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                customOperationResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//
//    fun getCustomOperationLookups() {
//        var serviceResponse: Resource<CustomOperationLookupsResponse>?
//        lookupsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.getCustomOperationLookups()
//                lookupsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                lookupsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getDoctors(page: Int, pageSize: Int) {
//        var serviceResponse: Resource<SalamtakListResponse<MedicalNetwork>>?
//        _doctorsResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.getDoctors(page, pageSize)
//                _doctorsResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _doctorsResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//    fun getMedicalProviders(page: Int, pageSize: Int) {
//        var serviceResponse: Resource<SalamtakListResponse<MedicalNetwork>>?
//        _providerResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.getMedicalProviders(page, pageSize)
//                _providerResponse.postValue(serviceResponse)
//            } catch (e: Exception) {
//                _providerResponse.postValue(
//                    Resource.NetworkError(
//                        NETWORK_ERROR
//                    )
//                )
//            }
//        }
//    }
//
//
////    fun getSubCategoryOperations(subCategoryId: Int) {
////
////        var serviceResponse: Resource<SalamtakListResponse<OperationN>>?
////        operationsN.postValue(Resource.Loading())
////        launch {
////            try {
////                serviceResponse = dataRepository.getSubCategoryOperations(subCategoryId,page)
////                operationsN.postValue(serviceResponse)
////            } catch (e: Exception) {
////                operationsN.postValue(
////                    Resource.NetworkError(
////                        NETWORK_ERROR
////                    )
////                )
////            }
////        }
////    }
//}
