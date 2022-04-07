package com.salamtak.app.ui.component.premiumcard

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.TransactionDetails
import com.salamtak.app.data.entities.responses.SalamtakResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.health.categories.PremiumViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_premium_status.*
import javax.inject.Inject
@AndroidEntryPoint
class PremiumCardStatusActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_premium_status

     val premiumViewModel: PremiumViewModel  by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun initializeViewModel() {
//        premiumViewModel = viewModelFactory.create(PremiumViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(premiumViewModel.gtrdResponse, ::handleStatus)
        observe(premiumViewModel.showLoading, ::showLoadingView)
        observe(premiumViewModel.showServerError, ::showServerErrorMessage)
    }

    private fun handleStatus(salamtakResponse: SalamtakResponse<TransactionDetails>?) {
        var date = convertDateFormat(salamtakResponse!!.data?.transactionDate.toString(), "yyyyMMddhhmmss", "MMM dd,yyyy, hh:mm") //"yyyy/MM/dd" "20210930005738"
        Log.e("date", date)
        //var date = salamtakResponse!!.data?.transactionDate.toString()
        tv_status.text =
            String.format(
                getString(R.string.premium_status_text),
                salamtakResponse!!.data?.amount.toString(),
                salamtakResponse!!.data?.status.toString(),
                date
            )
        //"Amount: ${salamtakResponse!!.data?.amount}\nStatus: ${salamtakResponse!!.data?.status}\nDate: ${salamtakResponse!!.data?.transactionDate}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        premiumViewModel.bookingId = intent?.getStringExtra(Constants.KEY_ID)
        premiumViewModel.referenceNumber = intent?.getStringExtra(Constants.KEY_REFERENCE)

        premiumViewModel.GTRD(premiumViewModel.bookingId!!)
        btn_go_home.setOnClickListener { navigateToMainScreen() }
        iv_close.setOnClickListener { navigateToMainScreen() }

    }

    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
        } else {
            progress_bar.toGone()
        }
    }


}