package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val type: Int?,
    val subCategories: List<SubCategory>?,
    val description: String?,
    val name2: String?,
    var isSelected: Boolean = false
) : Parcelable

