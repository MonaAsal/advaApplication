package com.salamtak.app.data.entities.responses

data class SalamtakListResponse<T>(
    val data: List<T>
):BasePagingResponse()