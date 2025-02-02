package io.toasting.domain.example.repository

import io.toasting.domain.example.entity.Example
import org.springframework.data.jpa.repository.JpaRepository

interface ExampleRepository : JpaRepository<Example, Long>
