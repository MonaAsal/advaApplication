package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Review

data class ReviewsResponse(
    val `data`: List<Review>
):BasePagingResponse()