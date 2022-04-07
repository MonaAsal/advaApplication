package com.salamtak.app.ui.component.finishing.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingPackage
import com.salamtak.app.data.entities.FinishingProvider
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.enums.ContactsType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.finishing.custom.FinishingFormState
import com.salamtak.app.ui.component.finishing.packagedetails.FinishingPackageDetailsActivity
import com.salamtak.app.ui.component.health.bookoperation.adapter.InstallmentTypesAdapter
import com.salamtak.app.utils.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_education.et_monthly_installment
import kotlinx.android.synthetic.main.activity_education.spinner_installment_plan
import kotlinx.android.synthetic.main.activity_finishing_details.*
import kotlinx.android.synthetic.main.activity_finishing_details.btn_submit
import kotlinx.android.synthetic.main.activity_finishing_details.et_amount
import kotlinx.android.synthetic.main.activity_finishing_details.progress_bar
import kotlinx.android.synthetic.main.activity_finishing_details.rv_installment_types_list
import kotlinx.android.synthetic.main.activity_finishing_details.tv_down_payment
import kotlinx.android.synthetic.main.activity_finishing_details.tv_more
import kotlinx.android.synthetic.main.dialog_service_added.view.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step3.*
import org.jetbrains.anko.intentFor


class FinishingProviderRequestFragment : BaseFragment(),
    FinishingDetailsListener, RecyclerItemPositionListener<InstallmentType> {
    override val layoutId: Int
        get() = R.layout.fragment_finishing_provider_request

    lateinit var installmentTypesAdater: InstallmentTypesAdapter
    lateinit var packagesAdapter: FinishingPackagesAdapter
    val viewModel: FinishingRequestViewModel by viewModels()
    var monthlyIstallments =""
    var months=""
    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoading)
            observe(customOperationFromState, ::handleFormState)
            observe(showServerError, ::showServerErrorMessage)
            observe(providerDetailsResponse, ::showProviderDetails)
            observe(installmentTypes, ::bindInstallmentTypes)
            observe(downPaymentString, ::showDownPayment)
         //   observe(customRequestResponse, ::handleBookingResponse)
            observe(finishingBookingResponse, ::handleBookingResponse)

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preventUiPopingAboveKeyBoard()

        viewModel.providerId = arguments?.getString(Constants.KEY_ID)!!

        viewModel.getInstallmentsLookup()
        viewModel.getProviderDetailsById()
        et_amount.filters=Constants.DisableDecimalWithMaxLength(6)

        iv_back.setOnClickListener { onBackPressed() }

        installmentTypesAdater = InstallmentTypesAdapter(this, viewModel.getLocale())

        et_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (et_amount.text.toString().isNotEmpty())
                    viewModel.onAmountChanged(et_amount.text.toString())
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

    private fun showDownPayment(downpayment: String) {
        tv_down_payment.text = downpayment
    }

    private fun bindPackages(packages: List<FinishingPackage>?) {
        if (!(packages.isNullOrEmpty())) {
            tv_packages_lbl.toVisible()
            rv_packages.toVisible()
            packagesAdapter =
                FinishingPackagesAdapter(packages.toMutableList(), this, viewModel.getLocale())
            rv_packages.adapter = packagesAdapter
            //packagesAdapter.selectPackage(0)
            viewModel.selectedPackageId = packages[0].id
            addVerticalItemDecoration(rv_packages)
        } else {
            tv_packages_lbl.toGone()
            rv_packages.toGone()
        }
    }


    private fun bindInstallmentTypes(types: List<InstallmentType>) {
        if (!types.isNullOrEmpty()) {
            installmentTypesAdater.setList(types.toMutableList())
            rv_installment_types_list.adapter = installmentTypesAdater
            monthlyIstallments= types.get(0).monthlyAmount.toString()
            months= types.get(0).numberOfMonths.toString()
        }
    }

    private fun showProviderDetails(providerDetails: FinishingProvider) {

        providerDetails.images?.let {
            showSlider(it.toMutableList(),providerDetails.images)
          //  imagesArrayList = providerDetails.images
        }

        iv_provider_logo.loadCircleImage(providerDetails.logoUrl, R.drawable.ic_circle)
        tv_provider_name.text = providerDetails.name
//        tv_reviews.text = String.format(getString(R.string.num_reviews), 10)

        providerDetails.contacts?.let {
            it.forEach { contact ->
                when (contact.type) {
                    ContactsType.Facebook.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_facebook.toVisible()
                        iv_facebook.setOnClickListener { openLink(contact.value) }
                    }
                    ContactsType.Website.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_link.toVisible()
                        iv_link.setOnClickListener { openLink(contact.value) }
                    }
                    ContactsType.Instagram.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_instgram.toVisible()
                        iv_instgram.setOnClickListener { openLink(contact.value) }
                    }
                    ContactsType.Phone.id,
                    ContactsType.Mobile.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_phone.toVisible()
                        iv_phone.setOnClickListener { callNumber(contact.value) }
                    }
                }
            }
        }

        tv_price.text = toDecimalNumberFormat(
            providerDetails.pricePerMeter.toDouble()
        )

        tv_provider_details.text = providerDetails.about
