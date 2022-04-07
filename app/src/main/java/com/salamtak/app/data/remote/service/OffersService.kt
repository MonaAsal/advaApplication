package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.OfferHistory
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface OffersService {

    @GET("medicalServices/offers/{providerId}")
    suspend fun getOffersByProvider(
        @Path("providerId") providerId: String, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<OffersResponse>

    @GET("medicalServices/availableMedicalProviders")
    suspend fun getOffersProviders(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Response<OffersProvidersResponse>

    @GET("discount/history")
    suspend fun getOffersUsage(@Query("page") page: Int,
                               @Query("pagesize") pageSize: Int): Response<SalamtakListResponse<OfferHistory>>

    @GET("discount/code/{id}")
    suspend fun getQrCode(@Path("id") id: String): Response<SalamtakResponse<QrData>>


//    @DELETE("PatientProfiles/medical/{id}")
//    suspend fun deleteChronicDisease(@Path("id") id: String): Response<BaseResponse>
//
//    @GET("MedicalProviders/{providerId}")
//    suspend fun getMedicalProviderInfo(
//        @Path("providerId") providerId: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int
//    )


}
