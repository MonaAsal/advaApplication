//package com.salamtak.app.ui.component.weddings
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.InputFilter
//import android.text.Spanned
//import android.text.TextWatcher
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import androidx.activity.viewModels
//import androidx.lifecycle.ViewModelProviders
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.InstallmentType
//import com.salamtak.app.data.entities.responses.BaseResponse
//
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
//import com.salamtak.app.utils.*
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_wedding_request.*
//import kotlinx.android.synthetic.main.toolbar.*
//import org.jetbrains.anko.intentFor
//import javax.inject.Inject
//@AndroidEntryPoint
//class WeddingRequestActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
//
//    override val layoutId: Int
//        get() = R.layout.activity_wedding_request
//
//    var showError = false
//
////    @Inject
////    lateinit
//    val viewmodel: WeddingsViewModel by viewModels()
////
////    @Inject
////    lateinit var viewModelFactory: ViewModelFactory
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        tv_lbl_name.text = tv_lbl_name.text.toString() + "*"
//
//        tv_lbl_price.text = tv_lbl_price.text.toString() + "*"
//        tv_lbl_installment.text = tv_lbl_installment.text.toString() + "*"
//        tv_lbl_monthly.text = tv_lbl_monthly.text.toString() + "*"
//        tv_lbl_insurance_type.text = tv_lbl_insurance_type.text.toString() + "*"
//        tv_lbl_insurance_company.text = tv_lbl_insurance_company.text.toString() + "*"
//
//
//        et_phone.setText(viewmodel.getUser()!!.phone)
//        et_name.setText(viewmodel.getUserName())
//        viewmodel.getInstallmentsLookup()
////        bindTypesData()
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        btn_submit.setOnClickListener {
//            viewmodel.CreateCustomWeddingBooking(
//                et_name.text.toString(),
//                et_phone.text.toString(),
//                et_venue.text.toString(),
//                et_invitees_number.text.toString(),
//                et_monthly_installment.text.toString(),
//                et_price.text.toString(),
//                tv_down_payment.text.toString()
//            )
//        }
//
//        et_price.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                if (et_price.text.toString().isNotEmpty()) {
//                    tv_down_payment.setText((et_price.text.toString().toDouble() * 10 / 100).toInt().toString())
//                    tv_down_payment.filters =
//                        arrayOf(InputFilterMinMax(0, et_price.text.toString().toInt()))
////                    et_down_payment.setFilters(
////                        arrayOf(
////                            InputFilterMinMax(minValue, maxValue),
////                            LengthFilter(String.valueOf(maxValue).length)
////                        )
////                    )
//
//                }
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
////                if (et_down_payment.text.toString().toDouble() > et_price.text.toString()
////                        .toDouble()
////                ) {
////                    showMessage("value cannot be more than the operation price")
////                }
//
//            }
//        })
//
//        tv_down_payment.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                try {
//                    if (et_price.text.toString().toInt() >= 100) {
//                        if ((tv_down_payment.text.toString()
//                                .toInt() < et_price.text.toString()
//                                .toInt() / 10)
//                        ) {
//                            tv_down_payment.error = getString(R.string._10_min)
//                        } else {
//                            calculateInstallment()
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//
//            }
//        })
//    }
//
//
//
//    private fun calculateInstallment() {
//        var installment = viewmodel.getInstallmentPerMonth(
//            et_price.text.toString().toDouble(),
//            tv_down_payment.text.toString().toDouble()
//        )
//
//        et_monthly_installment.setText(installment.toString())
//    }
//
//
////
////    private fun bindTypesData() {
////
////        var list = resources.getStringArray(R.array.insurance_types).toMutableList()
////
////        list.add(0, getString(R.string.choose))
////
////        val adapter = ArrayAdapter(
////            this,
////            R.layout.item_spinner, list
////        )
////
////        spinner_type.adapter = adapter
////        spinner_type.onItemSelectedListener = this
////
////    }
//
//    private fun bindInstallmentTypes(data: List<InstallmentType>) {
//        btn_submit.isEnabled = true
//        val adapter = ArrayAdapter(
//            this,
//            R.layout.item_spinner, data.map { it.name }
//        )
//
//        spinner_installment_plan.adapter = adapter
//        spinner_installment_plan.onItemSelectedListener = this
//
//    }
//
//    override fun initializeViewModel() {
////        viewmodel =
////            ViewModelProviders.of(this, viewModelFactory)
////                .get(viewmodel::class.java)
//    }
//
//    override fun observeViewModel() {
//        with(viewmodel)
//        {
//            observe(showLoading, ::showLoadingView)
//            observe(showServerError, ::showServerErrorMessage)
//            observe(viewmodel.customOperationFromState, ::handleFormState)
//            observe(viewmodel.customOperationResponse, ::handleBookingResponse)
//            observe(viewmodel.installmentTypes, ::bindInstallmentTypes)
//        }
//    }
//
//    fun showLoadingView(show: Boolean) {
//        if (show) {
//            progress_bar.toVisible()
//            btn_submit.isEnabled = false
//        }
//        else {
//            progress_bar.toGone()
//            btn_submit.isEnabled = true
//        }
//    }
//
//    private fun handleFormState(customOperationFormState: WeddingFormState) {
//        btn_submit.isEnabled = true
//        if (customOperationFormState?.nameError != null) {
//            et_name.error = getString(customOperationFormState.nameError)
//            et_name.shakeView()
//        }
//        if (customOperationFormState?.phoneError != null) {
//            et_phone.error = getString(customOperationFormState.phoneError)
//            et_phone.shakeView()
//        }
//        if (customOperationFormState?.companyNameError != null) {
//            et_venue.error = getString(customOperationFormState.companyNameError)
//            et_venue.shakeView()
//        }
//        if(customOperationFormState?.inviteesError != null)
//        {
//            et_invitees_number.error = getString(customOperationFormState.inviteesError)
//            et_invitees_number.shakeView()
//        }
//        if (customOperationFormState?.totalCost != null ) {
//            et_price.error = getString(customOperationFormState.totalCost)
//            et_price.shakeView()
//        }
//
//        if (customOperationFormState?.downPayment != null) {
//            tv_down_payment.error = getString(customOperationFormState.downPayment)
//            tv_down_payment.shakeView()
//        }
//
//
//        if (customOperationFormState?.monthlyInstallmentError != null) {
//            et_monthly_installment.error =
//                getString(customOperationFormState.monthlyInstallmentError)
//            et_monthly_installment.shakeView()
//        }
//
//
//    }
//
//    private fun handleBookingResponse(response: BaseResponse) {
//        btn_submit.isEnabled = true
//        if (response.status!!) {
//            LogAdjustEvent("l2kjzs")
//            response.let {
//                startActivity(intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewmodel.needFinancialInfo()))
//            }
//        } else {
//            showMessage(response.message!!)
//        }
//    }
//
//    fun yesClicked() {
//        navigateToMainScreen()
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//    }
//
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        when (parent?.id) {
////            R.id.spinner_type -> {
////                if (position == 0) {
////                    if (showError)
////                        et_speciality.error = getString(R.string.require_field)
////                    else
////                        showError = true
////                } else {
////                    et_speciality.error = null
////                    viewmodel.selectType(position)
////                }
////            }
//
//            R.id.spinner_installment_plan -> {
//                viewmodel.selectInstallmentPlan(position)
//                if (!tv_down_payment.text.isNullOrEmpty() && !et_price.text.isNullOrEmpty()) {
//                    var installment = viewmodel.getInstallmentPerMonth(
//                        et_price.text.toString().toDouble(),
//                        tv_down_payment.text.toString().toDouble()
//                    )
//
//                    //et_monthly_installment.setText(toDecimalNumberFormat(installment.toDouble()))
//                    et_monthly_installment.setText(installment.toString())
//                }
//            }
//        }
//
//    }
//
//    class InputFilterMinMax(private val min: Int, private val max: Int) : InputFilter {
//        override fun filter(
//            source: CharSequence,
//            start: Int,
//            end: Int,
//            dest: Spanned,
//            dstart: Int,
//            dend: Int
//        ): CharSequence? {
//            try {
//                val input = (dest.toString() + source.toString()).toInt()
//                if (isInRange(min, max, input))
//                    return null
//
//            } catch (nfe: NumberFormatException) {
//                nfe.printStackTrace()
//            }
//            return ""
//        }
//
//        private fun isInRange(min: Int, max: Int, input: Int): Boolean {
//            return if (max > min) input in min..max else input in max..min
//        }
//    }
//
//}