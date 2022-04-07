package com.salamtak.app.ui.component

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.gson.Gson
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.login.GetUserCallback
import com.salamtak.app.ui.component.login.GoogleSignInManager
import com.salamtak.app.ui.component.login.SocialLoginListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.test.*
@AndroidEntryPoint
class TestActivity : BaseActivity(), GetUserCallback.IGetUserResponse, SocialLoginListener {

    override val layoutId: Int
        get() = R.layout.test

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    val RC_SIGN_IN = 1

    lateinit var googleSignInManager: GoogleSignInManager

    lateinit var callbackManager: CallbackManager

    override fun initializeViewModel() {

    }

    override fun observeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FacebookSdk.sdkInitialize(this)
        super.onCreate(savedInstanceState)
//        configureGoogleSignIn()
        sign_in_button.setOnClickListener {
            googleSignInN()
        }

        sign_in_fb.setOnClickListener {
            getfbdata()
        }

        //// facebook ///

    }

    private fun getProfileInfo(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { `object`, response ->
            Log.e("response", Gson().toJson(response))
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,first_name,last_name,profile_pic")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getfbdata() {
//        val intn = Intent(this@MainActivity, AfterLoginfb::class.java)
        callbackManager = CallbackManager.Factory.create()
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (isLoggedIn) {
//            startActivity(Intent(this@MainActivity, AfterLoginfb::class.java))
//            finish()
        }
        login_button_fb.setPermissions(listOf("email", "public_profile"))
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        login_button_fb.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
                Log.e("accessToken", loginResult!!.accessToken.token)
                var profile = Profile.getCurrentProfile()
                var accessToken = AccessToken.getCurrentAccessToken()
                Log.e("profile", profile.firstName)
                Log.e("profile", profile.lastName)
                Log.e("profile", profile.getProfilePictureUri(100, 100).toString())

                makeUserRequest(GetUserCallback(this@TestActivity).callback)

            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Login failed", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext, "Failed to Login", Toast.LENGTH_LONG).show()
                Log.e("Error", exception.toString())
            }
        })
    }

    private val ME_ENDPOINT = "/me"

    fun makeUserRequest(callback: GraphRequest.Callback?) {
        val params = Bundle()
        params.putString("fields", "picture,name,id,email,permissions,first_name,last_name")
        val request = GraphRequest(
            AccessToken.getCurrentAccessToken(), ME_ENDPOINT, params, HttpMethod.GET, callback
        )
        request.executeAsync()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (::googleSignInManager.isInitialized && requestCode == googleSignInManager.RC_SIGN_IN) {
            googleSignInManager.handleGoogleSignInData(data)
        }

//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                Log.e("account", Gson().toJson(account))
//
//                Log.e("displayName", account.displayName!!)
//                Log.e("email", account.email!!)
//                Log.e("photoUrl", account.photoUrl!!.toString())
//                Log.e("familyName", account.familyName!!)
//                Log.e("givenName", account.givenName!!)
//                Log.e("idToken", account.idToken!!)
//                Log.e("serverAuthCode", account.serverAuthCode!!)
//                Log.e("id", account.id!!)
//            } catch (e: ApiException) {
//                e.printStackTrace()
//            }
//        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data)
//        }
    }

    override fun onCompleted(user: GetUserCallback.User?) {
        Log.e("user", Gson().toJson(user!!))
    }

    //// new
    private fun googleSignInN() {
//        loginViewModel.setLoginMethodGoogle()
        Log.e("clientId",getString(R.string.default_web_client_id))
        googleSignInManager = GoogleSignInManager(this, this, getString(R.string.default_web_client_id))
        googleSignInManager.googleSignIn()
    }

    override fun authWithGoogle(oauthToken: String) {
        Log.e("authWithGoogle", oauthToken)
        //loginViewModel.callAuthGoogle(oauthToken, contentResolver.getDeviceId())
    }

}