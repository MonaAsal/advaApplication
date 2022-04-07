package com.salamtak.app.data.entities.responses

import android.os.Parcelable
import com.salamtak.app.data.entities.Review
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookedOperation(
    val creationTime: String,
    val currentStatus: Int,
    val currentStatusNote: String?,
//    val downPayment: Double,
    val id: String,
//    val installmentType: InstallmentType,
//    val paymentTransactions: List<Any>,
//    val salamtakOperation: SalamtakOperation,
//    val statusTransactions: List<StatusTransaction>,
//    val review: Review?,
    val bookingType: Int,
    val name: String,
    val imageUrl: String,
    val provider: String?,
    val isPremiumPayment: Boolean, // yes
    val amount: String,
    val isPaidWithPremium: Boolean // no
) : Parcelable
