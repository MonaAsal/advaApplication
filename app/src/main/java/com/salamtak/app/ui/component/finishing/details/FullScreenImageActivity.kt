package com.salamtak.app.ui.component.finishing.details

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.salamtak.app.BuildConfig
import com.salamtak.app.R
import com.salamtak.app.data.entities.Attachment
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_finishing_details.*
import kotlinx.android.synthetic.main.activity_image.*
import okhttp3.ResponseBody
import java.io.File

@AndroidEntryPoint
class FullScreenImageActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_full_screen_zoom_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   val image = intent.extras!!.getString("Image")
        var imagesList =intent.extras?.getStringArrayList("images")

        iv_close.setOnClickListener { onBackPressed() }
//        if (image!!.isNotEmpty()) {
//            val url = BuildConfig.BASE_URL + image
//            iv_image.loadImageWithHeaderRefresh(url)
//        }

        Log.d("sliderImages", imagesList.toString())

        if (!imagesList.isNullOrEmpty()){
            showSlider(imagesList.toMutableList())
        }
    }
    override fun initializeViewModel() {}
    override fun observeViewModel() {}
    private fun showSlider(imgsList: MutableList<String>) {
        imageSlider.changeSizeAspectRatio(0)
        val adapter = SliderAdapterExample(imgsList)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

        imageSlider.startAutoCycle()
    }
}