package com.wolfpackdigital.cashli.presentation.claimCash

import android.os.Bundle
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.dimensionResource
import androidx.recyclerview.widget.GridLayoutManager
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ClaimCashFragmentBinding
import com.wolfpackdigital.cashli.presentation.claimCash.adapter.DeliveryMethodAdapter
import com.wolfpackdigital.cashli.presentation.claimCash.adapter.DeliveryMethodItemDecoration
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.views.StyledSlider
import com.wolfpackdigital.cashli.shared.utils.views.StyledSliderUIState
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val COLUMN_COUNT = 2

class ClaimCashFragment : BaseFragment<ClaimCashFragmentBinding, ClaimCashViewModel>(
    layoutResource = R.layout.fr_claim_cash
) {

    override val viewModel by viewModel<ClaimCashViewModel>()

    private lateinit var paymentSheet: PaymentSheet

    private val adapter by lazy {
        DeliveryMethodAdapter(viewModel::onDeliveryMethodSelected)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
    }

    override fun setupViews() {
        setupSliderView()
        setupRv()
        setupObservers()
        setupPaymentSheet()
    }

    private fun setupObservers() {
        viewModel.deliveryMethods.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when (command) {
                is ClaimCashViewModel.Command.TransitionToStart -> transitionToStart()
            }
        }
    }

    private fun setupRv() {
        binding?.rvDeliveryMethod?.apply {
            adapter = this@ClaimCashFragment.adapter
            itemAnimator = null
            layoutManager = GridLayoutManager(this.context, COLUMN_COUNT).apply {
                addItemDecoration(
                    DeliveryMethodItemDecoration(
                        spanCount = COLUMN_COUNT,
                        spacing = context.resources.getDimensionPixelSize(R.dimen.dimen_12dp),
                        includeEdge = true,
                        headerNum = 0
                    )
                )
            }
        }
    }

    private fun transitionToStart() {
        binding?.mlCashAmount?.transitionToStart()
    }

    private fun setupSliderView() {
        binding?.cvCashAmount?.setContent {
            MaterialTheme {
                StyledSlider(
                    uiState = StyledSliderUIState.ClaimCashSliderUIState(
                        sliderValue = viewModel.amountPerc,
                        labelValue = viewModel.amount,
                        isInvertedColorScheme = false
                    ),
                    onAmountChanged = viewModel::setAmountPerc,
                    horizontalPadding = dimensionResource(id = R.dimen.dimen_20dp)
                )
            }
        }
        viewModel.deliveryMethods.observe(viewLifecycleOwner, adapter::submitList)
    }

    private fun setupPaymentSheet() {
        viewModel.customerConfig.observe(viewLifecycleOwner) { stripeCustomerConfig ->
            context?.let {
                PaymentConfiguration.init(it, stripeCustomerConfig.publishableKey)
                paymentSheet.presentWithSetupIntent(
                    stripeCustomerConfig.setupIntentClientSecret,
                    PaymentSheet.Configuration(
                        merchantDisplayName = it.getString(R.string.app_name),
                        primaryButtonLabel = it.getString(R.string.generic_continue),
                        customer = stripeCustomerConfig.customerConfig,
                    )
                )
            }
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {

            }
            is PaymentSheetResult.Failed -> {

            }
            is PaymentSheetResult.Completed -> {

            }
        }
    }
}
