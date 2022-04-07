//package com.salamtak.app.ui.component.home
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.view.inputmethod.InputMethodManager
//import android.widget.TextView
//import androidx.constraintlayout.widget.Group
//import androidx.lifecycle.ViewModelProviders
//import com.salamtak.app.R
//import com.salamtak.app.data.Session
//import com.salamtak.app.data.local.SharedPrefHelper
//import com.salamtak.app.ui.ViewModelFactory
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.ui.component.medicalnetwork.MedicalNetworkActivity
//import com.salamtak.app.ui.component.offers.DiscountCardsActivity
//import com.salamtak.app.ui.component.health.categories.CategoriesActivity
//import com.salamtak.app.utils.*
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_health_home.*
//import kotlinx.android.synthetic.main.toolbar.*
//import org.jetbrains.anko.intentFor
//import org.jetbrains.anko.startActivity
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class HealthHomeActivity : BaseActivity() {
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    @Inject
//    lateinit var homeViewModel: HomeViewModel
//    override val layoutId: Int
//        get() = R.layout.activity_health_home
//
//    override fun initializeViewModel() {
//        homeViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(HomeViewModel::class.java)
//    }
//
//
//    override fun observeViewModel() {
////        observe(homeViewModel.financialProfileCompleted, ::handleFinancialProfileCompleted)
//    }
//
//
////    private fun handleFinancialProfileCompleted(response: FinancialProfileCompleted) {
////        if (response.isCompleted.not())
////            tv_add_financial_profile.visibility = View.VISIBLE
////        else
////            tv_add_financial_profile.visibility = View.GONE
////    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        logLanguageToAdjust()
//
//        initViews()
//
////        checkForceUpdateRemoteConfig()
////        checkForNewUpdate()
//
//        if (intent != null && intent.data != null) {
//            try {
//                //https://salamtakapp.page.link/asd14?to=home&id=123
//                var data = intent.data!!
//                var param = data.query!!
//
//                var to = data.getQueryParameter("to")
//                var id = data.getQueryParameter("id")
//
//                Log.e("DeepLink", "to : " + data.getQueryParameter("to"))
//                Log.e("DeepLink", "id : " + data.getQueryParameter("id"))
//
//                when (to) {
//                    "home" -> {
//                    }
//
//                }
//            } catch (e: Exception) {
//
//            }
//        }
//
//    }
//
////    override fun onResume() {
////        super.onResume()
////        homeViewModel.isFinancialProfileCompleted()
////    }
//
//
//    private fun initViews() {
//
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        tv_toolbar_title.text = getString(R.string.health)
//
//        setOnClickListeners()
//
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        et_search!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
//                val imm =
//                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(v.windowToken, 0)
//
//                if (et_search!!.text.toString().isNotEmpty()) {
//
//                 LogUtil.LogFirebaseEvent(
//                        "btn_search",
//                        "screen_" + this::class.java.simpleName,
//                        "search_key",
//                        et_search!!.text.toString()
//                    )
//
//                    openSearchScreen(et_search.text.toString())
//                }
//
//                return@OnEditorActionListener true
//            }
//            false
//        })
//
//    }
//
//    fun logLanguageToAdjust() {
//        var langSent = SharedPrefHelper.getFromSharedPref(
//            this,
//            Constants.LANG_SENT,
//            "false"
//        )
//        if (langSent!!.toBoolean().not()) {
//            var lang = Session.getUserLocale()
//            homeViewModel.callChangeLanguage(lang)
////            if (lang == Constants.ARABIC_LOCALE)
////                LogAdjustEvent("chaufo")// arabic lang
////            else
////                LogAdjustEvent("56ej2n")// english lang
//
//            SharedPrefHelper.setIntoSharedPref(
//                this,
//                Constants.LANG_SENT,
//                "true"
//            )
//        }
//
//    }
//
//
//    private fun setOnClickListeners() {
//        iv_operations.setOnClickListener {
//         LogUtil.LogFirebaseEvent(
//                "btn_operation",
//                "screen_" + this::class.java.simpleName
//            )
//            startActivity<CategoriesActivity>()
//        }
//
////        iv_installment2.setOnClickListener {
////            LogFirebaseEvent(
////                "btn_custom_operation",
////                "screen_" + this::class.java.simpleName
////            )
////            startActivity<OperationsTrackingActivity>()
////        }
////
////        tv_home.setOnClickListener { v ->
////            //Toast.makeText(this, "home", Toast.LENGTH_LONG).show()
//////            startActivity<CongratulationsActivity>()
////        }
////
////        tv_more.setOnClickListener { v ->
//////            Toast.makeText(this, "settings", Toast.LENGTH_LONG).show()
////            LogFirebaseEvent(
////                "btn_more",
////                "screen_" + this::class.java.simpleName
////            )
////            startActivity<MoreActivity>()
////        }
//
//        iv_offers.setOnClickListener {
//
//            LogAdjustEvent("9xkrz2")
//         LogUtil.LogFirebaseEvent(
//                "btn_offers",
//                "screen_" + this::class.java.simpleName
//            )
//            startActivity(
//                intentFor<DiscountCardsActivity>(
//                    //Constants.IS_FROM_FISCOUNT to true
//                )
//            )
//        }
//        iv_custom_operations.setOnClickListener {
//
////            LogAdjustEvent("5wmmoa")
//            LogAdjustEvent("2ckym9")
//         LogUtil.LogFirebaseEvent(
//                "btn_home_visits",
//                "screen_" + this::class.java.simpleName
//            )
////            startActivity<LandingHomeVisitsActivity>()
//            startActivity<MedicalNetworkActivity>()
//        }
//
////        tv_add_financial_profile.setOnClickListener {
////            LogFirebaseEvent(
////                "btn_add_financial_info",
////                "screen_" + this::class.java.simpleName
////            )
////            navigateToFinancialInfoScreen()
////        }
//
//    }
//
//    private fun Group.addOnClickListener(listener: (view: View) -> Unit) {
//        referencedIds.forEach { id ->
//            rootView.findViewById<View>(id).setOnClickListener(listener)
//        }
//    }
//}