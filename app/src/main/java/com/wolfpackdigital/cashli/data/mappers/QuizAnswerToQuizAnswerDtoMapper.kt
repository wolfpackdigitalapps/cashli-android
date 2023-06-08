package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.QuizAnswerDto
import com.wolfpackdigital.cashli.domain.entities.requests.QuizAnswer
import com.wolfpackdigital.cashli.shared.base.Mapper

class QuizAnswerToQuizAnswerDtoMapper : Mapper<QuizAnswer, QuizAnswerDto> {
    override fun map(input: QuizAnswer): QuizAnswerDto {
        return QuizAnswerDto(
            questionId = input.questionId,
            answer = input.answer
        )
    }
}
