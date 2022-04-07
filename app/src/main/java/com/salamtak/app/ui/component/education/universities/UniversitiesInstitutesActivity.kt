package com.salamtak.app.ui.component.education.universities


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels

import com.salamtak.app.R
import com.salamtak.app.data.entities.University
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.EducationTypes

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.education.universities.collages.UniversityInstituteDetailsActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_universities.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject
@AndroidEntryPoint
class UniversitiesInstitutesActivity : BaseActivity(), UniversityListener {
    override val layoutId: Int
        get() = R.layout.activity_universities

//    @Inject
//    lateinit
    val viewModel: UniversitiesViewModel  by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: UniversitiesAdapter

    override fun initializeViewModel() {
//        viewModel = viewModelFactory.create(UniversitiesViewModel::class.java)
    }

    override fun observeViewModel() {

        with(viewModel)
        {
//            observe(educationByCategory, ::showEducationByCategoryResponse)
            observe(universitiesResponse, ::showUniversitiesData)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }

    }

    private fun showUniversitiesData(salamtakListResponse: SalamtakListResponse<University>) {
        showUniversities(salamtakListResponse.data)
        tv_remaining_records.text = getRemaining(
            salamtakListResponse.totalRecords,
            viewModel.page
        )
    }

//    private fun showEducationByCategoryResponse(educationSubcategoriesData: EducationSubcategoriesData) {
//        showSubCategories(educationSubcategoriesData.subCategories)
//        showSchools(educationSubcategoriesData.schools)
//    }


    private fun showUniversities(universities: List<University>) {
//        if (viewModel.page > 1) {
//            tv_remaining_records.text = getRemaining(
//                viewModel.schoolsResponse.value!!.totalRecords,
//                viewModel.page
//            )
//
//            adapter.addData(schools!!)
//        } else
        if (!(universities.isNullOrEmpty())) {
            adapter.setList(universities.toMutableList())
            rv_education_list.adapter = adapter

//            viewModel.schoolsResponse.value?.let {
//                tv_remaining_records.text = getRemaining(
//                    viewModel.schoolsResponse.value!!.totalRecords,
//                    viewModel.page
//                )

//                rv_education_list.addOnScrollListener(object :
//                    PaginationScrollListener(rv_education_list.layoutManager as LinearLayoutManager) {
//                    override fun isLastPage(): Boolean {
//                        return viewModel.isLastPage
//                    }
//
//                    override fun isLoading(): Boolean {
//                        return viewModel.isLoading
//                    }
//
//                    override fun loadMoreItems() {
//                        viewModel.page++
//                        viewModel.isLoading = true
//                        viewModel.getAllEducationBySubCategoryId()
//                    }
//                })
//            }
            showDataView(true)
        } else {
            showDataView(false)
        }

//        viewModel.isLoading = false

    }


    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
     LogUtil.LogFirebaseEvent(
            "btn_university_selected",
            "screen_" + this::class.java.simpleName
        )

        viewModel.categoryId = intent.getIntExtra(Constants.KEY_CATEGORY_ID, 0)
        tv_toolbar_title.text = getString(R.string.all_universities)
        if (viewModel.categoryId == EducationTypes.Institute.value)
            tv_toolbar_title.text = getString(R.string.all_institutes)

//        tabs.addTab(tabs.newTab().setText("American"), true)
//        tabs.addTab(tabs.newTab().setText("British"))
//        tabs.addTab(tabs.newTab().setText("National"))
//
//        tabs.setOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                setCurrentTabFragment(tab.position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

        adapter = UniversitiesAdapter(this)

        viewModel.getUniversitiesOrInstitutes()
    }


    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_education_list.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }


    override fun onUniversityClicked(university: University) {
        Log.e("school selected", university.name)
        startActivity(
            intentFor<UniversityInstituteDetailsActivity>(
                Constants.KEY_ID to university.id,
                Constants.KEY_TYPE to viewModel.categoryId
            )
        )
//        startActivity(intentFor<SchoolDetailsActivity>(Constants.KEY_SCHOOL_ID to school.id))
    }

}