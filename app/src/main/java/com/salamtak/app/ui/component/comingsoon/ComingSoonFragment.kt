package com.salamtak.app.ui.component.comingsoon

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.component.LiveChat
import com.salamtak.app.ui.component.home.HomeViewModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.synthetic.main.activity_coming_soon.*
import kotlinx.android.synthetic.main.item_promotions.view.*
import kotlinx.android.synthetic.main.toolbar.*


class ComingSoonFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_coming_soon

    lateinit var liveChat: LiveChat
    val homeViewModel: HomeViewModel by viewModels()


    override fun observeViewModel() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_home.setOnClickListener {
            navigateToHome()
        }
        tv_title.text = getString(R.string.comming_soon_title)
        iv_toolbar_back.setOnClickListener { onBackPressed() }

        tv_contact_us.setOnClickListener {
            callNumber(Constants.SALAMTAK_PHONE_NUMBER)
        }

        val page = requireArguments().getInt("page", 1)
        when (page) {
            1 -> {
                tv_toolbar_title.text = getString(R.string.travel)
              //  iv_image.setImageResource(R.drawable.travel)
                iv_image.loadRoundedImage( requireArguments().getString("image"), Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )

                tv_sub_title.text =
                    getString(R.string.for_installing_travel)//getString(R.string.car_service_info)
            }
            2 -> {
                tv_toolbar_title.text = getString(R.string.bills)
                iv_image.loadRoundedImage( requireArguments().getString("image"), Constants.IMAGE_CORNER, R.drawable.ic_adva_logo )

                tv_sub_title.text = getString(R.string.for_installing_your_bills)
            }

            3 -> {
                tv_toolbar_title.text = getString(R.string.more)
                iv_image.setImageResource(R.drawable.ic_notification)

                tv_sub_title.text = getString(R.string.other_soon_text)
            }

        }
    }

}