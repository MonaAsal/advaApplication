package com.salamtak.app.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPrefHelper {
    companion object {
        private val KEY_PREFS = "salamtak.prefs"
        private val KEY_PREFS2 = "adva.prefs2"


        fun setIntoSharedPref(context: Context?, key: String, value: String, prefName: String) {

            val sharedPref =
                context?.getSharedPreferences(
                    prefName,
                    Context.MODE_PRIVATE
                )
                    ?: return
            with(sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun getFromSharedPref(
            context: Context?,
            key: String,
            default: String,
            prefName: String
        ): String? {

            val sharedPref =
                context?.getSharedPreferences(
                    prefName,
                    Context.MODE_PRIVATE
                )
            return sharedPref?.getString(key, default)
        }


        fun setIntoSharedPref(context: Context?, key: String, value: String) {
            val sharedPref =
                context?.getSharedPreferences(
                    KEY_PREFS,
                    Context.MODE_PRIVATE
                )
                    ?: return
            with(sharedPref.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun getFromSharedPref(context: Context?, key: String): String? {

            val sharedPref =
                context?.getSharedPreferences(
                    KEY_PREFS,
                    Context.MODE_PRIVATE
                )
            return sharedPref?.getString(key, "")
        }

        fun getFromSharedPref(context: Context?, key: String, default: String): String? {

            val sharedPref =
                context?.getSharedPreferences(
                    KEY_PREFS,
                    Context.MODE_PRIVATE
                )
            return sharedPref?.getString(key, default)
        }

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC

        fun getFromSharedPref2(context: Context, key: String): String? {
            return try {
                val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
                val sharedPreferences = EncryptedSharedPreferences.create(
                    KEY_PREFS2,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

                sharedPreferences.getString(key, "")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
//            val sharedPref =
//                context?.getSharedPreferences(
//                    KEY_PREFS2,
//                    Context.MODE_PRIVATE
//                )
//            return sharedPref?.getString(key, "")
        }

        fun setIntoSharedPref2(context: Context, key: String, value: String) {
            val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
             val sharedPreferences = EncryptedSharedPreferences.create(
                KEY_PREFS2,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            sharedPreferences
                .edit()
                .putString(key, value)
                .apply()

//            val sharedPref =
//                context?.getSharedPreferences(
//                    KEY_PREFS2,
//                    Context.MODE_PRIVATE
//                )
//                    ?: return
//            with(sharedPref.edit()) {
//                putString(key, value)
//                apply()
//            }
        }


        fun clearCache(context: Context) {
            val sharedPref =
                context?.getSharedPreferences(
                    KEY_PREFS,
                    Context.MODE_PRIVATE
                )
            sharedPref.edit().clear().commit()
        }
    }


}