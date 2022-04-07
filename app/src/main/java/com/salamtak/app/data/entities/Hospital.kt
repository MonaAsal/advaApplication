package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hospital(
    val hospitalId: String,
    val hospitalName: String,
    val salamtakOperationsWithBranches: List<SalamtakOperationsWithBranche>
) : Parcelable