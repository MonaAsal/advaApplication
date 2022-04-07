package com.salamtak.app.ui.component.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.ProfileResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.bookingrequests.BookedRequestsActivity
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.ui.component.health.favourites.FavouritesActivity
import com.salamtak.app.ui.component.login.LoginActivity
import com.salamtak.app.ui.component.medicalhistory.MedicalProfileQuestionActivity
import com.salamtak.app.ui.component.password.ChangePasswordActivity
import com.salamtak.app.ui.component.paymentmethods.PaymentMethodsActivity
import com.salamtak.app.ui.component.profile.edit.EditProfileActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.toolbar_profile.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_profile

    val profileActivityViewModel: ProfileActivityViewModel by viewModels()

    override fun initializeViewModel() {
//        profileActivityViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(ProfileActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        profileActivityViewModel.getFinancialProgress()
        bindUserInfo(profileActivityViewModel.getUserInfo()!!)
    }

    private fun initViews() {
        tv_toolbar_title.text = getString(R.string.my_profile)
        tv_financial_info.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_financial_profile", this::class.java.simpleName)
            //startActivity<FinancialBasicInfoActivity>()
            startActivity<FinancialInfoStep1Activity>()
        }

        tv_medical_history.setOnClickListener { startActivity<MedicalProfileQuestionActivity>() }
        tv_favourits.setOnClickListener {
            startActivity<FavouritesActivity>()
        }
        tv_change_lang.setOnClickListener { startActivity<ChooseLanguageActivity>() }
//        tv_home_visits.setOnClickListener { startActivity<HomeVisitsTrackingActivity>() }
        tv_payments.setOnClickListener { startActivity<PaymentMethodsActivity>() }
        tv_booked_operations.setOnClickListener { startActivity<BookedRequestsActivity>() }
        tv_change_password.setOnClickListener { startActivity<ChangePasswordActivity>() }
        tv_signout.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_logout", this::class.java.simpleName)
            logout()
        }

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_info.setOnClickListener { profileActivityViewModel.requestVerifyNumber() }
        tv_edit.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_change_profile", this::class.java.simpleName)
            startActivity<EditProfileActivity>()
        }
    }

    private fun logout() {

        openSalamtakDialog(
            supportFragmentManager,
            getString(R.string.confirm_logout),
            getString(R.string.confirm_logout_message),
            R.drawable.ic_warning,
            ::yesClicked,
            ::noClicked
        )
    }


    private fun noClicked() {
    }

    private fun yesClicked() {
        profileActivityViewModel.logout()
    }

    private fun bindUserInfo(userInfo: User) {
        if (userInfo != null) {
            if (userInfo.email.isNullOrEmpty())
                tv_email.toGone()
            else
                tv_email.text = userInfo.email
            tv_phone.text = userInfo.phone
//            if (userInfo.basicProfile != null) {
            iv_user_image.loadCircleImage(userInfo.imageUrl)
            tv_username.text =
                userInfo.firstName + " " + userInfo.lastName
//            }

            // if (userInfo.isMailVerified)
            tv_info.visibility = View.GONE
        }

    }

    private fun showLoadingView(b: Boolean) {
        showLoadingView(
            progress_bar, b
        )
    }


    override fun observeViewModel() {
        observe(profileActivityViewModel.showLoading, ::showLoadingView)
        observe(profileActivityViewModel.showServerError, ::showServerErrorMessage)
        observeToast(profileActivityViewModel.showToast)
        observe(profileActivityViewModel.logoutResponseLiveData, ::handleLogoutResponse)
        observe(
            profileActivityViewModel.requestVerificationResponseMutableLiveData,
            ::handleVerificationResponse
        )
        observe(profileActivityViewModel.financialResponse, ::showProgress)
    }

    private fun showProgress(baseResponse: BaseResponse) {
        when (baseResponse.message) {
            "0" -> {
                progress_completion.progress = 0
                tv_complition.text = "0%"
            }
            "1" -> {
                progress_completion.progress = 33
                tv_complition.text = "33%"
            }
            "2" -> {
                progress_completion.progress = 66
                tv_complition.text = "66%"
            }
            "3" -> {
                progress_completion.progress = 100
                tv_complition.text = "100%"
            }
        }
    }


    private fun handleProfileInfoResponse(resource: Resource<ProfileResponse>?) {
        when (resource) {
//            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> {
                progress_bar.toGone()
//                if (resource.data?.data!!.isMailVerified)
//                    tv_info.visibility = View.GONE
//                else
//                    tv_info.visibility = View.VISIBLE
            }

            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = profileActivityViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
            }
        }
    }

    private fun handleVerificationResponse(resource: ActionResponse) {
        showMessage(resource.data!!)
    }

    private fun handleLogoutResponse(resource: ActionResponse) {

        if (resource.status!!) {
            startActivity(intentFor<LoginActivity>().clearTask().newTask())
            finish()
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        } else
            showMessage(resource.message!!)

    }

}
