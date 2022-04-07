package com.salamtak.app.data.entities

import com.salamtak.app.data.entities.responses.BasePagingResponse
import com.salamtak.app.data.entities.responses.SalamtakListResponse

data class University(
    val about: String?,
    val colleges: CollageList?,
    val id: String,
    var isLiked: Boolean?,
    val imageUrl: String?,
    val logoUrl: String,
    val name: String,
    val rate: Int?,
    val reviews: Int?,
    val type: Int?,
    val locations: List<String>? = null,
    val rating: Int = 0
)

data class CollageList(val result: List<Collage>) : BasePagingResponse()