package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface CarsService {

    @POST("Book/CreateCustomCarServiceBooking")
    suspend fun createCustomCarServiceBooking(@Body input: CarCustomRequestBookingInput): Response<ActionResponse>

//    @POST("Book/create-institute-booking")
//    suspend fun createCarServiceBooking(@Body input: CarServiceRequestBookingInput): Response<ActionResponse>

    //changed
    @POST("Book/CreateCarServiceBooking")
    suspend fun createCarServiceBooking(@Body input: CarServiceRequestBookingInput): Response<ActionResponse>

    @GET("CarServices/CarServiceCategories")
    suspend fun getCarServiceCenters(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("MainCategoryId") MainCategoryId: Int=5

        //  @Query("brands") brands: List<Int>?,
       // @Query("services") services: List<Int>?,
     //   @Query("locations") locations: List<Int>?,
     //   @Query("query") query: String?
    ): Response<SalamtakListResponse<CarCategoryModel>>

    @GET("UniverisitesAndInstitutes/GetCollegeById/{id}")
    suspend fun getCarServiceCenterDetails(@Path("id") id: String): Response<SalamtakResponse<CarServiceCenter>>

    //car provider details screen
    @GET("CarServices/get-details-by-id/{id}")
    suspend fun getCarProviderDetails(@Path("id") id: String): Response<SalamtakResponse<CarProviderDetails>>

    //car providers view all
    @GET("CarServices/GetProvidersByCategoryId")
    suspend fun getCarProvidersViewAll(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("CategoryId") CategoryId: Int): Response<SalamtakObjectListResponse<CarProvidersData>>

}
