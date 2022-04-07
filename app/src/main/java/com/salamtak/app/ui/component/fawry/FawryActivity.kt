//package com.salamtak.app.ui.component.fawry
//
//import android.app.Activity
//import android.app.DownloadManager
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import com.emeint.android.fawryplugin.Plugininterfacing.FawrySdk
//import com.emeint.android.fawryplugin.Plugininterfacing.PayableItem
//import com.emeint.android.fawryplugin.exceptions.FawryException
//import com.emeint.android.fawryplugin.interfaces.FawrySdkCallback
//import com.emeint.android.fawryplugin.managers.FawryPluginAppClass
//import com.emeint.android.fawryplugin.models.FawryCardToken
//import com.emeint.android.fawryplugin.utils.UiUtils
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException
//import com.google.android.gms.common.GooglePlayServicesRepairableException
//import com.google.android.gms.common.GooglePlayServicesUtil
//import com.google.android.gms.security.ProviderInstaller
//import com.salamtak.app.R
//import com.salamtak.app.ui.ViewModelFactory
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.ui.component.splash.SplashViewModel
//import java.util.*
//import javax.inject.Inject
//
//
///**
// * Created by Radwa Elsahn on 3/21/2020
// */
//
//class FawryActivity : BaseActivity(), FawrySdkCallback {
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    @Inject
//    lateinit var splashViewModel: SplashViewModel
//
//    override val layoutId: Int
//        get() = R.layout.activity_splash
//
//    private val downloadManager: DownloadManager? = null
//    private var downloadReference: Long = 0
//
//    //fawry
//    private val PAYMENT_PLUGIN_REQUEST = 1023
//    private val CARD_TOKENIZER_REQUEST = 1024
//    private val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
//
//
//    override fun initializeViewModel() {
//        splashViewModel = viewModelFactory.create(splashViewModel::class.java)
//    }
//
//    override fun observeViewModel() {
//
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        hideNotificationBar()
//        super.onCreate(savedInstanceState)
//        FawrySdk.init(FawrySdk.Styles.STYLE2)
//        updateAndroidSecurityProvider(this)
//        initCardSDK()
//        initPaymentSDK()
//
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//    }
//
//
//    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
//        try {
//            ProviderInstaller.installIfNeeded(this)
//        } catch (e: GooglePlayServicesRepairableException) {
////             Thrown when Google Play Services is not installed, up-to-date, or enabled
////             Show dialog to allow users to install, update, or otherwise enable Google Play services.
//            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
//        } catch (e: GooglePlayServicesNotAvailableException) {
//            Log.e("SecurityException", "Google Play Services not available.")
//        }
//    }
//
//    fun initCardSDK() {
//        val merchantID = "1tSa6uxz2nQo8Y/WzKHb2Q=="
//        val serverUrl = "https://atfawry.fawrystaging.com"
//        val merchantRefNumber: String = randomAlphaNumeric(16)!!
//        val customerMobile = "01555553369"
//        val customerEmail = "admin@origin-me.com"
//        try {
//            FawrySdk.initializeCardTokenizer(
//                this,
//                serverUrl,
//                this,
//                merchantID,
//                merchantRefNumber,
//                customerMobile,
//                customerEmail,
//                FawrySdk.Language.EN,
//                CARD_TOKENIZER_REQUEST,
//                null,
//                UUID(1, 2)
//            )
//        } catch (e: FawryException) {
//            UiUtils.showDialog(this, e, false)
//            e.printStackTrace()
//        }
//    }
//
//    fun initPaymentSDK() {
//        val items: MutableList<PayableItem> = ArrayList()
//        val item = com.salamtak.app.ui.component.fawry.Item()
//        item.setPrice("50")
//        item.setDescription("test2")
//        item.setQty("40")
//        item.setSku("1")
//        items.add(item)
//        FawryPluginAppClass.enableLogging = false
//        val merchantID = "1tSa6uxz2nQo8Y/WzKHb2Q=="
//        val serverUrl = "https://atfawry.fawrystaging.com"
//        val merchantRefNumber: String = randomAlphaNumeric(16)!!
//        try {
//            FawrySdk.initialize(
//                this,
//                serverUrl,
//                this,
//                merchantID,
//                merchantRefNumber,
//                items,
//                FawrySdk.Language.EN,
//                PAYMENT_PLUGIN_REQUEST,
//                null,
//                UUID(1, 2)
//            )
//        } catch (e: FawryException) {
//            UiUtils.showDialog(this, e, false)
//            e.printStackTrace()
//        }
//    }
//
//
//    override fun onActivityResult(
//        requestCode: Int,
//        resultCode: Int,
//        data: Intent?
//    ) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PAYMENT_PLUGIN_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                val requestResult = data!!.getIntExtra(FawryPluginAppClass.REQUEST_RESULT, -1)
//                if (requestResult == FawryPluginAppClass.SUCCESS_CODE) {
//                } else if (requestResult == FawryPluginAppClass.FAILURE_CODE) {
//                }
//            } else {
//            }
//        } else if (requestCode == CARD_TOKENIZER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                val fawryCardToken: FawryCardToken =
//                    data!!.getParcelableExtra(FawryPluginAppClass.CARD_TOKEN_KEY)!!
//                UiUtils.showDialog(
//                    this, "Saving card result", """
//     token: ${fawryCardToken.token}
//
//     last four digits: ${fawryCardToken.lastFourDigits}
//     """.trimIndent(), "ok", null, null, null, true
//                )
//                Log.i("@@", "card tokenizer is succeed !")
//            } else {
//                Log.i("@@", "Error card tokenizer")
//            }
//        }
//    }
//
//    override fun paymentOperationSuccess(s: String, o: Any) {
//        Log.i("0", s + o.toString())
//    }
//
//    override fun paymentOperationFailure(s: String, o: Any) {
//        Log.i("1", s + o.toString())
//    }
//
////    fun randomAlphaNumeric(count: Int): String? {
////        var count = count
////        val builder = StringBuilder()
////        while (count-- != 0) {
////            val character =
////                (Math.random() * ALPHA_NUMERIC_STRING.length) as Int
////            builder.append(ALPHA_NUMERIC_STRING[character])
////        }
////        return builder.toString()
////    }
//
//    fun randomAlphaNumeric(count: Int): String {
//        var count = count
//        val builder = StringBuilder()
//        while (count-- != 0) {
//            val character = (Math.random() * ALPHA_NUMERIC_STRING.length).toInt()
//            builder.append(ALPHA_NUMERIC_STRING[character])
//        }
//        return builder.toString()
//    }
//
//}