package com.salamtak.app.ui.component.health.customoperations

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.CustomOperationLookupsResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_book_operation_n.*
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.activity_other_operation.*
import kotlinx.android.synthetic.main.activity_other_operation.btn_submit
import kotlinx.android.synthetic.main.activity_other_operation.et_doctor
import kotlinx.android.synthetic.main.activity_other_operation.et_monthly_installment
import kotlinx.android.synthetic.main.activity_other_operation.et_operation
import kotlinx.android.synthetic.main.activity_other_operation.et_price
import kotlinx.android.synthetic.main.activity_other_operation.et_speciality
import kotlinx.android.synthetic.main.activity_other_operation.progress_bar
import kotlinx.android.synthetic.main.activity_other_operation.spinner_installment_plan
import kotlinx.android.synthetic.main.activity_other_operation.spinner_speciality
import kotlinx.android.synthetic.main.activity_other_operation.tv_down_payment
import kotlinx.android.synthetic.main.dialog_service_added.view.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step1.*
import kotlinx.android.synthetic.main.fragment_other_operation.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class CustomOperationFragment : BaseFragment() , AdapterView.OnItemSelectedListener {
    override val layoutId: Int
        get() = R.layout.fragment_other_operation

    var showError = false

    val customOperationViewModel: CustomOperationViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()
        customOperationViewModel.getCustomOperationLookups()

        initViews()
        addCustomHealthBookingToCart()
        workingWithPrice()
        workingWithPayment()
    }

    private fun initViews() {
        tv_toolbar_title.text=getString(R.string.operation_request)
        et_phone_health.filters = (Constants.DisableDecimalWithMaxLength(11))
        et_phone_health.setText(customOperationViewModel.getUser()!!.phone)
        et_name_health.setText(customOperationViewModel.getUserName())
        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }

    private fun workingWithPayment() {
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

    private fun workingWithPrice() {
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
                                OtherOperationActivity.InputFilterMinMax(0,
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

    private fun addCustomHealthBookingToCart() {
        btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
            customOperationViewModel.addCustomHealthBookingToCart(
                spinner_speciality.selectedItemPosition!!,
                et_doctor.text.toString(),
                et_operation.text.toString(),
                et_monthly_installment.text.toString(),
                tv_down_payment.text.toString(),
                et_price.text.toString()
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

    @SuppressLint("SetTextI18n")
    private fun setServiceAddedDialogData(view: View, dialog: BottomSheetDialog) {
        view.iv_operation_image.setImageResource(R.drawable.ic_doctor_avatar) //operation image
        view.tv_service_name.text = et_operation.text.toString()
        view.tv_provider_name.text=et_doctor.text.toString()
        view.tv_price_after.text = et_monthly_installment.text.toString() +" "+ App.context.getString(R.string.egp_per)+spinner_installment_plan.selectedItem.toString()
        view.tv_price_before.text=""
        view.iv_close.setOnClickListener {dialog.dismiss()} //close...
        view.btn_continue.setOnClickListener { dialog.dismiss() } //close....
        view.btn_checkout.setOnClickListener {

            navigateToRequests() //cart....
            dialog.dismiss()
        } //check out.....
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
        val adapter = activity?.let {
            ArrayAdapter(
                it?.applicationContext,
                R.layout.item_spinner, list
            )
        }

        spinner_speciality.adapter = adapter
        spinner_speciality.onItemSelectedListener = this

    }

    private fun bindInstallmentTypes(data: List<InstallmentType>) {
        val adapter = activity?.let {
            ArrayAdapter(
                it?.applicationContext,
                R.layout.item_spinner, data.map { it.name }
            )
        }

        spinner_installment_plan.adapter = adapter
        spinner_installment_plan.onItemSelectedListener = this
    }

    override fun observeViewModel() {
        with(customOperationViewModel)
        {
            observe(customOperationViewModel.customOperationFromState, ::handleFormState)
          //  observe(customOperationViewModel.customOperationResponse, ::handleBookingResponse)
            observe(customOperationViewModel.customHealthbBookingResponse, ::handleBookingResponse)
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
//                startActivity(
//                    activity?.intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO
//                            to customOperationViewModel.needFinancialInfo()))

                showServiceAddedDialog()

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
        clearBacStack()
        navigateToHome()
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