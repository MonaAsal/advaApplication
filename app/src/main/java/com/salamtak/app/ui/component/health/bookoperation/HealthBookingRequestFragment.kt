//package com.salamtak.app.ui.component.health.bookoperation
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import androidx.activity.viewModels
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.Hospital
//import com.salamtak.app.data.entities.InstallmentType
//import com.salamtak.app.data.entities.OperationDetails
//import com.salamtak.app.data.entities.responses.BaseResponse
//import com.salamtak.app.data.enums.ProviderType
//import com.salamtak.app.ui.common.BaseFragment
//import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
//import com.salamtak.app.ui.component.health.bookoperation.adapter.InstallmentTypesAdapter
//import com.salamtak.app.ui.component.health.doctor.DoctorActivity
//import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderActivity
//import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
//import com.salamtak.app.utils.*
//import kotlinx.android.synthetic.main.activity_book_operation_n.*
//import kotlinx.android.synthetic.main.dialog_service_added.view.*
//import kotlinx.android.synthetic.main.toolbar.*
//import org.jetbrains.anko.intentFor
//
//
//class HealthBookingRequestFragment : BaseFragment(), AdapterView.OnItemSelectedListener,
//    RecyclerItemPositionListener<InstallmentType> {
//    override val layoutId: Int
//        get() = R.layout.fragment_health_booking_request
//    val viewModelBooking: BookOperationViewModel by viewModels()
//    lateinit var installmentTypesAdater: InstallmentTypesAdapter
//
//    override fun observeViewModel() {
//        with(viewModelBooking)
//        {
//            observe(showLoading, ::showLoadingView)
//            observe(showServerError, ::showServerErrorMessage)
//            // observe(bookResponse, ::handleBookingResponse)
//            observe(healthbBookingResponse,::handleBookingResponse)
//            observeEvent(openLogin, ::navigateToLoginScreen)
//            observeEvent(openSubmitted, ::handleNavigation)
//            observe(selectedInstallmentType, ::handleSelectInstallmentPlan)
//            observe(operationDetails, ::showOperationDetails)
//        }
//    }
//
//    fun showLoadingView(show: Boolean) {
//        if (show)
//            progress_bar.toVisible()
//        else {
//            progress_bar.toGone()
//            btn_submit.isEnabled = true
//        }
//    }
//
//    private fun handleSelectInstallmentPlan(installmentType: InstallmentType) {
//        LogAdjustEvent(
//            "sqsdjl",
//            "plan",
//            installmentType!!.numberOfMonths.toString()
//        )// book rate plan
//
//        LogUtil.LogFirebaseEvent(
//            "btn_installment_plan",
//            "screen_" + this::class.java.simpleName,
//            "plan",
//            installmentType.numberOfMonths.toString()
//        )
//    }
//
//    private fun handleNavigation(event: Event<Any>) {
//        navigateToRequestSubmittedScreen(event.getContentIfNotHandled() as Boolean)
//    }
//
////    fun openFinancialProfile(event: Event<Any>) {
////        startActivity(intentFor<FinancialInfoTypesActivity>())
////        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
////    }
//
//    private fun handleBookingResponse(response: BaseResponse) {
//
//        if (response.status!!)
//            response.let {
//                LogAdjustEvent(
//                    "6g2cvq"
//                )
////                LogAdjustEvent(
////                    "6g2cvq",
////                    "operation",
////                    viewModelBooking.selectedSalamtakOperationN!!.branchName +
////                            " price: " + viewModelBooking.selectedSalamtakOperationN!!.salamtakOperationPrice +
//////                        " doctor: " + viewModelBooking.selectedSalamtakOperation.value!!.doctor!!.name
////                            " plan: " + viewModelBooking.selectedInstallmentType.value!!.numberOfMonths
////                )
//
//                viewModelBooking.checkNavigation()
//            }
//        else {
////                    showMessage(response.data.message!!)
//        }
//    }
//
//    private fun navigateToRequestSubmittedScreen(hasFinancialInfo: Boolean) {
//        startActivity(intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to hasFinancialInfo))
//
//
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModelBooking.operationId = arguments?.getString(Constants.KEY_OPERATION_ITEM)!!
////        viewModelBooking.operationId = "cbd6ac5f-2530-447d-a412-1f6bcc34520c"
////        viewModelBooking.operationId = "80d58002-ffac-44ef-806c-053cc5f4faed"
//        initViews()
//    }
//
//    private fun initViews() {
//        viewModelBooking.getOperationDetails()
//        viewModelBooking.isFinancialProfileCompleted()
//
//        LogUtil.LogFirebaseEvent(
//            "btn_book_now",
//            "screen_" + this::class.java.simpleName,
//            "operation",
//            viewModelBooking.operationId
//        )
//
//        //tv_toolbar_title.text = getString(R.string.book)
//        iv_toolbar_back.setOnClickListener {
//            LogUtil.LogFireBaseBackEvent(
//                "screen_" + this::class.java.simpleName
//            )
//
//            onBackPressed()
//        }
//
////        var operation = viewModelBooking.selectedOperation.value!!
//    }
//
//    @SuppressLint("SetTextI18n")
//    fun showOperationDetails(operationDetails: OperationDetails) {
//
//        bindHospitals(operationDetails.hospitals)
//        tv_toolbar_title.text = operationDetails.operationName
//        rb_operation.rating = operationDetails.rate
//        tv_reviews.text = operationDetails.rate.toString()
//        tv_operation_owner.text = operationDetails.owner!!.name
//
//        tv_operation_owner.setOnClickListener {
//            onOwnerClicked(operationDetails)
//        }
//        tv_reviews_count.text = String.format(
//            viewModelBooking.getLocale(),
//            getString(R.string.num_reviews),
//            operationDetails.reviewsCount
//        )
//
//        tv_operation_details.text = operationDetails.details
//
//        if (tv_operation_details.lineCount > 2) {
//            tv_operation_details.maxLines = 2
//            tv_more.toVisible()
//        } else {
//            tv_more.toGone()
//        }
//
//        tv_more.setOnClickListener {
//            if (tv_operation_details.maxLines > 2) {
//                tv_operation_details.maxLines = 2
//                tv_more.text = getString(R.string.see_more)
//            } else {
//                tv_operation_details.maxLines = 10
//                tv_more.text = getString(R.string.view_less)
//
//            }
//        }
//
//        //spinner_hospitals.
////        operation.owner.type ==
//        //operationDetails.owner?.let { tv_operation_more.text = operationDetails.owner!!.name }
//
////        if (operation.owner!!.type != null) {
////            tv_operation_doctor.text = operation.doctor?.name
////        } else {
////            tv_operation_doctor.text = getString(R.string.hospital_doctor)
//////            tv_operation_doctor.toGone()
//////            tv_by.toGone()
////        }
//
//
//        operationDetails.owner!!.imageUrl?.let {
//            iv_operation_image.loadImage( operationDetails.owner!!.imageUrl) //operation image
//        }
//        installmentTypesAdater =
//            InstallmentTypesAdapter(
//                this, viewModelBooking.getLocale()
//            )
//
////        bindListData(operationDetails.i)
//
//        btn_submit.setOnClickListener {
//            //  submitOperationBooking()
//            addHealthBookingToCart()
//        }
//
//        val currency=getString(R.string.Egp)
//        val price= toDecimalNumberFormat(viewModelBooking.calculateDownPayment().toDouble())
//        tv_down_payment.setText(price  +" "+ currency)
//    }
//
//    fun onOwnerClicked(operation: OperationDetails) {
//        if (operation.owner!!.type == ProviderType.Doctor.typeId)
//            startActivity(
//                intentFor<DoctorActivity>(
//                    Constants.KEY_ID to operation.owner!!.id
//                )
//            )
//        else
//            startActivity(
//                intentFor<MedicalProviderActivity>(
//                    Constants.KEY_ID to operation.owner!!.id
//                )
//            )
//    }
//
//    private fun submitOperationBooking() {
//
//        btn_submit.isEnabled = false
//        LogUtil.LogFirebaseEvent(
//            "btn_submit",
//            "screen_" + this::class.java.simpleName,
//            "operation", ""
////            it.id.toString()
//        )
//
//        viewModelBooking.bookOperation()
//
//    }
//
//    private fun addHealthBookingToCart(){
//        btn_submit.isEnabled = false
//        LogUtil.LogFirebaseEvent(
//            "btn_submit",
//            "screen_" + this::class.java.simpleName,
//            "operation", "")
//        showServiceAddedDialog()
//
//    }
//
//    private fun showServiceAddedDialog(){
//        val dialog = BottomSheetDialog(this)
//        val view = layoutInflater.inflate(R.layout.dialog_service_added, null)
//        setServiceAddedDialogData(view,dialog)
//        dialog.setCancelable(false)
//        dialog.setContentView(view)
//        dialog.show()
//        btn_submit.isEnabled = true
//    }
//
//    private fun setServiceAddedDialogData(view: View, dialog: BottomSheetDialog) {
//        view.iv_operation_image.loadImage(viewModelBooking.operationDetails.value?.owner?.imageUrl) //operation image
//        view.tv_service_name.text = viewModelBooking.operationDetails.value?.operationName
//        view.tv_provider_name.text=viewModelBooking.operationDetails.value?.owner?.name
//        view.tv_price_after.text= viewModelBooking.operationDetails.value?.owner?.startFrom.toString()
//        view.tv_price_before.text=""
//        view.iv_close.setOnClickListener {dialog.dismiss()} //close...
//        view.btn_continue.setOnClickListener { dialog.dismiss() } //close....
//        view.btn_checkout.setOnClickListener {
//            viewModelBooking.addCategoryHealthBookingToCart()
//            dialog.dismiss()
//        } //check out.....
//    }
//
//    private fun bindHospitals(data: List<Hospital>) {
//
//        var hospitals = (data.map { it.hospitalName }).toMutableList()
//
//        val adapter = ArrayAdapter(
//            this,
//            R.layout.item_spinner, hospitals
//        )
//
//        spinner_hospitals.adapter = adapter
//        spinner_hospitals.onItemSelectedListener = this
//
//    }
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        when (parent?.id) {
//            R.id.spinner_hospitals -> {
//                viewModelBooking.selectHospital(position)
//                bindBranchesData(viewModelBooking.operationDetails.value!!.hospitals[position])
//            }
//
//            R.id.spinner_branches -> {
//                viewModelBooking.selectBranch(position)
//                viewModelBooking.selectedSalamtakOperationN?.let {
//                    bindInstallmentTypesListData(it.installmentTypes)
//                }
//            }
//        }
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//
//    }
//
//    private fun bindInstallmentTypesListData(types: List<InstallmentType>) {
//        if (!(types.isNullOrEmpty())) {
//            types.map { type ->
//                type.monthlyAmount = viewModelBooking.getInstallmentPerMonth(type).toDouble()
//            }
//
//            installmentTypesAdater.setList(types.toMutableList())
//            rv_installment_types_list.adapter = installmentTypesAdater
//
//            val linearLayoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            rv_installment_types_list.layoutManager = linearLayoutManager
//
//            addVerticalItemDecoration(rv_installment_types_list)
//
//            viewModelBooking.selectInstallmentType(types[0])
////            val dividerItemDecoration = DividerItemDecoration(
////                rv_installment_types_list.context,
////                linearLayoutManager.orientation
////            )
////
////            dividerItemDecoration.setDrawable(
////                ContextCompat.getDrawable(
////                    this,
////                    R.drawable.vertical_divider
////                )!!
////            )
////
////            rv_installment_types_list.addItemDecoration(dividerItemDecoration)
//
//        }
////        EspressoIdlingResource.decrement()
//    }
//
//    private fun bindBranchesData(hospital: Hospital) {
//        var list =
//            (hospital.salamtakOperationsWithBranches.map { it.branchName }).toMutableList()
//
//        val adapter = ArrayAdapter(
//            this,
//            R.layout.item_spinner, list
//        )
//
//        spinner_branches.adapter = adapter
//        spinner_branches.onItemSelectedListener = this
//    }
//
//    override fun onItemSelected(item: InstallmentType, position: Int) {
//        installmentTypesAdater.setSelectedItem(position, item)
//        viewModelBooking.selectInstallmentType(item)
//    }
//
//
//}