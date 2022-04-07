package com.salamtak.app.data

import android.util.Log
import com.google.gson.Gson
import com.salamtak.app.App.Companion.context
import com.salamtak.app.data.entities.User
import com.salamtak.app.data.local.SharedPrefHelper
import java.util.*

// Made is as a Util Function so later on it would used whenever an Exchanging Rate is required
// Of course we can change currencyRateMapping -> fetch it from Room or the last saved CurrencyConversionList

object Session {

    var xAccessToken: String
    var xRefreshToken: String
    var xLang: String

    private val AccessTokenKEY = "AccessToken"
    private val RefreshTokenKEY = "RefreshToken"
    private val KEY_LOCALE = "KEY_LOCALE"
    private val KEY_USER = "user"
    private val KEY_FORCE_UPDATE = "KEY_FORCE_UPDATE"
    private val KEY_PREFS2 = "salamtak.prefs2"

    init {
        xAccessToken =
            getAccessToken()
        xRefreshToken =
            getRefreshToken()
        xLang = getUserLocale()
    }

    fun setAccessToken(token: String?) {
        token?.let {
            SharedPrefHelper.setIntoSharedPref(
                context,
                AccessTokenKEY,
                it
            )
        }

        xAccessToken = token!!
    }

    fun getUserLocale(): String {
        return SharedPrefHelper.getFromSharedPref(
            context,
            KEY_LOCALE, Locale.getDefault().language, KEY_PREFS2
        ).toString()
    }

    fun setUserLocale(lang: String) {
        lang?.let {
            SharedPrefHelper.setIntoSharedPref(
                context,
                KEY_LOCALE,
                it, KEY_PREFS2
            )
        }

        xLang = lang!!
    }


    fun getAccessToken(): String {
        val accessToken = SharedPrefHelper.getFromSharedPref(
            context,
            AccessTokenKEY, ""
        )
            .toString()
        return accessToken
    }

    fun setRefreshToken(token: String?) {
        token?.let {
            SharedPrefHelper.setIntoSharedPref(
                context,
                RefreshTokenKEY,
                it
            )
        }

        xRefreshToken = token!!
    }

    private fun getRefreshToken(): String {
        val refreshToken = SharedPrefHelper.getFromSharedPref(
            context,
            RefreshTokenKEY, ""
        )
            .toString()
        return refreshToken
    }

    fun updateTokens(token: String, refreshToken: String) {
        var user = getUserInfo()
        user?.token = token
        user?.refreshToken = refreshToken
        saveUserInfo(user!!)
    }

    fun getUserInfo(): User? {
        val userItemJson = SharedPrefHelper.getFromSharedPref(context, KEY_USER, "")

        if (userItemJson!!.isNotEmpty()) {
            return Gson().fromJson(userItemJson, User::class.java)
        }

        return null
    }

    fun saveUserInfo(user: User) {
        val gson = Gson()
        SharedPrefHelper.setIntoSharedPref(context, KEY_USER, gson.toJson(user))
        Log.i("user", gson.toJson(user))

        setAccessToken(user?.token)
        setRefreshToken(user?.refreshToken)
    }


    fun clearSession() {
        setAccessToken("")
        setRefreshToken("")
        // setUserLocale("")
//        saveUserInfo(User())
    }

    fun saveForceUpdate(forceUpdate: Boolean) {
        SharedPrefHelper.setIntoSharedPref(context, KEY_FORCE_UPDATE, forceUpdate.toString())
    }

    fun getForceUpdate(): Boolean {
        val forceUpdate = SharedPrefHelper.getFromSharedPref(context, KEY_FORCE_UPDATE, "false")
        return forceUpdate!!.toBoolean()
    }


}