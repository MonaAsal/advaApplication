package com.salamtak.app.ui.component.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.unit.Constraints
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.data.entities.Notification
import com.salamtak.app.data.local.AppDatabase
//import com.salamtak.app.ui.component.bookingrequests.BookedRequestsActivity
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {

            Log.d(TAG, "From: ${remoteMessage.from}")
            Log.d(TAG, "Message data payload: " + remoteMessage.data!!)
            Log.d(TAG, "Message Notification Body: ${remoteMessage.notification!!.body}")
//            Log.d(TAG, "remoteMessage: " + Gson().toJson(remoteMessage!!))
            Log.d(TAG, "From: ${Gson().toJson(remoteMessage.notification)}")

            var notification: Notification? = null

            // Check if message contains a data payload.

            if (remoteMessage.data.isNotEmpty()) {
                val sharedPreference = getSharedPreferences("CardData", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("NotificationCount","100")
                editor.apply()
                //var notification =Gson().fromJson(remoteMessage.data.toString(), Notification::class.java)
                //"{onClick=OPEN_HOMEVISIT, body=Radwa message, type=1, image=Image, sound=default, title=Title, content={" ContentTest ":" Content demo"}}"

//            Log.e("type", remoteMessage.data["type"]!!)

                var requestId = remoteMessage.data["content"]

                val jsonObject = JSONObject(remoteMessage.data["content"])
                try {
                    requestId = jsonObject.get("RequestId").toString()
                } catch (e: Exception) {

                }
                notification = Notification(
                    remoteMessage.data["type"],
                    requestId,
                    remoteMessage.data["titleEN"],
                    remoteMessage.data["titleAr"],
                    remoteMessage.data["onClick"],
                    remoteMessage.data["image"],
                    remoteMessage.data["bodyEN"]!!,
                    remoteMessage.data["bodyAR"]
                )

                val db = AppDatabase.getAppDataBase(App.context)
                db?.notificationDao()?.insertAll(notification)
//                FirebaseViewModel().saveNotification(notification)

                if ( /* Check if data needs to be processed by long running job */true) {
                    // For long-running tasks (10 seconds or more) use WorkManager.
                    scheduleJob()
                } else {
                    // Handle message within 10 seconds
                    handleNow()
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.notification != null) {
                showNotification(
                    remoteMessage.notification!!.body,
                    remoteMessage.notification!!.title.toString(), notification
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)

    }

    private fun scheduleJob() {
        // [START dispatch_job]
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String) {
        try {
//            sharedUseCase.updateFcmToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showNotification(messageBody: String?, title: String, notification: Notification?) {
        try {
            if (notification != null) {
                var intent = Intent(this, MainActivity::class.java)
                when (notification.type) {
                    "1" -> {
//                        intent = Intent(this, BookedRequestsActivity::class.java)
//                        intent.putExtra(Constants.KEY_ID, notification.content)
                    }
//                "2" -> {
//                    intent = Intent(this, HomeVisitsTrackingActivity::class.java)
//                    intent.putExtra(Constants.KEY_ID, notification.content)
//                }
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                val pendingIntent = PendingIntent.getActivity(
                    this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT
                )
                val channelId = getString(R.string.default_notification_channel_id)
                val defaultSoundUri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder =
                    NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_not))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)


                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationBuilder.setSmallIcon(R.drawable.ic_not)
                    val channel = NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                } else {
                    notificationBuilder.setSmallIcon(R.drawable.ic_not_white)
                }
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }


}