package com.salamtak.app.ui.component.login

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.gson.Gson
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoogleSignInManager(
    val activity: AppCompatActivity,
    val listener: SocialLoginListener,
    val serverClientId: String,
    val dialog: BaseBottomSheetDialog? = null
) {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    val RC_SIGN_IN = 1

    init {
        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        // this key is working on the dev app
        mGoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(
                Scope(Scopes.DRIVE_APPFOLDER)
            ).requestServerAuthCode(serverClientId)
                .requestEmail()
                .requestIdToken(serverClientId)
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, mGoogleSignInOptions)
    }


    fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        dialog?.let {
            dialog.startActivityForResult(signInIntent, RC_SIGN_IN)
        } ?: activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    fun handleGoogleSignInData(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.e("account", Gson().toJson(account))

            Log.e("displayName", account.displayName!!)
            Log.e("email", account.email!!)
            Log.e("photoUrl", account.photoUrl!!.toString())
            Log.e("familyName", account.familyName!!)
            Log.e("givenName", account.givenName!!)
            Log.e("idToken", account.idToken!!)
            Log.e("serverAuthCode", account.serverAuthCode!!)
            Log.e("id", account.id!!)

            val scope = "oauth2:https://www.googleapis.com/auth/userinfo.profile"

            activity.lifecycleScope.launch(Dispatchers.IO) {
                val oauthToken: String = GoogleAuthUtil.getToken(activity, account.account!!, scope)


                listener.authWithGoogle(oauthToken)
            }

        } catch (e: ApiException) {
            e.printStackTrace()
        }


//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        try {
//            val account = task.getResult(ApiException::class.java)
////            val scope = "oauth2:https://www.googleapis.com/auth/userinfo.profile"
////            activity.lifecycleScope.launch(Dispatchers.IO) {
////                val oauthToken: String = GoogleAuthUtil.getToken(activity, account.account!!, scope)
////                listener.authWithGoogle(oauthToken)
////            }
//            Log.e("account", Gson().toJson(account))
//
//            Log.e("displayName", account.displayName!!)
//            Log.e("email", account.email!!)
//            Log.e("photoUrl", account.photoUrl!!.toString())
//            Log.e("familyName", account.familyName!!)
//            Log.e("givenName", account.givenName!!)
//            Log.e("idToken", account.idToken!!)
//            Log.e("serverAuthCode", account.serverAuthCode!!)
//            Log.e("id", account.id!!)
//        } catch (e: ApiException) {
//            e.printStackTrace()
//        }

    }

}

interface SocialLoginListener {
    fun authWithGoogle(oAuth2Token: String)
}
