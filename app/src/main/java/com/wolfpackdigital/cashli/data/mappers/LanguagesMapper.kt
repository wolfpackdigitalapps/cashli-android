package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguageDto
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.shared.base.Mapper

class LanguageDtoToLanguageMapper : Mapper<LanguageDto, Language> {
    override fun map(input: LanguageDto): Language {
        return when (input) {
            LanguageDto.ENGLISH -> Language.ENGLISH
            LanguageDto.SPANISH -> Language.SPANISH
            LanguageDto.HAITI -> Language.HAITI
        }
    }
}

class LanguageToLanguageDtoMapper : Mapper<Language, LanguageDto> {
    override fun map(input: Language): LanguageDto {
        return when (input) {
            Language.ENGLISH -> LanguageDto.ENGLISH
            Language.SPANISH -> LanguageDto.SPANISH
            Language.HAITI -> LanguageDto.HAITI
        }
    }
}
