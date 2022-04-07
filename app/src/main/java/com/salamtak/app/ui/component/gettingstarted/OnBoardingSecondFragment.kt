package com.salamtak.app.ui.component.gettingstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.salamtak.app.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingSecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // radwa change this
        return inflater.inflate(R.layout.fragment_onboarding_second, null)
    }
}