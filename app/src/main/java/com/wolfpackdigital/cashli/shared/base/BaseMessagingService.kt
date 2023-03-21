package com.wolfpackdigital.cashli.shared.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.main.MainActivity
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

const val FCM_NOTIFICATION_MODEL = "fcm_notification_model"
const val NO_TOKEN_MSG = "No token"

abstract class BaseMessagingService : FirebaseMessagingService() {

    @CallSuper
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val gson = Gson()
        val receivedJSON = gson.toJson(message.data)
        val receivedMessage = try {
            gson.fromJson(receivedJSON, NotificationModel::class.java)
        } catch (ex: JsonSyntaxException) {
            return
        }
        createNotificationFromPush(applicationContext, receivedMessage)
    }

    @CallSuper
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        runBlocking {
            withContext(Dispatchers.Main) {
                handleNewToken()
            }
        }
    }

    open fun buildNotificationFromPush(
        context: Context,
        pushNotificationData: NotificationModel
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            context,
            context.getString(R.string.push_notification_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_push_notifications)
            setContentTitle(pushNotificationData.title)
            setStyle(NotificationCompat.BigTextStyle().bigText(pushNotificationData.body))
            setAutoCancel(true)
            val notificationIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(FCM_NOTIFICATION_MODEL, pushNotificationData)
            }
            val resultPendingIntent = TaskStackBuilder.create(context).run {
                addParentStack(MainActivity::class.java)
                addNextIntentWithParentStack(notificationIntent)
                getPendingIntent(
                    pushNotificationData.body.hashCode(),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            setContentIntent(resultPendingIntent)
        }
    }

    private fun createNotificationFromPush(
        context: Context,
        pushNotificationData: NotificationModel
    ) {
        val notificationBuilder = buildNotificationFromPush(context, pushNotificationData)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.notify(
            pushNotificationData.body.hashCode(),
            notificationBuilder.build()
        )
    }

    abstract fun handleNewToken()

    companion object {
        suspend fun getDeviceToken(): String {
            return getDeviceTokenWrapper { completeListener ->
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener(completeListener)
            }
        }

        suspend fun deleteDeviceToken(): Void {
            return getDeviceTokenWrapper { completeListener ->
                FirebaseMessaging.getInstance().deleteToken()
                    .addOnCompleteListener(completeListener)
            }
        }

        private suspend fun <T> getDeviceTokenWrapper(block: (OnCompleteListener<T>) -> Unit) =
            suspendCancellableCoroutine<T> { cancellableContinuation ->
                block(
                    OnCompleteListener<T> {
                        it.result?.let { result ->
                            cancellableContinuation.resume(result)
                        } ?: cancellableContinuation.resumeWithException(Exception(NO_TOKEN_MSG))
                    }
                )
            }
    }
}
