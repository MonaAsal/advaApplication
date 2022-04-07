package com.salamtak.app.ui.component.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.RegisterUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject
constructor(private val registerUseCase: RegisterUseCase) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var registerLiveData: MutableLiveData<Resource<SalamtakResponse<User>>> =
        registerUseCase.registerLiveData

//    private val _ registerResult = MutableLiveData< UserResponse>()
//    val  registerResult: LiveData< UserResponse> = _ registerResult

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    fun register(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String,
        password: String,
        confirmPassword: String,
        isChecked: Boolean,
        deviceId: String

    ) {

        if (validateInput(
                firstName,
                lastName,
                userName,
                phone,
                email,
                image,
                password,
                confirmPassword,
                isChecked
            )
        ) {
            registerUseCase.register(
                firstName,
                lastName,
                userName,
                phone,
                email,
                image,
                password,
                confirmPassword,
                deviceId,
                registerUseCase.getSavedFCMToken()
            )
        }
    }

    fun validateInput(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String, password: String, confirmPassword: String, isChecked: Boolean
    ): Boolean {
        var valid = true
        if (!isChecked) {
            _registerForm.value =
                RegisterFormState(acceptedTerms = R.string.please_agree)

            valid = false
        } else if (firstName.isNullOrEmpty()) {
            _registerForm.value = RegisterFormState(firstnameError = R.string.required_first_name)
            valid = false
        } else if (!Validation.isValidUserName(firstName)) {
            _registerForm.value =
                RegisterFormState(firstnameError = R.string.invalid_first_name)

            valid = false
        }else if(lastName.isNullOrEmpty()){
            _registerForm.value = RegisterFormState(lastnameError = R.string.required_last_name)
            valid = false

        } else if (!Validation.isValidUserName(lastName)) {
            _registerForm.value = RegisterFormState(lastnameError = R.string.invalid_last_name)
            valid = false

        }else if(phone.isNullOrEmpty()){
         _registerForm.value = RegisterFormState(phoneError = R.string.required_mobile_number)
            valid = false

        } else if (!Validation.isValidPhone(phone)) {

            _registerForm.value = RegisterFormState(phoneError = R.string.invalid_mobile_number)

            valid = false
        } else if (!Validation.isValidPassword(password, Constants.password_lenght)) {
            _registerForm.value =
                RegisterFormState(
                    passwordError = String.format(
                        registerUseCase.getLocale(),
                        App.context.resources.getString(R.string.invalid_password_lenght),
                        Constants.password_lenght
                    )
                )
            valid = false
        } else if (password != confirmPassword) {
            _registerForm.value =
                RegisterFormState(
                    passwordDontMatchError =
                    App.context.resources.getString(R.string.password_dont_match)
                )
            valid = false
        } else if (email.isNotEmpty()) {
            if (!Validation.isValidEmail(email)) {
                _registerForm.value =
                    RegisterFormState(
                        emailError =
                        App.context.resources.getString(R.string.invalidEmailAddress)
                    )
                valid = false
            }
        } else {
            _registerForm.value =
                RegisterFormState(isDataValid = true)
            valid = true
        }

        return valid
    }

    fun saveCredentials(username: String, password: String) {
        registerUseCase.saveCredentials4Biometric(username, password)
    }
}