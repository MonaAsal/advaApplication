package com.salamtak.app.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChronicDiseaseInput
    (val ChronicDiseasId: Int, val InfectionDate: String, val Note: String) : Parcelable {
    companion object {
        fun chronicDiseaseListToMap(chronicDiseases: List<ChronicDiseaseInput>): HashMap<String, String> {
            val map: HashMap<String, String> = HashMap()
            for ((i, field) in chronicDiseases.withIndex()) {

                map["chronicDiseases[" + i + "].ChronicDiseasId"] = field.ChronicDiseasId.toString()
                map["chronicDiseases[" + i + "].InfectionDate"] = field.InfectionDate
                map["chronicDiseases[" + i + "].Note"] = field.Note

            }
            return map

        }
    }
}