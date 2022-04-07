package com.salamtak.app.ui.component.education.universities.collages


import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.enums.EducationTypes

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.education.universities.booking.UniversityBookingActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_collages.*
import org.jetbrains.anko.intentFor

import javax.inject.Inject
@AndroidEntryPoint
class UniversityInstituteDetailsActivity : BaseActivity(), RecyclerItemListener<Collage> {
    override val layoutId: Int
        get() = R.layout.activity_collages


    val viewModel: UniversityDetailsViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory


    override fun initializeViewModel() {
//        viewModel = viewModelFactory.create(UniversityDetailsViewModel::class.java)
    }

    override fun observeViewModel() {

        with(viewModel)
        {
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(universityDetailsResponse, ::showUniversityDetails)
        }
    }


    private fun showUniversityDetails(universityDetails: University) {
        universityDetails.rate?.let {
            rb_education_provider.rating = universityDetails.rate.toFloat()
        }
        iv_education_provider_image.changeSizeAspectRatio(0)
        iv_education_provider_image.loadImage(universityDetails.imageUrl)
        iv_education_provider_logo.loadCircleImage(universityDetails.logoUrl)
        tv_education_provider_name.text = universityDetails.name
        tv_reviews.text = String.format(getString(R.string.num_reviews), 10)
//        tv_reviews.text = String.format(
//            viewModel.getLocale(),
//            getString(R.string.num_reviews),
//            schoolDetails.reviewsCount
//        )

        tv_education_provider_details.text = universityDetails.about

        iv_back.setOnClickListener { onBackPressed() }

        if (tv_education_provider_details.lineCount > 2) {
            tv_education_provider_details.maxLines = 2
            tv_more.toVisible()
        } else {
            tv_more.toGone()
        }

        tv_more.setOnClickListener {
            if (tv_education_provider_details.maxLines > 2) {
                tv_education_provider_details.maxLines = 2
                tv_more.text = getString(R.string.see_more)
            } else {
                tv_education_provider_details.maxLines = 10
                tv_more.text = getString(R.string.view_less)

            }
        }

        universityDetails.colleges?.let {
            bindListData(it.result)
        }
    }

    private fun bindListData(departments: List<Collage>) {
        var adapter = DepartmentsAdapter(this)

        if (!(departments.isNullOrEmpty())) {
            adapter.setList(departments.toMutableList())
            rv_departments.adapter = adapter
            //showDataView(true)
        } else {
            //showDataView(false)
        }
    }


    fun showLoading(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
        } else {
            progress_bar.toGone()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.id = intent.getStringExtra(Constants.KEY_ID)!!
        viewModel.categoryId = intent.getIntExtra(Constants.KEY_TYPE, 0)
        viewModel.getUniversityInstituteDetailsById()

        if (viewModel.categoryId == EducationTypes.Institute.value)
            tv_about_education_provider_lbl.text = getString(R.string.about_the_institute)

    }

    override fun onItemSelected(item: Collage) {
        Log.e("school selected", item.name)
        Log.e("collageId", item.id)
        startActivity(
            intentFor<UniversityBookingActivity>(
                Constants.KEY_UNIVERSITY_ID to viewModel.id,
                Constants.KEY_ID to item.id,
                Constants.KEY_TYPE to viewModel.categoryId
            )
        )
    }
}