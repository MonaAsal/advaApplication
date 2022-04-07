package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.HomeVisit

data class HomeVisitsResponse(
    val `data`: List<HomeVisit>
):BasePagingResponse()