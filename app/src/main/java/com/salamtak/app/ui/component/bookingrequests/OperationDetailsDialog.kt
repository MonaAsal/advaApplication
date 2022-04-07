package com.salamtak.app.ui.component.bookingrequests

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.BookedOperation

import com.salamtak.app.ui.common.BaseDialogFragment
import com.salamtak.app.ui.component.requests.RequestsFragment
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_filter.tv_title
import kotlinx.android.synthetic.main.dialog_operation_details.*


@AndroidEntryPoint
class OperationDetailsDialog : BaseDialogFragment(){

    val operationTrackingViewModel: BookedRequestsViewModel  by viewModels()

//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

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
        val view = inflater.inflate(R.layout.dialog_operation_details, container, false)
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

        observeViewModel()

        initViews()

    }

    private fun initViews() {
        tv_title.setOnClickListener { dismiss() }
        var bookedOperation =
            arguments?.getParcelable<BookedOperation>(Constants.KEY_OPERATION_ITEM)
//        operationTrackingViewModel.getHomeVisit(visit!!.id)
        showOperation(bookedOperation!!)

        //   tv_operation_name.text = bookedOperation.currentStatusNote
        iv_close.setOnClickListener { dismiss() }

    }

    fun observeViewModel() {
//        observe(
//            viewLifecycleOwner,
//            operationTrackingViewModel.bookedOperationsLiveData,
//            ::handleOperationResponse
//
//        )
    }

//    private fun handleOperationResponse(resource: Resource<>?) {
//        when (resource) {
//            is Resource.Loading -> (activity!! as BaseActivity).showLoadingView(progress_bar)
//            is Resource.Success -> {
//                progress_bar.toGone()
//                resource.data?.data.let {
//                    showOperation(resource.data?.data!!)
//                }
//            }
//            is Resource.NetworkError -> {
//                progress_bar.toGone()
//                resource.errorCode?.let {
//                    var error = operationTrackingViewModel.getToastMessage(it)
//                    (activity as BaseActivity).showMessage(error)
//                }
//            }
//            is Resource.DataError -> {
//                progress_bar.toGone()
//                resource.errorResponse?.let { operationTrackingViewModel.showError(it) }
//            }
//        }
//    }

    private fun showOperation(data: BookedOperation) {
        tv_status.text = resources.getStringArray(R.array.operation_status)[data.currentStatus]
        if (data.currentStatusNote != null && data.currentStatusNote.isNotEmpty()) {
            tv_notes.toVisible()
            tv_notes.text =
                data.currentStatusNote //String.format(getString(R.string.missing_documents), data.currentStatusNote)
        } else
            tv_notes.visibility = View.GONE

//        tv_price.text = String.format(getString(R.string.price), toDecimalNumberFormat(data.total))
//
//        tv_notes.text = data.notes
//
//        tv_date.text = data.preferedTime.name
//        //convertDateFormat(visit.createdAt, "dd/MM/yyyy hh:mm") // radwa, ask about the prefered time
//        tv_location.text = data.streetAddress
//
//        if (data.doctor != null) {
//            tv_dr_name.text = data.doctor!!.name
//            tv_dr_specialization.text = data.doctor!!.specialization.name
//            rb_dr.rating = data.doctor!!.rate.toFloat()
//            iv_dr_image.loadRoundedImage(data.doctor!!.imageUrl, Constants.IMAGE_CORNER)
//        }
    }

}