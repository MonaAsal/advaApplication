package com.salamtak.app.ui.component.insurance

import android.preference.PreferenceManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.cart.InsuranceCustom
import com.salamtak.app.data.entities.cart.InsuranceRequestBody
import com.salamtak.app.data.entities.cart.InsuranceService
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
class InsuranceViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseViewModel() {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customOperationFrom = MutableLiveData<InsuranceFormState>()
    val customOperationFromState: LiveData<InsuranceFormState> = _customOperationFrom
    var customInsuranceResponse = MutableLiveData<BaseResponse>()
    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedType = 0
    var customOperationResponse = MutableLiveData<BaseResponse>()

    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    var insuranceRequestBody=InsuranceRequestBody()
    fun CreateCustomInsuranceBooking(
        fullName: String,
        phoneNumber: String,
        insuranceCompanyName: String,
        insuranceType: Int,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ) {
        if (validateBooking(
                fullName,
                phoneNumber,
                insuranceCompanyName,
                insuranceType,
                monthlyInstallment,
                totalCost,
                downPayment
            )
        ) {
            selectedInstallmentType.value?.let {
                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.CreateCustomInsuranceBooking(
                            fullName,
                            phoneNumber,
                            insuranceCompanyName,
                            insuranceType,
                            monthlyInstallment,
                            downPayment,
                            selectedInstallmentType.value!!.id,
                            totalCost
                        )) {
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

    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.INSURANCE.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        installmentTypesData.value = it
                        installmentTypes.value = it.insuranceInstallments!!
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

    fun selectType(position: Int) {
        if (position > 0) {
            selectedType =
                position
        }
    }

    fun getUserName(): String {
        try {
            return dataRepository.getUser()!!.firstName + " " + dataRepository.getUser()!!.lastName
        } catch (e: Exception) {
            return ""
        }
    }

    fun getUser(): User {
        return dataRepository.getUser()!!
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
        insuranceType: Int,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ): Boolean {

        var valid = true
        if (fullName.isEmpty()) {
            _customOperationFrom.value =
                InsuranceFormState(nameError = R.string.require_field)

            valid = false
        } else if (phoneNumber.isEmpty()) {
            _customOperationFrom.value =
                InsuranceFormState(phoneError = R.string.require_field)
            valid = false
        } else if (Validation.isValidPhone(phoneNumber).not()) {
            _customOperationFrom.value =
                InsuranceFormState(phoneError = R.string.invalid_mobile_number)
            valid = false
        } else if (insuranceCompanyName.isEmpty()) {
            _customOperationFrom.value =
                InsuranceFormState(companyNameError = R.string.require_field)

            valid = false
        } else if (selectedType == 0) {
            _customOperationFrom.value =
                InsuranceFormState(typeError = R.string.require_field)

            valid = false
        } else if (totalCost.isEmpty() || totalCost.toLong() == 0L) {
            _customOperationFrom.value =
                InsuranceFormState(totalCost = R.string.greater_than_0)
            valid = false
        } else if (monthlyInstallment.isEmpty()) {
            _customOperationFrom.value =
                InsuranceFormState(monthlyInstallmentError = R.string.require_field)
            valid = false
        } else if (downPayment.isEmpty()) {
            _customOperationFrom.value =
                InsuranceFormState(downPayment = R.string.require_field)
            valid = false
        } else if (downPayment.toLong() < totalCost.toLong() / 10) {
            _customOperationFrom.value =
                InsuranceFormState(downPayment = R.string._10_min)
            valid = false
        } else {
            _customOperationFrom.value =
                InsuranceFormState(isDataValid = true)
            valid = true
        }

        return valid

    }


    fun getInstallmentPerMonth(operationPrice: Double, downPayment: Double): Int {
//        return (((operationItem.value?.price!! - calculateDownPayment()) *
//                (1 + ((type.vat) * type.numberOfMonths / 100))) / type.numberOfMonths).toInt()

        var type = selectedInstallmentType.value!!
        var operationDownPayment = (operationPrice * 10) / 100
        if (downPayment != 0.0)
            operationDownPayment = downPayment

        var interestPerMonth = 0.14 / 12
        var adminFeesPerMonth = 0.05 / 12
        var installmentPlan = type.numberOfMonths.toDouble()
        var remainingAmount = operationPrice - operationDownPayment

        if (operationPrice > 8000) {
            var firstEqHalf =
                ((remainingAmount * adminFeesPerMonth * installmentPlan) + remainingAmount) //2
            var secondEqHalf = 1 + (installmentPlan * interestPerMonth) //3
            var installmentValue = (firstEqHalf * secondEqHalf) / installmentPlan

            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
                .toInt()

        } else {
            var installmentValue =
                ((operationPrice * (1 + (installmentPlan * adminFeesPerMonth))) - operationDownPayment) * (1 + (installmentPlan * interestPerMonth)) / installmentPlan

            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
                .toInt()
        }
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

    fun addCustomInsuranceBookingToCart(
        fullName: String,
        phoneNumber: String,
        customInsuranceCompany: String,
        insuranceType: Int,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ) {
        if (validateBooking(fullName, phoneNumber, customInsuranceCompany, insuranceType, monthlyInstallment, totalCost, downPayment)
        )
            viewModelScope.launch {
                showLoading.postValue(true)
                insuranceRequestBody = newRequestBody(downPayment,totalCost,fullName,
                    phoneNumber,customInsuranceCompany,monthlyInstallment,insuranceType)

                when (val resource =
                    dataRepository.addCustomInsuranceToCart(requestBody = insuranceRequestBody)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            customInsuranceResponse.value = it
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

    private fun newRequestBody(
        downPayment: String,
        totalCost: String,
        fullName: String,
        phoneNumber: String,
        customInsuranceCompany: String,
        monthlyInstallment: String,
        insuranceType: Int
    ): InsuranceRequestBody {

        insuranceRequestBody.type = CartType.INSURANCE.id //type.....
        insuranceRequestBody.installmentTypeId = selectedInstallmentType.value?.id?.toInt() //installment type....
        insuranceRequestBody.downPayment = downPayment.toInt() //downpayment....
        insuranceRequestBody.totalCost = totalCost.toInt()   //total cost.....
        insuranceRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        insuranceRequestBody.providerId =null
        insuranceRequestBody.cartUID=Constants.cartUID
        insuranceRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        insuranceRequestBody.MonthlyInstallment=  monthlyInstallment.toInt()
        //custom .......
        var custom = InsuranceCustom(fullName,phoneNumber,customInsuranceCompany,monthlyInstallment.toInt(),"")
        insuranceRequestBody.custom = custom
        //insurance service .....
        var service = InsuranceService(insuranceType,customInsuranceCompany)
        insuranceRequestBody.insuranceService = service

        return  insuranceRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()
}
