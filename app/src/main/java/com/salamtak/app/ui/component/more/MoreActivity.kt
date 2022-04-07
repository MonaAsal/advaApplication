package com.salamtak.app.ui.component.more

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import com.salamtak.app.R

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.aboutus.AboutUsActivity
import com.salamtak.app.ui.component.home.HomeViewModel
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
@AndroidEntryPoint
class MoreActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.fragment_more

    val homeViewModel: HomeViewModel  by viewModels()

    override fun initializeViewModel() {
//        homeViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(HomeViewModel::class.java)
    }

    override fun observeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.help)
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
//            var version = pInfo.versionName
            tv_version.text = String.format(getString(R.string.version_number), pInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }

        v_terms.setOnClickListener {

            startActivity(
                intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.terms_and_conditions),
                    Constants.KEY_URL to getString(R.string.terms_url)
                )
            )
        }

        v_change_pass.setOnClickListener {
            startActivity<ContactUsActivity>()
        }

        v_info.setOnClickListener {
//            startActivity(intentFor<TermsAndConditionsActivity>(Constants.KEY_TITLE to Constants.WHO_WE_ARE))
            startActivity<AboutUsActivity>()
        }

        v_edit_profile.setOnClickListener {
            startActivity(
                intentFor<WebViewActivity>(
                    Constants.KEY_TITLE to getString(R.string.faq_s),
                    Constants.KEY_URL to getString(R.string.faqs_url)
                )
            )
        }
    }
}
