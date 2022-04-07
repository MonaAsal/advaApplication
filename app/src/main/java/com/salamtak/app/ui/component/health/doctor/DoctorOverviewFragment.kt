package com.salamtak.app.ui.component.health.doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.DoctorDetails
import com.salamtak.app.data.entities.Review
import com.salamtak.app.data.entities.responses.ReviewsResponse


import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.ui.component.reviews.adapter.ReviewsAdapter
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_doctor.*
import kotlinx.android.synthetic.main.fragment_doctor_overview.*
import kotlinx.android.synthetic.main.layout_reviews.*

@AndroidEntryPoint
class DoctorOverviewFragment : Fragment()  {

    lateinit var doctor: DoctorDetails

     val doctorViewModel: DoctorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctor = it.getParcelable(Constants.DOCTOR_ITEM_KEY)!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        tv_about.text = doctor.bio

        tv_rate.text = "75%"//radwa
        tv_excellent_percent.text = "90%"
        tv_moderate_percent.text = "5%"
        tv_fair_percent.text = "5%"
        pb_excellent.progress = 90//radwa
        pb_moderate.progress = 5//doctor.rate
        pb_fair.progress = 5//doctor.rate
//        tv_rate.text =
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_overview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        doctorViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(DoctorViewModel::class.java)

//        observe(doctorViewModel.reviewsResponse, ::handleReviewsResponse)

        doctorViewModel.getDoctorReviews(doctor.id)


    }


    companion object {

        @JvmStatic
        fun newInstance(param1: DoctorDetails) =
            DoctorOverviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.DOCTOR_ITEM_KEY, param1)
                }
            }
    }

    private fun handleReviewsResponse(resource: Resource<ReviewsResponse>?) {
        when (resource) {
            is Resource.Success -> {
                bindReviewsData(resource.data!!.data!!)
            }
        }

    }

    private fun bindReviewsData(data: List<Review>) {

        if (data == null || data!!.isEmpty()) {
            group_reviews.visibility = View.GONE
            group_more.visibility = View.GONE
        } else {
            group_reviews.visibility = View.VISIBLE
            var adapter =
                ReviewsAdapter(false, Constants.TYPE_DOCTOR_REVIEW)
            adapter.setList(data!!.toMutableList())
            rv_reviews.adapter = adapter
//            if (data.size > 1) {
                group_more.visibility = View.VISIBLE

//                (activity as DoctorActivity).tv_reviews.text =
//                    String.format(doctorViewModel.getLocale(),getString(R.string.num_reviews), data.size)

                tv_num_more.text = String.format(doctorViewModel.getLocale(),getString(R.string.num_more), data.size - 1)
                tv_load_more.setOnClickListener {
                    (activity as BaseOperationsActivity).navigateToReviewsScreen(
                        Constants.DOCTOR_ITEM_KEY,
                        doctor.id
                    )
                }
//            } else {
//                (activity as DoctorActivity).tv_reviews.text = getString(R.string.no_reviews_yet)
//                group_more.visibility = View.GONE
//            }

        }
    }
}
