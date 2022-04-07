package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FinancialTypeAttachments(
    var attachmentId: Int,
    var id: Int,
    var title: String,
    var attachments: MutableList<Attachment>?,
    var max: Int = 3,
    var isRequired: Boolean = false
) : Parcelable
