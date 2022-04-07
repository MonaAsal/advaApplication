package com.salamtak.app.ui.common.listeners


/**
 * Created by Radwa Elsahn on 3/321/2020
 */
interface BookOperationClickedListener<OperationN,SalamtakOperation> {
    fun onBookOperationClicked(item: OperationN, salamtakOperation: SalamtakOperation)
}

