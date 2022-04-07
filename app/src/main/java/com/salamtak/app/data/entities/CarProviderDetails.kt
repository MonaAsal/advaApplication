package com.salamtak.app.data.entities.CarProviderDetails


class CarProviderDetails {
    val about: String? = ""
    val id: String = ""
    var isVerified: Boolean? = false
    val imageUrl: String? = ""
    val logoUrl: String? = ""
    val name: String = ""
    val rating: Int? = 0
    val locations: List<Location>? = null
    val contacts: List<Contacts>? = null
    val images: List<String>? = null
    val reviews: Int? = 0
    val categoryId: Int? = 0
    val subCategoryId: Int? = 0
    val services: List<String>? = null
    val brands:List<Brand>? = null
}
data class  Contacts(val type:Int,val value:String)
data class Location(val areaName: String, val latitude: Double, val longitude: Double ,var url:String, var address:String)
data class Brand(val id: Int ,val logoURL:String, val name:String)