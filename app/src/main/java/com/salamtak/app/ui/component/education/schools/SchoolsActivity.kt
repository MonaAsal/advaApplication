package com.salamtak.app.ui.component.education.schools


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.EducationSubCategory
import com.salamtak.app.data.entities.EducationSubcategoriesData
import com.salamtak.app.data.entities.School
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.data.enums.EducationTypes
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.education.schools.schooldetails.SchoolDetailsActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.addVerticalItemDecoration
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_schools.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class SchoolsActivity : BaseActivity(), SchoolListener {
    override val layoutId: Int
        get() = R.layout.activity_schools

    val viewModel: SchoolsViewModel by viewModels()

    lateinit var adapter: SchoolsAdapter
    lateinit var subcategoriesAdapter: EducationSubCategoryAdapter

    override fun initializeViewModel() {
    }

    override fun observeViewModel() {

        with(viewModel)
        {
            observe(educationByCategory, ::showEducationBuCategoryResponse)
            observe(schoolsResponse, ::showSchoolsList)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }

    }

    private fun showSchoolsList(salamtakListResponse: SalamtakListResponse<School>) {
        showSchools(salamtakListResponse.data)
        tv_remaining_records.text = getRemaining(
            viewModel.schoolsResponse.value!!.totalRecords,
            viewModel.page
        )
    }

    private fun showEducationBuCategoryResponse(educationSubcategoriesResponse: SalamtakObjectListResponse<EducationSubcategoriesData>) {
        educationSubcategoriesResponse.data?.let {
            showSubCategories(it.subCategories)
            showSchools(it.schools)
            tv_remaining_records.text = getRemaining(
                educationSubcategoriesResponse!!.totalRecords,
                viewModel.page
            )
        }

    }

    private fun showSubCategories(subCategories: List<EducationSubCategory>) {
        subcategoriesAdapter.setList(subCategories.toMutableList())
        rv_education_subcategories.adapter = subcategoriesAdapter

        addVerticalItemDecoration(rv_education_subcategories)
    }


    private fun showSchools(schools: List<School>) {
//        if (viewModel.page > 1) {
//
//            adapter.addData(schools!!)
//        } else
        if (!(schools.isNullOrEmpty())) {
            adapter.setList(schools.toMutableList())
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

        viewModel.categoryId = intent.getIntExtra(Constants.KEY_CATEGORY_ID, 0)
        LogUtil.LogFirebaseEvent(
            "screen_schools_open",
            "screen_" + this::class.java.simpleName
        )

        iv_toolbar_back.setOnClickListener { onBackPressed() }

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

        adapter = SchoolsAdapter(this)
        subcategoriesAdapter = EducationSubCategoryAdapter(this)
        viewModel.subCategoryId = 1

        if (viewModel.categoryId == EducationTypes.COURSES.value) {
            tv_toolbar_title.text = getString(R.string.all_courses)
            viewModel.getAllEducationByCategoryId(EducationTypes.COURSES.value)

        } else {
            tv_toolbar_title.text = getString(R.string.all_schools)
            viewModel.getAllEducationByCategoryId(EducationTypes.School.value)

        }


    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_education_list.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }

    override fun onSchoolClicked(school: School) {
        Log.e("school selected", school.name)
        startActivity(intentFor<SchoolDetailsActivity>(Constants.KEY_SCHOOL_ID to school.id, Constants.KEY_CATEGORY_ID to EducationTypes.COURSES.value))
    }

    override fun onSubcategoryClicked(subCategory: EducationSubCategory) {
        Log.e("subcategory selected", subCategory.name)
        subcategoriesAdapter.selectSubcategory(subCategory)
        viewModel.subCategoryId = subCategory.id
        viewModel.getAllEducationBySubCategoryId()
    }

}