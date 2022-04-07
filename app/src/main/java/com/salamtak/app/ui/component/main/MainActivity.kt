package com.salamtak.app.ui.component.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.salamtak.app.BuildConfig
import com.salamtak.app.R
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.responses.cartCountResponse
import com.salamtak.app.data.local.SharedPrefHelper
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.cart.CartViewModel
import com.salamtak.app.ui.component.firebase.FirebaseViewModel
import com.salamtak.app.ui.component.home.HomeViewModel
import com.salamtak.app.ui.component.splash.SplashActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.openSalamtakDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.ExecutionException

class MainActivity : BaseActivity() {
    val homeViewModel: HomeViewModel by viewModels()
    lateinit var _liveChat: LiveChat
    private var appBarConfiguration: AppBarConfiguration? = null

    lateinit var navController: NavController
    var firebaseScreenName = "home_Screen"

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initializeViewModel() {

    }

    override fun observeViewModel() {
        observe(homeViewModel.showLoading, ::showLoadingView)
        observe(homeViewModel.showServerError, ::showServerErrorMessage)
//        observe(homeViewModel.financialProfileCompleted, ::handleFinancialProfileCompleted)
    }

    fun showLoadingView(show: Boolean) {
        //   showLoadingView(progress_bar, show)
    }

// keep it for now
//    private fun handleFinancialProfileCompleted(response: FinancialProfileCompleted) {
//        if (response.isCompleted.not())
//            tv_add_financial_profile.visibility = View.VISIBLE
//        else
//            tv_add_financial_profile.visibility = View.GONE
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupNavigationBar()
        liveChat()
        setOnClickListeners()
        checkForceUpdateRemoteConfig()
        checkForNewUpdate()
        logLanguageToAdjust()

    }



    fun logLanguageToAdjust() {
        var langSent = SharedPrefHelper.getFromSharedPref(
            this,
            Constants.LANG_SENT,
            "false"
        )
        if (langSent!!.toBoolean().not()) {
            var lang = Session.getUserLocale()
            homeViewModel.callChangeLanguage(lang)
            if (lang == Constants.ARABIC_LOCALE)
                LogAdjustEvent("chaufo")// arabic lang
            else
                LogAdjustEvent("56ej2n")// english lang

            SharedPrefHelper.setIntoSharedPref(
                this,
                Constants.LANG_SENT,
                "true"
            )
        }

    }

    private fun checkForNewUpdate() {
//        val forceUpdate = Session.getForceUpdate()
//        if (forceUpdate)
        checkUpdates()

    }

    private fun checkUpdates() {
//        if (forceUpdate) {
        //   var currentVersion = packageManager.getPackageInfo(packageName, 0).versionName

        val versionChecker = SplashActivity.VersionChecker()
        try {
            var appVersionName: String = BuildConfig.VERSION_NAME
            var mLatestVersionName = versionChecker.execute().get()!!
            //  Log.e("mLatestVersionName", mLatestVersionName)
            if (appVersionName.toDouble() < mLatestVersionName.toDouble()) {
                showDialogDownload()
            } else {

            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showDialogDownload() {

//        showMessage(
//            getString(R.string.update_your_app_text), ::yesClicked, ::noClicked
//        )

        openSalamtakDialog(
            supportFragmentManager,
            "",
            getString(R.string.update_your_app_text),
            R.drawable.ic_warning,
            ::yesClicked,
            ::noClicked
        )
    }

    @SuppressLint("WrongConstant")
    fun yesClicked() {
        try {
            var intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )

            val activityInfo = intent.resolveActivityInfo(packageManager, intent.flags)
            if (activityInfo?.exported == true) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Please make sure you have google play store",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showDialogDownload()
        }

    }

    fun noClicked() {
    }

    private fun liveChat() {
        homeViewModel.getUser()?.let {
            _liveChat = LiveChat(this, homeViewModel.getUserName(), it.phone)
        }
    }


    private fun setOnClickListeners() {
        bttn_call_center.setOnClickListener {
            _liveChat.showChatWindow()
        }


//        tv_add_financial_profile.setOnClickListener {
//            LogUtil.LogFirebaseEvent(
//                "add_financial_info_button_clicked",
//                firebaseScreenName
//            )
//            navigateToFinancialInfoScreen()
//        }

    }

    override fun onBackPressed() {
        try {
            if (::_liveChat.isInitialized && _liveChat.onBackPressed().not())
                super.onBackPressed()
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        tview_user_name.text = homeViewModel.getUserName()
        homeViewModel.isFinancialProfileCompleted()
        getCardID()
//        if (homeViewModel.shouldAddFinancialInfo())
//            tv_add_financial_profile.visibility = View.VISIBLE
//        else
//            tv_add_financial_profile.visibility = View.GONE

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (::_liveChat.isInitialized)
                _liveChat.handleChatAttachment(requestCode, resultCode, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun bottomNavigationBarConfig() {
        //        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.homeFragment,
//                R.id.servicesFragment,
//                R.id.requestsFragment,
//                R.id.bookingFragment,
//                R.id.profileFragment,
//            )
//        )
        //    setupActionBarWithNavController(this , navController!!, appBarConfiguration!!) //the most important part
    }

    private fun setupNavigationBar() {
        navController = findNavController(R.id.nav_host_fragment) // main fragment
        nav_view.setupWithNavController(navController) // bottom navigation bar
        onRequestsCenterIconClicked()
        onBottomNavigationBarItemClicked()
        visibilityBottomNavigationBar()  //....show bottom bar at some fragments
        clearBackStack(navController)
        var ToCart = intent.getBooleanExtra("navigateToCart",false) //
        if(ToCart){
            navigateToRequests(navController)
        }else{
            navigateToHome(navController)
        }

    }

    private fun visibilityBottomNavigationBar() {
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                 R.id.homeFragment,
//                -> nav_view?.visibility = View.VISIBLE
//                else -> nav_view?.visibility = View.GONE
//            }
//        }

        showChatButton()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("SelectedItemId", nav_view.getSelectedItemId())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItemId = savedInstanceState.getInt("SelectedItemId")
        nav_view.setSelectedItemId(selectedItemId)
    }

    private fun showChatButton() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.profileFragment,
                -> bttn_call_center?.visibility = View.VISIBLE
                else -> bttn_call_center?.visibility = View.GONE
            }
        }
    }

    //  override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp(appBarConfiguration)
//    }
//

    private fun onBottomNavigationBarItemClicked() {
        nav_view?.setOnItemSelectedListener {
            // do stuff
            when (it.itemId) {
                R.id.homeFragment -> {
                    clearBackStack(navController)
                    navigateToHome(navController)
                }
                R.id.servicesFragment -> {
                    clearBackStack(navController)
                    navigateToServices(navController)
                }
                R.id.requestsFragment -> {
                    clearBackStack(navController)
                    navigateToRequests(navController)
                }
                R.id.bookingFragment -> {
                    clearBackStack(navController)
                    navigateToBooking(navController)
                }
                R.id.profileFragment -> {
                    clearBackStack(navController)
                    navigateToProfile(navController)
                }
            }
            return@setOnItemSelectedListener true
        }


    }

    private fun onRequestsCenterIconClicked() {
        requestsFabIcon.setOnClickListener {
            setRequestsItemEnabled(true)
            clearBackStack(navController)
            navigateToRequests(navController)
        }
    }

    private fun setRequestsItemEnabled(enabled: Boolean) {
        nav_view.menu.getItem(2).isEnabled = enabled
    }


 private  fun getCardID(){
    val sharedPreference = getSharedPreferences("CardData", Context.MODE_PRIVATE)
    var cardId= sharedPreference.getString("cartUID","")
    if(cardId.isNullOrEmpty() || cardId.equals("")){
        cardId=UUID.randomUUID().toString()
        val editor = sharedPreference.edit()
        editor.putString("cartUID",cardId)
        editor.apply()
    }
    Constants.cartUID=cardId
     getCartCount()
}

     fun ShowCartCount(cartCountResponse: cartCountResponse?) {
       // Toast.makeText(this@MainActivity, cartCountResponse!!.data!!.count.toString(), Toast.LENGTH_SHORT).show()
        if(cartCountResponse!!.data!!.count!=0){
            cartCountTv.visibility=View.VISIBLE
            cartCountTv.text=cartCountResponse!!.data!!.count.toString()}
        else{
            cartCountTv.visibility=View.INVISIBLE
        }
    }

    fun getCartCount(){
        val cartViewModel: CartViewModel by viewModels()
        observe(cartViewModel.CartCountResponse, ::ShowCartCount)
        cartViewModel.getCartCount(Constants.cartUID)
    }

    fun changeCartCount(count:Int) {
        if(count!=0){
            cartCountTv.visibility=View.VISIBLE
            cartCountTv.text=count.toString()}
        else{
            cartCountTv.visibility=View.INVISIBLE
        }
    }

}