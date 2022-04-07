package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Offer

data class OffersResponse(
    val `data`: List<Offer>
):BaseResponse()