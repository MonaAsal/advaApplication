package com.salamtak.app.data.entities

data class FinishingPackage(
    val id: String,
    val name: String,
    val subtitle: String,
    val pricePerMeter: Double,
    val details: String?,
    val packageCategories: List<PackageCategory>?,
    val pricingType: String?)

data class PackageCategory(val name: String, val items: String?)

