package com.salamtak.app.ui.component.health.subcategory.listeners

interface SubcategoryListener<T> {
    fun onClick(subcategory:T)

    fun onMoreDoctorsClick(subcategory:T)
}