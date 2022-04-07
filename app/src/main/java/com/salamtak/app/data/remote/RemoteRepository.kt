package com.salamtak.app.data.remote

import android.util.Log
import com.google.gson.Gson
import com.salamtak.app.App
import com.salamtak.app.data.Resource
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.Error.Companion.DEFAULT_ERROR
import com.salamtak.app.data.error.Error.Companion.NO_INTERNET_CONNECTION
import com.salamtak.app.data.remote.service.*
import com.salamtak.app.data.remote.service.FinishingService
import com.salamtak.app.data.remote.service.InsuranceService
import com.salamtak.app.data.remote.service.WeddingService
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Constants.INSTANCE.DEVICE_ID
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.Network.Utils.isConnected
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Part
import java.io.File
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class RemoteRepository
@Inject constructor() : RemoteSource {

    var refreshCount = 0
    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!isConnected(App.context)) {
            return createError("NO_INTERNET_CONNECTION")
//            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            Log.e("responseCode", responseCode.toString())
            if (response.isSuccessful) {
                if (response.body().toString().contains("status=true")
                    || (response.body() is BaseResponse && (response.body() as BaseResponse).status
                            || response.body() is BasePagingResponse && (response.body() as BaseResponse).status)
                ) {
                    refreshCount = 0
                    response.body()
                } else {
                    try {
                        val baseResponse = (response.body() as BaseResponse)
                        LogUtil.serverError("::Error Body", baseResponse.message!!)
                        logFireBaseError(response, null, "444")

                        val list = ArrayList<ErrorModel>()
                        list.add(ErrorModel("400", baseResponse.message!!))
                        var errorResponse = ErrorResponse(list.toList())
                        errorResponse.status = false
                        errorResponse
                    } catch (e: Exception) {
                        e.printStackTrace()
                        logFireBaseError(response, e, "000")
                        createError(e.message.toString())
                    }
                }
            } else {
//                Log.e("STATUSCODE", responseCode.toString())
                val errorBody =
                    response.errorBody()

                try {
                    val errorResponse =
                        Gson().fromJson<ErrorResponse>(
                            errorBody?.string(),
                            ErrorResponse::class.java
                        )
                    errorResponse
                } catch (e: Exception) {
                    e.printStackTrace()
                    logFireBaseError(response, e, "111")
                    createError(e.message!!.toString())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            logFireBaseError(null, e, "222")
            createError("Please try again!")
        }
    }

    fun logFireBaseError(response: Response<*>?, exception: Exception?, code: String) {

        var requestUrl = ""
        var requestHeaders = ""
        var requestBody = ""
        var logMessage = code + "\t"
        response?.let {
            try {
                requestUrl = response.raw().request.url.toString()
                logMessage = logMessage.plus(requestUrl).plus("\n")
                requestHeaders = response.raw().request.headers.toString()
                logMessage = logMessage.plus(requestHeaders).plus("\n")
                requestBody =
                    if (response.raw().request.body != null) Gson().toJson(response.raw().request.body.toString()) else ""
                logMessage.plus(requestBody).plus("\n")
                logMessage = logMessage.plus(response.toString()).plus("\n")
                    .plus(response.errorBody().toString())
                // if there is an exception
                exception?.let {
                    logMessage =
                        logMessage.plus(exception.message + " \n" + exception.stackTrace.toString())
                }
                LogUtil.serverError("::UNSUCCESSFUL RESPONSE $logMessage")
            } catch (ignore: Exception) {
                LogUtil.serverError("::ERROR ON REQ/RES INFO METHODS : $logMessage" + " \n") //+ exception.stackTrace.toString()
            }
        } ?: kotlin.run {
            exception?.let {
                logMessage = logMessage.plus(exception.message)
            }
            LogUtil.serverError("::No RESPONSE $logMessage")
        }
    }


    private fun createError(message: String): Any? {
        var list = ArrayList<ErrorModel>()
        list.add(ErrorModel("000", "ERROR: $message"))
        var errorResponse = ErrorResponse(list.toList())
        errorResponse.status = false
        return errorResponse
    }

    private suspend fun processCallFile(responseCall: suspend () -> Response<*>): Any? {
        if (!isConnected(App.context)) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()

            } else {
                val errorBody =
                    response.errorBody()

                try {
                    val errorResponse =
                        Gson().fromJson<ErrorResponse>(
                            errorBody?.string(),
                            ErrorResponse::class.java
                        )
                    errorResponse
                } catch (e: Exception) {
                    e.printStackTrace()
                    DEFAULT_ERROR
                }
//                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            DEFAULT_ERROR
        }
    }

    override suspend fun requestCategories():
            Resource<SalamtakListResponse<Category>> {
        val operationsService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall(operationsService::fetchCategories)
        return try {
            var res = response as SalamtakListResponse<Category>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun requestLogin(
        userName: String,
        password: String,
        deviceId: String,
        fcmToken: String,

        ): Resource<SalamtakResponse<User>> {
        val nonAuthService = ServiceGenerator.createService(NonAuthService::class.java, false)
        val response = processCall { nonAuthService.login(userName, password, deviceId,fcmToken) }
        return try {
            var res = response as SalamtakResponse<User>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response =
//            processCall { nonAuthService.login(userName, password, deviceId) }) {
//            is UserResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    suspend fun refreshToken(
    ): Resource<RefreshTokenResponse> {

        val nonAuthService =
            ServiceGenerator.createService(NonAuthService::class.java, false)
        return when (val response =
            processCall {
//                nonAuthService.forgotPassword(Session.xAccessToken)
                nonAuthService.refreshToken(
                    Session.xAccessToken,
                    Session.xRefreshToken
                )
            }) {
            is RefreshTokenResponse -> {
                if (response.data.token.isNotEmpty())
                    Session.updateTokens(response.data.token, response.data.refreshToken)

                Resource.Success(data = response)
            }
            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
                var error = ErrorModel("401", Constants.KEY_INVALID_TOKEN)
                var list = ArrayList<ErrorModel>()
                list.add(error)
                var errorResponse = ErrorResponse()
                errorResponse.status = false
                errorResponse.errors = list
//                ErrorResponse(false, arrayListOf<ErrorModel>())
                Resource.DataError(errorResponse)
            }
        }

    }

//    override suspend fun homeVisitsPayment(
//        homeVisitRequestId: String,
//        cardId: String,
//        paymentMethodId: Int
//    ): Resource<SalamtakResponse<BaseResponse>> {
//        val apisService =
//            ServiceGenerator.createService(HomeVisitService::class.java, true)
//
//        val response = processCall {
//            apisService.homeVisitPayment(
//                homeVisitRequestId,
//                cardId,
//                paymentMethodId
//            )
//        }
//        return try {
//            var res = response as SalamtakResponse<BaseResponse>
//            Resource.Success(data = res)
//        } catch (e: Exception) {
//            Resource.DataError(errorResponse = response as ErrorResponse)
//        }
//
//
////        return when (val response =
////            processCall {
////                apisService.homeVisitPayment(
////                    homeVisitRequestId,
////                    cardId,
////                    paymentMethodId
////                )
////            }) {
////            is BaseResponse -> {
////                Resource.Success(data = response)
////            }
////            else -> {
////                Resource.DataError(errorResponse = response as ErrorResponse)
////            }
////        }
//    }

    override suspend fun requestCities(
    ): Resource<SalamtakListResponse<City>> {
        val apisService =
            ServiceGenerator.createService(LookupsService::class.java, true)

        val response = processCall(apisService::getAllCities)
        return try {
            var res = response as SalamtakListResponse<City>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response = processCall(apisService::fetchCities)) {
//            is CitiesResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

//    override suspend fun fetchBookingDetails(
//        page: Int, pageSize: Int
//    ): Resource<BookingResponse> {
//        val operationsService =
//            ServiceGenerator.createService(OperationsService::class.java)
//
//        return when (val response =
//            processCall { operationsapisService.fetchBookingDetails(page, pageSize) }) {
//            is BookingResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
//    }

    override suspend fun postBookOperation(
        downPayment: Int, operationId: String,
        installmentTypeId: String
    ): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        val response =
            processCall {
                apisService.postBookOperation(
                    downPayment,
                    operationId,
                    installmentTypeId
                )
            }

        if (response is ErrorResponse) {
            return Resource.DataError(errorResponse = response as ErrorResponse)
        } else {
            return try {
                var res = response as BaseResponse
                Resource.Success(data = res)
            } catch (e: Exception) {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun postAddToFavourite(operationId: String): Resource<ActionResponse> {
        val operationsService =
            ServiceGenerator.createService(OperationsService::class.java, true)
     //   Log.d("favouriteopration", operationsService.postAddToFavourite(operationId).toString())

        return when (val response = processCall { operationsService.postAddToFavourite(operationId) })
        {

                    is ActionResponse -> { Resource.Success(data = response)
                    }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }

        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String, password: String,
        confirmPassword: String,
        deviceId: String,
        fcmToken: String

    ): Resource<SalamtakResponse<User>> {
        val nonAuthService =
            ServiceGenerator.createService(NonAuthService::class.java, false)

        val response = processCall {
            nonAuthService.register(
                firstName, lastName, userName,
                phone,
                email,
                image,
                password,
                confirmPassword,
                deviceId,
                fcmToken
            )
        }

        return try {
            var res = response as SalamtakResponse<User>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
//        return when (val response =
//            processCall {
//                nonAuthService.register(
//                    firstName, lastName, userName,
//                    phone,
//                    email,
//                    image,
//                    password,
//                    confirmPassword
//                )
//            }) {
//            is UserResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    override suspend fun postMedicalProfile(
        PatientBloodTypeId: Int,
        Shareable: Int,
        DateOfBirth: String,
        Weight: String,
        Height: String
    ): Resource<MedicalProfileResponse> {
        val apisService =
            ServiceGenerator.createService(MedicalProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.postMedicalProfile(
                    PatientBloodTypeId,
                    Shareable,
                    DateOfBirth,
                    Weight,
                    Height
                )
            }) {
            is MedicalProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun postEmploymentProfile(
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
    ): Resource<EmploymentProfileResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.postEmploymentProfile(
                    Profession,
                    CompanyName,
                    HiringDate,
                    NetMonthlyIncome,
                    NetMonthlyExpense,
                    EmploymentTypeId,
                    EmploymentFieldId,
                    StreetAddress,
                    BuildingNum,
                    CityId,
                    AreaId
                )
            }) {
            is EmploymentProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun postFinancialProfile(
        NumberOfChildren: Int,
        CarManufactureYear: String,
        CarModelId: Int,
        ClubId: Int,
        BankInfoId: Int,
        MaritalStatusId: Int
    ): Resource<FinancialProfileResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.postFinancialProfile(
                    NumberOfChildren,
                    CarManufactureYear,
                    CarModelId,
                    ClubId,
                    BankInfoId, MaritalStatusId, "Other", "Other"
                )
            }) {
            is FinancialProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun getMedicalProfile(): Resource<MedicalProfileResponse> {
        val apisService =
            ServiceGenerator.createService(MedicalProfileService::class.java, true)

        return when (val response =
            processCall { apisService.getMedicalProfile() }) {
            is MedicalProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getEmploymentProfile(): Resource<EmploymentProfileResponse> {
        val operationsService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        return when (val response =
            processCall { operationsService.getEmploymentProfile() }) {
            is EmploymentProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getFinancialProfile(): Resource<FinancialProfileResponse> {
        val operationsService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        return when (val response =
            processCall { operationsService.getFinancialProfile() }) {
            is FinancialProfileResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getFinancialLookups(): Resource<FinancialLookupsResponse> {
        val profileService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        return when (val response =
            processCall { profileService.getFinancialLookups() }) {
            is FinancialLookupsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun fetchMedicalLookups(): Resource<MedicalProfileLookupsResponse> {
        val apisService =
            ServiceGenerator.createService(LookupsService::class.java, true)

        return when (val response =
            processCall { apisService.fetchMedicalLookups() }) {
            is MedicalProfileLookupsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun postBasicProfile(
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
        areaId: String, image: String?
    ): Resource<BasicProfileInfoResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val map: HashMap<String, RequestBody> = HashMap()
        map["FirstName"] = createPartFromString(firstName)
        map["SecondName"] = createPartFromString(secondName)
        map["ThirdName"] = createPartFromString(thirdName)
        map["LastName"] = createPartFromString(lastName)
        map["Gender"] = createPartFromString(gender.toString())
        map["NationalId"] = createPartFromString(nationalId)
        map["NationalIdExpireDate"] = createPartFromString(nationalIdExpireDate)
        map["IsEgyptian"] = createPartFromString(isEgyptian.toString())
        map["ResidentalStatusId"] =
            createPartFromString(residentalStatusId.toString())
        map["OtherResidentalStatus"] =
            createPartFromString(otherResidentalStatus)
        map["PassportNumber"] = createPartFromString(passportNumber)
        map["PassportExpireDate"] = createPartFromString(passportExpireDate)
        map["StreetAddress"] = createPartFromString(streetAddress)
        map["BuildingNum"] = createPartFromString(buildingNum)
        map["CityId"] = createPartFromString(cityId)
        map["AreaId"] = createPartFromString(areaId)
        var body: MultipartBody.Part? = null

        return when (val response =
            processCall {
                if (isEgyptian == 1) {
                    body = getfileImage(image, "NationalIdImage")
                    apisService.postBasicProfile(map, body, null)
                } else {
                    body = getfileImage(image, "PassportImage")
                    apisService.postBasicProfile(map, null, body)
                }
            }) {
            is BasicProfileInfoResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    private fun getfileImage(image: String?, paramName: String): MultipartBody.Part? {

        var body: MultipartBody.Part? = null
        if (!image.isNullOrEmpty()) {
            val file = File(image)
            Log.e("file size", (file.length() / 1024).toString())
            val requestFile: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

            body =
                MultipartBody.Part.Companion.createFormData(paramName, file.name, requestFile)
        }

        return body
    }


    fun createPartFromString(param: String): RequestBody {
//        return RequestBody.create("application/x-www-form-urlencoded".toMediaTypeOrNull(), param)
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), param)
    }

    override suspend fun getBasicProfile(): Resource<BasicProfileInfoResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.getBasicProfile()
            }) {
            is BasicProfileInfoResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun fetchProfileLookups(): Resource<ProfileLookupsResponse> {
        val apisService =
            ServiceGenerator.createService(LookupsService::class.java, true)
        return when (val response =
            processCall {
                apisService.fetchProfileLookups()
            }) {
            is ProfileLookupsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun postDiseases(chronicDiseases: List<ChronicDiseaseInput>): Resource<ChronicDiseasesResponse> {
        val apisService =
            ServiceGenerator.createService(MedicalProfileService::class.java, true)

        var map = ChronicDiseaseInput.chronicDiseaseListToMap(chronicDiseases)
        return when (val response =
            processCall {
                apisService.postDiseases(map)
            }) {
            is ChronicDiseasesResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }

    }

    override suspend fun getHomeVisit(requestId: String): Resource<HomeVisitResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.getHomeVisit(requestId)
            }) {
            is HomeVisitResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getMyHomeVisits(
        page: Int,
        pageSize: Int
    ): Resource<HomeVisitsResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.getMyHomeVisits(page, pageSize)
            }) {
            is HomeVisitsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun deleteChronicDisease(id: String): Resource<SalamtakResponse<BaseResponse>> {
        val apisService =
            ServiceGenerator.createService(MedicalProfileService::class.java, true)

        val response =
            processCall {
                apisService.deleteChronicDisease(id)
            }

        return try {
            var res = response as SalamtakResponse<BaseResponse>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response =
//            processCall {
//                apisService.deleteChronicDisease(id)
//            }) {
//            is BaseResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    override suspend fun cancelHomeVisit(id: String): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.cancelHomeVisit(id)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun cancelOperation(id: String): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.cancelOperation(id)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun verifyNumber(
        verifyType: Int,
        verifyCode: String
    ): Resource<SalamtakResponse<User>> {
        val apisService = ServiceGenerator.createService(NonAuthService::class.java, true)
        val response = processCall { apisService.verifyNumber(1, verifyCode,DEVICE_ID)
        }
        return try {
            var res = response as SalamtakResponse<User>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
//        return when (val response =
//            processCall {
//                apisService.verifyNumber(1, verifyCode)
//            }) {
//            is UserResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    override suspend fun requestVerifyNumber(VerifyType: Int): Resource<ActionResponse> {
        val apisService = ServiceGenerator.createService(NonAuthService::class.java, true)
        return when (val response =
            processCall {
                apisService.requestVerifyNumber(VerifyType)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun addHomeVisit(
        DoctorSpecializationId: Int,
        PreferredTimeId: Int,
        IsForYou: Int, Name: String, Age: String,
        Notes: String, ContactNumber: String,
        StreetAddress: String, BuildingNum: String,
        AppartmentNumber: String, CityId: Int,
        AreaId: String, Lat: Double,
        Lng: Double, PaymentMethodId: Int, CardId: String
    ): Resource<AddHomeVisitResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.addHomeVisit(
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
            }) {
            is AddHomeVisitResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getPreferredTimes(): Resource<PreferredTimesResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.getPreferredTimes()
            }) {
            is PreferredTimesResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getDoctorSpecializations(): Resource<DoctorSpecializationsResponse> {
        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.getDoctorSpecializations()
            }) {
            is DoctorSpecializationsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun logout(): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(UserAuthService::class.java, true)
        return when (val response =
            processCall {
                apisService.logout()
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getBookedOperation(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<BookedOperation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getBookedOperation(page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<BookedOperation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun searchOperations(
        query: String, categoryId: String, areaId: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.searchOperations(query, categoryId, areaId, page, pageSize, "true")
//                apisService.searchOperations( categoryId, areaId, page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getFavourites(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.getFavourites(page, pageSize, "true")
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response =
//            processCall {
//                apisService.getFavourites(page, pageSize, "true")
//            }) {
//            is SalamtakListResponse<OperationCardData> -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    override suspend fun changePassword(
        OldPassword: String,
        Password: String,
        ConfirmPassword: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(UserAuthService::class.java, true)
        return when (val response =
            processCall {
                apisService.changePassword(OldPassword, Password, ConfirmPassword)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun forgotPassword(
        email: String
    ): Resource<ActionResponse> {
        val nonAuthService =
            ServiceGenerator.createService(NonAuthService::class.java, false)
        return when (val response =
            processCall {
                nonAuthService.forgotPassword(email)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun forgotPasswordReset(
        id: String,
        ResetCode: String,
        Password: String,
        ConfirmPassword: String,
        fcmToken: String

    ): Resource<SalamtakResponse<User>> {
        val nonAuthService =
            ServiceGenerator.createService(NonAuthService::class.java, false)
        val response = processCall {
            nonAuthService.forgotPasswordReset(id, ResetCode, Password, ConfirmPassword
                , DEVICE_ID,fcmToken)
        }
        return try {
            var res = response as SalamtakResponse<User>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response =
//            processCall {
//                nonAuthService.forgotPasswordReset(id, ResetCode, Password, ConfirmPassword)
//            }) {
//            is UserResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }


    override suspend fun getMedicalProviderDoctors(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<DoctorsResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getMedicalProviderDoctors(providerId, page, pageSize)
            }) {
            is DoctorsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getDoctorOperations(
        doctorId: String, page: Int, pageSize: Int, alphabetical: String,
        categoryId: String?
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getDoctorOperations(
                doctorId,
                page,
                pageSize,
                "true",
                categoryId
            )
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun getMedicalProviderOperations(
        providerId: String,
        page: Int,
        pageSize: Int, categoryId: String?
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getMedicalProviderOperations(
                providerId,
                page,
                pageSize,
                "true",
                categoryId
            )
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

//        return when (val response =
//            processCall {
//                apisService.getMedicalProviderOperations(providerId, page, pageSize, "true")
//            }) {
//            is SalamtakListResponse<OperationCardData> -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

//    override suspend fun getProfileInfo(): Resource<ProfileResponse> {
//        val profileService =
//            ServiceGenerator.createService(ProfileService::class.java, true)
//        return when (val response =
//            processCall {
//                profileService.getProfileInfo()
//            }) {
//            is ProfileResponse -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
//    }

    override suspend fun updateProfileInfo(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        Image: String?
    ): Resource<UpdateProfileResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val map: HashMap<String, RequestBody> = HashMap()
        map["FirstName"] = createPartFromString(firstName)
        map["LastName"] = createPartFromString(lastName)
        map["Phone"] = createPartFromString(phone)
        map["Email"] = createPartFromString(email)
        var body: MultipartBody.Part? = null

        val response =
            processCall {
                body = getfileImage(Image, "Image")
                apisService.updateProfileInfo(map, body)
            }
        return try {
            var res = response as SalamtakResponse<UpdateProfileResponse>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun addCard(): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(PaymentMethodsService::class.java, true)
        return when (val response =
            processCall {
                apisService.addCard()
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun deleteCard(cardId: String): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(PaymentMethodsService::class.java, true)
        return when (val response =
            processCall {
                apisService.deleteCard(cardId)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getPaymentMethods(): Resource<PaymentMethodsResponse> {
        val apisService =
            ServiceGenerator.createService(PaymentMethodsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getPaymentMethods()
            }) {
            is PaymentMethodsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun setDefaultCard(cardId: String): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(PaymentMethodsService::class.java, true)
        return when (val response =
            processCall {
                apisService.setDefaultCard(cardId)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun addBookingReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        medicalProviderRate: Int,
        review: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.addReview(
                    requestId,
                    experienceRate,
                    doctorRate,
                    medicalProviderRate,
                    review
                )
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun addHomeVisitReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        review: String
    ): Resource<ActionResponse> {

        val apisService =
            ServiceGenerator.createService(HomeVisitService::class.java, true)
        return when (val response =
            processCall {
                apisService.addReview(requestId, experienceRate, doctorRate, review)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun getOperationReviews(
        operationId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getOperationReviews(operationId, page, pageSize)
            }) {
            is ReviewsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getDoctorReviews(
        doctorId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getDoctorReviews(doctorId, page, pageSize)
            }) {
            is ReviewsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun getProviderReviews(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getProviderReviews(providerId, page, pageSize)
            }) {
            is ReviewsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun updateFcmToken(
        token: String
    ): Resource<ActionResponse> {
        val apisService = ServiceGenerator.createService(UserAuthService::class.java, true)
        return when (val response = processCall {
                apisService.updateFcmToken(token,DEVICE_ID,true)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)

            }
        }
    }

    override suspend fun contactUs(
        Phone: String,
        Message: String
    ): Resource<ContactUsResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.contactUs(Phone, Message)
            }) {
            is ContactUsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun changeLanguage(
        language: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        return when (val response =
            processCall {
                apisService.changeLanguage(language)
            }) {
            is ActionResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getOffersByProvider(
        providerId: String, page: Int, pageSize: Int
    ): Resource<OffersResponse> {
        val apisService =
            ServiceGenerator.createService(OffersService::class.java, true)
        return when (val response =
            processCall {
                apisService.getOffersByProvider(providerId, page, pageSize)
            }) {
            is OffersResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getOffersProviders(
        page: Int,
        pageSize: Int
    ): Resource<OffersProvidersResponse> {
        val apisService =
            ServiceGenerator.createService(OffersService::class.java, true)
        return when (val response =
            processCall {
                apisService.getOffersProviders(page, pageSize)
            }) {
            is OffersProvidersResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getOffersUsage(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<OfferHistory>> {
        val apisService =
            ServiceGenerator.createService(OffersService::class.java, true)

        val response = processCall {
            apisService.getOffersUsage(page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<OfferHistory>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getQrCode(id: String): Resource<SalamtakResponse<QrData>> {
        val apisService =
            ServiceGenerator.createService(OffersService::class.java, true)

        val response = processCall {
            apisService.getQrCode(id)
        }
        return try {
            var res = response as SalamtakResponse<QrData>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun fetchCategoryOperations(
        categoryId: Int,
        page: Int,
        pageSize: Int,
        alphabetical: String
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        val response =
            processCall {
                apisService.fetchCategoryOperations(
                    categoryId,
                    page,
                    pageSize,
                    alphabetical
                )
            }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }


//        return when (val response =
//            processCall {
//                apisService.fetchCategoryOperations(categoryId, page, pageSize, alphabetical)
//            }) {
//            is SalamtakListResponse<OperationCardData> -> {
//                Resource.Success(data = response)
//            }
//            else -> {
//                Resource.DataError(errorResponse = response as ErrorResponse)
//            }
//        }
    }

    override suspend fun bookCustomOperation(
        FullName: String,
        Phone: String,
        DoctorName: String,
        OperationName: String,
        CategoryId: Int,
        InstallmentTypeId: String,
        MonthlyInstallment: Double,
        DownPayment: Double,
        TotalCost: Double
    ): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.bookCustomOperation(
                    FullName, Phone,
                    DoctorName,
                    OperationName,
                    CategoryId,
                    InstallmentTypeId,
                    MonthlyInstallment,
                    DownPayment,
                    TotalCost
                )
            }) {
            is ErrorResponse ->
                Resource.DataError(errorResponse = response)
            is BaseResponse -> {
                Resource.Success(data = response)
            }
            else ->
                Resource.DataError(errorResponse = response as ErrorResponse)

        }
    }

    override suspend fun getCustomOperationLookups(): Resource<CustomOperationLookupsResponse> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)
        return when (val response =
            processCall {
                apisService.getCustomOperationLookups()
            }) {
            is CustomOperationLookupsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getDoctors(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getDoctors(page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<MedicalNetwork>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun getMedicalProviders(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getMedicalProviders(page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<MedicalNetwork>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun getSubCategories(
        categoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategory>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getSubCategories(categoryId, page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<SubCategory>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getNewSubCategories(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategoryModel>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getNewSubCategories(categoryId, page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<SubCategoryModel>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


//    override suspend fun getOperationDetails(
//        operationId: Int
//    ): Resource<SalamtakResponse<OperationN>> {
//        val apisService =
//            ServiceGenerator.createService(OperationsService::class.java, true)
//
//        val response = processCall {
//            apisService.getOperationDetails(operationId)
//        }
//        return try {
//            var res = response as SalamtakResponse<OperationN>
//            Resource.Success(data = res)
//        } catch (e: Exception) {
//            Resource.DataError(errorResponse = response as ErrorResponse)
//        }
//    }

    override suspend fun getSubCategoryOperations(
        subCategoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getSubCategoryOperations(subCategoryId, page, pageSize)
        }
        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getPreCreateFinancialProfileInfo(): Resource<SalamtakResponse<PreFinancialProfile>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.getPreCreateFinancialProfileInfo()
        }
        return try {
            var res = response as SalamtakResponse<PreFinancialProfile>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun saveBasicProfile(
        profileId: String?,
        mobile: String,
        limit: String,
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        id: String,
        expiryDate: String,
        street: String,
        building: String,
        cityId: String,
        areaId: String,
        selectedLoanType: Int,
        providerId: String,
        maritalStatusId: String,
        idFaceTempName: String?,
        idBackTempName: String?
    ): Resource<SalamtakResponse<IdObject>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.saveBasicProfile(
                profileId,
                mobile,
                limit,
                limit,
                firstName,
                secondName,
                thirdName,
                lastName,
                id,
                expiryDate,
                street,
                building,
                cityId,
                areaId,
                selectedLoanType,
                providerId,
                maritalStatusId, idFaceTempName, idBackTempName
            )
        }
        return try {
            var res = response as SalamtakResponse<IdObject>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun uploadFile(image: String): Resource<SalamtakResponse<UploadImagesResponse>> {

        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        var body = getfileImage(image, "image")
        val response = processCall {
            apisService.uploadFile(body)
        }
        return try {
            var res = response as SalamtakResponse<UploadImagesResponse>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun uploadMultipleFiles(
        images: Array<String>
    ): Resource<SalamtakResponse<UploadImagesResponse>> {

        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)


        var list = mutableListOf<MultipartBody.Part>()
        images.forEach {
            list.add(getfileImage(it, "")!!)
        }

        val response = processCall {
            apisService.uploadMultipleFiles(list.toList() as Array<Part>)
        }

        return try {
            var res = response as SalamtakResponse<UploadImagesResponse>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun addCategoryAttachment(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        filePath: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val map: HashMap<String, RequestBody> = HashMap()
        map["FinancnailProfileId"] = createPartFromString(FinancnailProfileId)
        map["CategoryId"] = createPartFromString(CategoryId)
        map["AttachmentId"] = createPartFromString(AttachmentId)
        map["position"] = createPartFromString(position.toString())

        var body = getfileImage(filePath, "Attachment")

        val response = processCall {
            apisService.addCategoryAttachment(map, body)
        }
        return try {
            var res = response as SalamtakResponse<AttachmentData>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun addAttachments(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        filePath: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val map: HashMap<String, RequestBody> = HashMap()
        map["FinancnailProfileId"] = createPartFromString(FinancnailProfileId)
        map["CategoryId"] = createPartFromString(CategoryId)
        map["AttachmentId"] = createPartFromString(AttachmentId)
        map["position"] = createPartFromString(position.toString())

        var body = getfileImage(filePath, "Attachment")

        val response = processCall {
            apisService.addAttachment(map, body)
        }
        return try {
            var res = response as SalamtakResponse<AttachmentData>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun addEmployeeData(
        job: String,
        salary: String,
        companyName: String,
        companyAddress: String,
        customerFinancialProfileId: String,
//        bankStatement: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addEmployeeData(
                job,
                salary,
                companyName,
                companyAddress,
                customerFinancialProfileId,
//                "1",
//                bankStatement,
//                utilityBillId,
//                guranatorIDFrontAttachmentId, guranatorIDBackAttachmentId
            )
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getFinancialCategoryAttachments(
        FinancialProfileId: String,
        categoryId: String
    ): Resource<SalamtakResponse<FinancialCategoriesData>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.getFinancialCategoryAttachments(FinancialProfileId, categoryId)
        }

        return try {
            var res = response as SalamtakResponse<FinancialCategoriesData>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }
//
//    override suspend fun getAttachment(fileId: String): Resource<File> {
//        val apisService =
//            ServiceGenerator.createServiceFile(ProfileService::class.java, true)
//
//        val response = processCall {
//            apisService.getAttachment(fileId)
//        }
//
//        return try {
//            var res = response as File
//            Resource.Success(data = res)
//        } catch (e: Exception) {
//            Log.e("erroe", e.message!!)
//            Resource.DataError(errorResponse = response as ErrorResponse)
//        }
//
//    }

    override suspend fun downloadFileWithDynamicUrlSync(url: String): ResponseBody? {

        val apisService =
            ServiceGenerator.createServiceFile(ProfileService::class.java, true)

        val response = processCallFile {
            apisService.downloadFileWithDynamicUrlSync(url)
        }

        return try {
            response as ResponseBody

//            Resource.Success(data = file)
        } catch (e: Exception) {
            //Log.e("erroe", e.message!!)
            null
//            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun addPensionData(
        customerFinancialProfileId: String,
        amount: String,
//        pensionId: String,
//        pension2Id: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {

        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addPensionData(
                customerFinancialProfileId,
                amount,
//                pensionId,
//                pension2Id,
//                utilityBillId,
//                guranatorIDFrontAttachmentId,
//                guranatorIDBackAttachmentId
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun addAssetOwnerData(
        customerFinancialProfileId: String,
        NetIncome: String,
//        AssetContractAttachmentId: String,
//        LeasingContractAttachmentId: String,
//        UtilityBillAttachmentId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addAssetOwnerData(
                customerFinancialProfileId,
                NetIncome,
//                AssetContractAttachmentId,
//                LeasingContractAttachmentId,
//                UtilityBillAttachmentId,
//                guranatorIDFrontAttachmentId,
//                guranatorIDBackAttachmentId
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun addBusinessOwnerData(
        customerFinancialProfileId: String,
        Job: String,
        CompanyNetIncome: String,
        CompanyName: String,
        CompanyAddress: String,
//        CommercialRegisterAttachmentId: String,
//        TaxIDAttachmentId: String,
//        TaxReturnAttachmentId: String,
//        BankStatementAttachmentId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {

        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addBusinessOwnerData(
                customerFinancialProfileId,
                Job,
                CompanyNetIncome,
                CompanyName,
                CompanyAddress,
//                CommercialRegisterAttachmentId,
//                TaxIDAttachmentId,
//                TaxReturnAttachmentId,
//                BankStatementAttachmentId,
//                guranatorIDFrontAttachmentId,
//                guranatorIDBackAttachmentId
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun addClubData(
        customerFinancialProfileId: String
    ): Resource<ActionResponse> {

        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addClubData(
                customerFinancialProfileId
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun addCarOwnerData(
        customerFinancialProfileId: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)
        8
        val response = processCall {
            apisService.addCarOwnerData(
                customerFinancialProfileId
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun addBankCertificateData(
        customerFinancialProfileId: String,
        NetIncome: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.addBankCertificateData(
                customerFinancialProfileId,
                NetIncome
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun isFinancialProfileCompleted(): Resource<FinancialProfileCompleted> {
        try {
            val apisService =
                ServiceGenerator.createService(ProfileService::class.java, true)

            val response = processCall {
                apisService.isFinancialProfileCompleted()
            }

            return try {
                var res = response as SalamtakResponse<FinancialProfileCompleted>
                Resource.Success(data = res.data!!)
            } catch (e: Exception) {
                // Log.e("erroe", e.message.toString())
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        } catch (e: Exception) {

            var list = ArrayList<ErrorModel>()
            list.add(ErrorModel("400", "ERROR"))
            var errorResponse = ErrorResponse(list.toList())
            errorResponse.status = false

            return Resource.DataError(errorResponse = errorResponse)
        }
    }

    override suspend fun getNationalIdAttachments(
        FinancialProfileId: String,
        IsFaceImage: Boolean
    ): ResponseBody? {
        val apisService =
            ServiceGenerator.createServiceFile(ProfileService::class.java, true)

        val response = processCallFile {
            apisService.getNationalIdAttachments(FinancialProfileId, IsFaceImage)
        }

        return try {
            response as ResponseBody

        } catch (e: Exception) {
            Log.e("erroe", e.message!!)
            null
        }
    }

    override suspend fun deleteAttachment(
        fileId: String,
//        financialProfileId: String,
//        categoryId: String
    ): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.deleteAttachment(fileId)
        }

        return try {
            var res = response as BaseResponse
            if (res.status)
                Resource.Success(data = res!!)
            else
                Resource.DataError(errorResponse = response as ErrorResponse)
        } catch (e: Exception) {
            Log.e("error", e.message!!)
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun getCategoryProviders(
        categoryId: Int, page: Int, pageSize: Int, filter: String?
    ): Resource<SalamtakListResponse<MedicalProvider>> {
        val apisService = ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
          //  apisService.getCategoryProviders(categoryId, page, pageSize, filter)
            apisService.getCategoryProviders(categoryId, page, pageSize)

        }

        return try {
            var res = response as SalamtakListResponse<MedicalProvider>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getMedicalProviderInfo(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<MedicalProviderDetails> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getMedicalProviderInfo(providerId, page, pageSize)
        }

        return try {
            var res = response as SalamtakResponse<MedicalProviderDetails>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getDoctorInfo(
        doctorId: String,
        page: Int,
        pageSize: Int
    ): Resource<DoctorDetails> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getDoctorInfo(doctorId, page, pageSize)
        }

        return try {
            var res = response as SalamtakResponse<DoctorDetails>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getOperationDetails(
        operationId: String
    ): Resource<OperationDetails> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getOperationDetails(operationId)
        }
        return try {
            var res = response as SalamtakResponse<OperationDetails>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun searchHealth(
        query: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.searchHealth(query, page, pageSize)
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

   override suspend fun CreateCustomInsuranceBooking(
        fullName: String,
        phoneNumber: String,
        insuranceCompanyName: String,
        insuranceType: Int,
        monthlyInstallment: String, downPayment: String, installmentTypeId: String, price: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(InsuranceService::class.java, true)

        val response = processCall {
            apisService.CreateCustomInsuranceBooking(
                fullName,
                phoneNumber,
                insuranceCompanyName,
                insuranceType,
                monthlyInstallment, downPayment, installmentTypeId, price
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun CreateCustomEducationBooking(input: EducationBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.CreateCustomEducationBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getInstallmentsLookup(type: String): Resource<InstallmentTypesData> {
        val apisService =
            ServiceGenerator.createService(GenericService::class.java, true)

        val response = processCall {
            apisService.getInstallmentsLookup(
                type
            )
        }

        return try {
            var res = response as SalamtakResponse<InstallmentTypesData>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getLatestAdded(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.getLatestAdded(
                page, pageSize
            )
        }

        return try {
            var res = response as SalamtakListResponse<MedicalNetwork>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getProvidersNames(categoryId: String): Resource<List<MedicalProvider>> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getProvidersNames(
                categoryId
            )
        }

        return try {
            var res = response as SalamtakListResponse<MedicalProvider>
            Resource.Success(data = res.data)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getPreHealthCategoryFilter(categoryId: String): Resource<FilterData> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getPreHealthCategoryFilter(
                categoryId
            )
        }

        return try {
            var res = response as SalamtakResponse<FilterData>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun getPreHealthAdvancedFilter(): Resource<FilterData> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getPreHealthAdvancedFilter(
            )
        }

        return try {
            var res = response as SalamtakResponse<FilterData>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun categoryHealthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.categoryHealthAdvancedSearch(
                if (categoryId.isNotEmpty()) categoryId else null,
                if (subCategoryId.isNotEmpty()) subCategoryId else null,
                medicalProvider,
                if (cityId.isNotEmpty()) cityId else null,
                startPrice,
                endPrice,
                page,
                pageSize
            )
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun healthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        Operation: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        val apisService =
            ServiceGenerator.createService(OperationsService::class.java, true)

        val response = processCall {
            apisService.healthAdvancedSearch(
                if (categoryId.isNotEmpty()) categoryId else null,
                if (subCategoryId.isNotEmpty()) subCategoryId else null,
                medicalProvider,
                if (cityId.isNotEmpty()) cityId else null,
                startPrice,
                endPrice,
                Operation,
                page,
                pageSize
            )
        }

        return try {
            var res = response as SalamtakListResponse<Operation>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }






    //health ...
    override suspend fun AddCategoryHealthBookingToCart(
        requestBody: HealthCategoryRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCategoryHealthBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //health custom...
    override suspend fun AddCustomHealthBookingToCart(
        requestBody: HealthCustomRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomHealthBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //finishing....
    override suspend fun AddCategoryFinishingBookingToCart(
        requestBody: FinishingCategoryRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCategoryFinishingBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //finishing custom....
    override suspend fun AddCustomFinishingBookingToCart(
        requestBody: FinishingCustomRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomFinishingBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //car....
    override suspend fun AddCategoryCarBookingToCart(
        requestBody: CarCategoryRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCategoryCarBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //car custom....
    override suspend fun AddCustomCarBookingToCart(
        requestBody: CarCustomRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomCarBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //Education custom....
    override suspend fun AddCustomEducationBookingToCart(
        requestBody: EducationCustomRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomEducationBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }
    //Education ....
    override suspend fun AddEducationBookingToCart(
        requestBody: EducationRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addEducationBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    //wedding custom.....
    override suspend fun AddCustomWeddingBookingToCart(
        requestBody: WeddingRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomWeddingBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun CreateCustomWeddingBooking(
        fullName: String,
        phoneNumber: String,
        venueName: String,
        inviteesNumber: Int,
        monthlyInstallment: String,
        downPayment: String,
        installmentTypeId: String,
        price: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(WeddingService::class.java, true)

        val response = processCall {
            apisService.CreateCustomWeddingBooking(
                fullName,
                phoneNumber,
                venueName,
                inviteesNumber,
                monthlyInstallment,
                downPayment,
                installmentTypeId,
                price
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getMoreDoctors(
        subCategoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalProvider>> {
        val apisService =
            ServiceGenerator.createService(MedicalProviderService::class.java, true)

        val response = processCall {
            apisService.getMoreDoctors(subCategoryId, page, pageSize)
        }

        return try {
            var res = response as SalamtakListResponse<MedicalProvider>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCategories(maincategoryId: Int): Resource<SalamtakListResponse<Category>> {
        val apisService =
            ServiceGenerator.createService(GenericService::class.java, true)

        val response = processCall {
            apisService.getCategories(maincategoryId)
        }

        return try {
            var res = response as SalamtakListResponse<Category>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getAllEducationByCategoryId(
        categoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakObjectListResponse<EducationSubcategoriesData>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getAllEducationByCategoryId(categoryId, page, pageSize)
        }

        return try {
            var res = response as SalamtakObjectListResponse<EducationSubcategoriesData>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun getAllEducationBySubCategoryId(
        SubCategoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<School>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getAllEducationBySubCategoryId(SubCategoryId, page, pageSize)
        }

        return try {
            var res = response as SalamtakListResponse<School>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun getSchoolDetailsById(schoolId: String): Resource<SalamtakResponse<SchoolDetails>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getSchoolDetailsById(schoolId)
        }

        return try {
            var res = response as SalamtakResponse<SchoolDetails>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createSchoolBooking(input: SchoolRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.createSchoolBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createUniversityBooking(input: UniversityRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.createUniversityBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createInstituteBooking(input: UniversityRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.createInstituteBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getUniversitiesAndInstitutes(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<University>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getUniversitiesAndInstitutes(categoryId, page, pageSize)
        }

        return try {
            var res = response as SalamtakListResponse<University>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCollages(
        id: String,
        type: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakResponse<University>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getCollages(id, page, pageSize)
        }

        return try {
            var res = response as SalamtakResponse<University>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCollageDetails(id: String): Resource<SalamtakResponse<Collage>> {
        val apisService =
            ServiceGenerator.createService(EducationService::class.java, true)

        val response = processCall {
            apisService.getCollageDetails(id)
        }

        return try {
            var res = response as SalamtakResponse<Collage>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun VOTP(
        otp: String,
        bookingId: String, referenceNumber: String
    ): Resource<BaseResponse> {
        val response = processCall {
            ServiceGenerator.createService(PremiumService::class.java, true)
                .VOTP(otp, bookingId, referenceNumber)
        }

        return try {
            var res = response as BaseResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun GOTP(
        cardNumber: String,
        expiry: String,
        BookingId: String
    ): Resource<SalamtakResponse<PremiumData>> {
        val apisService =
            ServiceGenerator.createService(PremiumService::class.java, true)

        val response = processCall {
            apisService.GOTP(cardNumber, expiry, BookingId)
        }

        return try {
            var res = response as SalamtakResponse<PremiumData>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun GTRD(
        referenceNumber: String
    ): Resource<SalamtakResponse<TransactionDetails>> {
        val apisService =
            ServiceGenerator.createService(PremiumService::class.java, true)

        val response = processCall {
            apisService.GTRD(referenceNumber)
        }

        return try {
            var res = response as SalamtakResponse<TransactionDetails>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getFinancialProgress(): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.getFinancialProgress()
        }

        return try {
            var res = response as SalamtakResponse<BaseResponse>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun financialPreStepOne(): Resource<PreStep1Data> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.financialPreStepOne()
        }

        return try {
            var res = response as SalamtakResponse<PreStep1Data>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun financialPreStepThree(id: String): Resource<Step3Data> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.financialPreStepThree(id)
        }

        return try {
            var res = response as SalamtakResponse<Step3Data>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun financialPreStepTwo(id: String): Resource<PreStep2Data> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.financialPreStepTwo(id)
        }

        return try {
            var res = response as SalamtakResponse<PreStep2Data>
            Resource.Success(data = res.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun postStepOne(input: FinancialProfile): Resource<SalamtakResponse<IdObject>> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.postStepOne(input)
        }

        return try {
            var res = response as SalamtakResponse<IdObject>
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun postStepTwo(
        input: Step2
//        id: String,
//        clubMembershipEnabled: Boolean,
//        clubId: String,
//        carOwnerEnabled: Boolean,
//        carId: String,
//        creditAndLoanEnabled: Boolean,
//        type: String,
//        limit: String
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.postStepTwo(
                input
//                id,
//                clubMembershipEnabled,
//                clubId,
//                carOwnerEnabled,
//                carId,
//                creditAndLoanEnabled,
//                type,
//                limit
            )
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun postStepThree(input: Step3Data): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(ProfileService::class.java, true)

        val response = processCall {
            apisService.postStepThree(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getFinishingProviders(
        categoryId: Int?,
        page: Int,
        pageSize: Int,
        query: String,
        sort: Int?
    ): Resource<SalamtakObjectListResponse<FinishingProvidersData>> {
        val apisService =
            ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.getFinishingProviders(categoryId, page, pageSize, query, sort)
        }

        return try {
            var res = response as SalamtakObjectListResponse<FinishingProvidersData>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getFinishingCategories(page:Int,pageSize: Int):
            Resource<SalamtakListResponse<FinishingCategoryModel>>{
        val apisService = ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.getFinishingCategories(page, pageSize)
        }

        return try {
            var res = response as SalamtakListResponse<FinishingCategoryModel>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

     }

    override suspend fun getFinishingProviderDetails(id: String): Resource<SalamtakResponse<FinishingProvider>> {
        val apisService = ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.getFinishingProviderDetails(id)
        }

        return try {
            var res = response as SalamtakResponse<FinishingProvider>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createCustomFinishingBooking(input: FinishingCustomRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.createCustomFinishingBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createFinishingBooking(input: FinishingRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.createFinishingBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    ///cars
    override suspend fun createCarServiceBooking(input: CarServiceRequestBookingInput): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.createCarServiceBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun createCustomCarServiceBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse> {
        val apisService = ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.createCustomCarServiceBooking(input)
        }

        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCarServiceCenterDetails(id: String): Resource<SalamtakResponse<CarServiceCenter>> {
        val apisService = ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.getCarServiceCenterDetails(id)
        }

        return try {
            var res = response as SalamtakResponse<CarServiceCenter>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCarProviderDetails(id: String): Resource<SalamtakResponse<CarProviderDetails>> {
        val apisService = ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.getCarProviderDetails(id)
        }
        return try {
            var res = response as SalamtakResponse<CarProviderDetails>
            res.data!!.services!!.map {
                Log.e("services1", it)

            }
            Log.e("servicesGson", Gson().toJson(res!!.data!!.services))

            Resource.Success(data = res!!)

        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCarProvidersViewAll(
        page: Int,
        pageSize: Int,
        categoryId: Int
    ): Resource<SalamtakObjectListResponse<CarProvidersData>> {
        val apisService =
            ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.getCarProvidersViewAll(
                page,
                pageSize,
                categoryId
            )
        }

        return try {
            var res = response as SalamtakObjectListResponse<CarProvidersData>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }


    override suspend fun getCarServiceCenters(
        input: GetCarServiceInput
    ): Resource<SalamtakListResponse<CarCategoryModel>> {
        val apisService =
            ServiceGenerator.createService(CarsService::class.java, true)

        val response = processCall {
            apisService.getCarServiceCenters(
                input.page,
                input.pageSize,
               // input.brands,
               // input.services,
              //  input.locations,
              //  input.query
            )
        }

        return try {
            var res = response as SalamtakListResponse<CarCategoryModel>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getPackageDetails(id: String): Resource<FinishingPackage> {
        val apisService =
            ServiceGenerator.createService(FinishingService::class.java, true)

        val response = processCall {
            apisService.getPackageDetails(id)
        }

        return try {
            var res = response as SalamtakResponse<FinishingPackage>
            Resource.Success(data = res!!.data!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCarBrands(): Resource<SalamtakListResponse<CarModel>> {
        val apisService =
            ServiceGenerator.createService(LookupsService::class.java, true)

        val response = processCall {
            apisService.getCarBrands()
        }

        return try {
            var res = response as SalamtakListResponse<CarModel>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun getCities(): Resource<SalamtakListResponse<City>> {
        val apisService =
            ServiceGenerator.createService(LookupsService::class.java, true)

        val response = processCall {
            apisService.getAllCities()
        }

        return try {
            var res = response as SalamtakListResponse<City>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }

    }

    override suspend fun getCitiesForCarFilter(): Resource<SalamtakListResponse<City>> {
        val apisService = ServiceGenerator.createService(LookupsService::class.java, true)

        val response = processCall {
            apisService.getCitiesForCarFilter()
        }

        return try {
            var res = response as SalamtakListResponse<City>
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }
    //    override suspend fun createCustomCarBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse> {
//        val apisService =
//            ServiceGenerator.createService(CarsService::class.java, true)
//
//        val response = processCall {
//            apisService.createCustomCarServiceBooking(input)
//        }
//
//        return try {
//            var res = response as ActionResponse
//            Resource.Success(data = res!!)
//        } catch (e: Exception) {
//            Resource.DataError(errorResponse = response as ErrorResponse)
//        }
//    }

    override suspend fun getFeaturedCategories(): Resource<FeaturedCategoriesResponse> {
        val apisService =
            ServiceGenerator.createService(HomeService::class.java, true)
        return when (val response =
            processCall {
                apisService.getHomeFeaturedCategories()
            }) {
            is FeaturedCategoriesResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }
    override suspend fun getAdvertisements(): Resource<AdvertisementsResponse> {
        val apisService =
            ServiceGenerator.createService(HomeService::class.java, true)
        return when (val response =
            processCall {
                apisService.getAdvertisements()
            }) {
            is AdvertisementsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }
    override suspend fun getPromotions(): Resource<PromotionsResponse> {
        val apisService =
            ServiceGenerator.createService(HomeService::class.java, true)
        return when (val response =
            processCall {
                apisService.getPromotions()
            }) {
            is PromotionsResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }
    override suspend fun getTopProviders(): Resource<TopProvidersResponse> {
        val apisService =
            ServiceGenerator.createService(HomeService::class.java, true)
        return when (val response =
            processCall {
                apisService.getTopProviders()
            }) {
            is TopProvidersResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }

    override suspend fun getServicesCategories(): Resource<ServicesCategoriesResponse> {
        val apisService =
            ServiceGenerator.createService(ServicesService::class.java, true)
        return when (val response =
            processCall {
                apisService.getServicesCategories()
            }) {
            is ServicesCategoriesResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }
    override suspend fun addCustomInsuranceToCart(
        requestBody: InsuranceRequestBody,
    ): Resource<ActionResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.addCustomWeddingBookingToCart(requestBody)
        }
        return try {
            var res = response as ActionResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun GetCartData(cartUID: String): Resource<GetCartResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        return when (val response =
            processCall {
                apisService.GetCartData(cartUID)
            }) {
            is GetCartResponse -> {
                Resource.Success(data = response)
            }
            else -> {
                Resource.DataError(errorResponse = response as ErrorResponse)
            }
        }
    }


    override suspend fun deleteCartItem(ItemID: String): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.deleteCartItem(ItemID)
        }
        return try {
            var res = response as BaseResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }

    override suspend fun checkoutCart(cartID: String): Resource<BaseResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.checkoutCart(cartID)
        }
        return try {
            val res = response as BaseResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }
    override suspend fun getCartCount(cartID: String): Resource<cartCountResponse> {
        val apisService =
            ServiceGenerator.createService(CartService::class.java, true)
        val response = processCall {
            apisService.getCartCount(cartID)
        }
        return try {
            val res = response as cartCountResponse
            Resource.Success(data = res!!)
        } catch (e: Exception) {
            Resource.DataError(errorResponse = response as ErrorResponse)
        }
    }
}