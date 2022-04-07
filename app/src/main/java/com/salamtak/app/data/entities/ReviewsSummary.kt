package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewsSummary(val totalReviewsCount: Int, val reviews: List<Review>?) : Parcelable