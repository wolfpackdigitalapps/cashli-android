package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.CompleteLinkBankAccountRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper :
    Mapper<CompleteLinkBankAccountRequest, CompleteLinkBankAccountRequestDto> {
    override fun map(input: CompleteLinkBankAccountRequest): CompleteLinkBankAccountRequestDto {
        return CompleteLinkBankAccountRequestDto(
            publicToken = input.publicToken,
            metadata = input.metadata
        )
    }
}

class CompleteLinkBankAccountRequestDtoToCompleteLinkBankAccountRequestMapper :
    Mapper<CompleteLinkBankAccountRequestDto, CompleteLinkBankAccountRequest> {
    override fun map(input: CompleteLinkBankAccountRequestDto): CompleteLinkBankAccountRequest {
        return CompleteLinkBankAccountRequest(
            publicToken = input.publicToken,
            metadata = input.metadata
        )
    }
}
