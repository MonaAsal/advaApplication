package com.salamtak.app.ui.component.bookingrequests

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.ui.common.BaseDialogFragment
import com.salamtak.app.ui.component.requests.RequestsFragment
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_cancel_operation.*
import kotlinx.android.synthetic.main.dialog_filter.progress_bar
import kotlinx.android.synthetic.main.dialog_filter.tv_title

@AndroidEntryPoint
class OperationCancelDialog() : BaseDialogFragment() {
    val operationTrackingViewModel: BookedRequestsViewModel by viewModels()

//    @Inject
//    lateinit var requestsFragment: RequestsFragment
    var requestsFragment: RequestsFragment = RequestsFragment()


    var requestSent = false
    override fun onStart() {
        super.onStart()
        val lp = dialog?.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        val displayMetrics = DisplayMetrics()
        requireActivity()!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        lp?.height = height
        val window = dialog?.window
        window?.attributes = lp

        observeViewModel()
        initViews()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_cancel_operation, container, false)
        dialog?.window?.setGravity(Gravity.CENTER)
//        dialog?.window?.setWindowAnimations(R.style.SlideInOutDialogAnimation)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        operationTrackingViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(BookedRequestsViewModel::class.java)


    }




    override fun onResume() {
        super.onResume()
        btn_cancel.isEnabled = true
    }

    private fun initViews() {
        tv_title.setOnClickListener {
            dismiss()
        }

        btn_cancel.setOnClickListener {
           LogAdjustEvent("mdxlag")
           // (activity as BaseOperationsActivity).LogAdjustEvent("mdxlag")

            requestSent = true
            var bookedOperation = arguments?.getParcelable<BookedOperation>(Constants.KEY_OPERATION_ITEM)
            operationTrackingViewModel.cancelOperation(bookedOperation!!)
            btn_cancel.isEnabled = false
        }

        btn_back.setOnClickListener { dismiss() }
    }

    fun observeViewModel() {
        observe(viewLifecycleOwner, operationTrackingViewModel.showLoading, ::showLoadingView)
        observe(viewLifecycleOwner, operationTrackingViewModel.showServerError, ::showServerErrorMessage)
        observe(viewLifecycleOwner, operationTrackingViewModel.cancelOperationResponseLiveData, ::handleCancelOperationResponse)
    }

    private fun showServerErrorMessage(errorResponse: ErrorResponse) {
        dialog?.dismiss()
        if (requestsFragment.isResumed){
          requestsFragment.showServerErrorMessage(errorResponse)

        }

    }

    private fun showLoadingView(b: Boolean) {
        requestsFragment.showLoadingView(progress_bar,b)

    }

    private fun handleCancelOperationResponse(response: ActionResponse) {

        if (response.status) {
            if (requestSent) {
              // (activity as BaseOperationsActivity).onResume()
              //  requestsFragment.reloadData()
              //  requestsFragment.onResume()
                dialog?.dismiss()
                activity?.findNavController(R.id.nav_host_fragment)?.navigate(R.id.bookingFragment)

                btn_cancel.isEnabled = true
                requestSent = false
            }

        }


    }

}

