package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Providers

data class TopProvidersResponse(
    val `data`: List<Providers>
):BasePagingResponse()