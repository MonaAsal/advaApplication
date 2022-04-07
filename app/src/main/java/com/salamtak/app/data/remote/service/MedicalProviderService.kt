package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface MedicalProviderService {
    @GET("MedicalProviders/getMedicalProviderDetails")
    suspend fun getMedicalProviderInfo(
        @Query("providerId") providerId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int
    ): Response<SalamtakResponse<MedicalProviderDetails>>

    @GET("Doctors/getDoctorInfoDetails")
    suspend fun getDoctorInfo(
        @Query("doctorId") doctorId: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<SalamtakResponse<DoctorDetails>>


    @GET("MedicalProviders/getProviderOperations/{providerId}")
    suspend fun getMedicalProviderOperations(
        @Path("providerId") providerId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int, @Query("orderByAlphabetical") alphabetical: String,
        @Query("categoryId") categoryId: String?
    ): Response<SalamtakListResponse<Operation>>


    @GET("doctors/getDoctorOperations/{doctorId}")
    suspend fun getDoctorOperations(
        @Path("doctorId") doctorId: String, @Query("page") page: Int, @Query(
            "pageSize"
        ) pageSize: Int, @Query("orderByAlphabetical") alphabetical: String,
        @Query("categoryId") categoryId: String?
    ): Response<SalamtakListResponse<Operation>>


//    @GET("Categories/getProviders/{categoryId}")
//    suspend fun getCategoryProviders(
//        @Path("categoryId") categoryId: Int, @Query("page") page: Int, @Query(
//            "pagesize"
//        ) pageSize: Int, @Query("filter") filter: String?
//    ): Response<SalamtakListResponse<MedicalProvider>>

    @GET("Categories/getProvidersv2")
    suspend fun getCategoryProviders(
        @Query("categoryId") categoryId: Int, @Query("page") page: Int, @Query("pagesize") pageSize: Int
    ): Response<SalamtakListResponse<MedicalProvider>>



    @GET("Doctors/getAllDoctorAndProvidersNames/{categoryId}")
    suspend fun getProvidersNames(@Path("categoryId") categoryId: String): Response<SalamtakListResponse<MedicalProvider>>

    @GET("Categories/PreHealthCategoryFilter/{categoryId}")
    suspend fun getPreHealthCategoryFilter(@Path("categoryId") categoryId: String): Response<SalamtakResponse<FilterData>>

    @GET("Categories/PreHealthFilter")
    suspend fun getPreHealthAdvancedFilter(): Response<SalamtakResponse<FilterData>>

    @GET("Categories/getSubCategoriesProviders/{subCategoryId}")
    suspend fun getMoreDoctors(
        @Path("subCategoryId") subCategoryId: String, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ):Response<SalamtakListResponse<MedicalProvider>>
}
