package com.mmk.kmpnotifier.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mmk.kmpnotifier.logger.currentLogger
import com.mmk.kmpnotifier.notification.AndroidNotifier
import com.mmk.kmpnotifier.notification.NotifierManagerImpl

internal class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val notifierManager by lazy { NotifierManagerImpl }
    private val notifier: AndroidNotifier by lazy { notifierManager.getLocalNotifier() as AndroidNotifier }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        currentLogger.log("FirebaseMessaging: onNewToken is called")
        notifierManager.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        val chatId = data["chatId"]

        if (chatId.isNullOrEmpty()) {
            // Fallback plain notification for messages without a chatId
            val title = data["title"] ?: "New message"
            val body = data["body"] ?: ""
            notifier.notify(title = title, body = body, payloadData = data)
            return
        }

        notifier.showConversationNotification(
            chatId = chatId,
            senderName = data["senderName"] ?: "",
            senderAvatarUrl = data["senderAvatarUrl"] ?: "",
            chatName = data["chatName"] ?: "",
            isGroup = data["isGroup"] == "true",
            body = data["body"] ?: "",
            payloadData = data,
        )
    }
}
