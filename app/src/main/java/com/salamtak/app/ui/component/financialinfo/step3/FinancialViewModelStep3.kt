package com.salamtak.app.ui.component.financialinfo.step3

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.AttachmentData
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.financialinfo.BaseFinancialViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.util.*
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class FinancialViewModelStep3 @Inject
constructor(private val dataRepository: DataRepository) : BaseFinancialViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    lateinit var employeeAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var pensionAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var assetsOwnerAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var businessOwnerAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var bankCertificateAttachmentsTypesObj: MutableList<FinancialTypeAttachments>

    val showCompleteMessage = MutableLiveData<Boolean>()
    private val preFinancialProfileResponseStep3 = MutableLiveData<Step3Data>()
    var profileId = ""
    var step2NONE = false
    var selectedCategoryId = ""

    var positionToDelete = 0
    var typePositionToDelete = 0
    var typeCategoryToDelete = 0

    val uploadAttachmentResponse = MutableLiveData<AttachmentData>()
    val saveTypeDataResponse = MutableLiveData<ActionResponse>()
    val file = MutableLiveData<File>()
    val fileResponseBody = MutableLiveData<ResponseBody>()

    val showEmployeeData = MutableLiveData<FinancialProfileEmployeesCategory>()
    val showBusinessOwnerData = MutableLiveData<FinancialProfileBusinessOwnerCategory>()
    val showValuCustomerData = MutableLiveData<FinancialProfileValuCustomerCategory>()
    val showBankCertificateData = MutableLiveData<FinancialProfileBankCertificateCategory>()
    val showAssetsOwnerData = MutableLiveData<FinancialProfileAssetsOwnerCategory>()
    val showPensionData = MutableLiveData<FinancialProfilePensionCategory>()
    val showPensionAttachments = MutableLiveData<FinancialProfilePensionCategory>()
    val showEmployeeAttachments = MutableLiveData<FinancialProfileEmployeesCategory>()
    val showBankCertificateAttachments = MutableLiveData<FinancialProfileBankCertificateCategory>()
    val showBusinessOwnerAttachments = MutableLiveData<FinancialProfileBusinessOwnerCategory>()
    val showPropertyRentalAttachments = MutableLiveData<FinancialProfileAssetsOwnerCategory>()

    val updateEmployeeList = MutableLiveData<Boolean>()
    val updatePensionList = MutableLiveData<Boolean>()
    val updateAssetsOwnerList = MutableLiveData<Boolean>()
    val updateBusinessOwnerList = MutableLiveData<Boolean>()
    val updateBankCertificateList = MutableLiveData<Boolean>()

    var step3FormState = MutableLiveData<Step3FormState>()
    var bindTypesSpinner = MutableLiveData<Boolean>()
    val inputStep3 = MutableLiveData<Step3Data>()//Step3Data? = null
    var inputStep32: Step3Data? = null

    var displayedCategories = mutableListOf<Int>()
    var updated = false
    var valid = false

    val saveTypeDataResponseStep3 = MutableLiveData<ActionResponse>()
    val goNext = MutableLiveData<Boolean>()

    val employeeAttachments = listOf(
        FinancialTypeAttachments(
            1,
            1,
            App.context.getString(R.string.bank_statement),
            null,
            3,
            true
        ),
        FinancialTypeAttachments(1, 2, App.context.getString(R.string.utility_bill), null, 3, true),
//            FinancialTypeAttachments(1, 3, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(1, 4, "Guranator ID back", null, 3)
    )

    val pensionAttachments = listOf(
        FinancialTypeAttachments(2, 1, App.context.getString(R.string.pension1), null, 3, true),
        FinancialTypeAttachments(2, 2, App.context.getString(R.string.pension2), null, 3),
        FinancialTypeAttachments(2, 3, App.context.getString(R.string.utility_bill), null, 3, true),
//            FinancialTypeAttachments(1, 4, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(2, 5, "Guranator ID back", null, 3)
    )

    val propertyRentAttachments = listOf(
        FinancialTypeAttachments(
            3,
            1,
            App.context.getString(R.string.asset_contract),
            null,
            3,
            true
        ),
        FinancialTypeAttachments(
            3,
            2,
            App.context.getString(R.string.leasing_contract),
            null,
            3,
            true
        ),
        FinancialTypeAttachments(3, 3, App.context.getString(R.string.utility_bill), null, 3, true),
//            FinancialTypeAttachments(3, 4, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(3, 5, "Guranator ID back", null, 3)
    )


    val businessOwnerAttachments = listOf(
        FinancialTypeAttachments(
            4,
            1,
            App.context.getString(R.string.commercial_register),
            null,
            3, true
        ),
        FinancialTypeAttachments(4, 2, App.context.getString(R.string.tax_id), null, 3, true),
        FinancialTypeAttachments(4, 3, App.context.getString(R.string.tax_return), null, 3),
        FinancialTypeAttachments(4, 4, App.context.getString(R.string.bank_statement2), null, 3),
//            FinancialTypeAttachments(4, 5, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(4, 6, "Guranator ID back", null, 3)
    )


    val bankCertificatesAttachments = listOf(
        FinancialTypeAttachments(
            6,
            1,
            App.context.getString(R.string.bank_certificate),
            null,
            3,
            true
        )
    )

    val financialTypes =
        listOf(
            FinancialType(
                1,
                R.drawable.ic_employees,
                App.context.getString(R.string.employee),
                employeeAttachments
            ),
            FinancialType(
                2, R.drawable.ic_pension, App.context.getString(R.string.pension),
                pensionAttachments
            ),
            FinancialType(
                3,
                R.drawable.ic_property_rent,
                App.context.getString(R.string.property_renter), propertyRentAttachments
            ),
            FinancialType(
                4,
                R.drawable.ic_business_owner,
                App.context.getString(R.string.business_owner), businessOwnerAttachments
            ),
//            FinancialType(
//                5,
//                R.drawable.ic_club_membership,
//                App.context.getString(R.string.club_membership), Constants.clubMembershipAttachments
//            ),
            FinancialType(
                6,
                R.drawable.ic_certificate,
                App.context.getString(R.string.bank_certificates),
                bankCertificatesAttachments
            ),
            FinancialType(
                9,
                R.drawable.ic_credit_loans,
                App.context.getString(R.string.valu_customer), null
            )
//            FinancialType(
//                7,
//                R.drawable.ic_credit_loans,
//                App.context.getString(R.string.credit_loans), null
//            ),
//            FinancialType(
//                8,
//                R.drawable.ic_car,
//                App.context.getString(R.string.car_owner),
//                Constants.carOwnerAttachments
//            )
        )

    //    val types = Constants.financialTypes.map { it.name }.toMutableList()
    var spinnerTypes = financialTypes.map { it.name }.toMutableList()

    fun findFinancialType(typeName: String): FinancialType {
        return financialTypes.filter { it.name == typeName }[0]
    }

    fun removeFinancialTypeFromSpinnerList(typeName: String) {
        spinnerTypes.remove(typeName)
    }

    fun addFinancialTypeToSpinnerList(typeName: String) {
        if (spinnerTypes.contains(typeName).not())
            spinnerTypes.add(typeName)

    }


    fun onDeleteClickListener() {
        when (typeCategoryToDelete) {
            employeeAttachments[0].attachmentId ->
                deleteEmployeeAttachment()
            pensionAttachments[0].attachmentId ->
                deletePensionAttachment()
            bankCertificatesAttachments[0].attachmentId ->
                deleteBankCertificateAttachments()
            businessOwnerAttachments[0].attachmentId ->
                deleteBusinessOwnerAttachments()
            propertyRentAttachments[0].attachmentId ->
                deletePropertyRentalAttachments()
        }
    }

    private fun deletePropertyRentalAttachments() {
        var fileId =
            assetsOwnerAttachmentsTypesObj[typePositionToDelete].attachments!![positionToDelete].id
        if (fileId != "0")
            deleteAttachment(fileId)
    }


    private fun deleteBusinessOwnerAttachments() {
        if (businessOwnerAttachmentsTypesObj[typePositionToDelete].attachments!!.size > positionToDelete) {
            var fileId =
                businessOwnerAttachmentsTypesObj[typePositionToDelete].attachments!![positionToDelete].id
            if (fileId != "0")
                deleteAttachment(fileId)
        }
    }

    private fun deleteBankCertificateAttachments() {
        var fileId =
            bankCertificateAttachmentsTypesObj[typePositionToDelete].attachments!![positionToDelete].id
        if (fileId != "0")
            deleteAttachment(fileId)
    }

    private fun deletePensionAttachment() {
        var fileId =
            pensionAttachmentsTypesObj[typePositionToDelete].attachments!![positionToDelete].id
        if (fileId != "0")
            deleteAttachment(fileId)
    }


    private fun deleteEmployeeAttachment() {
        var fileId =
            employeeAttachmentsTypesObj[typePositionToDelete].attachments!![positionToDelete].id
        if (fileId != "0")
            deleteAttachment(fileId)
    }

    fun imageDeleted() {
        when (typeCategoryToDelete) {
            employeeAttachments[0].attachmentId ->
                employeeAttachmentDeleted()
            pensionAttachments[0].attachmentId ->
                pensionAttachmentDeleted()
            bankCertificatesAttachments[0].attachmentId ->
                bankCertificateAttachmentsDeleted()
            businessOwnerAttachments[0].attachmentId ->
                businessOwnerAttachmentsDeleted()
            propertyRentAttachments[0].attachmentId ->
                propertyRentalAttachmentsDeleted()
        }
    }

    private fun propertyRentalAttachmentsDeleted() {

        var attachments = mutableListOf<Attachment>()
        assetsOwnerAttachmentsTypesObj[typePositionToDelete].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.removeAt(positionToDelete)
        assetsOwnerAttachmentsTypesObj[typePositionToDelete].attachments = attachments
        updateAssetsOwnerList.value = true
    }

    private fun businessOwnerAttachmentsDeleted() {
        var attachments = mutableListOf<Attachment>()
        businessOwnerAttachmentsTypesObj[typePositionToDelete].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.removeAt(positionToDelete)
        businessOwnerAttachmentsTypesObj[typePositionToDelete].attachments = attachments
        updateBusinessOwnerList.value = true
    }

    private fun bankCertificateAttachmentsDeleted() {
        var attachments = mutableListOf<Attachment>()
        bankCertificateAttachmentsTypesObj[typePositionToDelete].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.removeAt(positionToDelete)
        bankCertificateAttachmentsTypesObj[typePositionToDelete].attachments = attachments
        updateBankCertificateList.value = true
    }

    private fun pensionAttachmentDeleted() {

        var attachments = mutableListOf<Attachment>()
        pensionAttachmentsTypesObj[typePositionToDelete].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.removeAt(positionToDelete)
        pensionAttachmentsTypesObj[typePositionToDelete].attachments = attachments
        updatePensionList.value = true
    }

    private fun employeeAttachmentDeleted() {
        var attachments = mutableListOf<Attachment>()
        employeeAttachmentsTypesObj[typePositionToDelete].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.removeAt(positionToDelete)
        employeeAttachmentsTypesObj[typePositionToDelete].attachments = attachments
        updateEmployeeList.value = true
    }

    fun deleteAttachment(fileId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.deleteAttachment(fileId)
            ) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        imageDeleted()
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }

                }
            }

        }
    }

    fun addAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int = 1, imageSelectionPosition: Int
    ): Int {
        var position = 0
        when (selectedCategoryId) {
            employeeAttachments[0].attachmentId.toString() ->
                position = addEmployeeAttachment(bitmap, categoryPosition, absolutePath, type)
            pensionAttachments[0].attachmentId.toString() ->
                position = addPensionAttachment(bitmap, categoryPosition, absolutePath, type)
            propertyRentAttachments[0].attachmentId.toString() ->
                position = addPropertyRentalAttachment(bitmap, categoryPosition, absolutePath, type)
            bankCertificatesAttachments[0].attachmentId.toString() ->
                position =
                    addBankCertificateAttachment(bitmap, categoryPosition, absolutePath, type)
            businessOwnerAttachments[0].attachmentId.toString() ->
                position = addBusinessOwnerAttachment(bitmap, categoryPosition, absolutePath, type)
        }

        addAttachments(
            imageSelectionPosition.toString(),
            absolutePath, position
        )
        return position
    }

    private fun addBankCertificateAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int
    ): Int {

        var attachments = mutableListOf<Attachment>()
        bankCertificateAttachmentsTypesObj[categoryPosition].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type, true))
        bankCertificateAttachmentsTypesObj[categoryPosition].attachments = attachments

        updateBankCertificateList.value = true

        return attachments.size - 1
    }

    private fun addBusinessOwnerAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int
    ): Int {
        var attachments = mutableListOf<Attachment>()
        businessOwnerAttachmentsTypesObj[categoryPosition].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type, true))
        businessOwnerAttachmentsTypesObj[categoryPosition].attachments = attachments

        updateBusinessOwnerList.value = true

        return attachments.size - 1
    }

    private fun addPropertyRentalAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int
    ): Int {

        var attachments = mutableListOf<Attachment>()
        assetsOwnerAttachmentsTypesObj[categoryPosition].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type, true))
        assetsOwnerAttachmentsTypesObj[categoryPosition].attachments = attachments

        updateAssetsOwnerList.value = true

        return assetsOwnerAttachmentsTypesObj[categoryPosition].attachments!!.size - 1
    }

    fun addPensionAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int = 1
    ): Int {
        var attachments = mutableListOf<Attachment>()
        pensionAttachmentsTypesObj[categoryPosition].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type, true))
        pensionAttachmentsTypesObj[categoryPosition].attachments = attachments

        updatePensionList.value = true

        return attachments.size - 1
    }

    fun addEmployeeAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int = 1
    ): Int {

        var attachments = mutableListOf<Attachment>()
        employeeAttachmentsTypesObj[categoryPosition].attachments?.let {
            attachments = it.toMutableList()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type, true))
        employeeAttachmentsTypesObj[categoryPosition].attachments = attachments

        updateEmployeeList.value = true

        return employeeAttachmentsTypesObj[categoryPosition].attachments!!.size - 1
    }


    fun addEmployeeData(
        job: String,
        salary: String,
        companyName: String,
        companyAddress: String,
        customerFinancialProfileId: String,
//        bankStatement: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ) {

        cacheEmploymentData(
            job,
            salary,
            companyName,
            companyAddress,
            customerFinancialProfileId
        )
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addEmployeeData(
                    job,
                    salary,
                    companyName,
                    companyAddress,
                    customerFinancialProfileId,
//                    bankStatement,
//                    utilityBillId,
//                    guranatorIDFrontAttachmentId,
//                    guranatorIDBackAttachmentId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)

                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }

        }
    }

    private fun cacheEmploymentData(
        job: String,
        salary: String,
        companyName: String,
        companyAddress: String,
        customerFinancialProfileId: String
    ) {
//        var map = HashMap<String, String>()
//        map["et_job"] = job
//        map["et_income"] = salary
//        map["et_company_name"] = companyName
//        map["et_company_address"] = companyAddress
//
//        saveUserFinancialData(Constants.KEY_EMPLOYMENT_DATA, map)
//        saveUserFinancialImages(
//            Constants.KEY_EMPLOYMENT_IMAGES,
//            attachmentsTypesObj
//        )
    }

