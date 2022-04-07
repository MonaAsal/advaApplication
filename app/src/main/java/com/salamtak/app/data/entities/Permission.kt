package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Permission(
    val applicationSection: ApplicationSection,
    val applicationSectionId: Int,
    val canAdd: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean,
    val canSoftDelete: Boolean,
    val canView: Boolean,
    val id: String
):Parcelable