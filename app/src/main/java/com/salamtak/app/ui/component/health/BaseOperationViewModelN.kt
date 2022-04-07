package com.salamtak.app.ui.component.health

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject

@HiltViewModel
open class BaseOperationViewModelN @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    //    var operationItem: MutableLiveData<Operation> = MutableLiveData()
//    var operationItem: MutableLiveData<OperationCardData> = MutableLiveData()
//    var selectedSalamtakOperation: MutableLiveData<SalamtakOperation> = MutableLiveData()
    var selectedSalamtakOperationN: SalamtakOperationsWithBranche? = null

    //    var operation: OperationN? = null
    var operationId: String = ""
    var downPayment: MutableLiveData<Double> = MutableLiveData()
    val addToFavouriteLiveData = MutableLiveData<ActionResponse>()

    var operationsResponse = MutableLiveData<SalamtakListResponse<Operation>>()

    var favouritesResponse = MutableLiveData<SalamtakListResponse<Operation>>()
    private val openOperationBookingPrivate = MutableLiveData<Event<SalamtakOperation>>()
    val openOperationBooking = openOperationBookingPrivate

    private val _openDoctorScreen = MutableLiveData<Event<String>>()
    val openDoctorScreen = _openDoctorScreen

    private val _openMedicalProviderScreen = MutableLiveData<Event<String>>()
    val openMedicalProviderScreen = _openMedicalProviderScreen

    private val _openReviewsScreen = MutableLiveData<Event<Operation>>()
    val openReviewsScreen = _openReviewsScreen
    val openMap = MutableLiveData<SalamtakOperation>()
    val showDetails = MutableLiveData<Operation>()

    fun getMaxAmountPerMonth(): Int {
        return getInstallmentPerMonth(getMinNumberOfMonths())
    }

    fun getMinAmountPerMonth(): Int {
        return getInstallmentPerMonth(getMaxNumberOfMonths())
    }


    fun getMinNumberOfMonths(): InstallmentType {
        return selectedSalamtakOperationN!!.installmentTypes.minByOrNull { it.numberOfMonths }
            ?: selectedSalamtakOperationN!!.installmentTypes[0]
//        return operationItem.value!!.installmentTypes[0]
    }

    fun getMaxNumberOfMonths(): InstallmentType {

        return selectedSalamtakOperationN!!.installmentTypes.maxByOrNull { it.numberOfMonths }
            ?: selectedSalamtakOperationN!!.installmentTypes[0]
//        var lastIndex = operationItem.value!!.installmentTypes.size - 1
//        return operationItem.value!!.installmentTypes[lastIndex]
    }

    fun getInstallmentPerMonth(type: InstallmentType): Int {
//        return (((operationItem.value?.price!! - calculateDownPayment()) *
//                (1 + ((type.vat) * type.numberOfMonths / 100))) / type.numberOfMonths).toInt()

        var operationPrice = selectedSalamtakOperationN!!.salamtakOperationPrice!!
        var operationDownPayment = (operationPrice * 10) / 100
        var remainingAmount = operationPrice - operationDownPayment
        var installmentPlan = type.numberOfMonths.toDouble()
        var interestPerMonth = 0.15 / 12
        var adminFeesPerMonth = 0.05 / 12

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


    fun calculateDownPayment(): Int {
        selectedSalamtakOperationN?.let {
            downPayment.value = it!!.salamtakOperationPrice!! * .10
            return downPayment.value!!.toInt()
        } ?: return 0

    }

    fun addToFavourite(id: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.postAddToFavourite(id)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        addToFavouriteLiveData.value = it
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


//    fun openOperationDetails(operation: OperationCardData) {
//        openOperationDetailsPrivate.value = Event(operation)
//    }

    fun onBookOperationClicked(
        operationCard: Operation,
        salamtakOperation: SalamtakOperation
    ) {
//        salamtakOperation.title = operationCard.operationName
//        salamtakOperation.imageUrl = operationCard.imageUrl
////        salamtakOperation.branch!!.medicalProvider = operationCard.medicalProviderOperations[0]..medicalProvider
////        salamtakOperation.branch!!.medicalProvider = operationCard.ownerMedicalProvider
//        selectedSalamtakOperationN = salamtakOperation
//        //operationItem.value = operationCard
////        var card = salamtakOperationSelected(operationCard, salamtakOperation)
//
//        openOperationBookingPrivate.value = Event(salamtakOperation)
    }

    private fun salamtakOperationSelected(
        operationCard: Operation,
        salamtakOperation: SalamtakOperation
    ): Operation {
//        operationCard.medicalProviderOperations.forEach {
//            it.salamtakOperations.forEach { it2 ->
//                if (it2.id == salamtakOperation.id) {
//                    it2.selected = true
//                } else
//                    it2.selected = false
////                it2.selected = it2.id == salamtakOperation.id
//            }
//        }
        return operationCard
    }

//    fun onBookOperationClicked(item: Operation) {
//        openOperationBookingPrivate.value = Event(item)
//    }

    fun onFavouriteClicked(item: Operation) {
        addToFavourite(item.id)
    }

//    fun shouldAddFinancialInfo(): Boolean {
//        return dataRepository.shouldAddFinancialInfo()
//    }

    fun onDoctorClicked(item: DoctorBase) {
        _openDoctorScreen.value = Event(item.id)
    }

    fun onDoctorClicked(item: Doctor) {
        _openDoctorScreen.value = Event(item.id)
    }

    fun onMedicalProviderClicked(item: Branch) {
//        _openMedicalProviderScreen.value = Event(item.medicalProvider?.id!!)
    }

    fun searchOperations(query: String, categoryId: String, areaId: String) {

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.searchOperations(
                    query,
                    categoryId,
                    areaId,
                    0,
                    Constants.PAGE_SIZE
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
//                        operationsResponse.value = it
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

    fun openOperationReviews(operation: Operation) {
        _openReviewsScreen.value = Event(operation)
    }

    fun onLocationClicked(item: SalamtakOperation) {
        openMap.value = item
    }

//    fun selectSalamtakOperation(operation: SalamtakOperation) {
//        selectedSalamtakOperation.value = operation
//    }

//    fun getSelectedSalamtakOperation(): SalamtakOperation {
//        return selectedSalamtakOperation.value!!
//    }

    fun showHideDetails(operation: Operation, flag: Boolean) {
        if (flag) {
            showDetails.value = operation
        }

    }

    //    fun getLocale(): Locale {
//        return dataRepository.getLocale()
//    }
    fun getLocale(): Locale {
        return Locale(getLocaleString())
    }

    fun getLocaleString(): String {
        return dataRepository.getLocale()
    }


}