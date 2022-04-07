package com.salamtak.app.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.salamtak.app.BuildConfig
import com.salamtak.app.R
import com.salamtak.app.data.Session
import top.defaults.drawabletoolbox.DrawableBuilder


fun View.showKeyboard() {
    (context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.GONE
}


/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
//                EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(context.getString(it), timeLength)
        }
    })
}

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Any>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(context, it, timeLength).show()
                is Int -> Toast.makeText(context, context.getString(it), timeLength).show()
                else -> {
                }
            }
        }
    })
}

fun ImageView.loadImage(@DrawableRes resId: Int) = Glide.with(this)
    .load(resId)
    .into(this)

fun ImageView.loadImage(imageUrl: String?) {
    if (imageUrl != null && !imageUrl.isNullOrEmpty())
        Glide.with(this)
            .load(BuildConfig.BASE_URL + imageUrl)
            .placeholder(R.color.white)
            .into(this)
    else
        loadImage(R.color.white)
}

fun ImageView.loadRoundedImage(imageUrl: String?, corner: Int) {
    if (imageUrl != null && !imageUrl.isNullOrEmpty()) {
        val url: String? = BuildConfig.BASE_URL + imageUrl
        Glide.with(this)
            .load(url)
            .apply(
                RequestOptions().transforms(
                    CenterCrop(),
                    RoundedCorners(corner)
                )
            )
            .placeholder(R.drawable.bg_curved_gray_border_medium)
            .into(this)


    }
}

fun ImageView.loadRoundedImage(imageUrl: String?) {
    if (imageUrl != null && !imageUrl.isNullOrEmpty()) {
        val url: String? = BuildConfig.BASE_URL + imageUrl
        Log.e("imageUrl", url!!)
        Glide.with(this)
            .load(url)
            .apply(
                RequestOptions().transforms(
                    CenterCrop(),
                    RoundedCorners(Constants.IMAGE_CORNER)
                )
            )
            .placeholder(R.drawable.bg_curved_gray_border_medium)
            .into(this)
    }
}

fun ImageView.loadRoundedImage(bitmap: Bitmap) {
    Glide.with(this)
        .load(bitmap)
        .apply(
            RequestOptions().transforms(
                CenterCrop(),
                RoundedCorners(Constants.IMAGE_CORNER)
            )
        )
        .placeholder(R.drawable.bg_curved_gray_border_medium)
        .into(this)

}

fun ImageView.loadRoundedImage(imageUrl: String?, corner: Int, defaultIcon: Int) {
    if (imageUrl != null && !imageUrl.isNullOrEmpty()) {
        val url: String? = BuildConfig.BASE_URL + imageUrl
        Glide.with(this)
            .load(url)
            .apply(
                RequestOptions().transforms(
                    FitCenter(),
                    RoundedCorners(corner)
                )
            )
            .placeholder(defaultIcon)
            .into(this)
    } else
        Glide.with(this)
            .load(defaultIcon)
            .apply(
                RequestOptions().transforms(
                    CenterCrop(),
                    RoundedCorners(corner)
                )
            )
            .placeholder(defaultIcon)
            .into(this)

}

fun ImageView.loadCircleImage(imageUrl: String?) {
    try {
        if (imageUrl != null && !imageUrl.isNullOrEmpty()) {
            val url: String? = BuildConfig.BASE_URL + imageUrl
            Glide.with(this)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .into(this)
        }
    } catch (e: Exception) {
    }

}

private val CONTENT_TYPE = "Content-Type"
private val Authorization = "Authorization"
private val Language = "Accept-Language"
private val CONTENT_TYPE_VALUE = "application/json"
private val APP_VERSION = "AppVersion"
fun ImageView.loadImageWithHeader(url: String) {

    Log.e("url", url)
    val glideUrl = GlideUrl(
        url,
        LazyHeaders.Builder()
            .addHeader(Authorization, Session.xAccessToken)
            .addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
            .addHeader(APP_VERSION, BuildConfig.APP_VERSION)
            .addHeader(Language, Session.xLang)
            .build()
    )

    Glide.with(this.context)
        .load(glideUrl)
//        .diskCacheStrategy(DiskCacheStrategy.NONE)
//        .skipMemoryCache(true)
        .into(this)
}

fun ImageView.loadImageWithHeaderRefresh(url: String) {

    Log.e("url", url)
    val glideUrl = GlideUrl(
        url,
        LazyHeaders.Builder()
            .addHeader(Authorization, Session.xAccessToken)
            .addHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
            .addHeader(APP_VERSION, BuildConfig.APP_VERSION)
            .addHeader(Language, Session.xLang)
            .build()
    )

    Glide.with(this.context)
        .load(glideUrl)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .placeholder(R.drawable.progress_animation)
        .into(this)
}

