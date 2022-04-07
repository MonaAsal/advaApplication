package com.salamtak.app.ui.component.health.doctor

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R
import com.salamtak.app.data.entities.DoctorDetails
import com.salamtak.app.ui.common.MyViewPageStateAdapter
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_doctor.*
import kotlinx.android.synthetic.main.activity_doctor.progress_bar
import kotlinx.android.synthetic.main.activity_doctor.tabs
import kotlinx.android.synthetic.main.activity_doctor.viewPager
import kotlinx.android.synthetic.main.activity_doctor_n.*
import kotlinx.android.synthetic.main.activity_doctor_n.iv_toolbar_back
import kotlinx.android.synthetic.main.item_provider_header.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.math.RoundingMode

@AndroidEntryPoint
class DoctorActivity : BaseOperationsActivity() {
    //, AppBarLayout.OnOffsetChangedListener {
    override val layoutId: Int
        get() = R.layout.activity_doctor

    var doctorId = ""
    var selectedTab: Int = 0 // 2
    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter

    val viewModel: DoctorViewModel by viewModels()

    override fun initializeViewModel() {
//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(DoctorViewModel::class.java)
    }


    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(doctorInfo, ::showDoctorResponse)
        }
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    private fun showDoctorResponse(doctor: DoctorDetails) {
        bindDoctorData(doctor)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        appbar!!.addOnOffsetChangedListener(this)
//        startAlphaAnimation(tv_doctor_name2, 0, View.INVISIBLE)

        initViews()
    }

//    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
//        val maxScroll: Int = appBarLayout.getTotalScrollRange()
//        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()
//        handleAlphaOnTitle(percentage)
//        handleToolbarTitleVisibility(percentage)
//    }

//    private fun handleToolbarTitleVisibility(percentage: Float) {
//        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//            if (!mIsTheTitleVisible) {
//                startAlphaAnimation(
//                    tv_doctor_name2,
//                    ALPHA_ANIMATIONS_DURATION.toLong(),
//                    View.VISIBLE
//                )
//                mIsTheTitleVisible = true
//            }
//        } else {
//            if (mIsTheTitleVisible) {
//                startAlphaAnimation(
//                    tv_doctor_name2,
//                    ALPHA_ANIMATIONS_DURATION.toLong(),
//                    View.INVISIBLE
//                )
//                mIsTheTitleVisible = false
//            }
//        }
//    }

//    private fun handleAlphaOnTitle(percentage: Float) {
//        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
//            if (mIsTheTitleContainerVisible) {
//                startAlphaAnimation(
//                    main_linearlayout_title,
//                    ALPHA_ANIMATIONS_DURATION.toLong(),
//                    View.INVISIBLE
//                )
//                mIsTheTitleContainerVisible = false
//            }
//        } else {
//            if (!mIsTheTitleContainerVisible) {
//                startAlphaAnimation(
//                    main_linearlayout_title,
//                    ALPHA_ANIMATIONS_DURATION.toLong(),
//                    View.VISIBLE
//                )
//                mIsTheTitleContainerVisible = true
//            }
//        }
//    }

    fun startAlphaAnimation(v: View?, duration: Long, visibility: Int) {
        val alphaAnimation =
            if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v!!.startAnimation(alphaAnimation)
    }


    fun initViews() {

//        tv_toolbar_title.text = getString(R.string.doctor_profile)
        //iv_toolbar_back.setOnClickListener { onBackPressed() }

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
//        var doctorId = "775478c7-5d96-428c-a107-e8bd7e219d36"
        //"57507fa7-a859-48f8-bf27-3ff1d7669373"
        if (intent.extras != null && intent.extras!!.containsKey(Constants.KEY_ID))
            doctorId = intent.getStringExtra(Constants.KEY_ID)!!

        viewModel.getDoctorInfo(doctorId!!)
        //        if (LocalRepository.getLocale() == Constants.ENGLISH_LOCALE) {
//            selectedTab = 0
//            startTabIndx = 0
//        }
    }

//    private fun bindDoctorData(doctor: DoctorDetails) {
//        setupViewPager(doctor)
////        tv_toolbar_title.text = doctor.name
////        tv_doctor_name.text = doctor.name
////        tv_doctor_name2.text = doctor.name
//        tv_doctor_title.text = doctor.currentTitle
//        iv_doctor_image.loadCircleImage(doctor.imageUrl)
//        rb_doctor.rating = doctor.rate
//    }

    private fun bindDoctorData(doctor: DoctorDetails) {
        setupViewPager(doctor)
        tv_price.text = doctor.startFrom!!.toBigDecimal().setScale(0, RoundingMode.UP).toInt().toString()
        tv_toolbar_title.text = doctor.name
        //   iv_provider_image.loadCircleImage(doctor.imageUrl)
        doctor.imageUrl?.let {
            iv_provider_image.loadImage(doctor.imageUrl) //operation image
        }
        rb_provider.text = doctor.rate.toString()
        tv_branches.toGone()
    }
    private fun setupViewPager(doctor: DoctorDetails) {

        try {
            myViewPageStateAdapter =
                MyViewPageStateAdapter(
                    supportFragmentManager
                )
//            if (LocalRepository.getLocale() == Constants.ENGLISH_LOCALE) {
////                setStatePageAdapterAr()
            setStatePageAdapterEn(doctor)
//
//            } else
//                setStatePageAdapterAr()

            viewPager.adapter = myViewPageStateAdapter
            tabs.setupWithViewPager(viewPager, true)
            viewPager.offscreenPageLimit = 4
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

//    private fun setStatePageAdapterEnN(doctor: DoctorDetails) {
//        myViewPageStateAdapter.addFragment(
//            DoctorOperationsFragment.newInstance(viewModel.doctorInfo.value!!),
//            getString(R.string.operations)
//        )
//        myViewPageStateAdapter.addFragment(
//            DoctorOverviewFragment.newInstance(viewModel.doctorInfo.value!!),
//            getString(R.string.overview)
//        )
//        myViewPageStateAdapter.addFragment(
//            DoctorWorkFragment.newInstance(viewModel.doctorInfo.value!!),
//            getString(R.string.clinic)
//        )
//        myViewPageStateAdapter.addFragment(
//            DoctorContactsFragment.newInstance(viewModel.doctorInfo.value!!.contacts!!),
//            getString(R.string.contacts)
//        )
//    }

    private fun setStatePageAdapterEn(doctor: DoctorDetails) {

        myViewPageStateAdapter.addFragment(
            DoctorOperationsFragment.newInstance(doctor),
            getString(R.string.operations)
        )
        myViewPageStateAdapter.addFragment(
            DoctorOverviewFragment.newInstance(doctor),
            getString(R.string.overview)
        )
        myViewPageStateAdapter.addFragment(
            DoctorWorkFragment.newInstance(doctor),
            getString(R.string.clinic)
        )
        myViewPageStateAdapter.addFragment(
            DoctorContactsFragment.newInstance(doctor!!.contacts!!),
            getString(R.string.contacts)
        )
    }
}
