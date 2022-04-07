package com.salamtak.app.ui.component.more

import android.content.pm.PackageManager
import android.os.Bundle
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity


class MoreFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_more

    override fun observeViewModel() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    private fun initViews() {
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.help)
        try {
            val pInfo = context?.packageManager!!.getPackageInfo(requireContext().packageName, 0)
//            var version = pInfo.versionName
            tv_version.text = String.format(getString(R.string.version_number), pInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        v_terms.setOnClickListener {

            startActivity(
                activity?.intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.terms_and_conditions),
                    Constants.KEY_URL to getString(R.string.terms_url)
                )
            )
        }

        v_change_pass.setOnClickListener {
            activity?.startActivity<ContactUsActivity>()
        }

      /*  v_info.setOnClickListener {
            startActivity(intentFor<TermsAndConditionsActivity>(Constants.KEY_TITLE to Constants.WHO_WE_ARE))
            activity?.startActivity<AboutUsActivity>()
        }
      */
        v_edit_profile.setOnClickListener {
            startActivity(
                activity?.intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.faq_s),
                    Constants.KEY_URL to getString(R.string.faqs_url)
                )
            )
        }
    }
}