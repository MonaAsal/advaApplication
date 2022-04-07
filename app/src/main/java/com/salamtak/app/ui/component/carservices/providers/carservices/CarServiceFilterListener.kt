package com.salamtak.app.ui.component.carservices.providers.carservices

import com.salamtak.app.data.entities.Category

interface CarServiceFilterListener {
    fun onCarServicesSelected(categories: List<Category>?)

}