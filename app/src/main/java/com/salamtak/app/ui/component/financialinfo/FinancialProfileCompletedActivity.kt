package com.salamtak.app.ui.component.financialinfo

import android.os.Bundle
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_financial_profile_completed.*
@AndroidEntryPoint
class FinancialProfileCompletedActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_financial_profile_completed

    override fun initializeViewModel() {

    }

    override fun observeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LogAdjustEvent("bo44ko")
        btn_home.setOnClickListener { navigateToMainScreen() }
//        iv_back.setOnClickListener { onBackPressed() }
//        LogAdjustEvent("p12gd5")
    }
}
