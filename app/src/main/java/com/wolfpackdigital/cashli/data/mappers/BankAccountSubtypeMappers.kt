package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.BankAccountSubtypeDto
import com.wolfpackdigital.cashli.domain.entities.enums.BankAccountSubtype
import com.wolfpackdigital.cashli.shared.base.Mapper

class BankAccountSubtypeDtoToBankAccountSubtypeMapper :
    Mapper<BankAccountSubtypeDto, BankAccountSubtype> {
    override fun map(input: BankAccountSubtypeDto): BankAccountSubtype {
        return when (input) {
            BankAccountSubtypeDto.CHECKING -> BankAccountSubtype.CHECKING
            BankAccountSubtypeDto.DEPOSITORY -> BankAccountSubtype.DEPOSITORY
            BankAccountSubtypeDto.SAVINGS -> BankAccountSubtype.SAVINGS
        }
    }
}

class BankAccountSubtypeToBankAccountSubtypeDtoMapper :
    Mapper<BankAccountSubtype, BankAccountSubtypeDto> {
    override fun map(input: BankAccountSubtype): BankAccountSubtypeDto {
        return when (input) {
            BankAccountSubtype.CHECKING -> BankAccountSubtypeDto.CHECKING
            BankAccountSubtype.DEPOSITORY -> BankAccountSubtypeDto.DEPOSITORY
            BankAccountSubtype.SAVINGS -> BankAccountSubtypeDto.SAVINGS
        }
    }
}
