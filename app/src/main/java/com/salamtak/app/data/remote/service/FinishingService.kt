package com.salamtak.app.data.remote.service

import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface FinishingService {

    @POST("Book/CreateCustomFinishingBooking")
    suspend fun createCustomFinishingBooking(@Body input: FinishingCustomRequestBookingInput): Response<ActionResponse>


    @POST("Book/CreateFinishingBooking")
    suspend fun createFinishingBooking(@Body input: FinishingRequestBookingInput): Response<ActionResponse>

    //    @GET("Finishing/GetAllByCategoryId")
    @GET("Finishing/GetAllFinishingProviders")
    suspend fun getFinishingProviders(
        @Query("categoryId") categoryId: Int?,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("query") query: String,
        @Query("sort") sort: Int?
    ): Response<SalamtakObjectListResponse<FinishingProvidersData>>

    @GET("Finishing/Finishing-categories")
    suspend fun getFinishingCategories(@Query("page") page: Int, @Query("pagesize") pageSize: Int
    ): Response<SalamtakListResponse<FinishingCategoryModel>>


    @GET("Finishing/get-details-by-id/{id}")
    suspend fun getFinishingProviderDetails(@Path("id") id: String): Response<SalamtakResponse<FinishingProvider>>

    @GET("Finishing/get-package-details-by-id/{id}")
    suspend fun getPackageDetails(@Path("id") id: String):Response<SalamtakResponse<FinishingPackage>>
}
