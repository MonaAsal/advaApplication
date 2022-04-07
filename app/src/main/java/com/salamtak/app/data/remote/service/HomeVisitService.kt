package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

interface HomeVisitService {
    @FormUrlEncoded
    @POST("HomeVisit/Payment")
    suspend fun homeVisitPayment(
        @Field("HomeVisitRequestId") homeVisitRequestId: String = "63437ebd-34ba-4ead-b190-5d3eb3047bf8",
        @Field("CardId") cardId: String = "43c0232e-f261-4a45-bc27-fcf95684b80c",
        @Field("PaymentMethodId") paymentMethodId: Int = 1
    ): Response<SalamtakResponse<BaseResponse>>


    @GET("HomeVisit/{id}")
    suspend fun getHomeVisit(@Path("id") requestId: String): Response<HomeVisitResponse>

    @GET("patients/homeVisits")
    suspend fun getMyHomeVisits(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Response<HomeVisitsResponse>

    @FormUrlEncoded
    @POST("HomeVisit/cancel")
    suspend fun cancelHomeVisit(@Field("id") id: String): Response<ActionResponse>

    @FormUrlEncoded
    @POST("HomeVisit")
    suspend fun addHomeVisit(
        @Field("DoctorSpecializationId") DoctorSpecializationId: Int,
        @Field("PreferredTimeId") PreferredTimeId: Int,
        @Field("IsForYou") IsForYou: Int, @Field("Name") Name: String, @Field("Age") Age: String,
        @Field("Notes") Notes: String, @Field("ContactNumber") ContactNumber: String,
        @Field("StreetAddress") StreetAddress: String, @Field("BuildingNum") BuildingNum: String,
        @Field("AppartmentNumber") AppartmentNumber: String, @Field("CityId") CityId: Int,
        @Field("AreaId") AreaId: String, @Field("Lat") Lat: Double,
        @Field("Lng") Lng: Double, @Field("PaymentMethodId") PaymentMethodId: Int, @Field("CardId") CardId: String
    ): Response<AddHomeVisitResponse>


    @GET("HomeVisit/preferredTimes")
    suspend fun getPreferredTimes(): Response<PreferredTimesResponse>

    @GET("doctors/specializations")
    suspend fun getDoctorSpecializations():Response<DoctorSpecializationsResponse>

    @FormUrlEncoded
    @POST("reviews/homeVisit")
    suspend fun addReview(@Field("RequestId") requestId:String,@Field("ExperienceRate") experienceRate:Int,@Field("DoctorRate") doctorRate:Int, @Field("Review") review:String):Response<ActionResponse>

}
