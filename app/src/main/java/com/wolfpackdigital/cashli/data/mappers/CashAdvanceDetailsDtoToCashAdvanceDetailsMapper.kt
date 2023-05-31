package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.CashAdvanceDetailsDto
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails
import com.wolfpackdigital.cashli.shared.base.Mapper

class CashAdvanceDetailsDtoToCashAdvanceDetailsMapper(
    private val cashAdvanceStatusMapper: CashAdvanceStatusDtoToCashAdvanceStatusMapper
) : Mapper<CashAdvanceDetailsDto, CashAdvanceDetails> {

    override fun map(input: CashAdvanceDetailsDto) = CashAdvanceDetails(
        amount = input.amount,
        tip = input.tip,
        payoutFee = input.payoutFee,
        totalRepayable = input.totalRepayable,
        cashAdvanceStatus = cashAdvanceStatusMapper.map(input.cashAdvanceStatus),
        dueDate = input.dueDate,
        paidDate = input.paidDate,
    )
}
