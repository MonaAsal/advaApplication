package com.salamtak.app.ui.component.health.doctor

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R
import com.salamtak.app.data.entities.DoctorDetails
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.MyViewPageStateAdapter
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.fragment_doctor.*
import kotlinx.android.synthetic.main.fragment_doctor.progress_bar
import kotlinx.android.synthetic.main.fragment_doctor.tabs
import kotlinx.android.synthetic.main.fragment_medical_provider.*
import kotlinx.android.synthetic.main.item_provider_header.*
import kotlinx.android.synthetic.main.item_provider_header.tv_branches
import kotlinx.android.synthetic.main.toolbar_back.*
import kotlinx.android.synthetic.main.toolbar_back.iv_toolbar_back
import org.jetbrains.anko.intentFor
import java.math.RoundingMode


class DoctorFragment : BaseFragment(), OperationListenerN {
    override val layoutId: Int
        get() = R.layout.fragment_doctor

    var doctorId = ""
    var selectedTab: Int = 0 // 2
    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter
    val viewModel: DoctorViewModel by viewModels()


    override fun observeViewModel() {
        with(viewModel)
        {
            observe(showLoading, ::showLoadingView)
            observe(showServerError, ::showServerErrorMessage)
            observe(doctorInfo, ::showDoctorResponse)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()

    }

    fun startAlphaAnimation(v: View?, duration: Long, visibility: Int) {
        val alphaAnimation =
            if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v!!.startAnimation(alphaAnimation)
    }


    fun initViews() {

        if (arguments != null) {
            doctorId = requireArguments().getString(Constants.DOCTOR_ITEM_KEY).toString()
            viewModel.getDoctorInfo(doctorId)
        }

    }

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
//        tv_branches.text= String.format(getString(R.string.num_branch), doctor.branches?.size)
//        tv_doctor_name.text = doctor.name
//        tv_doctor_title.text = doctor.currentTitle
//        rb_doctor.rating = doctor.rate
    }

    private fun setupViewPager(doctor: DoctorDetails) {

        try {
            myViewPageStateAdapter = childFragmentManager?.let { MyViewPageStateAdapter(it) }!!

            setStatePageAdapterEn(doctor)

            viewPager.adapter = myViewPageStateAdapter
            tabs.setupWithViewPager(viewPager, true)
            viewPager.offscreenPageLimit = 4
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

            }
        })
    }


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

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        viewModel.addToFavourite(operation.id)
    }

    override fun onOwnerClicked(operation: Operation) {
        if (operation.owner!!.type == ProviderType.Doctor.typeId) {
            val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to operation.owner!!.id)
            navigateToDoctor(bundle)

        } else {
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