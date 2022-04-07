package com.salamtak.app.ui.component.password

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_new.*

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_change_password

    //
    val changePasswordViewModel: ChangePasswordViewModel by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun initializeViewModel() {
//        changePasswordViewModel = viewModelFactory.create(ChangePasswordViewModel::class.java)
    }

    override fun observeViewModel() {
        observeToast(changePasswordViewModel.showToast)
        observeError(changePasswordViewModel.showError)
        observe(changePasswordViewModel.formState, ::handleFromState)
        observe(changePasswordViewModel.changePasswordResponse, ::handleResponse)
        observe(changePasswordViewModel.loginMutableLiveData, ::handleResetPasswordResponse)
    }

    private fun handleResetPasswordResponse(resource: Resource<SalamtakResponse<User>>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()

                if (resource.data.status) {
                    showMessage(getString(R.string.password_changed_successfully))
                 //   navigateToMainScreen()
                    navigateToMainScreen()
                }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    fun initViews() {

        var fromRecovery = intent.getBooleanExtra(Constants.KEY_FROM_RECOVERY, false)
        if (fromRecovery) {
            tv_lbl_old_password.visibility = View.GONE
            et_old_password.visibility = View.GONE
        }
        tv_toolbar_title.text = getString(R.string.change_password)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        btn_change_password.setOnClickListener {
            if (fromRecovery) {
                changePasswordViewModel.forgotPasswordReset(
                    intent.getStringExtra(Constants.KEY_ID)!!,
                    intent.getStringExtra(Constants.KEY_VERIFICATION_CODE)!!,
                    et_password.text.toString(),
                    et_confirm_password.text.toString(),

                )
            } else {
                changePasswordViewModel.changePassword(
                    et_old_password.text.toString(),
                    et_password.text.toString(),
                    et_confirm_password.text.toString()
                )
            }
        }
    }

    fun yesClicked() {
        navigateToLoginScreen(Event(0))
    }

    private fun handleResponse(resource: Resource<ActionResponse>) {
        when (resource) {
            is Resource.Loading -> {
                btn_change_password.isEnabled = false
                showLoadingView(progress_bar)
            }
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
                btn_change_password.isEnabled = true
                if (resource.data.status) {
                    showMessage(resource.data.data!!, ::yesClicked)
//                    navigateToMainScreen()
                }
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                btn_change_password.isEnabled = true
                resource.errorCode?.let {
                    var error = changePasswordViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                btn_change_password.isEnabled = true
                resource.errorResponse?.let { showServerErrorMessage(it) }
                //showToastMessage(it.errors) }
            }
        }

    }


    private fun handleFromState(formState: ChangePasswordFormState) {

        if (formState.oldPasswordError != null) {
            et_old_password.error = formState.oldPasswordError
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
