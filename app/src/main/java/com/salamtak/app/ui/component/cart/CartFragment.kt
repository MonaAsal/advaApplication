package com.salamtak.app.ui.component.cart

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.salamtak.app.R
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.CartItem
import com.salamtak.app.data.entities.responses.GetCartResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.CartItemListener
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.dialog_confirm_service_removed.*
import kotlinx.android.synthetic.main.dialog_confirm_service_removed.btn_submit
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.progress
import kotlinx.android.synthetic.main.fragment_services.*
import kotlinx.android.synthetic.main.layout_no_cart_items.*
import kotlinx.android.synthetic.main.toolbar_new.*
import org.jetbrains.anko.intentFor
import java.util.*


class CartFragment : BaseFragment(), CartItemListener<CartItem> {
//    private var firebaseScreenName = "Booked_services_Screen"
    private val cartViewModel: CartViewModel by viewModels()
    override val layoutId: Int
        get() = R.layout.fragment_cart

    override fun observeViewModel() {
        with(cartViewModel)
        {
            observe(showLoading, ::showLoadingView)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_toolbar_title.text=getString(R.string.your_cart)
        iv_toolbar_back.visibility= View.INVISIBLE
        btn_checkout.setOnClickListener { checkout() }
        btn_browse.setOnClickListener { navigateToHome()}
    }

    override fun onResume() {
        showCardData()
        super.onResume()
    }

    private  fun checkout(){
        btn_checkout.isEnabled = false
        observe(cartViewModel.checkoutResponse, ::showSuccessCheckout)
        cartViewModel.checkoutCart(Constants.cartUID)
    }
    private fun showCardData(getCartResponse: GetCartResponse?) {
        if(getCartResponse!!.data.isEmpty()){
            cart_empty.visibility = View.VISIBLE
            btn_checkout.visibility=View.GONE
        }
        else{
            cart_empty.visibility = View.GONE
            btn_checkout.visibility=View.VISIBLE
        }
        val cartAdapter = CartAdapter(this, getCartResponse.data, requireActivity())
        rv_cart.adapter = cartAdapter
        rv_cart.setHasFixedSize(true)
        (activity as MainActivity?)!!.changeCartCount(getCartResponse.data.size)

    }
    override fun onCartItemRemoved(item: CartItem, Position: Int) {
        showDialog(item)
    }


    private fun ShowSucessDelete(baseResponse: BaseResponse?) {
     //   Toast.makeText(activity,"Item Deleted!",Toast.LENGTH_SHORT).show()
        if(baseResponse!!.status){

            showCardData()
        }
    }
    private fun showDialog(item: CartItem) {
        val dialog = BottomSheetDialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
    //    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_confirm_service_removed)
        dialog.iv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.btn_submit.setOnClickListener {
            dialog.dismiss()
            observe(cartViewModel.DeleteItemResponse, ::ShowSucessDelete)
            cartViewModel.deleteCartItem(item.itemUID)
        }
        dialog.show()
    }
    private fun showCardData(){
        observe(cartViewModel.CartResponse, ::showCardData)
        cartViewModel.GetCartData(Constants.cartUID)
        //(activity as MainActivity?)!!.getCartCount()
    }


    private fun showSuccessCheckout(baseResponse: BaseResponse?) {
        btn_checkout.isEnabled = true
        if (baseResponse?.status == true) {
            LogAdjustEvent("Checkout")
            (activity as MainActivity?)!!.changeCartCount(0)
            val sharedPreference = requireContext().getSharedPreferences("CardData", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("cartUID",UUID.randomUUID().toString())
                editor.apply()
            startActivity(activity?.intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to cartViewModel.needFinancialInfo()))
        } else {
            baseResponse?.message?.let { showMessage(it) }
        }
    }
    fun showLoadingView(show: Boolean) {
        if (show) {
            progress.toVisible()
        }
        else {
            progress.toGone()
        }
    }
}