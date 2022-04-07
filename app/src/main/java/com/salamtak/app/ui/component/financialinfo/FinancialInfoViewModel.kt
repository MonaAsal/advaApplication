package com.salamtak.app.ui.component.financialinfo

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.FinancialLookupsResponse
import com.salamtak.app.data.entities.responses.FinancialProfileResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.ProfileUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FinancialInfoViewModel @Inject
constructor(private val profileUseCase: ProfileUseCase) : BaseViewModel() {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var lookupsResponseMutableLiveData: MutableLiveData<Resource<FinancialLookupsResponse>> =
        profileUseCase.financialLookupsLiveData

    var updateBankInfoResponseMutableLiveData: MutableLiveData<Resource<FinancialProfileResponse>> =
        profileUseCase.financialProfileLiveData

    var getBankInfoProfileResponseMutableLiveData: MutableLiveData<Resource<FinancialProfileResponse>> =
        profileUseCase.getFinancialProfileLiveData

    var selectedBankInfo: MutableLiveData<BankInfo> = MutableLiveData()
    var selectedCarModel: MutableLiveData<CarModel> = MutableLiveData()
    var selectedMaritalStatu: MutableLiveData<MaritalStatus> = MutableLiveData()
    var selectedClub: MutableLiveData<Club> = MutableLiveData()
//    var bankFormState = MutableLiveData<BankFormState>()
    var selectedCarYear = MutableLiveData<String>()


    fun getLookUps() {
        profileUseCase.getFinancialLookups()
    }

    fun selectBankAccountAt(position: Int) {
        try {
            selectedBankInfo.value =
                lookupsResponseMutableLiveData?.value!!.data?.data?.bankInfos!![position]
        } catch (e: Exception) {
        }
    }


    fun selectMaritalStatusAt(position: Int) {
        try {
            selectedMaritalStatu.value =
                lookupsResponseMutableLiveData?.value!!.data?.data?.maritalStatus!![position]
        } catch (e: Exception) {
        }
    }

    fun selectCarYear(position: Int, years: List<String>) {
        try {
            selectedCarYear.value =
                years[position]
        } catch (e: Exception) {
        }
    }

    fun selectCarModelAt(position: Int) {
        try {
            if (lookupsResponseMutableLiveData?.value!!.data?.data != null)
                selectedCarModel.value =
                    lookupsResponseMutableLiveData?.value!!.data?.data?.carModels!![position]
        } catch (e: Exception) {
        }
    }

    fun selectClubAt(position: Int) {
        try {
            selectedClub.value =
                lookupsResponseMutableLiveData?.value!!.data?.data?.clubs!![position]
        } catch (e: Exception) {
        }
    }

//    fun updateFinancialInfo(
//        numberOfChildren: String
////        ,
////        carManufactureYear: String
//    ) {
//        if (validateFinancialInput(
//                numberOfChildren,
//                selectedCarYear.value!!,
//                selectedCarModel.value?.id!!,
//                selectedClub.value?.id!!,
//                selectedBankInfo.value?.id!!
//            )
//        )
//            profileUseCase.postFinancialProfile(
//                numberOfChildren.toInt(),
//                selectedCarYear.value!!,
//                selectedCarModel.value?.id!!,
//                selectedClub.value?.id!!,
//                selectedBankInfo.value?.id!!,
//                selectedMaritalStatu.value?.id!!
//            )
//    }

//    fun validateFinancialInput(
//        numberOfChildren: String,
//        carManufactureYear: String,
//        carModelId: Int,
//        clubId: Int,
//        bankInfoId: Int
//    ): Boolean {
//        var valid = true
//
//        var numberOfChildrenInt = numberOfChildren.toIntOrNull()
//        var carManufactureYearInt = carManufactureYear.toIntOrNull()
//
////        if (carManufactureYearInt == null || (carManufactureYearInt in 2000..2030).not()) {
////            bankFormState.value =
////                BankFormState(carYearError = R.string.invalid_car_year)
////
////            valid = false
////        } else
//
//        if (numberOfChildrenInt == null) {
//            bankFormState.value =
//                BankFormState(childrenError = R.string.invalid_children_number)
//
//            valid = false
//        } else {
//            bankFormState.value =
//                BankFormState(isDataValid = true)
//            valid = true
//        }
//
//        return valid
//    }

    fun getFinancialProfile() {
        profileUseCase.getFinancialProfile()
    }

    fun getPosition(patientMaritalStatus: PatientMaritalStatus): Int? {

        var indx =
            lookupsResponseMutableLiveData.value?.data?.data?.maritalStatus?.indexOfFirst { it.id == patientMaritalStatus.id }
        //  Log.e("radwa", indx.toString())
        return indx ?: 0
    }

    fun getPosition(patientClub: PatientClub): Int? {

        var indx =
            lookupsResponseMutableLiveData.value?.data?.data?.clubs?.indexOfFirst { it.id == patientClub.id }
        //   Log.e("radwa", indx.toString())
        return indx ?: 0
    }

    fun getPosition(patientBankInfo: PatientBankInfo): Int? {

        var indx =
            lookupsResponseMutableLiveData.value?.data?.data?.bankInfos?.indexOfFirst { it.name == patientBankInfo.name }
        //  Log.e("radwa", indx.toString())
        return indx ?: 0
    }

    fun getPosition(patientCarModel: PatientCarModel): Int? {

        var indx =
            lookupsResponseMutableLiveData.value?.data?.data?.carModels?.indexOfFirst { it.id == patientCarModel.id }
        //   Log.e("radwa", indx.toString())
        return indx ?: 0
    }

    fun getPosition(carYear: String): Int? {

        var years = App.context.resources.getStringArray(R.array.years).toList()
        var indx =
            years?.indexOfFirst { it == carYear }

        return indx ?: 0
    }
}