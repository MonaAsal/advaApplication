package com.salamtak.app.ui.component.education.schools.schooldetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.cart.EducationCService
import com.salamtak.app.data.entities.cart.EducationRequestBody
import com.salamtak.app.data.entities.cart.Students
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.InstallmentTypesData
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.enums.EducationProviderType
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.ui.component.education.custom.EducationFormState
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class SchoolDetailsViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false
    var busSubscribed = false
    var categoryId = 0

    var busFees = MutableLiveData<Int>()
    var schoolId = ""
    var selectedBranch: EducationBranch? = null
    var selectedGradeStudent1: Grade? = null
    var selectedGradeStudent2: Grade? = null
    var selectedGradeStudent3: Grade? = null

    val downPayment = MutableLiveData<Int>()
    var totalPrice = 0.0
    //    var monthlyInstallment = 0
    var student2Added = false
    var student3Added = false

    val schoolDetailsResponse = MutableLiveData<SchoolDetails>()

    private val _customOperationFrom = MutableLiveData<EducationFormState>()
    val customOperationFromState: LiveData<EducationFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedType = 0
    var customOperationResponse = MutableLiveData<BaseResponse>()

    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    var schoolBookingResponse = MutableLiveData<BaseResponse>()
    var educationRequestBody = EducationRequestBody()

    fun getSchoolDetailsById(
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getSchoolDetailsById(
                    schoolId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        schoolDetailsResponse.value = it.data!!
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

    fun selectBranch(position: Int) {
        schoolDetailsResponse.value?.let {
            selectedBranch = it.branches[position]
            calculateBusFees()
        }

    }

    fun recalculate() {
        calculateBusFees()
        calculateTotalPrice()
    }

    fun calculateBusFees(): Int {
        when {
            student3Added -> busFees.value = selectedBranch!!.busFees * 3
            student2Added -> busFees.value = selectedBranch!!.busFees * 2
            else -> busFees.value = selectedBranch!!.busFees
        }

        return busFees.value!!
    }

    fun selectGrade(position: Int, studentNo: Int) {
        selectedBranch?.let {
            when (studentNo) {
                1 ->
                    selectedGradeStudent1 = if (position > 0) it.grades[position - 1] else null
                2 ->
                    selectedGradeStudent2 = if (position > 0) it.grades[position - 1] else null
                3 ->
                    selectedGradeStudent3 = if (position > 0) it.grades[position - 1] else null
            }

            calculateTotalPrice()
        }
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
                        if (categoryId == EducationTypes.COURSES.value) {
                            selectInstallmentType(it.educationInstallments!!.coursesInstallments[0])
                            installmentTypes.value =
                                it.educationInstallments!!.coursesInstallments!!

                        } else {
                            selectInstallmentType(it.educationInstallments!!.inistituteInstallments[0])
                            installmentTypes.value =
                                it.educationInstallments!!.inistituteInstallments!!

                        }
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

    fun createSchoolRequest(
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

            var providerType = EducationProviderType.SCHOOL.id
            if (categoryId == EducationTypes.COURSES.value)
                providerType = EducationProviderType.COURSE.id

            selectedInstallmentType.value?.let {


                var input = SchoolRequestBookingInput(
                    downPayment,
                    selectedInstallmentType.value!!.id,
                    students,
                    selectedInstallmentType.value!!.monthlyAmount.toString(),
                    schoolId,
                    selectedBranch!!.id,
                    busFees.isNotEmpty(),
                    busFees,
                    totalPrice.toString(),
                    providerType
                )


                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.createSchoolBooking(input))

                        {
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
        } else if (schoolId.isEmpty()) {
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


    fun getInstallmentPerMonth(type: InstallmentType): Int {
        if (selectedGradeStudent1 != null || selectedGradeStudent2 != null || selectedGradeStudent3 != null) {
            if (totalPrice != 0.0) {
                var downPayment2 = totalPrice * 10 / 100
                return getInstallmentPerMonth(totalPrice, downPayment2, type)
            }
        }
        return 0
    }

    fun getInstallmentPerMonth(price: Double, downPayment: Double, type: InstallmentType): Int {
        selectedInstallmentType.value?.let {
            var installmentValue = 0.0
//        var downPayment2 = price * 10 / 100
//            if (downPayment > downPayment2)
//                downPayment2 = downPayment
            installmentValue =
                ((price / 2) * (1 - downPayment / price) * (1.12)) / type.numberOfMonths!!

            type.monthlyAmount =
                installmentValue.toBigDecimal().setScale(0, RoundingMode.UP).toDouble()
            return type.monthlyAmount.toInt()
        } ?: return 0


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



    fun addSchoolRequestToCart(
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

            var providerType = EducationProviderType.SCHOOL.id
            if (categoryId == EducationTypes.COURSES.value)
                providerType = EducationProviderType.COURSE.id

            selectedInstallmentType.value?.let {

                educationRequestBody = newRequestBody(
                    downPayment,
                    selectedInstallmentType.value!!.id,
                    students,
                    selectedInstallmentType.value!!.monthlyAmount,
                    schoolId,
                    selectedBranch!!.id,
                    selectedBranch!!.name,
                    busFees.isNotEmpty(),
                    busFees,
                    totalPrice.toInt(),
                    providerType

                )

                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.AddEducationBookingToCart(educationRequestBody))
                    {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                //  customOperationResponse.value = it
                                schoolBookingResponse.value = it

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

        private fun newRequestBody(
            downPayment: String,
            installmentTypeId: String,
            studentsMutable: MutableList<BaseStudent>,
            monthlyInstallment: Double,
            schoolId: String,
            selectedBranchId: String,
            selectedBranchName: String,

            busFeesIS:Boolean,
            busFees: String,
            totalPrice: Int,
            providerType: Int
        ): EducationRequestBody {

          educationRequestBody.type = CartType.EDUCATION.id //type.....
            educationRequestBody.installmentTypeId = installmentTypeId.toInt() //installment type....
            educationRequestBody.downPayment = downPayment.toInt() //downpayment....
            educationRequestBody.totalCost = totalPrice  //total cost.....
            educationRequestBody.deviceId = Constants.DEVICE_ID //device id .....
            educationRequestBody.providerId =schoolId
            educationRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
            educationRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
            educationRequestBody.MonthlyInstallment= monthlyInstallment.toInt()
        // service .....
            // service .....
            var students = ArrayList<Students>()
            var sudentObject = Students()
            var schoolBranchId = randomUUID()

            for (i in  studentsMutable.indices){
                var studentId =  studentsMutable.get(i).studentId
                var studentName =  studentsMutable.get(i).studentName
                var gradeId =   studentsMutable.get(i).gradeId
                var Bus_Fees =   studentsMutable.get(i).Bus_Fees.toDouble().toInt()
                var IsBusSubscriped =   studentsMutable.get(i).IsBusSubscriped
               var Study_Fees =   studentsMutable.get(i).Study_Fees.toDouble().toInt()

                sudentObject.studentId = studentId
                sudentObject.studentName = studentName
                sudentObject.customGrade = gradeId
                sudentObject.busFees = Bus_Fees
                sudentObject.isBusSubscriped = IsBusSubscriped
                sudentObject.studyFees = Study_Fees
                students.add(sudentObject)
            }

            Log.d("education request",educationRequestBody.toString())
            var service = EducationCService(schoolDetailsResponse.value?.name,
                selectedBranchName,selectedBranchId,4,students
            )
            educationRequestBody.educationService = service

        return educationRequestBody
    }




    fun randomUUID() = UUID.randomUUID().toString()

}





