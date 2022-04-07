package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SalamtakOperationsWithBranche(
    val branchId: String,
    val branchName: String,
    val installmentTypes: List<InstallmentType>,
    val salamtakOperationId: String,
    val salamtakOperationPrice: Double
):Parcelable