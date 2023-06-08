package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.TransferFeesDto
import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees
import com.wolfpackdigital.cashli.shared.base.Mapper

class TransferFeesDtoToTransferFeesMapper : Mapper<TransferFeesDto, TransferFees> {

    override fun map(input: TransferFeesDto) = TransferFees(
        id = input.id,
        lowerLimitFormatted = input.lowerLimitFormatted,
        lowerLimit = input.lowerLimit,
        upperLimitFormatted = input.upperLimitFormatted,
        upperLimit = input.upperLimit,
        regularFee = input.regularFee,
        nextDayFee = input.nextDayFee,
        sameDayFee = input.sameDayFee,
        instantFee = input.instantFee
    )
}
