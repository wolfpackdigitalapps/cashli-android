package com.wolfpackdigital.cashli.shared.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.wolfpackdigital.cashli.BR
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import com.wolfpackdigital.cashli.shared.utils.extensions.parcelable
import com.wolfpackdigital.cashli.shared.utils.views.LoadingDialog

abstract class BaseActivity<BINDING : ViewDataBinding, VIEW_MODEL : ViewModel>
constructor(@LayoutRes private val layoutResource: Int) : AppCompatActivity() {

    protected val binding by activityBinding<BINDING>(
        layoutResource
    )
    protected abstract val viewModel: VIEW_MODEL
    var loadingDialog: LoadingDialog? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.also {
            it.lifecycleOwner = this
            it.setVariable(BR.viewModel, viewModel)
        }
        loadingDialog = LoadingDialog(this)
        setupViews()
        parseNotificationFromIntent(getNotificationFromIntent())
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    abstract fun setupViews()
    private fun getNotificationFromIntent(newIntent: Intent? = null) =
        (newIntent ?: intent)?.parcelable<NotificationModel>(FCM_NOTIFICATION_MODEL)

    abstract fun parseNotificationFromIntent(notification: NotificationModel? = null)
}
