package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface HomeService {

    @GET("MobileHome/categories")
    suspend fun getHomeFeaturedCategories(
    ): Response<FeaturedCategoriesResponse>

    @GET("MobileHome/Advertisements")
    suspend fun getAdvertisements(
    ): Response<AdvertisementsResponse>

    @GET("MobileHome/Promotions")
    suspend fun getPromotions(
    ): Response<PromotionsResponse>

    @GET("MobileHome/TopProviders")
    suspend fun getTopProviders(
    ): Response<TopProvidersResponse>

}
