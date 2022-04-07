package com.salamtak.app.ui.component.education.custom

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_education.btn_submit
import kotlinx.android.synthetic.main.activity_education.et_monthly_installment
import kotlinx.android.synthetic.main.activity_education.et_name
import kotlinx.android.synthetic.main.activity_education.et_phone
import kotlinx.android.synthetic.main.activity_education.et_price
import kotlinx.android.synthetic.main.activity_education.progress_bar
import kotlinx.android.synthetic.main.activity_education.spinner_installment_plan
import kotlinx.android.synthetic.main.activity_education.tv_down_payment
import kotlinx.android.synthetic.main.activity_education.tv_lbl_installment
import kotlinx.android.synthetic.main.activity_education.tv_lbl_insurance_company
import kotlinx.android.synthetic.main.activity_education.tv_lbl_insurance_type
import kotlinx.android.synthetic.main.activity_education.tv_lbl_monthly
import kotlinx.android.synthetic.main.activity_education.tv_lbl_name
import kotlinx.android.synthetic.main.activity_education.tv_lbl_price
import kotlinx.android.synthetic.main.activity_wedding_request.*
import kotlinx.android.synthetic.main.fragment_custom_car_step2.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@AndroidEntryPoint
class EducationActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    override val layoutId: Int
        get() = R.layout.activity_education

    var showError = false

    val viewmodel: EducationViewModel  by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tv_lbl_name.text = tv_lbl_name.text.toString() + "*"
        tv_lbl_price.text = tv_lbl_price.text.toString() + "*"
        tv_lbl_installment.text = tv_lbl_installment.text.toString() + "*"
        tv_lbl_monthly.text = tv_lbl_monthly.text.toString() + "*"

        tv_lbl_education_fees.text = tv_lbl_education_fees.text.toString() + "*"
        tv_lbl_insurance_company.text = tv_lbl_insurance_company.text.toString() + "*"
        tv_lbl_grade.text = tv_lbl_grade.text.toString() + "*"
        tv_lbl_student_name.text = tv_lbl_student_name.text.toString() + "*"
        tv_lbl_insurance_type.text = tv_lbl_insurance_type.text.toString() + "*"
        tv_lbl_branch.text = tv_lbl_branch.text.toString() + "*"

        viewmodel.getInstallmentsLookup()
        et_phone.filters = (Constants.DisableDecimalWithMaxLength(11))
        et_phone.setText(viewmodel.getUser()!!.phone)
        et_name.setText(viewmodel.getUserName())

        bindTypesData()
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
            viewmodel.CreateCustomEducationBooking(
                et_name.text.toString(),
                et_phone.text.toString(),
                et_company_name.text.toString(),
                et_monthly_installment.text.toString(),
                et_price.text.toString(),
                tv_down_payment.text.toString(),
                et_branch.text.toString(),
                et_student_id.text.toString(),
                et_student_name.text.toString(),
                et_bus_fees.text.toString(),
                et_grade.text.toString(),
                et_education_fees.text.toString()
            )
//                et_name.text.toString(),
//                et_phone.text.toString(),
//                et_company_name.text.toString(),
//                1,
//                et_monthly_installment.text.toString(),
//                et_price.text.toString(), et_down_payment.text.toString()
//            )
        }


        et_bus_fees.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    et_price.setText(
                        (Integer.parseInt(et_bus_fees.text.toString()) + Integer.parseInt(
                            et_education_fees.text.toString()
                        )).toString()
                    )
                } catch (e: Exception) {
                    try {
                        if (et_education_fees.text.toString()
                                .isNotEmpty() && et_education_fees.text.toString()
                                .toIntOrNull() != null
                        )
                            et_price.setText(
                                Integer.parseInt(
                                    et_education_fees.text.toString()
                                ).toString()
                            )
                    } catch (e: Exception) {
                    }
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

        et_education_fees.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    et_price.setText(
                        (Integer.parseInt(et_bus_fees.text.toString()) + Integer.parseInt(
                            et_education_fees.text.toString()
                        )).toString()
                    )
                } catch (e: Exception) {
                    if (et_education_fees.text.toString().isNotEmpty()) {
                        et_price.setText(
                            Integer.parseInt(
                                et_education_fees.text.toString()
                            ).toString()
                        )
                    } else
                        et_price.setText("0")
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



        et_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                try {
                    if (et_price.text.toString().isNotEmpty()) {
                        tv_down_payment.setText(
                            (et_price.text.toString().toDouble() * 10 / 100).toInt().toString()
                        )
                        tv_down_payment.filters =
                            arrayOf(InputFilterMinMax(0, et_price.text.toString().toInt()))
                    }
                } catch (e: Exception) {

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
        var installment = viewmodel.getInstallmentPerMonth(
            et_price.text.toString().toDouble(),
            tv_down_payment.text.toString().toDouble()
        )

        et_monthly_installment.setText(installment.toString())
    }


    private fun bindTypesData() {

        var list = resources.getStringArray(R.array.education_types).toMutableList()

        list.add(0, getString(R.string.choose))

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, list
        )

        spinner_type.adapter = adapter
        spinner_type.onItemSelectedListener = this
    }

