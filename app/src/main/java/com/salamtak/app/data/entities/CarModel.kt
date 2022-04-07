package com.salamtak.app.data.entities

data class CarModel(
    val id: Int,
    val name: String,
    val name2: String?,
    val logoURL: String?,
    var isSelected: Boolean = false
)