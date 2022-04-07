package com.salamtak.app.data.entities.responses

data class SalamtakObjectListResponse<T>(
    val data: T? = null
):BasePagingResponse()