package com.salamtak.app.data.remote.service

import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface OperationsService {
    @GET("categories")
    suspend fun fetchCategories(): Response<SalamtakListResponse<Category>>

    @GET("categories/{id}/operations")
    suspend fun fetchCategoryDetails(
        @Path("id") categoryId: Int, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int, @Query("orderByAlphabetical") alphabetical: String
    ): Response<SalamtakListResponse<Operation>>

    @GET("categories/{id}/operations/grouped")
    suspend fun fetchCategoryOperations(
        @Path("id") categoryId: Int, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int, @Query("orderByAlphabetical") alphabetical: String
    ): Response<SalamtakListResponse<Operation>>

    @FormUrlEncoded
    @POST("Book/CreateOperationBooking")
    suspend fun postBookOperation(
        @Field("DownPayment") downPayment: Int,
        @Field("SalamtakOperationId") operationId: String,
        @Field("InstallmentTypeId") installmentTypeId: String
    ): Response<BaseResponse>


    @FormUrlEncoded
    @POST("patients/favourites")
    suspend fun postAddToFavourite(@Field("OperationId") operationId: String): Response<ActionResponse>

    @GET("Book/patientv2")
    suspend fun getBookedOperation(
        @Query("page") page: Int, @Query("pageSize") pageSize: Int
    ): Response<SalamtakListResponse<BookedOperation>>

    @GET("operations/search")
    suspend fun searchOperations(
        @Query("query") query: String,
        @Query("categoryId") categoryId: String,
        @Query("areaId") areaId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("orderByAlphabetical") alphabetical: String
    ): Response<SalamtakListResponse<Operation>>

    @FormUrlEncoded
    @POST("book/cancelv2")
    suspend fun cancelOperation(@Field("id") id: String): Response<ActionResponse>

    @GET("MedicalProviders/{providerId}/doctors")
    suspend fun getMedicalProviderDoctors(
        @Path("providerId") providerId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int
    ): Response<DoctorsResponse>

    @FormUrlEncoded
    @POST("reviews/booking")
    suspend fun addReview(
        @Field("RequestId") requestId: String,
        @Field("ExperienceRate") experienceRate: Int,
        @Field("DoctorRate") doctorRate: Int,
        @Field("MedicalProviderRate") medicalProviderRate: Int,
        @Field("Review") review: String
    ): Response<ActionResponse>

    //@GET("reviews/booking/operations/{operationId}")
    @GET("categories/operations/reviews/{operationId}")
    suspend fun getOperationReviews(
        @Path("operationId") operationId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int
    ): Response<ReviewsResponse>

    @GET("reviews/booking/doctors/{doctorId}")
    suspend fun getDoctorReviews(
        @Path("doctorId") doctorId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int
    ): Response<ReviewsResponse>


    @GET("reviews/booking/medicalProviders/{providerId}")
    suspend fun getProviderReviews(
        @Path("providerId") providerId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int
    ): Response<ReviewsResponse>


    @FormUrlEncoded
    @POST("Book/CreateCustomOperationBooking")
    suspend fun bookCustomOperation(
        @Field("FullName") FullName: String,
        @Field("PhoneNumber") Phone: String,
        @Field("CustomeDoctorName") DoctorName: String,
        @Field("CustomeOperationName") OperationName: String,
        @Field("CategoryId") CategoryId: Int,
        @Field("InstallmentTypeId") InstallmentTypeId: String,
        @Field("MonthlyInstallment") MonthlyInstallment: Double,
        @Field("DownPayment") DownPayment: Double,
        @Field("TotalCost") TotalCost: Double
    ): Response<BaseResponse>


    @GET("book/custom/lookups")
    suspend fun getCustomOperationLookups(): Response<CustomOperationLookupsResponse>

    //@GET("doctors/simple")
    @GET("doctors/recommended")
    suspend fun getDoctors(
        @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<MedicalNetwork>>

    @GET("medicalProviders/contracted")
    suspend fun getMedicalProviders(
        @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<MedicalNetwork>>

    @GET("categories/getSubcategories/{categoryId}")
    suspend fun getSubCategories(
        @Path("categoryId") categoryId: String, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<SubCategory>>


    @GET("Categories/getSubcategoriesv2")
    suspend fun getNewSubCategories(
        @Query("id") categoryId: Int, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<SubCategoryModel>>


//    @GET("Operations/getOperationDetails/{id}")
//    suspend fun getOperationDetails(
//        @Path("id") operationId: Int
//    ): Response<SalamtakResponse<OperationN>>


    @GET("Categories/getSubCategoryOperations/{id}")
    suspend fun getSubCategoryOperations(
        @Path("id") subCategoryId: Int,
        @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<Operation>>

    @GET("Operations/getOperationDetails/{id}")
    suspend fun getOperationDetails(
        @Path("id") operationId: String
    ): Response<SalamtakResponse<OperationDetails>>


    @GET("Operations/searchHealth")
    suspend fun searchHealth(
        @Query("query") query: String, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<Operation>>

    @GET("operations/lastestAdded")
    suspend fun getLatestAdded(
        @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<MedicalNetwork>>

    //        @Query("categoryId") categoryId: String,
//        @Query("subCategoryId") subCategoryId: String,
    @GET("Operations/HealthAdvancedSearch")
    suspend fun healthAdvancedSearch(
        @Query("categoryId") categoryId: String?,
        @Query("subCategoryId") subCategoryId: String?,
        @Query("medicalProvider") medicalProvider: String,
        @Query("cityId") cityId: String?,
        @Query("startPrice") startPrice: String,
        @Query("endPrice") endPrice: String,
        @Query("Operation") Operation: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Response<SalamtakListResponse<Operation>>

    @GET("Operations/CategoryHealthAdvancedSearch")
    suspend fun categoryHealthAdvancedSearch(
        @Query("categoryId") categoryId: String?,
        @Query("subCategoryId") subCategoryId: String?,
        @Query("medicalProvider") medicalProvider: String,
        @Query("cityId") cityId: String?,
        @Query("startPrice") startPrice: String,
        @Query("endPrice") endPrice: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Response<SalamtakListResponse<Operation>>
}

