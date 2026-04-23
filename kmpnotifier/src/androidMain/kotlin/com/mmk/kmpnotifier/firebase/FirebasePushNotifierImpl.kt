package com.mmk.kmpnotifier.firebase

import com.mmk.kmpnotifier.logger.currentLogger
import com.mmk.kmpnotifier.notification.PushNotifier
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.messaging.messaging
import kotlin.coroutines.cancellation.CancellationException

internal class FirebasePushNotifierImpl : PushNotifier {

    init {
        currentLogger.log("FirebasePushNotifier is initialized")
    }

    override suspend fun getToken(): String? {
        return try {
            return Firebase.messaging.getToken()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            null.also {
                currentLogger.log("Error while getting token: $e")
            }
        }
    }

    override suspend fun deleteMyToken() {
        Firebase.messaging.deleteToken()
    }

    override suspend fun subscribeToTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
    }

    override suspend fun unSubscribeFromTopic(topic: String) {
        Firebase.messaging.unsubscribeFromTopic(topic)
    }

}
