package com.salamtak.app.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.SalamtakOperation
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.data.local.SharedPrefHelper
import com.salamtak.app.ui.common.listeners.BaseView
import com.salamtak.app.ui.common.listeners.DialogCallBacks
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.ui.component.health.search.OperationsSearchActivity
import com.salamtak.app.ui.component.login.LoginActivity
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.paymentmethods.AddCardActivity
import com.salamtak.app.ui.component.profile.ProfileActivity
import com.salamtak.app.ui.component.register.RegisterActivity
import com.salamtak.app.ui.component.verifynumber.VerifyNumberActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_book_operation_n.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import kotlin.reflect.KFunction0


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected lateinit var baseViewModel: BaseViewModel

    abstract val layoutId: Int

//    val appThemeDark = true

    protected abstract fun initializeViewModel()
    abstract fun observeViewModel()

    var forceUpdate = false

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base!!))

        val config = resources.configuration
        App.context.resources.updateConfiguration(config, resources.displayMetrics)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
//        if (appThemeDark)
//            setTheme(R.style.AppThemeDark)
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(layoutId)


//        setUpFireBase()

//        initializeViewModel()
        observeViewModel()
        setUpFireBaseRemoteConfig()
    }

    override fun onResume() {
        super.onResume()
//        checkFireBaseShowSystemDown()
    }

    fun setUpFireBaseRemoteConfig() {
        App.mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }

        App.mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        App.mFirebaseRemoteConfig.setDefaultsAsync(R.xml.default_map)
    }


    fun checkFireBaseShowSystemDown() {

        App.mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            // If is successful, activated fetched
            if (task.isSuccessful) {
//                App.mFirebaseRemoteConfig.activate()
                var systemDown = App.mFirebaseRemoteConfig.getBoolean("showSystemDown")
                if (systemDown)
                    showSystemDownError()
            } else {
                Log.d("tag", "not activateFetched")
            }
        }
    }


//    // itemId: screen name
//    // itenName: button or action
//    fun LogFirebaseEvent(itemId: String, itemName: String, type: String) {
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
////        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type)
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//
//    }


    private fun initializeToolbar() {
        //supportActionBar?.hide()
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)


//        if (toolbar != null) {
//            setSupportActionBar(toolbar)
//          supportActionBar?.title = ""
//        }
    }

//    override fun setUpIconVisibility(visible: Boolean) {
//        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(visible)
//    }
//
//    override fun setTitle(titleKey: String) {
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            val title = findViewById<TextView>(R.id.txt_toolbar_title)
//            title?.text = titleKey
//        }
//    }

//    override fun setSettingsIconVisibility(visibility: Boolean) {
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            val icon = findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.ic_toolbar_notifications)
//            icon?.visibility = if (visibility) VISIBLE else GONE
//        }
//    }
//
//    override fun setRefreshVisibility(visibility: Boolean) {
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            val icon = findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.ic_toolbar_profile)
//            icon?.visibility = if (visibility) VISIBLE else GONE
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setAnimation() {
        if (Build.VERSION.SDK_INT > 21) {
            var slide = Slide()
            slide.setSlideEdge(Gravity.LEFT)
            slide.setDuration(400)
            slide.setInterpolator(DecelerateInterpolator())
            getWindow().exitTransition = slide
            getWindow().enterTransition = slide
        }
    }


    fun showLoadingView(progress_bar: ProgressBar) {
        progress_bar.toVisible()
//        tv_no_data.toGone()
//        rv_categories_list.toGone()
        //EspressoIdlingResource.increment()
    }

    fun showLoadingView(progress_bar: ProgressBar, show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    fun observeToast(event: LiveData<Event<Any>>) {
        if (event.value != null)
            showMessage(event.value.toString())
    }

    fun observeError(event: LiveData<Event<String>>) {

        if (event.value != null)
            showErrorMessage(event.value.toString())
    }

    fun showMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        getString(R.string.alert)
        openSalamtakDialog(supportFragmentManager, "", message, R.drawable.ic_warning)
    }

    fun showMessageNoIcon(title: String, message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        getString(R.string.alert)
        openSalamtakDialog(supportFragmentManager, title, message, R.drawable.bg_plaint_white)
    }


    fun showErrorMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        openSalamtakDialog(
            supportFragmentManager,
            getString(R.string.error),
            message,
            R.drawable.ic_server_error
        )
    }

    fun showServerErrorMessage(errorResponse: ErrorResponse) {

        if (errorResponse?.errors != null && errorResponse?.errors?.isNotEmpty()!!)
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.error),
                errorResponse?.errors?.get(0)?.error!!,
                R.drawable.ic_server_error
            )
    }

    fun showSystemDownError() {
        openSalamtakDialog(
            supportFragmentManager,
            getString(R.string.error),
            "SYSTEM DOWN",
            R.drawable.ic_server_error
        )
    }

    fun navigateToOperationBookingScreen(salamtakOperation: SalamtakOperation) {
        LogUtil.LogFirebaseEvent(
            "btn_book_now",
            "screen_" + this::class.java.simpleName,
            "operation",
            salamtakOperation.id
        )
        startActivity(
            intentFor<BookHealthRequestActivity>(
                Constants.KEY_OPERATION_ITEM to salamtakOperation
            )
        )
    }


