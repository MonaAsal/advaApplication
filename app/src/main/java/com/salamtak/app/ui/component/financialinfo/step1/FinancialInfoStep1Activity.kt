package com.salamtak.app.ui.component.financialinfo.step1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.IdObject
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.LookupsViewModel
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.financialinfo.DateTimePickerDialog
import com.salamtak.app.ui.component.financialinfo.ImageActivity
import com.salamtak.app.ui.component.financialinfo.ImagesListener
import com.salamtak.app.ui.component.financialinfo.step2.FinancialInfoStep2Activity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_financial_info_step1.*
import kotlinx.android.synthetic.main.layout_steps.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import java.io.File

@AndroidEntryPoint
class FinancialInfoStep1Activity : BaseActivity(), AttachCallBacks,
    AdapterView.OnItemSelectedListener,
    ImagesListener {
    override val layoutId: Int
        get() = R.layout.activity_financial_info_step1

    private val financialViewModelN: FinancialViewModelStep1 by viewModels()

    private val lookupsViewModel: LookupsViewModel by viewModels()

    lateinit var imageSelection: ImageSelection


    var idFaceUpdated = false
    var idBackUpdated = false
    var idFaceUpdatedGuarantor = false
    var idBackUpdatedGuarantor = false

    var isGuaranatorExpiry = false
    override fun initializeViewModel() {
    }

    @SuppressLint("SetTextI18n")
    fun forDeveloper() {
        et_first_name.setText("firstName")
        et_second_name.setText("middleName")
        et_third_name.setText("lastName")
        et_last_name.setText("familyName")

        et_mobile.setText("01225839198")

        et_id.setText("01234567891234")
        tv_expiry_date.setText("02/11/1987")

        et_building.setText("building")
        et_street.setText("streetAddress")

        /////
        et_first_name_guarantor.setText("firstName")
        et_second_name_guarantor.setText("middleName")
        et_third_name_guarantor.setText("lastName")
        et_last_name_guarantor.setText("familyName")

        et_mobile_guarantor.setText("01225839198")

        et_id_guarantor.setText("01234567891234")
        tv_expiry_date_guarantor.setText("02/11/1987")

//        et_limit.setText("1000")
    }

    override fun observeViewModel() {
        with(financialViewModelN)
        {
            observe(uploadResponse, ::showUploadResponse)
            observe(preFinancialProfileResponse, ::showData)
            observe(lookupsViewModel.citiesLiveData, ::handleCitiesResponse)
            observe(basicFormState, ::handleFromState)
            observe(hireDate, ::bindHireDate)
            observe(createFinancialProfileResponse, ::handleProfileSaved)
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(financialProgressResponse, ::showProgress)
        }
    }

    private fun showProgress(baseResponse: BaseResponse) {
        try {
            handleSteps(baseResponse.message!!.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showUploadResponse(uploadImagesResponse: UploadImagesResponse) {
        //var message = "Image ${uploadImagesResponse.savedFilesName!![0]!!} uploaded successfuly"
        enableImagesButtons()
        btn_finish.isEnabled = true

        showMessageNoIcon("", getString(R.string.image_uploaded_successfully))
        if (financialViewModelN.isFace)
            idFaceUpdated = true
        else
            idBackUpdated = true
    }

    private fun bindHireDate(date: String) {
//        if (isFutureDate(date!!).not()) {
        if (isGuaranatorExpiry) {
            tv_expiry_date_guarantor.text = date
            et_expiry_date_guarantor.error = null
        } else {
            tv_expiry_date.setText(date)
            et_expiry_date.error = null
        }
//    } else
//    {
//        if (isGuaranatorExpiry) {
//            tv_expiry_date_guarantor.text = ""
//            et_expiry_date_guarantor.error = getString(R.string.invalid_date)
//        } else {
//            tv_expiry_date.text = ""
//            et_expiry_date.error = getString(R.string.invalid_date)
//        }
//            showMessage(getString(R.string.invalid_date))
//        }
    }

    private fun handleProfileSaved(idObject: IdObject) {
//        startActivity(intentFor<FinancialInfoTypesActivity>(Constants.FINANCIAL_PROFILE_ID to idObject.id))
        startActivity(intentFor<FinancialInfoStep2Activity>(Constants.FINANCIAL_PROFILE_ID to idObject.id))
    }


    private fun handleFromState(basicFormState: Step1FormState) {
        if (basicFormState?.paymentError != null) {
            main_layout.scrollTo(
                spinner_payment_methods.x.toInt(),
                spinner_payment_methods.y.toInt()
            )
            et_payment_method.error = getString(basicFormState.paymentError)
            spinner_payment_methods.shakeView()
        }

        if (basicFormState?.phoneError != null) {
            et_mobile.error = getString(basicFormState.phoneError)
            et_mobile.requestFocus()
            et_mobile.shakeView()
            main_layout.scrollTo(et_mobile.x.toInt(), et_mobile.y.toInt())
        }
        if (basicFormState?.firstNameError != null) {
            et_first_name.error = getString(basicFormState.firstNameError)
            et_first_name.requestFocus()
            et_first_name.shakeView()
            main_layout.scrollTo(et_first_name.x.toInt(), et_first_name.y.toInt())
        }
        if (basicFormState.secondNameError != null) {
            et_second_name.error = getString(basicFormState.secondNameError)
            et_second_name.requestFocus()
            et_second_name.shakeView()
            main_layout.scrollTo(et_first_name.x.toInt(), et_first_name.y.toInt())
        }
        if (basicFormState.thirdNameError != null) {
            et_third_name.error = getString(basicFormState.thirdNameError)
            et_third_name.shakeView()
            main_layout.scrollTo(et_first_name.x.toInt(), et_first_name.y.toInt())
        }
        if (basicFormState.lastNameError != null) {
            et_last_name.error = getString(basicFormState.lastNameError)
            et_last_name.requestFocus()
            et_last_name.shakeView()
            main_layout.scrollTo(et_first_name.x.toInt(), et_first_name.y.toInt())
        }
        if (basicFormState.buildingNumError != null) {
            et_building.error = getString(basicFormState.buildingNumError)
            et_building.requestFocus()
            et_building.shakeView()
            main_layout.scrollTo(0, et_building.y.toInt())
        }
        if (basicFormState.nationalIdError != null) {
            et_id.error = getString(basicFormState.nationalIdError)
            et_id.requestFocus()
            et_id.shakeView()
            main_layout.scrollTo(0, et_id.y.toInt())
        }
        if (basicFormState.nationalIdExpireDateError != null) {
            et_expiry_date.error = getString(basicFormState.nationalIdExpireDateError)
            et_expiry_date.requestFocus()
            et_expiry_date.shakeView()
            main_layout.scrollTo(0, et_expiry_date.y.toInt())
        }
//        if (basicFormState.passportNumberError != null) {
//            et_id.error = getString(basicFormState.passportNumberError)
//            et_id.requestFocus()
//            et_id.shakeView()
//            main_layout.scrollTo(0, et_id.y.toInt())
//        }
        if (basicFormState.streetAddressError != null) {
            et_street.error = getString(basicFormState.streetAddressError)
            et_street.requestFocus()
            et_street.shakeView()
            main_layout.scrollTo(0, et_street.y.toInt())
        }

        ////////////////////////////////
        if (basicFormState?.firstNameErrorGuarantor != null) {
            et_first_name_guarantor.error = getString(basicFormState.firstNameErrorGuarantor)
            et_first_name_guarantor.requestFocus()
            et_first_name_guarantor.shakeView()
//            main_layout.scrollTo(
//                et_first_name_guarantor.x.toInt(),
//                et_first_name_guarantor.y.toInt()
//            )
        }
        if (basicFormState.secondNameErrorGuarantor != null) {
            et_second_name_guarantor.error = getString(basicFormState.secondNameErrorGuarantor)
            et_second_name_guarantor.requestFocus()
            et_second_name_guarantor.shakeView()
//            main_layout.scrollTo(
//                et_second_name_guarantor.x.toInt(),
//                et_second_name_guarantor.y.toInt()
//            )
        }
        if (basicFormState.thirdNameErrorGuarantor != null) {
            et_third_name_guarantor.error = getString(basicFormState.thirdNameErrorGuarantor)
            et_third_name_guarantor.requestFocus()
            et_third_name_guarantor.shakeView()
//            main_layout.scrollTo(
//                et_third_name_guarantor.x.toInt(),
//                et_third_name_guarantor.y.toInt()
//            )
        }
        if (basicFormState.lastNameErrorGuarantor != null) {
            et_last_name_guarantor.error = getString(basicFormState.lastNameErrorGuarantor)
            et_last_name_guarantor.requestFocus()
            et_last_name_guarantor.shakeView()
//            main_layout.scrollTo(
//                et_last_name_guarantor.x.toInt(),
//                et_last_name_guarantor.y.toInt()
//            )
        }

        if (basicFormState.nationalIdErrorGuarantor != null) {
            et_id_guarantor.error = getString(basicFormState.nationalIdErrorGuarantor)
            et_id_guarantor.requestFocus()
            et_id_guarantor.shakeView()
//            main_layout.scrollTo(0, et_id_guarantor.y.toInt())
        }
        if (basicFormState.phoneErrorGuarantor != null) {
            et_mobile_guarantor.error = getString(basicFormState.phoneErrorGuarantor)
            et_mobile_guarantor.requestFocus()
            et_mobile_guarantor.shakeView()
//            main_layout.scrollTo(0, et_mobile_guarantor.y.toInt())
        }
        if (basicFormState.nationalIdExpireDateErrorGuarantor != null) {
            et_expiry_date_guarantor.error =
                getString(basicFormState.nationalIdExpireDateErrorGuarantor)
            et_expiry_date_guarantor.requestFocus()
            et_expiry_date_guarantor.shakeView()
//            main_layout.scrollTo(0, et_expiry_date_guarantor.y.toInt())
        }

        if (basicFormState?.imagesError != null) {
            showMessage(getString(basicFormState.imagesError))
//            btn_upload_id.shakeView()
//            btn_upload_id_back.shakeView()
            main_layout.scrollTo(btn_upload_id.x.toInt(), btn_upload_id.y.toInt())
        } else if (basicFormState?.imagesErrorGuarantor != null) {
            showMessage(getString(basicFormState.imagesErrorGuarantor))
//            Log.e("y", btn_upload_id_guarantor.y.toInt().toString())
//            main_layout.scrollTo(
//                0,
//                btn_upload_id_guarantor.y.toInt()
//            )
            btn_upload_id_guarantor.shakeView()
            btn_upload_id_back_guarantor.shakeView()

        }

    }

    private fun handleCitiesResponse(resource: Resource<SalamtakListResponse<City>>) {
        when (resource) {
            is Resource.Loading -> {
            }
            is Resource.Success -> resource.data?.let {
                bindCitiesData(it.data)

                val handler = Handler()
                handler.postDelayed({
                    setSelectedArea()
                }, 1000)

            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
            }
            is Resource.DataError -> {
                progress_bar.toGone()
            }
        }
    }

    private fun setSelectedArea() {
        financialViewModelN.preFinancialProfileResponse.value?.let {
            it.financialProfile?.let {
                it.areaId?.let {
                    financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.areaId?.let {
                        var position =
                            lookupsViewModel.getAreaPosition(it.toInt())
                        Log.e("areaposition", position.toString())
                        spinner_areas.setSelection(position)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        financialViewModelN.getFinancialProgress()
        btn_upload_id.isEnabled = true
        btn_upload_id_back.isEnabled = true
        btn_upload_id_guarantor.isEnabled = true
        btn_upload_id_back_guarantor.isEnabled = true

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
        iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
        iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))

        imageSelection = ImageSelection(this, supportFragmentManager, false, 1, this)
        imageSelection.canCrop = true
//        attachmentsAdapter = AttachmentsAdapter(this, this)

        tv_toolbar_title.text = getString(R.string.financial_info)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        et_mobile.filters= (Constants.DisableDecimalWithMaxLength(11))
        et_id.filters= (Constants.DisableDecimalWithMaxLength(14))
        et_id_guarantor.filters= (Constants.DisableDecimalWithMaxLength(14))
        tv_your_mobile.text = tv_your_mobile.text.toString() + "*"
        tv_lbl_work_address.text = tv_lbl_work_address.text.toString() + "*"
        tv_id.text = tv_id.text.toString() + "*"
        tv_expiry_date_lbl.text = tv_expiry_date_lbl.text.toString() + "*"
        tv_first_name.text = tv_first_name.text.toString() + "*"
        tv_second_name.text = tv_second_name.text.toString() + "*"
        tv_third_name.text = tv_third_name.text.toString() + "*"
//        tv_last_name.text = tv_last_name.text.toString() + "*"


        tv_your_mobile_guarantor.text = tv_your_mobile_guarantor.text.toString() + "*"
        tv_id_guarantor.text = tv_id_guarantor.text.toString() + "*"
        tv_expiry_date_lbl_guarantor.text = tv_expiry_date_lbl_guarantor.text.toString() + "*"
        tv_first_name_guarantor.text = tv_first_name_guarantor.text.toString() + "*"
        tv_second_name_guarantor.text = tv_second_name_guarantor.text.toString() + "*"
        tv_third_name_guarantor.text = tv_third_name_guarantor.text.toString() + "*"
//        tv_last_name_guarantor.text = tv_last_name_guarantor.text.toString() + "*"

        tv_id_type.setOnClickListener {
            if (layout_my_info.visibility == View.VISIBLE) {
                layout_my_info.toGone()
                iv_expand_self_info.setImageResource(R.drawable.ic_plus)
            } else {
                layout_my_info.toVisible()
                iv_expand_self_info.setImageResource(R.drawable.ic_minimize)
            }
        }

        iv_expand_self_info.setOnClickListener {
            if (layout_my_info.visibility == View.VISIBLE) {
                layout_my_info.toGone()
                iv_expand_self_info.setImageResource(R.drawable.ic_plus)
            } else {
                layout_my_info.toVisible()
                iv_expand_self_info.setImageResource(R.drawable.ic_minimize)
            }
        }

        tv_id_type_guarantor.setOnClickListener {
            if (layout_my_info_guarantor.visibility == View.VISIBLE) {
                layout_my_info_guarantor.toGone()
                iv_expand_self_info_guarantor.setImageResource(R.drawable.ic_plus)
            } else {
                layout_my_info_guarantor.toVisible()
                iv_expand_self_info_guarantor.setImageResource(R.drawable.ic_minimize)
            }
        }

        iv_expand_self_info_guarantor.setOnClickListener {
            if (layout_my_info_guarantor.visibility == View.VISIBLE) {
                layout_my_info_guarantor.toGone()
                iv_expand_self_info_guarantor.setImageResource(R.drawable.ic_plus)
            } else {
                layout_my_info_guarantor.toVisible()
                iv_expand_self_info_guarantor.setImageResource(R.drawable.ic_minimize)
            }
        }

        iv_image.setOnClickListener {
            if (idFaceUpdated.not()) {
                if (financialViewModelN.preFinancialProfileResponse.value!!.financialProfile != null
                    && (financialViewModelN.idImageFrontName.isNullOrEmpty().not() ||
                            financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.idFaceUrl.isNullOrEmpty()
                                .not()
                            )
                )
                    startActivity(
                        intentFor<ImageActivity>(
                            Constants.KEY_IMAGE to true,
                            Constants.KEY_ID to financialViewModelN.preFinancialProfileResponse!!.value!!.financialProfile!!.id
                        )
                    )
            } else {
                showMessage(getString(R.string.complete_fin_profile))
            }
        }

        iv_image2.setOnClickListener {
            if (idBackUpdated.not()) {
                if (financialViewModelN.preFinancialProfileResponse.value!!.financialProfile != null
                    && (financialViewModelN.idImageBackName.isNullOrEmpty().not() ||
                            financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.idBackUrl.isNullOrEmpty()
                                .not()
                            )
                )
                    startActivity(
                        intentFor<ImageActivity>(
                            Constants.KEY_IMAGE to false,
                            Constants.KEY_ID to financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.id
                        )
                    )
            } else {
                showMessage(getString(R.string.complete_fin_profile))
            }
        }

        iv_image_guarantor.setOnClickListener {
            if (idFaceUpdatedGuarantor.not()) {
                if (financialViewModelN.preFinancialProfileResponse.value!!.financialProfile != null
                    && (financialViewModelN.idImageFrontNameGuaranator.isNullOrEmpty().not() ||
                            financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.customerFinancialGuarantor?.idFaceUrl.isNullOrEmpty()
                                .not()
                            )
                )
                    startActivity(
                        intentFor<ImageActivity>(
                            Constants.KEY_IMAGE to true,
                            Constants.KEY_GUARANTOR to true,
                            Constants.KEY_ID to financialViewModelN.preFinancialProfileResponse!!.value!!.financialProfile!!.id
                        )
                    )
            } else {
                showMessage(getString(R.string.complete_fin_profile))
            }
        }

        iv_image2_guarantor.setOnClickListener {
            if (idBackUpdatedGuarantor.not()) {
                if (financialViewModelN.preFinancialProfileResponse.value!!.financialProfile != null
                    && (financialViewModelN.idImageBackNameGuaranator.isNullOrEmpty().not() ||
                            financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.customerFinancialGuarantor?.idBackUrl.isNullOrEmpty()
                                .not()
                            )
                )
                    startActivity(
                        intentFor<ImageActivity>(
                            Constants.KEY_IMAGE to false,
                            Constants.KEY_GUARANTOR to true,
                            Constants.KEY_ID to financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.id
                        )
                    )
            } else {
                showMessage(getString(R.string.complete_fin_profile))
            }
        }

        et_mobile.setText(financialViewModelN.getUser()!!.phone)
        btn_finish.setOnClickListener {
//            startActivity<FinancialInfoStep2Activity>()
            if (lookupsViewModel.selectedCityBasic.value != null)
                financialViewModelN.onFinishClick(
                    et_mobile.text.toString(),
                    et_first_name.text.toString(),
                    et_second_name.text.toString(),
                    et_third_name.text.toString(),
                    et_last_name.text.toString(),
                    et_id.text.toString(),
                    tv_expiry_date.text.toString(),
                    et_street.text.toString(),
                    et_building.text.toString(),
                    lookupsViewModel.selectedCityBasic.value!!.id.toString(),
                    lookupsViewModel.selectedAreaBasic.value!!.id.toString(),
                    et_first_name_guarantor.text.toString(),
                    et_second_name_guarantor.text.toString(),
                    et_third_name_guarantor.text.toString(),
                    et_last_name_guarantor.text.toString(),
                    et_id_guarantor.text.toString(),
                    tv_expiry_date_guarantor.text.toString(),
                    et_mobile_guarantor.text.toString(), financialViewModelN.selectedMaritalStatusGuarantor
                )
        }

        btn_upload_id.setOnClickListener {
            btn_upload_id.isEnabled = false
            btn_upload_id_back.isEnabled = false
//            btn_finish.isEnabled = false
            idFaceUpdated = true
            financialViewModelN.isFace = true
            financialViewModelN.isBack = false
            financialViewModelN.isFaceGuarantor = false
            financialViewModelN.isBackGuarantor = false
            browseClicked()
        }

        btn_upload_id_back.setOnClickListener {
            btn_upload_id.isEnabled = false
            btn_upload_id_back.isEnabled = false
//            btn_finish.isEnabled = false

            idBackUpdated = true
            financialViewModelN.isFace = false
            financialViewModelN.isFaceGuarantor = false
            financialViewModelN.isBackGuarantor = false
            //financialViewModelN.isFace = false
            financialViewModelN.isBack = true
            browseClicked()
        }

        btn_upload_id_guarantor.setOnClickListener {
            btn_upload_id_guarantor.isEnabled = false
            btn_upload_id_back_guarantor.isEnabled = false

            idFaceUpdatedGuarantor = true
            financialViewModelN.isFaceGuarantor = true
            financialViewModelN.isFace = false
            financialViewModelN.isBack = false
            financialViewModelN.isBackGuarantor = false
            browseClicked()
        }

        btn_upload_id_back_guarantor.setOnClickListener {
            btn_upload_id_guarantor.isEnabled = false
//            btn_finish.isEnabled = false
            btn_upload_id_back_guarantor.isEnabled = false
            idBackUpdatedGuarantor = true
            financialViewModelN.isBackGuarantor = true
            financialViewModelN.isFace = false
            financialViewModelN.isBack = false
            financialViewModelN.isFaceGuarantor = false
            browseClicked()
        }

        tv_expiry_date.setOnClickListener {
//            if (::financialViewModelN.isInitialized) {
            isGuaranatorExpiry = false
            val fm = supportFragmentManager
            val dialog =
                DateTimePickerDialog.newInstance(financialViewModelN)
            dialog.show(fm, "")
//            }
        }


        tv_expiry_date_guarantor.setOnClickListener {
            //if (::financialViewModelN.isInitialized) {
            isGuaranatorExpiry = true
            val fm = supportFragmentManager
            val dialog =
                DateTimePickerDialog.newInstance(financialViewModelN)
            dialog.show(fm, "")
//            }
        }

        financialViewModelN.getfinancialPreStepOne()
        lookupsViewModel.getCities()
//        forDeveloper()
    }


    private fun showData(preFinancialProfile: PreStep1Data) {
//        lookupsViewModel.getProfileLookups()
        bindPaymentMethods(preFinancialProfile.financialProviders)
        bindMaritalStatuses(preFinancialProfile.martialStatues)
//        bindLoanTypes(resources.getStringArray(R.array.loan_types).toList())

        preFinancialProfile.financialProfile?.let {
            handleSteps(preFinancialProfile.financialProfile!!.progress)
            bindProfileData(preFinancialProfile)//preFinancialProfile.financialProfile,preFinancialProfile.martialStatues)
        }
    }

    private fun handleSteps(progress: Int) {
        Log.e("steps", progress.toString())
        when (progress) {
            3 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
            }
            2 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
            }
            1 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
            }
        }
    }

//    private fun bindLoanTypes(types: List<String>) {
//        financialViewModelN.loanTypes = types.toMutableList()
//
//        val adapter = ArrayAdapter(
//            this,
//            R.layout.item_spinner, financialViewModelN.loanTypes
//        )
//
//        spinner_depts.adapter = adapter
//        spinner_depts.onItemSelectedListener = this
//
//        spinner_depts.setSelection(2)
//    }

    private fun bindMaritalStatuses(martialStatues: List<MaritalStatus>) {
        financialViewModelN.maritalStatuses = martialStatues.map { it.name }.toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, financialViewModelN.maritalStatuses
        )

        spinner_marital_status.adapter = adapter
        spinner_marital_status.onItemSelectedListener = this

        spinner_marital_status_guarantor.adapter = adapter
        spinner_marital_status_guarantor.onItemSelectedListener = this
    }

    private fun bindPaymentMethods(financialProviders: List<FinancialProvider>?) {
        financialProviders?.let {
            financialViewModelN.financialProviders =
                financialProviders.map { it.name }.toMutableList()

            financialViewModelN.financialProviders.add(0, getString(R.string.choose))
            val adapter = ArrayAdapter(
                this,
                R.layout.item_spinner, financialViewModelN.financialProviders
            )

            spinner_payment_methods.adapter = adapter
            spinner_payment_methods.onItemSelectedListener = this
        }
    }


    private fun bindCitiesData(data: List<City>) {
        progress_bar.toGone()
        lookupsViewModel.citiesNamesList = (data.map { it.name }).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, lookupsViewModel.citiesNamesList
        )

        spinner_cities.adapter = adapter
        spinner_cities.onItemSelectedListener = this
        financialViewModelN.preFinancialProfileResponse.value?.let {
            it.financialProfile?.let {
                financialViewModelN.preFinancialProfileResponse.value!!.financialProfile!!.cityId?.let {
                    spinner_cities.setSelection(lookupsViewModel.getCityPosition(it.toInt()))
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_cities -> {
                lookupsViewModel.selectCityAt(position, false)
                bindAreasData(lookupsViewModel.selectedCityBasic.value!!.areas!!)
            }

            R.id.spinner_marital_status -> {
                financialViewModelN.selectMaritalStatus(
                    position
                )
            }

            R.id.spinner_marital_status_guarantor -> {
                financialViewModelN.selectMaritalStatusGuarantor(
                    position
                )
            }

            R.id.spinner_areas -> {
                lookupsViewModel.selectAreaAt(position, false)
            }

//            R.id.spinner_depts -> {
//                financialViewModelN.selectLoanType(position)
//
//                et_limit.isEnabled = position != 2
//                if (position == 2)
//                    et_limit.setText("")
//
////                if (position == 2) {
////                    et_limit.toGone()
////                    tv_dept_limit.toGone()
////                } else {
////                    et_limit.toVisible()
////                    tv_dept_limit.toVisible()
////                }
//            }

            R.id.spinner_payment_methods -> {
                if (position != 0)
                    financialViewModelN.selectPaymentMethod(position)
                else financialViewModelN.selectedFinancialProvider = null
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    fun bindProfileData(preStep1Data: PreStep1Data) {
        val data = preStep1Data.financialProfile
        if (data != null) {
            et_first_name.setText(data.firstName ?: "")
            et_second_name.setText(data.middleName ?: "")
            et_third_name.setText(data.familyName ?: "")
            et_last_name.setText(data.lastName ?: "")
            et_mobile.setText(data.mobile ?: "")
            et_id.setText(data.idNumber ?: "")
            tv_expiry_date.text = (data.nationalIdExpireDate ?: "")

            et_building.setText(data.building ?: "")
            et_street.setText(data.streetAddress ?: "")

            data.idFaceUrl?.let {
                if (data.idFaceUrl.isNotEmpty()) {
                    iv_image.loadRoundedImage(data.idFaceUrl)
                    iv_image.toVisible()
                    iv_image2.toVisible()
                }
            }

            data.idBackUrl?.let {
                if (data.idBackUrl.isNotEmpty()) {
                    iv_image2.loadRoundedImage(data.idBackUrl)
                    iv_image.toVisible()
                    iv_image2.toVisible()
                }
            }

            val paymentIndx = 1 +
                    (preStep1Data.financialProviders.indexOfFirst { preStep1Data.financialProfile!!.financialProviderId == it.id })
            spinner_payment_methods.setSelection(paymentIndx)
//            when (preFinancialProfile.financialProfile!!.paymentCard) {
//                1 -> spinner_depts.setSelection(0)
//                2 -> spinner_depts.setSelection(1)
//                4 -> spinner_depts.setSelection(2)
//            }

            val maritalstatusIndx =
                (preStep1Data.martialStatues.indexOfFirst { preStep1Data.financialProfile!!.maritalStatusId?.toInt() == it.id })
            if (maritalstatusIndx > 0)
                spinner_marital_status.setSelection(maritalstatusIndx)

            data.customerFinancialGuarantor?.let {
                val guarantor = it
                et_first_name_guarantor.setText(guarantor.firstName ?: "")
                et_second_name_guarantor.setText(guarantor.middleName ?: "")
                et_third_name_guarantor.setText(guarantor.familyName ?: "")
                et_last_name_guarantor.setText(guarantor.lastName ?: "")
                et_id_guarantor.setText(guarantor.idNumber ?: "")
                tv_expiry_date_guarantor.text = (guarantor.nationalIdExpireDate ?: "")
                et_mobile_guarantor.setText(guarantor.mobile ?: "")
                guarantor.idFaceUrl?.let {
                    if (it.isNotEmpty()) {
                        iv_image_guarantor.loadRoundedImage(it)
                        iv_image_guarantor.toVisible()
                        iv_image2_guarantor.toVisible()
                    }
                }

                guarantor.idBackUrl?.let {
                    if (it.isNotEmpty()) {
                        iv_image2_guarantor.loadRoundedImage(it)
                        iv_image_guarantor.toVisible()
                        iv_image2_guarantor.toVisible()
                    }
                }

                val maritalstatusIndx =
                    (preStep1Data.martialStatues.indexOfFirst { data.customerFinancialGuarantor.maritalStatusId?.toInt() == it.id })
                if (maritalstatusIndx > 0)
                    spinner_marital_status_guarantor.setSelection(maritalstatusIndx)

            }
        }
    }


    private fun bindAreasData(data: List<Area>) {
        lookupsViewModel.areasNamesList = (data.map { it.name }).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, lookupsViewModel.areasNamesList
        )

        spinner_areas.adapter = adapter
        spinner_areas.onItemSelectedListener = this

    }

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
        allowedNumToUpload: Int,
        categoryId: Int
    ) {

    }

    override fun browseClicked() {
        imageSelection.position = 0
        imageSelection.askPermission()
    }

    override fun handleAttachedImage(bitmap: Bitmap, file: File) {
        // save bitmap in the corresponding list
        // show using callback the bitmap
        if (financialViewModelN.isFace) {
            iv_image.toVisible()
            iv_image2.toVisible()
            iv_image.loadRoundedImage(bitmap)
        } else if (financialViewModelN.isBack) {
            iv_image.toVisible()
            iv_image2.toVisible()
            iv_image2.loadRoundedImage(bitmap)
        }

        if (financialViewModelN.isFaceGuarantor) {
            iv_image_guarantor.toVisible()
            iv_image2_guarantor.toVisible()
            iv_image_guarantor.loadRoundedImage(bitmap)
        } else if (financialViewModelN.isBackGuarantor) {
            iv_image_guarantor.toVisible()
            iv_image2_guarantor.toVisible()
            iv_image2_guarantor.loadRoundedImage(bitmap)
        }

        financialViewModelN.addImage(imageSelection.position, file.absolutePath)
//        attachmentsAdapter.setList(financialViewModel.attachments.toMutableList())
//        attachmentsAdapter.updateImages(imageSelection.position)

    }

    override fun handleAttachedImage(bitmap: Bitmap, file: File, typeId: String) {

        var size = file.length() / 1024 /// 1024
        Log.e("file size after", size.toString() + "MB")
        handleAttachedImage(bitmap, file)
    }

    override fun handleAttachedPDF(file: File) {
    }

    override fun imagesMoreThan3() {
        enableImagesButtons()
        showMessage(
            String.format(
                financialViewModelN.getLocale(),
                getString(R.string.more_images),
                Constants.IMAGES_LIMIT
            )
        )
    }

    private fun enableImagesButtons() {
        btn_upload_id_guarantor.isEnabled = true
        btn_upload_id_back_guarantor.isEnabled = true
        btn_upload_id.isEnabled = true
        btn_upload_id_back.isEnabled = true
    }

    override fun bigFile() {
        enableImagesButtons()
        showMessage(getString(R.string.big_file_message))
    }

    override fun cancelUpload() {
        Log.e("radwa", "cancelUpload")
        enableImagesButtons()
        btn_finish.isEnabled = true

        if (financialViewModelN.isFace)
            idFaceUpdated = false
        else
            idBackUpdated = false


    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    override fun deleteImage(position: Int, typePosition: Int, categoryInt: Int) {

    }

    override fun selectImage(position: Int, attachment: Attachment) {
        //   startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to attachment.thumbnailUrl))
    }

}