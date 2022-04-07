package com.salamtak.app.ui.component.financialinfo.step3

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BaseResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.financialinfo.FinancialProfileCompletedActivity
import com.salamtak.app.ui.component.financialinfo.ImageActivity
import com.salamtak.app.ui.component.financialinfo.ImagesListener
import com.salamtak.app.ui.component.financialinfo.saveinfo.adapter.AttachmentsAdapter
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_financial_info_step3.*
import kotlinx.android.synthetic.main.layout_employee.*
import kotlinx.android.synthetic.main.layout_steps.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import java.io.File

@AndroidEntryPoint
class FinancialInfoStep3Activity : BaseActivity(),
    AdapterView.OnItemSelectedListener, AttachCallBacks, ImagesListener {
    override val layoutId: Int
        get() = R.layout.activity_financial_info_step3

    //    @Inject
//    lateinit
    val financialViewModel: FinancialViewModelStep3 by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    lateinit var imageSelection: ImageSelection

    //    lateinit var attachmentsAdapter: AttachmentsAdapter
    var financialProfileId = ""
    var categoryId = ""

    lateinit var attachmentsAdapterEmployee: AttachmentsAdapter
    lateinit var attachmentsAdapterPension: AttachmentsAdapter
    lateinit var attachmentsAdapterPropertyRental: AttachmentsAdapter
    lateinit var attachmentsAdapterBankCertificates: AttachmentsAdapter
    lateinit var attachmentsAdapterBusinessOwner: AttachmentsAdapter

    override fun initializeViewModel() {
//        financialViewModel = viewModelFactory.create(FinancialViewModelStep3::class.java)
    }

    override fun onResume() {
        super.onResume()
        financialViewModel.getFinancialProgress()
        //iv_step3.setBackgroundColor(ContextCompat.getColor(this, R.color.separator))
    }

    override fun observeViewModel() {
        with(financialViewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(bindTypesSpinner, ::bindTypesSpinner)
            observe(saveTypeDataResponseStep3, ::savedSuccessfully)
            observe(showEmployeeData, ::initEmployeeData)
            observe(showAssetsOwnerData, ::initAssetsOwnerData)
            observe(showBankCertificateData, ::initBankCertificateData)
            observe(showBusinessOwnerData, ::initBusinessOwnerData)
            observe(showPensionData, ::initPensionData)
            observe(showValuCustomerData, ::initValuCustomerData)
            observe(step3FormState, ::handleFromState)
            observe(showServerError, ::showServerErrorMessage)
            observe(showCompleteMessage, ::showCompleteMessage)
            observe(updateEmployeeList, ::updateEmployeeList)
            observe(updatePensionList, ::updatePensionList)
            observe(updateAssetsOwnerList, ::updateAssetsOwnerList)
            observe(updateBusinessOwnerList, ::updateBusinessOwnerList)
            observe(updateBankCertificateList, ::updateBankCertificateList)
            observe(goNext, ::goNext)
            observe(financialProgressResponse, ::showProgress)
            observe(showPensionAttachments, ::showPensionAttachments)
            observe(showEmployeeAttachments, ::showEmployeeAttachments)
            observe(showBusinessOwnerAttachments, ::showBusinessOwnerAttachments)
            observe(showBankCertificateAttachments, ::showBankCertificateAttachments)
            observe(showPropertyRentalAttachments, ::showPropertyRentalAttachments)
        }
    }

    private fun showProgress(baseResponse: BaseResponse) {
        try {
            handleSteps(baseResponse.message!!.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showCompleteMessage(b: Boolean?) {
        showMessageNoIcon(getString(R.string.complete_fp), getString(R.string.complete_fp_message))

    }

    private fun bindTypesSpinner(data: Boolean) {
        bindTypesSpinner()
    }

    private fun initPensionData(it: FinancialProfilePensionCategory) {
        removeFromSpinnerList(App.context.getString(R.string.pension))
        group_pension.toVisible()
        iv_delete_pension.toGone()
        hideTopListShowBottomOne()
        showPensionData(it)
    }

    private fun initValuCustomerData(it: FinancialProfileValuCustomerCategory) {

        removeFromSpinnerList(App.context.getString(R.string.valu_customer))
        group_value_customer.toVisible()
        iv_delete_value_customer.toGone()
        hideTopListShowBottomOne()
        showValuCustomerData(it)

    }

    private fun initBusinessOwnerData(it: FinancialProfileBusinessOwnerCategory) {
        removeFromSpinnerList(App.context.getString(R.string.business_owner))
        group_business_owner.toVisible()
        iv_delete_business_owner.toGone()
        hideTopListShowBottomOne()
        showBusinessOwnerData(it)
    }

    private fun initBankCertificateData(it: FinancialProfileBankCertificateCategory) {
        removeFromSpinnerList(App.context.getString(R.string.bank_certificates))
        group_bank_certificates.toVisible()
        iv_delete_bank_certificates.toGone()
        hideTopListShowBottomOne()
        showBankCertificateData(it)
    }

    private fun initAssetsOwnerData(financialProfileAssetsOwnerCategory: FinancialProfileAssetsOwnerCategory) {
        removeFromSpinnerList(App.context.getString(R.string.property_renter))
        group_property_rental.toVisible()
        iv_delete_property_rental.toGone()
        hideTopListShowBottomOne()
        showAssetsOwnerData(financialProfileAssetsOwnerCategory)
    }

    private fun goNext(b: Boolean?) {
        navigateToMainScreen()
    }

    private fun updateBusinessOwnerList(b: Boolean?) {
        attachmentsAdapterBusinessOwner.setList(financialViewModel.businessOwnerAttachmentsTypesObj.toMutableList())
        attachmentsAdapterBusinessOwner.updateImages(imageSelection.position)
    }

    private fun updateBankCertificateList(b: Boolean?) {
        attachmentsAdapterBankCertificates.setList(financialViewModel.bankCertificateAttachmentsTypesObj.toMutableList())
        attachmentsAdapterBankCertificates.updateImages(imageSelection.position)
    }

    private fun updatePensionList(b: Boolean?) {
        attachmentsAdapterPension.setList(financialViewModel.pensionAttachmentsTypesObj.toMutableList())
        attachmentsAdapterPension.updateImages(imageSelection.position)
    }

    private fun updateAssetsOwnerList(b: Boolean?) {
        attachmentsAdapterPropertyRental.setList(financialViewModel.assetsOwnerAttachmentsTypesObj.toMutableList())
        attachmentsAdapterPropertyRental.updateImages(imageSelection.position)
    }

    private fun updateEmployeeList(b: Boolean?) {
        attachmentsAdapterEmployee.setList(financialViewModel.employeeAttachmentsTypesObj.toMutableList())
        attachmentsAdapterEmployee.updateImages(imageSelection.position)
    }

    fun hideTopListShowBottomOne() {
        if (financialViewModel.spinnerTypes.size <= 1) {
            group_add_anther.toGone()
        } else {
            group_add_anther.toVisible()
        }
        tv_spinner_type.toGone()
        layout_types.toGone()
        btn_next.toVisible()
    }

    fun showTopList() {
        group_add_anther.toGone()
        tv_spinner_type.toVisible()
        layout_types.toVisible()
        btn_next.toGone()
    }

    fun removeFromSpinnerList(type: String) {
        var type =
            financialViewModel.findFinancialType(type)
        when (type.id) {
            1 -> group_employee.toVisible()
            2 -> group_pension.toVisible()
            3 -> group_property_rental.toVisible()
            4 -> group_business_owner.toVisible()
            6 -> group_bank_certificates.toVisible()
            9 -> group_value_customer.toVisible()
        }

        financialViewModel.addTypeToDisplayedList(type)
        financialViewModel.removeFinancialTypeFromSpinnerList(type.name)
        bindTypesSpinner()
    }

    private fun initEmployeeData(employeeData: FinancialProfileEmployeesCategory) {
        removeFromSpinnerList(App.context.getString(R.string.employee))
        group_employee.toVisible()
        iv_delete_employee.toGone()
        hideTopListShowBottomOne()
        showEmployeeData(employeeData)
    }


    private fun handleSteps(progress: Int) {
        Log.e("steps", progress.toString())
        when (progress) {
            3 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
            }
            2 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
            }
            1 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
            }
        }
    }

    private fun showValuCustomerData(data: FinancialProfileValuCustomerCategory) {
        et_value_customer_name.setText(data.name)
        et_value_customer_email.setText(data.email)
        et_value_customer_phone.setText(data.phoneNumber)
    }

    fun showBankCertificateData(data: FinancialProfileBankCertificateCategory) {
        et_bank_certificates_amount.setText(data.netIncome.toString())
    }

    fun showBusinessOwnerData(data: FinancialProfileBusinessOwnerCategory) {
        et_business_owner_amount.setText(data.companyNetIncome.toString())
        et_company_address_business_owner.setText(data.companyAddress)
        et_company_name_business_owner.setText(data.companyName)
    }

    private fun showPensionData(data: FinancialProfilePensionCategory) {
        et_pension_amount.setText(data.pensionNetAmount.toString())
    }

    private fun showAssetsOwnerData(data: FinancialProfileAssetsOwnerCategory) {
        et_property_rental_amount.setText(data.netIncome.toString())
    }


    private fun showPropertyRentalAttachments(data: FinancialProfileAssetsOwnerCategory) {
        if (financialViewModel.propertyRentAttachments.size != data.attachments!!.size) {
            data.attachments!!.removeAt(data.attachments!!.size - 1)
            data.attachments!!.removeAt(data.attachments!!.size - 1)
        }
        financialViewModel.assetsOwnerAttachmentsTypesObj = data.attachments!!

        attachmentsAdapterPropertyRental.setList(financialViewModel.assetsOwnerAttachmentsTypesObj)
        rv_attachments_property_rental.adapter = attachmentsAdapterPropertyRental
    }

    private fun showBankCertificateAttachments(data: FinancialProfileBankCertificateCategory) {
        if (financialViewModel.bankCertificatesAttachments.size != data.attachments!!.size) {
            data.attachments!!.removeAt(data.attachments!!.size - 1)
            data.attachments!!.removeAt(data.attachments!!.size - 1)
        }
        financialViewModel.bankCertificateAttachmentsTypesObj = data.attachments!!

        attachmentsAdapterBankCertificates.setList(financialViewModel.bankCertificateAttachmentsTypesObj)
        rv_attachments_bank_certificates.adapter = attachmentsAdapterBankCertificates
    }

    private fun showBusinessOwnerAttachments(data: FinancialProfileBusinessOwnerCategory) {
        if (financialViewModel.businessOwnerAttachments.size != data.attachments!!.size) {
            data.attachments!!.removeAt(data.attachments!!.size - 1)
            data.attachments!!.removeAt(data.attachments!!.size - 1)
        }
        financialViewModel.businessOwnerAttachmentsTypesObj = data.attachments!!

        attachmentsAdapterBusinessOwner.setList(financialViewModel.businessOwnerAttachmentsTypesObj)
        rv_attachments_business_owner.adapter = attachmentsAdapterBusinessOwner
    }

    private fun showEmployeeAttachments(data: FinancialProfileEmployeesCategory) {
        if (financialViewModel.employeeAttachments.size != data.attachments!!.size) {
            data.attachments!!.removeAt(data.attachments!!.size - 1)
            data.attachments!!.removeAt(data.attachments!!.size - 1)
        }
        financialViewModel.employeeAttachmentsTypesObj = data.attachments!!
        attachmentsAdapterEmployee.setList(financialViewModel.employeeAttachmentsTypesObj)

        rv_attachments_employee.adapter = attachmentsAdapterEmployee
    }

    private fun showPensionAttachments(data: FinancialProfilePensionCategory) {
        if (financialViewModel.pensionAttachments.size != data.attachments!!.size) {
            data.attachments!!.removeAt(data.attachments!!.size - 1)
            data.attachments!!.removeAt(data.attachments!!.size - 1)
        }
        financialViewModel.pensionAttachmentsTypesObj = data.attachments!!

        attachmentsAdapterPension.setList(financialViewModel.pensionAttachmentsTypesObj)
        rv_attachments_pension.adapter = attachmentsAdapterPension
    }

    private fun handleFromState(formState: Step3FormState) {

        //employment
        if (formState?.jobError != null) {
            et_job.error = getString(formState?.jobError)
            et_job.shakeView()
            scrollTo(getEmployeeYPosition())
        }

        if (formState?.incomeError != null) {
            et_income.error = getString(formState?.incomeError)
            et_income.shakeView()
            scrollTo(getEmployeeYPosition())
        }

        if (formState?.companyNameError != null) {
            et_company_name.error = getString(formState?.companyNameError)
            et_company_name.shakeView()
            scrollTo(getEmployeeYPosition())
        }

        if (formState?.companyAddressError != null) {
            et_company_address.error = getString(formState?.companyAddressError)
            et_company_address.shakeView()
            scrollTo(getEmployeeYPosition())
        }

        /////
        if (formState?.pensionLimitError != null) {
            et_pension_amount.error = getString(formState?.pensionLimitError)
            et_pension_amount.shakeView()
            scrollTo(getPensionYPosition())
        }

        if (formState?.assetsOwnerLimitError != null) {
            et_property_rental_amount.error = getString(formState?.assetsOwnerLimitError)
            et_property_rental_amount.shakeView()
            scrollTo(getPropertyRentalYPosition())
        }

        /// business owner
        if (formState?.businessOwnerCompanyAddressError != null) {
            et_company_address_business_owner.error =
                getString(formState?.businessOwnerCompanyAddressError)
            et_company_address_business_owner.shakeView()
            scrollTo(getBusinessOwnerYPosition())
        }

        if (formState?.businessOwnerCompanyNameError != null) {
            et_company_name_business_owner.error =
                getString(formState?.businessOwnerCompanyNameError)
            et_company_name_business_owner.shakeView()
            scrollTo(getBusinessOwnerYPosition())
        }

        if (formState?.businessOwnerIncomeError != null) {
            et_business_owner_amount.error = getString(formState?.businessOwnerIncomeError)
            et_business_owner_amount.shakeView()
            scrollTo(getBusinessOwnerYPosition())
        }


        if (formState?.bankCertificateAmountError != null) {
            et_bank_certificates_amount.error = getString(formState?.bankCertificateAmountError)
            et_bank_certificates_amount.shakeView()
            scrollTo(getBankCertificateYPosition())
        }

        //valu
        if (formState?.valuPhoneError != null) {
            et_value_customer_phone.error = getString(formState?.valuPhoneError)
            et_value_customer_phone.shakeView()
        } //else et_value_customer_phone.error = null

        if (formState?.valuMailError != null) {
            et_value_customer_email.error = getString(formState?.valuMailError)
            et_value_customer_email.shakeView()
        } //else et_value_customer_email.error = null

        if (formState?.valuLimitError != null) {
            et_value_customer_name.error = getString(formState?.valuLimitError)
            et_value_customer_name.shakeView()
        } //else et_value_customer_name.error = null


    }

    fun showLoadingView(show: Boolean) {
        showLoadingView(progress_bar, show)
    }

    override fun onBackPressed() {
        if (financialViewModel.checkUpdated(
                et_job.text.toString().trim(),
                et_income.text.toString().trim(),
                et_company_name.text.toString().trim(),
                et_company_address.text.toString().trim(),
                et_pension_amount.text.toString().trim(),
                et_property_rental_amount.text.toString().trim(),
                et_business_owner_amount.text.toString().trim(),
                et_company_name_business_owner.text.toString().trim(),
                et_company_address_business_owner.text.toString().trim(),
                et_bank_certificates_amount.text.toString().trim(),
                et_value_customer_name.text.toString().trim(),
                et_value_customer_phone.text.toString().trim(),
                et_value_customer_email.text.toString().trim()
            )
        )
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::goBackClicked,
                ::noClicked
            )
        else
            super.onBackPressed()

    }

    fun goBackClicked() {
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv_toolbar_title.text = getString(R.string.prove_of_income)
        iv_toolbar_back.setOnClickListener { onBackPressed() }

        financialViewModel.profileId = intent.getStringExtra(Constants.FINANCIAL_PROFILE_ID)
            ?: "08d9caa8-9b3c-450f-891b-5f791ae85db1"
        financialViewModel.step2NONE = intent.getBooleanExtra(Constants.KEY_STEP_2_NONE, false)
        imageSelection = ImageSelection(this, supportFragmentManager)

        tv_toolbar_title.text = getString(R.string.financial_info)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        initLists()
        initCloseButtons()

        financialViewModel.getFinancialPreStepThree()

        btn_next.setOnClickListener {
            saveData()
        }
        //et_pension_amount.filters= arrayOf(Constants.filter)
    }

    private fun initCloseButtons() {
        iv_delete_bank_certificates.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deleteBankCertificateData,
                ::noClicked
            )
        }
        iv_delete_employee.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deleteEmployeeData,
                ::noClicked
            )
        }
        iv_delete_business_owner.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deleteBusinessOwnerData,
                ::noClicked
            )
        }

        iv_delete_pension.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deletePensionData,
                ::noClicked
            )
        }

        iv_delete_value_customer.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deleteValuCustomerData,
                ::noClicked
            )
        }

        iv_delete_property_rental.setOnClickListener {
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.confirm),
                getString(R.string.confirm_losing_data),
                R.drawable.ic_warning,
                ::deletePropertyRentalData,
                ::noClicked
            )
        }
    }

    private fun deleteValuCustomerData() {
        group_value_customer.toGone()
        et_value_customer_email.setText("")
        et_value_customer_name.setText("")
        et_value_customer_phone.setText("")
        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.VALUE_CUSTOMER.id)) {
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.VALUE_CUSTOMER.id)

            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.valu_customer))
            bindTypesSpinner()
        }
    }

    private fun deletePropertyRentalData() {
        group_property_rental.toGone()
        et_property_rental_amount.setText("")

        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.ASSETS_RENTAL.id)
        ) {
            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.property_renter))
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.ASSETS_RENTAL.id)
            bindTypesSpinner()
        }
    }

    private fun deletePensionData() {
        group_pension.toGone()
        et_pension_amount.setText("")

        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.PENSION.id)) {
            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.pension))
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.PENSION.id)
            bindTypesSpinner()
        }
    }

    private fun deleteBusinessOwnerData() {
        group_business_owner.toGone()
        et_business_owner_amount.setText("")
        et_company_name_business_owner.setText("")
        et_company_address_business_owner.setText("")

        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.BUSINESS_OWNER.id)
        ) {
            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.business_owner))
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.BUSINESS_OWNER.id)
            bindTypesSpinner()
        }
    }

    private fun deleteBankCertificateData() {
        group_bank_certificates.toGone()
        et_bank_certificates_amount.setText("")

        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.BANK_CERTIFICATES.id)
        ) {
            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.bank_certificates))
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.BANK_CERTIFICATES.id)
            bindTypesSpinner()
        }
    }

    private fun deleteEmployeeData() {
        group_employee.toGone()
        et_job.setText("")
        et_income.setText("")
        et_company_name.setText("")

        if (financialViewModel.displayedCategories.contains(Constants.INSTANCE.AttachmentType.EMPLOYMENT.id)
        ) {
            financialViewModel.addFinancialTypeToSpinnerList(App.context.getString(R.string.employee))
            financialViewModel.displayedCategories.remove(Constants.INSTANCE.AttachmentType.EMPLOYMENT.id)
            bindTypesSpinner()
        }
    }

    fun initLists() {
        financialViewModel.employeeAttachmentsTypesObj =
            financialViewModel.employeeAttachments.toMutableList()
        attachmentsAdapterEmployee = AttachmentsAdapter(
            this,
            this,
            Constants.INSTANCE.AttachmentType.EMPLOYMENT.id
        )
        attachmentsAdapterEmployee.setList(financialViewModel.employeeAttachments.toMutableList())
        rv_attachments_employee.adapter = attachmentsAdapterEmployee
        ///////
        financialViewModel.pensionAttachmentsTypesObj =
            financialViewModel.pensionAttachments.toMutableList()
        attachmentsAdapterPension =
            AttachmentsAdapter(this, this, Constants.INSTANCE.AttachmentType.PENSION.id)
        attachmentsAdapterPension.setList(financialViewModel.pensionAttachments.toMutableList())
        rv_attachments_pension.adapter = attachmentsAdapterPension

        /////
        financialViewModel.bankCertificateAttachmentsTypesObj =
            financialViewModel.bankCertificatesAttachments.toMutableList()
        attachmentsAdapterBankCertificates = AttachmentsAdapter(
            this,
            this,
            Constants.INSTANCE.AttachmentType.BANK_CERTIFICATES.id
        )
        attachmentsAdapterBankCertificates.setList(financialViewModel.bankCertificatesAttachments.toMutableList())
        rv_attachments_bank_certificates.adapter = attachmentsAdapterBankCertificates

        /////
        financialViewModel.assetsOwnerAttachmentsTypesObj =
            financialViewModel.propertyRentAttachments.toMutableList()
        attachmentsAdapterPropertyRental = AttachmentsAdapter(
            this,
            this,
            Constants.INSTANCE.AttachmentType.ASSETS_RENTAL.id
        )
        attachmentsAdapterPropertyRental.setList(financialViewModel.propertyRentAttachments.toMutableList())
        rv_attachments_property_rental.adapter = attachmentsAdapterPropertyRental

        ////
        financialViewModel.businessOwnerAttachmentsTypesObj =
            financialViewModel.businessOwnerAttachments.toMutableList()
        attachmentsAdapterBusinessOwner = AttachmentsAdapter(
            this,
            this,
            Constants.INSTANCE.AttachmentType.BUSINESS_OWNER.id
        )
        attachmentsAdapterBusinessOwner.setList(financialViewModel.businessOwnerAttachments.toMutableList())
        rv_attachments_business_owner.adapter = attachmentsAdapterBusinessOwner

    }

    private fun bindTypesSpinner() {
        if (financialViewModel.spinnerTypes[0] != getString(R.string.none)) {
            financialViewModel.spinnerTypes.add(0, getString(R.string.none))
        }

        if (financialViewModel.spinnerTypes.size <= 1) {
            group_add_anther.toGone()
            layout_types.toGone()
            tv_spinner_type.toGone()
        } else {
            val adapter = ArrayAdapter(
                this,
                R.layout.item_spinner, financialViewModel.spinnerTypes
            )

            spinner_types.adapter = adapter
            spinner_types.onItemSelectedListener = this

            spinner_types_another.adapter = adapter
            spinner_types_another.onItemSelectedListener = this

            if (financialViewModel.displayedCategories.size == 0) {
                showTopList()
            } else
                hideTopListShowBottomOne()
        }
    }


    private fun showEmployeeData(data: FinancialProfileEmployeesCategory) {

        et_job.setText(data.job)
        et_income.setText(data.salary.toString())
        et_company_name.setText(data.companyName)
        et_company_address.setText(data.companyAddress)
    }


    fun saveData() {
        financialViewModel.saveStep3Clicked(
            et_job.text.toString().trim(),
            et_income.text.toString().trim(),
            et_company_name.text.toString().trim(),
            et_company_address.text.toString().trim(),
            et_pension_amount.text.toString().trim(),
            et_property_rental_amount.text.toString().trim(),
            et_business_owner_amount.text.toString().trim(),
            et_company_name_business_owner.text.toString().trim(),
            et_company_address_business_owner.text.toString().trim(),
            et_bank_certificates_amount.text.toString().trim(),
            et_value_customer_name.text.toString().trim(),
            et_value_customer_phone.text.toString().trim(),
            et_value_customer_email.text.toString().trim()
        )
    }


    override fun cancelUpload() {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_types -> {
                handleTypeSelected(position)
            }

            R.id.spinner_types_another -> {
                handleTypeSelected(position)
            }
        }
    }


    fun handleTypeSelected(position: Int) {
        if (position == 0)
            btn_next.toVisible()
        else {
            var type =
                financialViewModel.handleSelectedType(financialViewModel.spinnerTypes[position])
            bindTypesSpinner()
            when (type.id) {
                1 -> {
                    group_employee.toVisible()
                }
                2 -> {
                    group_pension.toVisible()
                }
                3 -> {
                    group_property_rental.toVisible()
                }
                4 -> {
                    group_business_owner.toVisible()
                }
                6 -> {
                    group_bank_certificates.toVisible()
                }
                9 -> {
                    group_value_customer.toVisible()
                }
            }
            scrollToLayout(type.id)
        }
    }

    fun scrollToLayout(typeId: Int) {
        Handler().postDelayed({

            var scrollTo = 0
            when (typeId) {
                1 -> {
                    scrollTo = getEmployeeYPosition()
                }
                2 -> {
                    scrollTo = getPensionYPosition()
                }
                3 -> {
                    scrollTo = getPropertyRentalYPosition()

                }
                4 -> {
                    scrollTo = getBusinessOwnerYPosition()

                }
                6 -> {
                    scrollTo = getBankCertificateYPosition()

                }
                9 -> {
                    scrollTo = getValuCustomerYPosition()

                }
            }

            scroll_layout.scrollTo(
                0,
                scrollTo
            )

        }, 100)

    }

    private fun getValuCustomerYPosition(): Int {
        return tv_value_customer_header.y.toInt()
    }

    private fun getBankCertificateYPosition(): Int {
        return tv_bank_certificates_header.y.toInt()

    }

    private fun getBusinessOwnerYPosition(): Int {
        return tv_business_owner_header.y.toInt()
    }

    private fun getPropertyRentalYPosition(): Int {
        return tv_property_rental_header.y.toInt()
    }

    private fun getPensionYPosition(): Int {
        return tv_pension_header.y.toInt()
    }

    private fun getEmployeeYPosition(): Int {
        return tv_employee_header.y.toInt()
    }

    fun scrollTo(yPosition: Int) {
        scroll_layout.scrollTo(
            0,
            yPosition
        )
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun savedSuccessfully(actionResponse: ActionResponse) {
        startActivity<FinancialProfileCompletedActivity>()
    }

    fun noClicked() {}

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode === Activity.RESULT_OK) {
            imageSelection.onActivityResult(requestCode, resultCode, data, this)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun browseClicked(
        type: FinancialTypeAttachments,
        position: Int,
        allowedNumToUpload: Int, categoryId: Int
    ) {
        financialViewModel.selectedCategoryId = categoryId.toString()
        imageSelection.maxImageNum = allowedNumToUpload
        imageSelection.position = position
        imageSelection.askPermission()
    }


    override fun browseClicked() {}

    override fun handleAttachedImage(
        bitmap: Bitmap,
        file: File,
        typeId: String
    ) {  //typeID is 1-< image, 2-> pdf
        financialViewModel.addAttachment(
            bitmap,
            imageSelection.position,
            file.absolutePath, 1, (imageSelection.position + 1)

        )
    }

    override fun handleAttachedImage(bitmap: Bitmap, file: File) {
    }

    override fun handleAttachedPDF(file: File) {
        Log.i("file", file.absolutePath)

        var bitmap = ContextCompat.getDrawable(this, R.drawable.ic_pdf)!!.toBitmap(100, 100, null)
        financialViewModel.addAttachment(
            bitmap,
            imageSelection.position, file.absolutePath, 2, (imageSelection.position + 1)
        )
    }


    override fun deleteImage(position: Int, typePosition: Int, categoryId: Int) {
        financialViewModel.positionToDelete = position
        financialViewModel.typePositionToDelete = typePosition
        financialViewModel.typeCategoryToDelete = categoryId
        showMessage(
            getString(R.string.confirm_delete_file),
            ::yesDelete, ::dismiss
        )
    }

    fun dismiss() {

    }

    fun yesDelete() {
        financialViewModel.onDeleteClickListener()
    }

    override fun selectImage(position: Int, attachment: Attachment) {
        if (attachment.type == 2 && attachment.id == "0")
            showMessage(getString(R.string.uploading_wait))
        else
            startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to attachment))
    }

    override fun imagesMoreThan3() {
        showMessage(
            String.format(
                financialViewModel.getLocale(),
                getString(R.string.more_images),
                Constants.IMAGES_LIMIT
            )
        )
    }

    override fun bigFile() {
        Toast.makeText(this, getString(R.string.big_file_message), Toast.LENGTH_LONG).show()
    }


}

