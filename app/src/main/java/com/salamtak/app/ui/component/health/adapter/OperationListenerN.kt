package com.salamtak.app.ui.component.health.adapter

import com.salamtak.app.data.entities.Operation

interface OperationListenerN {
    fun onFavouriteClicked(operation: Operation, position: Int)
    fun onOwnerClicked(operation: Operation)
//    fun onDoctorClicked(operation: OperationN)
    fun onDetailsClicked(operation: Operation)

}