package com.salamtak.app.ui.component.login

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.ui.component.verifynumber.VerifyFormState
import com.salamtak.app.usecase.AuthUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Constants.INSTANCE.DEVICE_ID
import com.salamtak.app.utils.Validation
import com.salamtak.app.utils.arabicNumberToEnglish
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject
constructor(private val authUseCase: AuthUseCase) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var loginLiveData = authUseCase.loginLiveData
    var verifyResponseLiveData = authUseCase.verifyResponseMutableLiveData

    var requestVerificationNumberResponseMutableLiveData: MutableLiveData<Resource<ActionResponse>> =
        authUseCase.requestVerificationResponseMutableLiveData

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _verifyForm = MutableLiveData<VerifyFormState>()
    val verifyForm: LiveData<VerifyFormState> = _verifyForm

    fun login(username: String, password: String ) {

        if (validateInput(username, password)) {
           // val deviceId = Settings.Secure.getString(App.context.contentResolver, Settings.Secure.ANDROID_ID)
            Log.i("deviceId", DEVICE_ID)
             var fcmToken:String = authUseCase.getSavedFCMToken()
              val result = authUseCase.login(
                arabicNumberToEnglish(username),
                arabicNumberToEnglish(password),
                DEVICE_ID,
                fcmToken

            )
        }
    }

    fun loginBio(username: String, password: String, fcmToken:String) {
      //  val deviceId = Settings.Secure.getString(App.context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.i("deviceId", DEVICE_ID)

        authUseCase.login(
            arabicNumberToEnglish(username),
            arabicNumberToEnglish(password),
            DEVICE_ID,
            fcmToken
        )
    }

    fun loginBio() {
       // val deviceId = Settings.Secure.getString(App.context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.i("deviceId", DEVICE_ID)

        authUseCase.loginBiometric(
            DEVICE_ID,
        )

    }

    fun saveCredentials(username: String, password: String) {
        authUseCase.saveCredentials4Biometric(username, password)
    }

    fun validateInput(username: String, password: String): Boolean {
        var valid = true
        if (!Validation.isValidUserName(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)

            valid = false
        } else if (password.isEmpty()) {
            _loginForm.value =
                LoginFormState(passwordError = App.context.getString(R.string.invalid_password))
            valid = false
        } else if (!Validation.isValidPassword(password, Constants.password_lenght)) {
            _loginForm.value =
                LoginFormState(
                    passwordError = String.format(
                        authUseCase.getLocale(),
                        App.context.getString(R.string.invalid_password_lenght),
                        Constants.password_lenght
                    )
                )
            valid = false
        } else {
            _loginForm.value =
                LoginFormState(isDataValid = true)
            valid = true
        }

        return valid
    }

    fun verifyNumber(code: String) {
        if (code.length != 4) {
            _verifyForm.value =
                VerifyFormState(codeError = R.string.invalid_verification_code)
            return
        }

        _verifyForm.value =
            VerifyFormState(isDataValid = true)
        authUseCase.verifyNumber(Constants.TYPE_PHONE, arabicNumberToEnglish(code))
    }

    fun requestVerifyNumber(type: Int) {
        authUseCase.requestVerifyNumber(type)
    }

    fun isBiometricSaved():Boolean
    {
        return authUseCase.isBiometricSaved()
    }

}
