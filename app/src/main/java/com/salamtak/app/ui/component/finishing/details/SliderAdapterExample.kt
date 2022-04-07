package com.salamtak.app.ui.component.finishing.details

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.utils.loadImage
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapterExample(val mSliderItems: MutableList<String>) :
    SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>() {

    var imagesArrayList :List<String> ? = null

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, null)
        return SliderAdapterVH(inflate)
    }
    fun setImages(imagesList: List<String>){
        this.imagesArrayList = imagesList
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.bind(mSliderItems[position],imagesArrayList)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

        fun bind(
            item: String,
            imagesArrayList: List<String>?
        ) {
            val imageView = itemView.findViewById<ImageView>(R.id.iv_auto_image_slider)
            imageView.loadImage(item)
           imageView.setOnClickListener { showInFullScreen(imagesArrayList) }
        }

        private fun showInFullScreen(imagesList: List<String>?) {
            var imagesArray : ArrayList<String>? =ArrayList()

            if (!imagesList.isNullOrEmpty()){
                for (i in imagesList.indices){
                    var image = imagesList.get(i)
                    imagesArray?.add(image)
                }
                if (!imagesArray.isNullOrEmpty()){
                    val intent = Intent(App.context,FullScreenImageActivity::class.java)
                    intent.putExtra("images", imagesArray)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    App.context.startActivity(intent)
                }
                Log.d("sliderImages3", imagesArray.toString())

            }
        }

    }
}
