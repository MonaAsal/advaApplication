package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.RefreshTokenResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Radwa Elsahn on 3/24/2020
 */

interface UserAuthService {

    @FormUrlEncoded
    @POST("userAccount/refresh")
    suspend fun refreshToken(
        @Field("Token") token: String,
        @Field("RefreshToken") refreshToken: String
    ): Response<RefreshTokenResponse>

    //    @FormUrlEncoded
    @POST("userAccount/sessions/logout")
    suspend fun logout(): Response<ActionResponse>

    @FormUrlEncoded
    @POST("userAccount/password")
    suspend fun changePassword(
        @Field("OldPassword") OldPassword: String,
        @Field("Password") Password: String,
        @Field("ConfirmPassword") ConfirmPassword: String
    ): Response<ActionResponse>

//
//    @FormUrlEncoded
//    @POST("userAccount/firebase")
//    suspend fun updateFcmToken(
//        @Field("Token") Token: String
//    ): Response<ActionResponse>

    @FormUrlEncoded
    @POST("userAccount/firebasev2")
    suspend fun updateFcmToken(
        @Field("FCMToken") Token: String,
        @Field("DeviceId") deviceId: String,
        @Field("IsAllowed") isAllowed: Boolean,

        ): Response<ActionResponse>

//    @FormUrlEncoded
//    @POST("userAccount/refresh")
//    suspend fun refreshToken2(
//        @Field("Token") token: String,
//        @Field("RefreshToken") refreshToken: String
//    ): Call

}
