package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.BankTransactionDto
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.shared.base.Mapper
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

class BankTransactionToBankTransactionDtoMapper :
    Mapper<BankTransaction, BankTransactionDto> {
    override fun map(input: BankTransaction): BankTransactionDto {
        return BankTransactionDto(
            transactionId = input.transactionId,
            date = input.date,
            merchantName = input.merchantName,
            amount = input.amount
        )
    }
}

class BankTransactionDtoToBankTransactionMapper :
    Mapper<BankTransactionDto, BankTransaction> {
    override fun map(input: BankTransactionDto): BankTransaction {
        return BankTransaction(
            transactionId = input.transactionId,
            date = input.date ?: EMPTY_STRING,
            merchantName = input.merchantName ?: EMPTY_STRING,
            amount = input.amount ?: EMPTY_STRING
        )
    }
}