fun ImageView.loadCircleImage(imageUrl: String?, placeHolder: Int) {
    try {
        if (imageUrl != null && imageUrl!!.isNotEmpty() && !imageUrl.isNullOrEmpty()) {
            val url: String = BuildConfig.BASE_URL + imageUrl
            Glide.with(this)
                .load(url)
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(Constants.IMAGE_CORNER_CIRCLE))) // round
                .circleCrop()
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(this)
        }
    } catch (e: Exception) {
        Glide.with(this)
            .load(placeHolder)
            .circleCrop()
            .placeholder(placeHolder)
            .error(placeHolder)
            .into(this)
    }

}

fun TextView.makeUnderlined() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.makeUnderlined(text: String) {
//
//    var content = SpannableString(text)
//    content.setSpan(UnderlineSpan(), 0, content.length, 0)
//    this.text = content

//    this.text = Html.fromHtml(String.format(this.context.getString(R.string.underlined_text), text))

    //this.setPaintFlags(this.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    this.text = text
}

fun TextView.clearUnderlined(text: String) {
//    if (this.paintFlags == Paint.UNDERLINE_TEXT_FLAG)
    this.paintFlags = this.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()

    this.text = text
}

fun AppCompatTextView.setTextFutureExt(text: String) =
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            TextViewCompat.getTextMetricsParams(this),
            null
        )
    )

fun AppCompatEditText.setTextFutureExt(text: String) =
    setText(
        PrecomputedTextCompat.create(text, TextViewCompat.getTextMetricsParams(this))
    )

fun pulseAnimation(view: View) {
    val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        view,
        PropertyValuesHolder.ofFloat("scaleX", 1.2f),
        PropertyValuesHolder.ofFloat("scaleY", 1.2f)
    )
    scaleDown.duration = Constants.PULSE_ANIMATION_TIME.toLong()

    scaleDown.repeatCount = ObjectAnimator.INFINITE
    scaleDown.repeatMode = ObjectAnimator.REVERSE

    scaleDown.start()
}

fun rotateAnimation(view: View) {
    val rotate =
        RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    rotate.duration = 5000
    rotate.interpolator = LinearInterpolator()
    rotate.repeatCount = ObjectAnimator.INFINITE

    view.startAnimation(rotate)

}

fun View.changeSizeAspectRatio(widthDp: Int) {

    val params = this!!.layoutParams as ViewGroup.LayoutParams

    val display = context.resources.displayMetrics
//        params.width = display.widthPixels
//        params.height = display.widthPixels * 9 / 16

    var width = convertDpToPixel(widthDp, this.context)
    if (width != 0)
        params.width = width
    else params.width = display.widthPixels

    params.height = params.width * 9 / 16

    Log.i("width", params.width.toString() + "")
    Log.i("height", params.height.toString() + "")

    this!!.layoutParams = params

}

fun convertDpToPixel(dp: Int, context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

fun changeTextColor(textView: TextView, colorId: Int) {
    textView.setTextColor(
        ContextCompat.getColor(
            textView.context,
            colorId
        )
    )
}

fun View.shakeView() {
    val shake = AnimationUtils.loadAnimation(context, R.anim.anim_shake)
    startAnimation(shake)
}

fun changeIconFavouriteToIsFavourite(view: ImageView, isFav: Boolean) {
    if (isFav)
        view.setImageDrawable(
            ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_fav_active
            )
        )
    else
        view.setImageDrawable(
            ContextCompat.getDrawable(
                view.context,
                R.drawable.ic_fav_gray
            )
        )
}

fun addLinearHorizontalItemDecoration(recyclerView: RecyclerView) {
    val linearLayoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.layoutManager = linearLayoutManager
    val dividerItemDecoration =
        DividerItemDecoration(recyclerView.context, linearLayoutManager.orientation)
    recyclerView.addItemDecoration(dividerItemDecoration)
}

fun addVerticalItemDecoration(rv: RecyclerView) {
    val linearLayoutManager = LinearLayoutManager(
        rv.context,
        LinearLayoutManager.HORIZONTAL, false
    )
    rv.layoutManager = linearLayoutManager
    linearLayoutManager.scrollToPosition(5);

    val dividerItemDecoration = DividerItemDecoration(
        rv.context,
        linearLayoutManager.orientation
    )
    ContextCompat.getDrawable(
        rv.context,
        R.drawable.vertical_divider
    )?.let {
        dividerItemDecoration.setDrawable(
            it
        )
    }
    rv.addItemDecoration(dividerItemDecoration)

}

