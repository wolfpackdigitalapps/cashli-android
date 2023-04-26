package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountMetadataRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountBalanceRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountInfoRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountLocalizedBalanceRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountSubtypeRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountTypeRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountVerificationStatusRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkInstitutionRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountMetadataRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class LinkAccountMetadataRequestToLinkAccountMetadataRequestDtoMapper :
    Mapper<LinkAccountMetadataRequest, LinkAccountMetadataRequestDto> {
    override fun map(input: LinkAccountMetadataRequest): LinkAccountMetadataRequestDto {
        return LinkAccountMetadataRequestDto(
            accounts = input.accounts?.map { linkAccount ->
                LinkAccountInfoRequestDto(
                    id = linkAccount.id,
                    name = linkAccount.name,
                    mask = linkAccount.mask,
                    subtype = LinkAccountSubtypeRequestDto(
                        name = linkAccount.subtype?.name,
                        type = LinkAccountTypeRequestDto(
                            name = linkAccount.subtype?.type?.name
                        )
                    ),
                    verificationStatus = LinkAccountVerificationStatusRequestDto(
                        name = linkAccount.verificationStatus?.name
                    ),
                    balance = LinkAccountBalanceRequestDto(
                        available = linkAccount.balance?.available,
                        currency = linkAccount.balance?.currency,
                        current = linkAccount.balance?.current,
                        localized = LinkAccountLocalizedBalanceRequestDto(
                            available = linkAccount.balance?.localized?.available,
                            current = linkAccount.balance?.localized?.current
                        )
                    )
                )
            },
            institution = LinkInstitutionRequestDto(
                id = input.institution?.id,
                name = input.institution?.name
            ),
            linkSessionId = input.linkSessionId,
            metadataJson = input.metadataJson
        )
    }
}
