package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.CarModel
import com.salamtak.app.data.entities.City
import com.salamtak.app.data.entities.MedicalProfileLookupsResponse
import com.salamtak.app.data.entities.responses.ProfileLookupsResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface LookupsService {
//    @GET("cities")
//    suspend fun fetchCities(): Response<SalamtakListResponse<City>>

    @GET("PatientProfiles/medical/lookups")
    suspend fun fetchMedicalLookups(): Response<MedicalProfileLookupsResponse>

    @GET("PatientProfiles/employment/lookups")
    suspend fun fetchProfileLookups(): Response<ProfileLookupsResponse>

    @GET("lookups/car-brands")
    suspend fun getCarBrands(): Response<SalamtakListResponse<CarModel>>

    @GET("cities")
    suspend fun getAllCities(): Response<SalamtakListResponse<City>>

    @GET("cities/all-cities-for-car-filter")
    suspend fun getCitiesForCarFilter(): Response<SalamtakListResponse<City>>
}
