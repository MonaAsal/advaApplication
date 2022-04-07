package com.salamtak.app.data.remote.service


import com.salamtak.app.data.entities.FacebookProfile
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.RefreshTokenResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import retrofit2.Response
import retrofit2.http.*


interface NonAuthService {

    @FormUrlEncoded
    @POST("userAccount/refresh")
    suspend fun refreshToken(
        @Field("Token") token: String,
        @Field("RefreshToken") refreshToken: String
    ): Response<RefreshTokenResponse>

    @FormUrlEncoded
    //@POST("patients/loginv2")
    @POST("patientsV2/login")
    suspend fun login(
        @Field("Username") username: String,
        @Field("Password") password: String,
        @Field("DeviceId") deviceId: String,
        @Field("FCMToken") fcmToken: String,
    ): Response<SalamtakResponse<User>>

    @FormUrlEncoded
//    @POST("patients/registerv2/account")
    @POST("patientsv2/register")
    suspend fun register(
        @Field("FirstName") firstName: String,
        @Field("LastName") lastName: String,
        @Field("Username") username: String,
        @Field("Phone") phone: String,
        @Field("Email") email: String,
        @Field("Image") image: String,
        @Field("Password") password: String,
        @Field("ConfirmPassword") confirmPassword: String,
        @Field("DeviceId") deviceId: String,
        @Field("FCMToken") fcmToken: String
    ): Response<SalamtakResponse<User>>



//    @POST("userAccount/verify")

    @FormUrlEncoded
    @POST("userAccount/verifyV2")
   suspend fun verifyNumber(
        @Field("VerifyType") VerifyType: Int,
        @Field("VerifyCode") VerifyCode: String,
        @Field("DeviceId") deviceId: String
    ): Response<SalamtakResponse<User>>


    @FormUrlEncoded
    @POST("userAccount/verify/request")
    suspend fun requestVerifyNumber(@Field("VerifyType") VerifyType: Int): Response<ActionResponse>


    // @POST("userAccount/recovery")
    @FormUrlEncoded
    @POST("userAccount/RecoveryV2")
    suspend fun forgotPassword(
        @Field("Username") email: String
    ): Response<ActionResponse>

    //@POST("userAccount/recovery/codev2")
    @FormUrlEncoded
    @POST("userAccount/password/reset")
    suspend fun forgotPasswordReset(
        @Field("username") id: String,
        @Field("ResetCode") ResetCode: String,
        @Field("Password") Password: String,
        @Field("ConfirmPassword") ConfirmPassword: String,
        @Field("deviceId") deviceId: String,
        @Field("fcmToken") fcmToken: String
    ): Response<SalamtakResponse<User>>


    @GET("https://graph.facebook.com/{PSID}?fields=first_name,last_name,profile_pic")
    suspend fun getFacebookProfileInfo(
        @Path("PSID") PSID: String,
        @Query("access_token") accessToken: String
    ): Response<FacebookProfile>

}