package com.salamtak.app.ui.component.gettingstarted

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.Event
import com.salamtak.app.utils.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_getting_started.*
import javax.inject.Inject

@AndroidEntryPoint
class GettingStartedActivity : BaseActivity() {

    val gettingStartedViewModel: GettingStartedViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_getting_started


    override fun initializeViewModel() {
//        gettingStartedViewModel = viewModelFactory.create(GettingStartedViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val pager = findViewById<View>(R.id.pager) as ViewPager
        val fm: FragmentManager = supportFragmentManager
        val pagerAdapter = PagerAdapter(this, fm)

        pager.adapter = pagerAdapter
        pager.currentItem = 0

        val tabLayout = findViewById<View>(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(pager, true)

        tv_skip.setOnClickListener { goToHomeScreen() }
        btn_getting_started.setOnClickListener {
            if (pager.currentItem == 3)
                goToHomeScreen()
            else
                pager.currentItem++
        }
        tv_getting_started.setOnClickListener {
            goToHomeScreen()
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    tv_skip.visibility = View.INVISIBLE
                    tv_getting_started.visibility = View.VISIBLE
                    btn_getting_started.visibility = View.INVISIBLE
                    //btn_getting_started.text = getString(R.string.get_started)
                } else {
                    tv_skip.visibility = View.VISIBLE
                    tv_getting_started.visibility = View.INVISIBLE
                    btn_getting_started.visibility = View.VISIBLE
                    //btn_getting_started.text = getString(R.string.next)
                }
            }
        })
    }

    private fun goToHomeScreen() {

//        val i = Intent(this, HomeActivity::class.java)
//        if (Build.VERSION.SDK_INT > 20) {
//            val options = ActivityOptions.makeSceneTransitionAnimation(this)
//            startActivity(i, options.toBundle())
//        } else {
//            startActivity(i)
//        }

        gettingStartedViewModel.notFirstTime()
        gettingStartedViewModel.checkNavigationScreen()
    }

    override fun observeViewModel() {
        observeEvent(gettingStartedViewModel.openHome, ::openHome)
         observeEvent(gettingStartedViewModel.openLogin, ::navigateToLoginScreen)

        //  observeEvent(gettingStartedViewModel.openLogin, ::navigateToRegisterScreen)
    }

    private fun openHome(event: Event<Any>) {
        navigateToMainScreen()
    }


}