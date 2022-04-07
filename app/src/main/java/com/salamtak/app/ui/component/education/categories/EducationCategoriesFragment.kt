package com.salamtak.app.ui.component.education.categories

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
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.education.categories.adapter.GenericCategoriesAdapter
import com.salamtak.app.ui.component.education.schools.SchoolsActivity
import com.salamtak.app.ui.component.education.universities.UniversitiesInstitutesActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.toolbar_back_filter.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class EducationCategoriesFragment : BaseFragment() , RecyclerItemListener<Category> {
    override val layoutId: Int
        get() = R.layout.fragment_education_categories
    val catgoriesListViewModel: EducationCategoriesViewModel  by viewModels()

    lateinit var categoriesAdapter: GenericCategoriesAdapter
    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.idlingResource

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtil.LogFirebaseEvent(
            "screen_education_categories_open",
            "screen_" + this::class.java.simpleName
        )

        activity?.let { hideKeyboard(it) }
        initViews()
        catgoriesListViewModel.getCategories(MainCategories.EDUCATION.typeId)
    }

    private fun initViews() {

        iv_toolbar_filter.toGone()

        tv_add_financial_profile.setOnClickListener { navigateToFinancialInfoScreen() }

        tv_toolbar_title.text = getString(R.string.all_categories)
        iv_toolbar_back.setOnClickListener {
            LogUtil.LogFireBaseBackEvent(this::class.java.simpleName)
            onBackPressed()
        }

        categoriesAdapter =
            GenericCategoriesAdapter(this)

        et_search!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                if (et_search!!.text.toString().isNotEmpty()) {
                    LogUtil.LogFirebaseEvent(
                        "btn_education_search",
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

        val layoutManager = GridLayoutManager(context, 2)
        rv_categories_list.layoutManager = layoutManager
        rv_categories_list.setHasFixedSize(true)
    }

    private fun bindListData(categoriesModel: SalamtakListResponse<Category>) {
        if (!(categoriesModel.data.isNullOrEmpty())) {
            categoriesAdapter.setList(categoriesModel.data.toMutableList())
            rv_categories_list.adapter = categoriesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
//        EspressoIdlingResource.decrement()
    }

    private fun navigateToDetailsScreen(navigateEvent: Event<Category>) {
        navigateEvent.getContentIfNotHandled()?.let {

            //LogFirebaseEvent(this::class.java.simpleName, "category_selected" + it.name, it.name)
            LogUtil.LogFirebaseEvent(
                "btn_education_category_select",
                "screen_" + this::class.java.simpleName,
                "selected_category_id",
                it.id.toString()
            )

            LogCategorySelectedToAdjust(it.id)


            val bundle = bundleOf("${Constants.CATEGORY_ITEM_KEY}" to it)
            navigateToSubcategoryHealthFragment(bundle)
//            startActivity(activity?.intentFor<SubcategoryActivity>(Constants.CATEGORY_ITEM_KEY to it))
        }
    }

    private fun LogCategorySelectedToAdjust(id: Int) {
        when (id) {
//            1 -> LogAdjustEvent("5pp1vb")
//            2 -> LogAdjustEvent("eo5e2y")
//            3 -> LogAdjustEvent("mvpkyk")
//            4 -> LogAdjustEvent("g5fa2c")
//            5 -> LogAdjustEvent("r1xe8y")
//            6 -> LogAdjustEvent("lrlbcg")
//            7 -> LogAdjustEvent("szakia")
//            8 -> LogAdjustEvent("9k3o8v")
//            9 -> LogAdjustEvent("w5f9k8")
//            10 -> LogAdjustEvent("rrt938")
//            11 -> LogAdjustEvent("atpvnu")
//            12 -> LogAdjustEvent("c3fpom")
//            13 -> LogAdjustEvent("gicrvq")
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


    private fun noSearchResult(unit: Unit) {
        showSearchError()
        progress_bar.toGone()
    }

    private fun handleCategoriesList(categoriesModel: SalamtakListResponse<Category>) {
        bindListData(categoriesModel)
    }

    override fun observeViewModel() {
        observe(catgoriesListViewModel.categoriesLiveData, ::handleCategoriesList)
        observe(catgoriesListViewModel.showServerError, ::showServerErrorMessage)
//        observeEvent(catgoriesListViewModel.openCategoryDetails, ::navigateToDetailsScreen)
        observe(catgoriesListViewModel.showLoading, ::showLoading)

    }

    fun showLoading(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }


    override fun onItemSelected(item: Category) {
        when (item.id!!) {
            EducationTypes.School.value -> {
                activity?.startActivity<SchoolsActivity>()
            }
            EducationTypes.University.value -> {
                startActivity(activity?.intentFor<UniversitiesInstitutesActivity>(Constants.KEY_CATEGORY_ID to EducationTypes.University.value))
            }
            EducationTypes.Institute.value -> {
                startActivity(activity?.intentFor<UniversitiesInstitutesActivity>(Constants.KEY_CATEGORY_ID to EducationTypes.Institute.value))
            }
            EducationTypes.Others.value -> {
                //activity?.startActivity<EducationActivity>()
                navigateToEducationForm()
            }
            EducationTypes.COURSES.value -> {
                startActivity(activity?.intentFor<SchoolsActivity>(Constants.KEY_CATEGORY_ID to EducationTypes.COURSES.value,"title" to item.name))
            }
        }
    }
}