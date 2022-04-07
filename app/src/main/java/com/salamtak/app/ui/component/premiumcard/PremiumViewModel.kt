package com.salamtak.app.ui.component.health.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.R
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.TransactionDetails
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.ui.component.verifynumber.VerifyFormState
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class PremiumViewModel @Inject
constructor(val dataRepository: DataRepository) :
    BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    val votpResponse = MutableLiveData<BaseResponse>()
    val gotpResponse = MutableLiveData<PremiumData>()
    val gtrdResponse = MutableLiveData<SalamtakResponse<TransactionDetails>>()

    var booking: BookedOperation? = null
    var bookingId: String? = null
    var expiryDate: String? = null
    var cardNumber: String? = null
    var referenceNumber: String? = null
    var amount: String? = null
    private val _verifyForm = MutableLiveData<VerifyFormState>()

    val verifyForm: LiveData<VerifyFormState> = _verifyForm

    fun GOTP() {
        GOTP(
            cardNumber!!,
            expiryDate!!,
            bookingId!!
        )
    }

    fun GOTP(
        cardNumber: String,
        expiry: String,
        BookingId: String
    ) {
        this.cardNumber = cardNumber
        this.expiryDate = expiry
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.GOTP(
                    cardNumber, expiry, BookingId
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        gotpResponse.value = it.data
                        referenceNumber = it.data!!.referenceNumber

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


    fun GTRD(
        bookingId: String
    ) {
        referenceNumber?.let {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.GTRD(
                        it
                    )) {
                    is Resource.Success -> {
                        showLoading.postValue(false)
                        resource.data?.let {
                            gtrdResponse.value = it
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


    fun VOTP(
        otp: String,
        bookingId: String, referenceNumber: String
    ) {
        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.VOTP(
                    otp, bookingId, referenceNumber
                )) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        votpResponse.value = it
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

    fun verifyNumber(code: String) {
        if (code.length != 6) {
            _verifyForm.value =
                VerifyFormState(codeError = R.string.invalid_verification_code)
            return
        }

        _verifyForm.value =
            VerifyFormState(isDataValid = true)

        referenceNumber?.let {
            VOTP(code, bookingId!!, it)
        }
    }


}
