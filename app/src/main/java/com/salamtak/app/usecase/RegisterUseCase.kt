package com.salamtak.app.usecase

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.Error.Companion.NETWORK_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class RegisterUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope, SharedUseCase(dataRepository, coroutineContext) {

    private val registerMutableLiveData = MutableLiveData<Resource<SalamtakResponse<User>>>()
    val registerLiveData: MutableLiveData<Resource<SalamtakResponse<User>>> =
        registerMutableLiveData


    fun register(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String,
        password: String,
        confirmPassword: String,
        deviceId: String,
        fcmToken:String

    ) {
        var serviceResponse: Resource<SalamtakResponse<User>>?
        registerMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                var fcmToken = getSavedFCMToken()

                serviceResponse = dataRepository.register(
                    firstName,
                    lastName,
                    "",//userName,
                    phone,
                    email,
                    image,
                    password,
                    confirmPassword,
                    deviceId,
                    fcmToken

                )
                if (serviceResponse!!.data != null)
                    dataRepository.saveUser(serviceResponse!!.data!!.data!!)
                if (fcmToken.isNotEmpty()) {
                    updateFcmToken(fcmToken)
                }
                registerMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                registerMutableLiveData.postValue(Resource.NetworkError(NETWORK_ERROR))
            }
        }
    }

    fun saveCredentials4Biometric(username: String, password: String) {
        dataRepository.saveCredentials4Biometric(username, password)
    }


}
