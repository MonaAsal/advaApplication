package com.salamtak.app.ui.component.financialinfo.step1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.AttachmentData
import com.salamtak.app.data.entities.responses.IdObject
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.financialinfo.BaseFinancialViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class FinancialViewModelStep1 @Inject
constructor(private val dataRepository: DataRepository) : BaseFinancialViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var isFace = false
    var isBack = false
    var isBackGuarantor = false
    var isFaceGuarantor = false

    val preFinancialProfileResponse = MutableLiveData<PreStep1Data>()

    val uploadResponse = MutableLiveData<UploadImagesResponse>()
    val createFinancialProfileResponse = MutableLiveData<IdObject>()
    val uploadAttachmentResponse = MutableLiveData<AttachmentData>()

    var basicFormState = MutableLiveData<Step1FormState>()

    var isEgyptian = MutableLiveData<Boolean>()
    var image = MutableLiveData<String>()
    var selectedFinancialProvider: FinancialProvider? = null
    var selectedLoanType = 0
    var selectedMaritalStatus = ""
    var selectedMaritalStatusGuarantor = ""

    lateinit var financialProviders: MutableList<String>
    lateinit var maritalStatuses: MutableList<String>
    lateinit var loanTypes: MutableList<String>

    //    var listOfImages = mutableListOf<String>()
