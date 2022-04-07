package com.salamtak.app.ui.component.reviews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Review

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class ReviewsAdapter(val card: Boolean, val type: Int) : RecyclerView.Adapter<ReviewViewHolder>() {

    lateinit var reviews: MutableList<Review>

    fun setList(reviews: MutableList<Review>) {
        this.reviews = reviews
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view =
            if (card)
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_review_card, parent, false)
            else
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {

        holder.bind(reviews!![position], type, position == reviews.size - 1)
    }

    override fun getItemCount(): Int {
        return if (reviews != null) reviews!!.size else 0
    }


}

