package com.salamtak.app.ui.component.weddings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.cart.WeddingRequestBody
import com.salamtak.app.data.entities.cart.WeddingService
import com.salamtak.app.data.entities.cart.weddingCustom
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
class WeddingsViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customOperationFrom = MutableLiveData<WeddingFormState>()
    val customOperationFromState: LiveData<WeddingFormState> = _customOperationFrom

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var selectedType = 0
    var customOperationResponse = MutableLiveData<BaseResponse>()
    var customWeddingResponse = MutableLiveData<BaseResponse>()


    var installmentTypesData = MutableLiveData<InstallmentTypesData>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()

    var weddingRequestBody = WeddingRequestBody()

    fun CreateCustomWeddingBooking(
        fullName: String,
        phoneNumber: String,
        venueName: String,
        inviteesNumber: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ) {
        if (validateBooking(
                fullName,
                phoneNumber,
                venueName,
                inviteesNumber,
                monthlyInstallment,
                totalCost,
                downPayment
            )
        )
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.CreateCustomWeddingBooking(
                        fullName,
                        phoneNumber,
                        venueName,
                        inviteesNumber.toInt(),
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

    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.WEDDING.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        installmentTypesData.value = it
                        installmentTypes.value = it.weddingInstallments!!
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
        venueName: String,
        inviteesNumber: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ): Boolean {

        var valid = true
        if (fullName.isEmpty()) {
            _customOperationFrom.value =
                WeddingFormState(nameError = R.string.require_field)

            valid = false
        } else if (phoneNumber.isEmpty()) {
            _customOperationFrom.value =
                WeddingFormState(phoneError = R.string.require_field)
            valid = false
        } else if (Validation.isValidPhone(phoneNumber).not()) {
            _customOperationFrom.value =
                WeddingFormState(phoneError = R.string.invalid_mobile_number)
            valid = false
        } else if (venueName.isEmpty()) {
            _customOperationFrom.value =
                WeddingFormState(companyNameError = R.string.require_field)

            valid = false
        } else if (inviteesNumber.isEmpty()) {
            _customOperationFrom.value =
                WeddingFormState(inviteesError = R.string.require_field)
            valid = false
        } else if (inviteesNumber.toInt() <= 0) {
            _customOperationFrom.value =
                WeddingFormState(inviteesError = R.string.greater_than_0)
            valid = false
        } else if (inviteesNumber <= "0") {
            _customOperationFrom.value =
                WeddingFormState(inviteesError = R.string.greater_than_0)
            valid = false
        } else if (totalCost.isEmpty() || totalCost.toLong() == 0L) {
            _customOperationFrom.value =
                WeddingFormState(totalCost = R.string.greater_than_0)
            valid = false
        } else if (downPayment.isEmpty() || downPayment.toInt() == 0) {
            _customOperationFrom.value =
                WeddingFormState(downPayment = R.string.greater_than_0)
            valid = false
        } else if (downPayment.toLong() < totalCost.toLong() / 10) {
            _customOperationFrom.value =
                WeddingFormState(downPayment = R.string._10_min)
            valid = false
        } else if (monthlyInstallment.isEmpty() || monthlyInstallment.toInt() == 0) {
            _customOperationFrom.value =
                WeddingFormState(monthlyInstallmentError = R.string.greater_than_0)
            valid = false
        } else {
            _customOperationFrom.value =
                WeddingFormState(isDataValid = true)
            valid = true
        }

        return valid

    }


    fun getInstallmentPerMonth(operationPrice: Double, downPayment: Double): Int {
//        return (((operationItem.value?.price!! - calculateDownPayment()) *
//                (1 + ((type.vat) * type.numberOfMonths / 100))) / type.numberOfMonths).toInt()

        val type = selectedInstallmentType.value!!
        var operationDownPayment = (operationPrice * 10) / 100
        if (downPayment != 0.0)
            operationDownPayment = downPayment

        val interestPerMonth = 0.14 / 12
        val adminFeesPerMonth = 0.05 / 12
        val installmentPlan = type.numberOfMonths.toDouble()
        val remainingAmount = operationPrice - operationDownPayment

        if (operationPrice > 8000) {
            val firstEqHalf =
                ((remainingAmount * adminFeesPerMonth * installmentPlan) + remainingAmount) //2
            val secondEqHalf = 1 + (installmentPlan * interestPerMonth) //3
            val installmentValue = (firstEqHalf * secondEqHalf) / installmentPlan

            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
                .toInt()

        } else {
            val installmentValue =
                ((operationPrice * (1 + (installmentPlan * adminFeesPerMonth))) - operationDownPayment) * (1 + (installmentPlan * interestPerMonth)) / installmentPlan

            return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
                .toInt()
        }
    }

    fun needFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun addCustomWeddingBookingToCart(
        fullName: String,
        phoneNumber: String,
        venueName: String,
        inviteesNumber: String,
        monthlyInstallment: String,
        totalCost: String,
        downPayment: String
    ) {
        if (validateBooking(
                fullName,
                phoneNumber,
                venueName,
                inviteesNumber,
                monthlyInstallment,
                totalCost,
                downPayment
            )
        )

        viewModelScope.launch {
            showLoading.postValue(true)
               weddingRequestBody = newRequestBody(downPayment,totalCost,fullName,
                phoneNumber,venueName,monthlyInstallment,inviteesNumber)

            when (val resource =
                dataRepository.AddCustomWeddingBookingToCart(requestBody = weddingRequestBody)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        customWeddingResponse.value = it
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
        venueName: String,
        monthlyInstallment: String,
        inviteesNumber: String
    ): WeddingRequestBody {

        weddingRequestBody.type = CartType.WEDDING.id //type.....
        weddingRequestBody.installmentTypeId = selectedInstallmentType.value?.id?.toInt() //installment type....
        weddingRequestBody.downPayment = downPayment.toInt() //downpayment....
        weddingRequestBody.totalCost = totalCost.toInt()   //total cost.....
        weddingRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        weddingRequestBody.providerId =null
        weddingRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        weddingRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        weddingRequestBody.monthlyInstallment=  monthlyInstallment.toInt()

        //custom .......
        var custom = weddingCustom(fullName,phoneNumber,venueName,monthlyInstallment.toInt(),"")
        weddingRequestBody.custom = custom
        //wedding service .....
        var service = WeddingService(inviteesNumber.toInt(),venueName)
        weddingRequestBody.weddingService = service

        return  weddingRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()

}
