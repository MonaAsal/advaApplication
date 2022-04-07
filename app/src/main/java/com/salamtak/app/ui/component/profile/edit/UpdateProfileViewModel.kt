package com.salamtak.app.ui.component.profile.edit

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.ProfileUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class UpdateProfileViewModel @Inject
constructor(private val profileUseCase: ProfileUseCase) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var image = MutableLiveData<String>()
    val getProfileInfoResponse = profileUseCase.getProfileInfoResponse
    val updateProfileResponse = profileUseCase.updateProfileResponse

    var formState = MutableLiveData<ProfileFormState>()
    var phoneUpdated = false//MutableLiveData<Boolean>()

    fun getProfile() {
        profileUseCase.getUser()
    }

    fun getUser(): User {
        return profileUseCase.getUser()
    }

    fun validateUpdateProfile(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ): Boolean {
        var valid = false
        if (firstName.isNullOrEmpty()) {
            formState.value =
                ProfileFormState(firstNameError = R.string.require_field)

            valid = false
        } else if (lastName.isNullOrEmpty()) {
            formState.value =
                ProfileFormState(lastNameError = R.string.require_field)

            valid = false
        } else if (phone.isNullOrEmpty()) {
            formState.value =
                ProfileFormState(phoneError = R.string.require_field)

            valid = false
        } else if (email.isNotEmpty() && !Validation.isValidEmail(email)) {

            formState.value =
                ProfileFormState(
                    emailError =
                    App.context.resources.getString(R.string.invalid_email)
                )
            valid = false

        } else {
            formState.value =
                ProfileFormState(isDataValid = true)
            valid = true
        }

        return valid
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        phone: String,
        email: String
    ) {
        if (validateUpdateProfile(firstName, lastName, phone, email)) {
            isPhoneUpdated(phone)
            profileUseCase.updateProfileInfo(
                firstName,
                lastName,
                if (email != getUser().email) email else "",
                if (phone != getUser().phone) phone else "",
                if (image.value != null) image!!.value!! else null
            )
        }
    }

    fun isPhoneUpdated(phone: String) {
        if (phone != getUser().phone)
            phoneUpdated = true

    }


}
