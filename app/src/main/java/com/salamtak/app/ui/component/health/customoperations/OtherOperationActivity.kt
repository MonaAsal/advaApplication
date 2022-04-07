package com.salamtak.app.ui.component.health.customoperations

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.CustomOperationLookupsResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_other_operation.*
import kotlinx.android.synthetic.main.activity_other_operation.btn_submit
import kotlinx.android.synthetic.main.activity_other_operation.tv_down_payment
import kotlinx.android.synthetic.main.activity_other_operation.et_monthly_installment
import kotlinx.android.synthetic.main.activity_other_operation.et_price
import kotlinx.android.synthetic.main.activity_other_operation.et_speciality
import kotlinx.android.synthetic.main.activity_other_operation.progress_bar
import kotlinx.android.synthetic.main.activity_other_operation.spinner_installment_plan
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@AndroidEntryPoint
class OtherOperationActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    override val layoutId: Int
        get() = R.layout.activity_other_operation

    var showError = false

    val customOperationViewModel: CustomOperationViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv_toolbar_title.text=getString(R.string.operation_request)
        customOperationViewModel.getCustomOperationLookups()
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
            customOperationViewModel.bookCustomOperation(
                spinner_speciality.selectedItemPosition!!,
                et_doctor.text.toString(),
                et_operation.text.toString(),
                et_monthly_installment.text.toString(),
                tv_down_payment.text.toString(),
                et_price.text.toString()
            )
        }

        et_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (et_price.text.toString().isNotEmpty()) {
                    tv_down_payment.setText(
                        (et_price.text.toString().toDouble() * 10 / 100).toInt().toString()
                    )
                    tv_down_payment.filters =
                        arrayOf(InputFilterMinMax(0, et_price.text.toString().toInt()))
//                    et_down_payment.setFilters(
//                        arrayOf(
//                            InputFilterMinMax(minValue, maxValue),
//                            LengthFilter(String.valueOf(maxValue).length)
//                        )
//                    )

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                if (et_down_payment.text.toString().toDouble() > et_price.text.toString()
//                        .toDouble()
//                ) {
//                    showMessage("value cannot be more than the operation price")
//                }

            }
        })

        tv_down_payment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    if (et_price.text.toString().toInt() >= 100) {
                        if ((tv_down_payment.text.toString()
                                .toInt() < et_price.text.toString()
                                .toInt() / 10)
                        ) {
                            tv_down_payment.error = getString(R.string._10_min)
                        } else {
                            calculateInstallment()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
    }

    private fun calculateInstallment() {
        var installment = customOperationViewModel.getInstallmentPerMonth(
            et_price.text.toString().toDouble(),
            tv_down_payment.text.toString().toDouble()
        )

        et_monthly_installment.setText(installment.toString())
    }


    private fun handleCategoriesResponse(resource: Resource<SalamtakListResponse<Category>>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> {
                progress_bar.toGone()
                resource.data?.let { bindCategoriesData(it.data!!) }
            }
        }
    }

    private fun bindCategoriesData(data: List<Category>) {

        var list = data.map { it.name }.toMutableList()
        list.add(0, getString(R.string.choose))
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, list
        )

        spinner_speciality.adapter = adapter
        spinner_speciality.onItemSelectedListener = this

    }

    private fun bindInstallmentTypes(data: List<InstallmentType>) {
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, data.map { it.name }
        )

        spinner_installment_plan.adapter = adapter
        spinner_installment_plan.onItemSelectedListener = this
    }

    override fun initializeViewModel() {
//        customOperationViewModel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(CustomOperationViewModel::class.java)
    }

    override fun observeViewModel() {
        with(customOperationViewModel)
        {
            observe(customOperationViewModel.customOperationFromState, ::handleFormState)
            observe(customOperationViewModel.customOperationResponse, ::handleBookingResponse)
            observe(customOperationViewModel.lookupsResponse, ::handleLookupResponse)
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
        }
    }

    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_submit.isEnabled = false
        }
        else {
            progress_bar.toGone()
            btn_submit.isEnabled = true
        }
    }

    private fun handleFormState(customOperationFormState: CustomOperationFormState) {
        btn_submit.isEnabled = true
        if (customOperationFormState?.doctorNameError != null) {
            et_doctor.error = getString(customOperationFormState.doctorNameError)
            et_doctor.shakeView()
        }

        if (customOperationFormState?.specialityError != null) {
            if (showError) {
                et_speciality.error = getString(customOperationFormState.specialityError)
            } else {
                showError = true
            }
//            et_speciality.shakeView()
        }

        if (customOperationFormState?.operationName != null) {
            et_operation.error = getString(customOperationFormState.operationName)
            et_operation.shakeView()
        }

        if (customOperationFormState?.totalCost != null) {
            et_price.error = getString(customOperationFormState.totalCost)
            et_price.shakeView()
        }

        if (customOperationFormState?.downPayment != null) {
            tv_down_payment.error = getString(customOperationFormState.downPayment)
            tv_down_payment.shakeView()
        }

        if (customOperationFormState?.monthlyInstallment != null) {
            et_monthly_installment.error = getString(customOperationFormState.monthlyInstallment)
            et_monthly_installment.shakeView()
        }

    }

    private fun handleBookingResponse(response: BaseResponse) {
//        when (response) {
//            is Resource.Loading -> {
//                btn_submit.isEnabled = false
//                showLoadingView(progress_bar)
//            }
//            is Resource.Success -> {
//                progress_bar.toGone()
        btn_submit.isEnabled = true
        if (response.status!!) {
            LogAdjustEvent("3xutas")
            response.let {
//                        showMessage(response.data.data, ::yesClicked)
                startActivity(intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to customOperationViewModel.needFinancialInfo()))
            }
        } else {
            showMessage(response.message!!)
            //viewModelBooking.checkNavigation()oncli
        }
//            }
//            is Resource.NetworkError -> {
//                btn_submit.isEnabled = true
//                progress_bar.toGone()
//                response.errorCode?.let {
//                    var error = customOperationViewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                btn_submit.isEnabled = true
//                progress_bar.toGone()
//                response.errorResponse?.let { showServerErrorMessage(it) }
//            }
//        }
    }

    fun yesClicked() {
        navigateToMainScreen()
    }

    private fun handleLookupResponse(response: CustomOperationLookupsResponse) {
        bindCategoriesData(response.data.categories)
        bindInstallmentTypes(response.data.installmentTypes)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_speciality -> {
                if (position == 0) {
                    if (showError)
                        et_speciality.error = getString(R.string.require_field)
                    else
                        showError = true
                } else {
                    et_speciality.error = null
                    customOperationViewModel.selectCategory(position)
                }
            }

            R.id.spinner_installment_plan -> {
                customOperationViewModel.selectInstallmentPlan(position)
                if (!tv_down_payment.text.isNullOrEmpty() && !et_price.text.isNullOrEmpty()) {
                    var installment = customOperationViewModel.getInstallmentPerMonth(
                        et_price.text.toString().toDouble(),
                        tv_down_payment.text.toString().toDouble()
                    )

                    //et_monthly_installment.setText(toDecimalNumberFormat(installment.toDouble()))
                    et_monthly_installment.setText(installment.toString())
                }
            }
        }

    }

    class InputFilterMinMax(private val min: Int, private val max: Int) : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.toString() + source.toString()).toInt()
                if (isInRange(min, max, input))
                    return null

            } catch (nfe: NumberFormatException) {
                nfe.printStackTrace()
            }
            return ""
        }

        private fun isInRange(min: Int, max: Int, input: Int): Boolean {
            return if (max > min) input in min..max else input in max..min
        }
    }

}
