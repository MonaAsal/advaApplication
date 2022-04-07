package com.salamtak.app.ui.component.finishing.providers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.FinishingProvider
import com.salamtak.app.data.entities.FinishingProvidersData
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.finishing.custom.CustomFinishingActivity
import com.salamtak.app.ui.component.shared.GenericCategoriesAdapter
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_finishing_providers.*
import kotlinx.android.synthetic.main.layout_selected_filter_sort.*
import kotlinx.android.synthetic.main.layout_sort_by.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import kotlinx.android.synthetic.main.toolbar_back.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity


class FinishingProvidersFragment : BaseFragment() , FinishingProviderListener,
    RecyclerItemListener<Category> {
    override val layoutId: Int
        get() = R.layout.fragment_finishing_providers

    val viewModel: FinishingViewModel by viewModels()
    lateinit var adapter: FinishingProviderAdapter
    lateinit var categoriesAdapter: GenericCategoriesAdapter

    override fun observeViewModel() {
        with(viewModel)
        {
            observe(providersResponse, ::showProvidersData)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(categoriesLiveData, ::handleCategoriesList)
        }
    }

    private fun handleCategoriesList(categoriesModel: SalamtakListResponse<Category>) {
        bindListData(categoriesModel)
    }

    private fun bindListData(categoriesModel: SalamtakListResponse<Category>) {
        if (!(categoriesModel.data.isNullOrEmpty())) {
            categoriesAdapter.setList(categoriesModel.data.toMutableList())
            rv_filter_types.adapter = categoriesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.page=1
    }

    private fun showProvidersData(salamtakListResponse: SalamtakObjectListResponse<FinishingProvidersData>) {

        val providers = salamtakListResponse.data?.finishingProviders?.toMutableList()
        providers?.let {
            tv_remaining_records.text = getRemaining(salamtakListResponse!!.totalRecords, viewModel.page)

            if (viewModel.page > 1) {
                adapter.addData(providers)
            } else {
                if (providers.isNotEmpty()) {
                    adapter.setList(providers)
                    rv_providers_list.adapter = adapter

                    rv_providers_list.addOnScrollListener(object :
                        PaginationScrollListener(rv_providers_list.layoutManager as LinearLayoutManager) {
                        override fun isLastPage(): Boolean {
                            return viewModel.isLastPage
                        }

                        override fun isLoading(): Boolean {
                            return viewModel.isLoading
                        }

                        override fun loadMoreItems() {
                            viewModel.page++
                            viewModel.isLoading = true
                            viewModel.getFinishingProviders()
                        }
                    })
                    showDataView(true)

                }
                else
                    showDataView(false)
            }
        } ?: run {
            showDataView(false)
        }
        viewModel.isLoading = false
    }

    private fun showProviders(universities: List<FinishingProvider>) {
        if (!(universities.isNullOrEmpty())) {
            adapter.setList(universities.toMutableList())
            rv_providers_list.adapter = adapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }


    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FinishingProviderAdapter(this)
        categoriesAdapter = GenericCategoriesAdapter(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        LogUtil.LogFirebaseEvent(
            "btn_finishing_provider_selected",
            "screen_" + this::class.java.simpleName
        )

        viewModel.page=1
        viewModel.getFinishingProviders()
        viewModel.getCategories(MainCategories.FINISHING.typeId)

        initViews()
    }

    private fun initViews() {
            tv_toolbar_title.text = getString(R.string.all_finishing_providers)
            iv_toolbar_back.setOnClickListener { onBackPressed() }

        btn_filter.setOnClickListener {
            if (layout_filter.visibility == View.GONE) {
                showFilter()
            } else {
                hideFilter()
                handleShowingSelectedCriteria()
            }
        }

        btn_sort.setOnClickListener {
            if (layout_sort_by.visibility == View.GONE) {
                showSortBy()

            } else {
                hideSortBy()
                handleShowingSelectedCriteria()
            }
        }

        iv_remove_sort.setOnClickListener {
            viewModel.sortBy = null
            viewModel.query = et_search.text.toString()
            viewModel.page = 1
            viewModel.getFinishingProviders()

            handleShowingSelectedCriteria()
        }

        iv_remove_filter.setOnClickListener {
            viewModel.categoryId = null
            viewModel.query = et_search.text.toString()
            viewModel.page = 1
            viewModel.getFinishingProviders()

            handleShowingSelectedCriteria()
        }

        tv_clear_all.setOnClickListener {
            viewModel.sortBy = null
            viewModel.categoryId = null

            et_search.setText("")
            viewModel.query = ""
            viewModel.page = 1
            viewModel.getFinishingProviders()

            layout_selected_criteria.toGone()
        }

        iv_low_to_high.setOnClickListener {
            hideSortBy()

            tv_sort_by.text = getString(R.string.low_to_high)
            //String.format(getString(R.string.sort_1_s), getString(R.string.low_to_high))
            viewModel.sortBy = 1
            handleShowingSelectedCriteria()
            viewModel.query = et_search.text.toString()
            viewModel.page = 1
            viewModel.getFinishingProviders()
        }

        iv_high_to_low.setOnClickListener {
            hideSortBy()
            tv_sort_by.text = getString(R.string.high_to_low)
//                String.format(getString(R.string.sort_1_s), getString(R.string.high_to_low))

            viewModel.sortBy = 2
            handleShowingSelectedCriteria()
            viewModel.query = et_search.text.toString()
            viewModel.page = 1
            viewModel.getFinishingProviders()
        }

        iv_add.setOnClickListener {
            activity?.startActivity<CustomFinishingActivity>()
            activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }

        tv_add_provider.setOnClickListener {
            activity?.startActivity<CustomFinishingActivity>()
            activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }

        et_search!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                //if (et_search!!.text.toString().isNotEmpty()) {
                LogUtil.LogFirebaseEvent(
                    "btn_finishing_search",
                    "screen_" + this::class.java.simpleName,
                    "search_key",
                    et_search!!.text.toString()
                )
                viewModel.query = et_search.text.toString()
                viewModel.page = 1
                viewModel.getFinishingProviders()

//                }

                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun hideSortBy() {
        layout_sort_by.toGone()
        layout_filter.toGone()
        layout_selected_criteria.toGone()
    }

    private fun showSortBy() {
        layout_sort_by.toVisible()
        layout_filter.toGone()
        layout_selected_criteria.toGone()
    }

    private fun hideFilter() {
        layout_filter.toGone()
        layout_sort_by.toGone()
        layout_selected_criteria.toGone()
    }

    private fun showFilter() {
        layout_filter.toVisible()
        layout_sort_by.toGone()
        layout_selected_criteria.toGone()
    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
       // tv_remaining_records.visibility = if (show) View.VISIBLE else View.GONE
        rv_providers_list.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onProviderClicked(provider: FinishingProvider) {
        Log.e("provider selected", provider.name)
//        startActivity(
//            activity?.intentFor<FinishingProviderRequestActivity>(
//                Constants.KEY_ID to provider.id,
//                Constants.KEY_TYPE to viewModel.categoryId
//            )
//        )

        var bundle = bundleOf(
            Constants.KEY_ID to provider.id,
            Constants.KEY_TYPE to viewModel.categoryId)
        navigateToFinishingProviderRequestFragment(bundle)

    }

    override fun onItemSelected(item: Category) {
        hideFilter()
        tv_filter_by.text = item.name
        viewModel.categoryId = item.id
        handleShowingSelectedCriteria()
        viewModel.page = 1
        viewModel.getFinishingProviders()
    }

    fun handleShowingSelectedCriteria() {
        if (viewModel.categoryId == null && viewModel.sortBy == null) {
            layout_selected_criteria.toGone()
        } else {
            layout_selected_criteria.toVisible()
            if (viewModel.categoryId != null) {
                group_filter.toVisible()
            } else
                group_filter.toGone()

            if (viewModel.sortBy != null) {
                group_sort.toVisible()
            } else
                group_sort.toGone()
        }

    }


}