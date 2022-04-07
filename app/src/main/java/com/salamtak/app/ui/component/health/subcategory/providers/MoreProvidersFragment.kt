package com.salamtak.app.ui.component.health.subcategory.providers

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.categoryproviders.MedicalProvidersAdapter
import com.salamtak.app.ui.component.health.categoryproviders.ProvidersViewModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import kotlinx.android.synthetic.main.activity_more_providers.*
import kotlinx.android.synthetic.main.toolbar_back_filter.*


class MoreProvidersFragment : BaseFragment() , RecyclerItemListener<MedicalProvider> {
    override val layoutId: Int
        get() = R.layout.fragment_more_providers

    val providersViewModel: ProvidersViewModel by viewModels()

    lateinit var adapter: MedicalProvidersAdapter

    override fun observeViewModel() {
        with(providersViewModel)
        {
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(providers, ::showProviders)
            observe(openMedicalProviderProfile, ::openMedicalProviderProfile)
            observe(openDoctorProfile, ::openDoctorProfile)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null){
            arguments?.let {
                providersViewModel.subcategoryId = it.getString(Constants.KEY_SUBCATEGORY_ID)!!
                tv_toolbar_title.text = it.getString(Constants.KEY_NAME)!!
            }
        }

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        providersViewModel.getMoreDoctors()
        adapter = MedicalProvidersAdapter(this)
    }

    fun openMedicalProviderProfile(medicalProvider: MedicalProvider) {
        val bundle = bundleOf(Constants.KEY_ID to medicalProvider!!.id)
        navigateToMedicalProvider(bundle)
    }

    fun openDoctorProfile(medicalProvider: MedicalProvider) {
        val bundle = bundleOf(Constants.DOCTOR_ITEM_KEY to medicalProvider!!.id)
        navigateToDoctor(bundle)
    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showProviders(providers: List<MedicalProvider>) {
        tv_remaining_records.text = getRemaining(
            providersViewModel.moreProvidersResponse.value!!.totalRecords,
            providersViewModel.page
        )
        if (providersViewModel.page > 1) {
            adapter.addData(providers!!)
        } else if (!(providers.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                providersViewModel.moreProvidersResponse.value!!.totalRecords,
                providersViewModel.page
            )

            adapter.setList(providers.toMutableList())
            rv_providers_list.adapter = adapter

            rv_providers_list.addOnScrollListener(object :
                PaginationScrollListener(rv_providers_list.layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return providersViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return providersViewModel.isLoading
                }

                override fun loadMoreItems() {
                    providersViewModel.page++
                    providersViewModel.isLoading = true
                    providersViewModel.getMoreDoctors()
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }

        providersViewModel.isLoading = false

    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_providers_list.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onItemSelected(item: MedicalProvider) {
        providersViewModel.itemSelected(item)
    }
}