package com.salamtak.app.ui.component.biometriclogin

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.salamtak.app.R
import com.salamtak.app.utils.AvatarImageBehavior
import kotlin.reflect.KFunction0

object BiometricPromptUtils {
    private const val TAG = "BiometricPromptUtils"
    fun createBiometricPrompt(
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        cancelBehavior: KFunction0<Unit>
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                Log.e(TAG, "errCode is $errCode and errString is: $errString")
                if (errCode == 13 && errString == "Cancel")
                    cancelBehavior()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.e(
                    TAG, "User biometric rejected."
                )
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.e(TAG, "Authentication was successful")
                processSuccess(result)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return BiometricPrompt.PromptInfo.Builder().apply {
                setTitle(activity.getString(R.string.prompt_info_title))
                setSubtitle(activity.getString(R.string.prompt_info_subtitle))
//                setDescription(activity.getString(R.string.prompt_info_subtitle))
                setConfirmationRequired(false)
                setDeviceCredentialAllowed(true)
            }.build()
        else
            return BiometricPrompt.PromptInfo.Builder().apply {
                setTitle(activity.getString(R.string.prompt_info_title))
                setSubtitle(activity.getString(R.string.prompt_info_subtitle))
//                setDescription(activity.getString(R.string.prompt_info_subtitle))
                setConfirmationRequired(false)
                setNegativeButtonText(activity.getString(R.string.cancel))

            }.build()
    }
}