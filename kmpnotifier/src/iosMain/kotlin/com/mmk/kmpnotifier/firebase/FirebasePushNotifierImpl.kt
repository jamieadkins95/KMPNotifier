package com.mmk.kmpnotifier.firebase

import com.mmk.kmpnotifier.logger.currentLogger
import com.mmk.kmpnotifier.notification.IosNotifier
import com.mmk.kmpnotifier.notification.PushNotifier
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNUserNotificationCenter

@OptIn(ExperimentalForeignApi::class)
internal class FirebasePushNotifierImpl : PushNotifier {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        scope.launch {
            currentLogger.log("FirebasePushNotifier is initializing")
            UNUserNotificationCenter.currentNotificationCenter().delegate =
                IosNotifier.NotificationDelegate()
            UIApplication.sharedApplication.registerForRemoteNotifications()
        }
    }

    override suspend fun getToken(): String? = try {
        Firebase.messaging.getToken()
    } catch (e: Throwable) {
        currentLogger.log("Error while getting token: $e")
        null
    }

    override suspend fun deleteMyToken() = Firebase.messaging.deleteToken()

    override suspend fun subscribeToTopic(topic: String) = Firebase.messaging.subscribeToTopic(topic)

    override suspend fun unSubscribeFromTopic(topic: String) = Firebase.messaging.unsubscribeFromTopic(topic)
    
}
