package com.salamtak.app.usecase

import android.util.Log
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class AuthUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val loginMutableLiveData = MutableLiveData<Resource<SalamtakResponse<User>>>()
    val loginLiveData: MutableLiveData<Resource<SalamtakResponse<User>>> = loginMutableLiveData

    private val _changePasswordResponse = MutableLiveData<Resource<ActionResponse>>()
    val changePasswordResponse: MutableLiveData<Resource<ActionResponse>> = _changePasswordResponse

    private val _forgotPasswordResponse = MutableLiveData<Resource<ActionResponse>>()
    val forgotPasswordResponse: MutableLiveData<Resource<ActionResponse>> = _forgotPasswordResponse

    private val _verifyResponseMutableLiveData = MutableLiveData<Resource<SalamtakResponse<User>>>()
    val verifyResponseMutableLiveData: MutableLiveData<Resource<SalamtakResponse<User>>> =
        _verifyResponseMutableLiveData

    fun login(username: String, password: String, deviceId: String , fcmToken:String
    ) {
        var serviceResponse: Resource<SalamtakResponse<User>>?
        loginMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.login(username, password, deviceId,fcmToken)
                if (serviceResponse!!.data != null) {
                    dataRepository.saveUser(serviceResponse!!.data!!.data!!)
                    var fcmToken = getSavedFCMToken()
                    if (fcmToken.isNotEmpty())
                        updateFcmToken(fcmToken)
                }
                loginMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                loginMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

//    fun homeVisitsPayment() {
//        var serviceResponse: Resource<SalamtakResponse<BaseResponse>>?
//        loginMutableLiveData.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse =
//                    dataRepository.homeVisitsPayment(
//                        "63437ebd-34ba-4ead-b190-5d3eb3047bf8",
//                        "43c0232e-f261-4a45-bc27-fcf95684b80c",
//                        2
//                    )
//
////                loginMutableLiveData.postValue(serviceResponse)
//            } catch (e: Exception) {
//                loginMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
//            }
//        }
//
//    }

    fun verifyNumber(verifyType: Int, verifyCode: String) {
        var serviceResponse: Resource<SalamtakResponse<User>>?
        _verifyResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository.verifyNumber(
                        verifyType, verifyCode
                    )
                var user = dataRepository.getUser()!!
                if (serviceResponse?.data!!.data != null) {
                    user?.isPhoneVerified = true
                    user?.token = serviceResponse!!.data!!.data?.token!!
                    user?.refreshToken = serviceResponse!!.data!!.data?.refreshToken!!

                    dataRepository.saveUser(user!!) //serviceResponse!!.data!!.data!!
                }
                _verifyResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                _verifyResponseMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun changePassword(
        OldPassword: String,
        Password: String,
        ConfirmPassword: String
    ) {
        var serviceResponse: Resource<ActionResponse>?
        _changePasswordResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.changePassword(OldPassword, Password, ConfirmPassword)
               if (serviceResponse?.data?.status== true) {
                dataRepository.logout()
               }

                _changePasswordResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _changePasswordResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }

    }

    fun forgotPassword(
        email: String
    ) {
        var serviceResponse: Resource<ActionResponse>?
        _forgotPasswordResponse.postValue(Resource.Loading())
       launch {
            try {
                serviceResponse = dataRepository.forgotPassword(email)

                _forgotPasswordResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _forgotPasswordResponse.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun forgotPasswordReset(
        id: String,
        ResetCode: String,
        Password: String,
        ConfirmPassword: String,
        fcmToken:String

    ) {
        var serviceResponse: Resource<SalamtakResponse<User>>?
        loginMutableLiveData.postValue(Resource.Loading())
        launch {
            try {

               // var token : String =dataRepository.getFCMToken()
                serviceResponse = dataRepository.forgotPasswordReset(id, ResetCode, Password, ConfirmPassword
                   ,fcmToken
                )

                if (serviceResponse!!.data != null)
                    dataRepository.saveUser(serviceResponse!!.data!!.data!!)
                loginMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                loginMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun saveCredentials4Biometric(username: String, password: String) {
        dataRepository.saveCredentials4Biometric(username, password)
    }

    fun loginBiometric(deviceId: String) {
        login(dataRepository.getBioUserName(), dataRepository.getBioPassword(), deviceId,dataRepository.getFCMToken())
    }

    fun isBiometricSaved(): Boolean {
        return dataRepository.getBioUserName().isNotEmpty() && dataRepository.getBioPassword()
            .isNotEmpty()
    }


}
