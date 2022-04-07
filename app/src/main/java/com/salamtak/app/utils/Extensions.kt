package com.salamtak.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.salamtak.app.R
import com.salamtak.app.ui.common.SalamtakDialog
import com.salamtak.app.ui.common.listeners.DialogCallBacks
import java.io.*
import kotlin.reflect.KFunction0


fun <T> Context.openActivity(clazz: Class<T>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz).apply {
        bundle?.let { putExtras(it) }
    }
    startActivity(intent)
}

fun <T> Context.openActivityClearTask(clazz: Class<T>, bundle: Bundle? = null) {
    val intent = Intent(this, clazz).apply {
        bundle?.let { putExtras(it) }
    }
    intent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
    )

    startActivity(intent)
}


fun FragmentManager.openFragment(
    container: Int, fragment: Fragment,
    backstack: String? = null, sharedElements: HashMap<String, View> = HashMap(),
    hasReplaceAnimation: Boolean = true, withAdd: Boolean = false
) {
    beginTransaction().apply {
        setReorderingAllowed(true)
        sharedElements.forEach { addSharedElement(it.value, it.key) }
        if (hasReplaceAnimation) setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_from_left,
            R.anim.enter_from_left,
            R.anim.exit_from_right
        )
        if (withAdd) add(container, fragment, backstack) else replace(
            container,
            fragment,
            backstack
        )
        addToBackStack(null)
    }.commit()
}

fun ImageView.displayImage(path: String) {
    Glide.with(this)
        .load(path)
        .placeholder(R.drawable.ic_circle)
        .error(R.drawable.ic_circle)
        .into(this)

}

fun View.setVisibility(isVisible: Boolean) = if (isVisible) this.toVisible() else this.toGone()


fun AppCompatTextView.fadeInOut(
    text: String,
    duration: Long = 200,
    completion: (() -> Unit)? = null
) {
    fadeOutAnimation(duration) {
        this.text = text
        fadeInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadeOutAnimation(
    duration: Long = 200,
    visibility: Int = View.INVISIBLE,
    completion: (() -> Unit)? = null
) {
    animate()
        .alpha(0.6f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let { it() }
        }
}

fun View.fadeInAnimation(duration: Long = 200, completion: (() -> Unit)? = null) {
    alpha = 0.6f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction { completion?.let { it() } }
}

fun AppCompatTextView.setAppearance(@StyleRes styleId: Int) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(styleId) else setTextAppearance(
        this.context,
        styleId
    )

fun View.setMargin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    val params = (layoutParams as? ViewGroup.MarginLayoutParams)
    params?.setMargins(
        left ?: params.leftMargin,
        top ?: params.topMargin,
        right ?: params.rightMargin,
        bottom ?: params.bottomMargin
    )
    layoutParams = params
}

fun openSalamtakDialog(
    supportFragmentManager: FragmentManager,
    title: String,
    text: String,
    icon: Int
) {
    try {

        val fm = supportFragmentManager
        val dialog = SalamtakDialog(null, true)

        val bundle = Bundle()
        bundle.putInt(Constants.KEY_ICON, icon)
        bundle.putString(Constants.KEY_TEXT, text)
        bundle.putString(Constants.KEY_TITLE, title)
        dialog.arguments = bundle

        dialog.show(fm, "")
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun openSalamtakDialog(
    supportFragmentManager: FragmentManager,
    title: String,
    text: String,
    icon: Int,
    yesClicked: KFunction0<Unit>,
    noClicked: KFunction0<Unit>
) {

    val dialogCallBacks: DialogCallBacks =
        object : DialogCallBacks {
            override fun onOkClick() {
                yesClicked()
            }

            override fun onNoClick() {
                noClicked()
            }
        }
    val fm = supportFragmentManager
    val dialog =
        SalamtakDialog(dialogCallBacks, false)
//        dialog.setCallBack(dialogCallBacks)

    val bundle = Bundle()
    bundle.putInt(Constants.KEY_ICON, icon)
    bundle.putString(Constants.KEY_TEXT, text)
    bundle.putString(Constants.KEY_TITLE, title)

    dialog.arguments = bundle

    try {
        dialog.show(fm, "")
    } catch (e: Exception) {
    }

}

fun openSalamtakDialog(
    supportFragmentManager: FragmentManager,
    title: String,
    text: String,
    icon: Int,
    yesClicked: KFunction0<Unit>
) {

    val dialogCallBacks: DialogCallBacks =
        object : DialogCallBacks {
            override fun onOkClick() {
                yesClicked()
            }

            override fun onNoClick() {

            }
        }
    val fm = supportFragmentManager
    val dialog =
        SalamtakDialog(dialogCallBacks, true)
//        dialog.setCallBack(dialogCallBacks)

    val bundle = Bundle()
    bundle.putInt(Constants.KEY_ICON, icon)
    bundle.putString(Constants.KEY_TEXT, text)
    bundle.putString(Constants.KEY_TITLE, title)

    dialog.arguments = bundle

    dialog.show(fm, "")

}


fun openSalamtakDialog(
    supportFragmentManager: FragmentManager,
    title: String,
    text: String,
    icon: Int,
    yesText: String,
    noText: String,
    yesClicked: KFunction0<Unit>,
    noClicked: KFunction0<Unit>
) {

    val dialogCallBacks: DialogCallBacks =
        object : DialogCallBacks {
            override fun onOkClick() {
                yesClicked()
            }

            override fun onNoClick() {
                noClicked()
            }
        }
    val fm = supportFragmentManager
    val dialog =
        SalamtakDialog(dialogCallBacks, false)
//        dialog.setCallBack(dialogCallBacks)

    val bundle = Bundle()
    bundle.putInt(Constants.KEY_ICON, icon)
    bundle.putString(Constants.KEY_TEXT, text)
    bundle.putString(Constants.KEY_TITLE, title)
    bundle.putString(Constants.KEY_YES_TEXT, yesText)
    bundle.putString(Constants.KEY_NO_TEXT, noText)

    dialog.arguments = bundle

    dialog.show(fm, "")

}


fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap? {
    val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, widthPixels, heightPixels)
    drawable.draw(canvas)
    return bitmap
}

fun Context.writeResponseBodyToDisk(body: okhttp3.ResponseBody): File? {
    return try {
        // todo change the file location/name according to your needs
        val futureStudioIconFile =
            File(getExternalFilesDir(null).toString() + File.separator + "Future Studio Icon.png")
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            val fileSize: Long = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(futureStudioIconFile)
            while (true) {
                val read: Int = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
                Log.d("TAG", "file download: $fileSizeDownloaded of $fileSize")
            }
            outputStream.flush()
            return futureStudioIconFile
        } catch (e: IOException) {
            return null
        } finally {
            inputStream?.let {
                inputStream.close()
            }
            outputStream?.let {
                outputStream.close()
            }
        }
    } catch (e: IOException) {
        return null
    }
}

fun Spinner.setSpinnerError(errorMessage: String) {
    val errorText = this.selectedView as TextView

    errorText.error = errorMessage
    errorText.setTextColor(Color.RED)

//    errorText.text = errorMessage

}

fun Context.deviceHasBiometric(): Boolean {

    val canAuthenticate = BiometricManager.from(this).canAuthenticate()
    if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS)
        return true
    return false
}

fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)