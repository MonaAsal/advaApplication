package com.salamtak.app.ui.component.finishing.custom

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse


import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.health.bookoperation.adapter.InstallmentTypesAdapter
import com.salamtak.app.ui.component.insurance.InsuranceActivity
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.activity_insurance.et_monthly_installment
import kotlinx.android.synthetic.main.activity_insurance.spinner_installment_plan
import kotlinx.android.synthetic.main.dialog_service_added.view.*
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.*
import kotlinx.android.synthetic.main.fragment_custom_car_step3.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.btn_submit
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.et_amount
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.progress_bar3
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.rv_installment_types_list
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.tv_down_payment
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.tv_total_amount
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@AndroidEntryPoint
class CustomFinishingStep3Fragment : BaseFragment() ,
    RecyclerItemPositionListener<InstallmentType> {

    override val layoutId: Int
        get() = R.layout.fragment_custom_finishing_step3

    lateinit var installmentTypesAdater: InstallmentTypesAdapter

    val viewModel: CustomFinishingViewModel by activityViewModels()
    var monthlyIstallments =""
    var months=""

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        @JvmStatic
        fun newInstance() =
            CustomFinishingStep3Fragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(CustomFinishingViewModel::class.java)
        preventUiPopingAboveKeyBoard()
        viewModel.getInstallmentsLookup()
        installmentTypesAdater = InstallmentTypesAdapter(this, viewModel.getLocale())
        tv_total_amount.text = tv_total_amount.text.toString()
        workingWithPrice()
        et_amount.filters=Constants.DisableDecimalWithMaxLength(6)
    }

    override fun onResume() {
        super.onResume()

        btn_submit.setOnClickListener {
            viewModel.saveStep3Data()
        }
    }

    private fun workingWithPrice() {
        et_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                var price = et_amount.text.toString()
                if (!price.isNullOrEmpty()){
                    if(!price.contains(",") && !price.contains("..")){
                        viewModel.onAmountChanged(et_amount.text.toString())
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

    }

    private fun showServiceAddedDialog(){
     //   (activity as MainActivity?)?.getCartCount()
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_service_added, null)
        setServiceAddedDialogData(view,dialog)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("fragment","onDestroyView")

    }

    override fun onDetach() {
        super.onDetach()
        Log.d("fragment","onDetach")
    }

    @SuppressLint("SetTextI18n")
    private fun setServiceAddedDialogData(view: View, dialog: BottomSheetDialog) {
        view.iv_operation_image.setImageResource(R.drawable.ic_finishing) //operation image
        view.tv_service_name.text = viewModel.providerName
        view.tv_provider_name.text=""
      //  view.tv_price_after.text =et_amount.text
        view.tv_price_after.text = monthlyIstallments +" "+ App.context.getString(R.string.egp_per)+months+" "+ App.context.getString(R.string.month)
        view.tv_price_before.text=""
        view.iv_close.setOnClickListener {
            btn_submit.isEnabled = true
            dialog.dismiss()} //close...
        view.btn_continue.setOnClickListener {
            btn_submit.isEnabled = true
            dialog.dismiss() } //close....
        view.btn_checkout.setOnClickListener {
          //  navigateToRequests()
            navigateToCartScreen()

            dialog.dismiss()
        } //check out.....
    }


    private fun navigateToCartScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("navigateToCart",true)
        startActivity(intent)
    }

    private fun showDownPayment(downpayment: String) {
        tv_down_payment.text = downpayment
    }

    private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {
            response.let {
                LogUtil.LogFirebaseEvent(
                    "finishing_request_booked_custom",
                    this::class.java.simpleName
                )
                showServiceAddedDialog()


//                startActivity(
//                    requireActivity().intentFor<RequestSubmittedSuccessfullyActivity>(
//                        Constants.NEED_FINANCIAL_INFO to viewModel.needFinancialInfo()
//                    )
//                )
            }
        } else {
            (requireActivity() as BaseActivity).showMessage(response.message!!)
        }
    }

    private fun bindInstallmentTypes(types: List<InstallmentType>) {
        if (!types.isNullOrEmpty()) {
            installmentTypesAdater.setList(types.toMutableList())
            monthlyIstallments= types.get(0).monthlyAmount.toString()
            months= types.get(0).numberOfMonths.toString()
            rv_installment_types_list.adapter = installmentTypesAdater
        }
    }


    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(customFromState, ::handleFormState)
            observe(installmentTypes, ::bindInstallmentTypes)
            observe(downPaymentString, ::showDownPayment)
            observe(customFinishingBookingResponse, ::handleBookingResponse)

            //  observe(customRequestResponse, ::handleBookingResponse)
        }
    }

    fun showLoadingView(show: Boolean) {
        Log.e("showloading", show.toString())
        progress_bar3?.let {
            if (show) {
                progress_bar3.toVisible()
                btn_submit.isEnabled = false
            } else {
                progress_bar3.toGone()
                btn_submit.isEnabled = true
            }
        }
    }

    private fun handleFormState(customOperationFormState: FinishingFormState) {
        btn_submit.isEnabled = true

        if (customOperationFormState?.totalCostError != null) {
            et_amount.error = getString(customOperationFormState.totalCostError)
            et_amount.shakeView()
        }
    }

    override fun onItemSelected(installmentType: InstallmentType, position: Int) {
        monthlyIstallments= installmentType.monthlyAmount.toString()
        months= installmentType.numberOfMonths.toString()

        installmentTypesAdater.setSelectedItem(position, installmentType)
        viewModel.selectInstallmentType(installmentType)
    }


}