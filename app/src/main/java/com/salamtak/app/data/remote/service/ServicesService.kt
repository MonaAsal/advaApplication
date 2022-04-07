package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ServicesService {

    @GET("AdvaCategories/main-categories")
    suspend fun getServicesCategories(
    ): Response<ServicesCategoriesResponse>


}
