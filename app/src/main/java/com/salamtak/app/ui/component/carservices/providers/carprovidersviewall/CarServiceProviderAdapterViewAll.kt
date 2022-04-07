package com.salamtak.app.ui.component.carservices.providers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarServiceProvidersModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadImage
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_car.*
import kotlinx.android.synthetic.main.item_car.iv_logo
import kotlinx.android.synthetic.main.item_car.tv_branches
import kotlinx.android.synthetic.main.item_car.tv_name
import kotlinx.android.synthetic.main.item_car_service_provider.*
import kotlinx.android.synthetic.main.item_car_service_provider_viewall.*


class CarServiceProviderAdapterViewAll(val listener: CarServiceProviderViewAllListener) :
    RecyclerView.Adapter<ViewHolder>() {

    lateinit var providers: MutableList<CarServiceProvidersModel>

    fun setList(list: MutableList<CarServiceProvidersModel>) {
        this.providers = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_car_service_provider_viewall, parent, false)
        return ViewHolder(view)
    }

    fun addData(listItems: List<CarServiceProvidersModel>) {
        var size = providers?.size!!
        providers?.addAll(listItems)
        notifyItemRangeInserted(size, providers?.size!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(providers!![position], listener)
    }

    override fun getItemCount(): Int {
        return if (providers != null) providers!!.size else 0
    }
}

class ViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        provider: CarServiceProvidersModel,
        listener: CarServiceProviderViewAllListener
    ) {
        /*  iv_logo.loadCircleImage(
              provider.logoUrl,
              R.drawable.ic_circle
          )*/

        iv_logo.loadRoundedImage(provider.logoUrl, Constants.IMAGE_CORNER2)

        tv_name.text = provider.name
        rating_tv.text = provider.rating.let {it.toString()  }
        tv_branches.text =
            String.format(
                provider.branches?.let {
                    itemView.context.resources.getQuantityString(
                        R.plurals.number_branches,
                        it,
                        provider.branches
                    )
                }.toString()
            )

//        if (provider.isVerified)
//            layout_verified.toVisible()
//        else
//            layout_verified.toGone()


        /*  provider.services?.let {
              rv_tags.apply {
                  // adapter = TagAdapter(it.toMutableList())

                  var adapter = TagsAdapter(provider.services.toMutableList())
                  rv_tags.layoutManager = LinearLayoutManager(
                      itemView.context,
                      RecyclerView.HORIZONTAL, false
                  )
                  rv_tags.adapter = adapter

  //                var itemWidth =
  //                    itemView.context.resources.displayMetrics.widthPixels - convertDpToPixel(
  //                        16,
  //                        itemView.context
  //                    )
  //                setServiceList(itemView.context, it.toMutableList(), itemWidth)
              }
          }
  */

//

//        tagsPartStraggred(provider)
//        showTags(provider.services)

        //        tagsLinear(provider)
//        tv_show_more_tags.setOnClickListener {
//            listener.onMoreTagsClicked(provider)
//        }

        // btn_details.setOnClickListener { listener.onProviderClicked(provider) }
        itemView.setOnClickListener { listener.onProviderClicked(provider) }
    }

//    private fun setServiceList(context: Context, servicesList: List<String>?, itemWidth: Int) {
//        var width = 0
//        if (!(servicesList.isNullOrEmpty())) {
//            for (item in servicesList) {
//                var chip = Chip(context)
//                var chipItem = item.trim()
//                chip.text = chipItem
//                width += chip.width
//                if (width < itemWidth - convertDpToPixel(16, context))
//                    chipGroup.addView(chip)
//
//                Log.e("chipGroup", chipGroup.measuredWidth.toString())
//
//            }
//
//        }
//
//    }


//    private fun showTags(services: List<String>?) {
//
////        var list = listOf(
////            "سمكرة - دوكو",
////            "ميكانيكا",
////            "عفشة",
////            "قطع غيار",
////            "تعديل سيارات",
////            "فتيس",
////            "تكييف",
////            "إطارات-جنوط -زوايا"
////        )
//        services?.let {
//            rv_tags.apply {
//                adapter = TagAdapter(it.toMutableList())
//            }
//        }
//
//    }
//
//    private fun tagsPartStraggred(provider: CarServiceCenter) {
//
//        if (layoutPosition == 0)
//            provider.services = listOf(
//                "سمكرة - دوكو",
//                "ميكانيكا",
//                "عفشة",
//                "قطع غيار",
//                "تعديل سيارات",
//                "فتيس",
//                "تكييف",
//                "إطارات-جنوط -زوايا"
//            )
//        var adapter = TagsAdapter(provider.services.toMutableList())
//
//        rv_tags.layoutManager = StaggeredGridLayoutManager(
//            1,
//            StaggeredGridLayoutManager.HORIZONTAL
//        )
//
//        var span = calculateSpan(provider.services)
//        Log.e("span", span.toString())
//
////        if (span > 1) {
////            tv_show_more_tags.toVisible()
////        } else {
////            tv_show_more_tags.toInvisible()
////        }
//
//        collapse()
//
//        tv_show_more_tags.setOnClickListener {
//            if (tv_show_more_tags.rotation == 0f) {
//                expand(span)
//                tv_show_more_tags.rotation = 180f
//            } else {
//                collapse()
//                tv_show_more_tags.rotation = 0f
//            }
//        }
//
//        rv_tags.adapter = adapter
//    }
//
//
//    private fun tagsLinear(provider: CarServiceCenter) {
//
//        var tags = provider.services.toMutableList()
//        if (provider.services.size > 1)
//            tags = mutableListOf(provider.services[0])
//        var adapter = TagsAdapter(tags)
//
//        rv_tags.layoutManager = LinearLayoutManager(
//            itemView.context,
//            RecyclerView.VERTICAL, false
//        )
//
//        rv_tags.adapter = adapter
//    }
//
//    private fun expandLinear(services: List<String>) {
//        var adapter = TagsAdapter(services.toMutableList())
//        rv_tags.adapter = adapter
//    }
//
//    private fun collapseLinear(services: List<String>) {
//        var adapter = TagsAdapter(mutableListOf(services[0]))
//        rv_tags.adapter = adapter
//    }
//
//    private fun expand(span: Int) {
//        rv_tags.layoutManager = StaggeredGridLayoutManager(
//            span,
//            StaggeredGridLayoutManager.HORIZONTAL
//        )
//    }
//
//    private fun collapse() {
//        rv_tags.layoutManager = StaggeredGridLayoutManager(
//            1,
//            StaggeredGridLayoutManager.HORIZONTAL
//        )
//    }

    private fun calculateSpan(services: List<String>): Int {
        Log.e("size", services.size.toString())
        var span = if (services.size > 2) {
            if (services.size % 2 != 0)
                (services.size / 2) + 1
            else
                (services.size / 2)
        } else 1
        return span

    }


}

