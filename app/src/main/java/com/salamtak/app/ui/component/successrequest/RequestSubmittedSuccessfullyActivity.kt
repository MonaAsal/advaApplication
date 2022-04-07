package com.salamtak.app.ui.component.successrequest

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinancialProfileCompleted

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_operation_request_submitted.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class RequestSubmittedSuccessfullyActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_operation_request_submitted

    var needFinancialInfo = false

//    @Inject
    val viewModel: SubmittedViewModel  by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    lateinit var liveChat: LiveChat

    override fun initializeViewModel() {
//        viewModel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(viewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideNotificationBar()
        super.onCreate(savedInstanceState)

        viewModel.isFinancialProfileCompleted()
        initViews()

        tv_call.setOnClickListener {
            callNumber(Constants.SALAMTAK_PHONE_NUMBER)
        }

        viewModel.getUser()?.let {
            liveChat =
                LiveChat(this, viewModel.getUserName(), it.phone)
        } ?: run {
            liveChat = LiveChat(this, viewModel.getUserName())
        }

        btn_call_center.setOnClickListener {
            liveChat.showChatWindow()
        }

    }

    private fun navigateToFinancialProfileScreen() {
//        startActivity(intentFor<FinancialInfoTypesActivity>())
        startActivity(intentFor<FinancialInfoStep1Activity>())
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun initViews() {
//        needFinancialInfo = intent.getBooleanExtra(Constants.NEED_FINANCIAL_INFO, false)

        btn_go_home.setOnClickListener {
            if (!needFinancialInfo) {

                LogUtil.LogFirebaseEvent(
                    "btn_go_home",
                    "4screen_" + this::class.java.simpleName
                )
               // navigateToMainScreen()
                navigateToMainScreen()

            } else {
                LogUtil.LogFirebaseEvent(
                    "btn_go_financial",
                    "screen_" + this::class.java.simpleName,
                    "",
                    ""
                )
                navigateToFinancialProfileScreen()
            }
        }
        iv_close.setOnClickListener {
            LogUtil.LogFireBaseBackEvent(this::class.java.simpleName)
//            if (::liveChat.isInitialized && (liveChat != null && liveChat.onBackPressed()).not())
       // navigateToMainScreen()
            navigateToMainScreen()

        }
    }


    override fun observeViewModel() {
        observe(viewModel.financialProfileCompleted, ::changeButton)
        observe(viewModel.showLoading, ::showLoadingView)
        observe(viewModel.showServerError, ::showServerErrorMessage)
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun changeButton(financialProfileCompleted: FinancialProfileCompleted) {
        btn_go_home.isEnabled = true
        needFinancialInfo = financialProfileCompleted.isCompleted.not()
        if (financialProfileCompleted.isCompleted) {
            btn_go_home.text = getString(R.string.go_to_home_page)
        } else {
            btn_go_home.text = getString(R.string.complete_your_profile)
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

    override fun onBackPressed() {
        if (::liveChat.isInitialized && liveChat.onBackPressed().not())
         //   navigateToMainScreen()
            navigateToMainScreen()

    }

}
