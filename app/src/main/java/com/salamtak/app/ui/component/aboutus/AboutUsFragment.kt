package com.salamtak.app.ui.component.aboutus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title


class AboutUsFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_about_us

    override fun observeViewModel() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        tv_toolbar_title.text = getString(R.string.about_adva)
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        iv_facebook.setOnClickListener { openLink("https://www.facebook.com/ADVAEgypt") }
        iv_instagram.setOnClickListener { openLink("https://www.instagram.com/advaegypt/") }
        iv_youtube.setOnClickListener { openLink("https://www.youtube.com/channel/UCgxj4gcWd4ycFMBTWmeiKLA") }

        btn_contact_us.setOnClickListener {
            // startActivity<ContactUsActivity>()
            val intent = Intent(Intent.ACTION_DIAL)

            intent.data = Uri.parse("tel:" + Constants.SALAMTAK_PHONE_NUMBER)
            startActivity(intent)
        }


    }
    private fun openLink(link: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }
}