//    fun getUserFinancialData(key: String): HashMap<String, String>? {
//        return dataRepository.getFinancialData(key)
//    }

//    fun saveUserFinancialImages(key: String, value: List<FinancialTypeAttachments>) {
//        dataRepository.saveUserFinancialImages(key, value)
//    }

//    fun getUserFinancialImages(key: String): List<FinancialTypeAttachments>? {
//        return dataRepository.getUserFinancialImages(key)
//    }

    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }

    fun addAttachments(
        attachmentId: String,
        image: String, position: Int
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addAttachments(
                    profileId,
                    selectedCategoryId,
                    attachmentId,
                    image, position
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        uploadAttachmentResponse.postValue(it.data)
                        when (selectedCategoryId) {
                            employeeAttachments[0].attachmentId.toString() -> {
                                var attachments =
                                    employeeAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
                                if (position < attachments?.size!!) {
                                    var attachment = attachments?.get(position)!!
                                    attachment.id = it.data!!.id
                                    employeeAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
                                        attachment
                                }
                            }
                            pensionAttachments[0].attachmentId.toString() -> {
                                var attachments =
                                    pensionAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
                                if (position < attachments?.size!!) {
                                    var attachment = attachments?.get(position)!!
                                    attachment.id = it.data!!.id
                                    pensionAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
                                        attachment
                                }
                            }
                            propertyRentAttachments[0].attachmentId.toString() -> {
                                var attachments =
                                    assetsOwnerAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
                                if (position < attachments?.size!!) {
                                    var attachment = attachments?.get(position)!!
                                    attachment.id = it.data!!.id
                                    assetsOwnerAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
                                        attachment

                                }
                            }
                            bankCertificatesAttachments[0].attachmentId.toString() -> {
                                var attachments =
                                    bankCertificateAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
                                if (position < attachments?.size!!) {
                                    var attachment = attachments?.get(position)!!
                                    attachment.id = it.data!!.id
                                    bankCertificateAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
                                        attachment
                                }
                            }
                            businessOwnerAttachments[0].attachmentId.toString() -> {
                                var attachments =
                                    businessOwnerAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
                                if (position < attachments?.size!!) {
                                    var attachment = attachments?.get(position)!!
                                    attachment.id = it.data!!.id
                                    businessOwnerAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
                                        attachment
                                }
                            }
                        }
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //onDeleteClickListener(position, attachmentId.toInt() - 1)
                }
            }
        }
    }

