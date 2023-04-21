package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguagesDto
import com.wolfpackdigital.cashli.domain.entities.enums.Languages
import com.wolfpackdigital.cashli.shared.base.Mapper

class LanguagesDtoToLanguagesMapper :
    Mapper<LanguagesDto, Languages> {
    override fun map(input: LanguagesDto): Languages {
        return when (input) {
            LanguagesDto.ENGLISH -> Languages.ENGLISH
            LanguagesDto.SPANISH -> Languages.SPANISH
            LanguagesDto.HAITI -> Languages.HAITI
        }
    }
}

class LanguagesToLanguagesDtoMapper :
    Mapper<Languages, LanguagesDto> {
    override fun map(input: Languages): LanguagesDto {
        return when (input) {
            Languages.ENGLISH -> LanguagesDto.ENGLISH
            Languages.SPANISH -> LanguagesDto.SPANISH
            Languages.HAITI -> LanguagesDto.HAITI
        }
    }
}
