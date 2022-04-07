package com.salamtak.app.data.remote

import android.content.Intent
import com.salamtak.app.App
import com.salamtak.app.BuildConfig
import com.salamtak.app.data.Session
import com.salamtak.app.ui.component.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
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
                Authorization,//ظظ"salamtak_\$2a\$10\$ZSkA1sdNh8t9jV2ijgr4Ne427pZY3oTer.66ZoSa7GZr9EzIHGSpm"
                Session.xAccessToken
            )
            .method(originalRequest.method, originalRequest.body)

        if (Session.xAccessToken.isEmpty()
        ) {
            var intent = Intent(App.context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            App.context.startActivity(intent)
        }

        val response = chain.proceed(authorisedRequestBuilder.build())
        return response
    }
}