fun addVerticalItemDecorationWithScrolling(rv: RecyclerView, position: Int) {
    var linearLayoutManager = LinearLayoutManager(
        rv.context,
        LinearLayoutManager.HORIZONTAL, false
    )
    rv.layoutManager = linearLayoutManager
    linearLayoutManager.scrollToPosition(position)


    var dividerItemDecoration = DividerItemDecoration(
        rv.context,
        linearLayoutManager.orientation
    )
    ContextCompat.getDrawable(
        rv.context,
        R.drawable.vertical_divider
    )?.let {
        dividerItemDecoration.setDrawable(
            it
        )
    }
    rv.addItemDecoration(dividerItemDecoration)

}
fun addVerticalItemDecorationWithState(rv: RecyclerView, state: Parcelable?) {
    var linearLayoutManager = LinearLayoutManager(
        rv.context,
        LinearLayoutManager.HORIZONTAL, false
    )
    rv.layoutManager = linearLayoutManager
    linearLayoutManager.onRestoreInstanceState(state)


    var dividerItemDecoration = DividerItemDecoration(
        rv.context,
        linearLayoutManager.orientation
    )
    ContextCompat.getDrawable(
        rv.context,
        R.drawable.vertical_divider
    )?.let {
        dividerItemDecoration.setDrawable(
            it
        )
    }
    rv.addItemDecoration(dividerItemDecoration)

}

fun addVerticalItemDecorationGrid(recyclerView: RecyclerView) {

    if (recyclerView.itemDecorationCount == 0) {
        val decorator = DividerItemDecoration(recyclerView.context, LinearLayoutManager.HORIZONTAL)

        decorator.setDrawable(
            ContextCompat.getDrawable(
                recyclerView.context,
                R.drawable.horizontal_divider
            )!!
        )
        recyclerView.addItemDecoration(decorator)
    }

}


//fun showCustomDialog(
//    activity: Activity,
//    title: String,
//    message: String,
//    yesClicked: KFunction0<Unit>,
//    noClicked: KFunction0<Unit>
//) {
//    val builder: AlertDialog.Builder? = activity?.let {
//        AlertDialog.Builder(it, R.style.AlertDialogStyle)
//    }
//
//    builder?.setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_info_orange))
//    builder?.setTitle(title)
//    builder!!.setMessage(message)
//        .setCancelable(true)
//        .setPositiveButton("Yes") { dialog, id ->
//            yesClicked()
//
//        }
//        .setNegativeButton("No") { dialog, id ->
//            //  Action for 'NO' Button
//            dialog.cancel()
//            noClicked()
//        }
//
//    val alert = builder.create()
////    alert.setMessage(message)
//    alert.setTitle(title)
//
//    alert?.show()
//
//    val positiveButton: Button =
//        alert.getButton(AlertDialog.BUTTON_POSITIVE)
//    val negativeButton: Button =
//        alert.getButton(AlertDialog.BUTTON_NEGATIVE)
//    val neutralButton: Button =
//        alert.getButton(AlertDialog.BUTTON_NEUTRAL)
//
//    positiveButton.setTextColor(Color.parseColor("#FF0B8B42"))
////        positiveButton.setBackgroundColor(Color.parseColor("#FFE1FCEA"))
//
//    negativeButton.setTextColor(Color.parseColor("#FFFF0400"))
////        negativeButton.setBackgroundColor(Color.parseColor("#FFFCB9B7"))
//
//    neutralButton.setTextColor(Color.parseColor("#FF1B5AAC"))
////        neutralButton.setBackgroundColor(Color.parseColor("#FFD9E9FF"))
//
//    alert.window?.setBackgroundDrawableResource(R.drawable.bg_curved_white);
//
//}

fun hideKeyboard(activity: Activity) {
    val view = activity.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun preventUiFromPopUpAboveKeyboard(activity: Activity){
    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}

fun ImageView.setBackGroundCurved() {
    this.createRoundedShape(
        ContextCompat.getColor(context, android.R.color.transparent),
        ContextCompat.getColor(context, R.color.red),
        resources.getDimension(R.dimen.corner).toInt()
    )
    //this.setImageDrawable(drawable)
}


fun ImageView.createRoundedShape(background: Int, stroke: Int, corner: Int) {
    var drawable = DrawableBuilder()
//        .rectangle()
        .rounded()
        .hairlineBordered()
        .strokeColor(stroke)
        .strokeColorPressed(background)
        .solidColor(background)
        .solidColorPressed(background)

        //.ripple()
        .cornerRadius(corner)

        .build()

    this.setImageDrawable(drawable)
}

fun TextView.makeFontBold() {
    //val tf = Typeface.createFromAsset(this.context.assets, "font/cairobold.ttf")
    val tf = resources.getFont(R.font.cairobold)
    this.setTypeface(tf, Typeface.BOLD)
}

fun TextView.makeFontNormal() {

    val tf = resources.getFont(R.font.cairoregular)
//    val typeface = ResourcesCompat.getFont(context, R.font.myfont)
//    val tf = Typeface.createFromAsset(this.context.assets, "font/cairoregular.ttf")
    this.setTypeface(tf, Typeface.NORMAL)
}

fun SearchView.applyFontFamily() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val typeface = resources.getFont(R.font.cairoregular)


    var text = this.findViewById<EditText>(R.id.search_src_text)
    text.setTypeface(typeface)

    text.setTextSize(
        TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.text_size).toFloat()
    )

    text.setTextColor(this.context.color(R.color.colorPrimary))
    text.setHintTextColor(this.context.color(R.color.hintColor))

//    this.setTypeface(null, Typeface.NORMAL)
}