//    var imagesTmpNames = mutableListOf<String>()
    val idImageFront = MutableLiveData<ResponseBody>()
    val idImageBack = MutableLiveData<ResponseBody>()

    var idImageFrontName = ""
    var idImageBackName = ""
    var idImageFrontNameGuaranator = ""
    var idImageBackNameGuaranator = ""
    var currentUploadingImage = 0

    var profileId = ""

    fun onDeleteClickListener(position: Int, typePosition: Int) {
//        bitmaps.removeAt(position)
//        imagesTmpNames.removeAt(position)
    }

    fun addImage(position: Int, absolutePath: String) {
//        imagesTmpNames.add(absolutePath)
        uploadToServerId(absolutePath)
    }


    fun addAttachments(
        categoryId: String, attachmentId: String,
        image: String, position: Int
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.addCategoryAttachment(
                    profileId,
                    categoryId,
                    attachmentId,
                    image, position
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        uploadAttachmentResponse.postValue(it.data)
//
//                        var attachments =
//                            attachmentsTypesObj[attachmentId.toInt() - 1].attachments?.toList()
//                        var attachment = attachments?.get(position)!!
//                        attachment.id = it.data!!.id
//                        attachmentsTypesObj[attachmentId.toInt() - 1].attachments!![position] =
//                            attachment
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
//                    showServerError.postValue(it)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                    onDeleteClickListener(position, attachmentId.toInt() - 1)
                    //showError.postValue(Event(resource.errorResponse!!.message!!))
                    // error ="cannot create the live liveSession"
                }
            }
        }
    }

    private fun uploadToServerId(absolutePath: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.uploadFile(absolutePath)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        if (isFace)
                            idImageFrontName = (it.data!!.savedFilesName!![0])
                        else if (isBack)
                            idImageBackName = (it.data!!.savedFilesName!![0])
                        else if (isFaceGuarantor)
                            idImageFrontNameGuaranator = (it.data!!.savedFilesName!![0])
                        else if (isBackGuarantor)
                            idImageBackNameGuaranator = (it.data!!.savedFilesName!![0])

                        uploadResponse.postValue(it.data!!)

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

    fun saveUserFinancialData(key: String, value: HashMap<String, String>) {
        dataRepository.saveUserFinancialData(key, value)
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

    fun getfinancialPreStepOne() {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.financialPreStepOne()) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            preFinancialProfileResponse.postValue(it)
//                            if (it.data!!.financialProfile != null) {
//                                idImageFrontName = it.data!!.financialProfile!!.idFaceUrl
//                                idImageBackName = it.data!!.financialProfile!!.idBackUrl
//                                if (imagesTmpNames == null)
//                                    imagesTmpNames = mutableListOf<String>()
//                                imagesTmpNames.add(it.data!!.financialProfile!!.idFaceUrl)
//                                imagesTmpNames.add(it.data!!.financialProfile!!.idBackUrl)
//                            }
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectLoanType(position: Int) {
        when (position) {
            0 -> selectedLoanType = 1
            1 -> selectedLoanType = 2
            2 -> selectedLoanType = 4
        }
    }

    fun selectMaritalStatus(position: Int) {
        selectedMaritalStatus =
            preFinancialProfileResponse.value!!.martialStatues!![position].id.toString()
    }

    fun selectMaritalStatusGuarantor(position: Int) {
        selectedMaritalStatusGuarantor =
            preFinancialProfileResponse.value!!.martialStatues!![position].id.toString()
    }

    fun selectPaymentMethod(position: Int) {
        if (position != 0)
            selectedFinancialProvider =
                preFinancialProfileResponse.value!!.financialProviders!![position - 1]
    }

    fun getUser(): User {
        return dataRepository.getUser()!!
    }

    fun onFinishClick(
        mobile: String,
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        id: String,
        expiryDate: String,
        street: String,
        building: String,
        cityId: String,
        areaId: String,
        firstNameGuranator: String,
        secondNameGuranator: String,
        thirdNameGuranator: String,
        lastNameGuranator: String,
        idGuranator: String,
        expiryDateGuranator: String,
        mobileGuarantor: String, maritalStatusGuaranator: String
    ) {
        if (validateFinancialInput(
                mobile,
                firstName,
                secondName,
                thirdName,
                lastName,
                id,
                expiryDate,
                street,
                building,
                firstNameGuranator,
                secondNameGuranator,
                thirdNameGuranator,
                lastNameGuranator,
                idGuranator,
                expiryDateGuranator, mobileGuarantor
            )
        ) {
            saveBasicProfile(
                mobile,
                firstName,
                secondName,
                thirdName,
                lastName,
                id,
                expiryDate,
                street,
                building,
                cityId,
                areaId,
                firstNameGuranator,
                secondNameGuranator,
                thirdNameGuranator,
                lastNameGuranator,
                idGuranator,
                expiryDateGuranator,
                mobileGuarantor, maritalStatusGuaranator
            )
        }
    }

    fun saveBasicProfile(
        mobile: String,
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        idNumber: String,
        expiryDate: String,
        street: String,
        building: String,
        cityId: String,
        areaId: String,
        firstNameGuranator: String,
        secondNameGuranator: String,
        thirdNameGuranator: String,
        lastNameGuranator: String,
        idGuranator: String,
        expiryDateGuranator: String,
        mobileGuarantor: String, maritalStatusGuaranator: String
    ) {
        var profileId =
            if (preFinancialProfileResponse.value!!.financialProfile != null)
                preFinancialProfileResponse.value!!.financialProfile!!.id
            else null

        var guarantor =
            FinancialProfile(
                null,
                idGuranator,
                expiryDateGuranator,
                firstNameGuranator,
                secondNameGuranator,
                lastNameGuranator,
                thirdNameGuranator,
                null,
                null,
                null,
                null,
                mobileGuarantor,
                maritalStatusGuaranator,
                if (idImageFrontNameGuaranator.isNotEmpty()) idImageFrontNameGuaranator else null,
                if (idImageBackNameGuaranator.isNotEmpty()) idImageBackNameGuaranator else null,
                false,
                null,
                1,
                null,
                if (idImageFrontNameGuaranator.isNotEmpty()) idImageFrontNameGuaranator else null,
                if (idImageBackNameGuaranator.isNotEmpty()) idImageBackNameGuaranator else null,
            )


        var financialProfile = FinancialProfile(
            profileId,
            idNumber,
            expiryDate,
            firstName,
            secondName,
            lastName,
            thirdName,
            building,
            street,
            cityId,
            areaId,
            mobile,
            selectedMaritalStatus,
            if (idImageFrontName.isNotEmpty()) idImageFrontName else null,
            if (idImageBackName.isNotEmpty()) idImageBackName else null,
            false,
            selectedFinancialProvider!!.id,
            1,
            guarantor,
            if (idImageFrontName.isNotEmpty()) idImageFrontName else null,
            if (idImageBackName.isNotEmpty()) idImageBackName else null,

            )

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.postStepOne(financialProfile)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        preFinancialProfileResponse.value!!.financialProfile?.let {
                            if (it.progress == 0) it.progress = 1
                        } ?: getfinancialPreStepOne()

                        dataRepository.saveFinancialProfileId(it.data!!.id)
                        dataRepository.saveNeedFinancialInfo(false)
                        idImageFrontName = ""
                        idImageBackName = ""
                        idImageFrontNameGuaranator = ""
                        idImageBackNameGuaranator = ""
                        profileId = it.data.id
                        createFinancialProfileResponse.value = it.data

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


    fun validateFinancialInput(
        mobile: String,
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        nationalId: String,
        nationalIdExpireDate: String,
        streetAddress: String,
        buildingNum: String,
        firstNameGuranator: String,
        secondNameGuranator: String,
        thirdNameGuranator: String,
        lastNameGuranator: String,
        idGuranator: String,
        expiryDateGuranator: String,
        mobileGuarantor: String
    ): Boolean {
        var valid = true

        if (selectedFinancialProvider == null) {
            valid = false
            basicFormState.value =
                Step1FormState(paymentError = R.string.required)
            return false
        } else if (!Validation.isValidPhone(mobile)) {
            basicFormState.value =
                Step1FormState(phoneError = R.string.invalid_mobile_number)
            valid = false
            return false
        } else if (firstName.isNullOrEmpty()) {
            basicFormState.value = Step1FormState(firstNameError = R.string.first_name_error)
            valid = false
            return false
        } else if (secondName.isNullOrEmpty()) {
            basicFormState.value = Step1FormState(secondNameError = R.string.second_name_error)
            valid = false
            return false
        } else if (thirdName.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(thirdNameError = R.string.third_name_error)
            valid = false
            return false
        } else if (lastName.isNullOrEmpty()) {
            basicFormState.value = Step1FormState(lastNameError = R.string.last_name_error)
            valid = false
            return false
        } else if ((nationalId.isNullOrEmpty() || nationalId.length != 14)) {
            basicFormState.value = Step1FormState(nationalIdError = R.string.national_id_erorr)
            valid = false
            return false
        } else if (nationalIdExpireDate.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(nationalIdExpireDateError = R.string.national_id_expire_date_error)
            valid = false
            return false
        } else if (streetAddress.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(streetAddressError = R.string.street_address_error)
            valid = false
            return false
        } else if (buildingNum.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(buildingNumError = R.string.building_num_error)
            valid = false
            return false
        } else if (firstNameGuranator.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(firstNameErrorGuarantor = R.string.first_name_error)
            valid = false
            return false
        } else if (secondNameGuranator.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(secondNameErrorGuarantor = R.string.second_name_error)
            valid = false
            return false
        } else if (thirdNameGuranator.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(thirdNameErrorGuarantor = R.string.third_name_error)
            valid = false
            return false
        } else if (lastNameGuranator.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(lastNameErrorGuarantor = R.string.last_name_error)
            valid = false
            return false
        } else if ((idGuranator.isNullOrEmpty() || idGuranator.length != 14)) {
            basicFormState.value =
                Step1FormState(nationalIdErrorGuarantor = R.string.national_id_erorr)
            valid = false
            return false
        } else if (!Validation.isValidPhone(mobileGuarantor)) {
            basicFormState.value =
                Step1FormState(phoneErrorGuarantor = R.string.invalid_mobile_number)
            valid = false
            return false
        } else if (expiryDateGuranator.isNullOrEmpty()) {
            basicFormState.value =
                Step1FormState(nationalIdExpireDateErrorGuarantor = R.string.national_id_expire_date_error)
            valid = false
            return false
        }
//        else if (preFinancialProfileResponse.value!!.financialProfile == null && (idImageFrontNameGuaranator.isEmpty() || idImageBackNameGuaranator.isEmpty())) {
//            basicFormState.value =
//                Step1FormState(imagesErrorGuarantor = R.string.ids_images_error_guarantor)
//            valid = false
//            return false
//        }
        else { // check for images
            if (preFinancialProfileResponse.value!!.financialProfile == null) {
                if (idImageFrontName.isEmpty() || idImageBackName.isEmpty()) {
                    basicFormState.value = Step1FormState(imagesError = R.string.ids_images_error)
                    valid = false
                    return valid
                } else {
                    if (idImageFrontNameGuaranator.isEmpty() || idImageBackNameGuaranator.isEmpty()) {
                        basicFormState.value =
                            Step1FormState(imagesErrorGuarantor = R.string.ids_images_error_guarantor)
                        valid = false
                        return valid
                    }
                }
            } else { // profile is not null
                if ((preFinancialProfileResponse.value!!.financialProfile!!.idFaceUrl.isNullOrEmpty() && idImageFrontName.isNullOrEmpty()) ||
                    (preFinancialProfileResponse.value!!.financialProfile!!.idBackUrl.isNullOrEmpty() && idImageBackName.isNullOrEmpty())
                ) {
                    basicFormState.value = Step1FormState(imagesError = R.string.ids_images_error)
                    valid = false
                    return valid
                } else { // guarantor
                    if (preFinancialProfileResponse.value!!.financialProfile!!.customerFinancialGuarantor != null) {
                        if ((preFinancialProfileResponse.value!!.financialProfile!!.customerFinancialGuarantor!!.idFaceUrl.isNullOrEmpty() && idImageFrontNameGuaranator.isNullOrEmpty()) ||
                            (preFinancialProfileResponse.value!!.financialProfile!!.customerFinancialGuarantor!!.idBackUrl.isNullOrEmpty() && idImageBackNameGuaranator.isNullOrEmpty())
                        ) {
                            basicFormState.value =
                                Step1FormState(imagesErrorGuarantor = R.string.ids_images_error_guarantor)
                            valid = false
                            return valid
                        }
                    } else {
                        if (idImageFrontNameGuaranator.isEmpty() || idImageBackNameGuaranator.isEmpty()) {
                            basicFormState.value =
                                Step1FormState(imagesErrorGuarantor = R.string.ids_images_error_guarantor)
                            valid = false
                            return valid
                        }
                    }

                }
            }
        }

        basicFormState.value =
            Step1FormState(isDataValid = true)
        valid = true
        return valid
    }


    fun getNationalIdAttachments(
        FinancialProfileId: String,
        IsFaceImage: Boolean
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            val response =
                dataRepository.getNationalIdAttachments(FinancialProfileId, IsFaceImage)

            if (IsFaceImage)
                idImageFront.postValue(response!!)
            else
                idImageBack.postValue(response!!)
        }
    }


}
