@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.shared.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.wolfpackdigital.cashli.BR
import com.wolfpackdigital.cashli.shared.utils.extensions.hideSoftKeyboard
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.navigate
import com.wolfpackdigital.cashli.shared.utils.extensions.navigateById
import com.wolfpackdigital.cashli.shared.utils.extensions.openUrl
import com.wolfpackdigital.cashli.shared.utils.extensions.showDialer
import com.wolfpackdigital.cashli.shared.utils.extensions.showMessage
import com.wolfpackdigital.cashli.shared.utils.extensions.showPopupById
import com.wolfpackdigital.cashli.shared.utils.extensions.showSMSApp
import com.wolfpackdigital.cashli.shared.utils.views.LoadingDialog

abstract class BaseFragment<BINDING : ViewDataBinding, VIEW_MODEL : BaseViewModel>(
    @LayoutRes private val layoutResource: Int
) : Fragment() {

    // In the case of fragments, simply having the binding as a
    // lateinit can lead to memory leaks because it holds context
    protected var binding: BINDING? = null
        private set
    protected abstract val viewModel: VIEW_MODEL
    protected val loadingDialog: LoadingDialog?
        get() = (activity as? BaseActivity<*, *>)?.loadingDialog

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog?.apply { if (isShowing) dismiss() }
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        DataBindingUtil.inflate<BINDING>(
            layoutInflater,
            layoutResource,
            null,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.setVariable(BR.viewModel, viewModel)
            binding = it
        }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeLoadingDialog()
        observeBaseCommands()
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
                is BaseCommand.GoBack ->
                    if (navController?.popBackStack() == false) {
                        activity?.finish()
                    }

                is BaseCommand.GoBackTo -> navController?.popBackStack(
                    it.destinationId,
                    it.inclusive
                )

                is BaseCommand.ForceCloseKeyboard ->
                    binding?.root?.findFocus()?.let { viewWithFocus ->
                        hideSoftKeyboard(viewWithFocus)
                        viewWithFocus.clearFocus()
                    }

                is BaseCommand.OpenSMSApp -> activity?.showSMSApp(it.phoneNumber)
                is BaseCommand.OpenPhoneApp -> activity?.showDialer(it.phoneNumber)
            }
        }
    }

    private fun observeLoadingDialog() {
        viewModel.apiCallsCount.observe(viewLifecycleOwner) {
            if (it == 0)
                loadingDialog?.dismiss()
            else
                loadingDialog?.show()
        }
    }

    abstract fun setupViews()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /* Since the binding is nullable, in some cases,
    like after receiving a response from an api call,
    the fragment might be destroyed so we need to check it's
    nullability, in cases where we are sure it is not null (onCreateView)
    we can use this helper function to avoid null checks */
    protected fun requireBinding(): BINDING = binding
        ?: throw NullPointerException("View was destroyed and the binding is null")
}

abstract class BaseAndroidFragment<BINDING : ViewDataBinding, VIEW_MODEL : BaseAndroidViewModel>(
    @LayoutRes private val layoutResource: Int,
    private val enterTrans: android.transition.Transition = android.transition.Fade(),
    private val exitTrans: android.transition.Transition = android.transition.Fade()
) : Fragment() {

    // In the case of fragments, simply having the binding as a
    // lateinit can lead to memory leaks because it holds context
    protected var binding: BINDING? = null
        private set
    protected abstract val viewModel: VIEW_MODEL
    protected val loadingDialog: LoadingDialog?
        get() = (activity as? BaseActivity<*, *>)?.loadingDialog

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = enterTrans
        exitTransition = exitTrans
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        DataBindingUtil.inflate<BINDING>(
            layoutInflater,
            layoutResource,
            null,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.setVariable(BR.viewModel, viewModel)
            binding = it
        }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeLoadingDialog()
    }

    private fun observeLoadingDialog() {
        viewModel.apiCallsCount.observe(viewLifecycleOwner) {
            if (it == 0)
                loadingDialog?.dismiss()
            else
                loadingDialog?.show()
        }
    }

    abstract fun setupViews()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /* Since the binding is nullable, in some cases,
    like after receiving a response from an api call,
    the fragment might be destroyed so we need to check it's
    nullability, in cases where we are sure it is not null (onCreateView)
    we can use this helper function to avoid null checks */
    protected fun requireBinding(): BINDING = binding
        ?: throw NullPointerException("View was destroyed and the binding is null")
}
