package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.PaymentMethodsResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 5/13/2020
 */

interface PaymentMethodsService {
    @GET("PatientProfiles/cards")
    suspend fun getPaymentMethods(): Response<PaymentMethodsResponse>


    @POST("HomeVisit/Payment/card")
    suspend fun addCard(): Response<ActionResponse>

    @DELETE("HomeVisit/Payment/card/{cardId}")
    suspend fun deleteCard(@Path("cardId") cardId: String): Response<ActionResponse>

    @FormUrlEncoded
    @POST("PatientProfiles/cards/default")
    suspend fun setDefaultCard(@Field("CardId") cardId: String): Response<ActionResponse>
}
