package com.wolfpackdigital.cashli.domain.entities.claimCash

data class DeliveryMethodItem(
    val deliveryMethod: DeliveryMethod,
    val cost: String,
    var isSelected: Boolean
)
