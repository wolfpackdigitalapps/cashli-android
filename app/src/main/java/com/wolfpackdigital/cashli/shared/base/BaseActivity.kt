package com.wolfpackdigital.cashli.shared.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.wolfpackdigital.cashli.BR
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.parcelable
import com.wolfpackdigital.cashli.shared.utils.views.LoadingDialog

abstract class BaseActivity<BINDING : ViewDataBinding, VIEW_MODEL : ViewModel>
constructor(@LayoutRes private val layoutResource: Int) : AppCompatActivity() {

    protected val binding by activityBinding<BINDING>(
        layoutResource
    )
    protected abstract val viewModel: VIEW_MODEL
    var loadingDialog: LoadingDialog? = null

    private val newFCMNotificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val message = intent.extras?.parcelable<NotificationModel>(
                Constants.PUSH_NOTIFICATION_EXTRA_DATA_FOREGROUND
            )
            parseNotificationFromIntent(message, fromBackground = false)
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.also {
            it.lifecycleOwner = this
            it.setVariable(BR.viewModel, viewModel)
        }
        addFcmBroadcastReceiver()
        loadingDialog = LoadingDialog(this)
        setupViews()
        parseNotificationFromIntent(getNotificationFromIntent())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(newFCMNotificationReceiver)
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    abstract fun setupViews()

    private fun addFcmBroadcastReceiver() {
        registerReceiver(
            newFCMNotificationReceiver,
            IntentFilter(Constants.PUSH_NOTIFICATION_EXTRA_FOREGROUND)
        )
    }

    private fun getNotificationFromIntent(newIntent: Intent? = null) =
        (newIntent ?: intent)?.extras?.let { extra ->
            extra.getBundle(
                Constants.PUSH_NOTIFICATION_EXTRA
            )?.parcelable<NotificationModel>(Constants.PUSH_NOTIFICATION_EXTRA_DATA)
        }

    abstract fun parseNotificationFromIntent(
        notification: NotificationModel? = null,
        fromBackground: Boolean = false
    )
}
