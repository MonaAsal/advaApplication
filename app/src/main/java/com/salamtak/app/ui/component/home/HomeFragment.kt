package com.salamtak.app.ui.component.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.BuildConfig
import com.salamtak.app.R
import com.salamtak.app.data.Session
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.data.local.SharedPrefHelper
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.AdvertisementsItemListener
import com.salamtak.app.ui.common.listeners.PromotionsItemListener
import com.salamtak.app.ui.common.listeners.ProvidersItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.carservices.custom.CustomCarActivity
import com.salamtak.app.ui.component.education.categories.EducationCategoriesActivity
import com.salamtak.app.ui.component.education.schools.SchoolsActivity
import com.salamtak.app.ui.component.finishing.custom.CustomFinishingActivity
import com.salamtak.app.ui.component.firebase.FirebaseViewModel
import com.salamtak.app.ui.component.notifications.NotificationsActivity
import com.salamtak.app.ui.component.splash.SplashActivity
import com.salamtak.app.utils.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.progress_bar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.progress_completion
import kotlinx.android.synthetic.main.fragment_home.tv_complition
import kotlinx.android.synthetic.main.fragment_home.tv_financial_info
import kotlinx.android.synthetic.main.toolbar_home.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import java.util.concurrent.ExecutionException


@AndroidEntryPoint
class HomeFragment : BaseFragment(), RecyclerItemListener<HomeCategory>,AdvertisementsItemListener<Advertisement>,
    PromotionsItemListener<Promotions>, ProvidersItemListener<Providers>, View.OnClickListener {

    private val firebaseScreenName = "HomeScreen"
    private val homeViewModel: HomeViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun observeViewModel() {
        with(homeViewModel) {
            observe(showLoading, ::showLoadingView)
        }
        ObserveAllData()
    }

    private fun ObserveAllData() {

        observe(homeViewModel.financialProfileCompleted, ::handleFinancialProfileCompleted)
        homeViewModel.isFinancialProfileCompleted()
        observe(homeViewModel.featuredCategoriesResponse, ::showCategories)
        homeViewModel.getFeaturedCategories()
        observe(homeViewModel.advertisementsResponse, ::showSlider)
        homeViewModel.getAdvertisements()
        observe(homeViewModel.promotionsResponse, ::showPromotions)
        homeViewModel.getPromotions()
        observe(homeViewModel.topProvidersResponse, ::showTopProviders)
        homeViewModel.getTopProviders()
        observe(homeViewModel.financialResponse, ::showProgress)
        homeViewModel.getFinancialProgress()
        val firebaseViewModel: FirebaseViewModel by viewModels()
        observe(firebaseViewModel.notifications, ::handleNotifications)
        firebaseViewModel.getAllNotifications()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        checkForNewUpdate()
        logLanguageToAdjust()
        bindUserInfo(homeViewModel.getUserInfo()!!)
        btn_Request.setOnClickListener(this)
        bindServicesData()

    }


    private fun handleFinancialProfileCompleted(response: FinancialProfileCompleted) {
        if (response.isCompleted.not())
            cv_financial_profile.visibility = View.VISIBLE
        else
            cv_financial_profile.visibility = View.GONE
    }

    private fun showCategories(resource: FeaturedCategoriesResponse) {
        val featuredCategories = mutableListOf<HomeCategory>()
        val nonFeaturedCategories = mutableListOf<HomeCategory>()
        resource.data?.forEach() { item ->
            if (item.isFeatured) {
                featuredCategories.add(item)
            } else
                nonFeaturedCategories.add(item)
        }


        showFeaturedCategories(featuredCategories)
        showNonFeaturedCategories(nonFeaturedCategories)
    }


    private fun Group.addOnClickListener(listener: (view: View) -> Unit) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun logLanguageToAdjust() {
        val langSent = SharedPrefHelper.getFromSharedPref(
            context,
            Constants.LANG_SENT,
            "false"
        )
        if (langSent!!.toBoolean().not()) {
            val lang = Session.getUserLocale()
            homeViewModel.callChangeLanguage(lang)
            if (lang == Constants.ARABIC_LOCALE)
                LogAdjustEvent("chaufo")// arabic lang
            else
                LogAdjustEvent("56ej2n")// english lang

            SharedPrefHelper.setIntoSharedPref(
                context,
                Constants.LANG_SENT,
                "true"
            )
        }
    }

    @SuppressLint("WrongConstant")
    fun yesClicked() {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${context}packageName")
            )

            val activityInfo =
                context?.let { intent.resolveActivityInfo(it.packageManager, intent.flags) }
            if (activityInfo?.exported == true) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    context,
                    "Please make sure you have google play store",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showDialogDownload()
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
            val appVersionName: String = BuildConfig.VERSION_NAME
            val mLatestVersionName = versionChecker.execute().get()!!
            //  Log.e("mLatestVersionName", mLatestVersionName)
            if (appVersionName.toDouble() < mLatestVersionName.toDouble()) {
                showDialogDownload()
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
            parentFragmentManager,
            "",
            getString(R.string.update_your_app_text),
            R.drawable.ic_warning,
            ::yesClicked,
            ::noClicked
        )
    }

    fun noClicked() {
    }

    private fun setOnClickListeners() {

        tv_continue_profile.setOnClickListener {
            LogUtil.LogFirebaseEvent(
                "add_financial_info_button_clicked",
                firebaseScreenName
            )
            navigateToFinancialInfoScreen()
        }

//        ic_toolbar_profile.setOnClickListener {
//            LogUtil.LogFirebaseEvent(
//                "profile_button_clicked",
//                firebaseScreenName
//            )
//            requireActivity()?.startActivity<ProfileActivity>()
//        }

        ic_toolbar_notifications.setOnClickListener {
            LogUtil.LogFirebaseEvent(
                "notifications_clicked",
                firebaseScreenName
            )
            val sharedPreference = requireContext().getSharedPreferences("CardData", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("NotificationCount","0")
            editor.apply()
            notificationCountTv.visibility=View.INVISIBLE
            requireActivity()?.startActivity<NotificationsActivity>()        }
    }


    override fun onItemSelected(item: HomeCategory) {
        when (item.type) {
            MainCategories.HEALTH.typeId -> {
                LogUtil.LogFirebaseEvent(
                    "home_health_clicked",
                    firebaseScreenName
                )
                navigateToHealth()
                // activity?.startActivity<CategoriesActivity>()
            }
            MainCategories.EDUCATION.typeId -> {
                LogAdjustEvent("yx3o5w")
                LogUtil.LogFirebaseEvent(
                    "home_education_clicked",
                    firebaseScreenName
                )
                //requireContext().startActivity<EducationCategoriesActivity>()
               // startActivity(requireContext().intentFor<SchoolsActivity>(Constants.KEY_CATEGORY_ID to EducationTypes.COURSES.value))
                navigateToEducationCategory()
                // navigateToEducationForm()
                //  activity?.startActivity<EducationActivity>()
            }
            MainCategories.INSURANCE.typeId -> {
                LogAdjustEvent("nsnyqm")
                LogUtil.LogFirebaseEvent(
                    "home_insurance_clicked",
                    firebaseScreenName
                )
//                startActivity(
//                    activity?.intentFor<InsuranceActivity>(
//                        //Constants.IS_FROM_FISCOUNT to true
//                    )
//                )

                val bundle = bundleOf(Constants.IS_FROM_FISCOUNT to true)
                navigateToInsuranceForm(bundle)

            }
            MainCategories.FINISHING.typeId -> {
                LogAdjustEvent("oei1mm")
                LogUtil.LogFirebaseEvent(
                    "home_finishing_clicked",
                    firebaseScreenName
                )
                //   activity?.startActivity<FinishingProvidersActivity>()
                navigateToFinishingCategoriesFragment()
               // navigateToFinishing()
            }
            MainCategories.WEDDING.typeId -> {
                LogAdjustEvent("94ubaw")
                LogUtil.LogFirebaseEvent(
                    "home_wedding_clicked",
                    firebaseScreenName
                )
                //   activity?.startActivity<WeddingRequestActivity>()
                navigateToWeddingForm()
            }
            MainCategories.CARS.typeId -> {
                LogAdjustEvent("myzvcf")
                LogUtil.LogFirebaseEvent(
                    "CarServicesFilter_Android",
                    firebaseScreenName
                )
                navigateToCarServicesProviders()
                // activity?.startActivity<CarServicesProvidersActivity>()
            }
            MainCategories.TRAVEL.typeId -> {
                LogUtil.LogFirebaseEvent(
                    "btn_home_travel_clicked",
                    firebaseScreenName
                )
                val bundle = bundleOf("page" to 1,"image" to item.imageUrl)
                navigateToComingSoon(bundle)
//                startActivity(
//                    activity?.intentFor<ComingSoonActivity>(
//                        "page" to 1
//                    )
//                )
            }
            MainCategories.BILLS.typeId -> {
                LogUtil.LogFirebaseEvent(
                    "btn_home_bills_clicked",
                    firebaseScreenName
                )
                val bundle = bundleOf("page" to 2,"image" to item.imageUrl)
                navigateToComingSoon(bundle)
//                startActivity(
//                    activity?.intentFor<ComingSoonActivity>(
//                        "page" to 2
//                    )
//                )
            }
        }
    }

    private fun showNonFeaturedCategories(list: MutableList<HomeCategory>) {
        val adapterNonFeaturedCategories = FeaturedCategoriesAdapter(this, list)
        rv_notFeaturedCategories.layoutManager = object : LinearLayoutManager(
            context,
            HORIZONTAL, false
        ) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width / 3
                return true
            }
        }
        rv_notFeaturedCategories.adapter = adapterNonFeaturedCategories
    }

    private fun showFeaturedCategories(list: MutableList<HomeCategory>) {
        val adapterFeaturedCategories = FeaturedCategoriesAdapter(this, list)
//        addVerticalItemDecorationGrid(rv_FeaturedCategories)
        rv_FeaturedCategories.adapter = adapterFeaturedCategories
    }

    /*  private fun showAdvertisements(resource: AdvertisementsResponse) {
        val adapterAdvertisement = AdvertisementsAdapter(this, resource.data)
        rv_Advertisements.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width - 200
                return true
            }
        }
        rv_Advertisements.adapter = adapterAdvertisement
    }
*/
    override fun onAdvertisementSelected(item: Advertisement) {
    }

    private fun showPromotions(resource: PromotionsResponse) {
        sv_Promotions.changeSizeAspectRatio(0)

        Log.d("slider height ",  sv_Promotions.height.toString() )
        val adapter = PromotionsSliderAdapter(this, resource.data)
        sv_Promotions.setSliderAdapter(adapter)
        sv_Promotions.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sv_Promotions.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION)
        sv_Promotions.startAutoCycle()

    }

    override fun onPromotionSelected(item: Promotions) {
    }

    private fun showTopProviders(resource: TopProvidersResponse) {
        val adapterTopProviders = TopProvidersAdapter(this, resource.data)
        rv_TopProviders.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = (width / 3) -50
                return true
            }
        }
        rv_TopProviders.adapter = adapterTopProviders
    }

    override fun onProviderSelected(Provider: Providers) {
        if (Provider.type == ProviderType.Doctor.typeId){
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to Provider.id)
            navigateToDoctor(bundle)
        }
        else if (Provider.type == ProviderType.Finishing.typeId) {
            val bundle = bundleOf(Constants.KEY_ID to Provider.id)
            navigateToFinishingProviderRequestFragment(bundle)
        }
        else if (Provider.type == ProviderType.Car.typeId) {
            val bundle = bundleOf(Constants.KEY_ID to Provider.id)
            navigateToCarProviderDetailsRequest(bundle)
        }
            else{
            val bundle = bundleOf(Constants.KEY_ID to Provider.id)
            navigateToMedicalProvider(bundle)
        }

    }

    private fun showProgress(baseResponse: BaseResponse) {
        when (baseResponse.message) {
            "0" -> {
                progress_completion.progress = 0
                tv_complition.text = getString(R.string.zero)
                tv_financial_info.text = getString(R.string.complete_zero_financial_profile)
            }
            "1" -> {
                progress_completion.progress = 33
                tv_complition.text = getString(R.string.thirtyThree)
                tv_financial_info.text = getString(R.string.complete_thirty_financial_profile)
            }
            "2" -> {
                progress_completion.progress = 66
                tv_complition.text = getString(R.string.sixtySix)
                tv_financial_info.text = getString(R.string.complete_sixty_financial_profile)
            }
            "3" -> {
                cv_financial_profile.visibility = View.GONE
                progress_completion.progress = 100
                tv_complition.text = getString(R.string.hundred)
                tv_financial_info.text = getString(R.string.complete_all_financial_profile)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindUserInfo(userInfo: User) {
        if (userInfo != null) {
            txt_toolbar_title.text = getString(R.string.home_welcome) + " " + homeViewModel.getUserName().split(" ")[0]
        }

    }

    fun showLoadingView(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
        } else {
            progress_bar.toGone()
        }
    }

    override fun onResume() {
        ObserveAllData()
        spinner_services.setSelection(0)
        super.onResume()
    }

    private fun showSlider(imgsList: AdvertisementsResponse) {
        sv_Advertisements.changeSizeAspectRatio(0)
        var list: List<Advertisement> =imgsList.data
        if(imgsList.data.size>15){
             list=imgsList.data.subList(0,15)
        }
        val adapter = AdvertisementsSliderAdapter(list)
        sv_Advertisements.setSliderAdapter(adapter)
        sv_Advertisements.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sv_Advertisements.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION)
        sv_Advertisements.startAutoCycle()
    }

    fun openMedicalProviderProfile(providers: Providers) {

        val bundle = bundleOf(
            Constants.KEY_ID to providers.id
        )
        navigateToMedicalProvider(bundle)
    }


    fun openDoctorProfile(providers: Providers) {

        val bundle = bundleOf(
            Constants.DOCTOR_ITEM_KEY to providers.id
        )
        navigateToDoctor(bundle)
    }

    private fun bindServicesData() {

        val list = resources.getStringArray(R.array.Services_types).toMutableList()

        list.add(0, getString(R.string.choose_the_service))

        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.item_spinner, list
            )
        }
        spinner_services.adapter = adapter
        spinner_services?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                hideErrorIcon()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position!=0){
                    hideErrorIcon()
                }

            }

        }


    }


    override fun onClick(p0: View?) {
        when (spinner_services.selectedItemPosition) {
            0 ->{
                showErrorIcon()
            }

            1 -> {
                navigateToOtherOperations()
            }
            2 -> {
                navigateToEducationForm()
            }
            3 -> {
                val bundle = bundleOf(Constants.IS_FROM_FISCOUNT to true)
                navigateToInsuranceForm(bundle)
            }
            4 -> {
                activity?.startActivity<CustomCarActivity>()
                activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
            5 -> {
                activity?.startActivity<CustomFinishingActivity>()
                activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            }
            6 -> {
                val bundle = bundleOf("page" to 1)
                navigateToComingSoon(bundle)
            }
            7 -> {
                val bundle = bundleOf("page" to 2)
                navigateToComingSoon(bundle)
            }
            8 -> {
                navigateToWeddingForm()
            }
        }
    }

    private fun showErrorIcon() {
        et_error.error = (getString(R.string.choose_service_error))
    }

    private fun hideErrorIcon() {
        et_error.error = null    }

    private fun handleNotifications(notifications: List<Notification>?) {
        Log.i("list", notifications!!.size.toString())
        // Constants.NotificationCount=notifications!!.size
        val sharedPreference = requireContext().getSharedPreferences("CardData", Context.MODE_PRIVATE)
        val NotificationCount= sharedPreference.getString("NotificationCount","0")
        val count=NotificationCount
        if(!count.equals("0")){
            notificationCountTv.visibility=View.VISIBLE }
        else{
            notificationCountTv.visibility=View.INVISIBLE
        }
    }
}