package com.salamtak.app.usecase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adjust.sdk.Adjust
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.salamtak.app.App
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.Resource
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.Notification
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.data.entities.responses.PaymentMethodsResponse
import com.salamtak.app.data.error.Error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class SharedUseCase @Inject
constructor(
    private val dataRepository: DataSource,
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    val showLoadingLiveData = MutableLiveData<Boolean>()
    val errorCodeLiveData = MutableLiveData<Int>()
    val errorResponse = MutableLiveData<ErrorResponse>()
    val changeLanguageResponse = MutableLiveData<Resource<ActionResponse>>()
    val financialProfileCompleted = MutableLiveData<Boolean>()

    private val _requestVerificationResponseMutableLiveData =
        MutableLiveData<Resource<ActionResponse>>()
    val requestVerificationResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
        _requestVerificationResponseMutableLiveData


    private val _paymentMethodsResponse =
        MutableLiveData<Resource<PaymentMethodsResponse>>()
    val paymentMethodsResponse = _paymentMethodsResponse

    private val _deletePaymentMethodsResponse =
        MutableLiveData<Resource<ActionResponse>>()
    val deletePaymentMethodsResponse = _deletePaymentMethodsResponse

    private val _defaultPaymentMethodsResponse =
        MutableLiveData<Resource<ActionResponse>>()
    val defaultPaymentMethodsResponse = _defaultPaymentMethodsResponse

    private val _addCardResponse = MutableLiveData<Resource<ActionResponse>>()
    val addCardResponse: MutableLiveData<Resource<ActionResponse>> =
        _addCardResponse

    val updateTokenResponse = MutableLiveData<Resource<ActionResponse>>()

    //    val notifications = MutableLiveData<List<Notification>>()
    val notifications = MutableLiveData<List<Notification>>()
    var NotificationsCount=0


    fun saveSelectedCity(city: City) {
        dataRepository.saveSelectedCity(city)
    }

    fun getSelectedCity() : City {
        return dataRepository.getSelectedCity()!!
    }


    fun isLoggedIn(): Boolean {
        return (dataRepository?.getUser() == null).not()
    }

    fun getUser(): User {
        return dataRepository!!.getUser()!!
    }

    fun saveUser(user: User) {
        dataRepository!!.saveUser(user)
    }

    fun needFinancialInfo(): Boolean {
        return dataRepository?.needFinancialInfo()
    }

    fun saveNeedFinancialInfo(boolean: Boolean) {
        dataRepository.saveNeedFinancialInfo(boolean)
    }

    fun isFirstTime(): Boolean {
        return dataRepository?.isFirstTime()
    }

    fun notFirstTime() {
        dataRepository?.notFirstTime()
    }

//    fun shouldAddFinancialInfo(): Boolean {
//        return dataRepository?.needFinancialInfo()
//    }

    fun toggleLocale(): String {
        return dataRepository?.toggleLocale()
    }


    fun requestVerifyNumber(type: Int) {
        var serviceResponse: Resource<ActionResponse>?
        requestVerificationResponseMutableLiveData.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository?.requestVerifyNumber(type)
                requestVerificationResponseMutableLiveData.postValue(serviceResponse)
            } catch (e: Exception) {
                requestVerificationResponseMutableLiveData.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    ////////////////
    fun getSavedFCMToken(): String {
        var fcmToken = dataRepository?.getFCMToken()
        Log.e("fcmToken", fcmToken)
        return fcmToken
    }

    fun getFCMToken() {
        val token = dataRepository?.getFCMToken()
        Log.e("FCMtoken", token)
        if (token == null || token.isEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Firebase", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d("FCM TOKEN", token)
                if (token.isNotEmpty()) {
                    dataRepository?.saveFCMToken(token!!)
                    Adjust.setPushToken(token, App.context)
                    updateFcmToken(token)
                }
            })

        } else {
            Adjust.setPushToken(token, App.context)
            updateFcmToken(token)
            //FCMNotifier.getInstance().post(token)
        }
    }

    fun getLocaleName(): String {
        return dataRepository.getLocale()
    }

    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }
