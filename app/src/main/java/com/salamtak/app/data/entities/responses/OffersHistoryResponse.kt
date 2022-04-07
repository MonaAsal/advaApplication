package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.OfferHistory

data class OffersHistoryResponse(
    val `data`: List<OfferHistory>

) : BasePagingResponse()