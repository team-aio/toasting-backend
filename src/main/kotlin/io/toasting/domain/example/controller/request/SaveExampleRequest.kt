package io.toasting.domain.example.controller.request

import io.toasting.domain.example.entity.Example

class SaveExampleRequest(
    val name: String,
    val number: Long,
) {
    fun toEntity(): Example = Example(name = name, number = number)
}
