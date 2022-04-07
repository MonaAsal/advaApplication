package com.salamtak.app.ui.component.splash

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.gettingstarted.GettingStartedActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.Constants.INSTANCE.DEVICE_ID
import com.salamtak.app.utils.Event
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    val splashViewModel: SplashViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_splash

    private val downloadManager: DownloadManager? = null
    private var downloadReference: Long = 0

    override fun initializeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideNotificationBar()
        super.onCreate(savedInstanceState)
        Log.d("DeviceId", DEVICE_ID)

        splashViewModel.runWorkManagerService()
        splashViewModel.outputWorkInfos.observe(this, workInfosObserver())

//        throw RuntimeException("Test Crash") // Force a crash

        checkForceUpdateRemoteConfig()

        Handler().postDelayed({
            splashViewModel.checkNavigationScreen()
        }, 2500)

    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        val TAG = "WorkerService"

        return Observer { listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished) {
                Log.e(TAG, "isFinished")

                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                val output = workInfo.outputData.getString(Constants.KEY_WORKER_INPUT)
                if (!output.isNullOrEmpty()) {
                    splashViewModel.input.value = "input1"
                }
            } else {
                Log.e(TAG, "inProgress")
            }
        }


    }


//    override fun onResume() {
//        super.onResume()
//        showVideo()

//        Glide.with(this).asGif().load(R.raw.splash_anim).into(iv_logo)
//        Handler().postDelayed({
//            splashViewModel.checkNavigationScreen()
//        }, 2500)
//    }

//    fun showVideo() {
//
//        val video = Uri.parse(
//            "android.resource://" + packageName + "/"
//                    //+ R.raw.salmtak_splash
//                    + R.raw.splash_video
//        )
//
//        if (video_view != null) {
//            video_view.setVideoURI(video)
//            video_view.setZOrderOnTop(true)
//            video_view.setZOrderOnTop(true)
//            video_view.setOnPreparedListener(OnPreparedListener { mp ->
//                video_view.start()
//            })
//            video_view.setOnCompletionListener {
//
//                splashViewModel.checkNavigationScreen()
//            }
////            video_view.start()
//        }
//
//    }

    // Returns an intent object that you use to check for an update.

    private var mAppUpdateManager: AppUpdateManager? = null
    private val RC_APP_UPDATE = 999
    private var inAppUpdateType = AppUpdateType.IMMEDIATE
    private var appUpdateInfoTask: Task<AppUpdateInfo>? = null
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null


    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            //check if the broadcast message is for our Enqueued download
            val referenceId =
                intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadReference === referenceId) {
                Log.v("LOG_TAG", "Downloading of the new app version complete")
                //start the installation of the latest version
                val installIntent = Intent(Intent.ACTION_VIEW)
                installIntent.setDataAndType(
                    downloadManager?.getUriForDownloadedFile(downloadReference),
                    "application/vnd.android.package-archive"
                )
                installIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(installIntent)
            }
        }
    }


    private fun updateVersion() {

        val appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                Log.e("success", "success")
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    RC_APP_UPDATE
                )
            }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_APP_UPDATE) {
            //when user clicks update button
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this@SplashActivity, "App download starts...", Toast.LENGTH_LONG)
                    .show()
            } else if (resultCode != Activity.RESULT_CANCELED) {
                //if you want to request the update again just call checkUpdate()
                Toast.makeText(this@SplashActivity, "App download canceled.", Toast.LENGTH_LONG)
                    .show()
            } else if (resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                Toast.makeText(this@SplashActivity, "App download failed.", Toast.LENGTH_LONG)
                    .show()
            }
        }


    }

    fun popupSnackbarForCompleteUpdate() {

        var snackbar =
            Snackbar.make(
                findViewById(R.id.main_layout),
                "New app is ready!",
                Snackbar.LENGTH_INDEFINITE
            );

        snackbar.setAction("Install") {
            if (mAppUpdateManager != null) {
                mAppUpdateManager?.completeUpdate();
            }
        }


        snackbar.setActionTextColor(resources.getColor(R.color.red));
        snackbar.show()
    }

    private fun inAppUpdate() {
        try {
            // Checks that the platform will allow the specified type of update.
            appUpdateInfoTask!!.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(inAppUpdateType)
                ) {
                    // Request the update.
                    try {
                        mAppUpdateManager?.startUpdateFlowForResult( // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,  // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            inAppUpdateType,  // The current activity making the update request.
                            this@SplashActivity,  // Include a request code to later monitor this update request.
                            RC_APP_UPDATE
                        )
                    } catch (ignored: SendIntentException) {
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun openHome(event: Event<Any>) {
        //navigateToMainScreen()
        navigateToMainScreen()
    }


    private fun openGettingStarted(event: Event<Any>) {
        startActivity<GettingStartedActivity>()
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    override fun observeViewModel() {
        observeToast(splashViewModel.showToast)
        observeError(splashViewModel.showError)
        observeEvent(splashViewModel.openHome, ::openHome)
        observeEvent(splashViewModel.openLogin, ::navigateToRegisterScreen)
        observeEvent(splashViewModel.openGettingStarted, ::openGettingStarted)
        observe(splashViewModel.openVerify, ::openVerifyScreen)
    }

    private fun openVerifyScreen(phone: String) {
        navigateToVerifyNumberScreen(phone)
    }


    class VersionChecker :
        AsyncTask<String?, String?, String?>() {
        private var newVersion: String? = null
        override fun doInBackground(vararg params: String?): String? {
            try {
                newVersion =
                    Jsoup.connect("https://play.google.com/store/apps/details?id=com.salamtak.app&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div:containsOwn(Current Version)")
                        .next()
                        .text()

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return newVersion
        }
    }
}