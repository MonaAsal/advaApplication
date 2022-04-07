package com.salamtak.app.ui.component.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Notification

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.bookingrequests.BookedRequestsActivity
import com.salamtak.app.ui.component.firebase.FirebaseViewModel
import com.salamtak.app.ui.component.notifications.adapter.NotificationsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.SwipeToDeleteCallback
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_notifications

//    @Inject
    val firebaseViewModel: FirebaseViewModel  by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory


    override fun initializeViewModel() {
//        firebaseViewModel = viewModelFactory.create(FirebaseViewModel::class.java)
    }

    override fun observeViewModel() {
//        firebaseViewModel.notifications.observe(this, Observer {
//
//        })
        observe(firebaseViewModel.notifications, ::handleNotifications)
        observe(firebaseViewModel.openOperationTracking, ::navigateToOperationTracking)
        observe(firebaseViewModel.openVisitTracking, ::navigateToVisitTracking)
    }

    private fun navigateToVisitTracking(id: String) {
//        var intent = Intent(this, HomeVisitsTrackingActivity::class.java)
//        intent.putExtra(Constants.KEY_ID, id)
//        startActivity(intent)
    }

    private fun navigateToOperationTracking(id: String) {
        Log.e("radwa1 op id",id)
        var intent = Intent(this, BookedRequestsActivity::class.java)
        intent.putExtra(Constants.KEY_ID, id)
        startActivity(intent)
    }

    private fun handleNotifications(notifications: List<Notification>?) {
        Log.i("list", notifications?.size.toString())
        notifications?.let { bindListData(it) }
    }

    private fun bindListData(notifications: List<Notification>) {

        var adapter = NotificationsAdapter(firebaseViewModel, firebaseViewModel.getLocale())
        if (!(notifications.isNullOrEmpty())) {
            adapter.setList(notifications.toMutableList())
            rv_notifications.adapter = adapter
            showDataView(true)
        } else {
            showDataView(false)
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_notifications.adapter as NotificationsAdapter
                firebaseViewModel.deleteNotification(adapter.getItemAt(viewHolder.adapterPosition))
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_notifications)
    }

    private fun showDataView(show: Boolean) {
        v_empty.visibility = if (show) View.GONE else View.VISIBLE
        rv_notifications.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }


//    private fun handleNotifications(list: Resource<List<Notification>>) {
//        Log.i("list", list.data!!.size.toString())
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseViewModel.getAllNotifications()
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.notifications)

    }

}

