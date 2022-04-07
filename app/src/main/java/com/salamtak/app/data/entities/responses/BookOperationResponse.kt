package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Booking

data class BookOperationResponse(
    val `data`: Booking
) : BasePagingResponse()
