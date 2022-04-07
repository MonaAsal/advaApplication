package com.salamtak.app.utils

import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.salamtak.app.App
import com.salamtak.app.BuildConfig

object LogUtil {

    fun initFirebaseLogger() {
        try {
            if (BuildConfig.IsDebug.not()) {
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
                FirebaseCrashlytics.getInstance().sendUnsentReports()
            } else {
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
            }
        } catch (ignored: Exception) {
        }
    }

    // It logs to firebase crashlytics if we pass exception as a parameter
    fun serverError(TAG: String, message: String, exception: Throwable? = null) {
//        try {
//            if (BuildConfig.IsDebug.not()) {
//                FirebaseCrashlytics.getInstance().log("$TAG//$message")
//                exception?.let { FirebaseCrashlytics.getInstance().recordException(it) }
//            }
//        } catch (ignored: Exception) {
//        }
    }

    fun serverError(message: String) {
//        try {
//            var TAG = "RemoteRepository"
//            var exception = Exception(message)
//            if (BuildConfig.IsDebug.not()) {
//                FirebaseCrashlytics.getInstance().log("$TAG//$message")
//                exception?.let { FirebaseCrashlytics.getInstance().recordException(it) }
//            }
//        } catch (ignored: Exception) {
//        }
    }


    fun LogFirebaseEvent(eventId: String, pageName: String) {

        val param = Bundle()
        param.putString("page_name", pageName)

        App.firebaseAnalytics.logEvent(eventId, param)
    }

    fun LogFirebaseEvent(
        eventId: String,
        pageName: String,
        selectedKey: String,
        valueId: String
    ) {

        val param = Bundle()
        param.putString("page_name", pageName)
        param.putString(selectedKey, valueId)

        App.firebaseAnalytics.logEvent(eventId, param)
    }

    fun LogFireBaseOpenEvent(screenName: String) {
        LogFirebaseEvent(
            "open_screen_$screenName",
            "screen_$screenName",
            "",
            ""
        )

    }

    fun LogFireBaseBackEvent(screenName: String) {
        LogFirebaseEvent(
            "back_screen",
            "screen_$screenName",
            "",
            ""
        )

    }


}