package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.BankAccountDto
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.shared.base.Mapper
import java.time.LocalDateTime

@Suppress("MaxLineLength")
class BankAccountToBankAccountDtoMapper(
    private val bankAccountSubtypeMapper: BankAccountSubtypeToBankAccountSubtypeDtoMapper,
) : Mapper<BankAccount, BankAccountDto> {
    override fun map(input: BankAccount): BankAccountDto {
        return BankAccountDto(
            institutionName = input.institutionName,
            name = input.name,
            accountSubtype = bankAccountSubtypeMapper.map(input.accountSubtype),
            accountNumberMask = input.accountNumberMask,
            balance = input.balance,
            timestamp = input.timestamp,
            relinkableAt = input.relinkableAt,
        )
    }
}

@Suppress("MaxLineLength")
class BankAccountDtoToBankAccountMapper(
    private val bankAccountSubtypeMapper: BankAccountSubtypeDtoToBankAccountSubtypeMapper,
) : Mapper<BankAccountDto, BankAccount> {
    override fun map(input: BankAccountDto): BankAccount {
        return BankAccount(
            institutionName = input.institutionName,
            name = input.name,
            accountSubtype = bankAccountSubtypeMapper.map(input.accountSubtype),
            accountNumberMask = input.accountNumberMask,
            balance = input.balance,
            timestamp = input.timestamp ?: LocalDateTime.now().toString(),
            relinkableAt = input.relinkableAt,
        )
    }
}
