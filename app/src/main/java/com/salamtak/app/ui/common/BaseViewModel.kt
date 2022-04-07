package com.salamtak.app.ui.common

import android.provider.Settings
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.salamtak.app.App
import com.salamtak.app.data.DataSource
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.RoundingMode
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
//open class BaseViewModel : ViewModel() {
open class BaseViewModel @Inject constructor() : ViewModel() {

//open class BaseViewModel @Inject constructor(val dataRepository: DataSource) : ViewModel() {
//@Inject constructor(val dataRepository: DataSource) :  ViewModel()


/**Inject Singlton ErrorManager
 * Use this errorManager to get the Errors
 */
open val errorManager: ErrorManager = ErrorManager(ErrorMapper())

val openHome = MutableLiveData<Event<Any>>()

val openLogin = MutableLiveData<Event<Any>>()
val openFinancialProfile = MutableLiveData<Event<Any>>()
val showLoading = MutableLiveData<Boolean>()

private val showToastPrivate = MutableLiveData<Event<Any>>()
val showToast: LiveData<Event<Any>> get() = showToastPrivate
var hireDate = MutableLiveData<String>()

/**
 * Error handling as UI
 */
private val showSnackBarPrivate = MutableLiveData<Event<Int>>()
val showSnackBar: LiveData<Event<Int>> get() = showSnackBarPrivate

//    private val _showError = MutableLiveData<Event<String>>()
val showError = MutableLiveData<Event<String>>()
val showServerError = MutableLiveData<ErrorResponse>()


fun getToastMessage(errorCode: Int): String {
    val error = errorManager?.getError(errorCode)
    //showToastPrivate.value = Event(error.description)
    return error?.description!!
}

fun showToastMessage(message: String) {
    showToastPrivate.value = Event(message)
}

fun showSnackbarMessage(@StringRes message: Int) {
    showSnackBarPrivate.value = Event(message)
}

//    fun showError(it: ErrorResponse) {
//        try {
//            _showError.value = Event(it.errors!![0]!!.error)
//            Toast.makeText(App.context, it.errors!![0]!!.error, Toast.LENGTH_LONG).show()
//            (App.context as BaseActivity).openSalamtakDialog(App.context.getString(R.string.error), it.errors!![0]!!.error, R.drawable.ic_warning)
//
//        } catch (e: Exception) {
//
//        }
//    }

fun setHireDate(strDate: String) {
    hireDate.value = strDate
}

fun calculateMonthlyInstallment(
    price: Double,
    downPayment: Double,
    numberOfMonth: Int,
    interestPerMonth: Double,
    adminFeesPerMonth: Double
): Int {
    var remainingAmount = price - downPayment

    if (price > 8000) {
        var firstEqHalf =
            ((remainingAmount * adminFeesPerMonth * numberOfMonth) + remainingAmount) //2
        var secondEqHalf = 1 + (numberOfMonth * interestPerMonth) //3
        var installmentValue = (firstEqHalf * secondEqHalf) / numberOfMonth

        return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
            .toInt()

    } else {
        var installmentValue =
            ((price * (1 + (numberOfMonth * adminFeesPerMonth))) - downPayment) * (1 + (numberOfMonth * interestPerMonth)) / numberOfMonth

        return installmentValue.toBigDecimal().setScale(0, RoundingMode.UP)
            .toInt()
    }
}

fun getDeviceId(): String {
    val deviceId =
        Settings.Secure.getString(App.context.contentResolver, Settings.Secure.ANDROID_ID)
    Log.i("deviceId", deviceId)
    return deviceId
}


}
