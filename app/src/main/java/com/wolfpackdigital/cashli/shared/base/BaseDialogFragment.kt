package com.wolfpackdigital.cashli.shared.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.wolfpackdigital.cashli.BR
import com.wolfpackdigital.cashli.shared.utils.extensions.navigate
import com.wolfpackdigital.cashli.shared.utils.extensions.navigateById
import com.wolfpackdigital.cashli.shared.utils.extensions.openUrl
import com.wolfpackdigital.cashli.shared.utils.extensions.showMessage
import com.wolfpackdigital.cashli.shared.utils.extensions.showPopupById
import com.wolfpackdigital.cashli.shared.utils.extensions.showSMSApp
import com.wolfpackdigital.cashli.shared.utils.views.LoadingDialog

abstract class BaseDialogFragment<BINDING : ViewDataBinding, VIEW_MODEL : BaseViewModel>(
    @LayoutRes private val layoutRes: Int
) : DialogFragment() {

    protected var dialogBinding: BINDING? = null
    protected abstract val viewModel: VIEW_MODEL

    protected val loadingDialog: LoadingDialog?
        get() = (activity as? BaseActivity<*, *>)?.loadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog?.apply { if (isShowing) dismiss() }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<BINDING>(
        inflater,
        layoutRes,
        null,
        false
    ).apply {
        lifecycleOwner = viewLifecycleOwner
        setVariable(BR.viewModel, viewModel)
        dialogBinding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setIsCancelable()
        observeLoadingDialog()
        observeBaseCommands()
    }

    private fun observeLoadingDialog() {
        viewModel.apiCallsCount.observe(viewLifecycleOwner) {
            if (it == 0)
                loadingDialog?.dismiss()
            else
                loadingDialog?.show()
        }
    }

    private fun observeBaseCommands() {
        viewModel.baseCmd.observe(viewLifecycleOwner) {
            when (it) {
                is BaseCommand.OpenUrl -> context?.openUrl(it.urlResource)
                is BaseCommand.ShowToast -> showMessage(it.message)
                is BaseCommand.ShowSnackbar -> showMessage(
                    it.message,
                    isToast = false
                )

                is BaseCommand.PerformNavDeepLink -> navigate(it)
                is BaseCommand.PerformNavAction -> navigate(it)
                is BaseCommand.PerformNavById -> navigateById(it)
                is BaseCommand.ShowPopupById -> showPopupById(it.popupConfig)
                is BaseCommand.GoBack -> dismiss()
                is BaseCommand.OpenSMSApp -> activity?.showSMSApp(it.phoneNumber)
                else -> {}
            }
        }
    }

    abstract fun setupViews()

    open fun setIsCancelable(isDialogCancelable: Boolean = false) {
        isCancelable = isDialogCancelable
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        dialogBinding = null
    }
}
