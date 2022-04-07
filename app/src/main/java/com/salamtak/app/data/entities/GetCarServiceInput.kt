package com.salamtak.app.data.entities

data class GetCarServiceInput(
    var pageSize: Int = 0,
    var page: Int = 0,
    var brands: List<Int>? = null,
    var services: List<Int>? = null,
    var locations: List<Int>? = null,
    var query: String? = null
)