package com.salamtak.app.ui.component.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject
constructor(private val dataRepository: DataRepository) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val CartResponse = MutableLiveData<GetCartResponse>()
    val CartCountResponse = MutableLiveData<cartCountResponse>()

    val DeleteItemResponse = MutableLiveData<BaseResponse>()
    val checkoutResponse = MutableLiveData<BaseResponse>()


    fun GetCartData(cartUID: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.GetCartData(cartUID)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        CartResponse.value = it.data!!
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

    fun deleteCartItem(itemId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.deleteCartItem(itemId)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        DeleteItemResponse.value = it.data!!
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

    fun checkoutCart(cardId: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.checkoutCart(cardId)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        checkoutResponse.value = it.data!!
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
    fun getCartCount(cartUID: String) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getCartCount(cartUID)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource?.let {
                        CartCountResponse.value = it.data!!
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
    fun needFinancialInfo(): Boolean {
        return dataRepository.needFinancialInfo()
    }

}