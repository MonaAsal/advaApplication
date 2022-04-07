package com.salamtak.app.ui.component.education.universities.booking

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.InstallmentTypesData
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.ui.component.education.custom.EducationFormState
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.activity_university_booking.*
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class UniversityBookingViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false
    var student2Added = false
    var student3Added = false

    var categoryId = 0
    var busFees = MutableLiveData<Int>()
    var busSubscribed = false
    var collageId = ""
    var universityId = ""
    var selectedBranch: CollageBranch? = null
    var selectedGradeStudent1: Semester? = null
    var selectedGradeStudent2: Semester? = null
    var selectedGradeStudent3: Semester? = null

    val downPayment = MutableLiveData<Int>()
    var totalPrice = 0.0
    var monthlyInstallment = 0

    val collageDetailsResponse = MutableLiveData<Collage>()

    private val _customOperationFrom = MutableLiveData<EducationFormState>()
    val customOperationFromState: LiveData<EducationFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedType = 0
    var customOperationResponse = MutableLiveData<BaseResponse>()

    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()


    fun getCollageDetailsById(
    ) {
        Log.e("collageId", collageId)
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCollageDetails(
                    collageId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        collageDetailsResponse.value = it.data
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

    fun selectBranch(position: Int, busSubscribed: Boolean) {
        collageDetailsResponse.value?.let {
            selectedBranch = it.branches!![position]
            calculateBusFees()
            if (busSubscribed)
                calculateTotalPrice()
        }


    }

    fun calculateBusFees(): Int {
        when {
            student3Added -> busFees.value = selectedBranch!!.busFees * 3
            student2Added -> busFees.value = selectedBranch!!.busFees * 2
            else -> busFees.value = selectedBranch!!.busFees
        }

        return busFees.value!!
    }

    fun recalculate() {
        calculateBusFees()
        calculateTotalPrice()
    }

    fun selectGrade(position: Int, studentNo: Int) {
        selectedBranch?.let {
            when (studentNo) {
                1 ->
                    selectedGradeStudent1 = if (position > 0) it.semesters[position - 1] else null
                2 ->
                    selectedGradeStudent2 = if (position > 0) it.semesters[position - 1] else null
                3 ->
                    selectedGradeStudent3 = if (position > 0) it.semesters[position - 1] else null
            }

            calculateTotalPrice()
        }
    }

    fun clearSelectedGrade(studentNo: Int) {
        when (studentNo) {
            1 -> selectedGradeStudent1 = null

            2 ->
                selectedGradeStudent2 = null
            3 ->
                selectedGradeStudent3 = null
        }

        calculateTotalPrice()
    }

    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.EDUCATION.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        if (categoryId == EducationTypes.Institute.value)
                            installmentTypes.value =
                                it.educationInstallments!!.inistituteInstallments!!
                        else
                            installmentTypes.value =
                                it.educationInstallments!!.univeristyInstallments!!
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

    fun createUniversityRequest(
        downPayment: String,
        busFees: String,
        studentName1: String,
        studentName2: String,
        studentName3: String
    ) {
        if (validateBooking(
                downPayment, studentName1, studentName2, studentName3
            )
        ) {
            showLoading.postValue(true)

            val students = mutableListOf<BaseStudent>()

            selectedGradeStudent1?.let {
                var student = BaseStudent()
                student.gradeId = it.id
                student.studentName = studentName1
                student.Study_Fees = it.price.toString()
                student.Bus_Fees = if (busSubscribed) selectedBranch!!.busFees.toString() else "0"
                student.IsBusSubscriped = busSubscribed
                students.add(student)
            }

            selectedGradeStudent2?.let {
                var student = BaseStudent()
                student.gradeId = it.id
                student.studentName = studentName2
                student.Study_Fees = it.price.toString()
                student.Bus_Fees = if (busSubscribed) selectedBranch!!.busFees.toString() else "0"
                student.IsBusSubscriped = busSubscribed
                students.add(student)
            }

            selectedGradeStudent3?.let {
                var student = BaseStudent()
                student.gradeId = it.id
                student.studentName = studentName3
                student.Study_Fees = it.price.toString()
                student.Bus_Fees = if (busSubscribed) selectedBranch!!.busFees.toString() else "0"
                student.IsBusSubscriped = busSubscribed
                students.add(student)
            }

            var input = UniversityRequestBookingInput(
                universityId, collageId,
                selectedBranch!!.id,
                monthlyInstallment.toString(),
                selectedInstallmentType.value!!.id,
                downPayment,
                busFees.isNotEmpty(),
                busFees,
                totalPrice.toString(),
                students
            )
            viewModelScope.launch {

                if (categoryId == EducationTypes.Institute.value) {
                    when (val resource =
                        dataRepository.createInstituteBooking(input)) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                customOperationResponse.value = it
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
                } else {
                    when (val resource =
                        dataRepository.createUniversityBooking(input)) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                customOperationResponse.value = it
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
        }
    }

    fun validateBooking(
        downPayment: String,
        studentName: String, studentName2: String,
        studentName3: String
    ): Boolean {

        var valid = true
        if (selectedGradeStudent1 == null) {
            _customOperationFrom.value =
                EducationFormState(gradeError = R.string.require_field)

            valid = false
        } else if (studentName.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(nameError = R.string.require_field)

            valid = false
        } else if (student2Added && selectedGradeStudent2 == null) {
            _customOperationFrom.value =
                EducationFormState(gradeError2 = R.string.require_field)

            valid = false
        } else if (student2Added && studentName2.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(nameError2 = R.string.require_field)

            valid = false
        } else if (student3Added && selectedGradeStudent3 == null) {
            _customOperationFrom.value =
                EducationFormState(gradeError3 = R.string.require_field)

            valid = false
        } else if (student3Added && studentName3.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(nameError3 = R.string.require_field)

            valid = false
        } else if (downPayment.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(downPayment = R.string.require_field)

            valid = false
        } else if (collageId.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(schoolError = R.string.require_field)

            valid = false
        } else if (selectedBranch == null) {
            _customOperationFrom.value =
                EducationFormState(branchNameError = R.string.require_field)

            valid = false
        }

        return valid

    }


//    fun getInstallmentPerMonth(type: InstallmentType, busFees: String): Int {
//        selectedGradeStudent1?.let {
//            if (totalPrice != 0.0) {
//                var downPayment2 = totalPrice * 10 / 100
////            if (downPayment > downPayment2)
////                downPayment2 = downPayment
//                return getInstallmentPerMonth(totalPrice, downPayment2)
//            }
//        }
//        return 0
//    }


    fun getInstallmentPerMonth(type: InstallmentType): Int {
        if (selectedGradeStudent1 != null || selectedGradeStudent2 != null || selectedGradeStudent3 != null) {
            if (totalPrice != 0.0) {
                var downPayment2 = totalPrice * 10 / 100
//            if (downPayment > downPayment2)
//                downPayment2 = downPayment
                return getInstallmentPerMonth(type, totalPrice, downPayment2)
            }
        }
        return 0
    }

    fun getInstallmentPerMonth(type: InstallmentType, price: Double, downPayment: Double): Int {
        var installmentValue = 0.0

        installmentValue = when (type.numberOfMonths) {
            12 -> {
                ((price / 2) * (1 - downPayment / price) * (1 + 0.12)) / 6
            }
            18 -> {
                ((price - downPayment) * (1 + (0.16 / 12) * 18)) / 18
            }
            else -> {
                ((price - downPayment) * (1 + (0.16 / 12) * 18)) / type.numberOfMonths
            }
        }
        monthlyInstallment =
            installmentValue.toBigDecimal().setScale(0, RoundingMode.UP).toInt()
        Log.e("monthlyInstallment", monthlyInstallment.toString())
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


    fun calculateTotalPrice(): Double {
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
