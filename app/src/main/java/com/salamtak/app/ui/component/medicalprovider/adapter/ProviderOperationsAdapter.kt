package com.salamtak.app.ui.component.medicalprovider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_operation_horizontal.*
import java.math.RoundingMode


class ProviderOperationsAdapter(
    val showFavourite: Boolean,
    val listener: OperationListenerN
) : RecyclerView.Adapter<ProviderOperationsAdapter.OperationViewHolder>() {

    var operations: MutableList<Operation>? = null

    fun setList(operations: MutableList<Operation>) {
        this.operations = operations
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_operation_provider, parent, false)
        return OperationViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.bind(
            showFavourite,
            operations!![position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return if (operations != null) operations!!.size else 0
    }


    fun addData(listItems: List<Operation>) {
        var size = operations?.size!!
        operations?.addAll(listItems)
        notifyItemRangeInserted(size, operations?.size!!)
    }

    init {
        operations = ArrayList()
//        filter = OperationFilter(this@OperationsAdapter)
        //        this.mView = view;
    }

    class OperationViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        lateinit var operation: Operation

        fun bind(
            showFavourite: Boolean,
            operation: Operation,
            listener: OperationListenerN
        ) {


//            if (!showFavourite) {
////                iv_operation_favorite.visibility = View.GONE
//            } else {
            changeIconFavouriteToIsFavourite(
                iv_operation_favorite,
                operation.isFavourite ?: false
            )

            iv_operation_favorite.setOnClickListener {
                changeIconFavouriteToIsFavourite(
                    iv_operation_favorite,
                    (operation.isFavourite).not()
                )
                listener.onFavouriteClicked(operation, adapterPosition)
                operation.isFavourite = operation.isFavourite.not()
            }

//            }

            tv_operation_name.text = operation.operationName

            rb_operation.text = operation.rate.toString()

            tv_price.text =
                operation.startFrom.toBigDecimal().setScale(0, RoundingMode.UP)
                    .toInt().toString()


            operation_card.setOnClickListener {
                listener.onDetailsClicked(operation)
            }


//            if (operation.ownerDoctor == null) {
//                tv_operation_dr_name.clearUnderlined(tv_operation_dr_name.context.getString(R.string.hospital_doctor))
//                tv_operation_dr_name.setOnClickListener {
//
//                }
//            } else {
//                tv_operation_dr_name.makeUnderlined(operation.ownerDoctor!!.name)
//                tv_operation_dr_name.setOnClickListener {
//                    doctorListener.onItemSelected(operation.medicalProviderOperations[0].salamtakOperations[0].doctor!!)
//                }
//            }


        }

    }

}

