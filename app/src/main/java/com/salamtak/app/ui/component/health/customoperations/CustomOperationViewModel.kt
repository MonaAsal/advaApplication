package com.salamtak.app.ui.component.health.customoperations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.CustomOperationLookupsResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.health.BaseOperationViewModelN
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class CustomOperationViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseOperationViewModelN(dataRepository) {

    var lookupsResponse = MutableLiveData<CustomOperationLookupsResponse>()
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customOperationFrom = MutableLiveData<CustomOperationFormState>()
    val customOperationFromState: LiveData<CustomOperationFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedCategory: MutableLiveData<Category> = MutableLiveData()
    var customOperationResponse = MutableLiveData<BaseResponse>()

    val customHealthbBookingResponse = MutableLiveData<BaseResponse>()
    var customHealthRequestBody = HealthCustomRequestBody()


    fun getCustomOperationLookups() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCustomOperationLookups()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        lookupsResponse.value = it
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

    fun selectCategory(position: Int) {
        if (lookupsResponse.value!!.data?.categories != null && position > 0) {
            selectedCategory.value =
                lookupsResponse.value!!.data?.categories!![position - 1]

            Log.e("cat", selectedCategory.value!!.name)
        }
    }

    fun selectInstallmentPlan(position: Int) {
        if (lookupsResponse?.value?.data?.installmentTypes != null)
            selectedInstallmentType.value =
                lookupsResponse?.value?.data?.installmentTypes!![position]
    }


    fun bookCustomOperation(
        selectedSpecialityPosition: Int,
        DoctorName: String,
        OperationName: String,
        MonthlyInstallment: String,
        DownPayment: String,
        TotalCost: String
    ) {
        var user = dataRepository.getUser()!!

        if (validateBooking(
                selectedSpecialityPosition,
                DoctorName,
                OperationName,
                MonthlyInstallment,
                DownPayment,
                TotalCost
            )
        ) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.bookCustomOperation(
                        user.firstName + " " +
                                user.lastName,
                        user!!.phone,
                        DoctorName,
                        OperationName,
                        selectedCategory.value!!.id,
                        selectedInstallmentType.value!!.id,
                        MonthlyInstallment.toDouble(),
                        DownPayment.toDouble(),
                        TotalCost.toDouble()
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            customOperationResponse.value = it

                            dataRepository.bookedOoeration()
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

    private fun validateBooking(
        selectedSpecialityPosition: Int,
        doctorName: String,
        operationName: String,
        monthlyInstallment: String,
        downPayment: String,
        totalCost: String
    ): Boolean {

        var valid = true
        if (doctorName.isEmpty()) {
            _customOperationFrom.value =
                CustomOperationFormState(doctorNameError = R.string.require_field)

            valid = false
        } else if (selectedCategory.value == null || selectedSpecialityPosition == 0) {
            _customOperationFrom.value =
                CustomOperationFormState(specialityError = R.string.require_field)

            valid = false
        } else if (operationName.isEmpty()) {
            _customOperationFrom.value =
                CustomOperationFormState(operationName = R.string.require_field)
            valid = false
        }
        else if (totalCost.isEmpty() || totalCost == "0") {
            _customOperationFrom.value =
                CustomOperationFormState(totalCost = R.string.greater_than_0)
            valid = false
        }
        else if (monthlyInstallment.isEmpty()) {
            _customOperationFrom.value =
                CustomOperationFormState(monthlyInstallment = R.string.require_field)
            valid = false
        } else if (downPayment.isEmpty()) {
            _customOperationFrom.value =
                CustomOperationFormState(downPayment = R.string.require_field)
            valid = false
        } else if (downPayment.toLong() < totalCost.toLong() / 10) {
            _customOperationFrom.value =
                CustomOperationFormState(downPayment = R.string._10_min)
            valid = false
        } else {
            _customOperationFrom.value =
                CustomOperationFormState(isDataValid = true)
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

        var interestPerMonth = 0.15 / 12
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

    fun addCustomHealthBookingToCart(
        selectedSpecialityPosition: Int,
        DoctorName: String,
        OperationName: String,
        MonthlyInstallment: String,
        DownPayment: String,
        TotalCost: String
    ) {

        var user = dataRepository.getUser()!!

        if (validateBooking(
                selectedSpecialityPosition,
                DoctorName,
                OperationName,
                MonthlyInstallment,
                DownPayment,
                TotalCost
            )
        ) {
            viewModelScope.launch {
                showLoading.postValue(true)
                customHealthRequestBody = newRequestBody(
                    user.firstName + " " + user.lastName,
                    user!!.phone,
                    DoctorName,
                    OperationName,
                    selectedCategory.value!!.id,
                    selectedInstallmentType.value!!.id,
                    MonthlyInstallment.toDouble(),
                    DownPayment.toDouble(),
                    TotalCost.toDouble())

                Log.d("customHealthRequestBody",customHealthRequestBody.toString())

                when (val resource =
                    dataRepository.AddCustomHealthBookingToCart(requestBody = customHealthRequestBody)) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            customHealthbBookingResponse.value = it
                            dataRepository.bookedOoeration()
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

    private fun newRequestBody(
        fullName: String,
        phone: String,
        doctorName: String,
        operationName: String,
        categoryID: Int,
        installementID: String,
        monthlyInstallment: Double,
        downPayment: Double,
        totalCost: Double,
    ): HealthCustomRequestBody {

        customHealthRequestBody.type = CartType.HEALTH.id //type.....
        customHealthRequestBody.installmentTypeId = installementID.toInt() //installment type....
        customHealthRequestBody.downPayment = downPayment.toInt() //downpayment....
        customHealthRequestBody.totalCost = totalCost.toInt()   //total cost.....
        customHealthRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        customHealthRequestBody.providerId =null
        customHealthRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        customHealthRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        customHealthRequestBody.monthlyInstallment=  monthlyInstallment.toInt()
        //custom .......
        var custom = HealthCustom(fullName,phone,doctorName,monthlyInstallment.toInt(),"")
        customHealthRequestBody.custom = custom
        //health service .....
        var operationName = operationName
        var categoryId =categoryID
        var service = HealthService(customeOperationName = operationName,customeDoctorName = doctorName,categoryId =categoryId)
        customHealthRequestBody.healthService = service

        return  customHealthRequestBody
    }



    fun randomUUID() = UUID.randomUUID().toString()
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

}
