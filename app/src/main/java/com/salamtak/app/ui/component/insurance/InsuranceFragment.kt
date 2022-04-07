package com.salamtak.app.ui.component.insurance

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.activity_insurance.btn_submit
import kotlinx.android.synthetic.main.activity_insurance.et_company_name
import kotlinx.android.synthetic.main.activity_insurance.et_monthly_installment
import kotlinx.android.synthetic.main.activity_insurance.et_name
import kotlinx.android.synthetic.main.activity_insurance.et_phone
import kotlinx.android.synthetic.main.activity_insurance.et_price
import kotlinx.android.synthetic.main.activity_insurance.et_speciality
import kotlinx.android.synthetic.main.activity_insurance.progress_bar
import kotlinx.android.synthetic.main.activity_insurance.spinner_installment_plan
import kotlinx.android.synthetic.main.activity_insurance.spinner_type
import kotlinx.android.synthetic.main.activity_insurance.tv_down_payment
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_installment
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_insurance_company
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_insurance_type
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_monthly
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_name
import kotlinx.android.synthetic.main.activity_insurance.tv_lbl_price
import kotlinx.android.synthetic.main.activity_wedding_request.*
import kotlinx.android.synthetic.main.dialog_service_added.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor


class InsuranceFragment : BaseFragment() , AdapterView.OnItemSelectedListener{
    override val layoutId: Int
        get() = R.layout.fragment_insurance

    var showError = false

    val viewmodel: InsuranceViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()

        tv_lbl_name.text = tv_lbl_name.text.toString() + "*"
        tv_toolbar_title.text=getString(R.string.insurance_request)
        tv_lbl_price.text = tv_lbl_price.text.toString() + "*"
        tv_lbl_installment.text = tv_lbl_installment.text.toString() + "*"
        tv_lbl_monthly.text = tv_lbl_monthly.text.toString() + "*"
        tv_lbl_insurance_type.text = tv_lbl_insurance_type.text.toString() + "*"
        tv_lbl_insurance_company.text = tv_lbl_insurance_company.text.toString() + "*"
        et_price.filters = (Constants.DisableDecimalWithMaxLength(6))
        et_phone.setText(viewmodel.getUser()!!.phone)
        et_phone.filters = (Constants.DisableDecimalWithMaxLength(11))
        et_name.setText(viewmodel.getUserName())
        viewmodel.getInstallmentsLookup()
        bindTypesData()
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        addInsuranceToCart()
      /*  btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
            viewmodel.CreateCustomInsuranceBooking(
                et_name.text.toString(),
                et_phone.text.toString(),
                et_company_name.text.toString(),
                1,
                et_monthly_installment.text.toString(),
                et_price.text.toString(), tv_down_payment.text.toString()
            )
        }*/

        workWithPrice()


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

