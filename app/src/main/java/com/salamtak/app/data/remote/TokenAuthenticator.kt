package com.salamtak.app.data.remote

import android.util.Log
import com.google.gson.Gson
import com.salamtak.app.App
import com.salamtak.app.data.Resource
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.ErrorModel
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.data.entities.responses.RefreshTokenResponse
import com.salamtak.app.data.error.Error
import com.salamtak.app.data.remote.service.NonAuthService
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Network
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val nonAuthService: NonAuthService
) :
    Authenticator {

    var count = 0
    override fun authenticate(route: Route?, response: Response): Request? {
        var token = ""
        runBlocking {
            try {
                val resource = refreshToken()
                Log.e("refresh", Gson().toJson(resource))
                token = resource.data?.data?.token!!
            } catch (e: Exception) {
            }
        }

        return response.request.newBuilder()
            .header(
                "Authorization",
                token
            )
            .build()
    }


    suspend fun refreshToken(
    ): Resource<RefreshTokenResponse> {

        count++
        if (count < 2) {
            return when (val response =
                processCall {
                    nonAuthService.refreshToken(
                        Session.xAccessToken,
                        Session.xRefreshToken
                    )
                }) {
                is RefreshTokenResponse -> {
                    if (response.data.token.isNotEmpty())
                        Session.updateTokens(response.data.token, response.data.refreshToken)
                    Resource.Success(data = response)
                }
                else -> {
                    Session.updateTokens("", "")
                    var error = ErrorModel("401", Constants.KEY_INVALID_TOKEN)
                    var list = ArrayList<ErrorModel>()
                    list.add(error)
                    var errorResponse = ErrorResponse()
                    errorResponse.status = false
                    errorResponse.errors = list
                    Resource.DataError(errorResponse)
                }
            }
        } else {
            var error = ErrorModel("401", Constants.KEY_INVALID_TOKEN)
            var list = ArrayList<ErrorModel>()
            list.add(error)
            var errorResponse = ErrorResponse()
            errorResponse.status = false
            errorResponse.errors = list
            return Resource.DataError(errorResponse)
        }
    }

    private suspend fun processCall(responseCall: suspend () -> retrofit2.Response<*>): Any? {
        if (!Network.isConnected(App.context)) {
            return Error.NO_INTERNET_CONNECTION
        }
        try {
            val response = responseCall.invoke()
//            val responseCode = response.code()
            if (response.isSuccessful) {
                return response.body()
            }
            val errorBody =
                response.errorBody()

            return try {
                val errorResponse =
                    Gson().fromJson<ErrorResponse>(
                        errorBody?.string(),
                        ErrorResponse::class.java
                    )
                errorResponse
            } catch (e: Exception) {
                Error.NETWORK_ERROR
            }
        } catch (e: IOException) {
            return Error.NETWORK_ERROR
        }
    }

}