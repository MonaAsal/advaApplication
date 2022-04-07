package com.salamtak.app.ui.component.finishing.details

import com.salamtak.app.data.entities.FinishingPackage

interface FinishingDetailsListener {
    fun onFinishingPackageSelected(finishingPackage: FinishingPackage)
    fun openPackageDetails(finishingPackage: FinishingPackage)
}