package com.salamtak.app.ui.component.premiumcard

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.entities.responses.PremiumData
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.health.categories.PremiumViewModel
import com.salamtak.app.utils.Constants
import kotlinx.android.synthetic.main.activity_premium.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

import org.jetbrains.anko.intentFor
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_premium.progress_bar
import kotlinx.android.synthetic.main.toolbar.*
@AndroidEntryPoint
class PremiumActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_premium

//    @Inject
//    lateinit
    val viewmodel: PremiumViewModel  by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory


    override fun initializeViewModel() {
//        viewmodel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(PremiumViewModel::class.java)

    }

    override fun observeViewModel() {
        with(viewmodel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(gotpResponse, ::handleResponse)
        }
    }

    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_ok.isEnabled = false
        } else {
            progress_bar.toGone()
            btn_ok.isEnabled = true
        }
    }

    fun handleResponse(response: PremiumData) {
        when (response.premiumStatus) {
            "1" -> {
                startActivity(
                    intentFor<VerifyPremiumCardActivity>(
                        Constants.KEY_ID to viewmodel.booking!!.id,
                        Constants.KEY_CARD_NUM to viewmodel.cardNumber,
                        Constants.KEY_EXPIRY to viewmodel.expiryDate,
                        Constants.KEY_REFERENCE to response.referenceNumber
                    )
                )
            }
            "2", "3" -> {
                showMessage(getString(R.string.premium_invalid_date))
            }
            "4" -> {
                showMessage(getString(R.string.premium_card_expired))
            }
            "5" -> {
                showMessage(getString(R.string.premium_no_mobile_number))
            }
            else -> {
                showMessage(getString(R.string.premium_error))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tv_toolbar_title.text = getString(R.string.premium_card)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        viewmodel.booking =
            intent!!.getParcelableExtra<BookedOperation>(Constants.KEY_OPERATION_ITEM)!!

        viewmodel.amount = viewmodel.booking!!.amount
        tv_amount.text = viewmodel.amount

        btn_ok.setOnClickListener {
            if (credit_card_number.text.toString().isNullOrEmpty().not()) {
                Log.e(
                    "num",
                    credit_card_number.number + " lenght:" + credit_card_number.number.length
                )
                if (credit_card_number.number.length == 16) {
                    if (credit_card_date.date.toString().isNullOrEmpty().not()) {
                        if (validate(credit_card_date.date.toString())) {
                            if (validExpiry(credit_card_date.date.toString())) {
                                Log.e("date", credit_card_date.date.toString().removeRange(2, 3))
                                viewmodel.GOTP(
                                    credit_card_number.number,
                                    credit_card_date.date.toString().removeRange(2, 3),
                                    viewmodel.booking!!.id
                                )
                            } else
                                credit_card_date.error =
                                    getString(R.string.invalid_date2)//date_expired)
//                        Log.e("invalid", credit_card_date.date.toString())
                        } else
                            credit_card_date.error = getString(R.string.invalid_date2)
                    } else
                        credit_card_date.error = getString(R.string.require_field)
                } else
                    credit_card_number.error = getString(R.string.invalid_card_number)
            } else
                credit_card_number.error = getString(R.string.require_field)
        }
    }


    fun validate(text: String): Boolean {
//        val input = "11/12" // for example
        try {
            val simpleDateFormat = SimpleDateFormat("MM/yy")
//            simpleDateFormat.setLenient(false)
            val date: Date = simpleDateFormat.parse(text)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun validExpiry(text: String): Boolean {
//        val input = "11/12" // for example
        try {
            val simpleDateFormat = SimpleDateFormat("MM/yy")
            simpleDateFormat.setLenient(false)
            val expiry: Date = simpleDateFormat.parse(text)
            val valid: Boolean = expiry.after(Date())
            return valid
        } catch (e: Exception) {
            return false
        }
    }
}