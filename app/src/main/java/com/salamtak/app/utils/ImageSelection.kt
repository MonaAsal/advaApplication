package com.salamtak.app.utils

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.kotlinpermissions.KotlinPermissions
import com.salamtak.app.R
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ImageSelection(
    val activity: Activity,
    val supportFragmentManager: FragmentManager,
    val showPdf: Boolean = true, var maxImageNum: Int = 5, val callBacks: AttachCallBacks? = null
) {

    var canCrop = false

    var CAMERA_CAPTURE = 1
    val CROP_PIC = 2
    val OPEN_GALLERY = 3
    val REQUEST_CODE_DOC = 4
    var position: Int = 0
    private var typeId = ""
    lateinit var mCurrentPhotoPath: String

    fun browseForDocumnet() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        val mimeTypes = arrayOf(
            "application/pdf"
//            ,
//            "application/msword",
//            "text/plain"
        )

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type =
                if (mimeTypes.size === 1)
                    mimeTypes[0]
                else "*/*"
            if (mimeTypes.isNotEmpty()) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }

        activity.startActivityForResult(intent, REQUEST_CODE_DOC)
    }

    fun getImage() {
        if (showPdf)
            openSalamtakDialog(
                supportFragmentManager,
                activity.getString(R.string.browse),
                "",
                R.drawable.bg_plaint_white,
                activity.getString(R.string.select_image),
                activity.getString(R.string.select_docs),
                ::yesClicked,
                ::noClicked
            )
        else
            openSalamtakDialog(
                supportFragmentManager,
                activity.getString(R.string.browse),
                activity.getString(R.string.select_image),
                R.drawable.bg_plaint_white,
                ::yesClicked, ::cancelUpload
            )
    }

    fun cancelUpload() {
        callBacks?.cancelUpload()
    }

    fun yesClicked() {
        // captureFromCamera()
        browseMultipleImages()
//        if (maxImageNum > 1)
//            browseMultipleImages()
//        else
//            browseImage()
    }

    fun noClicked() {
        browseForDocumnet()
    }

    private fun browseImage() {
        var pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        activity.startActivityForResult(pickPhoto, OPEN_GALLERY)
    }

    private fun browseMultipleImages() {
        ImagePicker.create(activity)
            .showCamera(false)
            .limit(maxImageNum)
            .toolbarImageTitle(activity.getString(R.string.select_image))
            .showCamera(true)
//            .folderTitle(getString(R.string.folder))
//            .theme(R.style.ImagePickerTheme)
            .start(OPEN_GALLERY)
    }


    fun captureFromCamera() {
        val dir: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/picFolder/"
        val newdir = File(dir)
        newdir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val file = dir + timeStamp + ".jpg"
        val newfile = File(file)
        try {
            newfile.createNewFile()
        } catch (e: IOException) {
        }
        mCurrentPhotoPath = "file:" + newfile.absolutePath

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val outputFileUri = Uri.fromFile(newfile)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        activity.startActivityForResult(cameraIntent, CAMERA_CAPTURE);

    }

    fun askPermission() {
        if (
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            KotlinPermissions.with(activity as FragmentActivity)
                .permissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .onAccepted { permissions ->
                    getImage()
                }
                .onDenied { permissions ->
                    askPermission()
                }
                .onForeverDenied { permissions ->
                    Toast.makeText(
                        activity,
                        "You have to grant permissions! Grant them from app settings please.",
                        Toast.LENGTH_LONG
                    ).show()
                    activity.finish()
                }
                .ask()
        } else {
            getImage()
        }
    }

    fun performCrop(picUri: Uri) {
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        try {
            // call the standard crop action intent (the user device may not
            // support it)
            val cropIntent = Intent("com.android.camera.action.CROP")
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties
            cropIntent.putExtra("crop", "true")
            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 4)
//            cropIntent.putExtra("aspectY", 3)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256)
            cropIntent.putExtra("outputY", 256)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            // start the activity - we handle returning in onActivityResult
            activity.startActivityForResult(cropIntent, CROP_PIC)

        } // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            val toast = Toast
                .makeText(
                    activity,
                    "This device doesn't support the crop action!",
                    Toast.LENGTH_SHORT
                )
            toast.show()
        }
    }


    private fun compressFile(filePath: String): Bitmap? {
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, o)

        var size = File(filePath).length() / 1024 / 1024
        Log.e("file size", size.toString() + "MB")
// The new size we want to scale to
        val REQUIRED_SIZE = 1024