//        tv_provider_details.text = getString(R.string.sss)

        rb_provider.text = providerDetails.rating!!.toString()

        if (providerDetails.packageDtos != null && providerDetails.packageDtos.isNotEmpty()) {
            tv_egp2.toGone()
            tv_price.toGone()

            bindPackages(providerDetails.packageDtos)
        } else {
            tv_egp2.toVisible()
            tv_price.toVisible()

            tv_packages_lbl.toGone()
            rv_packages.toGone()
        }

        providerDetails.locations?.let {
            if (it.isNotEmpty()) {
                var locations = StringBuilder()
                it.forEach { s ->
                    locations.append("$locations, $s")
                }
//                tv_locations.text = locations.toString().removeSuffix(", ")
            }
//            else
//                tv_locations.toGone()
        }


        if (tv_provider_details.lineCount > 2) {
            tv_provider_details.maxLines = 2
            tv_more.toVisible()
        } else {
            tv_more.toGone()
        }

        tv_more.setOnClickListener {
            if (tv_provider_details.maxLines > 2) {
                tv_provider_details.maxLines = 2
                tv_more.text = getString(R.string.see_more)
            } else {
                tv_provider_details.maxLines = 10
                tv_more.text = getString(R.string.view_less)
            }
        }

        btn_submit.setOnClickListener {
//            viewModel.createProviderRequest(
//                et_amount.text.toString()
//            )
            //add to cart....
         //   showServiceAddedDialog()
            viewModel.addFinishingToCartRequest(et_amount.text.toString())

        }
    }

    private fun showServiceAddedDialog(){
      //  (activity as MainActivity?)!!.getCartCount()

        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.
        dialog_service_added, null)
        setServiceAddedDialogData(view,dialog)
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
        btn_submit.isEnabled = true
    }

    private fun setServiceAddedDialogData(view: View, dialog: BottomSheetDialog) {
        view.iv_operation_image.loadImage(viewModel.providerDetailsResponse.value?.logoUrl) //operation image
        view.tv_service_name.text = viewModel.providerDetailsResponse.value?.name
        view.tv_provider_name.text=""
        view.tv_price_after.text = monthlyIstallments +" "+ App.context.getString(R.string.egp_per)+months+" "+ App.context.getString(R.string.month)

        view.tv_price_before.text=""
        view.iv_close.setOnClickListener {dialog.dismiss()} //close...
        view.btn_continue.setOnClickListener { dialog.dismiss() } //close....
        view.btn_checkout.setOnClickListener {
            navigateToRequests()
            dialog.dismiss()
        } //check out.....
    }

    private fun showSlider(imgsList: MutableList<String>, images: List<String>) {
        imageSlider.changeSizeAspectRatio(0)
        val adapter = SliderAdapterExample(imgsList)
        adapter.setImages(images)

        imageSlider.setSliderAdapter(adapter)
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

        imageSlider.startAutoCycle()
        imageSlider.setOnClickListener {
//            showInFullScreen(imagesArrayList)
//            Log.d("sliderImages", imagesArrayList.toString())
        }

    }

    fun showLoading(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_submit.isEnabled = false
        } else {
            progress_bar.toGone()
            btn_submit.isEnabled = true
        }
    }

    private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {

            LogUtil.LogFirebaseEvent(
                "finishing_request_booked",
                this::class.java.simpleName
            )

            response.let {
             //   startActivity(activity?.intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewModel.needFinancialInfo()))
         showServiceAddedDialog()

            }
        } else {
            showMessage(response.message!!)
        }
    }


    private fun handleFormState(customOperationFormState: FinishingFormState) {
        btn_submit.isEnabled = true
        if (customOperationFormState?.totalCostError != null) {
            et_amount.error = getString(customOperationFormState.totalCostError)
            et_amount.shakeView()
        } else
            et_amount.error = null
    }


    override fun onFinishingPackageSelected(finishingPackage: FinishingPackage) {
        viewModel.selectedPackageId = finishingPackage.id
    }

    override fun openPackageDetails(finishingPackage: FinishingPackage) {
        startActivity(activity?.intentFor<FinishingPackageDetailsActivity>(Constants.KEY_ID to finishingPackage.id))
    }

    override fun onItemSelected(installmentType: InstallmentType, position: Int) {
        Log.e("selectedtypeR", installmentType.numberOfMonths.toString())
        installmentTypesAdater.setSelectedItem(position, installmentType)
        viewModel.selectInstallmentType(installmentType)
    }

    private fun openLink(link: String) {
        Log.e("link", link)
        try {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(link.trim()))

            startActivity(browserIntent)
        } catch (e: Exception) {
        }
    }

}