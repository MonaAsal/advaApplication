package com.salamtak.app.ui.component.paymentmethods

import com.salamtak.app.data.entities.PaymentMethodCard
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.SharedUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */
@HiltViewModel
class PaymentMethodsViewModel @Inject
constructor(private val sharedUseCase: SharedUseCase) : BaseViewModel() {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var paymentMethodsResponse = sharedUseCase.paymentMethodsResponse
    var deletePaymentMethodsResponse = sharedUseCase.deletePaymentMethodsResponse
    var addCardResponse = sharedUseCase.addCardResponse

    fun getPaymentMethods() {
        return sharedUseCase.getPaymentMethods()
    }

    fun deletePaymentMethod(cardId: String) {
        return sharedUseCase.deletePaymentMethod(cardId)
    }

    fun toggleDefaultCard(position: Int) {
//        var item = paymentMethodsResponse.value!!.data?.data!![position]!!
//        paymentMethodsResponse.value!!.data?.data!![position]!!.isDefault.not()
//            if(paymentMethodsResponse.value!!.data?.data!![position]!!.isDefault)
//            {
//                paymentMethodsResponse.value!!.data?.data!!.
//            }

        if (paymentMethodsResponse.value!!.data?.data!![position]!!.isDefault) {
            paymentMethodsResponse.value!!.data?.data!![position].isDefault = false
        } else {// will make it default
            paymentMethodsResponse.value!!.data?.data!!?.forEach { it.isDefault = false }
            paymentMethodsResponse.value!!.data?.data!![position].isDefault = true
        }


//        var checked = diseases[position]?.hasThisChronic!!.not()
//        diseases[position]?.hasThisChronic = checked

        sharedUseCase.toggleDefaultPaymentMethod(
            paymentMethodsResponse.value!!.data?.data!![position].id,
            paymentMethodsResponse.value!!.data?.data!![position].isDefault
        )
    }

    fun getPaymentMethodsList(): List<PaymentMethodCard> {
        return paymentMethodsResponse.value!!.data?.data!!
    }

    fun addPaymentCard() {
        sharedUseCase.addCard()
    }

    fun checkIfSetToDefault(cardId: String) {
        var cardId = sharedUseCase.getDefaultCard()
        if (cardId.isEmpty()) {
            sharedUseCase.toggleDefaultPaymentMethod(cardId, true)
        }

    }
}