//    lateinit var installmentTypesAdapter: ArrayAdapter<String>

    private fun bindInstallmentTypes(data: List<InstallmentType>) {
//        spinner_installment_plan.adapter = null

        var installmentTypesAdapter = ArrayAdapter(
            this,
            R.layout.item_spinner, data.map { it.name }
        )

        spinner_installment_plan.adapter = installmentTypesAdapter
        spinner_installment_plan.onItemSelectedListener = this

        viewmodel.selectInstallmentPlan(0)
    }

    override fun initializeViewModel() {
//        viewmodel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(viewmodel::class.java)
    }

    override fun observeViewModel() {
        with(viewmodel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(viewmodel.customOperationFromState, ::handleFormState)
            observe(viewmodel.customOperationResponse, ::handleBookingResponse)
            observe(viewmodel.installmentTypes, ::bindInstallmentTypes)
        }
    }


    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_submit.isEnabled = false
        } else {
            progress_bar.toGone()
            btn_submit.isEnabled = true
        }
    }

    private fun handleFormState(customOperationFormState: EducationFormState) {
        btn_submit.isEnabled = true
        if (customOperationFormState?.nameError != null) {
            et_name.error = getString(customOperationFormState.nameError)
            et_name.shakeView()
            main_layout.scrollTo(et_name.x.toInt(), et_name.y.toInt())
        } else
            et_name.error = null

        if (customOperationFormState?.phoneError != null) {
            et_phone.error = getString(customOperationFormState.phoneError)
            et_phone.shakeView()
            main_layout.scrollTo(et_phone.x.toInt(), et_phone.y.toInt())
        } else
            et_phone.error = null


        if (customOperationFormState?.typeError != null) {
            if (showError) {
                et_speciality.error = getString(customOperationFormState.typeError)
            } else {
                showError = true
            }
//            et_speciality.shakeView()
        } else
            et_speciality.error = null

        if (customOperationFormState?.companyNameError != null) {
            et_company_name.error = getString(customOperationFormState.companyNameError)
            et_company_name.shakeView()
        } else
            et_company_name.error = null

        if (customOperationFormState?.branchNameError != null) {
            et_branch.error = getString(customOperationFormState.branchNameError)
            et_branch.shakeView()
            main_layout.scrollTo(et_branch.x.toInt(), et_branch.y.toInt())
        } else
            et_branch.error = null

        if (customOperationFormState.studentNameError != null) {
            et_student_name.error =
                getString(customOperationFormState.studentNameError)
            et_student_name.shakeView()
        } else
            et_student_name.error = null

        if (customOperationFormState.gradeError != null) {
            et_grade.error =
                getString(customOperationFormState.gradeError)
            et_grade.shakeView()
        } else
            et_grade.error = null

        if (customOperationFormState.educationFeesError != null) {
            et_education_fees.error =
                getString(customOperationFormState.educationFeesError)
            et_education_fees.shakeView()
        } else
            et_education_fees.error = null

//        if (customOperationFormState.busFeesError != null) {
//            et_bus_fees.error =
//                getString(customOperationFormState.busFeesError)
//            et_bus_fees.shakeView()
//        } else
//            et_bus_fees.error = null

        if (customOperationFormState?.totalCost != null) {
            et_price.error = getString(customOperationFormState.totalCost)
            et_price.shakeView()
            main_layout.scrollTo(et_price.x.toInt(), et_price.y.toInt())
        } else
            et_price.error = null

        if (customOperationFormState?.downPayment != null) {
            tv_down_payment.error = getString(customOperationFormState.downPayment)
            tv_down_payment.shakeView()
        } else
            tv_down_payment.error = null

        if (customOperationFormState?.monthlyInstallmentError != null) {
            et_monthly_installment.error =
                getString(customOperationFormState.monthlyInstallmentError)
            et_monthly_installment.shakeView()
        } else
            et_monthly_installment.error = null

    }

    private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {
            LogAdjustEvent("2pnh1n")
            response.let {
//                        showMessage(response.data.data, ::yesClicked)
                startActivity(intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewmodel.needFinancialInfo()))
            }
        } else {
            showMessage(response.message!!)
            //viewModelBooking.checkNavigation()oncli
        }
    }

    fun yesClicked() {
        navigateToMainScreen()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_type -> {
                if (position == 0) {
                    if (showError)
                        et_speciality.error = getString(R.string.require_field)
                    else
                        showError = true
                } else {
                    et_speciality.error = null
                    viewmodel.selectType(position)
                }
            }

            R.id.spinner_installment_plan -> {
                viewmodel.selectInstallmentPlan(position)

                if (!tv_down_payment.text.isNullOrEmpty() && !et_price.text.isNullOrEmpty()) {
                    // if university && plan 12 month, show 0 downpayment & disable feild & reenable if not

                    var installment = viewmodel.getInstallmentPerMonth(
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