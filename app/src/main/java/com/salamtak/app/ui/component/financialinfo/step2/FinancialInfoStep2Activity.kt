package com.salamtak.app.ui.component.financialinfo.step2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinancialTypeAttachments
import com.salamtak.app.data.entities.IdNameObject
import com.salamtak.app.data.entities.PreStep2Data
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.AttachmentData
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.financialinfo.ImageActivity
import com.salamtak.app.ui.component.financialinfo.step3.FinancialInfoStep3Activity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_financial_info_step2.*
import kotlinx.android.synthetic.main.layout_steps.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import java.io.File

@AndroidEntryPoint
class FinancialInfoStep2Activity : BaseActivity(),
    AttachCallBacks,
    AdapterView.OnItemSelectedListener {
    override val layoutId: Int
        get() = R.layout.activity_financial_info_step2

    val financialViewModel: FinancialViewModelStep2 by viewModels()
    lateinit var imageSelection: ImageSelection

    override fun initializeViewModel() {
    }

    override fun observeViewModel() {
        with(financialViewModel) {
            observe(showLoading, ::showLoadingView)
            observe(saveTypeDataResponseStep2, ::savedSuccessfully)
            observe(preFinancialProfileResponseStep2, ::showData)
            observe(showServerError, ::showServerErrorMessage)
            observe(uploadAttachmentResponse, ::handleUploadResponse)
            observe(financialProgressResponse, ::showProgress)

            observe(step2FormState, ::handleFromState)
        }
    }

    private fun showProgress(baseResponse: BaseResponse) {
        try {
            handleSteps(baseResponse.message!!.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleSteps(progress: Int) {
        Log.e("steps", progress.toString())
        when (progress) {
            3 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
            }
            2 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
            }
            1 -> {
                iv_step1.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_light))
                iv_step2.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_dark))
                iv_step3.background = (ContextCompat.getDrawable(this, R.drawable.ic_step_white))
            }
        }
    }


    private fun handleFromState(basicFormState: Step2FormState) {
        if (basicFormState?.clubImage1Error != null) {
            showMessage(getString(basicFormState.clubImage1Error))
            btn_upload_club_front.shakeView()
//            btn_upload_club_front.requestFocus()
            main_layout.scrollTo(
                btn_upload_club_front.x.toInt(),
                btn_upload_club_front.y.toInt()
            )
        } else if (basicFormState?.clubImage2Error != null) {
            showMessage(getString(basicFormState.clubImage2Error))
            btn_upload_club_back.shakeView()
//            btn_upload_club_back.requestFocus()
            main_layout.scrollTo(btn_upload_club_back.x.toInt(), btn_upload_club_back.y.toInt())
        }

        if (basicFormState?.carImage1Error != null) {
            showMessage(getString(basicFormState.carImage1Error))
            btn_upload_car_front.shakeView()
            main_layout.scrollTo(btn_upload_car_front.x.toInt(), btn_upload_car_front.y.toInt())
        } else if (basicFormState?.carImage2Error != null) {
            showMessage(getString(basicFormState.carImage2Error))
            btn_upload_car_back.shakeView()
            main_layout.scrollTo(btn_upload_car_back.x.toInt(), btn_upload_car_back.y.toInt())
        }

        if (basicFormState?.carError != null) {
//            spinner_cars.error = getString(basicFormState.phoneError)
            spinner_cars.shakeView()
            main_layout.scrollTo(spinner_cars.x.toInt(), spinner_cars.y.toInt())
        }
        if (basicFormState?.clubError != null) {
//            spi.error = getString(basicFormState.firstNameError)
            spinner_clubs.shakeView()
            main_layout.scrollTo(spinner_clubs.x.toInt(), spinner_clubs.y.toInt())
        }
        if (basicFormState.limitError != null) {
            et_limit.error = getString(basicFormState.limitError)
            et_limit.shakeView()
            et_limit.requestFocus()
//            main_layout.scrollTo(et_limit.x.toInt(), et_limit.y.toInt())
        }
    }

    private fun handleUploadResponse(attachmentData: AttachmentData) {
        Log.e("uploadImage", "handleUploadResponse")
        showMessageNoIcon("", getString(R.string.image_uploaded_successfully))

        btn_upload_club_front.isEnabled = true
        btn_upload_club_back.isEnabled = true
        btn_upload_car_front.isEnabled = true
        btn_upload_car_back.isEnabled = true
        btn_next.isEnabled = true
        financialViewModel.currentUploadingImage = 0
    }


    private fun savedSuccessfully(actionResponse: ActionResponse?) {
        var step2None = false
        if (switch_club_member.isChecked.not() && switch_car_owner.isChecked.not() && switch_credit_loans.isChecked.not())
            step2None = true

        startActivity(
            intentFor<FinancialInfoStep3Activity>(
                Constants.FINANCIAL_PROFILE_ID to financialViewModel.profileId,
                Constants.KEY_STEP_2_NONE to step2None
            )
        )
    }

    private fun showData(preStep2Data: PreStep2Data) {
//        handleSteps(preStep2Data.stepTwo.progress)

        bindCars(preStep2Data.cars.toMutableList())
        bindClubs(preStep2Data.clubs.toMutableList())

        if (preStep2Data.attachments.carOwner[1].attachments.isNullOrEmpty().not()) {
            iv_image_car2.loadRoundedImage(
                preStep2Data.attachments.carOwner[1].attachments?.get(
                    0
                )?.thumbnailUrl
            )
            iv_image_car1.toVisible()
            iv_image_car2.toVisible()
        }
        if (preStep2Data.attachments.carOwner[0].attachments.isNullOrEmpty().not()) {
            iv_image_car1.loadRoundedImage(
                preStep2Data.attachments.carOwner[0].attachments?.get(
                    0
                )?.thumbnailUrl
            )
            iv_image_car1.toVisible()
            iv_image_car2.toVisible()
        }
        if (preStep2Data.attachments.clubMembership[1].attachments.isNullOrEmpty().not()) {
            iv_image_club2.loadRoundedImage(
                preStep2Data.attachments.clubMembership[1].attachments?.get(
                    0
                )?.thumbnailUrl
            )
            iv_image_club2.toVisible()
            iv_image_club1.toVisible()
        }
        if (preStep2Data.attachments.clubMembership[0].attachments.isNullOrEmpty().not()) {
            iv_image_club1.loadRoundedImage(
                preStep2Data.attachments.clubMembership[0].attachments?.get(
                    0
                )?.thumbnailUrl
            )
            iv_image_club2.toVisible()
            iv_image_club1.toVisible()
        }

        preStep2Data.stepTwo?.let {
            if(preStep2Data.stepTwo.limit.equals("0")){
                et_limit.setText("")
            }else{
                et_limit.setText(preStep2Data.stepTwo.limit)
            }
            switch_car_owner.isChecked = preStep2Data.stepTwo.carOwnerEnabled
            switch_club_member.isChecked = preStep2Data.stepTwo.clubMembershipEnabled
            switch_credit_loans.isChecked = preStep2Data.stepTwo.creditAndLoanEnabled

            var index = 1 +
                    (preStep2Data.clubs.indexOfFirst { preStep2Data.stepTwo!!.clubId == it.id })
            spinner_clubs.setSelection(index)

            index = 1 +
                    (preStep2Data.cars.indexOfFirst { preStep2Data.stepTwo!!.carId == it.id })
            spinner_cars.setSelection(index)

            index = if (preStep2Data.stepTwo!!.type == "1")
                0
            else
                1
            //index =1+(financialViewModel.loanTypes.indexOfFirst { preStep2Data.stepTwo!!.type == it })
            spinner_loan_type.setSelection(index)
        }
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

//    override fun onBackPressed() {
//        openSalamtakDialog(
//            supportFragmentManager,
//            getString(R.string.confirm),
//            getString(R.string.confirm_losing_data),
//            R.drawable.ic_warning,
//            ::yesClicked,
//            ::noClicked
//        )
//    }

    fun noClicked() {}


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

    override fun handleAttachedImage(bitmap: Bitmap, file: File) {
        // save bitmap in the corresponding list
        // show using callback the bitmap
        financialViewModel.uploadStep2Images(file.absolutePath)
        when (financialViewModel.currentUploadingImage) {
            0 -> {
                iv_image_club1.toVisible()
                iv_image_club2.toVisible()
                iv_image_club1.loadRoundedImage(bitmap)
            }
            1 -> {
                iv_image_club1.toVisible()
                iv_image_club2.toVisible()
                iv_image_club2.loadRoundedImage(bitmap)
            }
            2 -> {
                iv_image_car1.toVisible()
                iv_image_car2.toVisible()
                iv_image_car1.loadRoundedImage(bitmap)
            }
            3 -> {
                iv_image_car1.toVisible()
                iv_image_car2.toVisible()
                iv_image_car2.loadRoundedImage(bitmap)
            }
        }
    }


    override fun handleAttachedImage(bitmap: Bitmap, file: File, typeId: String) {
        var size = file.length() / 1024 /// 1024
        Log.e("file size after", size.toString() + "MB")
        handleAttachedImage(bitmap, file)
    }

    override fun handleAttachedPDF(file: File) {
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
        showMessage(getString(R.string.big_file_message))
    }

    override fun cancelUpload() {
        btn_upload_car_front.isEnabled = true
        btn_upload_car_back.isEnabled = true
        btn_upload_club_front.isEnabled = true
        btn_upload_club_back.isEnabled = true
        btn_next.isEnabled = true
        financialViewModel.currentUploadingImage = 0
    }

    fun yesClicked() {
        super.onBackPressed()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_step2.setBackgroundColor(ContextCompat.getColor(this, R.color.separator))
        iv_step3.setBackgroundColor(ContextCompat.getColor(this, R.color.separator))

        tv_toolbar_title.text = getString(R.string.financial_info)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        et_limit.filters=  (Constants.DisableDecimalWithMaxLength(6))
        financialViewModel.profileId = intent.getStringExtra(Constants.FINANCIAL_PROFILE_ID)
            ?: "08d9af42-4f45-4721-8317-4dfe18a9a3de"
        imageSelection = ImageSelection(this, supportFragmentManager, false, 1, this)
        switch_club_member.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layout_club_member.toVisible()
            else
                layout_club_member.toGone()
        }

        switch_car_owner.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layout_car_owner.toVisible()
            else
                layout_car_owner.toGone()
        }

        switch_credit_loans.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layout_credit_loans.toVisible()
            else
                layout_credit_loans.toGone()
        }

        btn_next.setOnClickListener {
            if (switch_credit_loans.isChecked && et_limit.text.toString().isEmpty() &&
                spinner_loan_type.selectedItemPosition != 2
            ) {
                et_limit.error = getString(R.string.require_field)
                et_limit.shakeView()
                return@setOnClickListener
            }

            financialViewModel.postStepTwo(
                switch_club_member.isChecked,
                switch_car_owner.isChecked,
                switch_credit_loans.isChecked,
                et_limit.text.toString()
            )
//            startActivity<FinancialInfoStep3Activity>()
        }

        btn_upload_club_front.setOnClickListener {
            financialViewModel.currentUploadingImage = 0
            // browseClicked(Constants.clubMembershipAttachments[0], 0, 1)
            browseClicked()
        }
        btn_upload_club_back.setOnClickListener {
            financialViewModel.currentUploadingImage = 1
            browseClicked()
//            browseClicked(Constants.clubMembershipAttachments[1], 0, 1)
        }

        btn_upload_car_front.setOnClickListener {
            financialViewModel.currentUploadingImage = 2
            browseClicked()
//            browseClicked(Constants.carOwnerAttachments[0], 0, 1)
        }
        btn_upload_car_back.setOnClickListener {
            financialViewModel.currentUploadingImage = 3
            browseClicked()
//            browseClicked(Constants.carOwnerAttachments[0], 1, 1)
        }

        iv_image_car1.setOnClickListener {
            financialViewModel.preFinancialProfileResponseStep2.value!!.attachments.carOwner[0].attachments?.let {
                if (it.size > 0)
                    startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to it[0]))
                else
                    showMessage(getString(R.string.complete_fin_profile))
            } ?: showMessage(getString(R.string.data_not_saved))
        }
        iv_image_car2.setOnClickListener {
            financialViewModel.preFinancialProfileResponseStep2.value!!.attachments.carOwner[1].attachments?.let {
                if (it.size > 0)
                    startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to it[0]))
                else
                    showMessage(getString(R.string.complete_fin_profile))
            } ?: showMessage(getString(R.string.data_not_saved))
        }

        iv_image_club1.setOnClickListener {
            financialViewModel.preFinancialProfileResponseStep2.value!!.attachments.clubMembership[0].attachments?.let {
                if (it.size > 0)
                    startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to it[0]))
                else
                    showMessage(getString(R.string.complete_fin_profile))
            } ?: showMessage(getString(R.string.data_not_saved))
        }
        iv_image_club2.setOnClickListener {
            financialViewModel.preFinancialProfileResponseStep2.value!!.attachments.clubMembership[1].attachments?.let {
                if (it.size > 0)
                    startActivity(intentFor<ImageActivity>(Constants.KEY_IMAGE to it[0]))
                else
                    showMessage(getString(R.string.complete_fin_profile))

            } ?: showMessage(getString(R.string.data_not_saved))
        }


        financialViewModel.loanTypes =
            resources.getStringArray(R.array.loan_types).toMutableList()
        bindLoanTypes(financialViewModel.loanTypes.toList())
        financialViewModel.getFinancialPreStepTwo()
    }

    override fun onResume() {
        super.onResume()
        financialViewModel.getFinancialProgress()
    }


    private fun bindLoanTypes(types: List<String>) {
        // financialViewModelN.loanTypes = types.toMutableList()
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, types
        )

        spinner_loan_type.adapter = adapter
        spinner_loan_type.onItemSelectedListener = this

