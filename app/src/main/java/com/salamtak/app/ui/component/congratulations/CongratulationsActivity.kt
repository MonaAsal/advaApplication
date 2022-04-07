//package com.salamtak.app.ui.component.congratulations
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ObjectAnimator
//import android.os.Bundle
//import android.os.Handler
//import android.view.View
//import android.view.animation.AccelerateDecelerateInterpolator
//import android.view.animation.DecelerateInterpolator
//import androidx.activity.viewModels
//import androidx.lifecycle.ViewModelProviders
//import com.salamtak.app.R
//import com.salamtak.app.ui.ViewModelFactory
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.utils.loadCircleImage
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_congratulations.*
//import kotlinx.android.synthetic.main.layout_offer_card.*
//import org.jetbrains.anko.startActivity
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class CongratulationsActivity : BaseActivity() {
//    override val layoutId: Int
//        get() = R.layout.activity_congratulations
//
//    var back = false
//
////    @Inject
////    lateinit var viewModelFactory: ViewModelFactory
////
////    @Inject
////    lateinit
////    val discountCardsViewModel: DiscountCardsViewModel by viewModels()
//
//    override fun initializeViewModel() {
////        discountCardsViewModel = ViewModelProviders.of(this, viewModelFactory)
////            .get(DiscountCardsViewModel::class.java)
//    }
//
//    override fun observeViewModel() {
//
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        hideNotificationBar()
//        super.onCreate(savedInstanceState)
//        var user = discountCardsViewModel.getUser()
//        if (user != null) {
//            tv_user_name.text =
//                (user.firstName + " " + user.lastName).toUpperCase()
//            tv_user_email.text = user.email
//
//            user.salamtakCardId?.let {
//                var cardNum = it.subSequence(0, 4).toString() + "  " +
//                        it.subSequence(4, 8).toString() + "  " +
//                        it.subSequence(8, 12).toString() + "  " +
//                        it.subSequence(12, 16).toString() + "  "
//
//                tv_card_number.text = cardNum
//            }
////            if (user.basicProfile != null) {
//            iv_user_image.loadCircleImage(user.imageUrl)
////            }
//        }
//        card.setOnClickListener { flipCard() }
//
//
//        btn_use_discount.setOnClickListener {
//            startActivity<DiscountCardsActivity>()
//        }
//
//        iv_close.setOnClickListener {
//            navigateToMainScreen()
////            if (isFromDiscount)
////                startActivity<DiscountCardsActivity>()
//        }
//
//        val handler = Handler()
//        handler.postDelayed({
//            flipCard()
//        }, 2500)
//    }
//
//    private fun flipCard() {
//        val oa1 = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0f)
//        val oa2 = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1f)
//        oa1.interpolator = DecelerateInterpolator()
//        oa2.interpolator = AccelerateDecelerateInterpolator()
//        oa1.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//                super.onAnimationEnd(animation)
////                card.setBackgroundResource(R.drawable.bg_discount_card_back)
//                if (!back) {
//
//                    iv_right.visibility = View.VISIBLE
//                    iv_left.visibility = View.VISIBLE
//                    group_card_back.visibility = View.VISIBLE
//                    group_user_details.visibility = View.GONE
//
//                } else {
//                    group_card_back_with_code.visibility = View.GONE
//                    group_user_details.visibility = View.VISIBLE
//                }
//
////                    card.setBackgroundResource(R.drawable.bg_curved_bottom_primary_light)
//                oa2.start()
//                back = back.not()
//            }
//        })
//        oa1.start()
//        oa1.duration = 500
//        oa2.duration = 500
//    }
//
//}