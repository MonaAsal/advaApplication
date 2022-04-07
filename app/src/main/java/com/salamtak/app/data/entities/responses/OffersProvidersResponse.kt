package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.MedicalProviderDetails

data class OffersProvidersResponse(
    val `data`: List<MedicalProviderDetails>
):BasePagingResponse()