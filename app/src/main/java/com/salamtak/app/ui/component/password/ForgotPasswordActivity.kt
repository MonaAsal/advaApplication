package com.salamtak.app.ui.component.password

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.ActionResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.verifynumber.VerifyNumberActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.shakeView
import com.salamtak.app.utils.toGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.progress_bar
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.toolbar_new.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject
@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_forgot_password

    val changePasswordViewModel: ChangePasswordViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun initializeViewModel() {
//        changePasswordViewModel = viewModelFactory.create(ChangePasswordViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(changePasswordViewModel.formState, ::handleFromState)
        observe(changePasswordViewModel.forgotPasswordResponse, ::handleResponse)
        observeToast(changePasswordViewModel.showToast)
        observeError(changePasswordViewModel.showError)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    fun initViews() {
        tv_toolbar_title.text = getString(R.string.forget_pass)
        et_email.filters=(Constants.DisableDecimalWithMaxLength(11))
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
        btn_reset.setOnClickListener {
            if (et_email.text.toString().isNotEmpty()) {
                changePasswordViewModel.forgotPassword(
                    et_email.text.toString().trim()
                )
                et_email.error = null
            } else
                et_email.error = getString(R.string.require_field)
        }
    }

    private fun handleResponse(resource: Resource<ActionResponse>) {
        when (resource) {
            is Resource.Loading -> {
                btn_reset.isEnabled = false
                showLoadingView(progress_bar)
            }
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
                btn_reset.isEnabled = true
                if (resource.data.status) {
                    startActivity(
                        intentFor<VerifyNumberActivity>(
                            Constants.KEY_FROM_RECOVERY to true,
                            Constants.KEY_ID to resource.data.data,
                            Constants.KEY_PHONE to et_email.text.toString().trim()
                        )
                    )
                    //finish()
                } else
                    showMessage(resource.data.message!!)
                // navigateToVerifyNumberScreen()
                //navigateToMainScreen()
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                btn_reset.isEnabled = true
                resource.errorCode?.let {
                    var error = changePasswordViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                btn_reset.isEnabled = true
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }


    private fun handleFromState(formState: ChangePasswordFormState) {

        if (formState.oldPasswordError != null) {
            et_old_password.error = formState.passwordError
            et_old_password.shakeView()
        }

        if (formState.passwordError != null) {
            et_password.error = formState.passwordError
            et_password.shakeView()
        }

        if (formState.passwordDontMatchError != null) {
            et_confirm_password.error = formState.passwordDontMatchError
            et_confirm_password.shakeView()
        }

    }

}