//        spinner_loan_type.setSelection(2)
    }

    private fun bindCars(cars: MutableList<IdNameObject>) {
        cars.add(0, IdNameObject("0", getString(R.string.choosecar)))
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, cars.map { it.name }
        )

        spinner_cars.adapter = adapter
        spinner_cars.onItemSelectedListener = this

//        spinner_cars.setSelection(0)
    }

    private fun bindClubs(clubs: MutableList<IdNameObject>) {
        clubs.add(0, IdNameObject("0", getString(R.string.chooseclub)))
        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, clubs.map { it.name }
        )

        spinner_clubs.adapter = adapter
        spinner_clubs.onItemSelectedListener = this

//        spinner_clubs.setSelection(2)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_loan_type -> {
//                financialViewModelN.selectLoanType(position)
                et_limit.isEnabled = position != 2

                if (position == 2) {
                    et_limit.setText("")
                    financialViewModel.selectedLoanType = 4
                }
                financialViewModel.selectLoanType(position)
            }
            R.id.spinner_cars -> {
                if (position != 0)
                    financialViewModel.selectCar(position - 1)
                else financialViewModel.selectedCar = null
            }

            R.id.spinner_clubs -> {
                if (position != 0)
                    financialViewModel.selectClub(position - 1)
                else financialViewModel.selectedClub = null
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}