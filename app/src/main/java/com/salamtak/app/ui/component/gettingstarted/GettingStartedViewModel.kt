package com.salamtak.app.ui.component.gettingstarted

import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.SharedUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class GettingStartedViewModel @Inject
constructor(private val sharedUseCase: SharedUseCase) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

//    private val _openHome = MutableLiveData<Event<Any>>()
//    val openHome: LiveData<Event<Any>> = _openHome
//
//    private val _openLogin = MutableLiveData<Event<Any>>()
//    val openLogin: LiveData<Event<Any>> = _openLogin

    fun notFirstTime() {
        sharedUseCase.notFirstTime()
    }

    fun checkNavigationScreen() {
        when {
            sharedUseCase.isLoggedIn() -> {
                openHome.value = Event(true)
            }
            else -> openLogin.value = Event(true)
        }
    }


}
