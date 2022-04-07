package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FinancialType(
    val id: Int,
    val drawableId: Int,
    val name: String,
    var attachments: List<FinancialTypeAttachments>?
) : Parcelable

