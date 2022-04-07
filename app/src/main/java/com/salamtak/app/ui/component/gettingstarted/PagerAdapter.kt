package com.salamtak.app.ui.component.gettingstarted

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class PagerAdapter(
    private val mContext: Context,
    fm: FragmentManager?
) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnbordingFirstFragment()
            1 -> OnBoardingSecondFragment()
            2 -> OnBoardingThirdFragment()
            3 -> OnboardingFourthFragment()
          //  4 -> OnboardingFifthFragment()
            else -> OnbordingFirstFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
//        return mContext.resources
//            .getString(TAB_TITLES[position])
    }

    override fun getCount(): Int { // Show 3 total pages.
        return 4
    }

    companion object {
//        @StringRes
//        private val TAB_TITLES =
//            intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)
    }

}