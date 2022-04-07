package com.salamtak.app.data.entities.responses

open class BasePagingResponse : BaseResponse() {
    val currentPage: Int = 1
    val totalPages: Int = 1
    val pageSize: Int = 10
    val totalRecords: Int = 0
    val related: Boolean? = false
//    val status: Boolean = false
//    val message: String? = null
//    val errors: List<ErrorModel>? = null
}