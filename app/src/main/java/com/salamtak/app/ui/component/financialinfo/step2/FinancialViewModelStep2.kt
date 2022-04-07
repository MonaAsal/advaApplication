package com.salamtak.app.ui.component.financialinfo.step2

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.AttachmentData
import com.salamtak.app.data.entities.responses.IdObject
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.financialinfo.BaseFinancialViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class FinancialViewModelStep2 @Inject
constructor(private val dataRepository: DataRepository) : BaseFinancialViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var isFace = false
    var isBack = false
    var isBackGuarantor = false
    var isFaceGuarantor = false

    val uploadResponse = MutableLiveData<UploadImagesResponse>()
    val createFinancialProfileResponse = MutableLiveData<IdObject>()
    val uploadAttachmentResponse = MutableLiveData<AttachmentData>()

    var step2FormState = MutableLiveData<Step2FormState>()
    var isEgyptian = MutableLiveData<Boolean>()
    var image = MutableLiveData<String>()
    var selectedFinancialProvider: FinancialProvider? = null
    var selectedLoanType = 0
    var selectedMaritalStatus = ""
    var selectedMaritalStatusGuarantor = ""

    lateinit var financialProviders: MutableList<String>
    lateinit var maritalStatuses: MutableList<String>
    lateinit var loanTypes: MutableList<String>

    var idImageFrontName = ""
    var idImageBackName = ""
    var idImageFrontNameGuaranator = ""
    var idImageBackNameGuaranator = ""
    var currentUploadingImage = 0

    var profileId = ""
    var selectedClub: IdNameObject? = null
    var selectedCar: IdNameObject? = null
    val preFinancialProfileResponseStep2 = MutableLiveData<PreStep2Data>()
    val saveTypeDataResponseStep2 = MutableLiveData<ActionResponse>()

    var carFrontAdded = false
    var carBackAdded = false
    var clubFrontAdded = false
    var clubBackAdded = false

//    var typesLiveData: MutableLiveData<Resource<SalamtakListResponse<Category>>> =
//        categoriesDataUseCase.categoriesLiveData


    fun onDeleteClickListener(position: Int, typePosition: Int) {
//        bitmaps.removeAt(position)
//        imagesTmpNames.removeAt(position)
    }

//    fun addImage(position: Int, absolutePath: String, delete: Boolean) {
////        imagesTmpNames.add(absolutePath)
//        if (delete)
//            absoluteFilePath = absolutePath
//        uploadToServerId(absolutePath)
//    }

    fun uploadStep2Images(file: String) {
        when (currentUploadingImage) {
            0 -> {
                clubFrontAdded = true
                addAttachments(
                    Constants.clubMembershipAttachments[0].attachmentId.toString(),
                    Constants.clubMembershipAttachments[0].id.toString(),
                    file,
                    0
                )
            }
            1 -> {
                clubBackAdded = true
                addAttachments(
                    Constants.clubMembershipAttachments[1].attachmentId.toString(),
                    Constants.clubMembershipAttachments[1].id.toString(),
                    file,
                    1
                )
            }
            2 -> {
                carFrontAdded = true
                addAttachments(
                    Constants.carOwnerAttachments[0].attachmentId.toString(),
                    Constants.carOwnerAttachments[0].id.toString(),
                    file,
                    0
                )
            }
            3 -> {
                carBackAdded = true
                addAttachments(
                    Constants.carOwnerAttachments[1].attachmentId.toString(),
                    Constants.carOwnerAttachments[1].id.toString(),
                    file,
                    1
                )
            }
        }
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
                    Log.e("uploadImage", "got response")
                    resource.data?.let {
                        uploadAttachmentResponse.postValue(it.data)
                    }
                    showLoading.postValue(false)
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


    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }


    fun selectLoanType(position: Int) {
        when (position) {
            0 -> selectedLoanType = 1
            1 -> selectedLoanType = 2
            2 -> selectedLoanType = 4
        }
    }


    fun getUser(): User {
        return dataRepository.getUser()!!
    }


    fun validateStep2(
        clubMembershipEnabled: Boolean,
        carOwnerEnabled: Boolean,
        creditAndLoanEnabled: Boolean,
        limit: String
    ): Boolean {
        var valid = true
        if (clubMembershipEnabled) {
            if (selectedClub == null || selectedClub!!.id == "0") {
                valid = false
                step2FormState.value =
                    Step2FormState(clubError = R.string.required)
            }

            if (preFinancialProfileResponseStep2.value!!.attachments.clubMembership[0].attachments.isNullOrEmpty() && clubFrontAdded.not()) {
                valid = false
                step2FormState.value =
                    Step2FormState(clubImage1Error = R.string.club_front_required)
            } else if (preFinancialProfileResponseStep2.value!!.attachments.clubMembership[1].attachments.isNullOrEmpty() && clubBackAdded.not()) {
                valid = false
                step2FormState.value =
                    Step2FormState(clubImage2Error = R.string.club_back_required)
            }

        }
        if (valid && carOwnerEnabled) {
            if (selectedCar == null || selectedCar!!.id == "0") {
                valid = false
                step2FormState.value =
                    Step2FormState(carError = R.string.required)
            }
            if (preFinancialProfileResponseStep2.value!!.attachments.carOwner[0].attachments.isNullOrEmpty() && carFrontAdded.not()) {
                valid = false
                step2FormState.value =
                    Step2FormState(carImage1Error = R.string.car_fron_required)
            } else if (preFinancialProfileResponseStep2.value!!.attachments.carOwner[1].attachments.isNullOrEmpty() && carBackAdded.not()) {
                valid = false
                step2FormState.value =
                    Step2FormState(carImage2Error = R.string.car_back_required)
            }
        }
        if (valid && creditAndLoanEnabled) {
            if (limit.isNullOrEmpty()) {
                valid = false
                step2FormState.value =
                    Step2FormState(limitError = R.string.required)
            }

            if (limit.toIntOrNull() == 0) {
                valid = false
                step2FormState.value =
                    Step2FormState(limitError = R.string.greater_than_0)
            }

//            else if (selectedLoanType == null) {
//                valid = false
//                step2FormState.value =
//                    Step2FormState(typeError = R.string.required)
//            }
        }
        if (valid)
            step2FormState.value =
                Step2FormState(isDataValid = true)

        return valid
    }


    fun postStepTwo(
        clubMembershipEnabled: Boolean,
        carOwnerEnabled: Boolean,
        creditAndLoanEnabled: Boolean,
        limit: String
    ) {
        preFinancialProfileResponseStep2.value?.let {
            if (validateStep2(
                    clubMembershipEnabled,
                    carOwnerEnabled,
                    creditAndLoanEnabled,
                    limit
                )
            ) {
                var input =
                    Step2(profileId, clubMembershipEnabled, selectedClub?.let { it.id } ?: "",
                        carOwnerEnabled,
                        selectedCar?.let { it.id } ?: "",
                        creditAndLoanEnabled,
                        selectedLoanType.toString(),
                        limit, 2)
                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.postStepTwo(
                            input
                        )) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            saveTypeDataResponseStep2.value = resource.data

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
            }
        }
    }


    fun getFinancialPreStepTwo() {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.financialPreStepTwo(profileId)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            preFinancialProfileResponseStep2.postValue(it)
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

    fun selectCar(position: Int) {
        selectedCar =
            preFinancialProfileResponseStep2.value!!.cars!![position]
    }

    fun selectClub(position: Int) {
        selectedClub =
            preFinancialProfileResponseStep2.value!!.clubs!![position]
    }
}
