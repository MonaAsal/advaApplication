package com.salamtak.app.ui.component.health.medicalprovider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.MedicalProviderDetails
import com.salamtak.app.data.entities.Review
import com.salamtak.app.data.entities.responses.ReviewsResponse


import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.reviews.adapter.ReviewsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_medical_provider.tv_reviews
import kotlinx.android.synthetic.main.fragment_medical_provider_reviews.progress_bar
import kotlinx.android.synthetic.main.layout_reviews.*

@AndroidEntryPoint
class MedicalProviderReviewsFragment : Fragment()  {

    lateinit var medicalProvider: MedicalProviderDetails

    val medicalProviderViewModel: MedicalProviderViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medicalProvider = it.getParcelable(Constants.BRANCH_ITEM_KEY)!!
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        observe(medicalProviderViewModel.reviewsResponse, ::handleReviewsResponse)
        medicalProviderViewModel.getProviderReviews(medicalProvider.id)

    }

    override fun onResume() {
        super.onResume()
        initViews()
    }


    private fun initViews() {

//        var adapter = ContactsAdapter()
//        adapter.setList(branch.contacts.toMutableList())
//        rv_contacts.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_provider_reviews, container, false)
    }

    private fun handleReviewsResponse(resource: Resource<ReviewsResponse>?) {
        when (resource) {
            is Resource.Loading -> progress_bar.toVisible()
            is Resource.Success -> {
                progress_bar.toGone()
                bindReviewsData(resource.data!!.data!!)
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = medicalProviderViewModel.getToastMessage(it)
                    (activity as BaseOperationsActivity).showMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { (activity as BaseActivity).showServerErrorMessage(it) }
            }
        }

    }

    private fun bindReviewsData(data: List<Review>) {
        if (data!!.isEmpty()) {
            group_reviews.visibility = View.GONE
            group_more.visibility = View.GONE
        } else {
            group_reviews.visibility = View.VISIBLE
            var adapter =
                ReviewsAdapter(false, Constants.TYPE_PROVIDER_REVIEW)
            adapter.setList(data!!.toMutableList())
            rv_reviews.adapter = adapter

//            if (data.size > 1) {
            group_more.visibility = View.VISIBLE
            (activity as MedicalProviderActivity).tv_reviews.text =
                String.format(medicalProviderViewModel.getLocale(),getString(R.string.num_reviews), data.size)
            tv_num_more.text = String.format(medicalProviderViewModel.getLocale(),getString(R.string.num_more), data.size - 1)
            tv_load_more.setOnClickListener {
                (activity as BaseOperationsActivity).navigateToReviewsScreen(
                    Constants.MEDICAL_PROVIDER_ITEM_KEY,
                    medicalProvider.id
                )
            }
//            } else {
//                (activity as MedicalProviderActivity).tv_reviews.text =
//                    getString(R.string.no_reviews_yet)
////                (activity as MedicalProviderActivity).rb_medical_provider.visibility=View.GONE
//                group_more.visibility = View.GONE
//            }

        }
    }


    companion object {

        @JvmStatic
        fun newInstance(medicalProvider: MedicalProviderDetails) =
            MedicalProviderReviewsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.BRANCH_ITEM_KEY, medicalProvider)
                }
            }
    }
}
