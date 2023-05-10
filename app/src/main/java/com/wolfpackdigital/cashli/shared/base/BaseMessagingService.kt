package com.wolfpackdigital.cashli.shared.base

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.os.bundleOf
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.main.MainActivity
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.isAppInForeground
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

abstract class BaseMessagingService : FirebaseMessagingService() {

    @CallSuper
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val gson = Gson()
        val receivedJSON = gson.toJson(message.data)
        val receivedMessage = try {
            gson.fromJson(receivedJSON, NotificationModel::class.java)
        } catch (ex: JsonSyntaxException) {
            FirebaseCrashlytics.getInstance().recordException(ex)
            return
        }
        if (applicationContext.isAppInForeground()) {
            sendBroadcast(
                Intent(Constants.PUSH_NOTIFICATION_EXTRA_FOREGROUND).apply {
                    putExtras(
                        bundleOf(
                            Constants.PUSH_NOTIFICATION_EXTRA_DATA_FOREGROUND to receivedMessage
                        )
                    )
                }
            )
        } else {
            createNotificationFromPush(applicationContext, receivedMessage)
        }
    }

    @CallSuper
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        runBlocking {
            withContext(Dispatchers.IO) {
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
            color = context.getColor(R.color.colorGreen71)
            setContentTitle(pushNotificationData.title)
            setStyle(NotificationCompat.BigTextStyle().bigText(pushNotificationData.body))
            setAutoCancel(true)
            val notificationIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(
                    Constants.PUSH_NOTIFICATION_EXTRA,
                    bundleOf(Constants.PUSH_NOTIFICATION_EXTRA_DATA to pushNotificationData)
                )
            }
            val resultPendingIntent = TaskStackBuilder.create(context).run {
                addParentStack(MainActivity::class.java)
                addNextIntentWithParentStack(notificationIntent)
                getPendingIntent(
                    pushNotificationData.body.hashCode(),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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

    abstract suspend fun handleNewToken()

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
                    OnCompleteListener<T> { task ->
                        if (task.isSuccessful)
                            cancellableContinuation.resume(task.result)
                        else {
                            FirebaseCrashlytics.getInstance()
                                .recordException(Throwable(task.exception))
                            cancellableContinuation.cancel()
                        }
                    }
                )
            }
    }
}
