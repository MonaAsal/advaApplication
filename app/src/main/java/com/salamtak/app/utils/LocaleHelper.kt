package com.salamtak.app.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.salamtak.app.data.Session
import java.util.*

class LocaleHelper {

    companion object {
        fun setLocale(context: Context, language: String): Context? {
            Session.setUserLocale(language)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else updateResourcesLegacy(context, language)
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun updateResources(context: Context, language: String): Context? {
            ///
//            val locale = Locale(language)
//            Locale.setDefault(locale)
//            val configuration = context.resources.configuration
//            configuration.setLocale(locale)
//            configuration.setLayoutDirection(locale)
//            return context.createConfigurationContext(configuration)

            val locale = Locale(language)
            Locale.setDefault(locale)

            val res: Resources = context.resources
            val config = Configuration(res.configuration)
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale)
                return context.createConfigurationContext(config)
            } else {
                config.locale = locale
                res.updateConfiguration(config, res.displayMetrics)
            }
            return context
        }

        private fun updateResourcesLegacy(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLayoutDirection(locale)
            }
            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }

        fun onAttach(context: Context): Context? {
            val lang =
                Session.getUserLocale()//getPersistedData(context, Locale.getDefault().language)
            return setLocale(context, lang)
        }

//        fun onAttach(context: Context, defaultLanguage: String): Context? {
//            val lang = getPersistedData(context, defaultLanguage)
//            return setLocale(context, lang)
//        }

//        private fun getPersistedData(context: Context, defaultLanguage: String): String {
//            return Session.getUserLocale()
//        }


    }

}