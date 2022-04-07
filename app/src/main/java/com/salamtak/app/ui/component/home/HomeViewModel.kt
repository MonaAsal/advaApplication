package com.salamtak.app.ui.component.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.FinancialProfileCompleted
import com.salamtak.app.data.entities.MedicalNetwork
import com.salamtak.app.data.entities.Providers
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class HomeViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val financialProfileCompleted = MutableLiveData<FinancialProfileCompleted>()

    val changeLanguageResponse = MutableLiveData<ActionResponse>()
    val latestAddedResponse = MutableLiveData<SalamtakListResponse<MedicalNetwork>>()
    val featuredCategoriesResponse = MutableLiveData<FeaturedCategoriesResponse>()
    val advertisementsResponse = MutableLiveData<AdvertisementsResponse>()
    val promotionsResponse = MutableLiveData<PromotionsResponse>()
    val topProvidersResponse = MutableLiveData<TopProvidersResponse>()
    val financialResponse = MutableLiveData<BaseResponse>()

    val openMedicalProviderProfile = MutableLiveData<Providers>()
    val openDoctorProfile = MutableLiveData<Providers>()


    fun shouldAddFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

    fun callChangeLanguage(lang: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.changeLanguage(lang)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        changeLanguageResponse.value = it
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

    fun isFinancialProfileCompleted() {
        try {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.isFinancialProfileCompleted()) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            financialProfileCompleted.value = it
                            dataRepository.saveNeedFinancialInfo(!it.isCompleted.not())
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserName(): String {
        try {
            return dataRepository.getUser()!!.firstName + " " + dataRepository.getUser()!!.lastName
        } catch (e: Exception) {
            return ""
        }
    }

    fun getUser(): User? {
        try {
            return dataRepository.getUser()
        } catch (e: Exception) {
            return null
        }
    }

    fun getLatestAdded() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getLatestAdded(1, 10)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        latestAddedResponse.value = it
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

    fun getFeaturedCategories() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getFeaturedCategories()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        featuredCategoriesResponse.value = it.data!!
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

    fun getAdvertisements() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getAdvertisements()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        advertisementsResponse.value = it.data!!
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

    fun getPromotions() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getPromotions()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        promotionsResponse.value = it.data!!
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

    fun getTopProviders() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getTopProviders()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        topProvidersResponse.value = it.data!!
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

//    fun updateSelectedLang() {
//        var locale = sharedUseCase.getLocale()
//        callChangeLanguage(locale.language!!)
//    }
    }

    fun getFinancialProgress() {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getFinancialProgress()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    financialResponse.value = resource.data!!
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

    fun getUserInfo(): User {
        return dataRepository!!.getUser()!!
    }

    fun handleProviderSelection(provider: Providers) {
        when (provider.type) {
            ProviderType.MedicalProvider.typeId ->
                openMedicalProviderProfile.value = provider
            ProviderType.Doctor.typeId ->
                openDoctorProfile.value = provider
        }

    }

}
