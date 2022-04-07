package com.salamtak.app.data.entities.responses

data class cartCountResponse(
    var status: Boolean = false,
    val data: CountObject? = null
) {
    class CountObject {
        var count: Int = 0
    }
}