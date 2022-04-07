package com.salamtak.app.ui.component.education.custom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.InstallmentTypesData
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class EducationViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseViewModel() {

    //    var lookupsResponse = MutableLiveData<CustomOperationLookupsResponse>()
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customOperationFrom = MutableLiveData<EducationFormState>()
    val customOperationFromState: LiveData<EducationFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedType = 0
    var customOperationResponse = MutableLiveData<BaseResponse>()

    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    var customEducationResponse = MutableLiveData<BaseResponse>()

    var educatioRequestBody = EducationCustomRequestBody()

    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.EDUCATION.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        installmentTypesData.value = it
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

    fun CreateCustomEducationBooking(
        fullName: String,
        phoneNumber: String,
        educationEntityName: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String,
        branchName: String,
        studentId: String,
        studentName: String,
        busFees: String,
        grade: String,
        educationFees: String
    ) {
        if (validateBooking(
                fullName,
                phoneNumber,
                educationEntityName,
                selectedType.toString(),
                monthlyInstallment,
                totalCost,
                downPayment,
                branchName, studentName, busFees, grade, educationFees
            )
        ) {
            var students: List<Student>? = null
            students =
                if (grade.isNotEmpty() || educationFees.isNotEmpty() || studentId.isNotEmpty() || studentName.isNotEmpty()) {
                    var student = Student(
                        if (busFees.isNotEmpty()) busFees else "0",
                        grade,
                        educationFees,
                        studentId,
                        studentName
                    )
                    listOf(student).toList()
                } else
                    null

            selectedInstallmentType.value?.let {
                var input = EducationBookingInput(
                    downPayment,
                    selectedType.toString(),
                    selectedInstallmentType.value!!.id,
                    phoneNumber,
                    students,
                    branchName,
                    educationEntityName,
                    fullName,
                    monthlyInstallment,
                    totalCost
                )

                //var input = EducationBookingInput(1000,1,"1","01028992867", null,"asdas","aaaa","kjdkjf",2000,20000.0)
                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.CreateCustomEducationBooking(input)) {
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


    fun selectType(position: Int) {
        if (position > 0) {
            selectedType =
                position

            installmentTypesData.value?.let {
                when (position) {
                    1 -> { // University
                        installmentTypes.value =
                            installmentTypesData.value!!.educationInstallments!!.univeristyInstallments
                    }
                    2, 3 -> { // Institute
                        installmentTypes.value =
                            installmentTypesData.value!!.educationInstallments!!.inistituteInstallments
                    }
                }
            }
        }
    }

    fun getUser(): User {
        return dataRepository.getUser()!!
    }

    fun getUserName(): String {
        try {
            return dataRepository.getUser()!!.firstName + " " + dataRepository.getUser()!!.lastName
        } catch (e: Exception) {
            return ""
        }
    }

    fun selectInstallmentPlan(position: Int) {
        if (installmentTypes.value != null)
            selectedInstallmentType.value =
                installmentTypes.value!![position]
    }


    private fun validateBooking(
        fullName: String,
        phoneNumber: String,
        insuranceCompanyName: String,
        educationType: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String, branchName: String,
        studentName: String,
        busFees: String,
        grade: String,
        educationFees: String
    ): Boolean {

        var valid = true
        if (fullName.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(nameError = R.string.require_field)

            valid = false
        } else if (insuranceCompanyName.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(companyNameError = R.string.require_field)

            valid = false
        } else if (selectedType == 0) {
            _customOperationFrom.value =
                EducationFormState(typeError = R.string.require_field)

            valid = false
        } else if (phoneNumber.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(phoneError = R.string.require_field)
            valid = false
        } else if (Validation.isValidPhone(phoneNumber).not()) {
            _customOperationFrom.value =
                EducationFormState(phoneError = R.string.invalid_mobile_number)
            valid = false
        } else if (branchName.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(branchNameError = R.string.require_field)
            valid = false

        } else if (studentName.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(studentNameError = R.string.require_field)
            valid = false

        } else if (grade.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(gradeError = R.string.require_field)
            valid = false

        } else if (educationFees.isEmpty() || educationFees == "0") {
            _customOperationFrom.value =
                EducationFormState(educationFeesError = R.string.greater_than_0)
            valid = false

        }
//        else if (busFees.isEmpty()) {
//            _customOperationFrom.value =
//                EducationFormState(busFeesError = R.string.require_field)
//            valid = false
//
//        }
        else if (totalCost.isEmpty() || totalCost == "0") {
            _customOperationFrom.value =
                EducationFormState(totalCost = R.string.greater_than_0)
            valid = false
        } else if (monthlyInstallment.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(monthlyInstallmentError = R.string.require_field)
            valid = false
        } else if (downPayment.isEmpty()) {
            _customOperationFrom.value =
                EducationFormState(downPayment = R.string.require_field)
            valid = false
        } else if (downPayment.toLong() < totalCost.toLong() / 10) {
            _customOperationFrom.value =
                EducationFormState(downPayment = R.string._10_min)
            valid = false
        } else {
            _customOperationFrom.value =
                EducationFormState(isDataValid = true)
            valid = true
        }

        return valid

    }


    fun getInstallmentPerMonth(operationPrice: Double, downPayment: Double): Int {
        var installmentValue = 0.0
        var downPayment2 = operationPrice * 10 / 100
        if (downPayment > downPayment2)
            downPayment2 = downPayment

        when (selectedType) {
            0, 1 -> { // University

                installmentValue = if (selectedInstallmentType.value!!.numberOfMonths == 12)
                //    ((operationPrice / 2) * (1 + .12)) / 6
                    ((operationPrice / 2) * (1 - downPayment2 / operationPrice) * (1.12)) / 6
                else {
                    ((operationPrice - downPayment2) * (1 + (.16 / 12) * 18)) / 18
                }
            }
            2, 3 -> { // Institute
//                installmentValue = ((operationPrice / 2) * (1 + .12)) / 6
                installmentValue =
                    ((operationPrice / 2) * (1 - downPayment2 / operationPrice) * (1.12)) / 6
            }
        }

        return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
            .toInt()
        //	((Price/2) *(100%+12%))/6


//        var operationDownPayment = (operationPrice * 10) / 100
//        if (downPayment != 0.0)
//            operationDownPayment = downPayment
//
//        var interestPerMonth = 0.14 / 12
//        var adminFeesPerMonth = 0.05 / 12
//        var installmentPlan = type.numberOfMonths.toDouble()
//        var remainingAmount = operationPrice - operationDownPayment
//
//        if (operationPrice > 8000) {
//            var firstEqHalf =
//                ((remainingAmount * adminFeesPerMonth * installmentPlan) + remainingAmount) //2
//            var secondEqHalf = 1 + (installmentPlan * interestPerMonth) //3
//            var installmentValue = (firstEqHalf * secondEqHalf) / installmentPlan
//
//            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
//                .toInt()
//
//        } else {
//            var installmentValue =
//                ((operationPrice * (1 + (installmentPlan * adminFeesPerMonth))) - operationDownPayment) * (1 + (installmentPlan * interestPerMonth)) / installmentPlan
//
//            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
//                .toInt()
//        }
    }


//    fun getInstallmentPerMonth(numberOfMonths: Int): Int {
//        return getInstallmentPerMonth(operationItem.value!!, numberOfMonths)
//    }
//
//    fun calculateDownPayment(): Double {
//        downPayment.value = calculateDownPayment(operationItem.value!!)
//        return downPayment.value!!
//    }

    fun needFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }
    
    fun addCustomEducationBookingToCart(
        fullName: String,
        phoneNumber: String,
        educationEntityName: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String,
        branchName: String,
        studentId: String,
        studentName: String,
        busFees: String,
        grade: String,
        educationFees: String
    ) {

        if (validateBooking(
                fullName,
                phoneNumber,
                educationEntityName,
                selectedType.toString(),
                monthlyInstallment,
                totalCost,
                downPayment,
                branchName, studentName, busFees, grade, educationFees
            )
        ) {
            var students: List<Student>? = null
            students =
                if (grade.isNotEmpty() || educationFees.isNotEmpty() || studentId.isNotEmpty() || studentName.isNotEmpty()) {
                    var student = Student(
                        if (busFees.isNotEmpty()) busFees else "0",
                        grade,
                        educationFees,
                        studentId,
                        studentName
                    )
                    listOf(student).toList()
                } else
                    null

            selectedInstallmentType.value?.let {
                educatioRequestBody = newRequestBody(downPayment,selectedType,selectedInstallmentType.value!!.id,
                    phoneNumber,students,branchName,educationEntityName,fullName,monthlyInstallment,
                    totalCost.toInt())

                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                    dataRepository.AddCustomEducationBookingToCart(requestBody = educatioRequestBody)) 

                {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                customEducationResponse.value = it
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
        selectedType: Int,
        installmentTypeId: String,
        phoneNumber: String,
        studentsList: List<Student>?,
        branchName: String,
        educationEntityName: String,
        fullName: String,
        monthlyInstallment: String,
        totalCost: Int
    ): EducationCustomRequestBody {


        educatioRequestBody.type = CartType.EDUCATION.id //type.....
        educatioRequestBody.installmentTypeId = installmentTypeId.toInt() //installment type....
        educatioRequestBody.downPayment = downPayment.toInt() //downpayment....
        educatioRequestBody.totalCost = totalCost  //total cost.....
        educatioRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        educatioRequestBody.providerId =null
        educatioRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        educatioRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        educatioRequestBody.monthlyInstallment= monthlyInstallment.toInt()
        //custom .......
        var custom = EducationCustom(
            fullName,phoneNumber,
            educationEntityName ,
            monthlyInstallment.toInt(),"")
        educatioRequestBody.custom = custom

        // service .....
        var students = ArrayList<Students>()
        var sudentObject = Students()
     //   var schoolBranchId = randomUUID()

        studentsList?.let {
            for (i in  studentsList.indices){
                var studentId =  studentsList.get(i).studentId
                var studentName =  studentsList.get(i).studentName
                var gradeId =   studentsList.get(i).CustomGrade
                var Bus_Fees =   studentsList.get(i).Bus_Fees.toDouble().toInt()
                var IsBusSubscriped =  false
                var Study_Fees =   studentsList.get(i).Study_Fees.toDouble().toInt()

                sudentObject.studentId = studentId
                sudentObject.studentName = studentName
                sudentObject.customGrade = gradeId
                sudentObject.busFees = Bus_Fees
                sudentObject.isBusSubscriped = IsBusSubscriped
                sudentObject.studyFees = Study_Fees
                students.add(sudentObject)
            }

        }

        // service .....
        var service = EducationCService(educationEntityName,
            branchName,null,selectedType,students
            )
        educatioRequestBody.educationService = service

        return educatioRequestBody
    }
    fun randomUUID() = UUID.randomUUID().toString()

}
