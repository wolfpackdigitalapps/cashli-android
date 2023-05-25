package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.DeliveryMethodDto
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.shared.base.Mapper

class DeliveryMethodToDeliveryMethodDtoMapper : Mapper<DeliveryMethod, DeliveryMethodDto> {
    override fun map(input: DeliveryMethod): DeliveryMethodDto {
        return when (input) {
            DeliveryMethod.REGULAR -> DeliveryMethodDto.STANDARD
            DeliveryMethod.EXPRESS_WITHIN_MINUTES -> DeliveryMethodDto.EXPRESS
            DeliveryMethod.EXPRESS_SEVERAL_HOURS -> DeliveryMethodDto.SAME_DAY
            DeliveryMethod.EXPRESS_WITHIN_20_HOURS -> DeliveryMethodDto.NEXT_DAY
        }
    }
}
