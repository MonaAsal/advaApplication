package com.salamtak.app.data.local

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.salamtak.app.App
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.*
import com.salamtak.app.utils.Constants
import java.lang.reflect.Type
import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class LocalRepository @Inject constructor() {

    private val KEY_USER = "user"
    private val KEY_DEFAULT_CARD_ID = "defaultCardId"
    private val KEY_DEVICEID = "deviceId"
    private val KEY_FCMTOKEN = "fcmToken"
    private val KEY_PROFILE = "profile"
    private val KEY_PREFS2 = "salamtak.prefs2"
    private val KEY_SEACH_KEY = "search_key"
    private val KEY_COUNTRY = "KEY_COUNTRY"
    private val KEY_EDUCATIONAL_LEVEL = "KEY_EDUCATIONAL_LEVEL"
    private val PLAYBACK_TIME = "play_time"
    private val KEY_IS_FIRST_TIME = "KEY_IS_FIRST_TIME"
    private val KEY_CATEGORIES = "KEY_CATEGORIES"
    private val KEY_NEED_FINANCIAL = "KEY_NEED_FINANCIAL"
    private val KEY_BOOKED_OPERATION = "KEY_BOOKED_OPERATION"
    private val KEY_FINANCIAL_ID = "KEY_FINANCIAL_ID"
    private val KEY_BIO_NAME = "KEY_BIO_NAME"
    private val KEY_BIO_PASSWORD = "KEY_BIO_PASSWORD"
    private val KEY_SELECTED_CITY= "KEY_SELECTED_CITY"


    init {
//        Session.xAccessToken = getAccessToken()!!
//        xLang = SharedPrefHelper.getFromSharedPref(App.context, KEY_LOCALE, "ar")!!
    }

//    fun setAccessToken(token: String?) {
//        Session.setAccessToken(token)
////        token?.let {
////            SharedPrefHelper.setIntoSharedPref(
////                App.context,
////                AccessTokenKEY,
////                it
////            )
////        }
////        Session.xAccessToken = token!!
//    }

//    fun getAccessToken(): String {
//        val accessToken = SharedPrefHelper.getFromSharedPref(
//            App.context,
//            AccessTokenKEY, ""
//        )
//            .toString()
//        return accessToken
//    }

//    fun setRefreshToken(token: String?) {
//        token?.let {
//            SharedPrefHelper.setIntoSharedPref(
//                App.context,
//                RefreshTokenKEY,
//                it
//            )
//        }
//
//        Session.xRefreshToken = token!!
//    }

//    fun getRefreshToken(): String {
//        val refreshToken = SharedPrefHelper.getFromSharedPref(
//            App.context,
//            RefreshTokenKEY, ""
//        )
//            .toString()
//        return refreshToken
//    }


    fun setLocale(locale: String) {
        Session.setUserLocale(locale)
//        SharedPrefHelper.setIntoSharedPref(
//            App.context,
//            KEY_LOCALE,
//            locale
//        )

//        xLang = locale
    }

    fun getLocale(): String {
        return Session.getUserLocale()
//        val locale = SharedPrefHelper.getFromSharedPref(
//            App.context,
//            KEY_LOCALE, Locale.getDefault().language
//        )
//            .toString()
//
//        xLang = locale
//        return locale
    }


    fun getUserInfo(): User? {
        val userItemJson = SharedPrefHelper.getFromSharedPref(App.context, KEY_USER, "")

        if (userItemJson!!.isNotEmpty()) {
            return Gson().fromJson(userItemJson, User::class.java)
        }

        return null
    }

    fun saveUserInfo(user: User) {
        Session.saveUserInfo(user)
//        val gson = Gson()
//        SharedPrefHelper.setIntoSharedPref(App.context, KEY_USER, gson.toJson(user))
//        Log.i("user", gson.toJson(user))
//
//        Session.setAccessToken(user?.token)
//        Session.setRefreshToken(user?.refreshToken)
//            userInfo = user
    }

    fun saveUser(user: User) {
        saveUserInfo(user)
//        val gson = Gson()
//        SharedPrefHelper.setIntoSharedPref(App.context, KEY_USER, gson.toJson(user))
//        Log.i("user", gson.toJson(user))
    }


    fun saveDefaultCard(cardId: String) {
        SharedPrefHelper.setIntoSharedPref(App.context, KEY_DEFAULT_CARD_ID, cardId)
    }

    fun getDefaultCardId(): String {
        return SharedPrefHelper.getFromSharedPref(App.context, KEY_DEFAULT_CARD_ID, "")!!
    }


    fun getFCMToken(): String {
        return SharedPrefHelper.getFromSharedPref(App.context, KEY_FCMTOKEN, "")!!
    }

    fun saveFCMToken(value: String) {
        SharedPrefHelper.setIntoSharedPref(App.context, KEY_FCMTOKEN, value)
    }


    fun isLogin(): Boolean {

        return Session.getAccessToken().isNotEmpty()
    }

    fun notFirstTime() {
        SharedPrefHelper.setIntoSharedPref(App.context, KEY_IS_FIRST_TIME, "false", KEY_PREFS2)
    }

    fun isFirstTime(): Boolean {
        return SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_IS_FIRST_TIME,
            "true", KEY_PREFS2
        )!!.toBoolean()
    }

    fun saveCategories(categories: List<Category>) {
        val gson = Gson()
        SharedPrefHelper.setIntoSharedPref(App.context, KEY_CATEGORIES, gson.toJson(categories))
        Log.d("categories", gson.toJson(categories))
    }


    fun getCategories(): List<Category>? {
        val categoriesJson = SharedPrefHelper.getFromSharedPref(App.context, KEY_CATEGORIES, "")

        if (categoriesJson!!.isNotEmpty()) {
            val categoryListType: Type = object :
                TypeToken<ArrayList<Category?>?>() {}.type

            return Gson().fromJson(categoriesJson, categoryListType)
        }

        return null
    }


    fun saveNeedFinancialInfo(hasInfo: Boolean) {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_NEED_FINANCIAL,
            hasInfo.toString()
        )
    }

    fun needFinancialInfo(): Boolean {
        var needInfo = false
        var needinfoStr = SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_NEED_FINANCIAL
        )

        needInfo = needinfoStr!!.toBoolean()

        val booked = bookedOperation()
        return booked && needInfo
    }

    fun bookedOoeration() {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_BOOKED_OPERATION,
            "true"
        )
    }

    fun bookedOperation(): Boolean {
        return SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_BOOKED_OPERATION, "false"
        )!!.toBoolean()
    }

    fun logout() {
        SharedPrefHelper.clearCache(App.context)
        Session.clearSession()
        deleteAllNotifications()
        dropTableNotifications()
    }

    fun toggleLocale(): String {
        return if (getLocale() == Constants.ENGLISH_LOCALE)
            Constants.ARABIC_LOCALE
        else
            Constants.ENGLISH_LOCALE
    }

    fun setVerified(mailVerified: Boolean, phoneVerified: Boolean) {
        var user = getUserInfo()
        user!!.isPhoneVerified = phoneVerified
        user!!.isMailVerified = mailVerified

        saveUserInfo(user)

    }

