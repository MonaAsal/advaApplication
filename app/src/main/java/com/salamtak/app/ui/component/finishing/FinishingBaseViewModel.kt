package com.salamtak.app.ui.component.finishing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.InstallmentType
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
open class FinishingBaseViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseViewModel() {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var interestPerMonth = 0.11 / 12
    var adminFeesPerMonth = 0.05 / 12

    val categoriesLiveData = MutableLiveData<SalamtakListResponse<Category>>()

    var selectedInstallmentType: MutableLiveData<InstallmentType> = MutableLiveData()
    var categoryId = 0
    var customRequestResponse = MutableLiveData<BaseResponse>()

    val installmentTypes = MutableLiveData<List<InstallmentType>>()
    var downPayment = 0
    val downPaymentString = MutableLiveData<String>()
    var totalPrice = 0.0


    fun getInstallmentsLookup() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getInstallmentsLookup(InstallmentTypes.FINISHING.typeId.toString())) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        installmentTypes.value = it.finishingInstallments!!
                        selectInstallmentType(it.finishingInstallments[0])
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


    fun getInstallmentPerMonth(price: Double, numOfMonths: Int): Int {
        var tmpDownPayment = calcDownPayment(price.toString(), numOfMonths)
//        if (downPayment != 0.0)
//            tmpDownPayment = downPayment.toInt()

        return calculateMonthlyInstallment(
            price,
            tmpDownPayment.toDouble(),
            numOfMonths,
            interestPerMonth,
            adminFeesPerMonth,
        )
    }

    fun needFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun calcDownPayment(price: String, numOfMonths: Int): Int {

        var downPaymentTmp = (price.toDouble() * 10 / 100).toInt()

        if (numOfMonths == 36) {
            downPaymentTmp = (price.toDouble() * 15 / 100).toInt()
        }

        return downPaymentTmp

    }

    fun calcDownPaymentNUpdateUI(price: String): Int {
        selectedInstallmentType.value?.let {
            var downPaymentTmp = (price.toDouble() * 10 / 100).toInt()

            if (selectedInstallmentType.value!!.numberOfMonths == 36) {
                downPaymentTmp = (price.toDouble() * 15 / 100).toInt()
                downPaymentString.value = "15% = $downPaymentTmp"
            } else {
                downPaymentString.value = "10% = $downPaymentTmp"
            }

            downPayment = downPaymentTmp

            return downPayment
        } ?: return 0
    }

    fun getCategories(categoryId: Int) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCategories(
                    categoryId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        categoriesLiveData.value = it
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

    fun getLocale(): Locale {
        return Locale(dataRepository?.getLocale())
    }

    fun selectInstallmentType(type: InstallmentType) {
        selectedInstallmentType.value = type
        if (totalPrice != 0.0 && totalPrice > 0.0) {
            calcDownPaymentNUpdateUI(totalPrice.toString())
        }
    }

    fun onAmountChanged(price: String) {
        if (price.isNotEmpty()) {
            totalPrice = price.toDouble()

            if (price.toDouble() > 0.0) {
                calcDownPaymentNUpdateUI(price)
                recalculateMonthlyAmounts()
            }
        }
    }

    fun recalculateMonthlyAmounts() {
        installmentTypes.value?.let {
            var types = installmentTypes.value!!//.toMutableList()
            if (types.isNullOrEmpty().not()) {
                types.map { type ->
                    var amount =
                        getInstallmentPerMonth(
                            totalPrice,
                            //downPayment.toDouble(),
                            type.numberOfMonths
                        ).toDouble()
                    type.monthlyAmount = amount
                }
            }
            installmentTypes.value = types
        }
    }


}
