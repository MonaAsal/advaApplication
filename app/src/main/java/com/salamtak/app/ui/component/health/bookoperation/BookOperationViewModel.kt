package com.salamtak.app.ui.component.health.bookoperation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.health.BaseOperationViewModelN
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class BookOperationViewModel @Inject
constructor(private val dataRepository: DataRepository) :
    BaseOperationViewModelN(dataRepository) {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

//    var operationItem: MutableLiveData<Operation> = MutableLiveData()
//    var downPayment: MutableLiveData<Double> = MutableLiveData()

    val openSubmitted = MutableLiveData<Event<Any>>()
    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    val operationDetails = MutableLiveData<OperationDetails>()
    var selectedHospital: Hospital? = null
    val bookResponse = MutableLiveData<BaseResponse>()
    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    val healthbBookingResponse = MutableLiveData<BaseResponse>()
    var healthRequestBody = HealthCategoryRequestBody()


    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */

    var bookOperationLiveData = MutableLiveData<Resource<SalamtakResponse<Booking>>>()


    fun bookOperation(
    ) {
        if (dataRepository.isLogin()) {
//            if (operationUseCase.hasFinancialInfo()) {
//        operationUseCase.bookOperation(2000, "e7a32715-ffc6-454a-b33f-67a9b7dc2432", 1)
            selectedInstallmentType.value?.let {
                viewModelScope.launch {
                    showLoading.postValue(true)
                    when (val resource =
                        dataRepository.postBookOperation(
                            downPayment.value!!.toInt(),
                            selectedSalamtakOperationN!!.salamtakOperationId,
                            selectedInstallmentType.value!!.id
                        )) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                bookResponse.value = it
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
            } ?: run { Log.e("health", "no selected type") }


        } else {
            openLogin.value = Event(true)
        }
    }

    fun getOperationDetails(
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getOperationDetails(
                    operationId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        if (it.hospitals.isNotEmpty()) {
                            if (it.hospitals[0].salamtakOperationsWithBranches.isNotEmpty())
                                selectedSalamtakOperationN =
                                    it.hospitals[0].salamtakOperationsWithBranches[0]
                        }
                    }
//                    selectedSalamtakOperationN =
//                        resource.data!!.hospitals[0].salamtakOperationsWithBranches[0]
                    operationDetails.value = resource.data!!
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

    fun isFinancialProfileCompleted() {
        try {
            viewModelScope.launch {
//            showLoading.postValue(true)
                when (val resource =
                    dataRepository.isFinancialProfileCompleted()) {
                    is Resource.Success -> {
//                    showLoading.postValue(false)
                        resource.data?.let {
                            financialProfileCompleted.value = it
                        }
                    }
                    is Resource.NetworkError -> {
//                    showLoading.postValue(false)
                    }

                    is Resource.DataError -> {
//                    showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkNavigation() {
//        var needFinancialInfo = dataRepository.needFinancialInfo()
        openSubmitted.value = Event(financialProfileCompleted.value!!.isCompleted.not())
    }

    fun selectInstallmentType(type: InstallmentType) {
        selectedInstallmentType.value = type
    }


    fun selectHospital(position: Int) {
        operationDetails.value?.let {
            selectedHospital = it.hospitals[position]
        }
    }

    fun selectBranch(position: Int) {
        selectedHospital?.let {
            selectedSalamtakOperationN = it.salamtakOperationsWithBranches[position]
        }
    }


    fun addCategoryHealthBookingToCart() {

        if (dataRepository.isLogin()) {
            selectedInstallmentType.value?.let {
                viewModelScope.launch {
                    showLoading.postValue(true)

                    healthRequestBody = newRequestBody(
                        downPayment.value?.toInt() ?: 0,
                        selectedSalamtakOperationN!!.salamtakOperationId,
                        selectedInstallmentType.value!!.id)

                    when (val resource =
                        dataRepository.AddCategoryHealthBookingToCart(healthRequestBody)) {
                        is Resource.Success -> {
                            showLoading.postValue(false)
                            resource.data?.let {
                                healthbBookingResponse.value = it
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
            } ?: run { Log.e("health", "no selected type") }


        } else {
            openLogin.value = Event(true)
        }

    }

    private fun newRequestBody(
        downPayment: Int,
        operationId: String,
        installmentType: String
    ): HealthCategoryRequestBody {

        healthRequestBody.type = CartType.HEALTH.id //type.....
        healthRequestBody.downPayment = downPayment //downpayment....
        healthRequestBody.installmentTypeId = installmentType.toInt() //installment type....
        healthRequestBody.totalCost = 0   //total cost.....
        healthRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        healthRequestBody.providerId = operationId
        healthRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        healthRequestBody.itemUID = randomUUID()  //auto generate id to item ...
        healthRequestBody.monthlyInstallment=  selectedInstallmentType.value!!.monthlyAmount.toInt()

        //health service .....
        var operationName =  operationDetails.value?.operationName
        var service = HealthService(customeOperationName = operationName,categoryId =null)
        healthRequestBody.healthService = service

        return healthRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()
}
