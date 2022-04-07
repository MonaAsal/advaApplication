//package com.salamtak.app.ui.component.reviews
//
//import android.os.Bundle
//
//import androidx.lifecycle.ViewModelProviders
//import com.salamtak.app.R
//import com.salamtak.app.data.Resource
//import com.salamtak.app.data.entities.Review
//import com.salamtak.app.data.entities.responses.ReviewsResponse
//import com.salamtak.app.ui.ViewModelFactory
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.ui.component.reviews.adapter.ReviewsAdapter
//import com.salamtak.app.utils.Constants
//import com.salamtak.app.utils.observe
//import com.salamtak.app.utils.toGone
//import com.salamtak.app.utils.toVisible
//import kotlinx.android.synthetic.main.activity_reviews.*
//import kotlinx.android.synthetic.main.layout_reviews.rv_reviews
//import kotlinx.android.synthetic.main.toolbar.*
//import javax.inject.Inject
//
//class ReviewsActivity : BaseActivity() {
//    override val layoutId: Int
//        get() = R.layout.activity_reviews
//
//    @Inject
//    lateinit var reviewsViewModel: ReviewsViewModel
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    var type = Constants.TYPE_OPERATION_REVIEW2
//    override fun initializeViewModel() {
//        reviewsViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(ReviewsViewModel::class.java)
//    }
//
//    override fun observeViewModel() {
//        observeToast(reviewsViewModel.showToast)
//        observeError(reviewsViewModel.showError)
//        observe(reviewsViewModel.reviewsResponse, ::handleReviewsResponse)
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        when {
//            intent.extras!!.containsKey(Constants.OPERATION_ITEM_KEY) -> {
//                reviewsViewModel.getOperationsReviews(intent.getStringExtra(Constants.OPERATION_ITEM_KEY)!!)
//
//            }
//            intent.extras!!.containsKey(Constants.DOCTOR_ITEM_KEY) -> {
//                type = Constants.TYPE_DOCTOR_REVIEW
//                reviewsViewModel.getDoctorReviews(intent.getStringExtra(Constants.DOCTOR_ITEM_KEY)!!)
//            }
//            intent.extras!!.containsKey(Constants.MEDICAL_PROVIDER_ITEM_KEY) -> {
//                type = Constants.TYPE_PROVIDER_REVIEW
//                reviewsViewModel.getProviderReviews(intent.getStringExtra(Constants.MEDICAL_PROVIDER_ITEM_KEY)!!)
//            }
//        }
//
//        tv_toolbar_title.text = getString(R.string.all_reviews)
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//    }
//
//    fun handleReviewsResponse(resource: Resource<ReviewsResponse>?) {
//        when (resource) {
//            is Resource.Loading -> progress_bar.toVisible()
//            is Resource.Success -> {
//                progress_bar.toGone()
//                bindReviewsData(resource.data!!.data!!)
//            }
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = reviewsViewModel.getToastMessage(it)
//                    showErrorMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { showServerErrorMessage(it) }
//            }
//        }
//
//    }
//
//    private fun bindReviewsData(data: List<Review>) {
//        if (data == null || data!!.isEmpty()) {
//            // group_reviews.visibility = View.GONE
//        } else {
////            group_reviews.visibility = View.VISIBLE
////            tv_num_more.text = String.format(getString(R.string.num_more), data.size)
////            tv_load_more.setOnClickListener { (activity as BaseOperationsActivity).navigateToReviewsScreen(Constants.OPERATION_ITEM_KEY,medicalProvider.id) }
//
//            var adapter =
//                ReviewsAdapter(true,type)
//            adapter.setList(data!!.toMutableList())
//            rv_reviews.adapter = adapter
//        }
//    }
//
//}
