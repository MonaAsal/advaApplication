package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Advertisement

data class AdvertisementsResponse(
    val `data`: List<Advertisement>
):BasePagingResponse()