package com.salamtak.app.ui.component.finishing.custom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinishingCustomRequestBookingInput
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.finishing.FinishingBaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class CustomFinishingViewModel @Inject
constructor(dataRepository: DataRepository) :
    FinishingBaseViewModel(dataRepository) {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customRequestFrom = MutableLiveData<FinishingFormState>()
    val customFromState: LiveData<FinishingFormState> = _customRequestFrom

    val goToStep = MutableLiveData<Int>()
    val liveInput = MutableLiveData<FinishingCustomRequestBookingInput>()

    val customFinishingBookingResponse = MutableLiveData<BaseResponse>()
    var customFinishingRequestBody = FinishingCustomRequestBody()
    var providerName = ""

    init {
        getCategories(MainCategories.FINISHING.typeId)
//        getCustomFinishingInput()
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


    fun saveStep1Data(name: String, phone: String) {

        when {
            name.isEmpty() -> {
                _customRequestFrom.value =
                    FinishingFormState(nameError = R.string.require_field)
                return
            }
            phone.isEmpty() -> {
                _customRequestFrom.value =
                    FinishingFormState(phoneError = R.string.require_field)
                return
            }
            Validation.isValidPhone(phone).not() -> {
                _customRequestFrom.value =
                    FinishingFormState(phoneError = R.string.invalid_mobile_number)
                return
            }
            else -> {
                _customRequestFrom.value = null
                liveInput.value = dataRepository.getCustomFinishingInput()
                liveInput.value?.let {
                    var tmp = it
                    tmp.FullName = name
                    tmp.PhoneNumber = phone

                    liveInput.value = tmp
                } ?: run {
                    liveInput.value = FinishingCustomRequestBookingInput(
                        null,
                        null,
                        null,
                        name,
                        phone,
                        null,
                        null, null, null, null
                    )
                }

                dataRepository.saveCustomFinishingInput(liveInput.value)
                goToStep.value = 1
            }
        }
    }

    fun isStep1Completed(): Boolean {
        dataRepository.getCustomFinishingInput()?.let {
            return it.FullName.isNullOrEmpty().not()
        }

        return true
    }

    fun isStep2Completed(): Boolean {
        dataRepository.getCustomFinishingInput()?.let {
            return it.ProviderName.isNullOrEmpty().not()
        }

        return true
    }


    fun getCustomFinishingInput(): FinishingCustomRequestBookingInput {
        var input = dataRepository.getCustomFinishingInput()

        if (input == null) {
            return createStep1Input()
        } else {
            liveInput.value = input!!
            return input
        }
    }

    private fun createStep1Input(): FinishingCustomRequestBookingInput {
        var input = FinishingCustomRequestBookingInput(
            null,
            null,
            null,
            getUserName(),
            getUser()!!.phone,
            null,
            null, null, null, null
        )
        dataRepository.saveCustomFinishingInput(input)

        liveInput.value = input

        return input
    }

    fun saveStep2Data(provider: String, phone: String, additionalInfo: String) {

        if (provider.isEmpty()) {
            _customRequestFrom.value =
                FinishingFormState(providerNameError = R.string.require_field)

            return
        } else if (phone.isEmpty()) {
            _customRequestFrom.value =
                FinishingFormState(phoneError = R.string.require_field)
            return
        } else if (Validation.isValidPhone(phone).not()) {
            _customRequestFrom.value =
                FinishingFormState(phoneError = R.string.invalid_mobile_number)
            return
        }

        _customRequestFrom.value = null
        liveInput.value = dataRepository.getCustomFinishingInput()
        categoriesLiveData.value?.let {
            liveInput.value?.let {
                var tmp = it
                tmp.ProviderName = provider
                tmp.ProviderPhone = phone
                tmp.AdditionalInfo = additionalInfo
                tmp.CategoryId =
                    if (categoryId != 0) categoryId.toString() else categoriesLiveData.value!!.data[0].id.toString()

                liveInput.value = tmp
            } ?: run {
                liveInput.value = FinishingCustomRequestBookingInput(
                    null,
                    null,
                    null,
                    null,
                    null,
                    provider,
                    null, phone, additionalInfo, categoryId.toString()
                )
            }


            dataRepository.saveCustomFinishingInput(liveInput.value)
            goToStep.value = 2
        }
    }

    fun getCategoryPosition(categoryId: String): Int {

        categoriesLiveData.value?.let {
            val b: List<Int> =
                it.data.mapIndexed { i, b -> if (b.id.toString() == categoryId) i else null }
                    .filterNotNull().toList()
            val c: List<Int> =
                it.data.withIndex().filter { it.value.id.toString() == categoryId }.map { it.index }

//            Log.e("b", b[0].toString() + " c: " + c)
            return b[0]
            //it.data.mapIndexedNotNull { index, event -> if (event.id.toString() == categoryId) index else 0 }[0]
        }
        return 0

    }

    fun saveStep3Data() {
        Log.e("custom","1")
        if (totalPrice.toString().isEmpty()) {
            _customRequestFrom.value =
                FinishingFormState(totalCostError = R.string.require_field)

            return
        }
        else if (totalPrice == 0.0) {
            _customRequestFrom.value =
                FinishingFormState(totalCostError = R.string.greater_than_0)
            return
        }
        Log.e("custom","2")
        selectedInstallmentType.value?.let { installmentType ->
            liveInput.value?.let {
                var tmp = it
                tmp.DownPayment = downPayment.toString()
                tmp.InstallmentTypeId = installmentType.id
                tmp.MonthlyInstallment = installmentType.monthlyAmount.toString()
                tmp.TotalCost = totalPrice.toString()

                liveInput.value = tmp
                dataRepository.saveCustomFinishingInput(liveInput.value)
            }
            Log.e("custom","3")
            liveInput.value?.let {
             //   postCustomFinishingRequest(it)
                addCustomFinishingBookingToCart(
                   it,downPayment
                    ,installmentType.id.toInt(), installmentType.monthlyAmount.toInt(),totalPrice.toInt() )
            }

        }

    }

    fun postCustomFinishingRequest(input: FinishingCustomRequestBookingInput) {
        viewModelScope.launch {
            Log.e("custom","4")
            showLoading.postValue(true)
            when (val resource =
                dataRepository.createCustomFinishingBooking(input
                )) {
                is Resource.Success -> {
                    resource.data?.let {
                        dataRepository.saveCustomFinishingInput(null)
                        liveInput.value = null
                        customRequestResponse.value = it
                    }
                    Log.e("custom","5")
                    showLoading.postValue(false)
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

    fun addCustomFinishingBookingToCart(
        input: FinishingCustomRequestBookingInput,
        downPayment: Int,
        installmentId: Int,
        monthlyAmount: Int,
        totalPrice: Int
    ){
        viewModelScope.launch {
            Log.e("custom","4")
            showLoading.postValue(true)
            customFinishingRequestBody = newRequestBody(input,downPayment, installmentId,monthlyAmount, totalPrice)
            
            when (val resource =
                dataRepository.AddCustomFinishingBookingToCart(customFinishingRequestBody))
                {
                is Resource.Success -> {
                    resource.data?.let {
                        dataRepository.saveCustomFinishingInput(null)
                        //liveInput.value = null
                        customFinishingBookingResponse.value = it
                    }
                    Log.e("custom","5")
                    showLoading.postValue(false)
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
        input: FinishingCustomRequestBookingInput,
        downPayment: Int,
        installmentId: Int,
        monthlyAmount: Int,
        totalPrice: Int
    ): FinishingCustomRequestBody {
        customFinishingRequestBody.type = CartType.FINISHING.id //type.....
        customFinishingRequestBody.installmentTypeId = installmentId //installment type....
        customFinishingRequestBody.downPayment = downPayment //downpayment....
        customFinishingRequestBody.totalCost = totalPrice //total cost.....
        customFinishingRequestBody.deviceId = Constants.DEVICE_ID //device id .....
       // customFinishingRequestBody.providerId =randomUUID()
        customFinishingRequestBody.providerId =null
        customFinishingRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        customFinishingRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        customFinishingRequestBody.monthlyInstallment=monthlyAmount

        //custom .......
        providerName = input.ProviderName.toString()

        var custom = FinishingCustom(input.FullName,input.PhoneNumber,
            input.ProviderName,input.ProviderPhone,monthlyAmount,input.AdditionalInfo)
        customFinishingRequestBody.custom = custom
        //finishing service .....
      //  var _selectedPackageId = randomUUID()
        var service = FinishingService(PackageId = null , CategoryId  =input.CategoryId?.toInt())
        customFinishingRequestBody.finishingService = service

      return customFinishingRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()

}
