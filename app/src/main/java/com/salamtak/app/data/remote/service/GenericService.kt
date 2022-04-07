package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface GenericService {

    @GET("AdvaCategories/categories")
    suspend fun getCategories(@Query("maincategoryId") maincategoryId: Int): Response<SalamtakListResponse<Category>>

    @GET("book/getInstallmentsLookup")
    suspend fun getInstallmentsLookup(@Query("type") type: String): Response<SalamtakResponse<InstallmentTypesData>>


}
