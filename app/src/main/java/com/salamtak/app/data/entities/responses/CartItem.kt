package com.salamtak.app.data.entities.responses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val type: Int,
    val providerId: String,
    val title: String,
    val subTitle: String,
    val imageUrl: String,
    val installmentMonths: Int,
    val monthlyInstallment: String,
    val cartUID: String,
    val itemUID: String
) : Parcelable
