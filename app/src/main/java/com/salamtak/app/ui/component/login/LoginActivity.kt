package com.salamtak.app.ui.component.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.biometriclogin.BiometricPromptUtils
import com.salamtak.app.ui.component.biometriclogin.CryptographyManager
import com.salamtak.app.ui.component.password.ForgotPasswordActivity
import com.salamtak.app.ui.component.register.RegisterActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_new.*
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity() {


    val loginViewModel: LoginViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_login

    private lateinit var biometricPrompt: BiometricPrompt
    private val TAG = "EnableBiometricLogin"

    lateinit var liveChat: LiveChat
    var firebaseScreenName = "Login_Screen"

    private var cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            Constants.SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            Constants.CIPHERTEXT_WRAPPER
        )

    override fun initializeViewModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideNotificationBar()
        super.onCreate(savedInstanceState)
//        forDevelopment()

        LogUtil.LogFirebaseEvent(
            "loginOpened",
            firebaseScreenName
        )
        initViews()
        initBiometric()

        liveChat = LiveChat(this, "")
        btn_call_center.setOnClickListener {
            liveChat.showChatWindow()
        }
    }

    private fun initBiometric() {
        if (deviceHasBiometric() && loginViewModel.isBiometricSaved() && ciphertextWrapper != null) {
            iv_use_biometrics.toVisible()
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    this,
                    ::encryptAndStoreServerToken,
                    ::handleNavigation
                )

        } else
            iv_use_biometrics.toGone()

        iv_use_biometrics.setOnClickListener {
            showBiometricPromptForDecryption()
        }

    }


    private fun forDevelopment() {
        et_username.setText("01225839198")
        et_password.setText("123456")
    }

    fun initViews() {
//        et_username.afterTextChanged {
        //            loginViewModel.validateInput(
//                et_username.text.toString(),
//                et_password.text.toString()
//            )
//        }
//
        tv_toolbar_title.text = getString(R.string.sign_in)
        et_password.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        LogUtil.LogFirebaseEvent(
                            "btn_login",
                            "screen_" + this::class.java.simpleName
                        )
                        loginViewModel.login(
                            et_username.text.toString(),
                            et_password.text.toString(),
                        )
                    }
                }
                false
            }
        }


        btn_login.setOnClickListener {
            // loading.visibility = View.VISIBLE
            LogUtil.LogFirebaseEvent(
                "loginButtonPressed",
                firebaseScreenName
            )
            loginViewModel.login(et_username.text.toString(), et_password.text.toString())
        }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_signup.setOnClickListener {
            LogUtil.LogFirebaseEvent(
                "btn_register",
                "screen_" + this::class.java.simpleName

            )
            startActivity<RegisterActivity>()
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            finish()
        }

        tv_forget_password.setOnClickListener {
            startActivity<ForgotPasswordActivity>()
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

            LogUtil.LogFirebaseEvent(
                "btn_forgot_password",
                "screen_" + this::class.java.simpleName
            )
        }
    }

    override fun onBackPressed() {
        if (::liveChat.isInitialized && liveChat.onBackPressed().not())
            super.onBackPressed()
    }

    override fun observeViewModel() {
        observeToast(loginViewModel.showToast)
        observeError(loginViewModel.showError)
//        observe(loginViewModel.loginLiveData, ::handleCategoriesList)
        observe(loginViewModel.loginLiveData, ::handleLoginResponse)
        observe(loginViewModel.loginFormState, ::handleFromState)

//        observeEvent(loginViewModel.openNewsDetails, ::navigateToDetailsScreen)
//        observeSnackBarMessages(catgoriesListViewModel.showSnackBar)
//        observeToast(catgoriesListViewModel.showToast)
    }

    private fun handleFromState(loginState: LoginFormState) {

        //btn_login.isEnabled = loginState.isDataValid

        if (loginState.usernameError != null) {
            et_username.error = getString(loginState.usernameError)
            et_username.shakeView()
        }
        if (loginState.passwordError != null) {
            et_password.error = loginState.passwordError
            et_password.shakeView()
        }

    }

    private fun handleLoginResponse(loginResponse: Resource<SalamtakResponse<User>>) {
        when (loginResponse) {
            is Resource.Loading -> {
                btn_login.isEnabled = false
                showLoadingView(progress_bar)
            }
            is Resource.Success -> loginResponse.data?.let {
                LogAdjustEvent("k2j2wj")
                LogUtil.LogFirebaseEvent(
                    "loginSuccessfully",
                    firebaseScreenName
                )
                btn_login.isEnabled = true
                progress_bar.toGone()

//                persistCiphertext(KEY_BIO_NAME, et_username.text.toString())
//                persistCiphertext(KEY_BIO_PASSWORD, et_password.text.toString())

                if (et_username.text.toString().isNotEmpty() && et_password.text.toString()
                        .isNotEmpty()
                )
                    loginViewModel.saveCredentials(
                        et_username.text.toString(),
                        et_password.text.toString()
                    )

                if (deviceHasBiometric() && ciphertextWrapper == null)
                    showBiometricPromptForEncryption()
                else {
                    if (it.data!!.isPhoneVerified)
                        navigateToMainScreen()
                    else {
                        loginViewModel.requestVerifyNumber(Constants.TYPE_PHONE)
                        navigateToVerifyNumberScreen(et_username.text.toString())
                    }
                }
            }
            is Resource.NetworkError -> {
                btn_login.isEnabled = true
                progress_bar.toGone()
                loginResponse.errorCode?.let {
                    var error = loginViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                btn_login.isEnabled = true
                progress_bar.toGone()
                loginResponse.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }
    }

    private fun checkNavigation(data: User) {
        if (data.isPhoneVerified)
        // navigateToMainScreen()
            navigateToMainScreen()
        else {
            loginViewModel.requestVerifyNumber(Constants.TYPE_PHONE)
            navigateToVerifyNumberScreen(et_username.text.toString())
        }

    }


    private fun showBiometricPromptForDecryption() {
        ciphertextWrapper?.let { textWrapper ->
            val secretKeyName = Constants.secret_key_name
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    this,
                    ::decryptServerTokenFromStorage, ::handleNavigation
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        ciphertextWrapper?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext =
                    cryptographyManager.decryptData(textWrapper.ciphertext, it)
                Log.d(
                    "TAG",
                    "The token from server decrypted is ${plaintext}"
                )
                loginViewModel.loginBio()
            }
        }
    }


    private fun showBiometricPromptForEncryption(): Boolean {
        try {
            if (deviceHasBiometric()) {
                val secretKeyName = Constants.secret_key_name
                val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
                val biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                        this,
                        ::encryptAndStoreServerToken,
                        ::handleNavigation
                    )
                val promptInfo = BiometricPromptUtils.createPromptInfo(this)
                biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

    private fun encryptAndStoreServerToken(authResult: BiometricPrompt.AuthenticationResult) {

        authResult.cryptoObject?.cipher?.apply {
            loginViewModel.loginLiveData.value?.let {
                Log.d(
                    "TAG",
                    "The token from server is ${it.data!!.data!!.token}"
                )

                try {
                    val encryptedServerTokenWrapper =
                        cryptographyManager.encryptData(
                            it.data!!.data!!.token,
                            authResult.cryptoObject?.cipher!!
                        )

                    cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                        encryptedServerTokenWrapper,
                        applicationContext,
                        Constants.SHARED_PREFS_FILENAME,
                        Context.MODE_PRIVATE,
                        Constants.CIPHERTEXT_WRAPPER
                    )
                } catch (e: Exception) {
                    cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                        it.data!!.data!!.token,
                        applicationContext,
                        Constants.SHARED_PREFS_FILENAME,
                        Context.MODE_PRIVATE,
                        Constants.CIPHERTEXT_WRAPPER
                    )
                }
            }
        }

        handleNavigation()
    }

    private fun handleNavigation() {
        loginViewModel.loginLiveData.value?.let {
            it.data?.let {
                if (it.data!!.isPhoneVerified)
                //      navigateToMainScreen()
                    navigateToMainScreen()
                else {
                    loginViewModel.requestVerifyNumber(Constants.TYPE_PHONE)
                    navigateToVerifyNumberScreen(et_username.text.toString())
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (::liveChat.isInitialized)
                liveChat.handleChatAttachment(requestCode, resultCode, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
