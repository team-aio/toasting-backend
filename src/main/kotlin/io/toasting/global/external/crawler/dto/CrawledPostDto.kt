package io.toasting.global.external.crawler.dto

data class CrawledPostDto(
    val link: String,
    val title: String,
    val content: String,
    val posted_at: String
) {
}