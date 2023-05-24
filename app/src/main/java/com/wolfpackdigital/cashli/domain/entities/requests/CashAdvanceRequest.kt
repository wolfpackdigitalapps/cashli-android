package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod

data class CashAdvanceRequest(
    val amount: Float,
    val tip: Float,
    val transferChannel: DeliveryMethod
)