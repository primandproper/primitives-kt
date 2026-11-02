package software.primandproper.primitives.notifications.noop

import software.primandproper.primitives.notifications.Platform
import software.primandproper.primitives.notifications.PushMessage
import software.primandproper.primitives.notifications.PushNotificationSender

/**
 * A [PushNotificationSender] that discards every message — the port of platform-go's
 * `notifications/mobile/noop.pushNotificationSender`. The safe default the config factory falls back
 * to when no provider is selected, and for tests that don't care about real delivery.
 */
public object NoopPushNotificationSender : PushNotificationSender {
    override suspend fun sendPush(
        platform: Platform,
        token: String,
        message: PushMessage,
    ) {
    }

    override suspend fun sendToTopic(
        platform: Platform,
        topic: String,
        message: PushMessage,
    ) {
    }
}
