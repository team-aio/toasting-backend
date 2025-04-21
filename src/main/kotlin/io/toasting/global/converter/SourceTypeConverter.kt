package io.toasting.global.converter

import io.toasting.domain.post.vo.SourceType
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class SourceTypeConverter : Converter<String, SourceType> {
    override fun convert(source: String): SourceType {
        return SourceType.from(source)
    }
}