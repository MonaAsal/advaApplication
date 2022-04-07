package com.salamtak.app.ui.component.carservices.custom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.CartType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.carservices.CarBaseViewModel
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
class CustomCarViewModel @Inject
constructor(dataRepository: DataRepository) :
    CarBaseViewModel(dataRepository) {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    private val _customRequestFrom = MutableLiveData<CarFormState>()
    val customFromState: LiveData<CarFormState> = _customRequestFrom

    val goToStep = MutableLiveData<Int>()
    val liveInput = MutableLiveData<CarCustomRequestBookingInput>()
    val citiesLiveData = MutableLiveData<SalamtakListResponse<City>>()
    val carBrandsLiveData = MutableLiveData<SalamtakListResponse<CarModel>>()


    var selectedCityBasic: City? = null
    var selectedAreaBasic: Area? = null
    var selectedCarBrand: CarModel? = null
    var providerName = ""

    lateinit var citiesNamesList: MutableList<String>
    lateinit var areasNamesList: MutableList<String>


    val customCarBookingResponse = MutableLiveData<BaseResponse>()
    var customCarRequestBody = CarCustomRequestBody()


    init {
//        getCategories(MainCategories.CARS.typeId)
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
                    CarFormState(nameError = R.string.require_field)
                return
            }
            phone.isEmpty() -> {
                _customRequestFrom.value =
                    CarFormState(phoneError = R.string.require_field)
                return
            }
            Validation.isValidPhone(phone).not() -> {
                _customRequestFrom.value =
                    CarFormState(phoneError = R.string.invalid_mobile_number)
                return
            }
            else -> {
                _customRequestFrom.value = null
                liveInput.value = dataRepository.getCustomCarInput()
                liveInput.value?.let {
                    var tmp = it
                    tmp.fullName = name
                    tmp.phoneNumber = phone

                    liveInput.value = tmp
                } ?: run {
                    liveInput.value = CarCustomRequestBookingInput(
                        null,
                        null,
                        null,
                        name,
                        phone,
                        null,
                        null,
                        null,
                        null,
                        selectedCarBrand?.let { it.id } ?: null,
                        selectedAreaBasic?.let { it.id } ?: null,
                        selectedCityBasic?.let { it.id } ?: null
                    )
                }

                dataRepository.saveCustomCarInput(liveInput.value)
                goToStep.value = 1
            }
        }
    }

    fun isStep1Completed(): Boolean {
        dataRepository.getCustomCarInput()?.let {
            return it.fullName.isNullOrEmpty().not()
        }

        return true
    }

    fun isStep2Completed(): Boolean {
        dataRepository.getCustomCarInput()?.let {
            return it.providerName.isNullOrEmpty().not()
        }

        return true
    }


    fun getCustomCarInput(): CarCustomRequestBookingInput {
        var input = dataRepository.getCustomCarInput()

        return if (input == null) {
            createStep1Input()
        } else {
            liveInput.value = input
            input
        }
    }

    private fun createStep1Input(): CarCustomRequestBookingInput {
        var input = CarCustomRequestBookingInput(
            null,
            null,
            null,
            getUserName(),
            getUser()!!.phone,
            null,
            null, null, null,
            selectedCarBrand?.let { it.id } ?: null,
            selectedAreaBasic?.let { it.id } ?: null,
            selectedCityBasic?.let { it.id } ?: null
        )
        dataRepository.saveCustomCarInput(input)

        liveInput.value = input

        return input
    }

    fun saveStep2Data(provider: String, phone: String, additionalInfo: String) {

        if (provider.isEmpty()) {
            _customRequestFrom.value =
                CarFormState(providerNameError = R.string.require_field)

            return
        } else if (phone.isEmpty()) {
            _customRequestFrom.value =
                CarFormState(phoneError = R.string.require_field)
            return
        } else if (Validation.isValidPhone(phone).not()) {
            _customRequestFrom.value =
                CarFormState(phoneError = R.string.invalid_mobile_number)
            return
        } else if (selectedCarBrand == null) {
            _customRequestFrom.value =
                CarFormState(carBrandError = R.string.require_field)
            return
        } else if (selectedCityBasic == null) {
            _customRequestFrom.value =
                CarFormState(cityError = R.string.require_field)
            return
        } else if (selectedAreaBasic == null) {
            _customRequestFrom.value =
                CarFormState(areaError = R.string.require_field)
            return
        }

        _customRequestFrom.value = null
        liveInput.value = dataRepository.getCustomCarInput()

        liveInput.value?.let {
            var tmp = it
            tmp.providerName = provider
            tmp.providerPhoneNumber = phone
            tmp.additionalInfo = additionalInfo
            tmp.carBrandId = selectedCarBrand?.id
            tmp.areaId = selectedAreaBasic?.id
            tmp.cityId = selectedCityBasic?.id

            liveInput.value = tmp
        } ?: run {
            liveInput.value = CarCustomRequestBookingInput(
                null,
                null,
                null,
                null,
                null,
                provider,
                null, phone, additionalInfo,
                selectedCarBrand?.let { it.id } ?: null,
                selectedAreaBasic?.let { it.id } ?: null,
                selectedCityBasic?.let { it.id } ?: null
            )
        }

        dataRepository.saveCustomCarInput(liveInput.value)
        goToStep.value = 2
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
        if (totalPrice.toString().isEmpty()) {
            _customRequestFrom.value =
                CarFormState(totalCostError = R.string.require_field)

            return
        } else if (totalPrice == 0.0) {
            _customRequestFrom.value =
                CarFormState(totalCostError = R.string.greater_than_0)
            return
        }

        selectedInstallmentType.value?.let { installmentType ->
            liveInput.value?.let {
                var tmp = it
                tmp.downPayment = downPayment.toString()
                tmp.installmentTypeId = installmentType.id
                tmp.monthlyInstallment = installmentType.monthlyAmount.toString()
                tmp.totalCost = totalPrice.toString()

                liveInput.value = tmp
                dataRepository.saveCustomCarInput(liveInput.value)
            }
            liveInput.value?.let {
                //postCustomCarRequest(it)
                addCustomCarBookingToCart(
                    it,
                    downPayment,
                    installmentType.id.toInt(),
                    installmentType.monthlyAmount.toInt(),
                    totalPrice.toInt()
                )
            }
        }
    }


    fun postCustomCarRequest(input: CarCustomRequestBookingInput) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.createCustomCarServiceBooking(
                    input
                )) {
                is Resource.Success -> {
                    resource.data?.let {
                        dataRepository.saveCustomCarInput(null)
                        liveInput.value = null
                        customRequestResponse.value = it
                    }
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

    fun onStep2Start() {
        getCarBrands()
        getCities()
    }

    fun getCities() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getAllCities(
                )) {
                is Resource.Success -> {
                    resource.data?.let {
                        citiesLiveData.value = it
                    }
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

    fun getCarBrands() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCarBrands(
                )) {
                is Resource.Success -> {
                    resource.data?.let {
                        carBrandsLiveData.value = it
                    }
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

    fun selectCityAt(position: Int) {
        try {
            if (position >= 0) {
                if (citiesLiveData?.value != null && citiesLiveData?.value?.data!!.size > position) {
                    selectedCityBasic =
                        citiesLiveData?.value?.data!![position]
                    Log.e("selectedCityBasic", selectedCityBasic!!.name)
                }
            } else selectedCityBasic = null
            selectedAreaBasic = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectAreaAt(position: Int) {
        try {
            if (position >= 0) {
                selectedCityBasic?.let { city ->
                    Log.e("selected area", city.areas!![position].name)
                    if (city.areas != null && city.areas!!.size > position) {
                        selectedAreaBasic =
                            city.areas?.get(position)
                        Log.e("selectedArea", selectedAreaBasic?.name!!)
                    }
                }
            } else selectedAreaBasic = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectCarBrandAt(position: Int) {
        try {
            if (position >= 0) {
                carBrandsLiveData.value?.let { brands ->
                    brands.data?.let {
                        selectedCarBrand =
                            brands.data?.get(position)
                        Log.e("selectedbrand", selectedCarBrand!!.name)
                    }
                }
            } else selectedCarBrand = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAreaPosition(areaId: Int): Int {
        selectedCityBasic?.let { city ->
            if (liveInput.value!!.cityId == city.id) {
                val index = city.areas!!.indexOfFirst { it.id == areaId }
                if (index != -1) {
                    selectedAreaBasic = city.areas!![index]
                    Log.e("selectedAreaBasic", selectedAreaBasic!!.name)
                    return index
                }
            }
        }
        // Log.e("radwa", indx.toString())
        return -1
    }

    fun getCityPosition(cityId: Int): Int {
        citiesLiveData.value?.let { cities ->
            var index =
                cities.data.indexOfFirst { it.id == cityId }
            selectedCityBasic = cities.data[index]
            Log.e("selectedCityBasic", selectedCityBasic!!.name)
            if (index != -1) {
                return index
            }

        }
        return -1
    }

    fun getCarBrandPosition(id: Int): Int {
        carBrandsLiveData!!.value?.data?.let { brands ->
            val index = brands!!.indexOfFirst { it.id == id }
            Log.e("radwa", index.toString())
            if (index != -1)
                return index
        }

        return 0
    }


    private fun addCustomCarBookingToCart(
        input: CarCustomRequestBookingInput,
        downPayment: Int,
        installementTypeId: Int,
        monthlyAmount: Int,
        totalprice: Int
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            customCarRequestBody = newRequestBody(input,downPayment, installementTypeId,monthlyAmount,totalprice)
            
            when (val resource =
                dataRepository.AddCustomCarBookingToCart(customCarRequestBody))
            {
                is Resource.Success -> {
                    resource.data?.let {
                        dataRepository.saveCustomCarInput(null)
                       // liveInput.value = null
                        customCarBookingResponse.value = it
                    }
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

    private fun newRequestBody(input: CarCustomRequestBookingInput,
                               downPayment: Int,
                               installementTypeId: Int,
                               monthlyAmount: Int,
                               totalprice: Int): CarCustomRequestBody {

        customCarRequestBody.type = CartType.CARS.id //type.....
        customCarRequestBody.installmentTypeId = installementTypeId //installment type....
        customCarRequestBody.downPayment = downPayment //downpayment....
        customCarRequestBody.totalCost = totalprice //total cost.....
        customCarRequestBody.deviceId = Constants.DEVICE_ID //device id .....
        customCarRequestBody.providerId =null
        customCarRequestBody.cartUID = Constants.cartUID //auto generate id to cart..
        customCarRequestBody.itemUID=  randomUUID()  //auto generate id to item ...
        customCarRequestBody.MonthlyInstallment=  monthlyAmount

        //custom .......
        providerName = input.providerName.toString()
        var custom = CarCustom(input.fullName,input.phoneNumber,
            input.providerName,input.providerPhoneNumber,monthlyAmount,input.additionalInfo)
        customCarRequestBody.custom = custom
        // service .....
        var service = CarService(areaId = input.areaId , carBrandId=input.carBrandId)
        customCarRequestBody.carService = service

        Log.d("customcarrequestbody",customCarRequestBody.toString() )
        return customCarRequestBody
    }

    fun randomUUID() = UUID.randomUUID().toString()

}
