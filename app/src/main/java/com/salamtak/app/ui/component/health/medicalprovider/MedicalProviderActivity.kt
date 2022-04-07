package com.salamtak.app.ui.component.health.medicalprovider

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.data.entities.responses.DoctorsResponse
import com.salamtak.app.ui.common.MyViewPageStateAdapter
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_medical_provider.*

@AndroidEntryPoint
class MedicalProviderActivity : BaseOperationsActivity() {
    override val layoutId: Int
        get() = R.layout.activity_medical_provider

    var selectedTab = 0

    val viewModel: MedicalProviderViewModel by viewModels()

    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter

    override fun initializeViewModel() {
//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(MedicalProviderViewModel::class.java)


    }

    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(viewModel.medicalProviderInfo, ::showMedicalProviderInfo)
//            observe(operationsResponse, ::showOperations)
        }
//        observe(viewModel.medicalProviderDoctors, ::handleDoctors)
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

//    private fun showOperations(salamtakListResponse: SalamtakListResponse<OperationN>) {
//
//    }

    private fun showMedicalProviderInfo(medicalProvider: MedicalProviderDetails) {
//        var medicalProvider = branch.medicalProvider
//        tv_toolbar_title.text = medicalProvider.name
//        tv_reviews.text = String.format(getString(R.string.num_reviews), 10)
        tv_medical_provider_name.text = medicalProvider.name
//        tv_medical_provider_address.text = medicalProvider.about
        iv_medical_provider_image.loadCircleImage(medicalProvider.image)

        medicalProvider.specialization?.let {
            tv_medical_provider_specialities.text = String.format(
                viewModel.getLocale(),
                getString(R.string.specialities_number),
                medicalProvider.specialization!!.size
            )
        }
//        medicalProvider.branches?.let {
//            if (medicalProvider.branches!!.isNotEmpty())
//                rb_medical_provider.rating = medicalProvider.branches[0]?.rate.toFloat()
//        }
        medicalProvider.rate?.let {
          //  rb_medical_provider.rating = it
        }
        setupViewPager(medicalProvider)
    }

    private fun handleDoctors(resource: Resource<DoctorsResponse>) {
//        when (resource) {
//            is Resource.Success -> {
//                viewModel.setDoctors(resource.data!!.data!!)
//                tv_medical_provider_doctors.text = String.format(viewModel.getLocale(),
//                    getString(R.string.doctors_number),
//                    resource.data!!.data.size
//                )
//
//                setupViewPager(viewModel.getMedicalProviderInfo())
//            }
//        }
    }

//    private fun handleMedicalProviderResponse(resource: Resource<MedicalProviderResponse>?) {
//        when (resource) {
//            is Resource.Loading -> showLoadingView(progress_bar)
//            is Resource.Success -> {
//                progress_bar.toGone()
////                bindBranchData(resource.data!!.data!!)
//                viewModel.getMedicalProviderDoctors(resource.data.data.id)
//            }
//
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = viewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { showServerErrorMessage(it) }
//            }
//        }
//
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        var id = intent.getStringExtra(Constants.KEY_ID)
//        var id = "b4284d9b-8256-4cde-a279-c603a61821d7"
        viewModel.onStart(id!!)
    }

//    override fun onResume() {
//        super.onResume()
//
//    }

    fun initViews() {
//        tv_toolbar_title.text = getString(R.string.medical_provider_profile)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        //var branch = intent.getParcelableExtra<Branch>(Constants.MEDICAL_PROVIDER_ITEM_KEY)

        //branch.medicalProvider.id)
    }


    private fun setupViewPager(medicalProvider: MedicalProviderDetails) {
        try {
            myViewPageStateAdapter =
                MyViewPageStateAdapter(
                    supportFragmentManager
                )
//            if (LocalRepository.getLocale() == Constants.ENGLISH_LOCALE) {
////                setStatePageAdapterAr()
            setStatePageAdapterEn(medicalProvider)
//
//            } else
//                setStatePageAdapterAr()

            viewPager.adapter = myViewPageStateAdapter
            viewPager.offscreenPageLimit = 3

            tabs.setupWithViewPager(viewPager, true)

//            var ss = SharedPrefHelper.getFromSharedPref(this, Constants.KEY_SELECTED_TAB)

            tabs.getTabAt(selectedTab)?.select()

            tabsSelection()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun tabsSelection() {
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager.currentItem = tab.position
                selectedTab = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

                //   viewPager.notifyAll();
            }
        })

    }

    private fun setStatePageAdapterEn(medicalProvider: MedicalProviderDetails) {
        myViewPageStateAdapter.addFragment(
            MedicalProviderOperationsFragment.newInstance(medicalProvider),
            getString(R.string.operations)
        )

        myViewPageStateAdapter.addFragment(
            MedicalProviderOverviewFragment.newInstance(medicalProvider),
            getString(R.string.overview)
        )
//        myViewPageStateAdapter.addFragment(
//            MedicalProviderReviewsFragment.newInstance(medicalProvider),
//            getString(R.string.reviews)
//        )
        myViewPageStateAdapter.addFragment(
            MedicalProviderContactsFragment.newInstance(medicalProvider),
            getString(R.string.contacts)
        )


    }

}