//    fun navigateToOperationBookingScreen(operationItem: Operation) {
//        LogFirebaseEvent(
//            "btn_book_now",
//            "screen_" + this::class.java.simpleName,
//            "operation",
//            operationItem.id.toString()
//        )
//        startActivity(intentFor<BookOperationActivity>(Constants.OPERATION_ITEM_KEY to operationItem))
//    }


    fun navigateToMainScreen() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
        finish()
    }

    fun hideNotificationBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    fun navigateToVerifyNumberScreen(phone: String) {
        Log.e("phone", phone)

        startActivity(
            intentFor<VerifyNumberActivity>(
                Constants.KEY_PHONE to phone
            )
        )
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun navigateToFinancialInfoScreen() {
//        startActivity<FinancialInfoTypesActivity>()
        startActivity<FinancialInfoStep1Activity>()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun navigateToRegisterScreen(event: Event<Any>) {
        startActivity<RegisterActivity>()
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun navigateToLoginScreen(event: Event<Any>) {
        startActivity<LoginActivity>()
        finish()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun navigateToProfileScreen() {
        startActivity<ProfileActivity>()
    }

    fun handleShowMessage(event: Event<Any>) {
        showMessage(event.getContentIfNotHandled().toString())
    }

    fun openSearchScreen(query: String, category: Category) {
        startActivity(
            intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query,
                Constants.CATEGORY_ID_KEY to category
            )
        )
    }

    fun openSearchScreen(query: String) {
        startActivity(
            intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query
            )
        )
    }

    fun navigateToAddCardScreen(url: String, fromHomeVisit: Boolean) {
        startActivity(
            intentFor<AddCardActivity>(
                Constants.KEY_URL to url,
                Constants.KEY_FROM_HOME_VISIT to fromHomeVisit
            )
        )
    }

//    fun openSalamtakDialog(title: String, text: String, icon: Int) {
//
//        val fm = supportFragmentManager
//        val dialog = SalamtakDialog(null, true)
//
//        val bundle = Bundle()
//        bundle.putInt(Constants.KEY_ICON, icon)
//        bundle.putString(Constants.KEY_TEXT, text)
//        bundle.putString(Constants.KEY_TITLE, title)
//        dialog.arguments = bundle
//
//        dialog.show(fm, getString(R.string.app_name))
//
//    }

    fun showMessage(message: String, yesClicked: KFunction0<Unit>) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        val dialogCallBacks: DialogCallBacks =
            object : DialogCallBacks {
                override fun onOkClick() {
                    yesClicked()
                }

                override fun onNoClick() {
                }
            }
        //openSalamtakDialog(getString(R.string.alert), message, R.drawable.ic_warning)
        val fm = supportFragmentManager
        val dialog =
            SalamtakDialog(dialogCallBacks, true)
//        dialog.setCallBack(dialogCallBacks)

        val bundle = Bundle()
        bundle.putInt(Constants.KEY_ICON, R.drawable.ic_warning)
        bundle.putString(Constants.KEY_TEXT, message)
        bundle.putString(Constants.KEY_TITLE, "")

        dialog.arguments = bundle

        dialog.show(fm, getString(R.string.app_name))

    }

    fun showMessage(message: String, yesClicked: KFunction0<Unit>, noClicked: KFunction0<Unit>) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        val dialogCallBacks: DialogCallBacks =
            object : DialogCallBacks {
                override fun onOkClick() {
                    yesClicked()
                }

                override fun onNoClick() {
                    noClicked()
                }
            }
        //openSalamtakDialog(getString(R.string.alert), message, R.drawable.ic_warning)
        val fm = supportFragmentManager
        val dialog =
            SalamtakDialog(dialogCallBacks, false)
//        dialog.setCallBack(dialogCallBacks)

        val bundle = Bundle()
        bundle.putInt(Constants.KEY_ICON, R.drawable.ic_warning)
        bundle.putString(Constants.KEY_TEXT, message)
        bundle.putString(Constants.KEY_TITLE, "")

        dialog.arguments = bundle

        dialog.show(fm, getString(R.string.app_name))

    }


