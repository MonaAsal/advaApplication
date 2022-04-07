package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.*
import retrofit2.Response
import retrofit2.http.*

interface CartService {

    //add to cart health category...
    @POST("carts/add-item")
    suspend fun addCategoryHealthBookingToCart(@Body requestBody: HealthCategoryRequestBody): Response<ActionResponse>

    //add to cart health custom...
    @POST("carts/add-item")
    suspend fun addCustomHealthBookingToCart(@Body requestBody: HealthCustomRequestBody): Response<ActionResponse>

    //add to cart finishing category...
    @POST("carts/add-item")
    suspend fun addCategoryFinishingBookingToCart(@Body requestBody: FinishingCategoryRequestBody): Response<ActionResponse>

    //add to cart finishing custom...
    @POST("carts/add-item")
    suspend fun addCustomFinishingBookingToCart(@Body requestBody: FinishingCustomRequestBody): Response<ActionResponse>

    //add to cart car category...
    @POST("carts/add-item")
    suspend fun addCategoryCarBookingToCart(@Body requestBody: CarCategoryRequestBody): Response<ActionResponse>

    //add to cart car custom...
    @POST("carts/add-item")
    suspend fun addCustomCarBookingToCart(@Body requestBody: CarCustomRequestBody): Response<ActionResponse>

    //add to cart education...
    @POST("carts/add-item")
    suspend fun addCustomEducationBookingToCart(@Body requestBody: EducationCustomRequestBody): Response<ActionResponse>

    //add to cart education...
    @POST("carts/add-item")
    suspend fun addEducationBookingToCart(@Body requestBody: EducationRequestBody): Response<ActionResponse>


    //add to cart wedding...
    @POST("carts/add-item")
    suspend fun addCustomWeddingBookingToCart(@Body requestBody: WeddingRequestBody): Response<ActionResponse>

    //add insurance item to cart
    @POST("carts/add-item")
    suspend fun addCustomWeddingBookingToCart(@Body requestBody: InsuranceRequestBody): Response<ActionResponse>

    // get my cart
    @GET("carts/my-cart")
    suspend fun GetCartData(  @Query("cartUID") cartUID: String?): Response<GetCartResponse>

    // delete item from my cart
    @DELETE("carts/{itemId}")
    suspend fun deleteCartItem(  @Path("itemId") ItemID: String?): Response<BaseResponse>

    // checkout cart
    @FormUrlEncoded
    @POST("carts/checkout")
    suspend fun checkoutCart(@Field("cartUID") cartUID: String?): Response<BaseResponse>

    // get my cart
    @GET("carts/my-cart-count")
    suspend fun getCartCount(  @Query("cartUID") cartUID: String?): Response<cartCountResponse>


}

