package com.salamtak.app.ui.component.comingsoon

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.home.HomeViewModel
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_coming_soon.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class ComingSoonActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_coming_soon

    lateinit var liveChat: LiveChat

    //    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    @Inject
//    lateinit
    val homeViewModel: HomeViewModel by viewModels()

    override fun initializeViewModel() {
//        homeViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(HomeViewModel::class.java)
    }


    override fun observeViewModel() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_home.setOnClickListener {
            navigateToMainScreen()
        }
        tv_title.text = getString(R.string.comming_soon_title)
        iv_toolbar_back.setOnClickListener { onBackPressed() }

        tv_contact_us.setOnClickListener {
            callNumber(Constants.SALAMTAK_PHONE_NUMBER)
        }

        homeViewModel.getUser()?.let {
            liveChat =
                LiveChat(this, homeViewModel.getUserName(), it.phone)
        }

        btn_call_center.setOnClickListener {
            liveChat.showChatWindow()
        }

        var page = intent.getIntExtra("page", 1)
        when (page) {
            1 -> {
                tv_toolbar_title.text = getString(R.string.travel)
                iv_image.setImageResource(R.drawable.ic_travel)

                tv_sub_title.text =
                    getString(R.string.for_installing_travel)//getString(R.string.car_service_info)
            }
            2 -> {
                tv_toolbar_title.text = getString(R.string.bills)
                iv_image.setImageResource(R.drawable.ic_bills)

                tv_sub_title.text = getString(R.string.for_installing_your_bills)
            }

            3 -> {
                tv_toolbar_title.text = getString(R.string.more)
                iv_image.setImageResource(R.drawable.ic_notification)

                tv_sub_title.text = getString(R.string.other_soon_text)
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

    override fun onBackPressed() {
        if (::liveChat.isInitialized && liveChat.onBackPressed().not())
            super.onBackPressed()
    }
}