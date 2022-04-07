package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface EducationService {

    @POST("Book/CreateCustomEduBooking")
    suspend fun CreateCustomEducationBooking(@Body input: EducationBookingInput): Response<ActionResponse>

    @GET("Schools/GetAllByCategoryId")
    suspend fun getAllEducationByCategoryId(
        @Query("categoryId") categoryId: Int, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakObjectListResponse<EducationSubcategoriesData>>


    @GET("Schools/GetAllBySubCategoryId")
    suspend fun getAllEducationBySubCategoryId(
        @Query("SubCategoryId") SubCategoryId: Int, @Query("page") page: Int, @Query(
            "pagesize"
        ) pageSize: Int
    ): Response<SalamtakListResponse<School>>

    @GET("Schools/GetSchoolDetailsById")
    suspend fun getSchoolDetailsById(@Query("schoolId") schoolId: String): Response<SalamtakResponse<SchoolDetails>>

    @POST("Book/create-school-booking")
    suspend fun createSchoolBooking(@Body input: SchoolRequestBookingInput): Response<ActionResponse>

    @POST("Book/create-University-booking")
    suspend fun createUniversityBooking(@Body input: UniversityRequestBookingInput): Response<ActionResponse>

    @POST("Book/create-institute-booking")
    suspend fun createInstituteBooking(@Body input: UniversityRequestBookingInput): Response<ActionResponse>

    @GET("UniverisitesAndInstitutes/GetAll")
    suspend fun getUniversitiesAndInstitutes(
        @Query("categoryId") categoryId: Int,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Response<SalamtakListResponse<University>>

    @GET("UniverisitesAndInstitutes/get-allEdu-provider-collages")
    suspend fun getCollages(
        @Query("id") id: String,
//        @Query("type") type: Int,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Response<SalamtakResponse<University>>


    @GET("UniverisitesAndInstitutes/GetCollegeById/{id}")
    suspend fun getCollageDetails(@Path("id") id: String): Response<SalamtakResponse<Collage>>


}