    private fun workWithPrice() {
        et_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                var price = et_price.text.toString()

                if (!price.isNullOrEmpty()){
                    if(!price.contains(",") && !price.contains("..")){
                        var priceDouble = price.toDouble()
                        var priceInt = price.toDouble().toInt()
                        var downPayment =((priceDouble * 10 / 100).toInt()).toString()
                        tv_down_payment.setText(downPayment)
                        tv_down_payment.filters = arrayOf(
                            InsuranceActivity.InputFilterMinMax(
                                0,
                                priceInt
                            )
                        )
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
//                if (et_down_payment.text.toString().toDouble() > et_price.text.toString()
//                        .toDouble()
//                ) {
//                    showMessage("value cannot be more than the operation price")
//                }

            }
        })

    }


    private fun calculateInstallment() {
        val installment = viewmodel.getInstallmentPerMonth(
            et_price.text.toString().toDouble(),
            tv_down_payment.text.toString().toDouble()
        )

        et_monthly_installment.setText(installment.toString())
    }



    private fun bindTypesData() {

        val list = resources.getStringArray(R.array.insurance_types).toMutableList()

        list.add(0, getString(R.string.choose))

        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.item_spinner, list
            )
        }

        spinner_type.adapter = adapter
        spinner_type.onItemSelectedListener = this

    }

    private fun bindInstallmentTypes(data: List<InstallmentType>) {
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.item_spinner, data.map { it.name }
            )
        }

        spinner_installment_plan.adapter = adapter
        spinner_installment_plan.onItemSelectedListener = this

    }

    override fun observeViewModel() {
        with(viewmodel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(viewmodel.customOperationFromState, ::handleFormState)
            //observe(viewmodel.customOperationResponse, ::handleBookingResponse)
            observe(viewmodel.customInsuranceResponse, ::handleCartBookingResponse)
            observe(viewmodel.installmentTypes, ::bindInstallmentTypes)
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

    private fun handleFormState(customOperationFormState: InsuranceFormState) {
        btn_submit.isEnabled = true
        if (customOperationFormState?.nameError != null) {
            et_name.error = getString(customOperationFormState.nameError)
            et_name.shakeView()
        }
        if (customOperationFormState?.phoneError != null) {
            et_phone.error = getString(customOperationFormState.phoneError)
            et_phone.shakeView()
        }
        if (customOperationFormState?.companyNameError != null) {
            et_company_name.error = getString(customOperationFormState.companyNameError)
            et_company_name.shakeView()
        }

        if (customOperationFormState?.typeError != null) {
            if (showError) {
                et_speciality.error = getString(customOperationFormState.typeError)
            } else {
                showError = true
            }
//            et_speciality.shakeView()
        }

        if (customOperationFormState?.totalCost != null) {
            et_price.error = getString(customOperationFormState.totalCost)
            et_price.shakeView()
        }

        if (customOperationFormState?.downPayment != null) {
            tv_down_payment.error = getString(customOperationFormState.downPayment)
            tv_down_payment.shakeView()
        }

        if (customOperationFormState?.monthlyInstallmentError != null) {
            et_monthly_installment.error =
                getString(customOperationFormState.monthlyInstallmentError)
            et_monthly_installment.shakeView()
        }

    }

 /*   private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {
            LogAdjustEvent("3ub4wn")
            response.let {
//                        showMessage(response.data.data, ::yesClicked)
                startActivity(activity?.intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewmodel.needFinancialInfo()))
            }
        } else {
            showMessage(response.message!!)
            //viewModelBooking.checkNavigation()oncli
        }
    }*/

    fun yesClicked() {
        navigateToHome()
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
    private fun addInsuranceToCart() {
        btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
            viewmodel.addCustomInsuranceBookingToCart(
                et_name.text.toString(),
                et_phone.text.toString(),
                et_company_name.text.toString(),
                viewmodel.selectedType,
                et_monthly_installment.text.toString(),
                et_price.text.toString(), tv_down_payment.text.toString()
            )
        }
    }

    private fun showServiceAddedDialog(){
      //  (activity as MainActivity?)!!.getCartCount()
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_service_added, null)
        setServiceAddedDialogData(view,dialog)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        btn_submit.isEnabled = true
    }

    private fun setServiceAddedDialogData(view: View, dialog: BottomSheetDialog) {
        view.iv_operation_image.setImageResource(R.drawable.ic_insurance) //operation image
        view.tv_service_name.text = et_company_name.text.toString()
        view.tv_provider_name.text=""
        view.tv_price_after.text = et_monthly_installment.text.toString() +" "+ App.context.getString(R.string.egp_per)+spinner_installment_plan.selectedItem.toString()
        view.tv_price_before.text=""
        view.iv_close.setOnClickListener {dialog.dismiss()} //close...
        view.btn_continue.setOnClickListener { dialog.dismiss() } //close....
        view.btn_checkout.setOnClickListener {

            navigateToRequests()//cart....
            dialog.dismiss()
        } //check out.....
    }


    private fun handleCartBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {
            LogAdjustEvent("l2kjzs")
            response.let {
              //  startActivity(activity?.intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewmodel.needFinancialInfo()))
                showServiceAddedDialog()

            }
        } else {
            showMessage(response.message!!)
        }
    }
}