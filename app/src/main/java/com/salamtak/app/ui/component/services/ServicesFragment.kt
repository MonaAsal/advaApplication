package com.salamtak.app.ui.component.services

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.HomeCategory
import com.salamtak.app.data.entities.responses.ServicesCategoriesResponse
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.ServicesCategoriesItemListener
import com.salamtak.app.ui.component.education.schools.SchoolsActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.toolbar_new.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class ServicesFragment : BaseFragment() , ServicesCategoriesItemListener<HomeCategory> {
    private var firebaseScreenName = "service_Screen"
    private val serviceViewModel: ServiceViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_services

    override fun observeViewModel() {
        with(serviceViewModel)
        {
            observe(showLoading, ::showLoadingView)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_toolbar_title.text=getString(R.string.services)
        iv_toolbar_back.visibility= View.INVISIBLE
    }

    override fun onResume() {
        observe(serviceViewModel.servicesCategoriesResponse, ::showServicesCategories)
        serviceViewModel.getServicesCategories()
        super.onResume()
    }

    private fun showServicesCategories(resource: ServicesCategoriesResponse) {
        val adapterServicesCategories = ServicesCategoriesAdapter(this, resource.data)
//        addVerticalItemDecorationGrid(rv_services)
        rv_services.adapter = adapterServicesCategories
        rv_services.setHasFixedSize(true)
    }

    override fun onServicesCategoriesSelected(item: HomeCategory) {
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
                navigateToEducationCategory()
              //  startActivity(requireContext().intentFor<SchoolsActivity>(Constants.KEY_CATEGORY_ID to EducationTypes.COURSES.value))

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
            MainCategories.FINISHING.typeId-> {
                LogAdjustEvent("oei1mm")
                LogUtil.LogFirebaseEvent(
                    "home_finishing_clicked",
                    firebaseScreenName
                )
                //   activity?.startActivity<FinishingProvidersActivity>()
               // navigateToFinishing()
                navigateToFinishingCategoriesFragment()
            }
            MainCategories.WEDDING.typeId->  {
                LogAdjustEvent("94ubaw")
                LogUtil.LogFirebaseEvent(
                    "home_wedding_clicked",
                    firebaseScreenName
                )
                //   activity?.startActivity<WeddingRequestActivity>()
                navigateToWeddingForm()
            }
            MainCategories.CARS.typeId->   {
                LogAdjustEvent("myzvcf")
                LogUtil.LogFirebaseEvent(
                    "CarServicesFilter_Android",
                    firebaseScreenName
                )
                navigateToCarServicesProviders()

                // activity?.startActivity<CarServicesProvidersActivity>()
            }
            MainCategories.TRAVEL.typeId-> {
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
            MainCategories.BILLS.typeId->  {
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
    fun showLoadingView(show: Boolean) {
        if (show) {
            progress.toVisible()
        }
        else {
            progress.toGone()
        }
    }

}