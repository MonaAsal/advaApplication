package com.salamtak.app.data.remote

import com.salamtak.app.BuildConfig
import com.salamtak.app.data.Session
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class FileInterceptor @Inject constructor() : Interceptor {
    private val CONTENT_TYPE = "Content-Type"
    private val Authorization = "Authorization"
    private val Language = "Accept-Language"
    private val CONTENT_TYPE_VALUE = "application/json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authorisedRequestBuilder = originalRequest.newBuilder()
            .header(CONTENT_TYPE, CONTENT_TYPE_VALUE)
            .addHeader("AppVersion", BuildConfig.APP_VERSION)
            .addHeader(Language, Session.xLang)
            .addHeader(
                Authorization, Session.xAccessToken
            )
            .method(originalRequest.method, originalRequest.body)

        val response = chain.proceed(authorisedRequestBuilder.build())
        return response
    }
}