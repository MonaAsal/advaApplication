package com.salamtak.app.data.entities.responses

import android.os.Parcelable
import com.salamtak.app.data.entities.BasicProfile
import kotlinx.android.parcel.Parcelize

//data class UpdateProfileResponse(
//
//    val `data`: BasicProfileData?
//
//) : BaseResponse()
@Parcelize
data class UpdateProfileResponse(
    val basicProfile: BasicProfile,
    val email: String?,
    val phone: String?,
    val isMailVerified: Boolean?,
    val isPhoneVerified: Boolean?
) : Parcelable