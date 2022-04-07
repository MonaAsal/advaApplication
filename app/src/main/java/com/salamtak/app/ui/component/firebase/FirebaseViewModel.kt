package com.salamtak.app.ui.component.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.Resource
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.Notification
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.error.Error
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.SharedUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject
constructor(private val sharedUseCase: SharedUseCase) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val notifications = sharedUseCase.notifications

    val openVisitTracking = MutableLiveData<String>()
    val openOperationTracking = MutableLiveData<String>()

    fun updateFcmToken(token: String) {
        sharedUseCase.updateFcmToken(token)
    }


    fun saveNotification(notification: Notification) {
        sharedUseCase.saveNotifications(notification)
    }

    fun getAllNotifications() {
        try {
            sharedUseCase.getAllNotifications()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun getNotificationCount():Int {
           return sharedUseCase.getNotificationsCount()
    }


    fun deleteNotification(notification: Notification) {
        sharedUseCase.deleteNotifications(notification)
    }

    fun onNotificationClicked(notification: Notification) {
//        when (notification.type) {
//            "1" -> {
//                openOperationTracking.value = notification.content
//            }
//            "2" -> {
//                openVisitTracking.value = notification.content
//            }
//            //else

//        }
//        if (notification.type.toInt() != 11)
        Log.e("content", notification.content!!)
        if (notification.content.isNullOrEmpty().not())
            openOperationTracking.value = notification.content!!
    }

    fun getLocale(): String {
        return sharedUseCase.getLocaleName()
    }

}
