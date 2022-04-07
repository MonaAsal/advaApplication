package com.salamtak.app.ui.component.paymentmethods

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.activity.viewModels
import com.salamtak.app.BuildConfig
import com.salamtak.app.R

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.Constants
import kotlinx.android.synthetic.main.activity_add_card.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import java.util.*
import javax.inject.Inject

class AddCardActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_add_card

    var fromHome = false

     val viewModel: PaymentMethodsViewModel  by viewModels()

    override fun initializeViewModel() {
    }

    override fun observeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fromHome = intent.getBooleanExtra(Constants.KEY_FROM_HOME_VISIT, false)
        tv_toolbar_title.text = getString(R.string.add_card)
        iv_toolbar_back.setOnClickListener { onBackPressed() }

        val url = intent.getStringExtra(Constants.KEY_URL)

        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.javaScriptEnabled = true

        val headers: HashMap<String, String> = HashMap()
        headers["AppVersion"] = BuildConfig.APP_VERSION

        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedClientCertRequest(view: WebView, request: ClientCertRequest) {
                super.onReceivedClientCertRequest(view, request)
                Log.d("webview", "onReceivedClientCertRequest")
            }


            override fun onUnhandledKeyEvent(view: WebView, event: KeyEvent) {
                super.onUnhandledKeyEvent(view, event)
                Log.e("event", event.toString())
                Log.e("view", view.originalUrl!!)

            }


            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                try {
                    Log.e("view", view.originalUrl!!)
                    Log.e("failingUrl : ", failingUrl)
                    Log.e("description : ", description)
                    Log.e("errorCode : ", errorCode.toString() + "")
                    //errorCall(view.getUrl());
                } catch (e: Exception) {
                    e.printStackTrace()
                    //errorCall(view.getUrl());
                }

            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                var url = url
                //  mTvLoading.setVisibility(View.VISIBLE);
                //showProgress(true, progress_bar)

                Log.e("shouldOverrideUrl", "url : $url")

                return false //Allow WebView to load url
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                Log.e("onPageFinished", url)

                // order=5023665&is_voided=false&id=2746353&owner=8663&source_data.type=card&txn_response_code=0&profile_id=7065&data.message=Approved&hmac=d7ce2e742be4b70905316d3344f28eee70bc655563b09bbeb042974bd12684dbf5a9006549c1c1016f545860a020626235e180cfb0579a5af5e5b1dffc3e6785&is_standalone_payment=true&amount_cents=100&is_void=false&created_at=2020-05-12T16%3A28%3A42.996272&captured_amount=0&is_3d_secure=true&is_capture=false&integration_id=12253&is_refunded=false&source_data.sub_type=Visa&acq_response_code=00&is_auth=false&is_refund=false&currency=EGP&source_data.pan=8769&merchant_order_id=add_card_70428068-dfd7-4bad-b203-695d78f93fe7
                //"/HomeVisit/Payment/Transactions/CallBack?is_voided=false&is_3d_secure=true&amount_cents=100&captured_amount=0&owner=8663&has_parent_transaction=false&merchant_order_id=add_card_2eaad27f-1ac5-4502-b8e0-5f26250b9e37&order=5027479&is_refund=false&pending=false&source_data.pan=8769&currency=EGP&data.message=Approved&is_capture=false&created_at=2020-05-13T13%3A46%3A55.071297&hmac=17d2950404e2a6e632989116d08498933da2f07c3e1cabb298e02b1687442cd3996878ce91516ec71f1d423bb5bb2343c523bfcac07f764b8cc92d1f67e3e064&source_data.sub_type=Visa&source_data.type=card&txn_response_code=0&integration_id=12253&acq_response_code=00&is_void=false&success=true&is_standalone_payment=true&id=2749504&refunded_amount_cents=0&profile_id=7065&error_occured=false&is_auth=false&is_refunded=false
                var success =
                    url.contains("/HomeVisit/Payment/Transactions/CallBack?")
                            && url.contains("success=true")
                if (success) {
//                    var order = url.split("order=")[1].split("&")[0]
//                    Log.i("order", order)

                    showMessage(getString(R.string.payment_done_successfully))

                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView, progress: Int) {

                            progress_bar.visibility = View.VISIBLE
                            if (progress == 100)
                                progress_bar.visibility = View.GONE
                        }
                    }

//                    if(BuildConfig.IS_PRODUCTION)
                    view.loadUrl(BuildConfig.BASE_URL)
//                    else
//                        view.loadUrl(BuildConfig.BASE_URL_Sta)


//                    if (fromHome) {
//                        LogAdjustEvent("zhnq3z")
//                        startActivity<HomeVisitRequestSubmittedActivity>()
//                        finish()
//                    } else
                        onBackPressed()

                }
            }


            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.d("onReceivedHttpError", view.url!!)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Log.d("onReceivedError", view.url!!)
                //                errorCall(view.getUrl());
            }
        }
//        webView.loadUrl(url)
        webView.loadUrl(url!!, headers)
    }


}
