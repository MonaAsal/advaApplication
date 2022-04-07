package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Promotions

data class PromotionsResponse(
    val `data`: List<Promotions>
):BasePagingResponse()