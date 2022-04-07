package com.salamtak.app.ui.component.reviews.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Review
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.convertDateFormatTimeAgo
import com.salamtak.app.utils.loadCircleImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_review.*


/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class ReviewViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        review: Review, type: Int, isLastItem: Boolean
    ) {
        tv_review_owner_name.text = review.patient?.name
        tv_review_text.text = review.review
        when (type) {
            Constants.TYPE_DOCTOR_REVIEW ->
                rb_operation.rating = review.doctorRate?.toFloat() ?: 0f
            Constants.TYPE_OPERATION_REVIEW ->
                rb_operation.rating = review.experienceRate?.toFloat() ?: 0f
            Constants.TYPE_PROVIDER_REVIEW ->
                rb_operation.rating = review.medicalProviderRate?.toFloat() ?: 0f
            Constants.TYPE_OPERATION_REVIEW2 -> {
                rb_operation.rating = review.rate?.toFloat() ?: 0f
//                if (!isLastItem)
//                    separator.visibility = View.VISIBLE
            }

        }

        tv_review_time.text = convertDateFormatTimeAgo(review.createdAt, "yyyy-MM-dd'T'HH:mm:ss")

        iv_review_owner_image.loadCircleImage(
            review.patient?.imageUrl, R.drawable.ic_avatar
        )
    }


}

