package com.salamtak.app.ui.component.profile

import android.os.Bundle
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.password.ChangePasswordActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import org.jetbrains.anko.startActivity


class AccountSettingsFragment : BaseFragment() {

    override val layoutId: Int
        get() = R.layout.fragment_account_settings

    override fun observeViewModel() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }

    private fun initViews() {
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.account_setting)
        v_change_pass.setOnClickListener {
            activity?.startActivity<ChangePasswordActivity>()
        }

        v_edit_profile.setOnClickListener {
            LogUtil.LogFirebaseEvent("btn_change_profile", this::class.java.simpleName)
            navigateToEditProfile()
        }


    }

}