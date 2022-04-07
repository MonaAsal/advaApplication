package com.salamtak.app.ui.component.premiumcard

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.PremiumData
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.biometriclogin.CryptographyManager
import com.salamtak.app.ui.component.health.categories.PremiumViewModel
import com.salamtak.app.ui.component.verifynumber.VerifyFormState
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_premium.*
import kotlinx.android.synthetic.main.activity_verify_premium_card.*
import kotlinx.android.synthetic.main.activity_verify_premium_card.progress_bar
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class VerifyPremiumCardActivity : BaseActivity(), MySMSBroadcastReceiver.OTPReceiveListener {
    override val layoutId: Int
        get() = R.layout.activity_verify_premium_card

    private var smsReceiver: MySMSBroadcastReceiver? = null

    //    @Inject
//    lateinit
    val premiumViewModel: PremiumViewModel by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    private var cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            Constants.SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            Constants.CIPHERTEXT_WRAPPER
        )


    var time = 60

    var timer = object : CountDownTimer(time * 1000L, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            time--
            tv_resend_code.isEnabled = false
//            tv_remaining_lbl.toVisible()
            tv_remaining.toVisible()

            tv_remaining.text = String.format(
                Locale.ENGLISH,
                SimpleDateFormat("ss").format(Date(millisUntilFinished))
            ) + " " + getString(R.string.seconds)
        }

        override fun onFinish() {
//            tv_remaining_lbl.text = getString(R.string.code_expired)
//            tv_resend_code.toVisible()
            tv_resend_code.isEnabled = true
//            tv_remaining_lbl.toGone()
            tv_remaining.toGone()
            hideKeyboard(this@VerifyPremiumCardActivity)
        }
    }


    override fun initializeViewModel() {
//        premiumViewModel = viewModelFactory.create(PremiumViewModel::class.java)
//        changePasswordViewModel = viewModelFactory.create(ChangePasswordViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(premiumViewModel.showLoading, ::showLoadingView)
        observe(premiumViewModel.showServerError, ::showServerErrorMessage)
        observe(premiumViewModel.gotpResponse, ::handleGOTPResponse)
//        observeToast(premiumViewModel.showToast)
//        observeError(premiumViewModel.showError)
        observe(premiumViewModel.votpResponse, ::handleVerifyResponse)
        observe(premiumViewModel.verifyForm, ::handleFromState)
//        observe(
//            premiumViewModel.gotpResponse,
//            ::handleResendResponse
//        )
//        observe(changePasswordViewModel.changePasswordResponse, ::handleChangePasswordResponse)
//        observe(changePasswordViewModel.forgotPasswordResponse, ::handleSendCode)
    }

    private fun handleGOTPResponse(response: PremiumData) {
        if (response.premiumStatus == "1")
            timer.start()
    }

    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_verify.isEnabled = false
            tv_resend_code.isEnabled = false
        } else {
            progress_bar.toGone()
            btn_verify.isEnabled = true
            tv_resend_code.isEnabled = true
        }
    }

    private fun handleSendCode(resource: Resource<ActionResponse>?) {
//        when (resource) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.Success -> resource.data?.let {
        progress_bar.toGone()
//            }
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = changePasswordViewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { showServerErrorMessage(it) }
//                //showToastMessage(it.errors) }
//            }
//    }

    }

    private fun handleFromState(verifyState: VerifyFormState) {
        btn_verify.isEnabled = true
        if (verifyState.codeError != null) {
            et_verify6.error = getString(verifyState.codeError)
            et_verify6.shakeView()
        }
    }


    private fun handleVerifyResponse(response: BaseResponse) {
        btn_verify.isEnabled = true
        when (response.message) {
            "10" -> {
                startActivity<PremiumCardStatusActivity>(
                    Constants.KEY_ID to premiumViewModel.bookingId,
                    Constants.KEY_REFERENCE to premiumViewModel.referenceNumber
                )
                finish()
            }
            "7" -> showMessage(getString(R.string.premium_wrong_otp))
            "8" -> showMessage(getString(R.string.premium_otp_expired))
            "5" -> showMessage(getString(R.string.premium_no_mobile_number))
            "11" -> showMessage(getString(R.string.premium_transaction_declined))
            else ->
                showMessage(getString(R.string.premium_error))
        }

//        response.let {
//            progress_bar.toGone()
        btn_verify.isEnabled = true

//            showMessage(it.message!!)
//startActivity(intentFor<CongratulationsActivity>().clearTask().newTask())
//////                    finish()
//////                    navigateToMainScreen()
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        hideNotificationBar()
        super.onCreate(savedInstanceState)

        premiumViewModel.bookingId = intent?.getStringExtra(Constants.KEY_ID)
        premiumViewModel.cardNumber = intent?.getStringExtra(Constants.KEY_CARD_NUM)
        premiumViewModel.expiryDate = intent?.getStringExtra(Constants.KEY_EXPIRY)
        premiumViewModel.referenceNumber = intent?.getStringExtra(Constants.KEY_REFERENCE)

        timer.start()
//        startSMSListener()

        btn_verify.setOnClickListener {
            verifyNumber()
            btn_verify.isEnabled = false
        }

        tv_resend_code.setOnClickListener {
            premiumViewModel.GOTP()
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        et_verify1.requestFocus()
        handleNextEditText(et_verify1, et_verify2)
        handleNextEditText(et_verify2, et_verify3)
        handleNextEditText(et_verify3, et_verify4)
        handleNextEditText(et_verify4, et_verify5)
        handleNextEditText(et_verify5, et_verify6)

        handleBackEditText(et_verify6, et_verify5)
        handleBackEditText(et_verify5, et_verify4)
        handleBackEditText(et_verify4, et_verify3)
        handleBackEditText(et_verify3, et_verify2)
        handleBackEditText(et_verify2, et_verify1)

        et_verify6.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (et_verify6.text.toString().length == 1) {
                    btn_verify.callOnClick()
                    hideKeyboard(this@VerifyPremiumCardActivity)
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
            override fun afterTextChanged(p0: Editable?) {
                if (edt1.text.toString().length == 1)     //size as per your requirement
                {
                    edt2.requestFocus()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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

        var code =
            et_verify1.text.toString() + et_verify2.text + et_verify3.text + et_verify4.text + et_verify5.text + et_verify6.text
        premiumViewModel.verifyNumber(code)
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

}
