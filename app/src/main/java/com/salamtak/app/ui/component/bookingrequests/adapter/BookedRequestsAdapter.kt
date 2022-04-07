package com.salamtak.app.ui.component.bookingrequests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.DoctorBase
import com.salamtak.app.data.entities.responses.BookedOperation
import com.salamtak.app.data.enums.InstallmentTypes
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.bookingrequests.BookedRequestsViewModel
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.loadRoundedImage
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_operation_tracking.*
import java.util.*

/**
 * Created by RadwaElsahn on 4/30/2020.
 */

class OperationsTrackingAdapter(
    val operationTrackingViewModel: BookedRequestsViewModel
) : RecyclerView.Adapter<OperationTrackingViewHolder>(),
    Filterable {

    var operations: MutableList<BookedOperation>? = null

    fun setList(operations: MutableList<BookedOperation>) {
        this.operations = operations
    }

    private val onItemClickListener: RecyclerItemListener<BookedOperation> =
        object : RecyclerItemListener<BookedOperation> {

            override fun onItemSelected(operation: BookedOperation) {
                operationTrackingViewModel?.onOperationItemClicked(operation)
            }

        }

    private val onPayClickListener: RecyclerItemListener<BookedOperation> =
        object : RecyclerItemListener<BookedOperation> {

            override fun onItemSelected(operation: BookedOperation) {
                operationTrackingViewModel?.onPayClicked(operation)
            }

        }

    private val onMoreClickListener: RecyclerItemListener<BookedOperation> =
        object : RecyclerItemListener<BookedOperation> {
            override fun onItemSelected(item: BookedOperation) {
                operationTrackingViewModel?.onMoreClicked(item)
            }
        }

    private val onReviewOperationClicked: RecyclerItemListener<BookedOperation> =
        object : RecyclerItemListener<BookedOperation> {
            override fun onItemSelected(item: BookedOperation) {
                operationTrackingViewModel?.onReviewOperationClicked(item)
            }
        }

    private val onDoctorListener: RecyclerItemListener<DoctorBase> =
        object : RecyclerItemListener<DoctorBase> {
            override fun onItemSelected(item: DoctorBase) {
                operationTrackingViewModel?.onDoctorClicked(item)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationTrackingViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_operation_tracking, parent, false)
        return OperationTrackingViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: OperationTrackingViewHolder, position: Int) {

        holder.bind(
            operations!![position],
            onItemClickListener,
            onReviewOperationClicked,
            onMoreClickListener,
            onDoctorListener, onPayClickListener,
            position
        )
    }

    override fun getItemCount(): Int {
        return if (operations != null) operations!!.size else 0
    }

    override fun getFilter(): Filter {
        return filter
    }

    fun addData(list: MutableList<BookedOperation>) {
        var size = operations?.size!!
        this.operations?.addAll(list)
        notifyItemRangeInserted(size, operations?.size!!)
    }


    init {
        operations = ArrayList()
    }
}

class OperationTrackingViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        bookedOperation: BookedOperation,
        recyclerItemListener: RecyclerItemListener<BookedOperation>,
        onReviewOperationClicked: RecyclerItemListener<BookedOperation>,
        moreClickListener: RecyclerItemListener<BookedOperation>,
        doctorListener: RecyclerItemListener<DoctorBase>,
        onPayClickListener: RecyclerItemListener<BookedOperation>,
        position: Int
    ) {

        tv_operation_name.text = bookedOperation.name

        tv_payment_info.setOnClickListener {
            recyclerItemListener.onItemSelected(bookedOperation)
        }

        tv_operation_owner.text = bookedOperation.provider

        tv_operation_status.text =
            itemView.context.resources.getStringArray(R.array.operation_status)[bookedOperation.currentStatus]
        // Log.e("status" ,bookedOperation.currentStatus.toString() + " " + itemView.context.resources.getStringArray(R.array.operation_status)[bookedOperation.currentStatus])
        tv_operation_reserve_date.text = bookedOperation.creationTime
//            convertDateFormat(bookedOperation.creationTime, "yyyy-MM-dd HH:mm")

//        if (bookedOperation.currentStatus == 0)
//            iv_more.toVisible()
//        else
//            iv_more.toGone()

        iv_operation_image.loadRoundedImage(
            bookedOperation.imageUrl
        )

        when (bookedOperation.bookingType) {
            InstallmentTypes.OPERATION.typeId -> {
                iv_type.setImageResource(R.drawable.ic_health_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_health
//                )
            }
            InstallmentTypes.EDUCATION.typeId -> {
                iv_type.setImageResource(R.drawable.ic_education_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_education
//                )
            }
            InstallmentTypes.INSURANCE.typeId -> {
                iv_type.setImageResource(R.drawable.ic_insurance_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_insurance
//                )
            }
            InstallmentTypes.WEDDING.typeId -> {
                iv_type.setImageResource(R.drawable.ic_wedding_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_wedding
//                )
            }
            InstallmentTypes.FINISHING.typeId -> {
                iv_type.setImageResource(R.drawable.ic_finishing_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_finishing
//                )
            }

            InstallmentTypes.CARS.typeId -> {
                iv_type.setImageResource(R.drawable.ic_car_service_gray)
//                iv_operation_image.loadCircleImage(
//                    bookedOperation.imageUrl,
//                    R.drawable.ic_finishing
//                )
            }

        }

        if (bookedOperation.isPremiumPayment && bookedOperation.isPaidWithPremium.not()) {
            btn_pay.toVisible()
        } else {
            btn_pay.toGone()
        }

        btn_pay.setOnClickListener {
            onPayClickListener.onItemSelected(bookedOperation)
        }

//        tv_pay.setOnClickListener { onPayClickListener.onItemSelected(bookedOperation) }

        iv_more.setOnClickListener {
//            iv_more.isEnabled = false
            moreClickListener.onItemSelected(bookedOperation)

//            val handler = Handler()
//            handler.postDelayed({
//                iv_more.isEnabled = true
//            }, 2500)

        }
    }
}
