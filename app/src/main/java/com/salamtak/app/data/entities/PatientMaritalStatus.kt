package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PatientMaritalStatus(
    val id: Int,
    val name: String
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is PatientMaritalStatus) {
            id == other.id
            return true
        } else {
            false
        }
        return false
    }

}