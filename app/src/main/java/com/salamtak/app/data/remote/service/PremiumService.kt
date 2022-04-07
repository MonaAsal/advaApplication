package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.TransactionDetails
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.PremiumData
import com.salamtak.app.data.entities.responses.SalamtakResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PremiumService {
    @GET("PremiumCard/VOTP")
    suspend fun VOTP(
        @Query("otp") otp: String,
        @Query("bookingId") bookingId: String,
        @Query("referenceNumber") referenceNumber:String
    ): Response<BaseResponse>

    @GET("PremiumCard/GOTP")
    suspend fun GOTP(
        @Query("CardNumber") cardNumber: String,
        @Query("Expiry") expiry: String,
        @Query("bookingId") BookingId: String
    ): Response<SalamtakResponse<PremiumData>>

    @GET("PremiumCard/GTRD")
    suspend fun GTRD(
        @Query("referenceNumber") referenceNumber:String
        //@Query("bookingId") BookingId: String
    ): Response<SalamtakResponse<TransactionDetails>>

}