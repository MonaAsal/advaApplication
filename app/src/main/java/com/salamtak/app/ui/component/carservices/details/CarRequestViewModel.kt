package com.salamtak.app.ui.component.carservices.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.CarProviderDetails.Location
import com.salamtak.app.data.entities.CarServiceRequestBookingInput
import com.salamtak.app.data.entities.FinancialProfileCompleted
import com.salamtak.app.data.entities.cart.CarCategoryRequestBody
import com.salamtak.app.data.entities.cart.CarService
import com.salamtak.app.data.entities.cart.FinishingService
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.carservices.CarBaseViewModel
import com.salamtak.app.ui.component.carservices.custom.CarFormState
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CarRequestViewModel @Inject
constructor(dataRepository: DataRepository) : CarBaseViewModel(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var page = 1
    var isLastPage = false
    var isLoading = false
    var monthlyInstallment = 0

    val providerDetailsResponse = MutableLiveData<CarProviderDetails>()
    val providerDetailsLocationsList = MutableLiveData<ArrayList<Location>>()
    var providerId = ""

    private val _customOperationFrom = MutableLiveData<CarFormState>()
    val customOperationFromState: LiveData<CarFormState> = _customOperationFrom
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    var carBookingResponse = MutableLiveData<BaseResponse>()
    var carRequestBody = CarCategoryRequestBody()

    fun getProviderDetailsById() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCarProviderDetails(
                    providerId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
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

    fun createProviderRequest(
        price: String
    ) {
        if (validateBooking(
                price
            )
        ) {
            selectedInstallmentType.value?.let {
                var input = CarServiceRequestBookingInput(
                    providerId,
                    it.id,
                    downPayment.toString(),
                    totalPrice.toString(),
                    it.monthlyAmount.toString(),
                )

                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.createCarServiceBooking(input)) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                customRequestResponse.value = it
                            //    Log.d("submit  customRequestResponse.value ",  customRequestResponse.value.toString()  )

                            }
                        }
                        is Resource.NetworkError -> {
                            showLoading.postValue(false)
                         //   Log.d("submit NetworkError", providerId )

                        }

                        is Resource.DataError -> {
                            showLoading.postValue(false)
                            resource.errorResponse?.let { showServerError.postValue(it) }
                         //   Log.d("submit DataError", it.toString() )

                        }
                    }

//
//
//
//                    Log.d("submit data", providerId)
//                    Log.d("submit data",it.id )
//                    Log.d("submit data",downPayment.toString())
//                    Log.d("submit data",totalPrice.toString())
//                    Log.d("submit data",it.monthlyAmount.toString() )

                }
            }
        }
    }


    fun validateBooking(
        totalPrice: String
    ): Boolean {
        var valid = true
        if (totalPrice.isEmpty()) {
            _customOperationFrom.value = CarFormState(totalCostError = R.string.require_field)
            valid = false
        } else if (totalPrice == "0") {
            _customOperationFrom.value = CarFormState(totalCostError = R.string.greater_than_0)
            valid = false
        }
        return valid

    }

    fun addCarToCartRequest(price: String) {
        if (validateBooking(
                price
            )
        ) {
            selectedInstallmentType.value?.let {
                carRequestBody = newRequestBody(
                    providerId,
                    it.id,
                    downPayment,
                    totalPrice.toInt(),
                    it.monthlyAmount.toInt()

                )
                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.AddCategoryCarBookingToCart(carRequestBody)
                    ) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                carBookingResponse.value = it
                                //    Log.d("submit  customRequestResponse.value ",  customRequestResponse.value.toString()  )

                            }
                        }
                        is Resource.NetworkError -> {
                            showLoading.postValue(false)
                            //   Log.d("submit NetworkError", providerId )

                        }

                        is Resource.DataError -> {
                            showLoading.postValue(false)
                            resource.errorResponse?.let { showServerError.postValue(it) }
                            //   Log.d("submit DataError", it.toString() )

                        }
                    }

                }
            }
        }
    }

    private fun newRequestBody(
        providerId: String,
        installementTypeid: String,
        downPayment: Int,
        totalPrice: Int,
        monthlyAmount: Int
    ): CarCategoryRequestBody {

        carRequestBody.type = CartType.CARS.id //type.....
        carRequestBody.downPayment = downPayment//downpayment....
        carRequestBody.installmentTypeId = installementTypeid.toInt() //installment type....
        carRequestBody.totalCost = totalPrice  //total cost.....
        carRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        carRequestBody.providerId = providerId
        carRequestBody.cartUID = Constants.cartUID//auto generate id to cart..
        carRequestBody.itemUID = randomUUID()  //auto generate id to item ...
        carRequestBody.monthlyInstallment=  selectedInstallmentType.value!!.monthlyAmount.toInt()


        // service .....
        var service = CarService(areaId = null, carBrandId  =null)
        carRequestBody.carService = service

        return carRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()

}