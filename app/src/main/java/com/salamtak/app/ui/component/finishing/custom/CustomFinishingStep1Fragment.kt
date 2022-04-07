package com.salamtak.app.ui.component.finishing.custom

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R


import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.shakeView
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_education.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step1.*
import kotlinx.android.synthetic.main.fragment_custom_finishing_step1.et_name
import kotlinx.android.synthetic.main.fragment_custom_finishing_step1.tv_lbl_name
import kotlinx.android.synthetic.main.layout_custom_steps.*
import javax.inject.Inject

@AndroidEntryPoint
class CustomFinishingStep1Fragment : BaseFragment()  {


    val viewmodel: CustomFinishingViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_custom_finishing_step1

    override fun observeViewModel() {
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CustomFinishingStep1Fragment()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()

//        viewmodel =
//            ViewModelProviders.of(requireActivity(), viewModelFactory)
//                .get(CustomFinishingViewModel::class.java)

        observe(viewmodel.customFromState, ::handleFormState)

        tv_lbl_name.text = tv_lbl_name.text.toString() + "*"

        if (viewmodel.isStep1Completed())
            (requireActivity() as CustomFinishingActivity).iv_circle1.toVisible()

        var input = viewmodel.getCustomFinishingInput()
        et_phone1.setText(input.PhoneNumber)
       et_phone1.filters =  (Constants.DisableDecimalWithMaxLength(11))

        et_name.setText(input.FullName)
    }

    fun onNextClicked() {
        viewmodel.saveStep1Data(et_name.text.toString(), et_phone1.text.toString())
    }

    private fun handleFormState(customOperationFormState: FinishingFormState) {
        if (customOperationFormState?.nameError != null) {
            et_name.error = getString(customOperationFormState.nameError)
            et_name.shakeView()
        }
        if (customOperationFormState?.phoneError != null) {
            et_phone1.error = getString(customOperationFormState.phoneError)
            et_phone1.shakeView()
        }
    }
}