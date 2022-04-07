package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.HomeCategory

data class FeaturedCategoriesResponse(
    val `data`: List<HomeCategory>
):BasePagingResponse()