package com.salamtak.app.ui.component.financialinfo

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.FinancialCategoriesData
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class FinancialViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    lateinit var attachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var employeeAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    lateinit var pensionAttachmentsTypesObj: MutableList<FinancialTypeAttachments>
    var profileId = ""

    val saveTypeDataResponse = MutableLiveData<ActionResponse>()
    val dataResponse = MutableLiveData<FinancialCategoriesData>()
    val file = MutableLiveData<File>()
    val fileResponseBody = MutableLiveData<ResponseBody>()
    val refreshList = MutableLiveData<Boolean>()

    val updatePensionList = MutableLiveData<Boolean>()


    fun onDeleteClickListener(position: Int, typePosition: Int) {
//        bitmaps.removeAt(position)
        var fileId = attachmentsTypesObj[typePosition].attachments!![position].id
        if (fileId != "0")
            deleteAttachment(fileId)
        attachmentsTypesObj[typePosition].attachments!!.removeAt(position)
        refreshList.value = true
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

//    fun addAttachment(
//        bitmap: Bitmap,
//        categoryPosition: Int,
//        absolutePath: String,
//        type: Int = 1
//    ): Int {
//        when (selectedCategoryId) {
//            Constants.employeeAttachments[0].attachmentId.toString() ->
//                return addEmployeeAttachment(bitmap, categoryPosition, absolutePath, type)
//            Constants.pensionAttachments[0].attachmentId.toString() ->
//                return addPensionAttachment(bitmap, categoryPosition, absolutePath, type)
//        }
//        return 0
//    }

    fun addPensionAttachment(
        bitmap: Bitmap,
        categoryPosition: Int,
        absolutePath: String,
        type: Int = 1
    ): Int {
        var attachments = pensionAttachmentsTypesObj[categoryPosition].attachments

        if (attachments == null) {
            attachments = mutableListOf()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type))
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
        var attachments = employeeAttachmentsTypesObj[categoryPosition].attachments
        if (attachments == null) {
            attachments = mutableListOf()
        }

        attachments!!.add(Attachment("0", categoryPosition, absolutePath, type))
        employeeAttachmentsTypesObj[categoryPosition].attachments = attachments

        return attachments.size - 1
    }

    fun saveUserFinancialData(key: String, value: HashMap<String, String>) {
        dataRepository.saveUserFinancialData(key, value)
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
        var map = HashMap<String, String>()
        map["et_job"] = job
        map["et_income"] = salary
        map["et_company_name"] = companyName
        map["et_company_address"] = companyAddress

        saveUserFinancialData(Constants.KEY_EMPLOYMENT_DATA, map)
        saveUserFinancialImages(
            Constants.KEY_EMPLOYMENT_IMAGES,
            attachmentsTypesObj
        )
    }

    fun getUserFinancialData(key: String): HashMap<String, String>? {
        return dataRepository.getFinancialData(key)
    }

    fun saveUserFinancialImages(key: String, value: List<FinancialTypeAttachments>) {
        dataRepository.saveUserFinancialImages(key, value)
    }

    fun getUserFinancialImages(key: String): List<FinancialTypeAttachments>? {
        return dataRepository.getUserFinancialImages(key)
    }

    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }

//    fun addAttachments(
//        attachmentId: String,
//        image: String, position: Int
//    ) {
//        viewModelScope.launch {
//            showLoading.postValue(true)
//            when (val resource =
//                dataRepository.addAttachments(
//                    profileId,
//                    selectedCategoryId,
//                    attachmentId,
//                    image, position
//                )) {
//                is Resource.Success -> {
//                    showLoading.postValue(false)
//                    resource.data?.let {
//                        uploadAttachmentResponse.postValue(it.data)
//                        when (selectedCategoryId) {
//                            Constants.employeeAttachments[0].attachmentId.toString() -> {
//                                var attachments =
//                                    employeeAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
//                                var attachment = attachments?.get(position)!!
//                                attachment.id = it.data!!.id
//                                employeeAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
//                                    attachment
//                            }
//                            Constants.pensionAttachments[0].attachmentId.toString() -> {
//                                var attachments =
//                                    pensionAttachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
//                                var attachment = attachments?.get(position)!!
//                                attachment.id = it.data!!.id
//                                pensionAttachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
//                                    attachment
//                            }
//                        }
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
//                    //onDeleteClickListener(position, attachmentId.toInt() - 1)
//                }
//            }
//        }
//    }

    fun getData(financialProfileId: String, categoryId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getFinancialCategoryAttachments(
                    financialProfileId,
                    categoryId
                )
            ) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        dataResponse.postValue(it.data)
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
}