//    fun saveUserProfile(basicProfile: BasicProfile) {
//        var user = getUserInfo()
//        user!!.basicProfile = basicProfile
//
//        saveUser(user)
//    }

    fun saveNotification(notification: Notification) {

        val db = AppDatabase.getAppDataBase(App.context)
        db?.notificationDao()?.insertAll(notification)
    }

    fun dropTableNotifications() {
        AppDatabase.destroyDataBase()
    }

    fun deleteNotification(notification: Notification) {
        val db = AppDatabase.getAppDataBase(App.context)
        db?.notificationDao()?.delete(notification)
    }

    fun deleteAllNotifications() {
        val db = AppDatabase.getAppDataBase(App.context)
        db?.notificationDao()?.deleteAll()
    }

    fun getAllNotifications(): List<Notification> {
        val db = AppDatabase.getAppDataBase(App.context)
        var notifications = db?.notificationDao()?.getAll()!!.sortedByDescending { it.id }!!
        return notifications
    }
    fun getNotificationsCount(): Int {
        val db = AppDatabase.getAppDataBase(App.context)
        return db?.notificationDao()?.getAll()!!.size
    }

    fun saveUserFinancialData(key: String, value: HashMap<String, String>) {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            key,
            Gson().toJson(value)
        )

    }

    fun saveUserFinancialImages(key: String, value: List<FinancialTypeAttachments>) {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            key,
            Gson().toJson(value)
        )

    }

    fun getFinancialData(key: String): HashMap<String, String>? {
        val value = SharedPrefHelper.getFromSharedPref(
            App.context,
            key
        )

        val type: Type = object :
            TypeToken<HashMap<String, String>>() {}.type

        return if (value != null)
            Gson().fromJson(value, type)
        else
            null
    }

    fun getUserFinancialImages(key: String): List<FinancialTypeAttachments>? {
        val value = SharedPrefHelper.getFromSharedPref(
            App.context,
            key
        )

        val type: Type = object :
            TypeToken<List<FinancialTypeAttachments>>() {}.type
        return if (value != null)
            Gson().fromJson(value, type)
        else
            null
    }

    fun saveFinancialProfileId(id: String) {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_FINANCIAL_ID,
            Gson().toJson(id)
        )
    }

    fun getFinancialProfileId(): String {
        return SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_FINANCIAL_ID
        ) ?: ""
    }

    fun saveCredentials4Biometric(username: String, password: String) {
        SharedPrefHelper.setIntoSharedPref2(App.context, KEY_BIO_NAME, username)
        SharedPrefHelper.setIntoSharedPref2(App.context, KEY_BIO_PASSWORD, password)
    }

    fun getBioUserName(): String {
        var ss = SharedPrefHelper.getFromSharedPref2(
            App.context,
            KEY_BIO_NAME
        ) ?: ""
        return ss
    }

    fun getBioPassword(): String {
        var ss = SharedPrefHelper.getFromSharedPref2(
            App.context,
            KEY_BIO_PASSWORD
        ) ?: ""
        return ss
    }

    var KEY_FinishingCustomRequestBookingInput = "FinishingCustomRequestBookingInput"

    fun saveCustomFinishingInput(value: FinishingCustomRequestBookingInput?) {
        value?.let {
            Log.e("SaveCustomF", Gson().toJson(value).toString())
            SharedPrefHelper.setIntoSharedPref(
                App.context,
                KEY_FinishingCustomRequestBookingInput,
                Gson().toJson(value)
            )
        } ?: SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_FinishingCustomRequestBookingInput,
            ""
        )

    }

    fun getCustomFinishingInput(): FinishingCustomRequestBookingInput? {
        val str = SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_FinishingCustomRequestBookingInput,
            ""
        )

        if (str!!.isNotEmpty()) {
            return Gson().fromJson(str, FinishingCustomRequestBookingInput::class.java)
        }

        return null
    }

    val KEY_CARCustomRequestBookingInput = "KEY_CARCustomRequestBookingInput"
    fun saveCustomCarInput(value: CarCustomRequestBookingInput?) {
        value?.let {
            Log.e("SaveCustomF", Gson().toJson(value).toString())
            SharedPrefHelper.setIntoSharedPref(
                App.context,
                KEY_CARCustomRequestBookingInput,
                Gson().toJson(value)
            )
        } ?: SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_CARCustomRequestBookingInput,
            ""
        )

    }

    fun getCustomCarInput(): CarCustomRequestBookingInput? {
        val str = SharedPrefHelper.getFromSharedPref(
            App.context,
            KEY_CARCustomRequestBookingInput,
            ""
        )
        if (str!!.isNotEmpty()) {
            return Gson().fromJson(str, CarCustomRequestBookingInput::class.java)
        }

        return null
    }

    fun saveSelectedCity(value: City?) {
        SharedPrefHelper.setIntoSharedPref(
            App.context,
            KEY_SELECTED_CITY,
            Gson().toJson(value)
        )
    }

    fun getSelectedCity() : City?{
        val cityJson = SharedPrefHelper.getFromSharedPref(App.context, KEY_SELECTED_CITY, "")
        if (cityJson!!.isNotEmpty()) {
            return Gson().fromJson(cityJson, City::class.java)
        }

        return null

    }

}