// Find the correct scale value. It should be the power of 2.
        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

// Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale
        var image = BitmapFactory.decodeFile(filePath, o2)
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val exifOrientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            var rotate = 0
            when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            }
            if (rotate != 0) {
                val w = image.width
                val h = image.height

                // Setting pre rotate
                val mtx = Matrix()
                mtx.preRotate(rotate!!.toFloat())

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, false)
            }
        } catch (e: IOException) {
            return null
        }

        return image.copy(Bitmap.Config.ARGB_8888, true)
    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        callBacks: AttachCallBacks
    ) {
        try {
            if (resultCode === Activity.RESULT_OK) {
                (when (requestCode) {
                    OPEN_GALLERY -> {
                        Log.d("===uploadPhoto", "gallery : " + attr.data)
                        var imagesList = ImagePicker.getImages(data!!) as ArrayList<Image?>
                        if (imagesList.isNotEmpty()) {
                            for (i in 0 until imagesList.size) {
                                var bitmap = BitmapFactory.decodeFile(imagesList[i]!!.path)

                                val fileSize =
                                    File(imagesList[i]!!.path).length() / 1024 / 1024
//
                                Log.e("file size2", fileSize.toString() + "MB")
                                if (fileSize >= 5) {
                                    var bitmap2 = compressFile(imagesList[i]!!.path)
                                    bitmap2?.let {
                                        val finalFile = getImageFile(activity, bitmap2)
                                        finalFile?.let {
                                            Log.i("file2", finalFile.absolutePath)

//                                    performCrop(Uri.parse(finalFile.absolutePath))
                                            callBacks.handleAttachedImage(
                                                bitmap2,
                                                File(finalFile.absolutePath),
                                                typeId
                                            )
                                        }
                                    }
                                } else {
//                                performCrop(Uri.parse(imagesList[i]!!.path))
                                    bitmap?.let {
                                        callBacks.handleAttachedImage(
                                            bitmap,
                                            File(imagesList[i]!!.path),
                                            typeId
                                        )
                                    }
                                }
                            }
                        }
                    }
                    CAMERA_CAPTURE -> {
                        performCrop(Uri.parse(mCurrentPhotoPath))
                    }
                    CROP_PIC -> {
                        try {
                            val bitmap = data!!.extras!!.get("data") as Bitmap

                            zipSaveNShowImage(bitmap, callBacks)

                        } catch (e: Exception) {

                            val f = File(data!!.data!!.path)

                            val bitmap =
                                MediaStore.Images.Media.getBitmap(
                                    activity.contentResolver,
                                    Uri.fromFile(f)
                                )

                            callBacks.handleAttachedImage(
                                bitmap,
                                File(f.path),
                                typeId
                            )

//                        zipSaveNShowImage(bitmap, callBacks)
                        }
                    }
                    REQUEST_CODE_DOC -> {
                        val input: InputStream =
                            activity.contentResolver.openInputStream(data?.data!!)!!

                        try {
                            val file =
                                File(
                                    ContextCompat.getCodeCacheDir(activity),
                                    Calendar.getInstance().timeInMillis.toString() + ".pdf"
                                )

                            FileOutputStream(file).use { output ->

                                val buffer = ByteArray(5 * 1024)
                                var read: Int
                                while (input.read(buffer).also { read = it } != -1) {
                                    output.write(buffer, 0, read)
                                }
                                output.flush()

                                Log.i("file1", file.absolutePath)
                                var size = file.length() / 1024 / 1024
                                Log.e("file size", size.toString() + "MB")
                                if (size <= 5) {
                                    callBacks.handleAttachedPDF(file)
                                } else
                                    callBacks.bigFile()
                            }


//                    var file1 = File(data?.data!!.path)
//                    Log.i("file", file.absolutePath)
//                    callBacks.handleAttachedPDF(file)
                        } finally {
                            input.close()
                        }
                    }
                })
            }
        } catch (e: OutOfMemoryError) {
        }
    }

    private fun zipSaveNShowImage(bitmap: Bitmap, callBacks: AttachCallBacks) {
        var bitmap1 = scaleDown(bitmap, 5000f, false)
        var out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)

        val tempUri = getImageUri(activity, bitmap1)

        val finalFile = File(getRealPathFromURI(tempUri, activity.contentResolver))
        Log.i("file", finalFile.absolutePath)
        Log.i("bitmap1 size", bitmap1.byteCount.toString())

        callBacks.handleAttachedImage(bitmap1, finalFile, typeId)
    }

}