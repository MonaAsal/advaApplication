package com.salamtak.app.ui.common.listeners

interface FavouriteClickedListener<Operation> {
    fun onFavouriteClicked(item: Operation, position: Int)
}

