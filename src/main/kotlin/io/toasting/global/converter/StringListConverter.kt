package io.toasting.global.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringListConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(attribute: List<Long>?): String? {
        return attribute?.joinToString(",")
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long>? =
        dbData
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?: emptyList()
}