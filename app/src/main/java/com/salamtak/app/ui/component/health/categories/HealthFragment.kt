package com.salamtak.app.ui.component.health.categories

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.test.espresso.IdlingResource
import com.google.android.material.snackbar.Snackbar
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.categories.adapter.CategoriesAdapter
import com.salamtak.app.ui.component.health.filter.FilterDialog
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.card_health.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_back_filter.*
import kotlinx.android.synthetic.main.toolbar_back_filter.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar_back_filter.tv_toolbar_title

@AndroidEntryPoint
class HealthFragment : BaseFragment(), RecyclerItemListener<Category> {

    override val layoutId: Int
        get() = R.layout.fragment_health

    val catgoriesListViewModel: CategoriesViewModel by viewModels()
    lateinit var categoriesAdapter: CategoriesAdapter
    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.idlingResource


    override fun onResume() {
        preventUiPopingAboveKeyBoard()
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LogAdjustEvent("e09oq6")
        activity?.let { hideKeyboard(it) }
        initViews()
        catgoriesListViewModel.getCategories(MainCategories.HEALTH.typeId)
        observeSnackBarMessages(catgoriesListViewModel.showSnackBar)

    }


    private fun initViews() {
//        if (catgoriesListViewModel.shouldAddFinancialInfo())
//            tv_add_financial_profile.visibility = View.VISIBLE
//        else
//            tv_add_financial_profile.visibility = View.GONE

//        iv_toolbar_filter.toGone()
      /*  iv_toolbar_filter.setOnClickListener {
            openFilterDialog()
        }*/
        tv_add_financial_profile.setOnClickListener {
            navigateToFinancialInfoScreen()
        }

        tv_toolbar_title.text = getString(R.string.health)
        iv_toolbar_back.setOnClickListener {
            LogUtil.LogFireBaseBackEvent(this::class.java.simpleName)
            onBackPressed()
        }

        btn_custom_operation.setOnClickListener { navigateToOtherOperations() }

        categoriesAdapter = CategoriesAdapter(this)


        et_search!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                if (et_search!!.text.toString().isNotEmpty()) {
                    LogUtil.LogFirebaseEvent(
                        "btn_search",
                        "screen_" + this::class.java.simpleName,
                        "search_key",
                        et_search!!.text.toString()
                    )

                    openSearchScreen(et_search.text.toString())
                }

                return@OnEditorActionListener true
            }
            false
        })

        val layoutManager = GridLayoutManager(activity, 2)
        rv_categories_list.layoutManager = layoutManager
        rv_categories_list.setHasFixedSize(true)
    }

    private fun bindListData(categoriesModel: SalamtakListResponse<Category>) {
        if (!(categoriesModel.data.isNullOrEmpty())) {
            var list = categoriesModel.data.toMutableList()
//            list.add(
//                Category(
//                    0,
//                    "image",
//                    getString(R.string.customized_operation),
//                    1,
//                    null,
//                    "",
//                    ""
//                )
//            )
            categoriesAdapter.setList(list)
            rv_categories_list.adapter = categoriesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
//        EspressoIdlingResource.decrement()
    }

    private fun openFilterDialog() {
        val fm = activity?.supportFragmentManager
        val dialog = FilterDialog()
        val bundle = Bundle()
        dialog.arguments = bundle

        fm?.let { dialog.show(it, getString(R.string.app_name)) }
    }

    private fun navigateToDetailsScreen(category: Category) {
//        navigateEvent.getContentIfNotHandled()?.let {

        LogCategorySelectedToAdjust(category.id)
        if (category.id != 0) {
            LogUtil.LogFirebaseEvent(
                "btn_category_select",
                "screen_" + this::class.java.simpleName,
                "selected_category_id",
                category.id.toString()
            )
            val bundle = bundleOf("${Constants.CATEGORY_ITEM_KEY}" to category)
            navigateToSubcategoryHealthFragment(bundle)
            //  startActivity(activity?.intentFor<SubcategoryActivity>(Constants.CATEGORY_ITEM_KEY to category))
        } else {
            LogAdjustEvent("2ckym9")
            LogUtil.LogFirebaseEvent(
                "btn_home_visits",
                "screen_" + this::class.java.simpleName
            )
            navigateToMedicalNetworkFragment()
            // activity?.startActivity<MedicalNetworkActivity>()
        }
    }

    private fun LogCategorySelectedToAdjust(id: Int) {
        when (id) {
            1 -> LogAdjustEvent("5pp1vb")
            2 -> LogAdjustEvent("eo5e2y")
            3 -> LogAdjustEvent("mvpkyk")
            4 -> LogAdjustEvent("g5fa2c")
            5 -> LogAdjustEvent("r1xe8y")
            6 -> LogAdjustEvent("lrlbcg")
            7 -> LogAdjustEvent("szakia")
            8 -> LogAdjustEvent("9k3o8v")
            9 -> LogAdjustEvent("w5f9k8")
            10 -> LogAdjustEvent("rrt938")
            11 -> LogAdjustEvent("atpvnu")
            12 -> LogAdjustEvent("c3fpom")
            13 -> LogAdjustEvent("gicrvq")
        }

    }

    fun observeSnackBarMessages(event: LiveData<Event<Int>>) {
        rv_categories_list.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }


    private fun showSearchError() {
        catgoriesListViewModel.showSnackbarMessage(R.string.search_error)
    }

    private fun showDataView(show: Boolean) {
        tv_no_data.visibility = if (show) View.GONE else View.VISIBLE
        rv_categories_list.visibility = if (show) View.VISIBLE else View.GONE
        progress_bar.toGone()
    }


    private fun showSearchResult(item: Category) {
        catgoriesListViewModel.openCategoryDetails(item)
        progress_bar.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        progress_bar.toGone()
    }

    private fun handleCategoriesList(categoriesModel: Resource<SalamtakListResponse<Category>>) {
        when (categoriesModel) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> categoriesModel.data?.let { bindListData(it) }
            is Resource.NetworkError -> {
                showDataView(false)
                categoriesModel.errorCode?.let {
                    var error = catgoriesListViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                showDataView(false)
                categoriesModel.errorResponse?.let { showServerErrorMessage(it) }
            }
        }

    }

    override fun observeViewModel() {
        observe(catgoriesListViewModel.categoriesLiveData, ::handleCategoriesList)
        observe(catgoriesListViewModel.noSearchFound, ::noSearchResult)
        observeToast(catgoriesListViewModel.showToast)
        observeError(catgoriesListViewModel.showError)

    }

    override fun onItemSelected(item: Category) {
        navigateToDetailsScreen(item)
    }


}