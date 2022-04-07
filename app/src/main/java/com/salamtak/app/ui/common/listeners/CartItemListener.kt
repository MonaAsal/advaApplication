package com.salamtak.app.ui.common.listeners

/**
 * Created by Mona on 3/3/2022
 */


interface CartItemListener<T> {
    fun onCartItemRemoved(item: T, Position:Int)
}

