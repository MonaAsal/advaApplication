package com.salamtak.app.ui.component.financialinfo

import com.salamtak.app.data.entities.Attachment

interface ImagesListener {
    fun deleteImage(
        position: Int,
        typePosition: Int, categoryInt: Int
    )

    fun selectImage(position: Int, attachment: Attachment)
}