package com.salamtak.app.ui.component.splash

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.SharedUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit

import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class SplashViewModel @Inject
constructor(
    private val application: Application,
    private val sharedUseCase: SharedUseCase,
    dataSource: DataSource
) :
    BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _openGettingStarted = MutableLiveData<Event<Any>>()
    val openGettingStarted: LiveData<Event<Any>> = _openGettingStarted

    val openVerify = MutableLiveData<String>()

    fun checkNavigationScreen() {
        sharedUseCase.getFCMToken()

        when {
            sharedUseCase.isFirstTime() -> {
                _openGettingStarted.value = Event(true)
            }
            sharedUseCase.isLoggedIn() -> {
                var user = sharedUseCase.getUser()
//                user.basicProfile?.let {
//                    user.firstName = it.firstName
//                    user.lastName = it.lastName
//                    sharedUseCase.saveUser(user)
//                }

                when {
                    user.isPhoneVerified -> openHome.value = Event(true)
                    else -> openVerify.value = user.phone
                }
            }
            else -> openLogin.value = Event(true)
        }
    }

    /// workManager part, here temporary, will be moved
    private val workManager: WorkManager = WorkManager.getInstance(application)
    val input = MutableLiveData<String>()
    val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(Constants.WORK_MANAGER_NAME)

    fun runWorkManagerService() {
        //https://android-developers.googleblog.com/2021/04/mad-skills-workmanager-wrap-up.html
        //https://developer.android.com/codelabs/android-workmanager?hl=AR#11
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresCharging(true)
//                .setRequiresStorageNotLow(true)
                .build()

        var data = createInputDataForInput(input.value.toString())
        val imageWorker = OneTimeWorkRequestBuilder<BackgroundWorkerService>()
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setInputData(data)
            .addTag(Constants.WORK_MANAGER_NAME)
            .build()

        workManager.beginUniqueWork(//enqueueUniqueWork(
            "delayedWorker",
            ExistingWorkPolicy.KEEP,
            imageWorker
        )
        //observeWork(imageWorker.id)
    }

    private fun createInputDataForInput(input: String): Data {
        val builder = Data.Builder()
        input?.let {
            builder.putString(Constants.KEY_WORKER_INPUT, input)
        }
        return builder.build()
    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(Constants.WORK_MANAGER_NAME)
    }


}
