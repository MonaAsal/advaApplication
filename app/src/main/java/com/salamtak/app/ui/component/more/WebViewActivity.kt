package com.salamtak.app.ui.component.more

import android.annotation.TargetApi
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class WebViewActivity : BaseActivity() {

    private var url: String? = null
    internal var title: String? = null
    override val layoutId: Int
        get() = R.layout.activity_webview

    override fun initializeViewModel() {

    }

    override fun observeViewModel() {

    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = intent!!.getStringExtra(Constants.KEY_URL)
        title = intent!!.getStringExtra(Constants.KEY_TITLE)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = title
    }


    override fun onStart() {
        super.onStart()
//        StringNotifier.getInstance().post<String>(title)
//        tv_title.setText(title);
        webView.settings.javaScriptEnabled = (true) // enable javascript
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(this@WebViewActivity, description, Toast.LENGTH_SHORT).show()
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                    view,
                    rerr.errorCode,
                    rerr.description.toString(),
                    req.url.toString()
                )
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {

                progress_bar.visibility = View.VISIBLE
                if (progress == 100)
                    progress_bar.visibility = View.GONE
            }
        }

        webView.loadUrl(url!!)
    }

}

