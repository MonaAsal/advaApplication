package com.salamtak.app.ui.component.finishing.providers

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingCategoryModel
import com.salamtak.app.data.entities.FinishingProvidersModel
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.finishing.adapters.FinishingCategoriesAdapter
import com.salamtak.app.ui.component.finishing.interfaces.FinishingCategoryProviderListener
import com.salamtak.app.ui.component.finishing.interfaces.FinishingViewAllListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import kotlinx.android.synthetic.main.fragment_finishing_categories.*
import kotlinx.android.synthetic.main.fragment_providers_list.*
import kotlinx.android.synthetic.main.fragment_subcategories_list.group_no_result
import kotlinx.android.synthetic.main.fragment_subcategories_list.progress_bar
import kotlinx.android.synthetic.main.fragment_subcategories_list.remaining_records_tv
import kotlinx.android.synthetic.main.toolbar_back.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar_back.tv_toolbar_title

class FinishingCategoriesFragment : BaseFragment(),
    FinishingViewAllListener<FinishingCategoryModel>,
    FinishingCategoryProviderListener<FinishingProvidersModel> {

    override val layoutId: Int
        get() = R.layout.fragment_finishing_categories

    val finishingCategoriesViewModel: FinishingCategoriesViewModel by viewModels()
    lateinit var finishingCategoryAdapter: FinishingCategoriesAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    var state: Parcelable? = null //to save recycler focus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finishingCategoriesViewModel.page = 1
        finishingCategoriesViewModel.getFinishingCategories()
        finishingCategoryAdapter = FinishingCategoriesAdapter(this, this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        mLayoutManager = LinearLayoutManager(context)
        restoreRecyclerPosition() //restore recycler focus
    }

    fun initViews() {
        tv_toolbar_title.text = getString(R.string.finishing)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
    }

    fun restoreRecyclerPosition() {
        if (state != null) {
            mLayoutManager.onRestoreInstanceState(state);
        }
    }

    override fun onPause() {
        super.onPause()
        state = mLayoutManager.onSaveInstanceState() // save recycler focus
    }

    override fun onResume() {
        super.onResume()
        Log.d("finishing state", "onResume")
        dataObservation()
        restoreRecyclerPosition()
    }

    private fun dataObservation() {
        finishingCategoriesViewModel.page = 1
        with(finishingCategoriesViewModel)
        {
            observe(finishingCategoriesList, ::showFinishingCategories)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }
    }

    override fun observeViewModel() {
        with(finishingCategoriesViewModel)
        {
            observe(finishingCategoriesList, ::showFinishingCategories)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("state", "onViewStateRestored")

        with(finishingCategoriesViewModel)
        {
            observe(finishingCategoriesList, ::showFinishingCategories)
        }

    }

    fun showFinishingCategories(finishingCategoriesList: List<FinishingCategoryModel>?) {
        if (finishingCategoriesViewModel.page > 1) {
            remaining_records_tv.text = getRemaining(
                finishingCategoriesViewModel.finishingCategorieResponse.value!!.totalRecords,
                finishingCategoriesViewModel.page
            )
            finishingCategoryAdapter.addData(finishingCategoriesList!!)

        } else if (!(finishingCategoriesList.isNullOrEmpty())) {
            showprovidersNumbers(finishingCategoriesViewModel.finishingCategorieResponse.value!!.totalRecords)

            finishingCategoriesList?.toMutableList()?.let {
                finishingCategoryAdapter.setList(it)
            }
            rv_finishin_categories.adapter = finishingCategoryAdapter
            rv_finishin_categories.layoutManager = mLayoutManager
            rv_finishin_categories.addOnScrollListener(object :
                PaginationScrollListener(mLayoutManager) {
                override fun isLastPage(): Boolean {
                    return finishingCategoriesViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return finishingCategoriesViewModel.isLoading
                }

                override fun loadMoreItems() {
                    finishingCategoriesViewModel.page++
                    finishingCategoriesViewModel.isLoading = true
                    finishingCategoriesViewModel.getFinishingCategories()

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        state = mLayoutManager.onSaveInstanceState()
                    }
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }

        finishingCategoriesViewModel.isLoading = false

    }

    private fun showprovidersNumbers(totalRecords: Int) {
        providerServicesNum.text = totalRecords.toString()

    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_finishin_categories.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    //view all ...
    override fun onClick(item: FinishingCategoryModel) {
        val bundle = bundleOf(Constants.KEY_ID to item.id)
        navigateToFinishing(bundle)
    }

    //on Item clicked...
    override fun onProviderClick(provider: FinishingProvidersModel) {
        var bundle = bundleOf(Constants.KEY_ID to provider.id, Constants.KEY_TYPE to null)
        navigateToFinishingProviderRequestFragment(bundle)
    }

}

