package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicalProviderOperation(
    val about: String,
    val name: String,
    val salamtakOperations: MutableList<SalamtakOperation>
):Parcelable