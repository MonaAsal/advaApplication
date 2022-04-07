package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface MedicalProfileService {

    @FormUrlEncoded
    @PUT("PatientProfiles/medical")
    suspend fun postMedicalProfile(
        @Field("PatientBloodTypeId") PatientBloodTypeId: Int,
        @Field("Shareable") Shareable: Int,
        @Field("DateOfBirth") DateOfBirth: String,
        @Field("Weight") Weight: String,
        @Field("Height") Height: String
    ): Response<MedicalProfileResponse>

    @FormUrlEncoded
    @POST("PatientProfiles/medical/chronicDiseases")
    suspend fun postDiseases(@FieldMap ChronicDiseases: HashMap<String, String>): Response<ChronicDiseasesResponse>

    @GET("PatientProfiles/medical")
    suspend fun getMedicalProfile(): Response<MedicalProfileResponse>

    @DELETE("PatientProfiles/medical/{id}")
    suspend fun deleteChronicDisease(@Path("id") id: String): Response<BaseResponse>

}
