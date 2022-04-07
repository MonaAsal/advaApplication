package com.salamtak.app.ui.common.listeners

/**
 * Created by Radwa Elsahn on 7/16/2020
 */


interface ItemClickListener<T> {
    fun onItemSelected(item: T)

    fun onProviderSelected(item: T)
}

