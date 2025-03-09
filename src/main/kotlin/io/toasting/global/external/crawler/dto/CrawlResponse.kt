package io.toasting.global.external.crawler.dto

data class CrawlResponse(
    val contents: MutableList<CrawledPostDto>
)