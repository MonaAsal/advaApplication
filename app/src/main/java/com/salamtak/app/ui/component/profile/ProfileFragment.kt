package com.salamtak.app.ui.component.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.ProfileResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.ui.component.login.LoginActivity
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.medicalhistory.MedicalProfileQuestionActivity
import com.salamtak.app.ui.component.paymentmethods.PaymentMethodsActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_back.*
import kotlinx.android.synthetic.main.toolbar_back.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar_back.tv_toolbar_title
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity


class ProfileFragment : BaseFragment() {
    private val profileActivityViewModel: ProfileActivityViewModel by viewModels()
    val viewModel: LanguageViewModel by viewModels()

    var locale = Constants.ENGLISH_LOCALE
    override val layoutId: Int
        get() = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }

    override fun onResume() {
        super.onResume()
        profileActivityViewModel.getFinancialProgress()
        bindUserInfo(profileActivityViewModel.getUserInfo()!!)
    }

    private fun initViews() {
        tv_toolbar_title.text = getString(R.string.profile)
        iv_toolbar_back.visibility=View.GONE

        tv_financial_info.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_financial_profile", this::class.java.simpleName)
            //startActivity<FinancialBasicInfoActivity>()
            activity?.startActivity<FinancialInfoStep1Activity>()
        }

        tv_medical_history.setOnClickListener {
            activity?.startActivity<MedicalProfileQuestionActivity>()

        }
        tv_favourits.setOnClickListener {
            navigateToWishList()
            // activity?.startActivity<FavouritesActivity>()

        }
        tv_change_lang.setOnClickListener { activity?.startActivity<ChooseLanguageActivity>() }
//        tv_home_visits.setOnClickListener { startActivity<HomeVisitsTrackingActivity>() }
        tv_payments.setOnClickListener {
            activity?.startActivity<PaymentMethodsActivity>()
        }
        tv_booked_operations.setOnClickListener {
            navigateToBooking()
            // activity?.startActivity<BookedRequestsActivity>()
        }
        /*   tv_change_password.setOnClickListener {
               activity?.startActivity<ChangePasswordActivity>() //no bottom bar
           }*/
        tv_signout.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_logout", this::class.java.simpleName)
            logout()
        }

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_info.setOnClickListener { profileActivityViewModel.requestVerifyNumber() }
        tv_account_setting.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_account_settings", this::class.java.simpleName)
            navigateToAccountSettings()
            //  activity?.startActivity<EditProfileActivity>()
        }
        tv_help.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_help", this::class.java.simpleName)
            navigateToHelp()
        }
        tv_about_adva.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_who_we_are", this::class.java.simpleName)
            navigateToAboutUs()
        }
        val currentLocale = viewModel.getLocale()
        if (currentLocale == "en") {
            tv_lang.text = getString(R.string.arabic)
        } else
            tv_lang.text = getString(R.string.english)
        tv_lang.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_change_lang", this::class.java.simpleName)
            if (currentLocale == "en") {
                changeLanguageTo(Constants.ARABIC_LOCALE)
            } else {
                changeLanguageTo(Constants.ENGLISH_LOCALE)
            }
        }
    }

    private fun logout() {

        activity?.supportFragmentManager?.let {
            openSalamtakDialog(
                it,
                getString(R.string.confirm_logout),
                getString(R.string.confirm_logout_message),
                R.drawable.ic_warning,
                ::yesClicked,
                ::noClicked
            )
        }
    }


    private fun noClicked() {
    }

    private fun yesClicked() {
        profileActivityViewModel.logout()
    }

    @SuppressLint("SetTextI18n")
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
        showLoadingView(progress_bar, b)
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
        observe(viewModel.restartActivity, ::handleRestartActivity)
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
                    val error = profileActivityViewModel.getToastMessage(it)
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
            startActivity(activity?.intentFor<LoginActivity>()?.clearTask()?.newTask())
            activity?.finish()
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        } else
            showMessage(resource.message!!)

    }

    private fun handleRestartActivity(b: Boolean?) {
        startActivity(activity?.intentFor<MainActivity>()!!.newTask().clearTask())
        activity?.finish()
    }

    private fun updateSelectedUI(currentLocale: String) {
        if (currentLocale == Constants.ARABIC_LOCALE) {
            iv_arabic_selected.visibility = View.VISIBLE
            iv_english_selected.visibility = View.GONE
        } else {
            iv_arabic_selected.visibility = View.GONE
            iv_english_selected.visibility = View.VISIBLE
        }
//        tv_arabic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0)
    }

    private fun changeLanguageTo(locale: String) {
        this.locale = locale
        viewModel.changeAppLanguage(locale, requireContext())
    }
}