package com.salamtak.app.ui.component.finishing.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialProfileCompleted
import com.salamtak.app.data.entities.FinishingProvider
import com.salamtak.app.data.entities.FinishingRequestBookingInput
import com.salamtak.app.data.entities.cart.FinishingCategoryRequestBody
import com.salamtak.app.data.entities.cart.FinishingService
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.finishing.FinishingBaseViewModel
import com.salamtak.app.ui.component.finishing.custom.FinishingFormState
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
class FinishingRequestViewModel @Inject
constructor(dataRepository: DataRepository) : FinishingBaseViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false

    var providerId = ""
    var monthlyInstallment = 0

    val providerDetailsResponse = MutableLiveData<FinishingProvider>()

    private val _customOperationFrom = MutableLiveData<FinishingFormState>()
    val customOperationFromState: LiveData<FinishingFormState> = _customOperationFrom

    var selectedPackageId = ""
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    var finishingBookingResponse = MutableLiveData<BaseResponse>()
    var finishingRequestBody = FinishingCategoryRequestBody()


    fun getProviderDetailsById() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getFinishingProviderDetails(
                    providerId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.data?.let {
                        providerDetailsResponse.value = it.data!!
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


    fun createProviderRequest(price: String) {
        if (validateBooking(price)
        ) {
            selectedInstallmentType.value?.let {
                var input = FinishingRequestBookingInput(
                    providerId,
                    selectedPackageId,
                    it.id,
                    downPayment.toString(),
                    totalPrice.toString(),
                    null,
                    null,
                    null,
                    it.monthlyAmount.toString()
                )

                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.createFinishingBooking(input)) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                customRequestResponse.value = it
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
        totalPrice: String
    ): Boolean {
        var valid = true
        if (totalPrice.isEmpty()) {
            _customOperationFrom.value = FinishingFormState(totalCostError = R.string.require_field)
            valid = false
        } else if (totalPrice == "0") {
            _customOperationFrom.value = FinishingFormState(totalCostError = R.string.greater_than_0)
            valid = false
        }
        return valid

    }
    
    
    fun addFinishingToCartRequest(price: String){
        if (validateBooking(price)
        ) {
            selectedInstallmentType.value?.let {
                finishingRequestBody = newRequestBody(
                    providerId,
                    selectedPackageId,
                    it.id,
                    downPayment.toString(),
                    totalPrice,
                    "null",
                    "null",
                    "null",
                    it.monthlyAmount.toString()
                
                )

                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.AddCategoryFinishingBookingToCart(finishingRequestBody)) {
                           
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                finishingBookingResponse.value = it
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
        providerId: String,
        selectedPackageId: String,
        installmentTypeId: String,
        downPayment: String,
        totalPrice: Double,
        fullname: String,
        phoneNumber: String,
        providerName: String,
        monthlyAmount: String
    ): FinishingCategoryRequestBody {

        finishingRequestBody.type = CartType.FINISHING.id //type.....
        finishingRequestBody.downPayment = downPayment.toInt() //downpayment....
        finishingRequestBody.installmentTypeId = installmentTypeId.toInt() //installment type....
        finishingRequestBody.totalCost = totalPrice.toInt()  //total cost.....
        finishingRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        finishingRequestBody.providerId = providerId
        finishingRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        finishingRequestBody.itemUID = randomUUID()  //auto generate id to item ...
        finishingRequestBody.monthlyInstallment=  selectedInstallmentType.value!!.monthlyAmount.toInt()

        // service .....
//        var _selectedPackageId:String
//        if (selectedPackageId.equals("") || selectedPackageId.isNullOrEmpty()){
//            _selectedPackageId = randomUUID()
//        }else{
//            _selectedPackageId = selectedPackageId
//        }
        var service = FinishingService(PackageId = selectedPackageId , CategoryId  =null)
        finishingRequestBody.finishingService = service

        return finishingRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()


}
