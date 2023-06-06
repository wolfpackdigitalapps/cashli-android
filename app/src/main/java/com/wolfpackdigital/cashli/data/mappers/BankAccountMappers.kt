package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.BankAccountDto
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.shared.base.Mapper

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
            relinkableAt = input.relinkableAt,
            balanceRefreshedAt = input.balanceRefreshedAt
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
            relinkableAt = input.relinkableAt,
            balanceRefreshedAt = input.balanceRefreshedAt,
        )
    }
}