//    fun handleLoadingAndError(resource: Resource<Object>, viewModel: BaseViewModel) {
//        when (resource) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = viewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { viewModel.showError(it) }
//            }
//        }
//    }


    fun getRemoteConfig(key: String) {

        App.mFirebaseRemoteConfig.fetch()
            .addOnCompleteListener(this) { task ->
                // If is successful, activated fetched
                if (task.isSuccessful) {
                    App.mFirebaseRemoteConfig.fetchAndActivate()
                    var value = App.mFirebaseRemoteConfig.getBoolean(key)
                    SharedPrefHelper.setIntoSharedPref(
                        this,
                        key,
                        value.toString()
                    )

                } else {
                    Log.d("tag", "not activateFetched")
                }
            }
    }

    fun checkForceUpdateRemoteConfig() {

        App.mFirebaseRemoteConfig.fetch()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("TAG", "Config params updated: $updated")

                    var forceUpdate = App.mFirebaseRemoteConfig.getBoolean("forceUpdate")//"showUpdateAlert")
//                    var value = App.mFirebaseRemoteConfig.getBoolean("Test")//"showUpdateAlert")

                    Session.saveForceUpdate(forceUpdate)

                } else {

                }

            }
    }


    fun LogAdjustEvent(token: String) {
        val adjustEvent = AdjustEvent(token)
        Adjust.trackEvent(adjustEvent)
    }

    fun LogAdjustEvent(eventId: String, key: String, value: String) {
        val adjustEvent = AdjustEvent(eventId)
        adjustEvent.addCallbackParameter(key, value)
        Adjust.trackEvent(adjustEvent)
    }

    fun getRemaining(totalRecords: Int, page: Int): String {
        try {
            return if (page * Constants.PAGE_SIZE <= totalRecords)
                String.format(
                    getString(R.string.num_more_records),
                    (totalRecords - (page * Constants.PAGE_SIZE)).toString()
                )
            else
                getString(R.string.no_more_records)
        } catch (e: Exception) {
            return getString(R.string.no_more_records)
        }
    }

    fun deleteImageFromMobile(path: String) {
//        if (path.isNotEmpty()) {
//            val fdelete = File(path)
//            if (fdelete.exists()) {
//                if (fdelete.delete()) {
//                    System.out.println("file Deleted :" + path)
//                } else {
//                    System.out.println("file not Deleted :" + path)
//                }
//            }
//        }
    }


    fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + number)
        startActivity(intent)
    }

    fun clearBackStack(navController: NavController) {
        navController.popBackStack()
    }


    fun navigateToHome(navController: NavController) {
        navController.navigate(R.id.homeFragment)
    }

    fun navigateToServices(navController: NavController) {
        navController.navigate(R.id.servicesFragment)
    }

    fun navigateToRequests(navController: NavController) {
        navController.navigate(R.id.requestsFragment)
    }

    fun navigateToBooking(navController: NavController) {
        navController.navigate(R.id.bookingFragment)
    }

    fun navigateToProfile(navController: NavController) {
        navController.navigate(R.id.profileFragment)
    }

    fun navigateToEditProfile(navController: NavController) {
        navController.navigate(R.id.editProfileFragment)
    }

    fun navigateToWishList(navController: NavController) {
        navController.navigate(R.id.wishListFragment)
    }

    fun navigateToHealth(navController: NavController) {
        navController.navigate(R.id.healthFragment)
    }


}
