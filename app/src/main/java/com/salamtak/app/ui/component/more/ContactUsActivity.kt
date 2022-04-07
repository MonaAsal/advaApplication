package com.salamtak.app.ui.component.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.ContactUsResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.profile.LanguageViewModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.shakeView
import com.salamtak.app.utils.toGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_contact_us.et_phone
import kotlinx.android.synthetic.main.activity_contact_us.progress_bar
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.activity_wedding_request.*

import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class ContactUsActivity : BaseActivity() {


   val viewModel: LanguageViewModel  by viewModels()
    override val layoutId: Int
        get() = R.layout.activity_contact_us

    lateinit var liveChat: LiveChat

    override fun initializeViewModel() {
//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(LanguageViewModel::class.java)
    }


    override fun observeViewModel() {
        observe(viewModel.contactUsResponseLiveData, ::handleResponse)
    }

    private fun handleResponse(resource: Resource<ContactUsResponse>) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> resource.data?.let {
                progress_bar.toGone()
                if (resource.data.status) {
                    showMessage(
                        getString(R.string.message_sent_successfully),
                        ::yesClicked
                    )

//                    openSalamtakDialog(
//                        "",
//                        getString(R.string.message_sent_successfully),
//                        R.drawable.ic_warning,
//                        ::yesClicked,
//                        ::yesClicked
//                    )


                    // showMessage(getString(R.string.message_sent_successfully))
//                    val handler = Handler()
//                    handler.postDelayed( {
//                        navigateToMainScreen()
//                    }, 5000)

                }
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = viewModel.getToastMessage(it)
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

    private fun yesClicked() {
        navigateToMainScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tv_toolbar_title.text = getString(R.string.contact_us)
        iv_toolbar_back.setOnClickListener { onBackPressed() }

        et_phone.setText(viewModel.getUserPhone())
        et_phone.filters = (Constants.DisableDecimalWithMaxLength(11))
        tv_call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)

            intent.data = Uri.parse("tel:" + Constants.SALAMTAK_PHONE_NUMBER)
            startActivity(intent)
        }

        btn_done.setOnClickListener {
            if (et_phone.text.toString().isNotEmpty()) {
                if (et_message.text.toString().isNotEmpty())
                    viewModel.contactUs(et_phone.text.toString(), et_message.text.toString())
                else {
                    et_message.error = getString(R.string.require_field)
                    et_message.shakeView()
                }
            } else {
                et_phone.error = getString(R.string.require_field)
                et_phone.shakeView()
            }
        }

        viewModel.getUser()?.let {
            liveChat =
                LiveChat(this, viewModel.getUserName(), it.phone)
        }
        btn_call_center.setOnClickListener {
            liveChat.showChatWindow()
        }


    }

    override fun onBackPressed() {
        if (::liveChat.isInitialized && liveChat.onBackPressed().not())
            super.onBackPressed()
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
