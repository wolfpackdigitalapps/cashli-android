package com.wolfpackdigital.cashli.domain.entities.response

data class LastMembership(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val status: LastMembershipStatus,
    val amount: String,
    val isMembershipActive: Boolean
)