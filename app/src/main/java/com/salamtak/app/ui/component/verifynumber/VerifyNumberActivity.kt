package com.salamtak.app.ui.component.verifynumber

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.widget.addTextChangedListener
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.biometriclogin.BiometricPromptUtils
import com.salamtak.app.ui.component.biometriclogin.CryptographyManager
import com.salamtak.app.ui.component.login.LoginActivity
import com.salamtak.app.ui.component.login.LoginViewModel
import com.salamtak.app.ui.component.password.ChangePasswordActivity
import com.salamtak.app.ui.component.password.ChangePasswordViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_verify_number.*
import kotlinx.android.synthetic.main.activity_verify_number.progress_bar
import kotlinx.android.synthetic.main.toolbar_back.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class VerifyNumberActivity : BaseActivity(), MySMSBroadcastReceiver.OTPReceiveListener {
    override val layoutId: Int
        get() = R.layout.activity_verify_number

    private var smsReceiver: MySMSBroadcastReceiver? = null

    val loginViewModel: LoginViewModel by viewModels()

    val changePasswordViewModel: ChangePasswordViewModel by viewModels()

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

    override fun observeViewModel() {
        observeToast(loginViewModel.showToast)
        observeError(loginViewModel.showError)
        observe(loginViewModel.verifyResponseLiveData, ::handleVerifyResponse)
        observe(loginViewModel.verifyForm, ::handleFromState)
        observe(
            loginViewModel.requestVerificationNumberResponseMutableLiveData,
            ::handleResendResponse
        )
        observe(changePasswordViewModel.changePasswordResponse, ::handleChangePasswordResponse)
        observe(changePasswordViewModel.forgotPasswordResponse, ::handleSendCode)
    }

    private fun handleSendCode(resource: Resource<ActionResponse>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = changePasswordViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }

    private fun handleChangePasswordResponse(resource: Resource<ActionResponse>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
                if (resource.data.status) {
                    startActivity(
                        intentFor<VerifyNumberActivity>(
                            Constants.KEY_FROM_RECOVERY to true,
                            Constants.KEY_ID to resource.data.data,
                            Constants.KEY_PHONE to intent.getStringExtra(Constants.KEY_PHONE)
                        )
                    )
                    finish()
                } else
                    showMessage(resource.data.message!!)
                // navigateToVerifyNumberScreen()
                //navigateToMainScreen()
            }
            is Resource.NetworkError -> {
                resource.errorCode?.let {
                    var error = changePasswordViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }

    private fun handleResendResponse(resource: Resource<ActionResponse>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = loginViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }

    private fun handleFromState(verifyState: VerifyFormState) {

        if (verifyState.codeError != null) {
            et_verify4.error = getString(verifyState.codeError)
            et_verify4.shakeView()
        }
    }


    private fun handleVerifyResponse(resource: Resource<SalamtakResponse<User>>) {
        when (resource) {
            is Resource.Loading -> {
                showLoadingView(progress_bar)
                btn_verify.isEnabled = false
            }
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
                btn_verify.isEnabled = true
                LogAdjustEvent("cpvexm")

                if (resource.data.status) {
                    if (deviceHasBiometric())
                        showBiometricPromptForEncryption()
//                    startActivity(intentFor<CongratulationsActivity>().clearTask().newTask())
//                    finish()
//                    navigateToMainScreen()
                } else
                    showMessage(resource.data.message!!)
            }
            is Resource.NetworkError -> {
                btn_verify.isEnabled = true
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = loginViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                btn_verify.isEnabled = true
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideNotificationBar()
        super.onCreate(savedInstanceState)

//        startSMSListener()
        et_verify1.requestFocus()
        val phone = intent.getStringExtra(Constants.KEY_PHONE) ?: "01225839198"
        tv_toolbar_title.text = getString(R.string.phone_verification)
        tv_title.text =
            String.format(getString(R.string.please_enter_your_verification_code), phone)

        btn_verify.setOnClickListener {
            var fromRecovery = intent.getBooleanExtra(Constants.KEY_FROM_RECOVERY, false)
            if (fromRecovery) {
                var code =
                    et_verify1.text.toString() + et_verify2.text + et_verify3.text + et_verify4.text
                startActivity(
                    intentFor<ChangePasswordActivity>(
                        Constants.KEY_FROM_RECOVERY to true,
                        Constants.KEY_VERIFICATION_CODE to code,
                        Constants.KEY_ID to intent.getStringExtra(Constants.KEY_ID)
                    )
                )
//                finish()
            } else
                verifyNumber()
        }

        tv_resend_code.setOnClickListener {
            var fromRecovery = intent.getBooleanExtra(Constants.KEY_FROM_RECOVERY, false)
            if (fromRecovery) {
                var phone = intent.getStringExtra(Constants.KEY_PHONE)
                changePasswordViewModel.forgotPassword(
                    phone!!
                )
            } else
                loginViewModel.requestVerifyNumber(Constants.TYPE_PHONE)
        }

        iv_toolbar_back.setOnClickListener {
            startActivity<LoginActivity>()
        }

        et_verify1.requestFocus()
        handleNextEditText(et_verify1, et_verify2)
        handleNextEditText(et_verify2, et_verify3)
        handleNextEditText(et_verify3, et_verify4)

        handleBackEditText(et_verify4, et_verify3)
        handleBackEditText(et_verify3, et_verify2)
        handleBackEditText(et_verify2, et_verify1)

        et_verify4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (et_verify4.text.toString().length == 1) {
                    btn_verify.callOnClick()
                    hideKeyboard(this@VerifyNumberActivity)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })



    }


    fun handleNextEditText(edt1: EditText, edt2: EditText) {
        edt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ed : Editable?) {
                if (edt1.text.toString().length == 1)     //size as per your requirement
                {
                    edt2.requestFocus()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edt1.text.toString().length > 1)     //size as per your requirement
                {
                }
            }
        })
    }

    private fun handleBackEditText(edt2: EditText, edt1: EditText) {

        edt2?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {

                edt1?.requestFocus()

                return@OnKeyListener false
            }

            false
        })

    }

    private fun verifyNumber() {

        var code = et_verify1.text.toString() + et_verify2.text + et_verify3.text + et_verify4.text
        loginViewModel.verifyNumber(code)
    }

    override fun onOTPReceived(otp: String) {
        Toast.makeText(this, otp, Toast.LENGTH_SHORT).show()

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
//        otpTxtView.text = "Your OTP is: $otp"
        Log.e("OTP Received", otp)
    }

    override fun onOTPTimeOut() {
//        otpTxtView.setText("Timeout")
        //  Toast.makeText(this, " SMS retriever API Timeout", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver!!)
        }
    }


    private fun startSMSListener() {
        try {
            smsReceiver = MySMSBroadcastReceiver()
            smsReceiver!!.initOTPListener(this)

            val intentFilter = IntentFilter()
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
            this.registerReceiver(smsReceiver, intentFilter)

            val client = SmsRetriever.getClient(this)

            val task = client.startSmsRetriever()
            task.addOnSuccessListener {
                // API successfully started
            }

            task.addOnFailureListener {
                // Fail to start API
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showBiometricPromptForEncryption() {
        if (deviceHasBiometric()) {
            val secretKeyName = Constants.secret_key_name
//            cryptographyManager = CryptographyManager()
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    this,
                    ::encryptAndStoreServerToken,
                    ::navigateToMainScreen
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
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
                            this
                        )

                    cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                        encryptedServerTokenWrapper,
                        applicationContext,
                        Constants.SHARED_PREFS_FILENAME,
                        Context.MODE_PRIVATE,
                        Constants.CIPHERTEXT_WRAPPER
                    )
                } catch (e: java.lang.Exception) {
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

       // navigateToMainScreen()
        navigateToMainScreen()

    }


}
