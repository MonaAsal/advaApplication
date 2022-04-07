package com.salamtak.app.ui.component.finishing.custom

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.MainCategories


import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.finishing.adapters.CustomCategoriesTypesAdapter
import com.salamtak.app.ui.component.shared.GenericCategoriesAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.shakeView
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_custom_finishing_step2.*
import kotlinx.android.synthetic.main.layout_custom_steps.*
import javax.inject.Inject

@AndroidEntryPoint
class CustomFinishingStep2Fragment : BaseFragment() , RecyclerItemListener<Category> {

    override val layoutId: Int
        get() = R.layout.fragment_custom_finishing_step2

    override fun observeViewModel() {
    }

    lateinit var categoriesAdapter: CustomCategoriesTypesAdapter

    val viewmodel: CustomFinishingViewModel by activityViewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        @JvmStatic
        fun newInstance() =
            CustomFinishingStep2Fragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewmodel =
//            ViewModelProviders.of(requireActivity(), viewModelFactory)
//                .get(CustomFinishingViewModel::class.java)

        preventUiPopingAboveKeyBoard()
        tv_lbl_provider_name.text = tv_lbl_provider_name.text.toString() + "*"
        categoriesAdapter =
            CustomCategoriesTypesAdapter(
                this, true
            )
        et_phone_step2.filters= Constants.DisableDecimalWithMaxLength(11)
        observe(viewmodel.categoriesLiveData, ::handleCategoriesList)
//        observe(viewmodel.showLoading, ::showLoading)
        observe(viewmodel.customFromState, ::handleFormState)
        viewmodel.getCategories(MainCategories.FINISHING.typeId)
        if (viewmodel.isStep2Completed())
            (requireActivity() as CustomFinishingActivity).iv_circle2.toVisible()

        showCahchedData()

    }

    fun onNextClicked() {
        viewmodel.saveStep2Data(
            et_provider_name.text.toString(),
            et_phone_step2.text.toString(),
            et_more_into.text.toString()
        )
    }

    fun showLoading(show: Boolean) {
        progress_bar2?.let {
            progress_bar2.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    private fun handleFormState(customOperationFormState: FinishingFormState) {
        if (customOperationFormState?.providerNameError != null) {
            et_provider_name.error = getString(customOperationFormState.providerNameError)
            et_provider_name.shakeView()
        }
        if (customOperationFormState?.phoneError != null) {
            et_phone_step2.error = getString(customOperationFormState.phoneError)
            et_phone_step2.shakeView()
        }
    }

    private fun handleCategoriesList(categoriesModel: SalamtakListResponse<Category>) {
        if (!(categoriesModel.data.isNullOrEmpty())) {
            categoriesAdapter.setList(categoriesModel.data.toMutableList())
            categoriesAdapter.selectCategory(0)
            rv_filter_types.adapter = categoriesAdapter
        }

        showCachedCategory()
    }

    private fun showCahchedData() {
        viewmodel.getCustomFinishingInput()?.let {
            Log.e("view", it.FullName.toString())
            it.ProviderName?.let {
                et_provider_name.setText(it)
            }
            it.ProviderPhone?.let {
                et_phone_step2.setText(it)
            }

            it.AdditionalInfo?.let {
                et_more_into.setText(it)
            }

            showCachedCategory()
        }

    }

    private fun showCachedCategory() {
        viewmodel.liveInput.value?.let {
            it.CategoryId?.let {
                var position = viewmodel.getCategoryPosition(it)
                categoriesAdapter.selectCategory(position)
            }
        }
    }

    override fun onItemSelected(item: Category) {
        viewmodel.categoryId = item.id
    }

}