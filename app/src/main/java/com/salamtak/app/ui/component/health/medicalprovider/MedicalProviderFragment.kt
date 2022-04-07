package com.salamtak.app.ui.component.health.medicalprovider

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.MyViewPageStateAdapter
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.fragment_medical_provider.*
import kotlinx.android.synthetic.main.fragment_medical_provider.iv_toolbar_back
import kotlinx.android.synthetic.main.fragment_medical_provider.progress_bar
import kotlinx.android.synthetic.main.toolbar_back.*
import org.jetbrains.anko.intentFor
import java.math.RoundingMode


class MedicalProviderFragment : BaseFragment() , OperationListenerN {
    override val layoutId: Int
        get() = R.layout.fragment_medical_provider

    var selectedTab = 0

    val viewModel: MedicalProviderViewModel by viewModels()

    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter


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


    @SuppressLint("SetTextI18n")
    private fun showMedicalProviderInfo(medicalProvider: MedicalProviderDetails) {
        tv_toolbar_title.text = medicalProvider.name
      //  tv_medical_provider_name.text = medicalProvider.name
     //   iv_medical_provider_image.loadCircleImage(medicalProvider.image)
       tv_start_price.text = medicalProvider.startFrom!!.toBigDecimal().setScale(0, RoundingMode.UP).toInt().toString()
        tv_branches.text= medicalProvider.branches!!.size.toString()+" "+getString(R.string.branch)
        medicalProvider.image?.let {
            iv_medical_provider_image.loadImage( medicalProvider.image) //operation image
        }
        medicalProvider.specialization?.let {
            tv_medical_provider_specialities.text = String.format(
                viewModel.getLocale(),
                getString(R.string.specialities_number),
                medicalProvider.specialization!!.size
            )
        }
        tv_rate.text= medicalProvider.rate.toString()
       /* medicalProvider.rate?.let {
            rb_medical_provider.rating = it
        }*/
        setupViewPager(medicalProvider)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        if(arguments != null) {
            val id = requireArguments().getString(Constants.KEY_ID).toString()
            viewModel.onStart(id!!)
        }

    }


    fun initViews() {
        iv_toolbar_back.setOnClickListener { onBackPressed() }

    }


    private fun setupViewPager(medicalProvider: MedicalProviderDetails) {
        try {
            myViewPageStateAdapter = childFragmentManager?.let {
            MyViewPageStateAdapter(it)}
//            if (LocalRepository.getLocale() == Constants.ENGLISH_LOCALE) {
////                setStatePageAdapterAr()
            setStatePageAdapterEn(medicalProvider)
//
//            } else
//                setStatePageAdapterAr()

            mviewPager.adapter = myViewPageStateAdapter
            mviewPager.offscreenPageLimit = 3

            tabs.setupWithViewPager(mviewPager, true)

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

                mviewPager.currentItem = tab.position
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

        myViewPageStateAdapter.addFragment(
            MedicalProviderContactsFragment.newInstance(medicalProvider),
            getString(R.string.contacts)
        )


    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        viewModel.addToFavourite(operation.id)
    }

    override fun onOwnerClicked(operation: Operation) {
        if (operation.owner!!.type == ProviderType.Doctor.typeId){
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to operation.owner!!.id)
            navigateToDoctor(bundle)

        } else{
            val bundle = bundleOf(Constants.KEY_ID to operation.owner!!.id)
            navigateToMedicalProvider(bundle)
        }

    }

    override fun onDetailsClicked(operation: Operation) {
        navigateToOperationBookingScreen(operation)
    }

    fun navigateToOperationBookingScreen(operation: Operation) {
        LogUtil.LogFirebaseEvent(
            "btn_book_now",
            "screen_" + this::class.java.simpleName,
            "operation",
            operation.id
        )

        startActivity(
            activity?.intentFor<BookHealthRequestActivity>(
                Constants.KEY_OPERATION_ITEM to operation.id
            )
        )
    }

}