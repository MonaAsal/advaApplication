package com.salamtak.app.ui.component.carservices.details

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarProviderDetails.Brand
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.CarProviderDetails.Contacts
import com.salamtak.app.data.entities.CarProviderDetails.Location
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.enums.ContactsType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.carservices.custom.CarFormState
import com.salamtak.app.ui.component.carservices.providers.TagsAdapter
import com.salamtak.app.ui.component.health.bookoperation.adapter.InstallmentTypesAdapter
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_book_operation_n.*
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.activity_education.et_monthly_installment
import kotlinx.android.synthetic.main.activity_education.spinner_installment_plan
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.dialog_service_added.view.*
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.*
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.btn_submit
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.progress_bar
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.rv_installment_types_list
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.tv_down_payment
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.tv_more
import kotlinx.android.synthetic.main.fragment_car_provider_details_request.tv_reviews
import kotlinx.android.synthetic.main.toolbar_back.*
import org.jetbrains.anko.intentFor
import java.util.*
import kotlin.collections.ArrayList


class CarProviderDetailsRequestFragment : BaseFragment(),
    RecyclerItemPositionListener<InstallmentType> {
    override val layoutId: Int
        get() = R.layout.fragment_car_provider_details_request

    val viewModel: CarRequestViewModel by viewModels()

    lateinit var chip: Chip
    var locationsList = ArrayList<Location>()
    var phoneList = ArrayList<String>()
    lateinit var geocoder: Geocoder
    lateinit var addresses: List<Address>
    lateinit var installmentTypesAdapter: InstallmentTypesAdapter
    var brandsListNames = ArrayList<String>()
    var monthlyIstallments =""
    var months=""

    override fun onCreate(savedInstanceState: Bundle?) {
        // activity?.let { hideNotificationBar(it) }
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()
        geocoder = Geocoder(activity, Locale.getDefault())
        back()
        getProviderId()
        getInstallmentTypes()
        getProviderDetails()
        branchesBottomSheet()
        phonesBottomSheet()
        calculateAmount()
        et_amount.filters=Constants.DisableDecimalWithMaxLength(6)
    }

    private fun moreBtn() {
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
    }

    private fun submitForReviewing() {
        btn_submit.setOnClickListener {
            btn_submit.isEnabled = false
              viewModel.addCarToCartRequest(et_amount.text.toString())
           // showServiceAddedDialog()
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
        view.iv_operation_image.loadImage(viewModel.providerDetailsResponse.value?.logoUrl) //operation image
        view.tv_service_name.text =viewModel.providerDetailsResponse.value?.name
        view.tv_provider_name.text=""
        view.tv_price_after.text = monthlyIstallments +" "+ App.context.getString(R.string.egp_per)+months+" "+ App.context.getString(R.string.month)
        view.tv_price_before.text=""
        view.iv_close.setOnClickListener {dialog.dismiss()} //close...
        view.btn_continue.setOnClickListener { dialog.dismiss() } //close....
        view.btn_checkout.setOnClickListener {
          //  viewModel.addCarToCartRequest(et_amount.text.toString())
            navigateToRequests()//cart...
            dialog.dismiss()
        } //check out.....
    }


    private fun calculateAmount() {
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

    private fun getInstallmentTypes() {
        viewModel.getInstallmentsLookup()
        installmentTypesAdapter = InstallmentTypesAdapter(this, viewModel.getLocale())

    }

    private fun getProviderId() {
        viewModel.providerId = arguments?.getString(Constants.KEY_ID)!!
        Log.d("car provider is ", viewModel.providerId)

    }

    private fun getProviderDetails() {
        viewModel.getProviderDetailsById()
    }

    private fun phonesBottomSheet() {
        iv_phone.setOnClickListener {
            openPhonesBottomSheet(phoneList)
        }
    }

    private fun branchesBottomSheet() {
        iv_branch.setOnClickListener {
            openBranchesBottomSheet(locationsList)
        }
    }

    fun openBranchesBottomSheet(locationsList: ArrayList<Location>) {
        val modalBottomSheet = CarBranchesBottomSheet(locationsList)
        activity?.let {
            modalBottomSheet.show(
                it?.supportFragmentManager,
                "CarBranchesBottomSheet"
            )
        }
    }

    fun openPhonesBottomSheet(phoneList: ArrayList<String>) {
        val modalBottomSheet = CarPhonesBottomSheet(this.phoneList)
        activity?.supportFragmentManager?.let { modalBottomSheet.show(it, "CarPhonesBottomSheet") }
    }

    private fun back() {
        tv_toolbar_title.text = getString(R.string.service_center)
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(providerDetailsResponse, ::showProviderDetails)
            observe(installmentTypes, ::bindInstallmentTypes)
            observe(downPaymentString, ::showDownPayment)
            observe(customOperationFromState, ::handleFormState)
           // observe(customRequestResponse, ::handleBookingResponse)
            observe(carBookingResponse, ::handleBookingResponse)


        }
    }

    private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status != null) {

            LogUtil.LogFirebaseEvent("fca_request_booked", this::class.java.simpleName)
            response.let {
//                startActivity(
//                    activity?.intentFor<RequestSubmittedSuccessfullyActivity>(
//                        Constants.NEED_FINANCIAL_INFO to viewModel.needFinancialInfo()
//                    )
//                )

                showServiceAddedDialog()
            }
        } else {
            showMessage(response.message.toString())
        }
    }

    private fun handleFormState(customOperationFormState: CarFormState) {
        btn_submit.isEnabled = true
        if (customOperationFormState?.totalCostError != null) {
            et_amount.error = getString(customOperationFormState.totalCostError)
            et_amount.shakeView()
        } else
            et_amount.error = null
    }

    private fun bindInstallmentTypes(types: List<InstallmentType>) {
        if (!types.isNullOrEmpty()) {
            installmentTypesAdapter.setList(types.toMutableList())
            rv_installment_types_list.adapter = installmentTypesAdapter
            monthlyIstallments= types.get(0).monthlyAmount.toString()
            months= types.get(0).numberOfMonths.toString()
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

    private fun showProviderDetails(providerDetails: CarProviderDetails) {
        iv_car_provider_logo.loadCircleImage(providerDetails.logoUrl, R.drawable.ic_circle) //image
        tv_provider_name.text = providerDetails.name //name
        showVerified(providerDetails.isVerified)
        providerDetails.rating?.let {
            rb_provider.rating = providerDetails.rating.toFloat() //rating
        }
        setReviews(providerDetails)// hide reviews if it's equal 0
        tv_provider_details.text = providerDetails.about //about
        moreBtn()//show or hide more button
        setServiceList(providerDetails.services) //service list
        setSocialMedia(providerDetails.contacts) //facebook , instegram
        setBranches(providerDetails.locations) //branches
        setBrandsList(providerDetails.brands) //car brands
        submitForReviewing()
    }

    private fun setReviews(providerDetails: CarProviderDetails) {
        if (providerDetails.reviews != null && providerDetails.reviews > 0) {
            tv_reviews.visibility = View.VISIBLE
            tv_reviews.text =
                providerDetails.reviews.toString() + " " + resources.getString(R.string.reviews) //reviewers
        }
    }

    private fun showVerified(verified: Boolean?) {
        if (verified == true) {
            iv_verified_Icon.visibility = View.VISIBLE
            tv_verified.visibility = View.VISIBLE
        } else {
            iv_verified_Icon.visibility = View.GONE
            tv_verified.visibility = View.GONE
        }
    }

    private fun setBranches(mlocations: List<Location>?) {
        if (!(mlocations.isNullOrEmpty())) {
            mlocations?.let {
                tv_contacts_lbl.toVisible()
                iv_branch.toVisible()
                locationsList.addAll(it)
                viewModel.providerDetailsLocationsList.value = locationsList
                Log.d(
                    "locations list From View Model",
                    viewModel.providerDetailsLocationsList.value.toString()
                )

            }
        }
    }

    private fun setSocialMedia(contacts: List<Contacts>?) {
        contacts?.let {
            it.forEach { contact ->
                when (contact.type) {
                    ContactsType.Facebook.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_facebook.toVisible()
                        iv_facebook.setOnClickListener { openLink(contact.value) }
                    }
                    ContactsType.Instagram.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_instgram.toVisible()
                        iv_instgram.setOnClickListener { openLink(contact.value) }
                    }
                    ContactsType.Phone.id -> {
                        tv_contacts_lbl.toVisible()
                        iv_phone.toVisible()
                        phoneList.add(contact.value)
                        // iv_phone.setOnClickListener { callNumber(contact.value) }
                    }
                }
            }
        }
    }

    private fun setServiceList(servicesList: List<String>?) {
        var chipItem = ""
        if (!(servicesList.isNullOrEmpty())) {
            rv_tags_service.apply {

                var adapter = TagsAdapter(servicesList.toMutableList())
                rv_tags_service.layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.HORIZONTAL, false
                )
                rv_tags_service.adapter = adapter
            }

//            for (item in servicesList) {
//                chip = Chip(this)
//                chipItem = item.trim()
//                chip.text = chipItem
//                chipGroup.addView(chip)
//
//            }

        }

    }

    private fun setBrandsList(brandssList: List<Brand>?) {

        if (!(brandssList.isNullOrEmpty())) {
            for (item in brandssList) {
                brandsListNames.add(item.name)
            }

            rv_tags_brands.apply {
                if (!(brandsListNames.isNullOrEmpty())) {
                    var adapter = TagsAdapter(brandsListNames.toMutableList())
                    rv_tags_brands.layoutManager = LinearLayoutManager(
                        App.context,
                        RecyclerView.HORIZONTAL, false
                    )
                    rv_tags_brands.adapter = adapter
                }
            }

//            for (item in brandssList) {
//                chip = Chip(this)
//                chipItem = item.name.trim()
//                chip.text = chipItem
//                chipGroupBrand.addView(chip)
//            }

        }

    }


    private fun openLink(link: String) {
        Log.e("link", link)
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link.trim()))
            startActivity(browserIntent)
        } catch (e: Exception) {
        }
    }

    fun getAddress(latitude: Double, longitude: Double): String {
        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        val address: String =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        //  val city: String = addresses[0].get
        // val state: String = addresses[0].getAdminArea()
        //val country: String = addresses[0].getCountryName()
        // val postalCode: String = addresses[0].getPostalCode()
        // val knownName: String = addresses[0].getFeatureName()

        Log.d("yasmen address", address)
        return address
    }

    private fun showDownPayment(downpayment: String) {
        tv_down_payment.text = downpayment
    }

    override fun onItemSelected(installmentType: InstallmentType, position: Int) {
        Log.e("selectedtypeR", installmentType.numberOfMonths.toString())
        installmentTypesAdapter.setSelectedItem(position, installmentType)
        viewModel.selectInstallmentType(installmentType)

    }
}