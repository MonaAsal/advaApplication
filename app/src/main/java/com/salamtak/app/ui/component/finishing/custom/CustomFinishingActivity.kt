package com.salamtak.app.ui.component.finishing.custom

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.preventUiFromPopUpAboveKeyboard
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_custom_finishing.*
import kotlinx.android.synthetic.main.layout_custom_steps.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class CustomFinishingActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_custom_finishing

    var showError = false
    lateinit var myViewPageStateAdapter: CustomFinishingViewPageStateAdapter


    var fragment1 = CustomFinishingStep1Fragment.newInstance()
    var fragment2 = CustomFinishingStep2Fragment.newInstance()
    var fragment3 = CustomFinishingStep3Fragment.newInstance()

    val viewmodel: CustomFinishingViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preventUiFromPopUpAboveKeyboard(this)
        viewmodel.liveInput.value = null
        tv_toolbar_title.text = getString(R.string.finishing_request)
        setupViewPager()


//        iv_circle1.setOnClickListener {
//            viewPager.currentItem = 0
////            handleSteps(0)
//        }

//        iv_previous.setOnClickListener {
//            when (viewPager.currentItem) {
//                1 -> viewPager.currentItem = 0
//                2 -> viewPager.currentItem = 1
//            }
//        }

        btn_next.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> {
                    //viewPager.currentItem = 1
                    fragment1.onNextClicked()
                }
                1 -> {
//                    viewPager.currentItem = 2
                    fragment2.onNextClicked()
                }
            }
        }

//        iv_circle2.setOnClickListener {
//            viewPager.currentItem = 1
////            handleSteps(1)
//        }
//
//        iv_circle3.setOnClickListener {
//            viewPager.currentItem = 2
////            handleSteps(2)
//        }

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
        val fragment1: CustomFinishingStep1Fragment,
        val fragment2: CustomFinishingStep2Fragment,
        val fragment3: CustomFinishingStep3Fragment
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