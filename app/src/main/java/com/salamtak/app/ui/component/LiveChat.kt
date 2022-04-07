package com.salamtak.app.ui.component

import android.content.Intent
import android.net.Uri
import com.livechatinc.inappchat.ChatWindowConfiguration
import com.livechatinc.inappchat.ChatWindowErrorType
import com.livechatinc.inappchat.ChatWindowView
import com.livechatinc.inappchat.models.NewMessageModel
import com.salamtak.app.ui.common.BaseActivity

class LiveChat(val activity: BaseActivity, val name: String, val phone: String = "") :
    ChatWindowView.ChatWindowEventsListener {
    lateinit var chatWindow: ChatWindowView

    init {
        initChat()
    }

    fun initChat() {
        try {

            chatWindow = ChatWindowView.createAndAttachChatWindowInstance(activity)
            chatWindow.setUpWindow(getChatWindowConfiguration())
            chatWindow.setUpListener(this)

            chatWindow.initialize()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showChatWindow() {
        chatWindow.showChatWindow()
    }

    fun getChatWindowConfiguration(): ChatWindowConfiguration? {

        return ChatWindowConfiguration(
            "13312449",
            "", //"General",
            name,
            phone,
            null
        )
    }


    fun onBackPressed(): Boolean {
        return chatWindow != null && chatWindow.onBackPressed()
    }

    fun handleChatAttachment(requestCode: Int, resultCode: Int, data: Intent?) {
        if (chatWindow != null) chatWindow.onActivityResult(requestCode, resultCode, data)
    }

    override fun onChatWindowVisibilityChanged(visible: Boolean) {
    }

    override fun onNewMessage(message: NewMessageModel?, windowVisible: Boolean) {
    }


    override fun onStartFilePickerActivity(intent: Intent?, requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }


    override fun onError(
        errorType: ChatWindowErrorType?,
        errorCode: Int,
        errorDescription: String?
    ): Boolean {
        return true
    }

    override fun handleUri(uri: Uri?): Boolean {
        return true
    }


}