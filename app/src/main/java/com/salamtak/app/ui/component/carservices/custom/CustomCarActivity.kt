package com.salamtak.app.ui.component.carservices.custom

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_custom_car.*
import kotlinx.android.synthetic.main.layout_custom_steps.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.toolbar_finishing_car.*
import kotlinx.android.synthetic.main.toolbar_new.*
import java.util.*

@AndroidEntryPoint
class CustomCarActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_custom_car

    var showError = false
    lateinit var myViewPageStateAdapter: CustomFinishingViewPageStateAdapter


    var fragment1 = CustomCarStep1Fragment.newInstance()
    var fragment2 = CustomCarStep2Fragment.newInstance()
    var fragment3 = CustomCarStep3Fragment.newInstance()

    val viewmodel: CustomCarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      // hideKeyboard(this)
        preventUiFromPopUpAboveKeyboard(this)
        viewmodel.liveInput.value = null
        tv_toolbar_title.text = getString(R.string.car_service_request)
        //iv_toolbar_icon.setImageResource(R.drawable.ic_car_service)

        setupViewPager()

        btn_next.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> {
                    fragment1.onNextClicked()
                }
                1 -> {
                    fragment2.onNextClicked()
                }
            }
        }

        iv_toolbar_back.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> onBackPressed()
                1 -> viewPager.currentItem = 0
                2 -> viewPager.currentItem = 1
            }
        }
    }


    private fun setupViewPager() {
        try {
            myViewPageStateAdapter =
                CustomFinishingViewPageStateAdapter(
                    this, fragment1, fragment2, fragment3
                )

            viewPager.isUserInputEnabled = false
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = myViewPageStateAdapter


            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    Log.e("position", position.toString())
                    if (position == 2) {
                        btn_next.toGone()
                    } else
                        btn_next.toVisible()

                    handleSteps(position)

                    super.onPageSelected(position)
                }
            })


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }

    class CustomFinishingViewPageStateAdapter(
        activity: AppCompatActivity,
        val fragment1: CustomCarStep1Fragment,
        val fragment2: CustomCarStep2Fragment,
        val fragment3: CustomCarStep3Fragment
    ) :
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 3
        }


        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 ->
                    fragment1//CustomFinishingStep1Fragment.newInstance()
                1 ->
                    fragment2//CustomFinishingStep2Fragment.newInstance()
                2 ->
                    fragment3//CustomFinishingStep3Fragment.newInstance()
                else -> fragment1
            }
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleSteps(position: Int) {
        when (position) {
            0 -> {
                tv_circle1.setTextColor(getColor(R.color.white))
                tv_circle1.background = getDrawable(R.drawable.ic_oval)

                tv_circle2.setTextColor(getColor(R.color.orange))
                tv_circle2.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_circle3.setTextColor(getColor(R.color.orange))
                tv_circle3.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_your_info.toVisible()
                tv_provider_info.toGone()
                tv_payment_plan.toGone()
            }

            1 -> {
                tv_circle2.setTextColor(getColor(R.color.white))
                tv_circle2.background = getDrawable(R.drawable.ic_oval)

                tv_circle1.setTextColor(getColor(R.color.orange))
                tv_circle1.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_circle3.setTextColor(getColor(R.color.orange))
                tv_circle3.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_your_info.toGone()
                tv_provider_info.toVisible()
                tv_payment_plan.toGone()
            }

            2 -> {
                tv_circle3.setTextColor(getColor(R.color.white))
                tv_circle3.background = getDrawable(R.drawable.ic_oval)

                tv_circle1.setTextColor(getColor(R.color.orange))
                tv_circle1.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_circle2.setTextColor(getColor(R.color.orange))
                tv_circle2.background = getDrawable(R.drawable.ic_circle_orange_light)

                tv_your_info.toGone()
                tv_provider_info.toGone()
                tv_payment_plan.toVisible()
            }
        }
        if (viewmodel.isStep1Completed())
            iv_circle1.toVisible()

        if (viewmodel.isStep2Completed())
            iv_circle2.toVisible()


    }


    override fun initializeViewModel() {
//        viewmodel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(viewmodel::class.java)
    }


    override fun observeViewModel() {
        with(viewmodel)
        {
            observe(goToStep, ::goToStep)
        }
    }

    fun goToStep(stepIndex: Int) {
        Log.e("stepIndex", stepIndex.toString())
        viewPager.currentItem = stepIndex
    }


}