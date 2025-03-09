package io.toasting.global.external.crawler

import io.toasting.global.external.crawler.dto.CrawlResponse
import io.toasting.global.external.crawler.dto.CrawledPostDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class PostCrawler(
    @Value("\${crawler.base-url}") private val baseUrl: String,
    @Value("\${crawler.tistory-uri}") private val tistoryUri: String,
    @Value("\${crawler.velog-uri}") private val velogUri: String,
) {
    fun crawlPost(id: String, sourceType: String): MutableList<CrawledPostDto> {
        return WebClient.create(baseUrl).get()
            .uri(determineUri(id, sourceType))
            .retrieve()
            .bodyToMono(CrawlResponse::class.java)
            .map { it.contents.toMutableList() }
            .block() ?: mutableListOf()
    }

    fun determineUri(id: String, sourceType: String) : String {
        if (sourceType.equals("tistory")) {
            return tistoryUri + id
        } else if (sourceType.equals("velog")){
            return velogUri + id
        } else {
            throw IllegalArgumentException("Unsupported sourceType: $sourceType")
        }
    }
}