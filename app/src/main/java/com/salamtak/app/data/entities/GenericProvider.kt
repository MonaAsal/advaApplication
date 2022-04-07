package com.salamtak.app.data.entities

open class GenericProvider {
    val about: String? = ""
    val id: String = ""
    var isLiked: Boolean? = false
    val imageUrl: String? = ""
    val logoUrl: String? = ""
    val name: String = ""
    val rating: Int? = 0
    val locations: List<String>? = null
    val contacts: List<ContactN>? = null
    val images: List<String>? = null
    val categoryName: String = ""

}

data class  ContactN(val type:Int,val value:String)