//    fun getData(financialProfileId: String, categoryId: String) {
//        viewModelScope.launch {
//            showLoading.postValue(true)
//            when (val resource =
//                dataRepository.getFinancialCategoryAttachments(
//                    financialProfileId,
//                    categoryId
//                )
//            ) {
//                is Resource.Success -> {
//                    showLoading.postValue(false)
//                    resource.data?.let {
//                        dataResponse.postValue(it.data)
//                    }
//                }
//                is Resource.NetworkError -> {
//                    showLoading.postValue(false)
////                    showServerError.postValue(it)
//                }
//
//                is Resource.DataError -> {
//                    showLoading.postValue(false)
//                    resource.errorResponse?.let { showServerError.postValue(it) }
//                    //showError.postValue(Event(resource.errorResponse!!.message!!))
//                    // error ="cannot create the live liveSession"
//                }
//            }
//        }
//    }

    fun downloadFileWithDynamicUrlSync(url: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            val response =
                dataRepository.downloadFileWithDynamicUrlSync(
                    url
                )

            fileResponseBody.postValue(response!!)
        }
    }

//    fun getAttachment(fileId: String) {
//        viewModelScope.launch {
//            showLoading.postValue(true)
//            when (val resource =
//                dataRepository.getAttachment(
//                    fileId
//                )
//            ) {
//                is Resource.Success -> {
//                    showLoading.postValue(false)
//                    resource.data?.let {
//                        Log.d("TAG", "file download was a success? $")
//                        file.postValue(it)
//                    }
//                }
//                is Resource.NetworkError -> {
//                    showLoading.postValue(false)
////                    showServerError.postValue(it)
//                }
//
//                is Resource.DataError -> {
//                    showLoading.postValue(false)
//                    resource.errorResponse?.let { showServerError.postValue(it) }
//                    //showError.postValue(Event(resource.errorResponse!!.message!!))
//                    // error ="cannot create the live liveSession"
//                }
//            }
//        }
//    }


    fun addPensionData(
        profileId: String,
        amount: String,
//        pensionId: String,
//        pension2Id: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addPensionData(
                    profileId,
                    amount
//                    ,
//                    pensionId,
//                    pension2Id,
//                    utilityBillId,
//                    guranatorIDFrontAttachmentId,
//                    guranatorIDBackAttachmentId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }


    fun addAssetOwnerData(
        profileId: String,
        NetIncome: String,
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addAssetOwnerData(
                    profileId,
                    NetIncome
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    fun addBusinessOwnerData(
        profileId: String,
        Job: String,
        CompanyNetIncome: String,
        CompanyName: String,
        CompanyAddress: String
    ) {

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addBusinessOwnerData(
                    profileId,
                    Job,
                    CompanyNetIncome,
                    CompanyName,
                    CompanyAddress
                )) {

                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }

                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    fun addClubData(
        profileId: String,
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addClubData(
                    profileId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    fun addCarOwnerData(
        profileId: String
    ) {

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addCarOwnerData(
                    profileId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    fun addBankCertificateData(
        profileId: String,
        NetIncome: String,
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addBankCertificateData(
                    profileId,
                    NetIncome
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        saveTypeDataResponse.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    fun getFinancialPreStepThree() {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.financialPreStepThree(profileId)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let { response ->
                            preFinancialProfileResponseStep3.value =
                                response // for checking updates
                            //inputStep32 = response.copy()
                            inputStep3.value = response.copy()
                            bindTypesSpinner.postValue(true)
                            fireViewsEvents(response)
                        }
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                    showServerError.postValue(it)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fireViewsEvents(step3Data: Step3Data) {
//        showSteps.value = it.progress
        step3Data.employees?.let {
            if (it.id.isNullOrEmpty().not())
                showEmployeeData.value = it

            it.attachments?.let { attachments ->
                if (attachments != null && attachments.size > 0)
                    showEmployeeAttachments.value = it
            }
        }

        step3Data.pension?.let {
            if (it.id.isNullOrEmpty().not())
                showPensionData.value = it

            it.attachments?.let { attachments ->
                if (attachments != null && attachments.size > 0)
                    showPensionAttachments.value = it
            }
        }

        step3Data.businessOwner?.let {
            if (it.id.isNullOrEmpty().not())
                showBusinessOwnerData.value = it

            it.attachments?.let { attachments ->
                if (attachments != null && attachments.size > 0)
                    showBusinessOwnerAttachments.value = it
            }
        }
        step3Data.bankCertificate?.let {
            if (it.id.isNullOrEmpty().not())
                showBankCertificateData.value = it

            it.attachments?.let { attachments ->
                if (attachments != null && attachments.size > 0)
                    showBankCertificateAttachments.value = it
            }
        }
        step3Data.assetsOwner?.let {
            if (it.id.isNullOrEmpty().not())
                showAssetsOwnerData.value = it

            it.attachments?.let { attachments ->
                if (attachments != null && attachments.size > 0)
                    showPropertyRentalAttachments.value = it
            }
        }
        step3Data.valuCustomer?.let {
            showValuCustomerData.value = it
        }

    }

    fun handleStep3Input(
        job: String,
        income: String,
        company: String,
        companyAddress: String,
        pensionLimit: String,
        propertyRentalLimit: String,
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String,
        bankCertificateAmount: String,
        valuName: String,
        valuPhone: String,
        valuMail: String
    ): Boolean {

        var employeeUpdated = false
        var pensionUpdated = false
        var assetsUpdated = false
        var certificateUpdated = false
        var businessUpdated = false
        var valueUpdated = false

        var employeeValid = true
        var pensionValid = true
        var assetsValid = true
        var certificateValid = true
        var businessValid = true
        var valuValid = true

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.EMPLOYMENT.id)) {
            employeeUpdated = checkUpdatedEmployee(companyAddress, company, job, income)
            if (employeeUpdated) {
                employeeValid = validateEmployeeData(job, income, company, companyAddress)
                if (employeeValid)
                    prepareEmployeeDataObject(job, income, company, companyAddress)
                else {
                    valid = false
                    return true
                }
            }
        } else
            inputStep3.value!!.employees = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.PENSION.id)) {
            pensionUpdated =
                checkUpdatedPension(pensionLimit)
            if (pensionUpdated) {
                pensionValid = validatePensionData(pensionLimit)
                if (pensionValid)
                    preparePensionDataObject(pensionLimit)
                else {
                    valid = false
                    return true
                }
            }
        } else
            inputStep3.value!!.pension = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.ASSETS_RENTAL.id)) {
            assetsUpdated =
                checkUpdatedAssetsOwner(propertyRentalLimit)
            if (assetsUpdated) {
                assetsValid = validatepropertyRentalLimitData(propertyRentalLimit)
                if (assetsValid)
                    preparePropertyRentalData(propertyRentalLimit)
                else {
                    valid = false
                    return true
                }
            }
        } else
            inputStep3.value!!.assetsOwner = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.BANK_CERTIFICATES.id)) {
            certificateUpdated =
                checkUpdatedBankCertificate(bankCertificateAmount)
            if (certificateUpdated) {
                certificateValid = validateBankCertificateData(bankCertificateAmount)
                if (certificateValid)
                    prepareBankCertificateData(bankCertificateAmount)
                else {
                    valid = false
                    return true
                }
            }
        } else
            inputStep3.value!!.bankCertificate = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.BUSINESS_OWNER.id)) {
            businessUpdated =
                checkUpdatedBusinessOwner(
                    businessOwnerIncome,
                    businessOwnerCompanyName,
                    businessOwnerCompanyAddress
                )
            if (businessUpdated) {
                businessValid = validateBusineeOwnerData(
                    businessOwnerIncome,
                    businessOwnerCompanyName,
                    businessOwnerCompanyAddress
                )
                if (businessValid)
                    prepareBusinessOwnerData(
                        businessOwnerIncome,
                        businessOwnerCompanyName,
                        businessOwnerCompanyAddress
                    ) else {
                    valid = false
                    return true
                }
            }
        } else
            inputStep3.value!!.businessOwner = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.VALUE_CUSTOMER.id)) {
            valueUpdated = checkUpdatedValuCustomer(valuName, valuPhone, valuMail)
            valuValid = validateValuCustomerData(valuName, valuPhone, valuMail)

            prepareValuCustomerData(valuName, valuPhone, valuMail)

            if (valuValid.not()) {
                valid = false
                return true
            }

        } else
            inputStep3.value!!.valuCustomer = null

        if (employeeValid && pensionValid && assetsValid && certificateValid && businessValid && valuValid)
            valid = true

        if (employeeUpdated || pensionUpdated || assetsUpdated || certificateUpdated || businessUpdated || valueUpdated) {
            updated = true
            return true
        }

        return false
    }

    fun checkUpdated(
        job: String,
        income: String,
        company: String,
        companyAddress: String,
        pensionLimit: String,
        propertyRentalLimit: String,
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String,
        bankCertificateAmount: String,
        valuName: String,
        valuPhone: String,
        valuMail: String
    ): Boolean {

        var employeeUpdated = false
        var pensionUpdated = false
        var assetsUpdated = false
        var certificateUpdated = false
        var businessUpdated = false
        var valueUpdated = false

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.EMPLOYMENT.id)) {
            employeeUpdated = checkUpdatedEmployee(companyAddress, company, job, income)
        }

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.PENSION.id)) {
            pensionUpdated =
                checkUpdatedPension(pensionLimit)

        }
        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.ASSETS_RENTAL.id)) {
            assetsUpdated =
                checkUpdatedAssetsOwner(propertyRentalLimit)
        } else
            inputStep3.value!!.assetsOwner = null

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.BANK_CERTIFICATES.id)) {
            certificateUpdated =
                checkUpdatedBankCertificate(bankCertificateAmount)
        }

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.BUSINESS_OWNER.id)) {
            businessUpdated =
                checkUpdatedBusinessOwner(
                    businessOwnerIncome,
                    businessOwnerCompanyName,
                    businessOwnerCompanyAddress
                )
        }

        if (displayedCategories.contains(Constants.INSTANCE.AttachmentType.VALUE_CUSTOMER.id)) {
            valueUpdated = checkUpdatedValuCustomer(valuName, valuPhone, valuMail)

        }

        if (employeeUpdated || pensionUpdated || assetsUpdated || certificateUpdated || businessUpdated || valueUpdated) {
            updated = true
            return true
        }

        return false
    }


    fun saveStep3Clicked(
        job: String,
        income: String,
        company: String,
        companyAddress: String,
        pensionLimit: String,
        propertyRentalLimit: String,
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String,
        bankCertificateAmount: String,
        valuName: String,
        valuPhone: String,
        valuMail: String
    ) {
        updated = handleStep3Input(
            job,
            income,
            company,
            companyAddress,
            pensionLimit,
            propertyRentalLimit,
            businessOwnerIncome,
            businessOwnerCompanyName,
            businessOwnerCompanyAddress,
            bankCertificateAmount,
            valuName,
            valuPhone,
            valuMail
        )

        var step3None = true
        if (inputStep3.value != null)
            step3None = chefckIfStep3None(inputStep3.value!!)

        if (step3None.not()) { // step3 filled
            if (valid) {
                if (updated) {
                    inputStep3.value!!.let {
                        postStep3(it)
                    }
                } else {
                    goNext.value = true
                }
            }
        } else // step3 none
            if (step2NONE.not())
                goNext.value = true
            else
                showCompleteMessage.value = true
    }

    private fun chefckIfStep3None(step3Data: Step3Data): Boolean {
        return !(step3Data.assetsOwner != null ||
                step3Data.bankCertificate != null ||
                step3Data.businessOwner != null ||
                step3Data.employees != null ||
                step3Data.pension != null ||
                step3Data.valuCustomer != null)
    }

    private fun prepareBankCertificateData(bankCertificateAmount: String): Boolean {
//        if (checkUpdatedBankCertificate(
//                bankCertificateAmount
//            )
//        ) {
        inputStep3.value!!.bankCertificate = FinancialProfileBankCertificateCategory(
            "",
            true, bankCertificateAmount.toDouble(),
            null
        )
        return true
//        } else {
//            return false
//        }
    }


    private fun prepareValuCustomerData(
        valuName: String,
        valuPhone: String,
        valuMail: String
    ): Boolean {
//        if (checkUpdatedValuCustomer(
//                valuName, valuPhone, valuMail
//            )
//        ) {
        inputStep3.value!!.valuCustomer = FinancialProfileValuCustomerCategory(
            true, valuName, valuPhone, valuMail,
            null
        )
        return true
//        } else {
//            return false
//        }
    }


    private fun prepareBusinessOwnerData(
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String
    ): Boolean {
//        return if (checkUpdatedBusinessOwner(
//                businessOwnerIncome, businessOwnerCompanyName, businessOwnerCompanyAddress
//            )
//        ) {
        inputStep3.value!!.businessOwner = FinancialProfileBusinessOwnerCategory(
            "",
            true,
            businessOwnerCompanyAddress,
            businessOwnerCompanyName,
            businessOwnerIncome.toDouble(),
            " ",
            null
        )

        return true
//        } else {
//            false
//        }
    }


    private fun preparePropertyRentalData(propertyRentalLimit: String): Boolean {
        inputStep3.value!!.assetsOwner = FinancialProfileAssetsOwnerCategory(
            "",
            true, propertyRentalLimit.toDouble(),
            null
        )
        return true
    }


    private fun preparePensionDataObject(pensionLimit: String): Boolean {

//        if (checkUpdatedPension(
//                pensionLimit
//            )
//        ) {
        inputStep3.value!!.pension = FinancialProfilePensionCategory(
            "", true, pensionLimit.toDouble(),
            null
        )
        return true
//        } else {
//            Log.e("Aasdlas", "Aajksdlakj")
//            return false
//        }
    }


    private fun validateValuCustomerData(
        valuLimit: String,
        valuPhone: String,
        valuMail: String
    ): Boolean {
        var valid = true
        if (valuLimit.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(valuLimitError = R.string.required)
        }
        if (valuPhone.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(valuPhoneError = R.string.required)
        }
        if (Validation.isValidPhone(valuPhone).not()) {
            valid = false
            step3FormState.value =
                Step3FormState(valuPhoneError = R.string.invalid_mobile_number)
        }
        if (valuMail.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(valuMailError = R.string.required)
        }
        if (Validation.isValidEmail(valuMail).not()) {
            valid = false
            step3FormState.value =
                Step3FormState(valuMailError = R.string.invalidEmailAddress)
        }
        return valid
    }

    private fun validateBankCertificateData(bankCertificateAmount: String): Boolean {
        var valid = true
        if (bankCertificateAmount.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(bankCertificateAmountError = R.string.required)
        }
        if (bankCertificateAmount.toDoubleOrNull() == 0.0) {
            valid = false
            step3FormState.value =
                Step3FormState(bankCertificateAmountError = R.string.greater_than_0)
        }
        return valid
    }

    private fun validateBusineeOwnerData(
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String
    ): Boolean {

        var valid = true
        if (businessOwnerIncome.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(businessOwnerIncomeError = R.string.required)
        }
        if (businessOwnerIncome.toDoubleOrNull() == 0.0) {
            valid = false
            step3FormState.value =
                Step3FormState(businessOwnerIncomeError = R.string.greater_than_0)
        }
        if (businessOwnerCompanyName.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(businessOwnerCompanyNameError = R.string.required)
        }
        if (businessOwnerCompanyAddress.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(businessOwnerCompanyAddressError = R.string.required)
        }
        return valid
    }

    private fun validatepropertyRentalLimitData(propertyRentalLimit: String): Boolean {
        var valid = true
        if (propertyRentalLimit.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(assetsOwnerLimitError = R.string.required)
        }
        if (propertyRentalLimit.toDoubleOrNull() == 0.0) {
            valid = false
            step3FormState.value =
                Step3FormState(assetsOwnerLimitError = R.string.greater_than_0)
        }
        return valid
    }

    private fun validatePensionData(pensionLimit: String): Boolean {
        var valid = true
        if (pensionLimit.isNullOrEmpty()) {
            valid = false
            step3FormState.value =
                Step3FormState(pensionLimitError = R.string.required)
        }
        if (pensionLimit.toDoubleOrNull() == 0.0) {
            valid = false
            step3FormState.value =
                Step3FormState(pensionLimitError = R.string.greater_than_0)
        }

        return valid
    }

    fun validateEmployeeData(
        job: String,
        income: String,
        company: String,
        companyAddress: String
    ): Boolean {
        var valid = true

//        if (job.isEmpty() && income.isEmpty() && company.isEmpty() && companyAddress.isEmpty()) {
//            step3FormState.value =
//                Step3FormState(isDataValid = true)
//            valid = true
//            return valid
//        }

        when {
            job.isEmpty() -> {
                valid = false
                step3FormState.value =
                    Step3FormState(jobError = R.string.required)
            }
            income.isEmpty() -> {
                valid = false
                step3FormState.value =
                    Step3FormState(incomeError = R.string.required)
            }
            income.toDoubleOrNull() == 0.0
            -> {
                valid = false
                step3FormState.value =
                    Step3FormState(incomeError = R.string.greater_than_0)
            }
            company.isEmpty() -> {
                valid = false
                step3FormState.value =
                    Step3FormState(companyNameError = R.string.required)
            }
            companyAddress.isEmpty() -> {
                valid = false
                step3FormState.value =
                    Step3FormState(companyAddressError = R.string.required)
            }
            else -> {
//                step3FormState.value =
//                    Step3FormState(isDataValid = true)
                valid = true
            }
        }
        return valid
    }


    private fun prepareEmployeeDataObject(
        job: String,
        income: String,
        company: String,
        companyAddress: String
    ): Boolean {

        inputStep3.value!!.employees = FinancialProfileEmployeesCategory(
            "",
            true,
            companyAddress, company, job, income.toDouble(), null
        )

        return true
    }


    private fun checkUpdatedPension(pensionLimit: String): Boolean {

        preFinancialProfileResponseStep3.value?.let {
            it.pension?.let { input ->
                try {
                    if (input.pensionNetAmount != pensionLimit.toDoubleOrNull())
                        return true
                    else if (pensionLimit.toDoubleOrNull() == 0.0)
                        return true

                } catch (e: Exception) {
                    return true
                }
            } ?: return true
        } ?: return true
//        run {
//            if (pensionLimit.isNotEmpty())
//                return true
//        }

        return false
    }


    private fun checkUpdatedBusinessOwner(
        businessOwnerIncome: String,
        businessOwnerCompanyName: String,
        businessOwnerCompanyAddress: String
    ): Boolean {
        preFinancialProfileResponseStep3.value?.let {
            it.businessOwner?.let { input ->
                try {
                    if (input.companyNetIncome != businessOwnerIncome.toDouble())
                        return true
                    else if (businessOwnerIncome.toDoubleOrNull() == 0.0)
                        return true
                    if (input.companyName != businessOwnerCompanyName)
                        return true
                    else if (businessOwnerCompanyName.isEmpty())
                        return true
                    if (input.companyAddress.toString() != businessOwnerCompanyAddress)
                        return true
                    else if (businessOwnerCompanyAddress.isEmpty())
                        return true
                } catch (e: Exception) {
                    return true
                }
            } ?: return true
//                ?: run {
//                if (businessOwnerCompanyAddress.isNotEmpty() || businessOwnerCompanyName.isNotEmpty() || businessOwnerIncome.isNotEmpty())
//                    return true
//            }
        } ?: return true

        return false
    }

    private fun checkUpdatedAssetsOwner(propertyRentalLimit: String): Boolean {
        preFinancialProfileResponseStep3.value?.let {
            it.assetsOwner?.let { input ->
                Log.e("radwa", input.netIncome.toString() + " " + propertyRentalLimit)

                var limit = propertyRentalLimit.toDoubleOrNull()
                limit?.let {
                    if (input.netIncome != limit)
                        return true
                    else {
                        if (limit == 0.0)
                            return true
                    }
                } ?: run {
                    return true
                    //return propertyRentalLimit.isNotEmpty()
                }
            } ?: return true
        } ?: return true

        return false
    }

    private fun checkUpdatedValuCustomer(
        valuName: String,
        valuPhone: String,
        valuMail: String
    ): Boolean {

        preFinancialProfileResponseStep3.value?.let {
            it.valuCustomer?.let { data ->
                try {
                    if (data.name != valuName)
                        return true
                    if (data.email != valuMail)
                        return true
                    if (data.phoneNumber != valuPhone)
                        return true
                } catch (e: Exception) {
                    return true
                }
            } ?: run {
                return valuName.isNotEmpty() || valuMail.isNotEmpty() || valuPhone.isNotEmpty()
            }
        }

        return false
    }

    private fun checkUpdatedBankCertificate(bankCertificateAmount: String): Boolean {
        preFinancialProfileResponseStep3.value?.let {
            it.bankCertificate?.let { data ->
                try {
                    if (data.netIncome != bankCertificateAmount.toDoubleOrNull())
                        return true
                    if (bankCertificateAmount.toDoubleOrNull() == 0.0)
                        return true
                } catch (e: Exception) {
                    return true
                }
            } ?: return true
        } ?: return true
//            ?: run {
//            if (bankCertificateAmount.isNotEmpty())
//                return true
//        }

        return false
    }

    fun checkUpdatedEmployee(
        companyAdderss: String,
        company: String,
        job: String,
        income: String
    ): Boolean {
        preFinancialProfileResponseStep3.value?.let {
            it.employees?.let { employee ->
                if (employee.companyAddress != companyAdderss)
                    return true
                if (employee.companyName != company)
                    return true
                if (employee.job != job)
                    return true
                if (employee.salary != income.toDoubleOrNull())
                    return true
            } ?: return true
        } ?: return true
//            ?: run {
//            if (job.isNotEmpty() || companyAdderss.isNotEmpty() || company.isNotEmpty() || income.isNotEmpty())
//                return true
//        }

        return false
    }

    fun postStep3(input: Step3Data) {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.postStepThree(input)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            saveTypeDataResponseStep3.value = it
                        }
                    }
                    is Resource.NetworkError -> {
                        showLoading.postValue(false)
//                    showServerError.postValue(it)
                    }

                    is Resource.DataError -> {
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addTypeToDisplayedList(type: FinancialType) {
        displayedCategories.add(type.id)
    }

    fun handleSelectedType(typeText: String): FinancialType {
        var type = findFinancialType(typeText)
        removeFinancialTypeFromSpinnerList(type.name)
        addTypeToDisplayedList(type)

        bindTypesSpinner.postValue(true)
        return type

    }


}
