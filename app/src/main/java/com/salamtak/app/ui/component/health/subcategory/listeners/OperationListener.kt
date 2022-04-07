package com.salamtak.app.ui.component.health.subcategory.listeners

interface OperationListener<Operation> {

    fun onOperationClicked(operation: Operation)
    fun onOwnerClicked(operation: Operation)
    fun onMoreDoctorsClicked(subcategory:Operation)
}