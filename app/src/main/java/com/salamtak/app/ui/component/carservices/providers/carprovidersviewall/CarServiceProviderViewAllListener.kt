package com.salamtak.app.ui.component.carservices.providers

import com.salamtak.app.data.entities.CarServiceProvidersModel

interface CarServiceProviderViewAllListener {

        fun onProviderClicked(provider: CarServiceProvidersModel)
        fun onMoreTagsClicked(provider: CarServiceProvidersModel)
}