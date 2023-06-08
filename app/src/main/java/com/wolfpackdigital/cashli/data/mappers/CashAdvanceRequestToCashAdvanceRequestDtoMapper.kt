package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.CashAdvanceRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class CashAdvanceRequestToCashAdvanceRequestDtoMapper(
    private val deliveryMethodMapper: DeliveryMethodToDeliveryMethodDtoMapper,
    private val quizAnswerMapper: QuizAnswerToQuizAnswerDtoMapper
) : Mapper<CashAdvanceRequest, CashAdvanceRequestDto> {

    override fun map(input: CashAdvanceRequest) = CashAdvanceRequestDto(
        amount = input.amount,
        tip = input.tip,
        transferChannel = deliveryMethodMapper.map(input.transferChannel),
        quizAnswers = input.quizAnswers.map { quizAnswerMapper.map(it) }
    )
}
