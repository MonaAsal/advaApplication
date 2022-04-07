//package com.salamtak.app.ui.component.operations.tracking
//
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.util.DisplayMetrics
//import android.view.*
//import androidx.lifecycle.ViewModelProviders
//import com.salamtak.app.R
//import com.salamtak.app.data.Resource
//import com.salamtak.app.data.entities.responses.ActionResponse
//import com.salamtak.app.data.entities.responses.BookedOperation
//
//import com.salamtak.app.ui.ViewModelFactory
//import com.salamtak.app.ui.common.BaseDialogFragment
//import com.salamtak.app.ui.component.operations.BaseOperationsActivity
//import com.salamtak.app.utils.Constants
//import com.salamtak.app.utils.loadCircleImage
//import com.salamtak.app.utils.observe
//import com.salamtak.app.utils.toGone
//import kotlinx.android.synthetic.main.dialog_operation_review.*
//import javax.inject.Inject
//
//
//class OperationReviewDialog : BaseDialogFragment(),
//    Injectable {
//
//    @Inject
//    lateinit var operationTrackingViewModel: OperationTrackingViewModel
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory
//
//    var clicked = false
//    override fun onStart() {
//        super.onStart()
//        val lp = dialog?.window?.attributes
//        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
//        val displayMetrics = DisplayMetrics()
//        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        val height = displayMetrics.heightPixels
//        lp?.height = height
//        val window = dialog?.window
//        window?.attributes = lp
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.dialog_operation_review, container, false)
//        dialog?.window?.setGravity(Gravity.CENTER)
////        dialog?.window?.setWindowAnimations(R.style.SlideInOutDialogAnimation)
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
////        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
//        return view
//    }
//
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////        operationTrackingViewModel =
////            (activity as OperationsTrackingActivity).operationsTrackingViewModel
//
////        observeViewModel()
//        operationTrackingViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(operationTrackingViewModel::class.java)
//
//        observeViewModel()
//        initViews()
//
//    }
//
//
//    private fun initViews() {
//        tv_title.setOnClickListener { dismiss() }
//        var operation = arguments?.getParcelable<BookedOperation>(Constants.OPERATION_ITEM_KEY)
//
//        tv_operation_name.text = operation?.salamtakOperation!!.title
//
////        if(operation.salamtakOperation!!.branch?.medicalProvider != null) {
//            tv_hospital.text = operation?.salamtakOperation?.branch?.name
//            //iv_image.loadCircleImage(operation?.salamtakOperation!!.branch?.medicalProvider!!.logo)
//
////        }
//        iv_image.loadCircleImage(operation?.salamtakOperation!!.imageUrl)
////            String.format(getString(R.string._1_s_hospital), operation?.branch.medicalProvider.name)
//
//        btn_done.setOnClickListener {
//            clicked = true
//            var operation = arguments?.getParcelable<BookedOperation>(Constants.OPERATION_ITEM_KEY)
//            operationTrackingViewModel.addOperationReview(
//                operation?.id!!,
//                rb_operation?.rating!!.toInt(),
//                rb_doctor.rating.toInt(),
//                rb_hospital.rating.toInt(),
//                et_feedback.text.toString()
//            )
//        }
//
//        if (operation?.review != null) {
//            rb_doctor.rating = operation?.review?.doctorRate?.toFloat()!!
//            rb_hospital.rating = operation?.review?.medicalProviderRate?.toFloat()!!
//            rb_operation?.rating = operation?.review?.experienceRate?.toFloat()!!
//            et_feedback.setText(operation?.review?.review ?: "")
//            if (operation?.review?.isReviewed!!) {
//                if (operation?.review?.isBlocked!!) {
//                    tv_title.text = operation?.review?.blockReason
//                }
////                else {
////                    tv_title.text = getString(R.string.done)
////                }
//            }
////            else
////                tv_title.text = getString(R.string.not_reviewed_yet)
//        }
////        else {
////            tv_title.text = getString(R.string.not_reviewed_yet)
////        }
//    }
//
//    fun observeViewModel() {
//        observe(
//            viewLifecycleOwner,
//            operationTrackingViewModel.reviewResponse,
//            ::handleReviewResponse
//
//        )
//    }
//
//    private fun handleReviewResponse(resource: Resource<ActionResponse>?) {
//        if (clicked)
//            when (resource) {
//                is Resource.Loading -> (activity!! as BaseOperationsActivity).showLoadingView(progress_bar)
//                is Resource.Success -> {
//                    progress_bar.toGone()
//                    resource.data?.let {
//                        if (it.data != null)
//                            (activity as BaseOperationsActivity).showMessage(it.data!!)
//                        else if (it.message != null)
//                            (activity as BaseOperationsActivity).showMessage(it.message!!)
//
//                        dismiss()
//                        (activity as OperationsTrackingActivity).fromReviews = true
//                        (activity as OperationsTrackingActivity).onResume()
//                    }
//                }
//                is Resource.NetworkError -> {
//                    progress_bar.toGone()
//                    resource.errorCode?.let {
//                        var error = operationTrackingViewModel.getToastMessage(it)
//                        (activity as BaseOperationsActivity).showMessage(error)
//                    }
//                }
//                is Resource.DataError -> {
//                    progress_bar.toGone()
//                    resource.errorResponse?.let {
//                        (activity as BaseOperationsActivity).showServerErrorMessage(
//                            it
//                        )
//                    }
//                }
//            }
//
//    }
//
//}