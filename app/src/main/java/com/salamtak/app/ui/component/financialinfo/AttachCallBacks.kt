package com.salamtak.app.ui.component.financialinfo

import android.graphics.Bitmap
import com.salamtak.app.data.entities.FinancialTypeAttachments
import java.io.File

interface AttachCallBacks {
    fun browseClicked(
        type: FinancialTypeAttachments,
        position: Int,
        allowedNumToUpload: Int,
        categoryId: Int
    )

//    fun browseClicked(
//        type: FinancialTypeAttachments,
//        position: Int,
//        allowedNumToUpload: Int
//    )

    fun browseClicked()
    fun handleAttachedImage(bitmap: Bitmap, file: File)
    fun handleAttachedImage(bitmap: Bitmap, file: File, typeId: String)
    fun handleAttachedPDF(file: File)
    fun imagesMoreThan3()
    fun bigFile()
    abstract fun cancelUpload()
}