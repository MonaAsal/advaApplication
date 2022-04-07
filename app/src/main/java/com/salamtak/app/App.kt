package com.salamtak.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.salamtak.app.utils.LocaleHelper
import com.salamtak.app.utils.LogUtil
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltAndroidApp
class App : Application(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        context = applicationContext

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

//        UXCam.startWithKey("d0qri5lzly0y88f")

//        initDagger()

        val appToken = BuildConfig.ADJUST_TOKEN
        var environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        if (BuildConfig.IS_PRODUCTION.not())
            environment = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, appToken, environment)
        config.setLogLevel(LogLevel.ERROR); // enable all logs

        LogUtil.initFirebaseLogger()
//        config.setAppSecret(secretId, info1, info2, info3, info4)

        config.setOnAttributionChangedListener {

        }
        Adjust.onCreate(config)

        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
        setUpFireBaseRemoteConfig()
//        checkInstallReferrer();

    }

    fun setUpFireBaseRemoteConfig() {
//        FirebaseApp.initializeApp(this)
        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_map)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.setUserId(getDeviceId())
    }

    fun getDeviceId(): String {
        val deviceId = Settings.Secure.getString(App.context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.i("deviceId", deviceId)
        return deviceId
    }

    override fun attachBaseContext(context1: Context) {
        context = context1
        super.attachBaseContext(LocaleHelper.onAttach(context1))
    }

    open fun initDagger() {
//        AppInjector.init(this)
//        DaggerAppComponent.builder().build().inject(this)
    }

//    override fun activityInjector() = dispatchingAndroidInjector
//    override fun serviceInjector() = dispatchingServiceInjector

    companion object {
        lateinit var context: Context
        lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
        lateinit var firebaseAnalytics: FirebaseAnalytics
    }

    private class AdjustLifecycleCallbacks : ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            Adjust.onResume()
        }

        override fun onActivityPaused(activity: Activity) {
            Adjust.onPause()
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityDestroyed(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e("AppClass", "onResume")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        LogUtil.LogFirebaseEvent(
            "onTrimMemory",
            this::class.java.simpleName
        )
        Log.e("AppClass", "onTrimMemory")
    }

    override fun onDestroy(owner: LifecycleOwner) {// not guaranteed to be called
        super.onDestroy(owner)
        LogUtil.LogFirebaseEvent(
            "onDestroy",
            this::class.java.simpleName
        )
        Log.e("AppClass", "onDestroy")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        LogUtil.LogFirebaseEvent(
            "onLowMemory",
            this::class.java.simpleName
        )
        Log.e("AppClass", "onLowMemory")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
//        LogUtil.LogFirebaseEvent(
//            "onPause",
//            this::class.java.simpleName
//        )
        Log.e("AppClass", "onPause")
        // do the termination here
    }
}
