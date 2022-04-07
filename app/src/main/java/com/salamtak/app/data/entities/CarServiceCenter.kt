package com.salamtak.app.data.entities

data class CarServiceCenter(
    val id: String,
    var isLiked: Boolean?,
    val logoUrl: String,
    val name: String,
    val rating: Int = 0,
    var services: List<String>,
    val branches: Int,
    val isVerified: Boolean
)