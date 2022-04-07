package com.salamtak.app.ui.component.bookingrequests.installment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.HomeVisit
import com.salamtak.app.data.entities.responses.HomeVisitResponse

import com.salamtak.app.ui.common.BaseDialogFragment
import com.salamtak.app.ui.component.health.BaseOperationsActivity
import com.salamtak.app.utils.toGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_filter.*


@AndroidEntryPoint
class InstallmentDetailsDialog : BaseDialogFragment(){


    override fun onStart() {
        super.onStart()
        val lp = dialog?.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        lp?.height = height
//        lp?.height = height - height / 9
        val window = dialog?.window
        window?.attributes = lp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_installment_details, container, false)
        dialog?.window?.setGravity(Gravity.CENTER)
//        dialog?.window?.setWindowAnimations(R.style.SlideInOutDialogAnimation)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//
//        homeVisitsViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(HomeVisitsTrackingViewModel::class.java)

        observeViewModel()

        initViews()

    }

    private fun initViews() {
        tv_title.setOnClickListener { dismiss() }
//        var visit = arguments?.getParcelable<HomeVisit>(Constants.HOME_VISIT_ITEM_KEY)
////        homeVisitsViewModel.getHomeVisit(visit!!.id)
//        showHomeVisit(visit!!)

//        LogAdjustEvent("5wmmoa","homeVisit",visit.

    }

    fun observeViewModel() {
//        observe(
//            viewLifecycleOwner,
//            homeVisitsViewModel.homeVisitLiveData,
//            ::handleVisitResponse
//
//        )
    }

    private fun handleVisitResponse(resource: Resource<HomeVisitResponse>?) {
        when (resource) {
            is Resource.Loading -> (activity!! as BaseOperationsActivity).showLoadingView(progress_bar)
            is Resource.Success -> {
                progress_bar.toGone()
//                resource.data?.data.let {
//                    showHomeVisit(resource.data?.data!!)
//                }
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
//                    var error = homeVisitsViewModel.getToastMessage(it)
//                    (activity as BaseActivity).showMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { (activity as BaseOperationsActivity).showServerErrorMessage(it) }
            }
        }
    }

    private fun showInstallmentDetails(data: HomeVisit) {
//        tv_price.text = String.format(getString(R.string.price), toDecimalNumberFormat(data.total))
//        tv_notes.text = data.notes
//
//        tv_date.text = data.preferedTime.name
//        //convertDateFormat(visit.createdAt, "dd/MM/yyyy hh:mm") // radwa, ask about the prefered time
//        tv_location.text = data.streetAddress
//
//        if (data.doctor != null) {
//            group_doctor.visibility = View.VISIBLE
//            tv_dr_name.text = data.doctor!!.name
//            tv_dr_specialization.text = data.doctor!!.currentTitle
//            rb_dr.rating = data.doctor!!.rate.toFloat()
//            iv_dr_image.loadRoundedImage(data.doctor!!.imageUrl, Constants.IMAGE_CORNER)
//        } else {
//            group_doctor.visibility = View.GONE
//        }
    }

}