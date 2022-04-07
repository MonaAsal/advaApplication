package com.salamtak.app.data.entities

data class Collage(
    val id: String,
    val logoUrl: String,
    val imageUrl:String?,
    val about: String = "",
    val rating: Int = 0,
    val name: String,
    val branches: List<CollageBranch>?
)