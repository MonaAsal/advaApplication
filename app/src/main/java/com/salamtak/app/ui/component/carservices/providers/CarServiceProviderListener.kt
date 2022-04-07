package com.salamtak.app.ui.component.carservices.providers

import com.salamtak.app.data.entities.CarServiceCenter


interface CarServiceProviderListener {
    fun onProviderClicked(provider: CarServiceCenter)
    fun onMoreTagsClicked(provider: CarServiceCenter)
}