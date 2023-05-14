package com.wolfpackdigital.cashli.presentation.claimCash

import com.stripe.android.paymentsheet.PaymentSheet

class StripeCustomerConfig(
    val customerConfig: PaymentSheet.CustomerConfiguration,
    val setupIntentClientSecret: String,
    val publishableKey: String
)