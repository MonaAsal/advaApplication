package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MedicalProfile(
    val bloodType: BloodType,
    val chronicDiseas: List<ChronicDisea>,
    val dateOfBirth: String,
    val hasChronicDiseases: Boolean,
    val hasSurgeriesBefore: Boolean,
    val height: Double,
    val id: String,
//    val medications: List<Any>,
//    val scansMedicalAnalyses: List<Any>,
    val shareable: Boolean,
//    val surgeries: List<Any>,
    val takingMedications: Boolean,
    val weight: Double
):Parcelable