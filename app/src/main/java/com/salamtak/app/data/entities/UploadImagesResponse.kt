package com.salamtak.app.data.entities

data class UploadImagesResponse(
    val failedFilesName: List<String>?,
    val savedFilesName: List<String>?
)