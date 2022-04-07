package com.salamtak.app.ui.common

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ProfileLookupsResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.usecase.LookupsUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LookupsViewModel @Inject constructor(private val lookupsUseCase: LookupsUseCase) :
    BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var categoryDetailsLiveData: MutableLiveData<Resource<SalamtakListResponse<Operation>>> =
        lookupsUseCase.categoryDetailsLiveData

    var categoryItem: MutableLiveData<Category> = MutableLiveData()
    var page: MutableLiveData<Int> = MutableLiveData()

    var categoriesList: MutableLiveData<List<Category>> = MutableLiveData()
    var citiesLiveData: MutableLiveData<Resource<SalamtakListResponse<City>>> =
        lookupsUseCase.citiesLiveData

    var medicalLookupsLiveData: MutableLiveData<Resource<MedicalProfileLookupsResponse>> =
        lookupsUseCase.medicalLookupsLiveData

    var profileLookupsLiveData: MutableLiveData<Resource<ProfileLookupsResponse>> =
        lookupsUseCase.profileLookupsLiveData

    lateinit var citiesNamesList: MutableList<String>
    lateinit var areasNamesList: MutableList<String>
    lateinit var residentialNames: MutableList<String>



    var selectedCategoryId: MutableLiveData<Int> = MutableLiveData()
    var selectedGender: MutableLiveData<Int> = MutableLiveData()
    var selectedCityEmployment: MutableLiveData<City> = MutableLiveData()
    var selectedAreaEmployment: MutableLiveData<Area> = MutableLiveData()

    var selectedCityBasic: MutableLiveData<City> = MutableLiveData()
    var selectedAreaBasic: MutableLiveData<Area> = MutableLiveData()
    var selectedResidentialStatus: MutableLiveData<ResidentalStatus> = MutableLiveData()

    fun getCities() {
        lookupsUseCase.getCities()
    }

    fun getProfileLookups() {
        lookupsUseCase.fetchProfileLookups()
    }


    fun selectCategoryAt(position: Int) {
        selectedCategoryId.value = categoriesList?.value!![position].id

    }

    fun selectCityAt(position: Int, isEmployment: Boolean) {
        try {
            Log.e("cities", citiesLiveData?.value?.data?.data!!.size.toString())
            if (citiesLiveData?.value != null && citiesLiveData?.value?.data?.data!!.size > position) {
                if (isEmployment)
                    selectedCityEmployment.value =
                        citiesLiveData?.value?.data?.data!![position]
                else
                    selectedCityBasic.value =
                        citiesLiveData?.value?.data?.data!![position]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectAreaAt(position: Int, isEmployment: Boolean) {
        try {
            if (isEmployment) {
                Log.e("areas", selectedCityEmployment.value!!.areas!!.size.toString())
                Log.e("selected area", selectedCityEmployment.value!!.areas!![position].name)
                if (selectedCityEmployment.value!!.areas != null && selectedCityEmployment.value!!.areas!!.size > position) {
                    selectedAreaEmployment.value =
                        selectedCityEmployment.value!!.areas?.get(position)
                }
            } else {
                Log.e("selected area", selectedCityBasic.value!!.areas!![position].name)
                if (selectedCityBasic.value!!.areas != null && selectedCityBasic.value!!.areas!!.size > position) {
                    selectedAreaBasic.value =
                        selectedCityBasic.value!!.areas?.get(position)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCityPosition(city: City): Int {
        var indx =
            citiesLiveData.value?.data?.data?.indexOfFirst { it.id == city.id }
        // Log.e("radwa", indx.toString())
        return indx ?: 0

    }

    fun getCityPosition(cityId: Int): Int {

        var indx =
            citiesLiveData.value?.data?.data?.indexOfFirst { it.id == cityId }
        // Log.e("radwa", indx.toString())
        return indx ?: 0

    }

    fun getCityPosition(area: Area): Int {

        for (city in citiesLiveData.value?.data?.data!!) {
            val index = city.areas!!.indexOfFirst { it.name == area.name }
            if (index != -1)
                return index
        }

        // Log.e("radwa", indx.toString())
        return 0

    }

    fun getAreaPosition(areaId: Int): Int {

        for (city in citiesLiveData!!.value?.data?.data!!) {
            val index = city.areas!!.indexOfFirst { it.id == areaId }
            if (index != -1)
                return index
        }

        // Log.e("radwa", indx.toString())
        return 0

    }

    fun getCityPosition(residentalStatus: ResidentalStatus): Int {
        if (::residentialNames.isInitialized) {
            var indx =
                residentialNames.indexOfFirst { it == residentalStatus.name }
            // Log.e("radwa", indx.toString())
            return indx ?: 0
        } else return 0
    }

    fun getMedicalProfileLookups() {
        lookupsUseCase.fetchMedicalLookups()
    }

    fun selectGender(gender: Int) {
        selectedGender.value = gender
    }

    fun selectResidentialStatusAt(position: Int, isEgyptian: Boolean) {
        if (profileLookupsLiveData?.value != null) {
            if (isEgyptian)
                selectedResidentialStatus.value =
                    profileLookupsLiveData?.value?.data?.data!!.egyptianResidentalStatus[position]
            else
                selectedResidentialStatus.value =
                    profileLookupsLiveData?.value?.data?.data!!.foreignResidentalStatus[position]
        }
    }
}