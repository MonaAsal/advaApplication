package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import org.mockito.internal.debugging.InvocationsPrinter
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface WeddingService {

    @FormUrlEncoded
    @POST("Book/CreateCustomWeddingBooking")
    suspend fun CreateCustomWeddingBooking(
        @Field("FullName") fullName: String,
        @Field("PhoneNumber") phoneNumber: String,
        @Field("Venue") venueName: String,
        @Field("InviteesNumber") inviteesNumber: Int,
        @Field("MonthlyInstallment") monthlyInstallment: String,
        @Field("DownPayment") downPayment: String,
        @Field("installmentTypeId") installmentTypeId: String,
        @Field("price") price: String
    ): Response<ActionResponse>

}
