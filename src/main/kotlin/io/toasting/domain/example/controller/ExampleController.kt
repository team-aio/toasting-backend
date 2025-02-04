package io.toasting.domain.example.controller

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.example.controller.request.SaveExampleRequest
import io.toasting.domain.example.controller.response.GetExampleResponse
import io.toasting.domain.example.exception.ExampleHandler
import io.toasting.domain.example.repository.ExampleRepository
import io.toasting.global.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/v1/example")
class ExampleController(
    private val exampleRepository: ExampleRepository,
) {
    @PostMapping("/save")
    fun successTest(
        @RequestBody @Valid saveExampleRequest: SaveExampleRequest,
    ): ApiResponse<Unit> {
        val example = saveExampleRequest.toEntity()
        exampleRepository.save(example)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/find/{exampleId}")
    fun findTest(
        @PathVariable exampleId: Long,
    ): ApiResponse<GetExampleResponse> {
        val example =
            exampleRepository
                .findById(exampleId)
                .getOrNull()

        if (example == null) {
            throw ExampleHandler(ErrorStatus.EXAMPLE_NOT_FOUND)
        }

        return ApiResponse.onSuccess(
            GetExampleResponse(
                name = example.name,
                number = example.number,
            ),
        )
    }
}