//
//    fun getLocale(): String {
//        return dataRepository?.getLocale()
//    }


    fun getPaymentMethods() {
        var serviceResponse: Resource<PaymentMethodsResponse>?
        _paymentMethodsResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository?.getPaymentMethods()
                if (serviceResponse!!.data?.data?.size!! > 0) {
                    for (paymentMethod in serviceResponse!!.data?.data!!) {
                        if (paymentMethod.isDefault)
                            dataRepository?.saveDefaultCard(paymentMethod.id)
                    }
                }
                _paymentMethodsResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _paymentMethodsResponse.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun deletePaymentMethod(cardId: String) {
        var serviceResponse: Resource<ActionResponse>?
        _deletePaymentMethodsResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository?.deleteCard(cardId)
                _deletePaymentMethodsResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _deletePaymentMethodsResponse.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun toggleDefaultPaymentMethod(cardId: String, default: Boolean) {

        var serviceResponse: Resource<ActionResponse>?
        _defaultPaymentMethodsResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse =
                    dataRepository?.setDefaultCard(cardId)

                dataRepository?.saveDefaultCard(if (default) cardId else "")
                _defaultPaymentMethodsResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _defaultPaymentMethodsResponse.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun getDefaultCard(): String {
        return dataRepository?.getDefaultCardId()
    }

    fun addCard() {
        var serviceResponse: Resource<ActionResponse>?
        _addCardResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository?.addCard()
                _addCardResponse.postValue(serviceResponse)
            } catch (e: Exception) {
                _addCardResponse.postValue(
                    Resource.NetworkError(
                        Error.NETWORK_ERROR
                    )
                )
            }
        }
    }

    fun updateFcmToken(
        token: String
    ) {
        if (token.isNotEmpty() && Session.xAccessToken.isNotEmpty()) {
            var serviceResponse: Resource<ActionResponse>
            updateTokenResponse.postValue(Resource.Loading())
            launch {
                try {
                    serviceResponse = dataRepository?.updateFcmToken(token)

                    updateTokenResponse.postValue(serviceResponse)

                } catch (e: Exception) {
                    updateTokenResponse.postValue(Resource.NetworkError(Error.NETWORK_ERROR))
                }
            }
        } else
        {
            Log.e("FCMtoken"," or session empty")
        }
    }

    fun saveNotifications(notification: Notification) {

        launch {
            try {
                dataRepository?.saveNotification(notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteNotifications(notification: Notification) {

        launch {
            try {
                dataRepository?.deleteNotifications(notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAllNotifications() {
        launch {
            try {
                var list = dataRepository?.getAllNotifications()
                for (i  in list.indices){
                    Log.d("getAllNotifications",list.get(i).id.toString())
                    Log.d("getAllNotifications",list.get(i).body)
                }
                notifications.postValue(list)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @JvmName("getNotificationsCount1")
    fun getNotificationsCount():Int {
        launch {
            try {
                val list = dataRepository?.getAllNotifications()
                NotificationsCount = list.size
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return NotificationsCount
    }

    fun callChangeLanguage(lang: String) {
        var serviceResponse: Resource<ActionResponse>?
        changeLanguageResponse.postValue(Resource.Loading())
        launch {
            try {
                serviceResponse = dataRepository.changeLanguage(lang)
                changeLanguageResponse.postValue(serviceResponse!!)
            } catch (e: Exception) {
                changeLanguageResponse.postValue(Resource.NetworkError(Error.NETWORK_ERROR))
            }
        }
    }

//    fun isFinancialProfileCompleted() {
//        var serviceResponse: Resource<FinancialProfileCompleted>?
//        changeLanguageResponse.postValue(Resource.Loading())
//        launch {
//            try {
//                serviceResponse = dataRepository.isFinancialProfileCompleted()
//                financialProfileCompleted.postValue(serviceResponse!!.data!!.isCompleted)
//            } catch (e: Exception) {
////                changeLanguageResponse.postValue(Resource.NetworkError(Error.NETWORK_ERROR))
//            }
//        }
//
//    }

}