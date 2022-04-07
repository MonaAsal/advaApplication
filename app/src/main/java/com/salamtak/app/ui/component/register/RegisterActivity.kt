package com.salamtak.app.ui.component.register

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.login.LoginActivity
import com.salamtak.app.ui.component.more.WebViewActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_new.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    val registerViewModel: RegisterViewModel by viewModels()

    lateinit var liveChat: LiveChat

    override val layoutId: Int
        get() = R.layout.activity_register

    var firebaseScreenName = "signUp_Screen"

    override fun initializeViewModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
       // hideNotificationBar()
        super.onCreate(savedInstanceState)

        LogUtil.LogFirebaseEvent(
            "signUpOpened",
            firebaseScreenName
        )
        initViews()
//        forTesting()

        liveChat = LiveChat(this, "")
        btn_call_center.setOnClickListener {
            liveChat.showChatWindow()
        }
    }

    override fun onResume() {
        super.onResume()

        LogUtil.LogFireBaseOpenEvent(this::class.java.simpleName)
    }


    private fun forTesting() {
        et_first_name.setText("Radwa")
        et_last_name.setText("Elsahn")
        et_username.setText("Radwa Elsahn")
        et_email.setText("radwaelsahn@gmail.com")
        et_phone.setText("01225839198")
        et_password.setText("123456")
        et_confirm_password.setText("123456")
    }

    override fun observeViewModel() {
        observeToast(registerViewModel.showToast)
        observeError(registerViewModel.showError)
        observe(registerViewModel.registerLiveData, ::handleUserResponse)
        observe(registerViewModel.registerFormState, ::handleFromState)
    }

    fun initViews() {
        tv_toolbar_title.text = getString(R.string.create_account)
        et_phone.filters= (Constants.DisableDecimalWithMaxLength(11))
        tv_signin.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        btn_register.setOnClickListener {
            LogUtil.LogFirebaseEvent(
                "btn_register",
                "screen_" + this::class.java.simpleName
            )
            registerViewModel.register(
                et_first_name.text.toString().trim(),
                et_last_name.text.toString().trim(),
                et_username.text.toString().trim(),
                et_phone.text.toString().trim(),
                et_email.text.toString().trim(),
                "",
                et_password.text.toString().trim(),
                et_confirm_password.text.toString().trim(),
                cb_agree_on_terms.isChecked,
                getDeviceId()
            )
        }
        tv_terms.setOnClickListener {
//            startActivity<TermsAndConditionsActivity>()

            startActivity(
                intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.terms_and_conditions),
                    Constants.KEY_URL to getString(R.string.terms_url)
                )
            )
        }
        tv_privacy_policy.setOnClickListener {
            startActivity(
                intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.privacy_policy),
                    Constants.KEY_URL to getString(R.string.privacy_policy_url)
                )
            )
        }

        tv_agree_on_terms.setOnClickListener {
            cb_agree_on_terms.isChecked = cb_agree_on_terms.isChecked.not()
        }
    }

    fun getDeviceId(): String {
        val deviceId =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.i("deviceId", deviceId)
        return deviceId
    }

    override fun onBackPressed() {
        if (::liveChat.isInitialized && liveChat.onBackPressed().not())
            super.onBackPressed()
    }


    private fun handleFromState(registerState: RegisterFormState) {

//        btn_register.isEnabled = registerState.isDataValid

        if (registerState.acceptedTerms != null) {
            cb_agree_on_terms.shakeView()
            showMessage(getString(registerState.acceptedTerms))
        }
        if (registerState.firstnameError != null) {
            et_first_name.error = getString(registerState.firstnameError)
            et_first_name.shakeView()
        }
        if (registerState.lastnameError != null) {
            et_last_name.error = getString(registerState.lastnameError)
            et_last_name.shakeView()
        }
        if (registerState.passwordError != null) {
            et_password.error = registerState.passwordError
            et_password.shakeView()
        }
        if (registerState.phoneError != null) {
            et_phone.error = getString(registerState.phoneError)
            et_phone.shakeView()
        }

        if (registerState.emailError != null) {
            et_email.error = registerState.emailError
            et_email.shakeView()
        }

        if (registerState.passwordDontMatchError != null) {
            et_confirm_password.error = registerState.passwordDontMatchError
            et_confirm_password.shakeView()
        }

    }

    private fun handleUserResponse(UserResponse: Resource<SalamtakResponse<User>>) {
        when (UserResponse) {
            is Resource.Loading -> {
                btn_register.isEnabled = false
                showLoadingView(progress_bar)
            }
            is Resource.Success -> UserResponse.data?.let {
                LogAdjustEvent("7x05a1")
                LogUtil.LogFirebaseEvent(
                    "registration_completed",
                    "screen_" + this::class.java.simpleName,
                    "userId",
                    it.data?.let { it.id } ?: ""
                )

                btn_register.isEnabled = true
                progress_bar.toGone()

//                if (et_username.text.toString().isNotEmpty() && et_password.text.toString()
//                        .isNotEmpty()
//                )
                registerViewModel.saveCredentials(
                    et_username.text.toString().trim(),
                    et_password.text.toString().trim()
                )

                navigateToVerifyNumberScreen(et_phone.text.toString().trim())
            }

            is Resource.NetworkError -> {
                LogAdjustEvent("8r33gv")
                btn_register.isEnabled = true
                progress_bar.toGone()
                UserResponse.errorCode?.let {
                    var error = registerViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }

            is Resource.DataError -> {
                LogAdjustEvent("8r33gv")
                btn_register.isEnabled = true
                progress_bar.toGone()
                UserResponse.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
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
