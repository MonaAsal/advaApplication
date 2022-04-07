package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CarProvidersData(
    val carServiceProviders: ArrayList<CarServiceProvidersModel>?= arrayListOf()
)