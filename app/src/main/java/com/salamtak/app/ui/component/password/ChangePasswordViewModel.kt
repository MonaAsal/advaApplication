package com.salamtak.app.ui.component.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.AuthUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject
constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var changePasswordResponse: MutableLiveData<Resource<ActionResponse>> =
        authUseCase.changePasswordResponse

    var forgotPasswordResponse: MutableLiveData<Resource<ActionResponse>> =
        authUseCase.forgotPasswordResponse

    var loginMutableLiveData: MutableLiveData<Resource<SalamtakResponse<User>>> = authUseCase.loginLiveData

    private val _Form = MutableLiveData<ChangePasswordFormState>()
    val formState: LiveData<ChangePasswordFormState> = _Form

    fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {

        if (validateInput(oldPassword, newPassword, confirmPassword)) {
            val result = authUseCase.changePassword(oldPassword, newPassword, confirmPassword)
        }
    }

    private fun validateInput(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        var valid = true
        if (!Validation.isValidPassword(oldPassword, Constants.password_lenght)) {
            _Form.value =
                ChangePasswordFormState(
                    oldPasswordError = String.format(authUseCase.getLocale(),
                        App.context.getString(R.string.invalid_password_lenght),
                        Constants.password_lenght
                    )
                )
            valid = false
        }
        else if (!Validation.isValidPassword(newPassword, Constants.password_lenght)) {
            _Form.value =
                ChangePasswordFormState(
                    passwordError = String.format(authUseCase.getLocale(),
                        App.context.getString(R.string.invalid_password_lenght),
                        Constants.password_lenght
                    )
                )
            valid = false
        } else if (newPassword != confirmPassword) {
            _Form.value =
                ChangePasswordFormState(
                    passwordDontMatchError =
                    App.context.getString(R.string.password_dont_match)
                )
            valid = false
        } else {
            _Form.value =
                ChangePasswordFormState(isDataValid = true)
            valid = true
        }

        return valid
    }

    fun forgotPassword(email: String) {
        if (!Validation.isValidString(email)) {
            _Form.value =
                ChangePasswordFormState(
                    emailError = R.string.invalid_mobile_number
                )
            return
        }
        authUseCase.forgotPassword(email)
    }

    fun forgotPasswordReset(
        id: String,
        resetCode: String,
        password: String,
        confirmPassword: String,

    ) {
        if (!Validation.isValidPassword(password, Constants.password_lenght)) {
            _Form.value =
                ChangePasswordFormState(
                    passwordError = String.format(authUseCase.getLocale(),
                        App.context.getString(R.string.invalid_password_lenght),
                        Constants.password_lenght
                    )
                )
            return
        }
        else if (password != confirmPassword) {
            _Form.value =
                ChangePasswordFormState(
                    passwordDontMatchError =
                    App.context.getString(R.string.password_dont_match)
                )
            return
        } else {
            _Form.value =
                ChangePasswordFormState(isDataValid = true)
        }


        authUseCase.forgotPasswordReset(id, resetCode, password, confirmPassword,
            authUseCase.getSavedFCMToken()
        )
    }
}
