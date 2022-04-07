package com.salamtak.app.ui.component.notifications.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.salamtak.app.R
import com.salamtak.app.data.entities.Notification
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.firebase.FirebaseViewModel
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.convertDateFormatTimeAgo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_notification.*


/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class NotificationsAdapter(firebaseViewModel: FirebaseViewModel, val lang: String) :
    RecyclerView.Adapter<NotificationsViewHolder>() {

    lateinit var notifications: MutableList<Notification>

    fun setList(notifications1: MutableList<Notification>) {
        this.notifications = notifications1
    }

    fun removeItem(position: Int) {
        notifications.removeAt(position)
        notifyItemRemoved(position)
    }

    private val onItemClickListener: RecyclerItemListener<Notification> =
        object : RecyclerItemListener<Notification> {

            override fun onItemSelected(notification: Notification) {
                firebaseViewModel.onNotificationClicked(notification)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        return NotificationsViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(notifications!![position], onItemClickListener, lang)
    }

    override fun getItemCount(): Int {
        return if (notifications != null) notifications!!.size else 0
    }

    fun getItemAt(adapterPosition: Int): Notification {
        return notifications[adapterPosition]
    }


}

class NotificationsViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        notification: Notification,
        onItemClickListener: RecyclerItemListener<Notification>,
        lang: String
    ) {

        Log.e("notif", Gson().toJson(notification).toString())
        if (lang == Constants.ARABIC_LOCALE)
            tv_notification_text.text = notification.bodyAr
        else
            tv_notification_text.text = notification.body

        try {
            tv_notification_time.text =
                convertDateFormatTimeAgo(notification.time!!, "yyyy-MM-dd HH:mm:ss")
        } catch (e: Exception) {
            tv_notification_time.text =
                convertDateFormatTimeAgo(notification.time!!, "yyyy-MM-dd HH:mm:ss")
        }

        if (lang == Constants.ARABIC_LOCALE)
            tv_notification_type.text = notification.titleAr
        else
            tv_notification_type.text = notification.title

//        when (notification.type!!.toInt()) {
//            1 -> tv_notification_type.text = App.context.getString(R.string.operations)
//            3 -> tv_notification_type.text = App.context.getString(R.string.social)
//            4 -> tv_notification_type.text = App.context.getString(R.string.general)
//        }
//        tv_notification_type.text =
//            Constants.notificationTypes[notification.type!!.toInt()]
        iv_notification_icon.setImageDrawable(Constants.notificationTypesIcons[notification.type!!.toInt()])
//        iv_workPlace_image.loadRoundedImage(
//            workplace.medicalProviderBranch?.imageUrl,
//            Constants.IMAGE_CORNER, R.drawable.ic_hospital_avatar
//        )
        containerView.setOnClickListener {
            onItemClickListener.onItemSelected(notification)
        }
    }


}

