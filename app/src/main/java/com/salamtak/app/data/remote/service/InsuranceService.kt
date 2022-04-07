package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface InsuranceService {

    @FormUrlEncoded
    @POST("Book/CreateCustomInsuranceBooking")
    suspend fun CreateCustomInsuranceBooking(
        @Field("FullName") fullName: String,
        @Field("PhoneNumber") phoneNumber: String,
        @Field("InsuranceCompanyName") insuranceCompanyName: String,
        @Field("InsuranceType") insuranceType: Int,
        @Field("MonthlyInstallment") monthlyInstallment: String,
        @Field("DownPayment") downPayment: String,
        @Field("installmentTypeId") installmentTypeId: String,
        @Field("price") price: String
    ): Response<ActionResponse>

}
