package com.salamtak.app.ui.component.education.universities.collages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.InstallmentTypesData
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.ui.component.education.custom.EducationFormState
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class UniversityDetailsViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false

    var busFees = MutableLiveData<Int>()
    var id = ""
    var categoryId = 0
    var selectedBranch: EducationBranch? = null
    var selectedGradeStudent1: Grade? = null
    var selectedGradeStudent2: Grade? = null
    var selectedGradeStudent3: Grade? = null

    val downPayment = MutableLiveData<Int>()
    var totalPrice = 0.0
    var monthlyInstallment = 0

    val universityDetailsResponse = MutableLiveData<University>()

    private val _customOperationFrom = MutableLiveData<EducationFormState>()
    val customOperationFromState: LiveData<EducationFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var customOperationResponse = MutableLiveData<BaseResponse>()

    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()


    fun getUniversityInstituteDetailsById(
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCollages(
                    id, categoryId, page, 100
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        universityDetailsResponse.value = it.data
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

    fun calculateBusFees(): Int {
        when {
            selectedGradeStudent3 != null -> busFees.value = selectedBranch!!.busFees * 3
            selectedGradeStudent2 != null -> busFees.value = selectedBranch!!.busFees * 2
            else -> busFees.value = selectedBranch!!.busFees
        }

        return busFees.value!!
    }

    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.EDUCATION.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
//                        installmentTypesData.value = it
                        installmentTypes.value = it.educationInstallments!!.inistituteInstallments!!
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


    fun getInstallmentPerMonth(): Int {
        if (selectedGradeStudent1 != null || selectedGradeStudent2 != null || selectedGradeStudent3 != null)  {
            if (totalPrice != 0.0) {
                var downPayment2 = totalPrice * 10 / 100
//            if (downPayment > downPayment2)
//                downPayment2 = downPayment
                return getInstallmentPerMonth(totalPrice, downPayment2)
            }
        }
        return 0
    }

    fun getInstallmentPerMonth(price: Double, downPayment: Double): Int {

        var installmentValue = 0.0
//        var downPayment2 = price * 10 / 100
//            if (downPayment > downPayment2)
//                downPayment2 = downPayment
        installmentValue =
            ((price / 2) * (1 - downPayment / price) * (1.12)) / 6

        monthlyInstallment =
            installmentValue.toBigDecimal().setScale(0, RoundingMode.UP).toInt()
        return monthlyInstallment

    }


    fun needFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }

    fun selectInstallmentType(type: InstallmentType) {
        selectedInstallmentType.value = type
    }


    fun calculateTotalPrice(busSubscribed: Boolean): Double {
        totalPrice = 0.0
        selectedGradeStudent1?.let { totalPrice = it.price }
        selectedGradeStudent2?.let { totalPrice += it.price }
        selectedGradeStudent3?.let { totalPrice += it.price }

//        var busFees = 0
//        if (busFeesStr.isNotEmpty())
//            busFees = busFeesStr.toInt()
        if (busSubscribed)
            calculateBusFees()
        totalPrice += (if (busSubscribed) busFees.value!! else 0)

        downPayment.value =
            (totalPrice * 10 / 100).toBigDecimal().setScale(0, RoundingMode.UP).toInt()


        return totalPrice

    }


}
