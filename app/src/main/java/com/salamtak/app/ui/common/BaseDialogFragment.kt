package com.salamtak.app.ui.common

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.salamtak.app.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Radwa Elsahn on 5/12/2019.
 */
@AndroidEntryPoint
abstract class BaseDialogFragment : AppCompatDialogFragment() {


//    abstract val layoutId: Int

    protected var baseViewModel: BaseViewModel? = null

//    protected abstract fun initializeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(activity)
        super.onCreate(savedInstanceState)
//        initializeDagger()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initializeViewModel()
//    }

//    fun initializeDagger()
//    {
////        DaggerAppComponent.builder().build().inject(App())
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(layoutId, container, false)
//    }


    protected fun showProgressBar(progressBar: ProgressBar?, show: Boolean) {
        if (progressBar != null)
            if (show) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
    }


//    fun sendScreenName(name: String) {
//        Log.i("TAG", "Setting screen name: " + name)
//
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "radwa")
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "type")
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
//
////        mTracker!!.setScreenName("Image~" + name)
////        mTracker!!.send(HitBuilders.ScreenViewBuilder().build())
////        mTracker!!.setScreenName(null)
//    }

    fun showError(error: String) {
    }

    fun setTitle(title: String) {
        val actionBar = (activity as BaseActivity).supportActionBar
        if (actionBar != null) {
            val titleTextView = activity?.findViewById<TextView>(R.id.txt_toolbar_title)
            if (title.isNotEmpty()) {
                titleTextView?.text = title
            }
        }
    }


    fun LogAdjustEvent(eventId: String) {
        (activity as BaseActivity).LogAdjustEvent(eventId)
    }

    fun LogAdjustEvent(eventId: String, key: String, value: String) {
        (activity as BaseActivity).LogAdjustEvent(eventId, key, value)
    }
}
