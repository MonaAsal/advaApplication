package com.salamtak.app.ui.component.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.ProfileUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class LanguageViewModel @Inject
constructor(private val profileUseCase: ProfileUseCase) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var otherLang = MutableLiveData<String>()
    var restartActivity = MutableLiveData<Boolean>()
    val contactUsResponseLiveData = profileUseCase.contactUsResponseLiveData
    val changeLanguageResponse = profileUseCase.changeLanguageResponse
    fun logout() {
        profileUseCase.logout()
    }

    fun getOtherLanguageString(): String {

        return if (profileUseCase.toggleLocale() == Constants.ENGLISH_LOCALE)
            Constants.ENGLISH_LOCALE
        else Constants.ARABIC_LOCALE
    }

    fun changeAppLanguage(locale: String, context: Context) {

        LocaleHelper.setLocale(context, locale)
        //profileUseCase.setLocale(locale)

        restartActivity.value = true

        callChangeLanguage(locale)

    }

    fun callChangeLanguage(lang: String) {
        return profileUseCase.callChangeLanguage(lang)
    }

    fun getLocale(): String {
        return profileUseCase.getLocaleName()
    }


    fun contactUs(
        Phone: String,
        Message: String
    ) {
        if (Phone.isNotEmpty() && Message.isNotEmpty())
            profileUseCase.contactUs(Phone, Message)
    }

    fun getUserPhone(): String
    {
        return profileUseCase.getUser().phone
    }
    fun getUserName(): String {
        try {
            return profileUseCase.getUser()!!.firstName + " " + profileUseCase.getUser()!!.lastName
        } catch (e: Exception) {
            return ""
        }
    }

    fun getUser(): User {
        return profileUseCase.getUser()!!
    }


}
