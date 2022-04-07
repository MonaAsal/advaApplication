package com.salamtak.app.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.ContextWrapper
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


fun <T> androidLazy(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun getPath(context: Context, uri: Uri?): String? {
    var result: String? = null
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri!!, proj, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
            result = cursor.getString(column_index)
        }
        cursor.close()
    }
    if (result == null) {
        result = "Not found"
    }
    return result
}


fun getFileNameByUri(context: Context, uri: Uri): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs =
                    arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        // Return the remote address
        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
            context,
            uri,
            null,
            null
        )
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun getRealPathFromURI(uri: Uri?, contentResolver: ContentResolver): String? {
    return try {
        val cursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        cursor.getString(idx)
    } catch (e: Exception) {
        ""
    }
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
    val cw = ContextWrapper(inContext)
    val directory = cw.getDir("adva", Context.MODE_PRIVATE)
    if (!directory.exists()) {
        directory.mkdir()
    }
    val file = File(directory, Calendar.getInstance().timeInMillis.toString())

    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
        inImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    } catch (e: java.lang.Exception) {
        Log.e("SAVE_IMAGE", e.message, e)
    }

    return Uri.parse(file.absolutePath)

//    val bytes = ByteArrayOutputStream()
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//    val path =
//        MediaStore.Images.Media.insertImage(
//            inContext.contentResolver,
//            inImage,
//            Calendar.getInstance().timeInMillis.toString(),
//            null
//        )
//    return Uri.parse(path)
}


fun getImageFile(inContext: Context, inImage: Bitmap): File? {
    val cw = ContextWrapper(inContext)
    val directory = cw.getDir("adva", Context.MODE_PRIVATE)
    if (!directory.exists()) {
        directory.mkdir()
    }
    val file = File(directory, Calendar.getInstance().timeInMillis.toString()+".png")
    Log.e("file4", file.absolutePath)
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
        inImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    } catch (e: java.lang.Exception) {
        Log.e("SAVE_IMAGE", e.message, e)
    }

    return file

}

